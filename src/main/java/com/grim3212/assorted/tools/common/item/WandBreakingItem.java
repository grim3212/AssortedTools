package com.grim3212.assorted.tools.common.item;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import com.grim3212.assorted.tools.common.util.NBTHelper;
import com.grim3212.assorted.tools.common.util.WandCoord3D;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.PushReaction;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class WandBreakingItem extends WandItem {

	public WandBreakingItem(boolean reinforced, Properties props) {
		super(reinforced, props);
	}

	protected static boolean isOre(BlockState state) {
		return ToolsConfig.COMMON.destructiveSparedBlocks.getLoadedStates().contains(state);
	}

	@Override
	protected boolean canBreak(World worldIn, BlockPos pos, ItemStack stack) {
		BlockState state = worldIn.getBlockState(pos);

		switch (BreakingMode.fromString(NBTHelper.getString(stack, "Mode"))) {
		case BREAK_WEAK:
			return (state.getMaterial().isReplaceable() || state.getMaterial().getPushReaction() == PushReaction.DESTROY || state.getMaterial().isLiquid());
		case BREAK_ALL:
			return (state.getBlock() != Blocks.BEDROCK || ToolsConfig.COMMON.bedrockBreaking.get());
		case BREAK_XORES:
			return (state.getBlock() != Blocks.BEDROCK || ToolsConfig.COMMON.bedrockBreaking.get()) && !isOre(state);
		}
		return false;
	}

	@Override
	protected boolean isTooFar(int range, int maxDiff, int range2d, ItemStack stack) {
		return range - 250 > maxDiff;
	}

	@Override
	protected double[] getParticleColor() {
		return new double[] { 0.5D, 0.5D, 0.5D };
	}

	@Override
	protected boolean doEffect(World world, PlayerEntity entityplayer, Hand hand, WandCoord3D start, WandCoord3D end, BlockState state) {
		boolean damage = doBreaking(world, start, end, entityplayer, hand);
		if (damage)
			world.playSound((PlayerEntity) null, end.pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 2.5F, 0.5F + world.rand.nextFloat() * 0.3F);
		return damage;
	}

	private boolean doBreaking(World world, WandCoord3D start, WandCoord3D end, PlayerEntity entityplayer, Hand hand) {
		int cnt = 0;
		for (int X = start.pos.getX(); X <= end.pos.getX(); X++) {
			for (int Y = start.pos.getY(); Y <= end.pos.getY(); Y++) {
				for (int Z = start.pos.getZ(); Z <= end.pos.getZ(); Z++) {
					BlockPos newPos = new BlockPos(X, Y, Z);
					BlockState stateAt = world.getBlockState(newPos);
					if ((stateAt.getBlock() != Blocks.AIR) && (canBreak(world, newPos, entityplayer.getHeldItem(hand)))) {
						cnt++;
					}
				}
			}
		}

		if (cnt == 0) {
			if (!world.isRemote)
				error(entityplayer, end, "nowork");
			return false;
		}

		for (int X = start.pos.getX(); X <= end.pos.getX(); X++) {
			for (int Y = start.pos.getY(); Y <= end.pos.getY(); Y++) {
				for (int Z = start.pos.getZ(); Z <= end.pos.getZ(); Z++) {
					BlockPos newPos = new BlockPos(X, Y, Z);
					BlockState stateAt = world.getBlockState(newPos);

					if (stateAt.getBlock() != Blocks.AIR) {
						if (canBreak(world, newPos, entityplayer.getHeldItem(hand))) {
							stateAt.getBlock().onBlockHarvested(world, newPos, stateAt, entityplayer);
							world.setBlockState(newPos, Blocks.AIR.getDefaultState());
							if (this.rand.nextInt(cnt / 50 + 1) == 0)
								particles(world, newPos, 1);
						}
					}
				}
			}
		}

		return true;
	}

	@Override
	public ItemStack cycleMode(PlayerEntity player, ItemStack stack) {
		BreakingMode mode = BreakingMode.fromString(NBTHelper.getString(stack, "Mode"));
		BreakingMode next = BreakingMode.getNext(mode, stack, reinforced);
		NBTHelper.putString(stack, "Mode", next.getString());
		this.sendMessage(player, new TranslationTextComponent(AssortedTools.MODID + ".wand.switched", next.getTranslatedString()));
		return stack;
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.isInGroup(group)) {
			ItemStack stack = new ItemStack(this);
			NBTHelper.putString(stack, "Mode", BreakingMode.BREAK_WEAK.getString());
			items.add(stack);
		}
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		BreakingMode mode = BreakingMode.fromString(NBTHelper.getString(stack, "Mode"));
		if (mode != null)
			tooltip.add(new TranslationTextComponent(AssortedTools.MODID + ".wand.current", mode.getTranslatedString()));
		else
			tooltip.add(new TranslationTextComponent(AssortedTools.MODID + ".broken"));
	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
		NBTHelper.putString(stack, "Mode", BreakingMode.BREAK_WEAK.getString());
	}

	private static enum BreakingMode implements IStringSerializable {
		BREAK_WEAK("breakweak", 0), BREAK_ALL("breakall", 1), BREAK_XORES("breakxores", 2);

		private final String name;
		private final int order;
		private final boolean reinforcedOnly;

		private BreakingMode(String name, int order) {
			this(name, order, false);
		}

		private BreakingMode(String name, int order, boolean reinforcedOnly) {
			this.name = name;
			this.order = order;
			this.reinforcedOnly = reinforcedOnly;
		}

		private static final List<BreakingMode> values = Lists.newArrayList(values()).stream().sorted(Comparator.comparingInt(BreakingMode::getOrder)).collect(Collectors.toList());
		private static final List<BreakingMode> notReinforced = values.stream().filter((mode) -> !mode.reinforcedOnly).sorted(Comparator.comparingInt(BreakingMode::getOrder)).collect(Collectors.toList());

		private static BreakingMode getNext(BreakingMode current, ItemStack stack, boolean reinforced) {
			if (reinforced) {
				int i = values.indexOf(current) + 1;
				if (i >= values.size()) {
					i = 0;
				}
				return values.get(i);
			} else {
				int i = notReinforced.indexOf(current) + 1;
				if (i >= notReinforced.size()) {
					i = 0;
				}
				return notReinforced.get(i);
			}
		}

		private static BreakingMode fromString(String type) {
			for (BreakingMode mode : BreakingMode.values) {
				if (mode.getString().equalsIgnoreCase(type)) {
					return mode;
				}
			}
			return null;
		}

		private int getOrder() {
			return order;
		}

		@Override
		public String getString() {
			return this.name;
		}

		public TranslationTextComponent getTranslatedString() {
			return new TranslationTextComponent(AssortedTools.MODID + ".wand.mode." + this.name);
		}
	}
}
