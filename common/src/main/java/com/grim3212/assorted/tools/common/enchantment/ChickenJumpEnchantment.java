package com.grim3212.assorted.tools.common.enchantment;

import com.grim3212.assorted.lib.annotations.LoaderImplement;
import com.grim3212.assorted.tools.ToolsCommonMod;
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
        return ToolsCommonMod.COMMON_CONFIG.chickenSuitEnabled.get() ? super.canEnchant(stack) : false;
    }

    @LoaderImplement(loader = LoaderImplement.Loader.FORGE, value = "IForgeEnchantment")
    public boolean isAllowedOnBooks() {
        return ToolsCommonMod.COMMON_CONFIG.chickenSuitEnabled.get();
    }

    @Override
    public boolean isTradeable() {
        return ToolsCommonMod.COMMON_CONFIG.chickenSuitEnabled.get() ? super.isTradeable() : false;
    }

    @Override
    public boolean isDiscoverable() {
        return ToolsCommonMod.COMMON_CONFIG.chickenSuitEnabled.get() ? super.isDiscoverable() : false;
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
