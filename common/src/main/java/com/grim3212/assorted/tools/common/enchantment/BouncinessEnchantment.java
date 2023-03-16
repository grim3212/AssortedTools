package com.grim3212.assorted.tools.common.enchantment;

import com.grim3212.assorted.lib.core.enchantment.LibEnchantment;
import com.grim3212.assorted.tools.ToolsCommonMod;
import com.grim3212.assorted.tools.common.item.BetterSpearItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.Optional;

public class BouncinessEnchantment extends LibEnchantment {

    protected BouncinessEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return ToolsCommonMod.COMMON_CONFIG.betterSpearsEnabled.get() ? stack.getItem() instanceof BetterSpearItem || stack.getItem() == Items.BOOK : false;
    }

    @Override
    public Optional<Boolean> assortedlib_canApplyAtEnchantingTable(ItemStack stack) {
        return Optional.of(this.canEnchant(stack));
    }

    @Override
    public boolean isTradeable() {
        return ToolsCommonMod.COMMON_CONFIG.betterSpearsEnabled.get() ? super.isTradeable() : false;
    }

    @Override
    public boolean isDiscoverable() {
        return ToolsCommonMod.COMMON_CONFIG.betterSpearsEnabled.get() ? super.isDiscoverable() : false;
    }

    @Override
    public int getMinCost(int enchantmentLevel) {
        return 1 + (enchantmentLevel - 1) * 8;
    }

    @Override
    public int getMaxCost(int enchantmentLevel) {
        return super.getMinCost(enchantmentLevel) + 25;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    protected boolean checkCompatibility(Enchantment enchantment) {
        return super.checkCompatibility(enchantment) && enchantment != ToolsEnchantments.UNSTABLE.get();
    }
}
