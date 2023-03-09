package com.grim3212.assorted.decor.common.handlers;

import com.grim3212.assorted.decor.common.fluid.FluidHelper;
import com.grim3212.assorted.lib.core.fluid.FluidInformation;
import com.grim3212.assorted.lib.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nonnull;

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

    private final DefaultDispenseItemBehavior dispenseBehavior = new DefaultDispenseItemBehavior();

    @Override
    @Nonnull
    public ItemStack execute(@Nonnull BlockSource source, @Nonnull ItemStack stack) {
        Level level = source.getLevel();
        Direction dispenserFacing = source.getBlockState().getValue(DispenserBlock.FACING);
        BlockPos blockpos = source.getPos().relative(dispenserFacing);

        if (Services.FLUIDS.get(stack).isPresent() && Services.FLUIDS.get(stack).get().fluid() != level.getFluidState(blockpos).getType()) {
            return dumpContainer(source, stack);
        } else {
            return fillContainer(source, stack);
        }
    }

    /**
     * Picks up fluid in front of a Dispenser and fills a container with it.
     */
    @Nonnull
    private ItemStack fillContainer(@Nonnull BlockSource source, @Nonnull ItemStack stack) {
        Level level = source.getLevel();
        Direction dispenserFacing = source.getBlockState().getValue(DispenserBlock.FACING);
        BlockPos blockpos = source.getPos().relative(dispenserFacing);


        FluidInformation fluidHandler = Services.FLUIDS.get(stack).orElse(null);
        Fluid fluid = level.getFluidState(blockpos).getType();
        if (fluidHandler == null || (fluidHandler.fluid() != Fluids.EMPTY && fluidHandler.fluid() != fluid)) {
            return super.execute(source, stack);
        }

        ItemStack filledStack = Services.FLUIDS.insertInto(stack, new FluidInformation(fluid, Services.FLUIDS.getBucketAmount()));

        if (stack.getCount() == 1) {
            return filledStack;
        } else if (((DispenserBlockEntity) source.getEntity()).addItem(filledStack) < 0) {
            this.dispenseBehavior.dispense(source, filledStack);
        }

        ItemStack stackCopy = stack.copy();
        stackCopy.shrink(1);
        return stackCopy;
    }

    /**
     * Drains a filled container and places the fluid in front of the Dispenser.
     */
    @Nonnull
    private ItemStack dumpContainer(BlockSource source, @Nonnull ItemStack stack) {
        ItemStack singleStack = stack.copy();
        singleStack.setCount(1);
        FluidInformation fluidHandler = Services.FLUIDS.get(singleStack).orElse(null);
        if (fluidHandler == null || fluidHandler.fluid() == Fluids.EMPTY) {
            return super.execute(source, stack);
        }


        Direction dispenserFacing = source.getBlockState().getValue(DispenserBlock.FACING);
        BlockPos blockpos = source.getPos().relative(dispenserFacing);

        boolean result = FluidHelper.tryPlaceFluid(null, source.getLevel(), blockpos, null, fluidHandler);

        if (result) {
            ItemStack drainedStack = Services.FLUIDS.extractFrom(singleStack, Services.FLUIDS.getBucketAmount());

            if (drainedStack.getCount() == 1) {
                return drainedStack;
            } else if (!drainedStack.isEmpty() && ((DispenserBlockEntity) source.getEntity()).addItem(drainedStack) < 0) {
                this.dispenseBehavior.dispense(source, drainedStack);
            }

            ItemStack stackCopy = drainedStack.copy();
            stackCopy.shrink(1);
            return stackCopy;
        } else {
            return this.dispenseBehavior.dispense(source, stack);
        }
    }


}
