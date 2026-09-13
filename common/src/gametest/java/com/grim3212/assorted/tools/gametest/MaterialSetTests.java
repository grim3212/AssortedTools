package com.grim3212.assorted.tools.gametest;

import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.tools.ToolsCommonMod;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.grim3212.assorted.tools.config.ArmorMaterialConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.lib.test.TestSupport.*;
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
        out.accept("each_tool_shape_swings_like_its_vanilla_shape", MaterialSetTests::eachToolShapeSwingsLikeItsVanillaShape);
    }

    /**
     * A material's tools are ranked against each other the way vanilla ranks its own: the sword hits
     * hardest and fastest, the pickaxe is a mining tool rather than a second sword, and the hoe is
     * the worst weapon of the set. Everything numeric is baked into {@code attribute_modifiers} at
     * registration, so there is no live getter to ask - a wrong baseline is silent.
     */
    private static void eachToolShapeSwingsLikeItsVanillaShape(GameTestHelper helper) {
        for (ToolsItems.MaterialGroup group : ToolsItems.MATERIAL_GROUPS.values()) {
            String name = group.tier.getName();

            double swordDamage = swing(group.SWORD.get().getDefaultInstance(), Attributes.ATTACK_DAMAGE);
            double pickaxeDamage = swing(group.PICKAXE.get().getDefaultInstance(), Attributes.ATTACK_DAMAGE);
            double hoeDamage = swing(group.HOE.get().getDefaultInstance(), Attributes.ATTACK_DAMAGE);

            helper.assertTrue(pickaxeDamage < swordDamage, name + " pickaxe hits for " + pickaxeDamage + " against the sword's " + swordDamage + "; a pickaxe is not a sword");
            helper.assertTrue(hoeDamage < pickaxeDamage, name + " hoe hits for " + hoeDamage + " against the pickaxe's " + pickaxeDamage + "; the hoe is the worst weapon of the set");

            double swordSpeed = swing(group.SWORD.get().getDefaultInstance(), Attributes.ATTACK_SPEED);
            double pickaxeSpeed = swing(group.PICKAXE.get().getDefaultInstance(), Attributes.ATTACK_SPEED);

            helper.assertTrue(pickaxeSpeed < swordSpeed, name + " pickaxe swings at " + pickaxeSpeed + ", the same as or faster than the sword's " + swordSpeed);
        }

        helper.succeed();
    }

    /** What an item's baked {@code attribute_modifiers} add to {@code attribute} in the main hand. */
    private static double swing(ItemStack stack, Holder<Attribute> attribute) {
        return stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY).compute(attribute, 0.0D, EquipmentSlot.MAINHAND);
    }

    /**
     * Every extra material's five tool recipes, found through the recipe manager so a new family is
     * covered automatically. A family whose material tag is empty is skipped, since its recipes are
     * conditioned on {@code item_tag_populated}.
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
     * Armour equips and grants its configured defence, which is baked into the {@code equippable}
     * and {@code attribute_modifiers} components at construction.
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
