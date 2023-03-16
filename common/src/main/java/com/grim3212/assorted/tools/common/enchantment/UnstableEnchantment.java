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

public class UnstableEnchantment extends LibEnchantment {

    protected UnstableEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
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
        return 10 + (enchantmentLevel - 1) * 8;
    }

    @Override
    public int getMaxCost(int enchantmentLevel) {
        return super.getMinCost(enchantmentLevel) + 40;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    protected boolean checkCompatibility(Enchantment enchantment) {
        return super.checkCompatibility(enchantment) && enchantment != ToolsEnchantments.CONDUCTIVE.get() && enchantment != ToolsEnchantments.FLAMMABLE.get() && enchantment != ToolsEnchantments.BOUNCINESS.get();
    }
}
