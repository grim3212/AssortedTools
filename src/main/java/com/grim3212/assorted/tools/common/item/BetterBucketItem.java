package com.grim3212.assorted.tools.common.item;

import java.util.List;

import com.grim3212.assorted.tools.common.handler.DispenseBucketHandler;
import com.grim3212.assorted.tools.common.handler.ItemTierHolder;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import com.grim3212.assorted.tools.common.util.NBTHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class BetterBucketItem extends Item {

	private boolean milkPause = false;
	public final ItemTierHolder tierHolder;
	private final boolean isExtraMaterial;

	public BetterBucketItem(Properties props, ItemTierHolder tierHolder) {
		this(props, tierHolder, false);
	}

	public BetterBucketItem(Properties props, ItemTierHolder tierHolder, boolean isExtraMaterial) {
		super(props.stacksTo(1));

		this.tierHolder = tierHolder;

		this.isExtraMaterial = isExtraMaterial;

		DispenserBlock.registerBehavior(this, DispenseBucketHandler.getInstance());
	}

	public ItemStack getEmptyStack() {
		ItemStack stack = new ItemStack(this);
		setFluid(stack, "empty");
		setAmount(stack, 0);
		return stack;
	}

	@Override
	public void onCraftedBy(ItemStack stack, Level worldIn, Player playerIn) {
		setFluid(stack, "empty");
		setAmount(stack, 0);
	}

	@Override
	protected boolean allowedIn(CreativeModeTab group) {
		if (this.isExtraMaterial) {
			return ToolsConfig.COMMON.betterBucketsEnabled.get() && ToolsConfig.COMMON.extraMaterialsEnabled.get() ? super.allowedIn(group) : false;
		}

		return ToolsConfig.COMMON.betterBucketsEnabled.get() ? super.allowedIn(group) : false;
	}

	public void pauseForMilk() {
		milkPause = true;
	}

	public int getMaximumMillibuckets() {
		return this.tierHolder.getMaxBuckets() * FluidType.BUCKET_VOLUME;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if (getAmount(stack) <= 0) {
			tooltip.add(Component.translatable("tooltip.buckets.empty"));
		} else {
			tooltip.add(Component.translatable("tooltip.buckets.contains", getAmount(stack), getMaximumMillibuckets()));
		}
	}

	public FluidStack getFluidStack(ItemStack container) {
		CompoundTag tagCompound = container.getOrCreateTag();
		if (tagCompound == null) {
			return FluidStack.EMPTY;
		}
		return FluidStack.loadFluidStackFromNBT(NBTHelper.getTag(tagCompound, FluidHandlerItemStack.FLUID_NBT_KEY));
	}

	@Override
	public Component getName(ItemStack stack) {
		FluidStack fluidStack = getFluidStack(stack);

		if (!fluidStack.isEmpty()) {
			return Component.translatable("item.assortedtools." + ForgeRegistries.ITEMS.getKey(stack.getItem()).getPath() + "_filled", fluidStack.getDisplayName());
		}

		return super.getName(stack);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand hand) {
		ItemStack itemStackIn = playerIn.getItemInHand(hand);

		boolean canContainMore = getAmount(itemStackIn) < getMaximumMillibuckets();
		if (milkPause) {
			milkPause = false;
			return InteractionResultHolder.success(itemStackIn);
		}

		// IFluidHandler fluidHandler = Utils.getFluidHandler(itemStackIn);

		// clicked on a block?
		BlockHitResult blockhitresult = getPlayerPOVHitResult(worldIn, playerIn, canContainMore ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
		InteractionResultHolder<ItemStack> ret = ForgeEventFactory.onBucketUse(playerIn, worldIn, itemStackIn, blockhitresult);

		if (ret != null)
			return ret;
		if (blockhitresult.getType() == HitResult.Type.MISS) {
			return InteractionResultHolder.pass(itemStackIn);
		} else if (blockhitresult.getType() != HitResult.Type.BLOCK) {
			return InteractionResultHolder.pass(itemStackIn);
		} else {
			BlockPos clickPos = blockhitresult.getBlockPos();
			Direction direction = blockhitresult.getDirection();
			BlockPos clickPosOffset = clickPos.relative(direction);

			if (canContainMore) {
				FluidActionResult filledResult = FluidUtil.tryPickUpFluid(itemStackIn, playerIn, worldIn, clickPos, direction);

				if (filledResult.isSuccess()) {

					// Don't change if in creative
					// Also if it isn't a complete bucket then don't add it either
					if (playerIn.isCreative()) {
						return InteractionResultHolder.success(itemStackIn);
					}

					if (!ToolsConfig.COMMON.allowPartialBucketAmounts.get()) {
						int filledAmount = getAmount(filledResult.getResult());

						int leftover = filledAmount % FluidType.BUCKET_VOLUME;

						if (leftover != 0) {
							// Remove the leftovers so we have a perfect bucket amount again
							setAmount(itemStackIn, filledAmount - leftover);
							return InteractionResultHolder.success(itemStackIn);
						}
					}

					return InteractionResultHolder.success(filledResult.result);
				}
			}

			if (worldIn.mayInteract(playerIn, clickPos)) {

				// For placement we want to make sure we don't account for the fluids that might
				// get in the way of the block
				// that we are targeting
				blockhitresult = getPlayerPOVHitResult(worldIn, playerIn, ClipContext.Fluid.NONE);
				if (blockhitresult.getType() == HitResult.Type.MISS) {
					return InteractionResultHolder.pass(itemStackIn);
				} else if (blockhitresult.getType() != HitResult.Type.BLOCK) {
					return InteractionResultHolder.pass(itemStackIn);
				} else {
					// Get new values
					clickPos = blockhitresult.getBlockPos();
					direction = blockhitresult.getDirection();
					clickPosOffset = clickPos.relative(direction);

					// can the player place there?
					if (playerIn.mayUseItemAt(clickPosOffset, direction, itemStackIn)) {
						int amount = getAmount(itemStackIn);
						if (amount >= FluidType.BUCKET_VOLUME) {
							FluidStack fluidStack = getFluidStack(itemStackIn);

							// try placing liquid
							if (!fluidStack.isEmpty()) {
								if (this.tryPlaceFluid(playerIn, fluidStack, worldIn, clickPosOffset, itemStackIn, hand) && !playerIn.isCreative()) {
									// success!
									playerIn.awardStat(Stats.ITEM_USED.get(this));
									setAmount(itemStackIn, amount - FluidType.BUCKET_VOLUME);
									return InteractionResultHolder.success(this.tryBreakBucket(itemStackIn));
								}
							}
						}
					}
				}
			}
		}
		// couldn't place liquid there2
		return InteractionResultHolder.pass(itemStackIn);

	}

	/**
	 * Check to see if an ItemStack contains empty or the type in its stored NBT
	 * 
	 * @param stack The ItemStack to check
	 * @param type  The type to check on the ItemStack
	 * @return True if the ItemStack contains empty or the type in NBT
	 */
	public static boolean isEmptyOrContains(ItemStack stack, String type) {
		return getFluid(stack).equals("empty") || getFluid(stack).equals(type);
	}

	public boolean tryPlaceBlock(Player player, Block block, Level worldIn, BlockPos pos) {
		if (block == null) {
			return false;
		}

		Material material = worldIn.getBlockState(pos).getMaterial();
		boolean isSolid = material.isSolid();

		// can only place in air or non-solid blocks
		if (!worldIn.isEmptyBlock(pos) && isSolid) {
			return false;
		}

		// water goes poof?
		if (!worldIn.isClientSide && !isSolid && !material.isLiquid()) {
			worldIn.destroyBlock(pos, true);
		}

		worldIn.playSound(player, pos, block == Blocks.FIRE ? SoundEvents.FLINTANDSTEEL_USE : SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
		worldIn.setBlockAndUpdate(pos, block.defaultBlockState());
		return true;
	}

	public boolean tryPlaceFluid(Player player, FluidStack block, Level worldIn, BlockPos pos, ItemStack stack, InteractionHand hand) {
		if (!block.isEmpty()) {
			// Handle vanilla differently
			if (block.getFluid() == Fluids.WATER || block.getFluid() == Fluids.LAVA) {
				BlockState iblockstate = worldIn.getBlockState(pos);
				Material material = iblockstate.getMaterial();
				boolean flag = !material.isSolid();
				boolean flag1 = iblockstate.canBeReplaced(block.getFluid());

				if (!worldIn.isEmptyBlock(pos) && !flag && !flag1) {
					return false;
				} else {
					if (worldIn.dimensionType().ultraWarm() && block.getFluid() == Fluids.WATER) {
						int l = pos.getX();
						int i = pos.getY();
						int j = pos.getZ();
						worldIn.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (worldIn.random.nextFloat() - worldIn.random.nextFloat()) * 0.8F);

						for (int k = 0; k < 8; ++k) {
							worldIn.addParticle(ParticleTypes.LARGE_SMOKE, (double) l + Math.random(), (double) i + Math.random(), (double) j + Math.random(), 0.0D, 0.0D, 0.0D);
						}
					} else {
						if (!worldIn.isClientSide && (flag || flag1) && !material.isLiquid()) {
							worldIn.destroyBlock(pos, true);
						}

						worldIn.playSound(player, pos, block.getFluid() == Fluids.WATER ? SoundEvents.BUCKET_EMPTY : SoundEvents.BUCKET_EMPTY_LAVA, SoundSource.BLOCKS, 1.0F, 1.0F);
						// Specify exactly which blocks to place
						worldIn.setBlockAndUpdate(pos, block.getFluid() == Fluids.WATER ? Blocks.WATER.defaultBlockState() : Blocks.LAVA.defaultBlockState());
					}

					return true;
				}
			} else {
				return FluidUtil.tryPlaceFluid(player, worldIn, hand, pos, stack, block).isSuccess();
			}
		}
		return false;
	}

	@Override
	public boolean hasCraftingRemainingItem(ItemStack stack) {
		return getAmount(stack) >= FluidType.BUCKET_VOLUME;
	}

	@Override
	public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
		int amount = getAmount(itemStack);
		setAmount(itemStack, amount - FluidType.BUCKET_VOLUME);

		return this.tryBreakBucket(itemStack);
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		// Don't show if the bucket is empty
		if (getAmount(stack) <= 0)
			return false;
		return true;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		// Get remainder calculations from stored and maxAmount
		int reversedAmount = getMaximumMillibuckets() - getAmount(stack);
		return Math.round(13.0F - (float) reversedAmount * 13.0F / (float) getMaximumMillibuckets());
	}

	@Override
	public int getBarColor(ItemStack stack) {
		float f = Math.max(0.0F, (float) getAmount(stack) / (float) getMaximumMillibuckets());
		return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
	}

	public ItemStack getBreakStack() {
		if (this.tierHolder.getBreaksAfterUse()) {
			ItemStack[] repairMaterial = this.tierHolder.getDefaultTier().getRepairIngredient().getItems();
			if (repairMaterial.length > 0) {
				return new ItemStack(repairMaterial[0].copy().getItem(), 2);
			}
		}
		return ItemStack.EMPTY;
	}

	public ItemStack tryBreakBucket(ItemStack stack) {
		if (getAmount(stack) <= 0) {
			if (this.tierHolder.getBreaksAfterUse()) {
				return this.getBreakStack();
			} else if (!this.getEmptyStack().isEmpty()) {
				return this.getEmptyStack().copy();
			} else {
				// Return empty if both are empty anyway
				return ItemStack.EMPTY;
			}
		}
		return stack.copy();
	}

	public static String getFluid(ItemStack stack) {
		CompoundTag tag = NBTHelper.getTag(stack, FluidHandlerItemStack.FLUID_NBT_KEY);
		return NBTHelper.getString(tag, "FluidName");
	}

	public static void setFluid(ItemStack stack, String fluidName) {
		CompoundTag tag = NBTHelper.getTag(stack, FluidHandlerItemStack.FLUID_NBT_KEY);
		NBTHelper.putString(tag, "FluidName", fluidName);
	}

	public static int getAmount(ItemStack stack) {
		CompoundTag tag = NBTHelper.getTag(stack, FluidHandlerItemStack.FLUID_NBT_KEY);
		return NBTHelper.getInt(tag, "Amount");
	}

	public static void setAmount(ItemStack stack, int amount) {
		CompoundTag tag = NBTHelper.getTag(stack, FluidHandlerItemStack.FLUID_NBT_KEY);
		NBTHelper.putInt(tag, "Amount", amount);

		if (amount <= 0) {
			NBTHelper.putString(tag, "FluidName", "empty");
			NBTHelper.putInt(tag, "Amount", 0);
		}
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
		return new BucketFluidHandler(stack, this.getBreakStack(), this.getEmptyStack(), getMaximumMillibuckets());
	}

	public static class BucketFluidHandler extends FluidHandlerItemStack {

		private final LazyOptional<IFluidHandlerItem> holder = LazyOptional.of(() -> this);

		private ItemStack empty = ItemStack.EMPTY;
		private ItemStack onBroken = ItemStack.EMPTY;

		public BucketFluidHandler(ItemStack container, ItemStack onBroken, ItemStack empty, int capacity) {
			super(container, capacity);

			this.empty = empty;
			this.onBroken = onBroken;
		}

		@Override
		public int fill(FluidStack resource, FluidAction action) {
			if (ToolsConfig.COMMON.allowPartialBucketAmounts.get()) {
				return super.fill(resource, action);
			} else {
				/*
				 * if (container.getItem() instanceof ItemBetterMilkBucket) { if
				 * (resource.getUnlocalizedName().equals("fluid.milk")) { return
				 * fillIncremental(resource, doFill); } else { return 0; } } else { return
				 * fillIncremental(resource, doFill); }
				 */
				return fillIncremental(resource, action);
			}
		}

		private int fillIncremental(FluidStack resource, FluidAction action) {
			if (container.getCount() != 1 || resource.isEmpty() || !canFillFluidType(resource)) {
				return 0;
			}

			FluidStack contained = getFluid();
			if (contained.isEmpty()) {
				int fillAmount = Math.min(capacity, resource.getAmount());
				int leftover = fillAmount % FluidType.BUCKET_VOLUME;

				if (leftover != 0) {
					// Account for offset and only fill in bucket increments
					fillAmount -= leftover;
				}

				if (action == FluidAction.EXECUTE) {
					FluidStack filled = resource.copy();
					filled.setAmount(fillAmount);
					setFluid(filled);
				}

				return fillAmount;
			} else {
				if (contained.isFluidEqual(resource)) {
					int fillAmount = Math.min(capacity - contained.getAmount(), resource.getAmount());
					int leftover = fillAmount % FluidType.BUCKET_VOLUME;

					if (leftover != 0) {
						// Account for offset and only fill in bucket increments
						fillAmount -= leftover;
					}

					if (action == FluidAction.EXECUTE && fillAmount > 0) {
						contained.setAmount(contained.getAmount() + fillAmount);
						setFluid(contained);
					}

					return fillAmount;
				}

				return 0;
			}
		}

		@Override
		public FluidStack drain(int maxDrain, FluidAction action) {
			if (ToolsConfig.COMMON.allowPartialBucketAmounts.get()) {
				return super.drain(maxDrain, action);
			} else {
				if (container.getCount() != 1 || maxDrain <= 0) {
					return FluidStack.EMPTY;
				}

				FluidStack contained = getFluid();
				if (contained.isEmpty() || !canDrainFluidType(contained)) {
					return FluidStack.EMPTY;
				}

				int drainAmount = Math.min(contained.getAmount(), maxDrain);
				int leftover = drainAmount % FluidType.BUCKET_VOLUME;

				if (leftover != 0) {
					// Account for offset and only drain in bucket increments
					drainAmount -= leftover;
				}

				FluidStack drained = contained.copy();
				drained.setAmount(drainAmount);

				if (action == FluidAction.EXECUTE) {
					contained.setAmount(contained.getAmount() - drainAmount);
					if (contained.getAmount() == 0) {
						setContainerToEmpty();
					} else {
						setFluid(contained);
					}
				}

				return drained;
			}
		}

		@Override
		protected void setContainerToEmpty() {
			super.setContainerToEmpty();

			if (!this.onBroken.isEmpty()) {
				container = this.onBroken.copy();
			} else {
				container = this.empty.copy();
			}
		}

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
			return CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.orEmpty(capability, holder);
		}
	}
}
