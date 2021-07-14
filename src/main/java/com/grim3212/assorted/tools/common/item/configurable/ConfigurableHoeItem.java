package com.grim3212.assorted.tools.common.item.configurable;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.grim3212.assorted.tools.common.handler.ItemTierHolder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.ForgeEventFactory;

public class ConfigurableHoeItem extends ConfigurableToolItem {

	private static final Set<Block> EFFECTIVE_ON_BLOCKS = ImmutableSet.of(Blocks.NETHER_WART_BLOCK, Blocks.WARPED_WART_BLOCK, Blocks.HAY_BLOCK, Blocks.DRIED_KELP_BLOCK, Blocks.TARGET, Blocks.SHROOMLIGHT, Blocks.SPONGE, Blocks.WET_SPONGE, Blocks.JUNGLE_LEAVES, Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.ACACIA_LEAVES, Blocks.BIRCH_LEAVES);

	public ConfigurableHoeItem(ItemTierHolder tierHolder, Item.Properties properties) {
		super(tierHolder, -tierHolder.getHarvestLevel(), 0.0F, EFFECTIVE_ON_BLOCKS, properties.addToolType(ToolType.HOE, tierHolder.getHarvestLevel()));
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		World world = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		int hook = ForgeEventFactory.onHoeUse(context);
		if (hook != 0)
			return hook > 0 ? ActionResultType.SUCCESS : ActionResultType.FAIL;
		if (context.getClickedFace() != Direction.DOWN && world.isEmptyBlock(blockpos.above())) {
			BlockState blockstate = world.getBlockState(blockpos).getToolModifiedState(world, blockpos, context.getPlayer(), context.getItemInHand(), ToolType.HOE);
			if (blockstate != null) {
				PlayerEntity playerentity = context.getPlayer();
				world.playSound(playerentity, blockpos, SoundEvents.HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
				if (!world.isClientSide) {
					world.setBlock(blockpos, blockstate, 11);
					if (playerentity != null) {
						context.getItemInHand().hurtAndBreak(1, playerentity, (player) -> {
							player.broadcastBreakEvent(context.getHand());
						});
					}
				}

				return ActionResultType.sidedSuccess(world.isClientSide);
			}
		}

		return ActionResultType.PASS;
	}

	@Override
	public void addToolTypes(Map<ToolType, Integer> toolClasses, ItemStack stack) {
		toolClasses.put(ToolType.HOE, this.getTierHarvestLevel());
	}
}
