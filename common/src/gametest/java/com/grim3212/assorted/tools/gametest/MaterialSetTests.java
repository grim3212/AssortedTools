package com.grim3212.assorted.tools.gametest;

import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.tools.ToolsCommonMod;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.grim3212.assorted.tools.config.ArmorMaterialConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.tools.gametest.ToolsTestSupport.*;

/**
 * Every material's tool and armour set: crafting, equipping and protecting.
 */
final class MaterialSetTests {

    private MaterialSetTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("every_material_tool_set_crafts", MaterialSetTests::everyMaterialToolSetCrafts);
        out.accept("every_material_armour_set_crafts", MaterialSetTests::everyMaterialArmourSetCrafts);
        out.accept("armour_equips_and_protects", MaterialSetTests::armourEquipsAndProtects);
    }

    /**
     * Every extra material's five tool recipes, asked of the recipe manager itself rather than
     * enumerated by hand, so a family added to the configuration is covered the day it is added.
     * <p>
     * A family whose material tag has nothing in it is skipped, not failed: the recipe carries an
     * {@code item_tag_populated} condition, so without a mod supplying the ingot the recipe is
     * legitimately never loaded. Copper, amethyst and emerald come from the loaders themselves, so
     * there is always something to check.
     */
    private static void everyMaterialToolSetCrafts(GameTestHelper helper) {
        ItemStack rod = firstOf(LibCommonTags.Items.RODS_WOODEN);
        helper.assertFalse(rod.isEmpty(), "c:rods/wooden is empty, so no tool recipe could match");

        final ItemStack no = ItemStack.EMPTY;
        int families = 0;

        for (ToolsItems.MaterialGroup group : ToolsItems.MATERIAL_GROUPS.values()) {
            ItemStack x = firstOf(group.material);
            if (x.isEmpty()) {
                continue;
            }

            assertCrafts(helper, 3, 3, List.of(x, x, x, no, rod, no, no, rod, no), group.PICKAXE.get());
            assertCrafts(helper, 1, 3, List.of(x, rod, rod), group.SHOVEL.get());
            assertCrafts(helper, 2, 3, List.of(x, x, x, rod, no, rod), group.AXE.get());
            assertCrafts(helper, 2, 3, List.of(x, x, no, rod, no, rod), group.HOE.get());
            assertCrafts(helper, 1, 3, List.of(x, x, rod), group.SWORD.get());
            families++;
        }

        helper.assertTrue(families > 0, "not one material family had a populated material tag, so nothing was checked");
        helper.succeed();
    }

    /** The same sweep for armour: four pieces per material family. */
    private static void everyMaterialArmourSetCrafts(GameTestHelper helper) {
        final ItemStack no = ItemStack.EMPTY;
        int families = 0;

        for (ToolsItems.MaterialGroup group : ToolsItems.MATERIAL_GROUPS.values()) {
            ItemStack x = firstOf(group.material);
            if (x.isEmpty()) {
                continue;
            }

            assertCrafts(helper, 3, 2, List.of(x, x, x, x, no, x), group.HELMET.get());
            assertCrafts(helper, 3, 3, List.of(x, no, x, x, x, x, x, x, x), group.CHESTPLATE.get());
            assertCrafts(helper, 3, 3, List.of(x, x, x, x, no, x, x, no, x), group.LEGGINGS.get());
            assertCrafts(helper, 3, 2, List.of(x, no, x, x, no, x), group.BOOTS.get());
            families++;
        }

        helper.assertTrue(families > 0, "not one material family had a populated material tag, so nothing was checked");
        helper.succeed();
    }

    /**
     * Armour goes into the armour slots and the defence it grants is the configured one. Those
     * per-slot numbers used to come from an {@code ArmorItem} override; they are baked into the
     * {@code equippable} and {@code attribute_modifiers} components at construction now, so this is
     * the only thing that proves the configuration reached them.
     */
    private static void armourEquipsAndProtects(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper, ItemStack.EMPTY);
        stand(helper, player, new BlockPos(4, 1, 4));

        ToolsItems.MATERIAL_GROUPS.forEach((name, group) -> {
            ArmorMaterialConfig armor = ToolsCommonMod.COMMON_CONFIG.moddedArmors.get(name);
            int expected = armor.getHelmetReductionAmount() + armor.getChestPlateReductionAmount() + armor.getLeggingsReductionAmount() + armor.getBootsReductionAmount();
            assertArmourValue(helper, player, name, group.HELMET.get(), group.CHESTPLATE.get(), group.LEGGINGS.get(), group.BOOTS.get(), expected);
        });

        ArmorMaterialConfig chicken = ToolsCommonMod.COMMON_CONFIG.chickenSuitArmorMaterial;
        assertArmourValue(helper, player, "chicken_suit",
                ToolsItems.CHICKEN_SUIT_HELMET.get(), ToolsItems.CHICKEN_SUIT_CHESTPLATE.get(), ToolsItems.CHICKEN_SUIT_LEGGINGS.get(), ToolsItems.CHICKEN_SUIT_BOOTS.get(),
                chicken.getHelmetReductionAmount() + chicken.getChestPlateReductionAmount() + chicken.getLeggingsReductionAmount() + chicken.getBootsReductionAmount());

        helper.succeed();
    }
}
