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
import net.minecraft.world.level.material.Fluid;
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
     * {@code BlockSource} is a record now - {@code level()}, {@code pos()}, {@code state()} and
     * {@code blockEntity()} - and {@code DefaultDispenseItemBehavior#dispense} is final, with
     * {@code execute} the protected hook that does the work.
     */
    @Override
    @NotNull
    protected ItemStack execute(@NotNull BlockSource source, @NotNull ItemStack stack) {
        ServerLevel level = source.level();
        Direction dispenserFacing = source.state().getValue(DispenserBlock.FACING);
        BlockPos blockpos = source.pos().relative(dispenserFacing);

        if (Services.FLUIDS.get(stack).isPresent() && !Services.FLUIDS.get(stack).get().fluid().isSame(level.getFluidState(blockpos).getType())) {
            return dumpContainer(source, stack);
        } else {
            return fillContainer(source, stack);
        }
    }

    /**
     * Picks up fluid in front of a Dispenser and fills a container with it.
     */
    @NotNull
    private ItemStack fillContainer(@NotNull BlockSource source, @NotNull ItemStack stack) {
        ServerLevel level = source.level();
        Direction dispenserFacing = source.state().getValue(DispenserBlock.FACING);
        BlockPos blockpos = source.pos().relative(dispenserFacing);

        Fluid fluid = level.getFluidState(blockpos).getType();
        Optional<FluidInformation> fluidHandler = FluidHelper.tryPickupFluid(null, level, blockpos);
        if (fluidHandler.isEmpty()) {
            return super.execute(source, stack);
        }

        ItemStack filledStack = Services.FLUIDS.insertInto(stack, new FluidInformation(fluid, Services.FLUIDS.getBucketAmount()));

        // DispenserBlockEntity#addItem is gone (insertItem returns the leftover instead of a slot
        // index), and consumeWithRemainder is the vanilla helper that does exactly what both of
        // these branches used to hand-roll: shrink the dispensed stack by one and put the result
        // back in the dispenser, dispensing it if there is no room.
        return this.consumeWithRemainder(source, stack, filledStack);
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
