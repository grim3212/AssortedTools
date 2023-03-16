package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.lib.annotations.LoaderImplement;
import com.grim3212.assorted.lib.core.fluid.FluidInformation;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.util.NBTHelper;
import com.grim3212.assorted.tools.ToolsCommonMod;
import com.grim3212.assorted.tools.api.item.ITiered;
import com.grim3212.assorted.tools.common.fluid.FluidHelper;
import com.grim3212.assorted.tools.common.handlers.DispenseBucketHandler;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
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

import java.util.List;
import java.util.Optional;

public class BetterBucketItem extends Item implements ITiered {

    private boolean milkPause = false;
    public final ItemTierConfig tierHolder;

    public BetterBucketItem(Properties props, ItemTierConfig tierHolder) {
        super(props.stacksTo(1));

        this.tierHolder = tierHolder;

        DispenserBlock.registerBehavior(this, DispenseBucketHandler.getInstance());
    }

    public ItemStack getEmptyStack() {
        ItemStack stack = new ItemStack(this);
        setFluid(stack, "empty");
        setAmount(stack, 0);
        return stack;
    }

    @Override
    public ItemTierConfig getTierHolder() {
        return tierHolder;
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level worldIn, Player playerIn) {
        setFluid(stack, "empty");
        setAmount(stack, 0);
    }

    public void pauseForMilk() {
        milkPause = true;
    }

    public int getMaximumMillibuckets() {
        return this.tierHolder.getMaxBuckets() * getBucketAmount();
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (getAmount(stack) <= 0) {
            tooltip.add(Component.translatable("tooltip.buckets.empty"));
        } else {
            tooltip.add(Component.translatable("tooltip.buckets.contains", getAmount(stack), getMaximumMillibuckets()));
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        Optional<FluidInformation> fluid = Services.FLUIDS.get(stack);
        if (!fluid.isEmpty() && fluid.get().fluid() != Fluids.EMPTY) {
            Component fluidName = Services.FLUIDS.getDisplayName(fluid.get().fluid());

            return Component.translatable("item.assortedtools." + Services.PLATFORM.getRegistry(Registries.ITEM).getRegistryName(stack.getItem()).getPath() + "_filled", fluidName);
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

        // clicked on a block?
        BlockHitResult blockhitresult = getPlayerPOVHitResult(worldIn, playerIn, canContainMore ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);

        if (blockhitresult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemStackIn);
        } else if (blockhitresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemStackIn);
        } else {
            BlockPos clickPos = blockhitresult.getBlockPos();
            Direction direction = blockhitresult.getDirection();
            BlockPos clickPosOffset = clickPos.relative(direction);

            if (canContainMore) {
                Optional<FluidInformation> filledResult = FluidHelper.tryPickupFluid(playerIn, hand, worldIn, blockhitresult);
                if (!filledResult.isEmpty()) {
                    // Don't change if in creative
                    // Also if it isn't a complete bucket then don't add it either
                    if (playerIn.isCreative()) {
                        return InteractionResultHolder.success(itemStackIn);
                    }


                    int filledAmount = getAmount(itemStackIn) + (int) filledResult.get().amount();

                    if (filledAmount > this.getMaximumMillibuckets()) {
                        filledAmount = this.getMaximumMillibuckets();
                    }

                    int leftover = filledAmount % getBucketAmount();
                    int newFillAmount = ToolsCommonMod.COMMON_CONFIG.allowPartialBucketAmounts.get() ? filledAmount : filledAmount - leftover;
                    setAmount(itemStackIn, newFillAmount);

                    return InteractionResultHolder.success(itemStackIn);
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
                        if (amount >= getBucketAmount()) {
                            Optional<FluidInformation> fluidInformation = Services.FLUIDS.get(itemStackIn);

                            // try placing liquid
                            if (!fluidInformation.isEmpty() && fluidInformation.get().fluid() != Fluids.EMPTY) {
                                if (this.tryPlaceFluid(playerIn, fluidInformation.get(), worldIn, clickPosOffset, blockhitresult) && !playerIn.isCreative()) {
                                    // success!
                                    playerIn.awardStat(Stats.ITEM_USED.get(this));
                                    setAmount(itemStackIn, amount - getBucketAmount());
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

    public int getBucketAmount() {
        //Currently set to an int might change to a long
        return (int) Services.FLUIDS.getBucketAmount();
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

    public boolean tryPlaceFluid(Player player, FluidInformation fluid, Level worldIn, BlockPos pos, BlockHitResult hitResult) {
        if (fluid.fluid() != Fluids.EMPTY) {
            // Handle vanilla differently
            if (fluid.fluid() == Fluids.WATER || fluid.fluid() == Fluids.LAVA) {
                BlockState iblockstate = worldIn.getBlockState(pos);
                Material material = iblockstate.getMaterial();
                boolean flag = !material.isSolid();
                boolean flag1 = iblockstate.canBeReplaced(fluid.fluid());

                if (!worldIn.isEmptyBlock(pos) && !flag && !flag1) {
                    return false;
                } else {
                    if (worldIn.dimensionType().ultraWarm() && fluid.fluid() == Fluids.WATER) {
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

                        worldIn.playSound(player, pos, fluid.fluid() == Fluids.WATER ? SoundEvents.BUCKET_EMPTY : SoundEvents.BUCKET_EMPTY_LAVA, SoundSource.BLOCKS, 1.0F, 1.0F);
                        // Specify exactly which blocks to place
                        worldIn.setBlockAndUpdate(pos, fluid.fluid() == Fluids.WATER ? Blocks.WATER.defaultBlockState() : Blocks.LAVA.defaultBlockState());
                    }

                    return true;
                }
            } else {
                return FluidHelper.tryPlaceFluid(player, worldIn, pos, hitResult, fluid);
            }
        }
        return false;
    }

    @LoaderImplement(loader = LoaderImplement.Loader.FORGE, value = "IForgeItem")
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        int amount = getAmount(itemStack);
        setAmount(itemStack, amount - getBucketAmount());

        return this.tryBreakBucket(itemStack);
    }

    @LoaderImplement(loader = LoaderImplement.Loader.FORGE, value = "IForgeItem")
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return getAmount(stack) >= getBucketAmount();
    }

    @LoaderImplement(loader = LoaderImplement.Loader.FABRIC, value = "FabricItem")
    public ItemStack getRecipeRemainder(ItemStack stack) {
        if (this.hasCraftingRemainingItem(stack)) {
            return this.getCraftingRemainingItem(stack);
        }

        return ItemStack.EMPTY;
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
        CompoundTag tag = NBTHelper.getTag(stack, Services.FLUIDS.fluidStackTag());
        return NBTHelper.getString(tag, "FluidName");
    }

    public static void setFluid(ItemStack stack, String fluidName) {
        CompoundTag tag = NBTHelper.getTag(stack, Services.FLUIDS.fluidStackTag());
        NBTHelper.putString(tag, "FluidName", fluidName);
    }

    public static int getAmount(ItemStack stack) {
        CompoundTag tag = NBTHelper.getTag(stack, Services.FLUIDS.fluidStackTag());
        return NBTHelper.getInt(tag, "Amount");
    }

    public static void setAmount(ItemStack stack, int amount) {
        CompoundTag tag = NBTHelper.getTag(stack, Services.FLUIDS.fluidStackTag());
        NBTHelper.putInt(tag, "Amount", amount);

        if (amount <= 0) {
            NBTHelper.putString(tag, "FluidName", "empty");
            NBTHelper.putInt(tag, "Amount", 0);
        }
    }
}
