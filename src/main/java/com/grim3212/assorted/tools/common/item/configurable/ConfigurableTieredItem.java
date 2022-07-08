package com.grim3212.assorted.tools.common.item.configurable;

import com.grim3212.assorted.tools.common.handler.ItemTierHolder;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ConfigurableTieredItem extends Item {

	private final ItemTierHolder tierHolder;

	public ConfigurableTieredItem(ItemTierHolder tierHolder, Properties builder) {
		super(builder);
		this.tierHolder = tierHolder;
	}

	public ItemTierHolder getTierHolder() {
		return tierHolder;
	}

	@Override
	public boolean isDamageable(ItemStack stack) {
		return true;
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return this.tierHolder.getMaxUses();
	}

	@Override
	public int getEnchantmentValue() {
		return this.tierHolder.getEnchantability();
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
		return this.tierHolder.getDefaultTier().getRepairIngredient().test(repair) || super.isValidRepairItem(toRepair, repair);
	}
}
