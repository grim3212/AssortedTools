package com.grim3212.assorted.tools.common.item.configurable;

import java.util.Map;

import com.grim3212.assorted.tools.common.handler.ItemTierHolder;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ToolType;

public class ConfigurablePickaxeItem extends ConfigurableToolItem {

	public ConfigurablePickaxeItem(ItemTierHolder tierHolder, Item.Properties builder) {
		super(tierHolder, 3, -2.4F, BlockTags.MINEABLE_WITH_PICKAXE, builder.addToolType(ToolType.PICKAXE, tierHolder.getHarvestLevel()));
	}

	@Override
	public boolean isCorrectToolForDrops(BlockState blockIn) {
		int i = this.getTier().getLevel();
		if (blockIn.getHarvestTool() == ToolType.PICKAXE) {
			return i >= blockIn.getHarvestLevel();
		}
		Material material = blockIn.getMaterial();
		return material == Material.STONE || material == Material.METAL || material == Material.HEAVY_METAL;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		Material material = state.getMaterial();
		return material != Material.METAL && material != Material.HEAVY_METAL && material != Material.STONE ? super.getDestroySpeed(stack, state) : this.speed;
	}

	@Override
	public void addToolTypes(Map<ToolType, Integer> toolClasses, ItemStack stack) {
		toolClasses.put(ToolType.PICKAXE, this.getTierHarvestLevel());
	}
}
