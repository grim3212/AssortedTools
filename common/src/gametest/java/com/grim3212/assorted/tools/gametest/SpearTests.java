package com.grim3212.assorted.tools.gametest;

import com.grim3212.assorted.tools.api.item.SpearStats;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import net.minecraft.world.item.component.KineticWeapon;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The vanilla-style spears of the extra materials. The throwing spears are covered by the
 * projectile and enchantment tests.
 */
final class SpearTests {

    private SpearTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("material_spears_are_vanilla_spears", SpearTests::materialSpearsAreVanillaSpears);
    }

    /**
     * Every extra material's spear is what vanilla's iron spear is: it carries every component type
     * the iron spear carries (the lunge, the piercing hit, the reach, the stab swing, the spear
     * damage type), sits in the same tags, and has its own material's durability. That is what makes
     * vanilla's lunge, dismount and enchantments apply to it without a line of our own.
     */
    private static void materialSpearsAreVanillaSpears(GameTestHelper helper) {
        for (ToolsItems.MaterialGroup group : ToolsItems.MATERIAL_GROUPS.values()) {
            Item item = group.SPEAR.get();
            String name = BuiltInRegistries.ITEM.getKey(item).getPath();
            ItemStack stack = new ItemStack(item);

            for (TypedDataComponent<?> component : Items.IRON_SPEAR.components()) {
                helper.assertTrue(item.components().has(component.type()), name + " lacks the iron spear's " + component.type());
            }
            helper.assertTrue(stack.getOrDefault(DataComponents.DAMAGE_TYPE, null) != null && stack.get(DataComponents.DAMAGE_TYPE).is(DamageTypes.SPEAR), name + " does not deal spear damage");
            helper.assertValueEqual(stack.getMaxDamage(), group.tier.getMaxUses(), name + " durability, against its material's");

            // The configured lunge values are what the components were built from.
            SpearStats configured = group.tier.getSpearStats();
            KineticWeapon lunge = stack.get(DataComponents.KINETIC_WEAPON);
            helper.assertValueEqual(lunge.damageMultiplier(), (float) configured.damageMultiplier(), name + " lunge damage multiplier, against its configuration");
            // Vanilla turns seconds into ticks in float arithmetic, so the expectation must too.
            helper.assertValueEqual(lunge.delayTicks(), (int) ((float) configured.delaySeconds() * 20.0F), name + " lunge delay, against its configuration");
            helper.assertTrue(stack.is(ItemTags.SPEARS), name + " is not in minecraft:spears");
            helper.assertTrue(stack.is(ItemTags.LUNGE_ENCHANTABLE), name + " cannot take lunge");
            helper.assertTrue(stack.is(ItemTags.MELEE_WEAPON_ENCHANTABLE), name + " cannot take melee enchantments");
            helper.assertTrue(stack.is(ItemTags.PIGLIN_PREFERRED_WEAPONS), name + " is not a weapon piglins prefer");
        }

        helper.succeed();
    }
}
