package com.grim3212.assorted.tools.common.enchantment;

import com.grim3212.assorted.tools.common.handler.ToolsConfig;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class ChickenJumpEnchantment extends Enchantment {

	protected ChickenJumpEnchantment() {
		super(Rarity.UNCOMMON, EnchantmentType.ARMOR, new EquipmentSlotType[] { EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET });
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		return ToolsConfig.COMMON.chickenSuitEnabled.get() ? super.canEnchant(stack) : false;
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return ToolsConfig.COMMON.chickenSuitEnabled.get() ? super.canApplyAtEnchantingTable(stack) : false;
	}

	@Override
	public boolean isAllowedOnBooks() {
		return ToolsConfig.COMMON.chickenSuitEnabled.get() ? super.isAllowedOnBooks() : false;
	}
	
	@Override
	public boolean isDiscoverable() {
		return ToolsConfig.COMMON.chickenSuitEnabled.get() ? super.isDiscoverable() : false;
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
