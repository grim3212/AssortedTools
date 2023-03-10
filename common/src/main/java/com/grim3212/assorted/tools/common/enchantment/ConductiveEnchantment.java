package com.grim3212.assorted.tools.common.enchantment;

import com.grim3212.assorted.tools.common.item.BetterSpearItem;
import com.grim3212.assorted.tools.config.ToolsConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ConductiveEnchantment extends Enchantment {

    protected ConductiveEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return ToolsConfig.Common.betterSpearsEnabled.getValue() ? stack.getItem() instanceof BetterSpearItem || stack.getItem() == Items.BOOK : false;
    }

    @Override
    public boolean isTradeable() {
        return ToolsConfig.Common.betterSpearsEnabled.getValue() ? super.isTradeable() : false;
    }

    @Override
    public boolean isDiscoverable() {
        return ToolsConfig.Common.betterSpearsEnabled.getValue() ? super.isDiscoverable() : false;
    }

    @Override
    public int getMinCost(int enchantmentLevel) {
        return 15 + (enchantmentLevel - 1) * 9;
    }

    @Override
    public int getMaxCost(int enchantmentLevel) {
        return super.getMinCost(enchantmentLevel) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    protected boolean checkCompatibility(Enchantment enchantment) {
        return super.checkCompatibility(enchantment) && enchantment != ToolsEnchantments.UNSTABLE.get();
    }
}
