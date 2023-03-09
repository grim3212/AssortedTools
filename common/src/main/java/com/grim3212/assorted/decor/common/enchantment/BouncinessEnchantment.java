package com.grim3212.assorted.decor.common.enchantment;

import com.grim3212.assorted.decor.common.item.BetterSpearItem;
import com.grim3212.assorted.decor.config.ToolsConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class BouncinessEnchantment extends Enchantment {

    protected BouncinessEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
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
