package com.grim3212.assorted.tools.common.enchantment;

import com.grim3212.assorted.tools.config.ToolsConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class CoralCutterEnchantment extends Enchantment {

    protected CoralCutterEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.DIGGER, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return ToolsConfig.Common.moreShearsEnabled.getValue() ? stack.getItem() instanceof ShearsItem || stack.getItem() == Items.BOOK : false;
    }

    @Override
    public boolean isTradeable() {
        return ToolsConfig.Common.moreShearsEnabled.getValue() ? super.isTradeable() : false;
    }

    @Override
    public boolean isDiscoverable() {
        return ToolsConfig.Common.moreShearsEnabled.getValue() ? super.isDiscoverable() : false;
    }

    @Override
    public int getMinCost(int enchantmentLevel) {
        return 12;
    }

    @Override
    public int getMaxCost(int enchantmentLevel) {
        return super.getMaxCost(enchantmentLevel) + 35;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}
