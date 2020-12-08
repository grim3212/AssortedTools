package com.grim3212.assorted.tools.api.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface ISwitchModes {

	public default ItemStack cycleMode(PlayerEntity player, ItemStack stack) {
		return stack;
	}

	public default ItemStack setMode(PlayerEntity player, ItemStack stack, int mode) {
		return stack;
	}

}
