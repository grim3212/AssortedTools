package com.grim3212.assorted.decor.api.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ISwitchModes {

    default ItemStack cycleMode(Player player, ItemStack stack) {
        return stack;
    }

    default ItemStack setMode(Player player, ItemStack stack, int mode) {
        return stack;
    }

}
