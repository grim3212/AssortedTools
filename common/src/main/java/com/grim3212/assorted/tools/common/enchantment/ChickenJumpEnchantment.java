package com.grim3212.assorted.tools.common.enchantment;

import com.grim3212.assorted.lib.annotations.LoaderImplement;
import com.grim3212.assorted.tools.config.ToolsConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ChickenJumpEnchantment extends Enchantment {

    protected ChickenJumpEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentCategory.ARMOR, new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET});
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return ToolsConfig.Common.chickenSuitEnabled.getValue() ? super.canEnchant(stack) : false;
    }

    @LoaderImplement(loader = LoaderImplement.Loader.FORGE, value = "IForgeEnchantment")
    public boolean isAllowedOnBooks() {
        return ToolsConfig.Common.chickenSuitEnabled.getValue();
    }

    @Override
    public boolean isTradeable() {
        return ToolsConfig.Common.chickenSuitEnabled.getValue() ? super.isTradeable() : false;
    }

    @Override
    public boolean isDiscoverable() {
        return ToolsConfig.Common.chickenSuitEnabled.getValue() ? super.isDiscoverable() : false;
    }

    @Override
    public int getMinCost(int enchantmentLevel) {
        return 10;
    }

    @Override
    public int getMaxCost(int enchantmentLevel) {
        return super.getMaxCost(enchantmentLevel) + 20;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}
