package com.grim3212.assorted.tools.common.item.configurable;

import java.util.Map;

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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.ForgeEventFactory;

public class ConfigurableHoeItem extends ConfigurableToolItem {

	public ConfigurableHoeItem(ItemTierHolder tierHolder, Item.Properties properties) {
		super(tierHolder, -tierHolder.getHarvestLevel(), 0.0F, BlockTags.MINEABLE_WITH_HOE, properties.addToolType(ToolType.HOE, tierHolder.getHarvestLevel()));
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		int hook = ForgeEventFactory.onHoeUse(context);
		if (hook != 0)
			return hook > 0 ? InteractionResult.SUCCESS : InteractionResult.FAIL;
		if (context.getClickedFace() != Direction.DOWN && world.isEmptyBlock(blockpos.above())) {
			BlockState blockstate = world.getBlockState(blockpos).getToolModifiedState(world, blockpos, context.getPlayer(), context.getItemInHand(), ToolType.HOE);
			if (blockstate != null) {
				Player playerentity = context.getPlayer();
				world.playSound(playerentity, blockpos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
				if (!world.isClientSide) {
					world.setBlock(blockpos, blockstate, 11);
					if (playerentity != null) {
						context.getItemInHand().hurtAndBreak(1, playerentity, (player) -> {
							player.broadcastBreakEvent(context.getHand());
						});
					}
				}

				return InteractionResult.sidedSuccess(world.isClientSide);
			}
		}

		return InteractionResult.PASS;
	}

	@Override
	public void addToolTypes(Map<ToolType, Integer> toolClasses, ItemStack stack) {
		toolClasses.put(ToolType.HOE, this.getTierHarvestLevel());
	}
}
