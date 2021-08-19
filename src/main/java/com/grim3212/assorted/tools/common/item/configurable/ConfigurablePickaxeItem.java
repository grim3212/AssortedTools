package com.grim3212.assorted.tools.common.item.configurable;

import com.grim3212.assorted.tools.common.handler.ItemTierHolder;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class ConfigurablePickaxeItem extends ConfigurableToolItem {

	public ConfigurablePickaxeItem(ItemTierHolder tierHolder, Item.Properties builder) {
		super(tierHolder, 3, -2.4F, BlockTags.MINEABLE_WITH_PICKAXE, builder);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		Material material = state.getMaterial();
		return material != Material.METAL && material != Material.HEAVY_METAL && material != Material.STONE ? super.getDestroySpeed(stack, state) : this.speed;
	}

	@Override
	public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
		return ToolActions.DEFAULT_PICKAXE_ACTIONS.contains(toolAction);
	}
}
