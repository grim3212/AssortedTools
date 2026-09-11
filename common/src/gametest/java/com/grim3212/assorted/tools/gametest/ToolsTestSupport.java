package com.grim3212.assorted.tools.gametest;

import com.grim3212.assorted.tools.common.item.CapturedEntity;
import com.grim3212.assorted.lib.platform.Services;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.core.component.DataComponentType;
import java.util.ArrayList;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.common.entity.BetterSpearEntity;
import com.grim3212.assorted.tools.common.entity.BoomerangEntity;
import com.grim3212.assorted.tools.common.entity.PokeballEntity;
import com.grim3212.assorted.tools.common.entity.ToolsEntities;
import com.grim3212.assorted.tools.common.item.BetterBucketItem;
import com.grim3212.assorted.tools.common.item.BetterSpearItem;
import com.grim3212.assorted.tools.common.item.BoomerangItem;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

/**
 * Helpers, constants and fixtures shared by AssortedTools' gametest classes, which import them statically.
 */
final class ToolsTestSupport {

    private ToolsTestSupport() {
    }

    static Set<ResourceKey<Enchantment>> offeredAtTable(Registry<Enchantment> registry, ItemStack stack) {
        return EnchantmentHelper.getAvailableEnchantmentResults(30, stack, registry.getOrThrow(EnchantmentTags.IN_ENCHANTING_TABLE).stream())
                .stream().map(instance -> instance.enchantment().unwrapKey().orElseThrow()).collect(Collectors.toSet());
    }

    static ItemStack combine(ServerPlayer player, ItemStack left, Holder<Enchantment> enchantment) {
        ItemEnchantments.Mutable stored = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        stored.set(enchantment, 1);
        ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
        book.set(DataComponents.STORED_ENCHANTMENTS, stored.toImmutable());

        AnvilMenu menu = new AnvilMenu(0, player.getInventory());
        menu.getSlot(AnvilMenu.INPUT_SLOT).set(left);
        menu.getSlot(AnvilMenu.ADDITIONAL_SLOT).set(book);
        menu.createResult();
        return menu.getSlot(AnvilMenu.RESULT_SLOT).getItem();
    }

    // -----------------------------------------------------------------------------------------
    // Helpers for the checks above
    // -----------------------------------------------------------------------------------------

    /** The first item in {@code tag}, or empty when nothing in the game populates it. */
    static ItemStack firstOf(TagKey<Item> tag) {
        for (Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
            return new ItemStack(holder.value());
        }
        return ItemStack.EMPTY;
    }

    /**
     * Asserts that a grid of ingredients resolves to a recipe producing {@code expected}. Going
     * through the recipe manager is the whole point: it is what actually loaded the json, its
     * conditions included, so a recipe dropped by a failing condition shows up here and nowhere
     * else.
     */
    static void assertCrafts(GameTestHelper helper, int width, int height, List<ItemStack> grid, Item expected) {
        Identifier id = BuiltInRegistries.ITEM.getKey(expected);
        CraftingInput input = CraftingInput.of(width, height, grid);
        Optional<RecipeHolder<CraftingRecipe>> found = helper.getLevel().recipeAccess().getRecipeFor(RecipeType.CRAFTING, input, helper.getLevel());

        helper.assertTrue(found.isPresent(), "no crafting recipe matched the pattern for " + id);

        ItemStack result = found.get().value().assemble(input);
        helper.assertTrue(result.is(expected), "the pattern for " + id + " crafted " + BuiltInRegistries.ITEM.getKey(result.getItem()) + " instead");
    }

    static void assertSpeed(GameTestHelper helper, Item tool, BlockState state, float expected, String what) {
        helper.assertValueEqual(new ItemStack(tool).getDestroySpeed(state), expected, what);
    }

    static void expect(List<String> missing, TagKey<Item> tag, Item... items) {
        for (Item item : items) {
            if (!new ItemStack(item).is(tag)) {
                missing.add(BuiltInRegistries.ITEM.getKey(item) + " not in #" + tag.location());
            }
        }
    }

    static void expectDurable(List<String> missing, Item... items) {
        expect(missing, ItemTags.DURABILITY_ENCHANTABLE, items);
        expect(missing, ItemTags.VANISHING_ENCHANTABLE, items);
    }

    static void expectMiningTool(List<String> missing, Item... items) {
        expect(missing, ItemTags.MINING_ENCHANTABLE, items);
        expect(missing, ItemTags.MINING_LOOT_ENCHANTABLE, items);
        expectDurable(missing, items);
    }

    static void expectMeleeWeapon(List<String> missing, Item... items) {
        expect(missing, ItemTags.WEAPON_ENCHANTABLE, items);
        expect(missing, ItemTags.MELEE_WEAPON_ENCHANTABLE, items);
        expect(missing, ItemTags.SHARP_WEAPON_ENCHANTABLE, items);
        expect(missing, ItemTags.FIRE_ASPECT_ENCHANTABLE, items);
        expectDurable(missing, items);
    }

    static void expectArmour(List<String> missing, TagKey<Item> slotTag, Item... items) {
        expect(missing, slotTag, items);
        expect(missing, ItemTags.ARMOR_ENCHANTABLE, items);
        expect(missing, ItemTags.EQUIPPABLE_ENCHANTABLE, items);
        expectDurable(missing, items);
    }

    /**
     * Puts one set of armour on and reads the defence back off the player. Equipment attribute
     * modifiers are only collected during the entity's own tick, so the player is ticked once
     * between putting the set on and asking.
     */
    static void assertArmourValue(GameTestHelper helper, ServerPlayer player, String name, Item helmet, Item chestplate, Item leggings, Item boots, int expected) {
        player.setItemSlot(EquipmentSlot.HEAD, new ItemStack(helmet));
        player.setItemSlot(EquipmentSlot.CHEST, new ItemStack(chestplate));
        player.setItemSlot(EquipmentSlot.LEGS, new ItemStack(leggings));
        player.setItemSlot(EquipmentSlot.FEET, new ItemStack(boots));

        helper.assertTrue(player.getEquipmentSlotForItem(new ItemStack(helmet)) == EquipmentSlot.HEAD, name + " helmet does not belong in the head slot");
        helper.assertTrue(player.getEquipmentSlotForItem(new ItemStack(boots)) == EquipmentSlot.FEET, name + " boots do not belong in the feet slot");

        // doTick, not tick: ServerPlayer#tick does not call super, so the equipment sweep that
        // turns the armour's components into attribute modifiers only runs from doTick.
        player.doTick();

        helper.assertValueEqual(player.getArmorValue(), expected, "armour value while wearing a full set of " + name);
    }

    /** Throws whatever spear is in the main hand through the item's own charge and release. */
    static BetterSpearEntity throwHeldSpear(GameTestHelper helper, ServerPlayer player) {
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        BetterSpearItem spear = (BetterSpearItem) stack.getItem();

        player.startUsingItem(InteractionHand.MAIN_HAND);
        // releaseUsing counts backwards: twenty short of the full duration is a twenty tick charge,
        // well past the ten it insists on.
        boolean thrown = spear.releaseUsing(stack, helper.getLevel(), player, spear.getUseDuration(stack, player) - 20);
        helper.assertTrue(thrown, "the spear refused to be thrown");

        List<BetterSpearEntity> spears = helper.getEntities(ToolsEntities.BETTER_SPEAR.get());
        helper.assertValueEqual(spears.size(), 1, "spear entities in the world after one throw");
        return spears.get(0);
    }

    /**
     * Ticks one entity by hand up to {@code limit} times, stopping as soon as {@code done} holds.
     * Bounded on purpose: a test that waits on physics it does not drive is a test that hangs.
     */
    static boolean tickUntil(Entity entity, int limit, BooleanSupplier done) {
        for (int tick = 0; tick < limit; tick++) {
            if (done.getAsBoolean()) {
                return true;
            }
            entity.tick();
        }
        return done.getAsBoolean();
    }

    static void assertBoomerangReturns(GameTestHelper helper, BoomerangItem item) {
        Identifier id = BuiltInRegistries.ITEM.getKey(item);
        ServerPlayer player = survivalPlayer(helper, new ItemStack(item));
        hover(helper, player, new Vec3(4.5D, 2.0D, 4.5D), -90.0F);

        helper.assertTrue(player.getItemInHand(InteractionHand.MAIN_HAND).use(helper.getLevel(), player, InteractionHand.MAIN_HAND).consumesAction(), id + " refused to be thrown");
        helper.assertTrue(player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty(), id + " stayed in the hand after being thrown");

        List<BoomerangEntity> flying = helper.getLevel().getEntitiesOfClass(BoomerangEntity.class, player.getBoundingBox().inflate(4.0D));
        helper.assertValueEqual(flying.size(), 1, "boomerang entities in the world after throwing " + id);

        BoomerangEntity boomerang = flying.get(0);
        // Out for its configured range and back at half a block a tick; two hundred is generous.
        boolean returned = tickUntil(boomerang, 200, boomerang::isRemoved);

        helper.assertTrue(returned, id + " never came back to the thrower");
        helper.assertValueEqual(countInInventory(player, item), 1, id + " in the thrower's inventory after it returned");

        helper.killAllEntitiesOfClass(BoomerangEntity.class);
        helper.getLevel().getServer().getPlayerList().remove(player);
    }

    static int countInInventory(ServerPlayer player, Item item) {
        int count = 0;
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (stack.is(item)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    static void removeFromInventory(ServerPlayer player, Item item) {
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            if (player.getInventory().getItem(slot).is(item)) {
                player.getInventory().setItem(slot, ItemStack.EMPTY);
            }
        }
    }

    /** Puts the player at a spot in the air inside the test box, looking at the given pitch. */
    static void hover(GameTestHelper helper, ServerPlayer player, Vec3 relative, float xRot) {
        Vec3 at = helper.absoluteVec(relative);
        player.snapTo(at.x, at.y, at.z, 0.0F, xRot);
    }

    /**
     * The translation key behind a component. A dedicated server loads no mod language file, so a
     * rendered string would just be the key anyway - this asks for it directly instead.
     */
    static String translationKey(Component component) {
        return component.getContents() instanceof TranslatableContents translatable ? translatable.getKey() : component.getString();
    }

    static JsonObject readLang(GameTestHelper helper) {
        try (InputStream in = ToolsTestSupport.class.getResourceAsStream("/assets/" + Constants.MOD_ID + "/lang/en_us.json")) {
            helper.assertTrue(in != null, "en_us.json is not on the classpath");
            return JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException("could not read en_us.json", e);
        }
    }

    /** Whether a resource is on the classpath. The mod's own assets are, even on a headless server. */
    static boolean resourceExists(String path) {
        try (InputStream in = ToolsTestSupport.class.getResourceAsStream(path)) {
            return in != null;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * A real, fully joined survival player, because none of {@code GameTestHelper}'s three mock
     * player factories is usable here: {@code makeMockServerPlayerInLevel} hard codes
     * {@code gameMode()} to CREATIVE, and creative changes the answer of nearly everything under
     * test (a bucket does not fill, a hammer does not break, a cow cannot be milked, a wand costs
     * nothing); the other two hand back a player that was never placed, so its {@code connection}
     * is null and anything that messages it - a wand announcing its new mode - throws.
     * <p>
     * This is what the deprecated in-level factory does, minus that game mode override.
     */
    static ServerPlayer survivalPlayer(GameTestHelper helper, ItemStack held) {
        ServerLevel level = helper.getLevel();
        CommonListenerCookie cookie = CommonListenerCookie.createInitial(new GameProfile(UUID.randomUUID(), "assortedtools-test"), false);
        ServerPlayer player = new ServerPlayer(level.getServer(), level, cookie.gameProfile(), cookie.clientInformation());

        Connection connection = new Connection(PacketFlow.SERVERBOUND);
        new EmbeddedChannel(connection);
        level.getServer().getPlayerList().placeNewPlayer(connection, player, cookie);
        helper.runBeforeTestEnd(() -> level.getServer().getPlayerList().remove(player));

        player.setGameMode(GameType.SURVIVAL);
        helper.assertFalse(player.isCreative(), "the test player is in creative, which changes every path under test");
        player.setItemInHand(InteractionHand.MAIN_HAND, held);
        return player;
    }

    /** Stands the player on top of {@code rel}, which is inside reach of anything nearby. */
    static void stand(GameTestHelper helper, ServerPlayer player, BlockPos rel) {
        Vec3 on = helper.absoluteVec(Vec3.atBottomCenterOf(rel.above()));
        player.snapTo(on.x, on.y, on.z, 0.0F, 0.0F);
    }

    /**
     * Hovers the player two blocks above {@code rel} looking straight down and uses the held item.
     * This is the only way to drive {@code BetterBucketItem#use}: it decides everything from a POV
     * raytrace rather than from a position it is handed.
     */
    static void useLookingDownAt(GameTestHelper helper, ServerPlayer player, BlockPos rel) {
        Vec3 above = helper.absoluteVec(Vec3.atCenterOf(rel).add(0.0D, 2.0D, 0.0D));
        player.snapTo(above.x, above.y, above.z, 0.0F, 90.0F);
        player.getItemInHand(InteractionHand.MAIN_HAND).getItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
    }

    /** Right clicks the top face of {@code rel} with whatever is in the main hand. */
    static InteractionResult useOnTopOf(GameTestHelper helper, ServerPlayer player, BlockPos rel) {
        BlockPos pos = helper.absolutePos(rel);
        BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(pos).add(0.0D, 0.5D, 0.0D), Direction.UP, pos, false);
        return player.getItemInHand(InteractionHand.MAIN_HAND).useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, hit));
    }

    static void throwPokeball(GameTestHelper helper, ServerPlayer player, ItemStack ball, Vec3 at) {
        Vec3 from = helper.absoluteVec(new Vec3(4.5D, 1.8D, 1.5D));
        PokeballEntity thrown = new PokeballEntity(player, helper.getLevel(), ball);
        thrown.snapTo(from.x, from.y, from.z, 0.0F, 0.0F);

        Vec3 aim = at.subtract(from);
        thrown.shoot(aim.x, aim.y, aim.z, 1.0F, 0.0F);
        helper.getLevel().addFreshEntity(thrown);
    }

    static List<ItemStack> filledPokeballs(GameTestHelper helper) {
        return helper.getEntities(EntityTypes.ITEM).stream()
                .map(ItemEntity::getItem)
                .filter(stack -> stack.is(ToolsItems.POKEBALL.get()) && !CapturedEntity.of(stack).isEmpty())
                .toList();
    }

    /** The translation keys of the lines one component adds to a stack's tooltip, in order. */
    static <T extends TooltipProvider> List<String> tooltipKeys(GameTestHelper helper, ItemStack stack, DataComponentType<T> type) {
        List<String> keys = new ArrayList<>();
        stack.addToTooltip(type, Item.TooltipContext.of(helper.getLevel()), TooltipDisplay.DEFAULT, line -> keys.add(tooltipKey(line)), TooltipFlag.NORMAL);
        return keys;
    }

    /** The translation keys of a stack's whole tooltip, as the loader builds it. */
    static List<String> fullTooltipKeys(GameTestHelper helper, ItemStack stack) {
        return stack.getTooltipLines(Item.TooltipContext.of(helper.getLevel()), null, TooltipFlag.NORMAL).stream().map(ToolsTestSupport::tooltipKey).toList();
    }

    /** A line's translation key, or its text when it is not translatable. */
    static String tooltipKey(Component line) {
        return line.getContents() instanceof TranslatableContents translatable ? translatable.getKey() : line.getString();
    }

    /** NeoForge adds mod component tooltip lines on the server too; Fabric only on the client. */
    static boolean onNeoForge() {
        return "Forge".equals(Services.PLATFORM.getPlatformName());
    }
}
