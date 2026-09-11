package com.grim3212.assorted.tools.data;

import com.google.common.collect.Lists;
import com.grim3212.assorted.lib.core.conditions.LibConditionProvider;
import com.grim3212.assorted.lib.data.LibDatapackRegistryProvider;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.tools.common.crafting.ToolsConditions;
import com.grim3212.assorted.tools.common.enchantment.ToolsEnchantments;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;
import java.util.Map;

/**
 * Writes the mod's enchantments as datapack JSON. Weights and anvil costs follow the old rarity
 * scale: common, uncommon, rare, very rare are weight 10/5/2/1 and anvil cost 1/2/4/8.
 */
public class ToolsEnchantmentData extends LibDatapackRegistryProvider {

    @Override
    public void addEntries(RegistrySetBuilder builder) {
        builder.add(Registries.ENCHANTMENT, ToolsEnchantmentData::bootstrap);
    }

    @Override
    public List<ResourceKey<? extends Registry<?>>> registries() {
        return Lists.newArrayList(Registries.ENCHANTMENT);
    }

    /**
     * Each enchantment exists only while its part is enabled. The four spear enchantments name each
     * other as exclusive, which is safe because they share one part.
     */
    @Override
    public Map<ResourceKey<?>, List<LibConditionProvider>> conditions() {
        List<LibConditionProvider> spears = List.of(Services.CONDITIONS.partEnabled(ToolsConditions.Parts.BETTER_SPEARS));
        return Map.of(
                ToolsEnchantments.CHICKEN_JUMP, List.of(Services.CONDITIONS.partEnabled(ToolsConditions.Parts.CHICKEN_SUIT)),
                ToolsEnchantments.BOUNCINESS, spears,
                ToolsEnchantments.CONDUCTIVE, spears,
                ToolsEnchantments.FLAMMABLE, spears,
                ToolsEnchantments.UNSTABLE, spears,
                ToolsEnchantments.CORAL_CUTTER, List.of(Services.CONDITIONS.partEnabled(ToolsConditions.Parts.MORE_SHEARS)));
    }

    private static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        HolderGetter<Enchantment> enchantments = context.lookup(Registries.ENCHANTMENT);

        HolderSet<Item> spears = items.getOrThrow(ToolsEnchantments.SPEAR_ENCHANTABLE);
        HolderSet<Item> shears = items.getOrThrow(ToolsEnchantments.SHEARS_ENCHANTABLE);

        HolderSet<Enchantment> unstable = HolderSet.direct(List.of(enchantments.getOrThrow(ToolsEnchantments.UNSTABLE)));
        HolderSet<Enchantment> unstableExclusions = HolderSet.direct(List.of(
                enchantments.getOrThrow(ToolsEnchantments.CONDUCTIVE),
                enchantments.getOrThrow(ToolsEnchantments.FLAMMABLE),
                enchantments.getOrThrow(ToolsEnchantments.BOUNCINESS)));

        register(context, ToolsEnchantments.CHICKEN_JUMP, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                        5,
                        1,
                        Enchantment.constantCost(10),
                        Enchantment.constantCost(35),
                        2,
                        EquipmentSlotGroup.ARMOR)));

        register(context, ToolsEnchantments.BOUNCINESS, Enchantment.enchantment(
                Enchantment.definition(
                        spears,
                        5,
                        5,
                        Enchantment.dynamicCost(1, 8),
                        Enchantment.dynamicCost(36, 10),
                        2,
                        EquipmentSlotGroup.MAINHAND))
                .exclusiveWith(unstable));

        register(context, ToolsEnchantments.CONDUCTIVE, Enchantment.enchantment(
                Enchantment.definition(
                        spears,
                        1,
                        3,
                        Enchantment.dynamicCost(15, 9),
                        Enchantment.dynamicCost(61, 10),
                        8,
                        EquipmentSlotGroup.MAINHAND))
                .exclusiveWith(unstable));

        register(context, ToolsEnchantments.FLAMMABLE, Enchantment.enchantment(
                Enchantment.definition(
                        spears,
                        5,
                        1,
                        Enchantment.constantCost(5),
                        Enchantment.constantCost(30),
                        2,
                        EquipmentSlotGroup.MAINHAND))
                .exclusiveWith(unstable));

        register(context, ToolsEnchantments.UNSTABLE, Enchantment.enchantment(
                Enchantment.definition(
                        spears,
                        2,
                        2,
                        Enchantment.dynamicCost(10, 8),
                        Enchantment.dynamicCost(51, 10),
                        4,
                        EquipmentSlotGroup.MAINHAND))
                .exclusiveWith(unstableExclusions));

        register(context, ToolsEnchantments.CORAL_CUTTER, Enchantment.enchantment(
                Enchantment.definition(
                        shears,
                        2,
                        1,
                        Enchantment.constantCost(12),
                        Enchantment.constantCost(52),
                        4,
                        EquipmentSlotGroup.MAINHAND)));
    }

    private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.identifier()));
    }
}
