package com.grim3212.assorted.tools.common.item;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;
import com.grim3212.assorted.tools.common.handler.ItemTierHolder;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import com.grim3212.assorted.tools.common.item.configurable.ConfigurableToolItem;
import com.grim3212.assorted.tools.common.util.ToolsTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.ForgeEventFactory;

public class MultiToolItem extends ConfigurableToolItem {

	private static final Set<Material> EFFECTIVE_ON_MATERIALS = Sets.newHashSet(Material.WOOD, Material.NETHER_WOOD, Material.PLANT, Material.REPLACEABLE_PLANT, Material.BAMBOO, Material.VEGETABLE, Material.METAL, Material.HEAVY_METAL, Material.STONE);
	private final boolean isExtraMaterial;

	public MultiToolItem(ItemTierHolder tier, Item.Properties builderIn) {
		this(tier, builderIn, false);
	}

	public MultiToolItem(ItemTierHolder tier, Item.Properties builderIn, boolean isExtraMaterial) {
		super(tier, tier.getAxeDamage() > tier.getDamage() ? tier.getAxeDamage() : tier.getDamage(), -2.8f, ToolsTags.Blocks.MINEABLE_MULTITOOL, builderIn.addToolType(ToolType.AXE, tier.getHarvestLevel()).addToolType(ToolType.SHOVEL, tier.getHarvestLevel()).addToolType(ToolType.PICKAXE, tier.getHarvestLevel()).addToolType(ToolType.HOE, tier.getHarvestLevel()));
		this.isExtraMaterial = isExtraMaterial;
	}

	@Override
	protected boolean allowdedIn(CreativeModeTab group) {
		if (this.isExtraMaterial) {
			return ToolsConfig.COMMON.multiToolsEnabled.get() && ToolsConfig.COMMON.extraMaterialsEnabled.get() ? super.allowdedIn(group) : false;
		}

		return ToolsConfig.COMMON.multiToolsEnabled.get() ? super.allowdedIn(group) : false;
	}

	@Override
	public void addToolTypes(Map<ToolType, Integer> toolClasses, ItemStack stack) {
		toolClasses.put(ToolType.AXE, this.getTierHarvestLevel());
		toolClasses.put(ToolType.PICKAXE, this.getTierHarvestLevel());
		toolClasses.put(ToolType.SHOVEL, this.getTierHarvestLevel());
		toolClasses.put(ToolType.HOE, this.getTierHarvestLevel());
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
	public boolean isCorrectToolForDrops(BlockState blockIn) {
		int i = this.getTierHarvestLevel();
		if (blockIn.getHarvestTool() == ToolType.PICKAXE) {
			return i >= blockIn.getHarvestLevel();
		}
		Material material = blockIn.getMaterial();
		return material == Material.STONE || material == Material.METAL || material == Material.HEAVY_METAL || blockIn.is(Blocks.SNOW) || blockIn.is(Blocks.SNOW_BLOCK) || blockIn.is(Blocks.COBWEB);
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
		BlockState axeBlock = blockstate.getToolModifiedState(world, blockpos, playerentity, itemstack, ToolType.AXE);
		if (axeBlock != null) {
			world.playSound(playerentity, blockpos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
			if (!world.isClientSide) {
				world.setBlock(blockpos, axeBlock, 11);
				if (playerentity != null) {
					itemstack.hurtAndBreak(1, playerentity, (p_220040_1_) -> {
						p_220040_1_.broadcastBreakEvent(context.getHand());
					});
				}
			}

			return InteractionResult.sidedSuccess(world.isClientSide);
		} else {
			BlockState shovelBlock = blockstate.getToolModifiedState(world, blockpos, playerentity, itemstack, ToolType.SHOVEL);
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
}
