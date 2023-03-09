package com.grim3212.assorted.decor.common.fluid;

import com.grim3212.assorted.lib.core.fluid.FluidInformation;
import com.grim3212.assorted.lib.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.*;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FluidHelper {

    public static Optional<FluidInformation> tryPickupFluid(@Nullable Player playerIn, InteractionHand hand, Level level, BlockHitResult hitResult) {
        if (level == null || hitResult == null) {
            return Optional.empty();
        }

        BlockPos blockPos = hitResult.getBlockPos();
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.getBlock() instanceof BucketPickup) {
            BucketPickup bucketPickup = (BucketPickup) blockState.getBlock();
            ItemStack pickupBlock = bucketPickup.pickupBlock(level, blockPos, blockState);
            if (!pickupBlock.isEmpty()) {
                FluidState fluid = level.getFluidState(blockPos);
                if (!fluid.isEmpty()) {
                    bucketPickup.getPickupSound().ifPresent((sound) -> playerIn.playSound(sound, 1.0F, 1.0F));
                    return Optional.of(new FluidInformation(fluid.getType(), Services.FLUIDS.getBucketAmount()));
                }
            }
        }

        return Optional.empty();
    }

    public static boolean tryPlaceFluid(@Nullable Player player, Level level, BlockPos pos, @Nullable BlockHitResult hitResult, FluidInformation information) {
        Fluid content = information.fluid();
        if (!(content instanceof FlowingFluid))
            return false;

        BlockState blockState = level.getBlockState(pos);
        Block block = blockState.getBlock();
        Material material = blockState.getMaterial();
        boolean bl = blockState.canBeReplaced(content);
        boolean bl2 = blockState.isAir() || bl || block instanceof LiquidBlockContainer && ((LiquidBlockContainer) block).canPlaceLiquid(level, pos, blockState, content);
        if (!bl2) {
            return hitResult != null && tryPlaceFluid(player, level, hitResult.getBlockPos().relative(hitResult.getDirection()), null, information);
        } else if (level.dimensionType().ultraWarm() && content.is(FluidTags.WATER)) {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            level.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.8F);

            for (int l = 0; l < 8; ++l) {
                level.addParticle(ParticleTypes.LARGE_SMOKE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0, 0.0, 0.0);
            }

            return true;
        } else if (block instanceof LiquidBlockContainer && content == Fluids.WATER) {
            ((LiquidBlockContainer) block).placeLiquid(level, pos, blockState, ((FlowingFluid) content).getSource(false));
            playEmptySound(player, level, pos, information);
            return true;
        } else {
            if (!level.isClientSide() && bl && !material.isLiquid()) {
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
        SoundEvent soundEvent = content.is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
        level.playSound(player, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(player, GameEvent.FLUID_PLACE, pos);
    }
}
