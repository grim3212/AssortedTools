package com.grim3212.assorted.tools.common.item;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import com.grim3212.assorted.tools.common.util.NBTHelper;
import com.grim3212.assorted.tools.common.util.WandCoord3D;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class WandMiningItem extends WandItem {

	public WandMiningItem(boolean reinforced, Properties props) {
		super(reinforced, props);
	}

	private static boolean isMiningOre(BlockState state) {
		return ToolsConfig.COMMON.miningSurfaceBlocks.getLoadedStates().contains(state);
	}

	@Override
	protected boolean canBreak(Level worldIn, BlockPos pos, ItemStack stack) {
		BlockState state = worldIn.getBlockState(pos);

		switch (MiningMode.fromString(NBTHelper.getString(stack, "Mode"))) {
			case MINE_ALL:
				return (state.getBlock() != Blocks.BEDROCK || ToolsConfig.COMMON.bedrockBreaking.get()) && (state.getBlock() != Blocks.OBSIDIAN || ToolsConfig.COMMON.easyMiningObsidian.get());
			case MINE_DIRT:
				return (state.getBlock() == Blocks.GRASS || state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.SAND || state.getBlock() == Blocks.GRAVEL || state.getBlock() instanceof LeavesBlock || state.getBlock() == Blocks.FARMLAND || state.getBlock() == Blocks.SNOW || state.getBlock() == Blocks.SOUL_SAND || state.getBlock() == Blocks.VINE || state.getBlock() instanceof FlowerBlock);
			case MINE_WOOD:
				return state.getMaterial() == Material.WOOD;
			case MINE_ORES:
				return isMiningOre(state) && (state.getBlock() != Blocks.BEDROCK || ToolsConfig.COMMON.bedrockBreaking.get()) && (state.getBlock() != Blocks.OBSIDIAN || ToolsConfig.COMMON.easyMiningObsidian.get());
		}
		return false;
	}

	@Override
	protected boolean isTooFar(int range, int maxDiff, int range2D, ItemStack stack) {
		switch (MiningMode.fromString(NBTHelper.getString(stack, "Mode"))) {
			case MINE_ALL:
			case MINE_DIRT:
				return range - 250 > maxDiff;
			case MINE_WOOD:
				return range2D - 400 > maxDiff;
			case MINE_ORES:
				return range2D - 60 > maxDiff;
		}
		return true;
	}

	@Override
	protected double[] getParticleColor() {
		return new double[] { 0.01D, 0.8D, 1.0D };
	}

	@Override
	protected boolean doEffect(Level world, Player entityplayer, InteractionHand hand, WandCoord3D start, WandCoord3D end, BlockState state) {
		boolean damage = doMining(world, start, end, entityplayer, hand);
		if (damage)
			world.playSound((Player) null, end.pos, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 2.5F, 0.5F + world.random.nextFloat() * 0.3F);
		return damage;
	}

	private boolean doMining(Level world, WandCoord3D start, WandCoord3D end, Player entityplayer, InteractionHand hand) {
		ItemStack stack = entityplayer.getItemInHand(hand);
		MiningMode mode = MiningMode.fromString(NBTHelper.getString(stack, "Mode"));

		// MINING OBSIDIAN 1x1x1
		BlockPos startPos = start.pos;
		BlockPos endPos = end.pos;
		if (mode == MiningMode.MINE_ALL && startPos.equals(endPos) && !ToolsConfig.COMMON.easyMiningObsidian.get()) {
			BlockState state = world.getBlockState(startPos);
			if (state.getBlock() == Blocks.OBSIDIAN) {
				state.getBlock().playerWillDestroy(world, startPos, state, entityplayer);
				state.getBlock().playerDestroy(world, entityplayer, startPos, state, world.getBlockEntity(startPos), entityplayer.getUseItem());
				particles(world, startPos, 1);
				return true;
			}
		}
		int X, Y, Z;
		BlockState stateAt;
		int blocks2Dig = 0;
		boolean isFree = ToolsConfig.COMMON.bedrockBreaking.get() || entityplayer.isCreative();
		int max = (reinforced || isFree) ? 1024 : 512;
		// MINING ORES
		if (mode == MiningMode.MINE_ORES) {
			if (!this.reinforced) {
				error(entityplayer, end, "cantminesurface");
				return false;
			}

			// counting ores to mine
			List<BlockPos> iterable = BlockPos.betweenClosedStream(new BlockPos(start.pos.getX(), 1, start.pos.getZ()), new BlockPos(end.pos.getX(), end.pos.getY() + 1, end.pos.getZ())).map(BlockPos::immutable).collect(Collectors.toList());
			for (BlockPos pos : iterable) {
				stateAt = world.getBlockState(pos);
				if (isMiningOre(stateAt)) {
					// add more drops for redstone and lapis
					if (stateAt.getBlock() == Blocks.REDSTONE_ORE || stateAt.getBlock() == Blocks.LAPIS_ORE) {
						blocks2Dig += 4;
					} else {
						blocks2Dig++;
					}
				}
			}
			if (blocks2Dig - max > 10) {// 10 blocks tolerance
				this.sendMessage(entityplayer, new TranslatableComponent("error.wand.toomany", blocks2Dig, max));
				return true;
			}
			// harvesting the ores
			boolean underground;
			int surface = 127;
			long cnt = 0;
			boolean surfaceBlock;
			BlockPos pos;
			for (X = start.pos.getX(); X <= end.pos.getX(); X++) {
				for (Z = start.pos.getZ(); Z <= end.pos.getZ(); Z++) {
					underground = false;
					for (Y = 127; Y > 1; Y--) {
						pos = new BlockPos(X, Y, Z);
						stateAt = world.getBlockState(pos);
						if (!underground && world.isEmptyBlock(pos)) {
							surface = Y;
						}
						surfaceBlock = isSurface(stateAt);
						if (!underground && surfaceBlock)
							underground = true;
						if (isMiningOre(stateAt)) {
							BlockEntity tile = world.getBlockEntity(pos);
							if (world.setBlockAndUpdate(pos, Blocks.STONE.defaultBlockState())) {
								pos = new BlockPos(X, surface, Z);
								stateAt.getBlock().playerWillDestroy(world, pos, stateAt, entityplayer);
								stateAt.getBlock().playerDestroy(world, entityplayer, pos, stateAt, tile, entityplayer.getUseItem());
								cnt++;
							}
						}
					}
				}
			}
			if (cnt == 0) {
				if (!world.isClientSide)
					sendMessage(entityplayer, new TranslatableComponent("result.wand.mine"));
				return false;
			}
			return true;
		}
		// NORMAL MINING
		List<BlockPos> iterable = BlockPos.betweenClosedStream(startPos, endPos).map(BlockPos::immutable).collect(Collectors.toList());
		for (BlockPos pos : iterable) {
			stateAt = world.getBlockState(pos);
			if (canBreak(world, pos, stack)) {
				// add more drops for redstone and lapis
				if (stateAt.getBlock() == Blocks.REDSTONE_ORE || stateAt.getBlock() == Blocks.LAPIS_ORE) {
					blocks2Dig += 4;
				} else {
					blocks2Dig++;
				}
			}
		}
		if (blocks2Dig > max) {
			this.sendMessage(entityplayer, new TranslatableComponent("error.wand.toomany", blocks2Dig, (this.reinforced || isFree) ? 1024 : 512));
			return false;
		}
		// now the mining itself.
		if (blocks2Dig == 0) {
			if (!world.isClientSide)
				error(entityplayer, end, "nowork");
			return false;
		}
		iterable = BlockPos.betweenClosedStream(startPos, endPos).map(BlockPos::immutable).collect(Collectors.toList());
		for (Object object : iterable) {
			BlockPos newPos = (BlockPos) object;
			stateAt = world.getBlockState(newPos);
			if (canBreak(world, newPos, stack)) {
				BlockEntity tile = world.getBlockEntity(newPos);
				if (stateAt.getBlock().onDestroyedByPlayer(stateAt, world, newPos, entityplayer, true, world.getFluidState(newPos))) {
					stateAt.getBlock().playerWillDestroy(world, newPos, stateAt, entityplayer);
					stateAt.getBlock().playerDestroy(world, entityplayer, newPos, stateAt, tile, entityplayer.getUseItem());
					if (rand.nextInt(blocks2Dig / 50 + 1) == 0)
						particles(world, newPos, 1);
				}
			}
		}
		return true;
	}

	@Override
	public ItemStack cycleMode(Player player, ItemStack stack) {
		MiningMode mode = MiningMode.fromString(NBTHelper.getString(stack, "Mode"));
		MiningMode next = MiningMode.getNext(mode, stack, reinforced);
		NBTHelper.putString(stack, "Mode", next.getSerializedName());
		this.sendMessage(player, new TranslatableComponent(AssortedTools.MODID + ".wand.switched", next.getTranslatedString()));
		return stack;
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (this.allowdedIn(group)) {
			ItemStack stack = new ItemStack(this);
			NBTHelper.putString(stack, "Mode", MiningMode.MINE_ALL.getSerializedName());
			items.add(stack);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		MiningMode mode = MiningMode.fromString(NBTHelper.getString(stack, "Mode"));
		if (mode != null)
			tooltip.add(new TranslatableComponent(AssortedTools.MODID + ".wand.current", mode.getTranslatedString()));
		else
			tooltip.add(new TranslatableComponent(AssortedTools.MODID + ".broken"));
	}

	@Override
	public void onCraftedBy(ItemStack stack, Level worldIn, Player playerIn) {
		NBTHelper.putString(stack, "Mode", MiningMode.MINE_ALL.getSerializedName());
	}

	private static enum MiningMode implements StringRepresentable {
		MINE_ALL("mineall", 0),
		MINE_DIRT("minedirt", 1),
		MINE_WOOD("minewood", 2),
		MINE_ORES("mineores", 3, true);

		private final String name;
		private final int order;
		private final boolean reinforcedOnly;

		private MiningMode(String name, int order) {
			this(name, order, false);
		}

		private MiningMode(String name, int order, boolean reinforcedOnly) {
			this.name = name;
			this.order = order;
			this.reinforcedOnly = reinforcedOnly;
		}

		private static final List<MiningMode> values = Lists.newArrayList(values()).stream().sorted(Comparator.comparingInt(MiningMode::getOrder)).collect(Collectors.toList());
		private static final List<MiningMode> notReinforced = values.stream().filter((mode) -> !mode.reinforcedOnly).sorted(Comparator.comparingInt(MiningMode::getOrder)).collect(Collectors.toList());

		private static MiningMode getNext(MiningMode current, ItemStack stack, boolean reinforced) {
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

		private static MiningMode fromString(String type) {
			for (MiningMode mode : MiningMode.values) {
				if (mode.getSerializedName().equalsIgnoreCase(type)) {
					return mode;
				}
			}
			return null;
		}

		private int getOrder() {
			return order;
		}

		@Override
		public String getSerializedName() {
			return this.name;
		}

		public TranslatableComponent getTranslatedString() {
			return new TranslatableComponent(AssortedTools.MODID + ".wand.mode." + this.name);
		}
	}
}