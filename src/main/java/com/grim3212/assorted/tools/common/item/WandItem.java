package com.grim3212.assorted.tools.common.item;

import java.util.Random;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import com.grim3212.assorted.tools.common.util.NBTHelper;
import com.grim3212.assorted.tools.common.util.WandCoord3D;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public abstract class WandItem extends Item implements ISwitchModes {

	protected final boolean reinforced;
	protected Random rand;

	protected BlockState stateOrig = Blocks.AIR.getDefaultState();
	protected BlockState stateClicked = Blocks.AIR.getDefaultState();

	public WandItem(boolean reinforced, Properties properties) {
		super(properties.group(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP));
		this.reinforced = reinforced;
		this.rand = new Random();
	}

	@Override
	public abstract void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn);

	protected abstract boolean canBreak(World worldIn, BlockPos pos, ItemStack stack);

	public ItemStack getNeededItem(World world, BlockState state, PlayerEntity player) {
		return state.getBlock().getPickBlock(state, null, world, BlockPos.ZERO, player);
	}

	public int getNeededCount(BlockState state) {
		if (state.getBlock() instanceof SlabBlock) {
			return 2;
		}
		return 1;
	}

	public boolean isSurface(BlockState state) {
		return (state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.GRASS || state.getBlock() == Blocks.STONE || state.getBlock() == Blocks.GRAVEL || state.getBlock() == Blocks.SANDSTONE || state.getBlock() == Blocks.SAND || state.getBlock() == Blocks.BEDROCK || state.getBlock() == Blocks.COAL_ORE || state.getBlock() == Blocks.IRON_ORE || state.getBlock() == Blocks.GOLD_ORE || state.getBlock() == Blocks.DIAMOND_ORE || state.getBlock() == Blocks.LAPIS_ORE);
	}

	protected abstract boolean isTooFar(int range, int maxDiff, int range2D, ItemStack stack);

	public boolean isTooFar(WandCoord3D a, WandCoord3D b, ItemStack stack) {
		if (ToolsConfig.COMMON.freeBuildMode.get()) {
			return a.getDistance(b) > 1500.0D;
		}
		return this.isTooFar((int) a.getDistance(b), 10, (int) a.getDistanceFlat(b), stack);
	}

	protected abstract boolean doEffect(World world, PlayerEntity entityplayer, Hand hand, WandCoord3D start, WandCoord3D end, BlockState state);

	protected void sendMessage(PlayerEntity player, ITextComponent message) {
		if (!player.world.isRemote) {
			player.sendMessage(message, player.getUniqueID());
		}
	}

	protected void error(PlayerEntity entityplayer, WandCoord3D p, String reason) {
		entityplayer.world.playSound(entityplayer, p.pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, (entityplayer.world.rand.nextFloat() + 0.7F) / 2.0F, 0.5F + entityplayer.world.rand.nextFloat() * 0.3F);
		sendMessage(entityplayer, new TranslationTextComponent("error.wand." + reason));
		particles(entityplayer.world, p.pos, 3);
	}

	protected abstract double[] getParticleColor();

	private void particles(World world, WandCoord3D c, int effect) {
		particles(world, c.pos, effect);
	}

	protected void particles(World world, BlockPos pos, int effect) {
		double d = 0.0625D;

		if (effect == 1) {
			world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
			return;
		}
		if (effect == 2) {
			world.addParticle(ParticleTypes.SPLASH, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
			return;
		}

		double R = 0.0D;
		double G = 0.0D;
		double B = 0.0D;

		if (effect == 0) {
			double[] color = getParticleColor();
			R = color[0];
			G = color[1];
			B = color[2];
		} else {
			R = 0.8D;
		}

		for (int l = 0; l < 6; l++) {
			double d1 = pos.getX() + this.rand.nextFloat();
			double d2 = pos.getY() + this.rand.nextFloat();
			double d3 = pos.getZ() + this.rand.nextFloat();

			if ((l == 0) && (!world.getBlockState(pos.up()).isOpaqueCube(world, pos))) {
				d2 = pos.getY() + 1 + d;
			}
			if ((l == 1) && (!world.getBlockState(pos.down()).isOpaqueCube(world, pos))) {
				d2 = pos.getY() + 0 - d;
			}
			if ((l == 2) && (!world.getBlockState(pos.south()).isOpaqueCube(world, pos))) {
				d3 = pos.getZ() + 1 + d;
			}
			if ((l == 3) && (!world.getBlockState(pos.north()).isOpaqueCube(world, pos))) {
				d3 = pos.getZ() + 0 - d;
			}
			if ((l == 4) && (!world.getBlockState(pos.east()).isOpaqueCube(world, pos))) {
				d1 = pos.getX() + 1 + d;
			}
			if ((l == 5) && (!world.getBlockState(pos.west()).isOpaqueCube(world, pos))) {
				d1 = pos.getX() + 0 - d;
			}
			if ((d1 < pos.getX()) || (d1 > pos.getX() + 1) || (d2 < 0.0D) || (d2 > pos.getY() + 1) || (d3 < pos.getZ()) || (d3 > pos.getZ() + 1))
				world.addParticle(RedstoneParticleData.REDSTONE_DUST, d1, d2, d3, R, G, B);
		}
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		BlockPos pos = context.getPos();
		World worldIn = context.getWorld();
		PlayerEntity playerIn = context.getPlayer();
		Hand hand = context.getHand();
		boolean isFree = ToolsConfig.COMMON.freeBuildMode.get() || playerIn.isCreative();

		this.stateOrig = worldIn.getBlockState(pos);
		BlockState state = this.stateOrig;

		if (state.getBlock() == Blocks.GRASS) {
			state = Blocks.DIRT.getDefaultState();
		}

		WandCoord3D clicked_current = new WandCoord3D(pos, state);

		if (isIncompatible(state)) {
			error(playerIn, clicked_current, "cantbuild");
			return ActionResultType.SUCCESS;
		}

		ItemStack stack = playerIn.getHeldItem(hand);
		WandCoord3D start = WandCoord3D.getFromNBT(stack.getTag(), "Start");
		
		if (start == null) {
			worldIn.playSound((PlayerEntity) null, pos, state.getBlock().getSoundType(state, worldIn, pos, null).getBreakSound(), SoundCategory.BLOCKS, (state.getBlock().getSoundType(state, worldIn, pos, null).getVolume() + 1.0F) / 2.0F, state.getBlock().getSoundType(state, worldIn, pos, null).getPitch() * 0.8F);

			this.stateClicked = state;
			clicked_current.writeToNBT(stack.getTag(), "Start");

			particles(worldIn, clicked_current, 0);

			NBTHelper.putBoolean(stack, "firstUse", false);
			return ActionResultType.SUCCESS;
		} else {

			if (NBTHelper.getBoolean(stack, "firstUse")) {
				NBTHelper.removeTag(stack, "Start");
				error(playerIn, clicked_current, "nostart");
				return ActionResultType.SUCCESS;
			}

			WandCoord3D.findEnds(start, clicked_current);

			if (isTooFar(start, clicked_current, stack)) {
				NBTHelper.removeTag(stack, "Start");
				error(playerIn, clicked_current, "toofar");
				return ActionResultType.SUCCESS;
			}

			boolean damage = this.doEffect(worldIn, playerIn, hand, start, clicked_current, state);

			if (damage) {
				NBTHelper.putBoolean(stack, "firstUse", true);
				if (!isFree) {
					NBTHelper.removeTag(stack, "Start");
					stack.damageItem(1, playerIn, (s) -> {
						s.sendBreakAnimation(hand);
					});
					return ActionResultType.SUCCESS;
				}
			}
		}

		// Clear out start if we somehow pop out one of the ifs above
		NBTHelper.removeTag(stack, "Start");
		return ActionResultType.SUCCESS;
	}

	protected boolean isIncompatible(BlockState state) {
		return false;
	}
}
