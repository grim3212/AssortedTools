package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.common.enchantment.ToolsEnchantments;
import com.grim3212.assorted.tools.common.handler.ItemTierHolder;
import com.grim3212.assorted.tools.common.util.ToolsTags;

import net.minecraft.core.dispenser.ShearsDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MaterialShears extends ShearsItem implements ITiered {

	private final ItemTierHolder tierHolder;

	public MaterialShears(Properties props, ItemTierHolder tierHolder) {
		super(props);
		this.tierHolder = tierHolder;

		DispenserBlock.registerBehavior(this, new ShearsDispenseItemBehavior());
	}

	@Override
	public boolean isDamageable(ItemStack stack) {
		return true;
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		// This is the current durability between Iron tools and the Iron Shears
		return (int) Math.rint(this.tierHolder.getMaxUses() * 0.952F);
	}

	@Override
	public int getEnchantmentValue(ItemStack stack) {
		return this.tierHolder.getEnchantability();
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return enchantment == ToolsEnchantments.CORAL_CUTTER.get() || enchantment == Enchantments.BLOCK_EFFICIENCY || super.canApplyAtEnchantingTable(stack, enchantment);
	}

	@Override
	public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
		if (ToolsEnchantments.hasCoralCutter(stack)) {
			return state.is(ToolsTags.Blocks.ALL_CORALS) ? true : super.isCorrectToolForDrops(stack, state);
		}

		return super.isCorrectToolForDrops(stack, state);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		if (ToolsEnchantments.hasCoralCutter(stack)) {
			return state.is(ToolsTags.Blocks.ALL_CORALS) ? 10.0F : super.getDestroySpeed(stack, state);
		}

		return super.getDestroySpeed(stack, state);
	}

	@Override
	public ItemTierHolder getTierHolder() {
		return this.tierHolder;
	}
}
