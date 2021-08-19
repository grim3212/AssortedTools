package com.grim3212.assorted.tools.common.item.configurable;

import java.util.function.Consumer;
import java.util.function.Predicate;

import com.grim3212.assorted.tools.common.handler.ItemTierHolder;
import com.grim3212.assorted.tools.common.item.MultiToolItem;
import com.mojang.datafixers.util.Pair;

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
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.ForgeEventFactory;

public class ConfigurableHoeItem extends ConfigurableToolItem {

	public ConfigurableHoeItem(ItemTierHolder tierHolder, Item.Properties properties) {
		super(tierHolder, -tierHolder.getHarvestLevel(), 0.0F, BlockTags.MINEABLE_WITH_HOE, properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> pair = MultiToolItem.TILLABLES.get(level.getBlockState(blockpos).getBlock());
		int hook = ForgeEventFactory.onHoeUse(context);
		if (hook != 0)
			return hook > 0 ? InteractionResult.SUCCESS : InteractionResult.FAIL;
		if (context.getClickedFace() != Direction.DOWN && level.isEmptyBlock(blockpos.above())) {
			if (pair == null) {
				return InteractionResult.PASS;
			} else {
				Predicate<UseOnContext> predicate = pair.getFirst();
				Consumer<UseOnContext> consumer = pair.getSecond();
				if (predicate.test(context)) {
					Player player = context.getPlayer();
					level.playSound(player, blockpos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
					if (!level.isClientSide) {
						consumer.accept(context);
						if (player != null) {
							context.getItemInHand().hurtAndBreak(1, player, (p_150845_) -> {
								p_150845_.broadcastBreakEvent(context.getHand());
							});
						}
					}

					return InteractionResult.sidedSuccess(level.isClientSide);
				} else {
					return InteractionResult.PASS;
				}
			}
		}

		return InteractionResult.PASS;
	}

	@Override
	public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
		return ToolActions.DEFAULT_HOE_ACTIONS.contains(toolAction);
	}
}
