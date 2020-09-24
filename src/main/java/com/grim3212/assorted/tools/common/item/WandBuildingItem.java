package com.grim3212.assorted.tools.common.item;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import com.grim3212.assorted.tools.common.util.NBTHelper;
import com.grim3212.assorted.tools.common.util.WandCoord3D;

import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.material.PushReaction;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class WandBuildingItem extends WandItem {

	public WandBuildingItem(boolean reinforced, Properties props) {
		super(reinforced, props);
	}

	@Override
	protected boolean canBreak(World worldIn, BlockPos pos, ItemStack stack) {
		BlockState state = worldIn.getBlockState(pos);

		if (state.getMaterial().isReplaceable() || state.getMaterial().getPushReaction() == PushReaction.DESTROY || state.getMaterial().isLiquid())
			return true;

		switch (BuildingMode.fromString(NBTHelper.getString(stack, "Mode"))) {
		case BUILD_BOX:
		case BUILD_ROOM:
		case BUILD_FRAME:
		case BUILD_CAVES:
			return (state.getBlock() instanceof FlowingFluidBlock);
		case BUILD_WATER:
		case BUILD_LAVA:
			return (state.getBlock() == Blocks.TORCH || state.getBlock() instanceof FlowingFluidBlock);
		default:
			return false;
		}
	}

	@Override
	protected boolean isTooFar(int range, int maxDiff, int range2D, ItemStack stack) {
		switch (BuildingMode.fromString(NBTHelper.getString(stack, "Mode"))) {
		case BUILD_BOX:
		case BUILD_FRAME:
		case BUILD_ROOM:
		case BUILD_WATER:
		case BUILD_LAVA:
		case BUILD_TORCHES:
			return range - 400 > maxDiff;
		case BUILD_CAVES:
			return range2D - 1600 > maxDiff;
		}
		return true;
	}

	@Override
	protected double[] getParticleColor() {
		return new double[] { 1.0D, 0.8D, 0.0D };
	}

	@Override
	protected boolean isIncompatible(BlockState state) {
		return state.getBlock() == Blocks.AIR || state.getBlock() == Blocks.MOVING_PISTON || state.getBlock() == Blocks.PISTON_HEAD || state.getBlock() instanceof BedBlock || state.getBlock() instanceof DoorBlock || state.getBlock() instanceof AbstractSignBlock;
	}

	private boolean canPlace(World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand) {
		if (canBreak(world, pos, player.getHeldItem(hand))) {
			// Probably will need to be changed
			if (player.abilities.allowEdit)
				return true;
			if (state.getBlock() == Blocks.CACTUS || state.getBlock() == Blocks.SUGAR_CANE || state.getBlock() == Blocks.REDSTONE_WIRE || state.getBlock() instanceof PressurePlateBlock || state.getBlock() == Blocks.SNOW) {
				return false;
			}
			if (state.getBlock() instanceof TorchBlock || state.getBlock() instanceof FlowerBlock) {
				return false;
			}
			return true;
		}
		return false;
	}

	protected boolean consumeItems(ItemStack neededStack, PlayerEntity entityplayer, int neededItems, WandCoord3D end) {
		if (ToolsConfig.COMMON.freeBuildMode.get() || entityplayer.isCreative()) {
			return true;
		}
		int invItems = 0;
		// count items in inv.
		for (int t = 0; t < entityplayer.inventory.getSizeInventory(); t++) {
			ItemStack currentItem = entityplayer.inventory.getStackInSlot(t);
			if (!currentItem.isEmpty() && currentItem.isItemEqual(neededStack)) {
				invItems += currentItem.getCount();
				if (invItems == neededItems)
					break; // enough, no need to continue counting.
			}
		}
		if (neededItems > invItems) {
			sendMessage(entityplayer, new TranslationTextComponent("error.wand.toofewitems", neededItems, invItems));
			return false; // abort
		}
		// remove blocks from inventory, highest positions first (quickbar last)
		for (int t = entityplayer.inventory.getSizeInventory() - 1; t >= 0; t--) {
			ItemStack currentItem = entityplayer.inventory.getStackInSlot(t);
			if (!currentItem.isEmpty() && currentItem.isItemEqual(neededStack)) {
				int stackSize = currentItem.getCount();
				if (stackSize < neededItems) {
					entityplayer.inventory.setInventorySlotContents(t, ItemStack.EMPTY);
					neededItems -= stackSize;
				} else if (stackSize >= neededItems) {
					entityplayer.inventory.decrStackSize(t, neededItems);
					neededItems = 0;
					break;
				}
			}
		}
		return true;
	}

	private boolean emptyBuckets(PlayerEntity entityplayer, int neededItems, boolean lava) {
		Item vanillaBucket = lava ? Items.LAVA_BUCKET : Items.WATER_BUCKET;

		if (ToolsConfig.COMMON.freeBuildMode.get() || entityplayer.isCreative()) {
			return true;
		}
		int itemsInInventory = 0;
		for (int t = 0; t < entityplayer.inventory.getSizeInventory(); t++) {
			ItemStack currentItem = entityplayer.inventory.getStackInSlot(t);
			if (!currentItem.isEmpty()) {
				if (currentItem.getItem() == vanillaBucket) {
					itemsInInventory++;
				}
			}
		}
		// ? error - not enough items!
		if (itemsInInventory < neededItems) {
			return false;
		}
		// remove buckets from inventory, highest positions first (quickbar
		// last)
		for (int t = entityplayer.inventory.getSizeInventory() - 1; t >= 0; t--) {
			ItemStack currentItem = entityplayer.inventory.getStackInSlot(t);
			if (!currentItem.isEmpty()) {
				if (currentItem.getItem() == vanillaBucket) {
					entityplayer.inventory.setInventorySlotContents(t, ItemStack.EMPTY);
					entityplayer.inventory.addItemStackToInventory(new ItemStack(Items.BUCKET));
					if (--neededItems == 0)
						return true;
				}
			}
		}
		return false;
	}

	@Override
	protected boolean doEffect(World world, PlayerEntity entityplayer, Hand hand, WandCoord3D start, WandCoord3D end, BlockState state) {
		BuildingMode mode = BuildingMode.fromString(NBTHelper.getString(entityplayer.getHeldItem(hand), "Mode"));

		if (state != this.stateClicked && (mode == BuildingMode.BUILD_BOX || mode == BuildingMode.BUILD_ROOM || mode == BuildingMode.BUILD_FRAME || mode == BuildingMode.BUILD_TORCHES)) {
			error(entityplayer, end, "notsamecorner");
			return false;
		}
		boolean flag = doBuilding(world, start, end, mode, entityplayer, hand, state);
		if (flag && mode != BuildingMode.BUILD_WATER && mode != BuildingMode.BUILD_LAVA)
			world.playSound((PlayerEntity) null, end.pos, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.BLOCKS, (world.rand.nextFloat() + 0.7F) / 2.0F, 1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.4F);
		if (flag && mode == BuildingMode.BUILD_WATER)
			world.playSound((PlayerEntity) null, end.pos, SoundEvents.ENTITY_PLAYER_SPLASH, SoundCategory.BLOCKS, (world.rand.nextFloat() + 0.7F) / 2.0F, 1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.4F);
		if (flag && mode == BuildingMode.BUILD_WATER)
			world.playSound((PlayerEntity) null, end.pos, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, (world.rand.nextFloat() + 0.7F) / 2.0F, 1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.4F);

		return flag;
	}

	private boolean doBuilding(World world, WandCoord3D start, WandCoord3D end, BuildingMode mode, PlayerEntity entityplayer, Hand hand, BlockState state) {
		int X = 0;
		int Y = 0;
		int Z = 0;
		BlockState stateAt = Blocks.AIR.getDefaultState();
		ItemStack neededStack = getNeededItem(world, state, entityplayer);
		int multiplier = getNeededCount(state);
		int neededItems = 0;
		int affected = 0;
		switch (mode) {
		case BUILD_BOX:
			neededItems = 0;

			for (X = start.pos.getX(); X <= end.pos.getX(); X++) {
				for (Z = start.pos.getZ(); Z <= end.pos.getZ(); Z++) {
					for (Y = start.pos.getY(); Y <= end.pos.getY(); Y++) {
						if (canPlace(world, new BlockPos(X, Y, Z), state, entityplayer, hand)) {
							neededItems += multiplier;
						}
					}
				}

			}

			if (neededItems == 0) {
				if (!world.isRemote)
					error(entityplayer, end, "nowork");
				return false;
			}

			if (consumeItems(neededStack, entityplayer, neededItems, end)) {
				for (X = start.pos.getX(); X <= end.pos.getX(); X++) {
					for (Z = start.pos.getZ(); Z <= end.pos.getZ(); Z++) {
						for (Y = start.pos.getY(); Y <= end.pos.getY(); Y++) {
							BlockPos newPos = new BlockPos(X, Y, Z);

							if (canPlace(world, newPos, state, entityplayer, hand)) {
								world.setBlockState(newPos, state, 3);
								if (this.rand.nextInt(neededItems / 50 + 1) == 0)
									particles(world, newPos, 0);
								affected++;
							}
						}
					}
				}

				if ((this.stateOrig.getBlock() == Blocks.GRASS) && (affected > 0)) {
					for (int run = 0; run <= 1; run++) {
						if (run == 0)
							Y = start.pos.getY();
						if (run == 1) {
							Y = end.pos.getY();
						}
						for (X = start.pos.getX(); X <= end.pos.getX(); X++) {
							for (Z = start.pos.getZ(); Z <= end.pos.getZ(); Z++) {
								BlockPos newPos = new BlockPos(X, Y, Z);

								if ((world.getBlockState(newPos).getBlock() == Blocks.DIRT) && ((world.getBlockState(newPos.up()).getBlock() == null) || (!world.getBlockState(newPos.up()).isOpaqueCube(world, newPos.up())))) {
									world.setBlockState(newPos, Blocks.GRASS.getDefaultState());
								}
							}
						}
					}
				}

				return affected > 0;
			}
			return false;
		case BUILD_ROOM:
			neededItems = 0;

			for (X = start.pos.getX(); X <= end.pos.getX(); X++) {
				for (Z = start.pos.getZ(); Z <= end.pos.getZ(); Z++) {
					for (Y = start.pos.getY(); Y <= end.pos.getY(); Y++) {
						if (((X == start.pos.getX()) || (Y == start.pos.getY()) || (Z == start.pos.getZ()) || (X == end.pos.getX()) || (Y == end.pos.getY()) || (Z == end.pos.getZ())) && (canPlace(world, new BlockPos(X, Y, Z), state, entityplayer, hand))) {
							neededItems += multiplier;
						}
					}
				}

			}

			if (neededItems == 0) {
				if (!world.isRemote)
					error(entityplayer, end, "nowork");
				return false;
			}

			if (consumeItems(neededStack, entityplayer, neededItems, end)) {
				for (X = start.pos.getX(); X <= end.pos.getX(); X++) {
					for (Z = start.pos.getZ(); Z <= end.pos.getZ(); Z++) {
						for (Y = start.pos.getY(); Y <= end.pos.getY(); Y++) {
							BlockPos newPos = new BlockPos(X, Y, Z);
							if (((X == start.pos.getX()) || (Y == start.pos.getY()) || (Z == start.pos.getZ()) || (X == end.pos.getX()) || (Y == end.pos.getY()) || (Z == end.pos.getZ())) && (canPlace(world, newPos, state, entityplayer, hand))) {
								world.setBlockState(newPos, state, 3);
								if (this.rand.nextInt(neededItems / 50 + 1) == 0)
									particles(world, newPos, 0);
								affected++;
							}
						}
					}
				}

				if ((this.stateOrig.getBlock() == Blocks.GRASS) && (affected > 0)) {
					for (int run = 0; run <= 1; run++) {
						if (run == 0)
							Y = start.pos.getY();
						if (run == 1) {
							Y = end.pos.getY();
						}
						for (X = start.pos.getX(); X <= end.pos.getX(); X++) {
							for (Z = start.pos.getZ(); Z <= end.pos.getZ(); Z++) {
								BlockPos newPos = new BlockPos(X, Y, Z);

								if ((world.getBlockState(newPos).getBlock() == Blocks.DIRT) && ((world.getBlockState(newPos.up()).getBlock() == null) || (!world.getBlockState(newPos.up()).isOpaqueCube(world, newPos.up())))) {
									world.setBlockState(newPos, Blocks.GRASS.getDefaultState());
								}
							}
						}
					}
				}
				return affected > 0;
			}
			return false;
		case BUILD_FRAME:
			neededItems = 0;

			for (X = start.pos.getX(); X <= end.pos.getX(); X++) {
				for (Z = start.pos.getZ(); Z <= end.pos.getZ(); Z++) {
					for (Y = start.pos.getY(); Y <= end.pos.getY(); Y++) {
						if (((X == start.pos.getX()) && (Y == start.pos.getY())) || ((Y == start.pos.getY()) && (Z == start.pos.getZ())) || ((Z == start.pos.getZ()) && (X == start.pos.getX())) || ((X == start.pos.getX()) && (Y == end.pos.getY())) || ((X == end.pos.getX()) && (Y == start.pos.getY())) || ((Y == start.pos.getY()) && (Z == end.pos.getZ())) || ((Y == end.pos.getY()) && (Z == start.pos.getZ())) || ((Z == start.pos.getZ()) && (X == end.pos.getX()))
								|| ((Z == end.pos.getZ()) && (X == start.pos.getX())) || ((X == end.pos.getX()) && (Y == end.pos.getY())) || ((Y == end.pos.getY()) && (Z == end.pos.getZ())) || ((Z == end.pos.getZ()) && (X == end.pos.getX()) && (canPlace(world, new BlockPos(X, Y, Z), state, entityplayer, hand)))) {
							neededItems += multiplier;
						}
					}
				}

			}

			if (neededItems == 0) {
				if (!world.isRemote)
					error(entityplayer, end, "nowork");
				return false;
			}

			if (consumeItems(neededStack, entityplayer, neededItems, end)) {
				for (X = start.pos.getX(); X <= end.pos.getX(); X++) {
					for (Z = start.pos.getZ(); Z <= end.pos.getZ(); Z++) {
						for (Y = start.pos.getY(); Y <= end.pos.getY(); Y++) {
							if (((X == start.pos.getX()) && (Y == start.pos.getY())) || ((Y == start.pos.getY()) && (Z == start.pos.getZ())) || ((Z == start.pos.getZ()) && (X == start.pos.getX())) || ((X == start.pos.getX()) && (Y == end.pos.getY())) || ((X == end.pos.getX()) && (Y == start.pos.getY())) || ((Y == start.pos.getY()) && (Z == end.pos.getZ())) || ((Y == end.pos.getY()) && (Z == start.pos.getZ())) || ((Z == start.pos.getZ()) && (X == end.pos.getX()))
									|| ((Z == end.pos.getZ()) && (X == start.pos.getX())) || ((X == end.pos.getX()) && (Y == end.pos.getY())) || ((Y == end.pos.getY()) && (Z == end.pos.getZ())) || ((Z == end.pos.getZ()) && (X == end.pos.getX()))) {
								BlockPos newPos = new BlockPos(X, Y, Z);
								stateAt = world.getBlockState(newPos);

								if (canPlace(world, newPos, state, entityplayer, hand)) {
									world.setBlockState(newPos, state, 3);
									if (this.rand.nextInt(neededItems / 50 + 1) == 0)
										particles(world, newPos, 0);
									affected++;
								}
							}
						}
					}
				}

				if ((this.stateOrig.getBlock() == Blocks.GRASS) && (affected > 0)) {
					for (int run = 0; run <= 1; run++) {
						if (run == 0)
							Y = start.pos.getY();
						if (run == 1) {
							Y = end.pos.getY();
						}
						for (X = start.pos.getX(); X <= end.pos.getX(); X++) {
							for (Z = start.pos.getZ(); Z <= end.pos.getZ(); Z++) {
								BlockPos newPos = new BlockPos(X, Y, Z);
								if (((X == start.pos.getX()) && (Y == start.pos.getY())) || ((Y == start.pos.getY()) && (Z == start.pos.getZ())) || ((Z == start.pos.getZ()) && (X == start.pos.getX())) || ((X == start.pos.getX()) && (Y == end.pos.getY())) || ((X == end.pos.getX()) && (Y == start.pos.getY())) || ((Y == start.pos.getY()) && (Z == end.pos.getZ())) || ((Y == end.pos.getY()) && (Z == start.pos.getZ())) || ((Z == start.pos.getZ()) && (X == end.pos.getX()))
										|| ((Z == end.pos.getZ()) && (X == start.pos.getX())) || ((X == end.pos.getX()) && (Y == end.pos.getY())) || ((Y == end.pos.getY()) && (Z == end.pos.getZ())) || ((Z == end.pos.getZ()) && (X == end.pos.getX()) && (world.getBlockState(newPos).getBlock() == Blocks.DIRT) && ((world.getBlockState(newPos.up()).getBlock() == null) || (!world.getBlockState(newPos.up()).isOpaqueCube(world, newPos.up()))))) {
									world.setBlockState(newPos, Blocks.GRASS.getDefaultState());
								}
							}
						}
					}

				}

				return affected > 0;
			}
			return false;
		case BUILD_TORCHES:
			neededItems = 0;

			for (X = start.pos.getX(); X <= end.pos.getX(); X += 5) {
				for (Z = start.pos.getZ(); Z <= end.pos.getZ(); Z += 5) {
					for (Y = start.pos.getY(); Y <= end.pos.getY(); Y++) {
						BlockPos newPos = new BlockPos(X, Y, Z);
						stateAt = world.getBlockState(newPos);

						if (canPlace(world, newPos, state, entityplayer, hand)) {
							neededItems += multiplier;
						}
					}
				}
			}

			if (neededItems == 0) {
				if (!world.isRemote)
					error(entityplayer, end, "nowork");
				return false;
			}

			if (consumeItems(neededStack, entityplayer, neededItems, end)) {
				for (X = start.pos.getX(); X <= end.pos.getX(); X += 5) {
					for (Z = start.pos.getZ(); Z <= end.pos.getZ(); Z += 5) {
						for (Y = start.pos.getY(); Y <= end.pos.getY(); Y++) {
							BlockPos newPos = new BlockPos(X, Y, Z);
							stateAt = world.getBlockState(newPos);

							if (canPlace(world, newPos, state, entityplayer, hand)) {
								world.setBlockState(newPos, state, 3);
								particles(world, newPos, 0);
								affected++;
							}
						}
					}
				}
				return affected > 0;
			}
			return false;
		case BUILD_WATER:
			if ((!this.reinforced) && (!ToolsConfig.COMMON.freeBuildMode.get())) {
				error(entityplayer, end, "cantfillwater");
				return false;
			}

			if (!ToolsConfig.COMMON.freeBuildMode.get()) {
				neededItems = 0;

				for (X = start.pos.getX(); X <= end.pos.getX(); X++) {
					for (Z = start.pos.getZ(); Z <= end.pos.getZ(); Z++) {
						for (Y = start.pos.getY(); Y <= end.pos.getY(); Y++) {
							BlockPos newPos = new BlockPos(X, Y, Z);
							stateAt = world.getBlockState(newPos);

							if (canBreak(world, newPos, entityplayer.getHeldItem(hand))) {
								neededItems++;
							}
						}
					}
				}

				if (neededItems == 0) {
					if (!world.isRemote)
						error(entityplayer, end, "nowork");
					return false;
				}
			}

			if (emptyBuckets(entityplayer, 2, false)) {
				for (X = start.pos.getX(); X <= end.pos.getX(); X++) {
					for (Z = start.pos.getZ(); Z <= end.pos.getZ(); Z++) {
						for (Y = start.pos.getY(); Y <= end.pos.getY(); Y++) {
							BlockPos newPos = new BlockPos(X, Y, Z);
							stateAt = world.getBlockState(newPos);

							if (canBreak(world, newPos, entityplayer.getHeldItem(hand))) {
								world.setBlockState(newPos, Blocks.WATER.getDefaultState());
								affected++;
							}
						}
					}
				}

				if (affected == 0) {
					return false;
				}
				for (X = start.pos.getX(); X <= end.pos.getX(); X++) {
					for (Z = start.pos.getZ(); Z <= end.pos.getZ(); Z++) {
						for (Y = start.pos.getY(); Y <= end.pos.getY(); Y++) {
							BlockPos newPos = new BlockPos(X, Y, Z);
							stateAt = world.getBlockState(newPos);

							if (stateAt.getBlock() == Blocks.WATER) {
								world.notifyNeighborsOfStateChange(newPos, Blocks.WATER);
								if (world.getBlockState(newPos.up()).getBlock() == Blocks.AIR)
									particles(world, newPos, 2);
							}
						}
					}
				}
				return true;
			}
			error(entityplayer, end, "toofewwater");

			return false;
		case BUILD_LAVA:
			if ((!this.reinforced) && (!ToolsConfig.COMMON.freeBuildMode.get())) {
				error(entityplayer, end, "cantfilllava");
				return false;
			}

			neededItems = 0;

			if (!ToolsConfig.COMMON.freeBuildMode.get()) {
				for (X = start.pos.getX(); X <= end.pos.getX(); X++) {
					for (Z = start.pos.getZ(); Z <= end.pos.getZ(); Z++) {
						for (Y = start.pos.getY(); Y <= end.pos.getY(); Y++) {
							BlockPos newPos = new BlockPos(X, Y, Z);
							stateAt = world.getBlockState(newPos);

							if (canBreak(world, newPos, entityplayer.getHeldItem(hand))) {
								neededItems++;
							}
						}
					}
				}
				if (neededItems == 0) {
					if (!world.isRemote)
						error(entityplayer, end, "nowork");
					return false;
				}
			}

			if (emptyBuckets(entityplayer, neededItems, true)) {
				for (X = start.pos.getX(); X <= end.pos.getX(); X++) {
					for (Z = start.pos.getZ(); Z <= end.pos.getZ(); Z++) {
						for (Y = start.pos.getY(); Y <= end.pos.getY(); Y++) {
							BlockPos newPos = new BlockPos(X, Y, Z);
							stateAt = world.getBlockState(newPos);
							if (canBreak(world, newPos, entityplayer.getHeldItem(hand))) {
								world.setBlockState(newPos, Blocks.LAVA.getDefaultState());
								affected++;
							}
						}
					}
				}

				if (affected == 0) {
					return false;
				}
				for (X = start.pos.getX(); X <= end.pos.getX(); X++) {
					for (Z = start.pos.getZ(); Z <= end.pos.getZ(); Z++) {
						for (Y = start.pos.getY(); Y <= end.pos.getY(); Y++) {
							BlockPos newPos = new BlockPos(X, Y, Z);
							stateAt = world.getBlockState(newPos);
							if (stateAt.getBlock() == Blocks.LAVA) {
								world.notifyNeighborsOfStateChange(newPos, Blocks.LAVA);
							}
						}
					}
				}
				return true;
			}
			error(entityplayer, end, "toofewlava");

			return false;
		case BUILD_CAVES:
			if ((!this.reinforced) && (!ToolsConfig.COMMON.freeBuildMode.get())) {
				error(entityplayer, end, "cantfillcave");
				return false;
			}

			boolean underground = false;
			long cnt = 0L;
			for (X = start.pos.getX(); X <= end.pos.getX(); X++) {
				for (Z = start.pos.getZ(); Z <= end.pos.getZ(); Z++) {
					underground = false;
					for (Y = 127; Y > 1; Y--) {
						BlockPos newPos = new BlockPos(X, Y, Z);
						stateAt = world.getBlockState(newPos);

						boolean surfaceBlock = isSurface(stateAt);

						if ((!underground) && (surfaceBlock)) {
							underground = true;
						} else if (underground) {
							if (canBreak(world, newPos, entityplayer.getHeldItem(hand))) {
								world.setBlockState(newPos, Blocks.STONE.getDefaultState());
								cnt += 1L;
							}
						}
					}
				}
			}
			if (cnt > 0L) {
				if (!world.isRemote)
					sendMessage(entityplayer, new TranslationTextComponent("result.wand.fill", cnt));
				return true;
			} else {
				if (!world.isRemote)
					error(entityplayer, end, "nocave");
				return false;
			}
		}

		return false;
	}

	@Override
	public ItemStack cycleMode(PlayerEntity player, ItemStack stack) {
		BuildingMode mode = BuildingMode.fromString(NBTHelper.getString(stack, "Mode"));
		BuildingMode next = BuildingMode.getNext(mode, stack, reinforced);
		NBTHelper.putString(stack, "Mode", next.getString());
		this.sendMessage(player, new TranslationTextComponent(AssortedTools.MODID + ".wand.switched", next.getTranslatedString()));
		return stack;
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		BuildingMode mode = BuildingMode.fromString(NBTHelper.getString(stack, "Mode"));
		if (mode != null)
			tooltip.add(new TranslationTextComponent(AssortedTools.MODID + ".wand.current", mode.getTranslatedString()));
		else
			tooltip.add(new TranslationTextComponent(AssortedTools.MODID + ".wand.broken"));
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.isInGroup(group)) {
			ItemStack stack = new ItemStack(this);
			NBTHelper.putString(stack, "Mode", BuildingMode.BUILD_BOX.getString());
			items.add(stack);
		}
	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
		NBTHelper.putString(stack, "Mode", BuildingMode.BUILD_BOX.getString());
	}

	private static enum BuildingMode implements IStringSerializable {
		BUILD_BOX("buildbox", 0), BUILD_ROOM("buildroom", 1), BUILD_FRAME("buildframe", 2), BUILD_TORCHES("buildtorches", 3), BUILD_WATER("buildwater", 4, true), BUILD_LAVA("buildlava", 5, true), BUILD_CAVES("buildcaves", 6, true);

		private final String name;
		private final int order;
		private final boolean reinforcedOnly;

		private BuildingMode(String name, int order) {
			this(name, order, false);
		}

		private BuildingMode(String name, int order, boolean reinforcedOnly) {
			this.name = name;
			this.order = order;
			this.reinforcedOnly = reinforcedOnly;
		}

		private static final List<BuildingMode> values = Lists.newArrayList(values()).stream().sorted(Comparator.comparingInt(BuildingMode::getOrder)).collect(Collectors.toList());
		private static final List<BuildingMode> notReinforced = values.stream().filter((mode) -> !mode.reinforcedOnly).sorted(Comparator.comparingInt(BuildingMode::getOrder)).collect(Collectors.toList());

		private static BuildingMode getNext(BuildingMode current, ItemStack stack, boolean reinforced) {
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

		private static BuildingMode fromString(String type) {
			for (BuildingMode mode : BuildingMode.values) {
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
