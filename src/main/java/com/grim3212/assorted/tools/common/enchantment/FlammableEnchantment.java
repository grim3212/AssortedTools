package com.grim3212.assorted.tools.common.enchantment;

import com.grim3212.assorted.tools.common.handler.ToolsConfig;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class FlammableEnchantment extends Enchantment {

	protected FlammableEnchantment() {
		super(Rarity.UNCOMMON, ToolsEnchantmentTypes.SPEAR, new EquipmentSlotType[] { EquipmentSlotType.MAINHAND });
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		return ToolsConfig.COMMON.betterSpearsEnabled.get() ? super.canEnchant(stack) : false;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return ToolsConfig.COMMON.betterSpearsEnabled.get() ? super.canApplyAtEnchantingTable(stack) : false;
	}

	@Override
	public boolean isAllowedOnBooks() {
		return ToolsConfig.COMMON.betterSpearsEnabled.get() ? super.isAllowedOnBooks() : false;
	}

	@Override
	public boolean isDiscoverable() {
		return ToolsConfig.COMMON.betterSpearsEnabled.get() ? super.isDiscoverable() : false;
	}

	@Override
	public int getMinCost(int enchantmentLevel) {
		return 5;
	}

	@Override
	public int getMaxCost(int enchantmentLevel) {
		return 30;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	@Override
	protected boolean checkCompatibility(Enchantment enchantment) {
		return super.checkCompatibility(enchantment) && enchantment != ToolsEnchantments.UNSTABLE.get();
	}
}
