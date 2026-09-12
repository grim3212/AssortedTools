package com.grim3212.assorted.tools.gametest;

import com.grim3212.assorted.lib.events.AnvilUpdatedEvent;
import com.grim3212.assorted.tools.common.enchantment.ToolsEnchantments;
import com.grim3212.assorted.tools.common.handlers.ChickenSuitConversionHandler;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.tools.gametest.ToolsTestSupport.*;

/**
 * Enchantments: what each tool takes, what is obtainable, the spear vetoes and the chicken suit anvil conversion.
 */
final class EnchantmentTests {

    private EnchantmentTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("tools_take_the_right_enchantments", EnchantmentTests::toolsTakeTheRightEnchantments);
        out.accept("tools_enchantments_are_obtainable", EnchantmentTests::toolsEnchantmentsAreObtainable);
        out.accept("spears_are_never_offered_riptide_or_channeling", EnchantmentTests::spearsAreNeverOfferedRiptideOrChanneling);
        out.accept("spear_anvil_rejects_riptide_and_channeling", EnchantmentTests::spearAnvilRejectsRiptideAndChanneling);
        out.accept("chicken_suit_converts_armour_in_an_anvil", EnchantmentTests::chickenSuitConvertsArmourInAnAnvil);
    }

    /**
     * Every tool, weapon and armour piece is in its {@code #minecraft:enchantable/*} tags. They are
     * opt-in, so a missing item is silently unenchantable. Every gap is reported at once.
     */
    private static void toolsTakeTheRightEnchantments(GameTestHelper helper) {
        List<String> missing = new ArrayList<>();

        expectMiningTool(missing, ToolsItems.WOOD_HAMMER.get(), ToolsItems.STONE_HAMMER.get(), ToolsItems.GOLD_HAMMER.get(), ToolsItems.IRON_HAMMER.get(), ToolsItems.DIAMOND_HAMMER.get(), ToolsItems.NETHERITE_HAMMER.get());
        expectMiningTool(missing, ToolsItems.WOODEN_MULTITOOL.get(), ToolsItems.STONE_MULTITOOL.get(), ToolsItems.GOLDEN_MULTITOOL.get(), ToolsItems.IRON_MULTITOOL.get(), ToolsItems.DIAMOND_MULTITOOL.get(), ToolsItems.NETHERITE_MULTITOOL.get());
        expectMeleeWeapon(missing, ToolsItems.WOOD_HAMMER.get(), ToolsItems.STONE_HAMMER.get(), ToolsItems.GOLD_HAMMER.get(), ToolsItems.IRON_HAMMER.get(), ToolsItems.DIAMOND_HAMMER.get(), ToolsItems.NETHERITE_HAMMER.get());
        expectMeleeWeapon(missing, ToolsItems.WOODEN_MULTITOOL.get(), ToolsItems.STONE_MULTITOOL.get(), ToolsItems.GOLDEN_MULTITOOL.get(), ToolsItems.IRON_MULTITOOL.get(), ToolsItems.DIAMOND_MULTITOOL.get(), ToolsItems.NETHERITE_MULTITOOL.get());
        expectMeleeWeapon(missing, ToolsItems.ULTIMATE_FIST.get());
        expectDurable(missing, ToolsItems.WOOD_THROWING_SPEAR.get(), ToolsItems.STONE_THROWING_SPEAR.get(), ToolsItems.GOLD_THROWING_SPEAR.get(), ToolsItems.IRON_THROWING_SPEAR.get(), ToolsItems.DIAMOND_THROWING_SPEAR.get(), ToolsItems.NETHERITE_THROWING_SPEAR.get());
        expectDurable(missing, ToolsItems.WOOD_SHEARS.get(), ToolsItems.STONE_SHEARS.get(), ToolsItems.GOLD_SHEARS.get(), ToolsItems.DIAMOND_SHEARS.get(), ToolsItems.NETHERITE_SHEARS.get());
        expectDurable(missing, ToolsItems.WOOD_BUCKET.get(), ToolsItems.STONE_BUCKET.get(), ToolsItems.GOLD_BUCKET.get(), ToolsItems.DIAMOND_BUCKET.get(), ToolsItems.NETHERITE_BUCKET.get());
        expectDurable(missing, ToolsItems.BUILDING_WAND.get(), ToolsItems.BREAKING_WAND.get(), ToolsItems.MINING_WAND.get(), ToolsItems.REINFORCED_BUILDING_WAND.get(), ToolsItems.REINFORCED_BREAKING_WAND.get(), ToolsItems.REINFORCED_MINING_WAND.get());
        expectArmour(missing, ItemTags.HEAD_ARMOR_ENCHANTABLE, ToolsItems.CHICKEN_SUIT_HELMET.get());
        expectArmour(missing, ItemTags.CHEST_ARMOR_ENCHANTABLE, ToolsItems.CHICKEN_SUIT_CHESTPLATE.get());
        expectArmour(missing, ItemTags.LEG_ARMOR_ENCHANTABLE, ToolsItems.CHICKEN_SUIT_LEGGINGS.get());
        expectArmour(missing, ItemTags.FOOT_ARMOR_ENCHANTABLE, ToolsItems.CHICKEN_SUIT_BOOTS.get());

        // The mod's own two tags, which are what the six modded enchantments name as their
        // supported items - the replacement for the deleted canEnchant overrides.
        expect(missing, ToolsEnchantments.SPEAR_ENCHANTABLE, ToolsItems.WOOD_THROWING_SPEAR.get(), ToolsItems.NETHERITE_THROWING_SPEAR.get());
        expect(missing, ToolsEnchantments.SHEARS_ENCHANTABLE, Items.SHEARS, ToolsItems.DIAMOND_SHEARS.get());

        for (ToolsItems.MaterialGroup group : ToolsItems.MATERIAL_GROUPS.values()) {
            expectMiningTool(missing, group.PICKAXE.get(), group.SHOVEL.get(), group.AXE.get(), group.HOE.get(), group.HAMMER.get(), group.MULTITOOL.get());
            expectMeleeWeapon(missing, group.SWORD.get(), group.AXE.get(), group.HAMMER.get(), group.MULTITOOL.get());
            expectArmour(missing, ItemTags.HEAD_ARMOR_ENCHANTABLE, group.HELMET.get());
            expectArmour(missing, ItemTags.CHEST_ARMOR_ENCHANTABLE, group.CHESTPLATE.get());
            expectArmour(missing, ItemTags.LEG_ARMOR_ENCHANTABLE, group.LEGGINGS.get());
            expectArmour(missing, ItemTags.FOOT_ARMOR_ENCHANTABLE, group.BOOTS.get());
            expectDurable(missing, group.THROWING_SPEAR.get(), group.SHEARS.get(), group.BUCKET.get());
            expect(missing, ToolsEnchantments.SPEAR_ENCHANTABLE, group.THROWING_SPEAR.get());
            expect(missing, ToolsEnchantments.SHEARS_ENCHANTABLE, group.SHEARS.get());
        }

        helper.assertTrue(missing.isEmpty(), missing.size() + " item/tag pairs are missing from the enchantable tags: " + String.join(", ", missing));
        helper.succeed();
    }

    /**
     * With the default config every part is on, so all six of this mod's enchantments must be
     * registered - their definitions are conditional on their part now - and in the three vanilla
     * tags that make an enchantment obtainable.
     */
    private static void toolsEnchantmentsAreObtainable(GameTestHelper helper) {
        Registry<Enchantment> registry = helper.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        List<String> missing = new ArrayList<>();

        for (ResourceKey<Enchantment> key : List.of(ToolsEnchantments.CHICKEN_JUMP, ToolsEnchantments.BOUNCINESS, ToolsEnchantments.CONDUCTIVE, ToolsEnchantments.FLAMMABLE, ToolsEnchantments.UNSTABLE, ToolsEnchantments.CORAL_CUTTER)) {
            Optional<Holder.Reference<Enchantment>> enchantment = registry.get(key);
            if (enchantment.isEmpty()) {
                missing.add(key.identifier() + " (not registered)");
                continue;
            }

            for (TagKey<Enchantment> tag : List.of(EnchantmentTags.IN_ENCHANTING_TABLE, EnchantmentTags.TRADEABLE, EnchantmentTags.ON_RANDOM_LOOT)) {
                if (!enchantment.get().is(tag)) {
                    missing.add(key.identifier() + " not in #" + tag.location());
                }
            }
        }

        helper.assertTrue(missing.isEmpty(), "enchantments missing or not obtainable: " + String.join(", ", missing));
        helper.succeed();
    }

    /**
     * Asks the way the enchanting table does, so each loader's wiring is under test: NeoForge goes
     * through {@code isPrimaryItemFor}, Fabric through {@code ALLOW_ENCHANTING}. A cost of 30 falls
     * in every trident and spear enchantment's window; the vanilla trident is checked so the veto
     * cannot leak onto it.
     */
    private static void spearsAreNeverOfferedRiptideOrChanneling(GameTestHelper helper) {
        Registry<Enchantment> registry = helper.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        Set<ResourceKey<Enchantment>> spear = offeredAtTable(registry, new ItemStack(ToolsItems.IRON_THROWING_SPEAR.get()));
        Set<ResourceKey<Enchantment>> trident = offeredAtTable(registry, new ItemStack(Items.TRIDENT));

        helper.assertFalse(spear.contains(Enchantments.RIPTIDE) || spear.contains(Enchantments.CHANNELING), "a spear was offered riptide or channeling: " + spear);
        helper.assertTrue(spear.containsAll(List.of(Enchantments.LOYALTY, Enchantments.IMPALING)), "a spear was not offered loyalty and impaling: " + spear);
        helper.assertTrue(spear.containsAll(List.of(ToolsEnchantments.BOUNCINESS, ToolsEnchantments.CONDUCTIVE, ToolsEnchantments.FLAMMABLE, ToolsEnchantments.UNSTABLE)), "a spear was not offered all four spear enchantments: " + spear);
        helper.assertTrue(trident.containsAll(List.of(Enchantments.RIPTIDE, Enchantments.CHANNELING)), "the vanilla trident lost riptide or channeling: " + trident);
        helper.succeed();
    }

    /**
     * The anvil path, through a real {@link AnvilMenu} so each loader's hook runs: Riptide and
     * Channeling books do not combine with a spear, Loyalty does, and a vanilla trident still takes
     * Riptide.
     */
    private static void spearAnvilRejectsRiptideAndChanneling(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper, ItemStack.EMPTY);
        Registry<Enchantment> registry = helper.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> riptide = registry.getOrThrow(Enchantments.RIPTIDE);
        Holder<Enchantment> loyalty = registry.getOrThrow(Enchantments.LOYALTY);

        helper.assertTrue(combine(player, new ItemStack(ToolsItems.IRON_THROWING_SPEAR.get()), riptide).isEmpty(), "riptide went onto a spear at an anvil");
        helper.assertTrue(combine(player, new ItemStack(ToolsItems.IRON_THROWING_SPEAR.get()), registry.getOrThrow(Enchantments.CHANNELING)).isEmpty(), "channeling went onto a spear at an anvil");

        ItemStack loyalSpear = combine(player, new ItemStack(ToolsItems.IRON_THROWING_SPEAR.get()), loyalty);
        helper.assertValueEqual(loyalSpear.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY).getLevel(loyalty), 1, "loyalty level on a spear from an anvil");

        ItemStack riptideTrident = combine(player, new ItemStack(Items.TRIDENT), riptide);
        helper.assertValueEqual(riptideTrident.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY).getLevel(riptide), 1, "riptide level on a vanilla trident from an anvil");
        helper.succeed();
    }

    /**
     * A chicken suit piece put in an anvil against matching armour hands back that armour with
     * Chicken Jump on it. The handler is called with the library event directly, the same way the
     * milking test is, because the two loaders raise it from different call sites.
     */
    private static void chickenSuitConvertsArmourInAnAnvil(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper, ItemStack.EMPTY);
        stand(helper, player, new BlockPos(4, 1, 4));

        AnvilUpdatedEvent matching = new AnvilUpdatedEvent(new ItemStack(Items.IRON_CHESTPLATE), new ItemStack(ToolsItems.CHICKEN_SUIT_CHESTPLATE.get()), "", 0, player);
        ChickenSuitConversionHandler.anvilUpdateEvent(matching);

        ItemStack output = matching.getOutput();
        helper.assertTrue(output.is(Items.IRON_CHESTPLATE), "converting an iron chestplate gave back " + output);
        // Read off the stack's own component rather than through EnchantmentHelper, whose
        // level lookup is deprecated; this is the same accessor the rest of the mod uses.
        helper.assertValueEqual(ToolsEnchantments.getLevel(output, ToolsEnchantments.CHICKEN_JUMP), 1, "chicken jump level on the converted armour");
        helper.assertValueEqual(matching.getMaterialCost(), 1, "chicken suit pieces consumed");
        helper.assertValueEqual(matching.getCost(), 5, "level cost of converting a chestplate");

        // A helmet against a chestplate is the wrong slot and has to be left alone.
        AnvilUpdatedEvent mismatched = new AnvilUpdatedEvent(new ItemStack(Items.IRON_HELMET), new ItemStack(ToolsItems.CHICKEN_SUIT_CHESTPLATE.get()), "", 0, player);
        ChickenSuitConversionHandler.anvilUpdateEvent(mismatched);
        helper.assertTrue(mismatched.getOutput().isEmpty(), "a chicken suit chestplate converted a helmet");

        // So does anything that is not armour at all.
        AnvilUpdatedEvent notArmour = new AnvilUpdatedEvent(new ItemStack(Items.DIAMOND_PICKAXE), new ItemStack(ToolsItems.CHICKEN_SUIT_HELMET.get()), "", 0, player);
        ChickenSuitConversionHandler.anvilUpdateEvent(notArmour);
        helper.assertTrue(notArmour.getOutput().isEmpty(), "a chicken suit helmet converted a pickaxe");

        helper.succeed();
    }
}
