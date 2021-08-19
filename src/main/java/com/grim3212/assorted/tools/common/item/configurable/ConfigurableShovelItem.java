package com.grim3212.assorted.tools.common.item.configurable;

import com.grim3212.assorted.tools.common.handler.ItemTierHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class ConfigurableShovelItem extends ConfigurableToolItem {

	public ConfigurableShovelItem(ItemTierHolder tierHolder, Item.Properties builder) {
		super(tierHolder, 1.5F, -3.0F, BlockTags.MINEABLE_WITH_SHOVEL, builder);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		BlockState blockstate = world.getBlockState(blockpos);
		if (context.getClickedFace() == Direction.DOWN) {
			return InteractionResult.PASS;
		} else {
			Player playerentity = context.getPlayer();
			BlockState blockstate1 = blockstate.getToolModifiedState(world, blockpos, playerentity, context.getItemInHand(), ToolActions.SHOVEL_FLATTEN);
			BlockState blockstate2 = null;
			if (blockstate1 != null && world.isEmptyBlock(blockpos.above())) {
				world.playSound(playerentity, blockpos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
				blockstate2 = blockstate1;
			} else if (blockstate.getBlock() instanceof CampfireBlock && blockstate.getValue(CampfireBlock.LIT)) {
				if (!world.isClientSide()) {
					world.levelEvent((Player) null, 1009, blockpos, 0);
				}

				CampfireBlock.dowse(playerentity, world, blockpos, blockstate);
				blockstate2 = blockstate.setValue(CampfireBlock.LIT, Boolean.valueOf(false));
			}

			if (blockstate2 != null) {
				if (!world.isClientSide) {
					world.setBlock(blockpos, blockstate2, 11);
					if (playerentity != null) {
						context.getItemInHand().hurtAndBreak(1, playerentity, (player) -> {
							player.broadcastBreakEvent(context.getHand());
						});
					}
				}

				return InteractionResult.sidedSuccess(world.isClientSide);
			} else {
				return InteractionResult.PASS;
			}
		}
	}

	@Override
	public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
		return ToolActions.DEFAULT_SHOVEL_ACTIONS.contains(toolAction);
	}
}
