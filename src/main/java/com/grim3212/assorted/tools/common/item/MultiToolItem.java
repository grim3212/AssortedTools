package com.grim3212.assorted.tools.common.item;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.google.common.collect.Sets;
import com.grim3212.assorted.tools.common.handler.ItemTierHolder;
import com.grim3212.assorted.tools.common.item.configurable.ConfigurableToolItem;
import com.grim3212.assorted.tools.common.util.ToolsTags;
import com.mojang.datafixers.util.Pair;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class MultiToolItem extends ConfigurableToolItem {

	private static final Set<Material> EFFECTIVE_ON_MATERIALS = Sets.newHashSet(Material.WOOD, Material.NETHER_WOOD, Material.PLANT, Material.REPLACEABLE_PLANT, Material.BAMBOO, Material.VEGETABLE, Material.METAL, Material.HEAVY_METAL, Material.STONE);

	public MultiToolItem(ItemTierHolder tier, Item.Properties builderIn) {
		super(tier, () -> tier.getAxeDamage() > tier.getDamage() ? tier.getAxeDamage() : tier.getDamage() + tier.getDamage(), () -> -2.8f, ToolsTags.Blocks.MINEABLE_MULTITOOL, builderIn);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		// Support any enchantment that one of the 5 types of tools that make a
		// multitool can
		return super.canApplyAtEnchantingTable(stack, enchantment) || enchantment.canApplyAtEnchantingTable(new ItemStack(Items.IRON_SWORD)) || enchantment.canApplyAtEnchantingTable(new ItemStack(Items.IRON_SHOVEL)) || enchantment.canApplyAtEnchantingTable(new ItemStack(Items.IRON_PICKAXE)) || enchantment.canApplyAtEnchantingTable(new ItemStack(Items.IRON_HOE)) || enchantment.canApplyAtEnchantingTable(new ItemStack(Items.IRON_AXE));
	}

	@Override
	public boolean canAttackBlock(BlockState state, Level worldIn, BlockPos pos, Player player) {
		return !player.isCreative();
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.hurtAndBreak(1, attacker, (entity) -> {
			entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
		});
		return true;
	}

	@Override
	public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
		return ToolActions.DEFAULT_HOE_ACTIONS.contains(toolAction) || ToolActions.DEFAULT_SHOVEL_ACTIONS.contains(toolAction) || ToolActions.DEFAULT_SWORD_ACTIONS.contains(toolAction) || ToolActions.DEFAULT_PICKAXE_ACTIONS.contains(toolAction) || ToolActions.DEFAULT_AXE_ACTIONS.contains(toolAction);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		if (state.is(Blocks.COBWEB)) {
			return 15.0F;
		}
		Material material = state.getMaterial();
		return EFFECTIVE_ON_MATERIALS.contains(material) ? this.getTierHolder().getEfficiency() : material == Material.PLANT || material == Material.REPLACEABLE_PLANT || material == Material.VEGETABLE || state.is(BlockTags.LEAVES) ? 1.5F : super.getDestroySpeed(stack, state);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		BlockState blockstate = world.getBlockState(blockpos);
		Player playerentity = context.getPlayer();
		ItemStack itemstack = context.getItemInHand();
		Optional<BlockState> optional = Optional.ofNullable(blockstate.getToolModifiedState(context, ToolActions.AXE_STRIP, false));
		Optional<BlockState> optional1 = optional.isPresent() ? Optional.empty() : Optional.ofNullable(blockstate.getToolModifiedState(context, ToolActions.AXE_SCRAPE, false));
		Optional<BlockState> optional2 = optional.isPresent() || optional1.isPresent() ? Optional.empty() : Optional.ofNullable(blockstate.getToolModifiedState(context, ToolActions.AXE_WAX_OFF, false));
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
			BlockState shovelBlock = blockstate.getToolModifiedState(context, ToolActions.SHOVEL_FLATTEN, false);
			if (shovelBlock != null) {
				if (context.getClickedFace() == Direction.DOWN) {
					return InteractionResult.PASS;
				} else {
					if (world.isEmptyBlock(blockpos.above())) {
						world.playSound(playerentity, blockpos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
					} else if (blockstate.getBlock() instanceof CampfireBlock && blockstate.getValue(CampfireBlock.LIT)) {
						if (!world.isClientSide()) {
							world.levelEvent((Player) null, 1009, blockpos, 0);
						}

						CampfireBlock.dowse(playerentity, world, blockpos, blockstate);
						shovelBlock = blockstate.setValue(CampfireBlock.LIT, false);
					}

					if (shovelBlock != null) {
						if (!world.isClientSide) {
							world.setBlock(blockpos, shovelBlock, 11);
							if (playerentity != null) {
								itemstack.hurtAndBreak(1, playerentity, (player) -> {
									player.broadcastBreakEvent(context.getHand());
								});
							}
						}

						return InteractionResult.sidedSuccess(world.isClientSide);
					} else {
						return onHoeUse(context);
					}
				}
			}
			return onHoeUse(context);
		}
	}

	public InteractionResult onHoeUse(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		BlockState toolModifiedState = level.getBlockState(blockpos).getToolModifiedState(context, ToolActions.HOE_TILL, false);
		Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> pair = toolModifiedState == null ? null : Pair.of(ctx -> true, HoeItem.changeIntoState(toolModifiedState));

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
}
