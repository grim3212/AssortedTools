package com.grim3212.assorted.tools.gametest;

import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.tools.api.item.ITiered;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.tools.gametest.ToolsTestSupport.*;

/**
 * Machetes: one per material, cutting plants at the material's speed and swinging like a lighter
 * sword.
 */
final class MacheteTests {

    private MacheteTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("machete_cuts_plants_at_its_material_speed", MacheteTests::macheteCutsPlantsAtItsMaterialSpeed);
        out.accept("machete_swings_faster_and_lighter_than_a_sword", MacheteTests::macheteSwingsFasterAndLighterThanASword);
    }

    /**
     * The {@code #assortedtools:mineable/machete} rule baked into the tool component: leaves, wool
     * and cactus at the tier's speed, cobweb at a sword's, stone at nothing special.
     */
    private static void macheteCutsPlantsAtItsMaterialSpeed(GameTestHelper helper) {
        List<String> wrong = new ArrayList<>();
        for (Item item : ToolsItems.machetes()) {
            float speed = Math.max(((ITiered) item).getTierHolder().getEfficiency(), 1.5F);
            ItemStack stack = new ItemStack(item);
            for (var state : List.of(Blocks.OAK_LEAVES.defaultBlockState(), Blocks.WOOL.white().defaultBlockState(), Blocks.CACTUS.defaultBlockState(), Blocks.VINE.defaultBlockState())) {
                if (stack.getDestroySpeed(state) != speed) {
                    wrong.add(item + " cuts " + state.getBlock() + " at " + stack.getDestroySpeed(state) + " rather than " + speed);
                }
            }
            if (stack.getDestroySpeed(Blocks.COBWEB.defaultBlockState()) != 15.0F || !stack.isCorrectToolForDrops(Blocks.COBWEB.defaultBlockState())) {
                wrong.add(item + " does not cut cobweb like a sword");
            }
            if (stack.getDestroySpeed(Blocks.STONE.defaultBlockState()) != 1.0F) {
                wrong.add(item + " mines stone at " + stack.getDestroySpeed(Blocks.STONE.defaultBlockState()));
            }
            if (!stack.is(ItemTags.SWORDS) || !stack.is(LibCommonTags.Items.TOOLS_MELEE_WEAPONS) || !stack.is(ItemTags.SHARP_WEAPON_ENCHANTABLE)) {
                wrong.add(item + " is missing from the sword, melee weapon or sharp weapon tags");
            }
        }

        helper.assertTrue(ToolsItems.machetes().size() == 6 + ToolsItems.MATERIAL_GROUPS.size(), "expected one machete per vanilla tier and per extra material");
        helper.assertTrue(wrong.isEmpty(), String.join("; ", wrong));
        helper.succeed();
    }

    /** Against the sword of the same material: a lighter hit and a quicker swing. */
    private static void macheteSwingsFasterAndLighterThanASword(GameTestHelper helper) {
        for (ToolsItems.MaterialGroup group : ToolsItems.MATERIAL_GROUPS.values()) {
            String name = group.tier.getName();
            double swordDamage = swing(group.SWORD.get().getDefaultInstance(), Attributes.ATTACK_DAMAGE);
            double macheteDamage = swing(group.MACHETE.get().getDefaultInstance(), Attributes.ATTACK_DAMAGE);
            double swordSpeed = swing(group.SWORD.get().getDefaultInstance(), Attributes.ATTACK_SPEED);
            double macheteSpeed = swing(group.MACHETE.get().getDefaultInstance(), Attributes.ATTACK_SPEED);

            helper.assertTrue(macheteDamage < swordDamage, name + " machete hits for " + macheteDamage + ", not less than the sword's " + swordDamage);
            helper.assertTrue(macheteSpeed > swordSpeed, name + " machete swings at " + macheteSpeed + ", not faster than the sword's " + swordSpeed);
            helper.assertValueEqual(group.MACHETE.get().getDefaultInstance().getMaxDamage(), group.SWORD.get().getDefaultInstance().getMaxDamage(), name + " machete durability against its sword");
        }

        helper.assertTrue(ToolsItems.NETHERITE_MACHETE.get().getDefaultInstance().has(DataComponents.DAMAGE_RESISTANT), "the netherite machete should survive fire and lava");
        helper.succeed();
    }

    private static double swing(ItemStack stack, Holder<Attribute> attribute) {
        return stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY).compute(attribute, 0.0D, EquipmentSlot.MAINHAND);
    }
}
