package com.grim3212.assorted.tools.common.item.configurable;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;
import com.grim3212.assorted.tools.common.handler.ModdedItemTierHolder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class ConfigurableAxeItem extends ConfigurableToolItem {

	private static final Set<Material> EFFECTIVE_ON_MATERIALS = Sets.newHashSet(Material.WOOD, Material.NETHER_WOOD, Material.PLANT, Material.REPLACEABLE_PLANT, Material.BAMBOO, Material.VEGETABLE);
	private static final Set<Block> EFFECTIVE_ON_BLOCKS = Sets.newHashSet(Blocks.LADDER, Blocks.SCAFFOLDING, Blocks.OAK_BUTTON, Blocks.SPRUCE_BUTTON, Blocks.BIRCH_BUTTON, Blocks.JUNGLE_BUTTON, Blocks.DARK_OAK_BUTTON, Blocks.ACACIA_BUTTON, Blocks.CRIMSON_BUTTON, Blocks.WARPED_BUTTON);

	public ConfigurableAxeItem(ModdedItemTierHolder tierHolder, Item.Properties builder) {
		super(tierHolder, tierHolder.getAxeDamage(), tierHolder.getAxeSpeed(), EFFECTIVE_ON_BLOCKS, builder.addToolType(ToolType.AXE, tierHolder.getHarvestLevel()));
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		Material material = state.getMaterial();
		return EFFECTIVE_ON_MATERIALS.contains(material) ? this.speed : super.getDestroySpeed(stack, state);
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		World world = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		BlockState blockstate = world.getBlockState(blockpos);
		BlockState block = blockstate.getToolModifiedState(world, blockpos, context.getPlayer(), context.getItemInHand(), ToolType.AXE);
		if (block != null) {
			PlayerEntity playerentity = context.getPlayer();
			world.playSound(playerentity, blockpos, SoundEvents.AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
			if (!world.isClientSide) {
				world.setBlock(blockpos, block, 11);
				if (playerentity != null) {
					context.getItemInHand().hurtAndBreak(1, playerentity, (p_220040_1_) -> {
						p_220040_1_.broadcastBreakEvent(context.getHand());
					});
				}
			}

			return ActionResultType.sidedSuccess(world.isClientSide);
		} else {
			return ActionResultType.PASS;
		}
	}

	@Override
	public void addToolTypes(Map<ToolType, Integer> toolClasses, ItemStack stack) {
		toolClasses.put(ToolType.AXE, this.getTierHarvestLevel());
	}
}
