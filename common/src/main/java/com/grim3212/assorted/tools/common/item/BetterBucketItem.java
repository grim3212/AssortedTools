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
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

public class BetterBucketItem extends Item implements ITiered {

    /**
     * The key the stored fluid lives under inside the stack's {@code minecraft:custom_data}. Each
     * loader's fluid handler goes through the static accessors below.
     */
    public static final String FLUID_TAG = "Fluid";
    private static final String FLUID_NAME_KEY = "FluidName";
    private static final String AMOUNT_KEY = "Amount";

    public final ItemTierConfig tierHolder;

    public BetterBucketItem(Properties props, ItemTierConfig tierHolder) {
        super(props.stacksTo(1));

        this.tierHolder = tierHolder;

        DispenserBlock.registerBehavior(this, DispenseBucketHandler.getInstance());
    }

    public ItemStack getEmptyStack() {
        ItemStack stack = new ItemStack(this);
        storeFluid(stack, Fluids.EMPTY, 0);
        return stack;
    }

    @Override
    public ItemTierConfig getTierHolder() {
        return tierHolder;
    }

    @Override
    public void onCraftedBy(ItemStack stack, Player playerIn) {
        storeFluid(stack, Fluids.EMPTY, 0);
    }

    public int getMaximumMillibuckets() {
        return this.tierHolder.getMaxBuckets() * getBucketAmount();
    }

    /**
     * {@code Item.appendHoverText} is marked deprecated in 26.x - tooltips are meant to come from
     * data components implementing {@code TooltipProvider} - but it is still the only per item
     * hook, and vanilla's own items still override it.
     */
    @SuppressWarnings("deprecation")
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flagIn) {
        if (getAmount(stack) <= 0) {
            tooltip.accept(Component.translatable("tooltip.buckets.empty"));
        } else {
            tooltip.accept(Component.translatable("tooltip.buckets.contains", getAmount(stack) / BetterBucketItem.getBucketAmount(), getMaximumMillibuckets() / BetterBucketItem.getBucketAmount()));
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
    public InteractionResult use(Level worldIn, Player playerIn, InteractionHand hand) {
        ItemStack itemStackIn = playerIn.getItemInHand(hand);

        boolean canContainMore = getAmount(itemStackIn) < getMaximumMillibuckets();

        // clicked on a block?
        BlockHitResult blockhitresult = getPlayerPOVHitResult(worldIn, playerIn, canContainMore ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);

        if (blockhitresult.getType() == HitResult.Type.MISS) {
            return InteractionResult.PASS;
        } else if (blockhitresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResult.PASS;
        } else {
            BlockPos clickPos = blockhitresult.getBlockPos();
            Direction direction = blockhitresult.getDirection();
            BlockPos clickPosOffset = clickPos.relative(direction);

            if (canContainMore) {
                Optional<FluidInformation> filledResult = FluidHelper.tryPickupFluid(playerIn, worldIn, blockhitresult);
                if (!filledResult.isEmpty()) {
                    // Don't change if in creative
                    // Also if it isn't a complete bucket then don't add it either
                    if (playerIn.isCreative()) {
                        return InteractionResult.SUCCESS;
                    }

                    int filledAmount = getAmount(itemStackIn) + (int) filledResult.get().amount();

                    if (filledAmount > this.getMaximumMillibuckets()) {
                        filledAmount = this.getMaximumMillibuckets();
                    }

                    int leftover = filledAmount % getBucketAmount();
                    int newFillAmount = ToolsCommonMod.COMMON_CONFIG.allowPartialBucketAmounts.get() ? filledAmount : filledAmount - leftover;
                    storeFluid(itemStackIn, filledResult.get(), newFillAmount);

                    return InteractionResult.SUCCESS;
                }
            }

            if (worldIn.mayInteract(playerIn, clickPos)) {

                // For placement we want to make sure we don't account for the fluids that might
                // get in the way of the block
                // that we are targeting
                blockhitresult = getPlayerPOVHitResult(worldIn, playerIn, ClipContext.Fluid.NONE);
                if (blockhitresult.getType() == HitResult.Type.MISS) {
                    return InteractionResult.PASS;
                } else if (blockhitresult.getType() != HitResult.Type.BLOCK) {
                    return InteractionResult.PASS;
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
                                    return InteractionResult.SUCCESS.heldItemTransformedTo(this.tryBreakBucket(itemStackIn));
                                }
                            }
                        }
                    }
                }
            }
        }
        // couldn't place liquid there
        return InteractionResult.PASS;

    }

    public static int getBucketAmount() {
        //Currently set to an int might change to a long
        return (int) Services.FLUIDS.getBucketAmount();
    }

    /** Whether the stack holds no fluid or {@code toCheck}. */
    public static boolean isEmptyOrContains(ItemStack stack, String toCheck) {
        return getFluid(stack).equals(emptyMarker()) || getFluid(stack).equals(toCheck);
    }

    /**
     * {@code isSolid()} and {@code liquid()} are deprecated with no replacement, but vanilla's
     * {@code BucketItem.emptyContents} asks exactly these, and any other test would change which
     * blocks a bucket may flood.
     */
    @SuppressWarnings("deprecation")
    public boolean tryPlaceFluid(Player player, FluidInformation fluid, Level worldIn, BlockPos pos, BlockHitResult hitResult) {
        if (fluid.fluid() != Fluids.EMPTY) {
            // Handle vanilla differently
            if (fluid.fluid() == Fluids.WATER || fluid.fluid() == Fluids.LAVA) {
                BlockState iblockstate = worldIn.getBlockState(pos);
                boolean flag = !iblockstate.isSolid();
                boolean flag1 = iblockstate.canBeReplaced(fluid.fluid());

                if (!worldIn.isEmptyBlock(pos) && !flag && !flag1) {
                    return false;
                } else {
                    // DimensionType.ultraWarm is gone. Whether water boils away is a per position
                    // environment attribute now, so a datapack can give any dimension that behaviour.
                    if (worldIn.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, pos) && fluid.fluid() == Fluids.WATER) {
                        int l = pos.getX();
                        int i = pos.getY();
                        int j = pos.getZ();
                        worldIn.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (worldIn.getRandom().nextFloat() - worldIn.getRandom().nextFloat()) * 0.8F);

                        for (int k = 0; k < 8; ++k) {
                            worldIn.addParticle(ParticleTypes.LARGE_SMOKE, (double) l + Math.random(), (double) i + Math.random(), (double) j + Math.random(), 0.0D, 0.0D, 0.0D);
                        }
                    } else {
                        if (!worldIn.isClientSide() && (flag || flag1) && !iblockstate.liquid()) {
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

    /**
     * The crafting remainder: the same bucket with one bucket's worth taken out, or whatever
     * {@link #tryBreakBucket} leaves once it runs dry. Null means no remainder.
     */
    private @Nullable ItemStackTemplate craftingRemainder(ItemStack stack) {
        if (getAmount(stack) < getBucketAmount()) {
            return null;
        }

        ItemStack remainder = stack.copy();
        setAmount(remainder, getAmount(remainder) - getBucketAmount());

        ItemStack result = this.tryBreakBucket(remainder);
        return result.isEmpty() ? null : ItemStackTemplate.fromStack(result);
    }

    @LoaderImplement(loader = LoaderImplement.Loader.FORGE, value = "IItemExtension")
    public @Nullable ItemStackTemplate getCraftingRemainder(ItemInstance instance) {
        return instance instanceof ItemStack stack ? this.craftingRemainder(stack) : this.getCraftingRemainder();
    }

    @LoaderImplement(loader = LoaderImplement.Loader.FABRIC, value = "FabricItem")
    public @Nullable ItemStackTemplate getCraftingRemainder(ItemStack stack) {
        return this.craftingRemainder(stack);
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
            // Tier#getRepairIngredient is gone; a material's repair material is a TagKey<Item> now,
            // so "what is this bucket made of" is answered straight off the tag.
            for (Holder<Item> repairMaterial : BuiltInRegistries.ITEM.getTagOrEmpty(this.tierHolder.material().repairItems())) {
                return new ItemStack(repairMaterial, 2);
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

    public static String emptyMarker() {
        return getStringFromFluid(Fluids.EMPTY);
    }

    public static String getStringFromFluid(Fluid f) {
        return Services.PLATFORM.getRegistry(Registries.FLUID).getRegistryName(f).toString();
    }

    public static Fluid getFluidFromString(String s) {
        return Services.PLATFORM.getRegistry(Registries.FLUID).getValue(Identifier.parse(s)).orElse(Fluids.EMPTY);
    }

    public static void store(ItemStack stack, String toStore, int amount) {
        write(stack, toStore, amount);
    }

    public static void storeFluid(ItemStack stack, Fluid fluid, int amount) {
        store(stack, getStringFromFluid(fluid), amount);
    }

    public static void storeFluid(ItemStack stack, FluidInformation information, int amount) {
        storeFluid(stack, information.fluid(), amount);
    }

    public static void storeFluid(ItemStack stack, FluidInformation information) {
        storeFluid(stack, information.fluid(), (int) information.amount());
    }

    public static void setAmount(ItemStack stack, int amount) {
        write(stack, getFluid(stack), amount);
    }

    /**
     * Custom data is immutable, so the sub tag read back here is a detached copy: it has to be
     * handed back through {@code putTag} for the change to reach the stack at all. The 1.20.1 code
     * mutated the tag in place and relied on that writing through, which no longer happens.
     */
    private static void write(ItemStack stack, String fluidName, int amount) {
        CompoundTag tag = NBTHelper.getTag(stack, FLUID_TAG);
        String stored = amount <= 0 || fluidName.isEmpty() ? emptyMarker() : fluidName;

        tag.putString(FLUID_NAME_KEY, stored);
        tag.putInt(AMOUNT_KEY, amount <= 0 ? 0 : amount);

        NBTHelper.putTag(stack, FLUID_TAG, tag);
    }

    /**
     * The stored fluid, or {@link #emptyMarker()} for a bucket that has never stored one - a bucket
     * from the creative tab or a command never went through {@link #onCraftedBy}, and read as "" it
     * matched no fluid at all, so it could not be milked or filled.
     */
    public static String getFluid(ItemStack stack) {
        String stored = NBTHelper.getString(NBTHelper.getTag(stack, FLUID_TAG), FLUID_NAME_KEY);
        return stored.isEmpty() ? emptyMarker() : stored;
    }

    public static int getAmount(ItemStack stack) {
        return NBTHelper.getInt(NBTHelper.getTag(stack, FLUID_TAG), AMOUNT_KEY);
    }
}
