package com.grim3212.assorted.tools.common.handlers;

import com.grim3212.assorted.lib.core.fluid.FluidInformation;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.tools.common.fluid.FluidHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Fills or drains a fluid container item using a Dispenser.
 */
public class DispenseBucketHandler extends DefaultDispenseItemBehavior {
    private static final DispenseBucketHandler INSTANCE = new DispenseBucketHandler();

    public static DispenseBucketHandler getInstance() {
        return INSTANCE;
    }

    private DispenseBucketHandler() {
    }

    /**
     * {@code DefaultDispenseItemBehavior#dispense} is final; {@code execute} is the hook.
     */
    @Override
    @NotNull
    protected ItemStack execute(@NotNull BlockSource source, @NotNull ItemStack stack) {
        BlockPos front = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
        FluidInformation bucketful = new FluidInformation(FluidHelper.pickupableFluid(source.level(), front), Services.FLUIDS.getBucketAmount());

        // Room is decided before the pickup: FluidHelper#tryPickupFluid removes the source block as
        // it answers, so a container asked afterwards has already swallowed what it cannot hold.
        if (bucketful.fluid() != Fluids.EMPTY && Services.FLUIDS.simulateInsert(stack, bucketful) >= bucketful.amount()) {
            return fillContainer(source, front, stack);
        }

        return dumpContainer(source, stack);
    }

    /**
     * Picks up the fluid in front of a Dispenser and fills a container with it.
     */
    @NotNull
    private ItemStack fillContainer(@NotNull BlockSource source, @NotNull BlockPos front, @NotNull ItemStack stack) {
        Optional<FluidInformation> picked = FluidHelper.tryPickupFluid(null, source.level(), front);
        if (picked.isEmpty()) {
            return super.execute(source, stack);
        }

        // consumeWithRemainder shrinks the dispensed stack by one and puts the result back, or
        // dispenses it if there is no room.
        return this.consumeWithRemainder(source, stack, Services.FLUIDS.insertInto(stack, picked.get().withSource()));
    }

    /**
     * Drains a filled container and places the fluid in front of the Dispenser.
     */
    @NotNull
    private ItemStack dumpContainer(BlockSource source, @NotNull ItemStack stack) {
        ItemStack singleStack = stack.copy();
        singleStack.setCount(1);
        FluidInformation fluidHandler = Services.FLUIDS.get(singleStack).orElse(null);
        if (fluidHandler == null || fluidHandler.fluid() == Fluids.EMPTY) {
            return super.execute(source, stack);
        }

        Direction dispenserFacing = source.state().getValue(DispenserBlock.FACING);
        BlockPos blockpos = source.pos().relative(dispenserFacing);

        if (!FluidHelper.tryPlaceFluid(null, source.level(), blockpos, null, fluidHandler)) {
            return super.execute(source, stack);
        }

        ItemStack drainedStack = Services.FLUIDS.extractFrom(singleStack, Services.FLUIDS.getBucketAmount());
        return this.consumeWithRemainder(source, stack, drainedStack);
    }
}
