package com.grim3212.assorted.tools.common.fluid;

import com.grim3212.assorted.lib.core.fluid.FluidInformation;
import com.grim3212.assorted.lib.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FluidHelper {

    public static Optional<FluidInformation> tryPickupFluid(@Nullable Player playerIn, Level level, BlockPos pos) {
        if (level == null || pos == null) {
            return Optional.empty();
        }

        BlockState blockState = level.getBlockState(pos);
        if (blockState.getBlock() instanceof BucketPickup) {
            BucketPickup bucketPickup = (BucketPickup) blockState.getBlock();
            ItemStack pickupBlock = bucketPickup.pickupBlock(playerIn, level, pos, blockState);
            if (!pickupBlock.isEmpty()) {
                FluidInformation fluid = Services.FLUIDS.get(pickupBlock).orElse(new FluidInformation(Fluids.EMPTY));
                if (!fluid.fluid().isSame(Fluids.EMPTY)) {
                    if (playerIn != null)
                        bucketPickup.getPickupSound().ifPresent((sound) -> playerIn.playSound(sound, 1.0F, 1.0F));
                    return Optional.of(new FluidInformation(fluid.fluid(), Services.FLUIDS.getBucketAmount()));
                }
            }
        }

        return Optional.empty();
    }

    public static Optional<FluidInformation> tryPickupFluid(@Nullable Player playerIn, Level level, BlockHitResult hitResult) {
        return tryPickupFluid(playerIn, level, hitResult.getBlockPos());
    }

    public static boolean tryPlaceFluid(@Nullable Player player, Level level, BlockPos pos, @Nullable BlockHitResult hitResult, FluidInformation information) {
        Fluid content = information.fluid();
        if (!(content instanceof FlowingFluid))
            return false;

        BlockState blockState = level.getBlockState(pos);
        Block block = blockState.getBlock();
        boolean bl = blockState.canBeReplaced(content);
        boolean bl2 = blockState.isAir() || bl || block instanceof LiquidBlockContainer && ((LiquidBlockContainer) block).canPlaceLiquid(player, level, pos, blockState, content);
        if (!bl2) {
            return hitResult != null && tryPlaceFluid(player, level, hitResult.getBlockPos().relative(hitResult.getDirection()), null, information);
            // Whether water boils off is an environment attribute sampled per position now, not a
            // flat dimensionType().ultraWarm() flag. Level#random is protected; getRandom() is the
            // accessor, and the particles now use it rather than Math.random() so they follow the
            // level's own random source like vanilla's bucket does.
        } else if (level.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, pos) && BuiltInRegistries.FLUID.wrapAsHolder(content).is(FluidTags.WATER)) {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            RandomSource random = level.getRandom();
            level.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F);

            for (int l = 0; l < 8; ++l) {
                level.addParticle(ParticleTypes.LARGE_SMOKE, (double) i + random.nextFloat(), (double) j + random.nextFloat(), (double) k + random.nextFloat(), 0.0, 0.0, 0.0);
            }

            return true;
        } else if (block instanceof LiquidBlockContainer && content == Fluids.WATER) {
            ((LiquidBlockContainer) block).placeLiquid(level, pos, blockState, ((FlowingFluid) content).getSource(false));
            playEmptySound(player, level, pos, information);
            return true;
        } else {
            // BlockStateBase#liquid() is @Deprecated with no replacement in the jar - vanilla's own
            // BucketItem#emptyContents still calls it here, so this mirrors it.
            @SuppressWarnings("deprecation")
            boolean replacingLiquid = blockState.liquid();
            if (!level.isClientSide() && bl && !replacingLiquid) {
                level.destroyBlock(pos, true);
            }

            if (!level.setBlock(pos, content.defaultFluidState().createLegacyBlock(), 11) && !blockState.getFluidState().isSource()) {
                return false;
            } else {
                playEmptySound(player, level, pos, information);
                return true;
            }
        }
    }

    protected static void playEmptySound(@Nullable Player player, LevelAccessor level, BlockPos pos, FluidInformation information) {
        Fluid content = information.fluid();
        SoundEvent soundEvent = BuiltInRegistries.FLUID.wrapAsHolder(content).is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
        level.playSound(player, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(player, GameEvent.FLUID_PLACE, pos);
    }
}
