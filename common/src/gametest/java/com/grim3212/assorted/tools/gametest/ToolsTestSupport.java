package com.grim3212.assorted.tools.gametest;

import com.grim3212.assorted.tools.common.item.CapturedEntity;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

import static com.grim3212.assorted.lib.test.TestSupport.craft;
import static com.grim3212.assorted.lib.test.TestSupport.hover;
import static com.grim3212.assorted.lib.test.TestSupport.survivalPlayer;

/**
 * Helpers, constants and fixtures shared by AssortedTools' gametest classes, which import them
 * statically, alongside AssortedLib's {@code TestSupport}.
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
     * Asserts a grid crafts {@code expected} through the recipe manager, so a recipe dropped by a
     * failing load condition fails here.
     */
    static void assertCrafts(GameTestHelper helper, int width, int height, List<ItemStack> grid, Item expected) {
        Identifier id = BuiltInRegistries.ITEM.getKey(expected);
        ItemStack result = craft(helper, CraftingInput.of(width, height, grid), "the pattern for " + id);
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
     * Hovers the player two blocks above {@code rel} looking straight down and uses the held item.
     * This is the only way to drive {@code BetterBucketItem#use}: it decides everything from a POV
     * raytrace rather than from a position it is handed.
     */
    static void useLookingDownAt(GameTestHelper helper, ServerPlayer player, BlockPos rel) {
        hover(helper, player, Vec3.atCenterOf(rel).add(0.0D, 2.0D, 0.0D), 90.0F);
        player.getItemInHand(InteractionHand.MAIN_HAND).getItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
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
}
