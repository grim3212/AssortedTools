package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.ToolsCommonMod;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

public class ForgeBetterBucketFluidHandler extends FluidHandlerItemStack {

    private final LazyOptional<IFluidHandlerItem> holder = LazyOptional.of(() -> this);

    private ItemStack empty = ItemStack.EMPTY;
    private ItemStack onBroken = ItemStack.EMPTY;

    public ForgeBetterBucketFluidHandler(ItemStack container, ItemStack onBroken, ItemStack empty, int capacity) {
        super(container, capacity);

        this.empty = empty;
        this.onBroken = onBroken;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (ToolsCommonMod.COMMON_CONFIG.allowPartialBucketAmounts.get()) {
            return super.fill(resource, action);
        } else {
            return fillIncremental(resource, action);
        }
    }

    @Override
    public boolean canFillFluidType(FluidStack fluid) {
        return this.container.getItem() instanceof BetterMilkBucketItem ? ForgeMod.MILK.isPresent() && fluid.getFluid() == ForgeMod.MILK.get() : fluid.getFluid() != Fluids.EMPTY;
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
            if (contained.getFluid().isSame(resource.getFluid())) {
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
        if (ToolsCommonMod.COMMON_CONFIG.allowPartialBucketAmounts.get()) {
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
        return ForgeCapabilities.FLUID_HANDLER_ITEM.orEmpty(capability, holder);
    }
}
