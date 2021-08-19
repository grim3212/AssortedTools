package com.grim3212.assorted.tools.common.item.configurable;

import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Sets;
import com.grim3212.assorted.tools.common.handler.ModdedItemTierHolder;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class ConfigurableAxeItem extends ConfigurableToolItem {

	private static final Set<Material> EFFECTIVE_ON_MATERIALS = Sets.newHashSet(Material.WOOD, Material.NETHER_WOOD, Material.PLANT, Material.REPLACEABLE_PLANT, Material.BAMBOO, Material.VEGETABLE);

	public ConfigurableAxeItem(ModdedItemTierHolder tierHolder, Item.Properties builder) {
		super(tierHolder, tierHolder.getAxeDamage(), tierHolder.getAxeSpeed(), BlockTags.MINEABLE_WITH_AXE, builder);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		Material material = state.getMaterial();
		return EFFECTIVE_ON_MATERIALS.contains(material) ? this.speed : super.getDestroySpeed(stack, state);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		BlockState blockstate = world.getBlockState(blockpos);
		Player playerentity = context.getPlayer();
		ItemStack itemstack = context.getItemInHand();
		Optional<BlockState> optional = Optional.ofNullable(blockstate.getToolModifiedState(world, blockpos, playerentity, itemstack, ToolActions.AXE_STRIP));
		Optional<BlockState> optional1 = Optional.ofNullable(blockstate.getToolModifiedState(world, blockpos, playerentity, itemstack, ToolActions.AXE_SCRAPE));
		Optional<BlockState> optional2 = Optional.ofNullable(blockstate.getToolModifiedState(world, blockpos, playerentity, itemstack, ToolActions.AXE_WAX_OFF));
		Optional<BlockState> optional3 = Optional.empty();
		if (optional.isPresent()) {
			world.playSound(playerentity, blockpos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
			optional3 = optional;
		} else if (optional1.isPresent()) {
			world.playSound(playerentity, blockpos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
			world.levelEvent(playerentity, 3005, blockpos, 0);
			optional3 = optional1;
		} else if (optional2.isPresent()) {
			world.playSound(playerentity, blockpos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
			world.levelEvent(playerentity, 3004, blockpos, 0);
			optional3 = optional2;
		}

		if (optional3.isPresent()) {
			if (playerentity instanceof ServerPlayer) {
				CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) playerentity, blockpos, itemstack);
			}

			world.setBlock(blockpos, optional3.get(), 11);
			if (playerentity != null) {
				itemstack.hurtAndBreak(1, playerentity, (p_150686_) -> {
					p_150686_.broadcastBreakEvent(context.getHand());
				});
			}

			return InteractionResult.sidedSuccess(world.isClientSide);
		} else {
			return InteractionResult.PASS;
		}
	}

	@Override
	public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
		return ToolActions.DEFAULT_AXE_ACTIONS.contains(toolAction);
	}
}
