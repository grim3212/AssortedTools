package com.grim3212.assorted.tools.common.enchantment;

import com.grim3212.assorted.lib.core.enchantment.LibEnchantment;
import com.grim3212.assorted.tools.ToolsCommonMod;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.Optional;

public class CoralCutterEnchantment extends LibEnchantment {

    protected CoralCutterEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.DIGGER, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        // TODO: Fix Can still get on tools
        return ToolsCommonMod.COMMON_CONFIG.moreShearsEnabled.get() ? stack.getItem() instanceof ShearsItem || stack.getItem() == Items.BOOK : false;
    }

    @Override
    public Optional<Boolean> assortedlib_canApplyAtEnchantingTable(ItemStack stack) {
        return Optional.of(ToolsCommonMod.COMMON_CONFIG.moreShearsEnabled.get() ? stack.getItem() instanceof ShearsItem || stack.getItem() == Items.BOOK : false);
    }

    @Override
    public boolean isTradeable() {
        return ToolsCommonMod.COMMON_CONFIG.moreShearsEnabled.get() ? super.isTradeable() : false;
    }

    @Override
    public boolean isDiscoverable() {
        return ToolsCommonMod.COMMON_CONFIG.moreShearsEnabled.get() ? super.isDiscoverable() : false;
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
