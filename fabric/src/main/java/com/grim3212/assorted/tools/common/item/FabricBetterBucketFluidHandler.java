package com.grim3212.assorted.tools.common.item;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class FabricBetterBucketFluidHandler implements SingleSlotStorage<FluidVariant> {
    private final ContainerItemContext context;

    public FabricBetterBucketFluidHandler(ContainerItemContext context) {
        this.context = context;
    }

    @Override
    public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(resource, maxAmount);

        if (!(context.getItemVariant().getItem() instanceof BetterBucketItem)) return 0;

        var stack = context.getItemVariant().toStack();
        if (BetterBucketItem.isEmptyOrContains(stack, BetterBucketItem.getStringFromFluid(resource.getFluid()))) {
            // TODO: Fix this
            int amount = BetterBucketItem.getBucketAmount();
            ItemVariant newVariant = ItemVariant.of(stack);
            if (context.exchange(newVariant, 1, transaction) == 1) {
                return amount;
            }
        }

        return 0;
    }

    @Override
    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(resource, maxAmount);

        if (!(context.getItemVariant().getItem() instanceof BetterBucketItem)) return 0;

        ItemStack stack = context.getItemVariant().toStack();
        String stored = BetterBucketItem.getFluid(stack);
        if (stored.equals(BetterBucketItem.getStringFromFluid(resource.getFluid())) && !BetterBucketItem.getFluidFromString(stored).isSame(Fluids.EMPTY)) {
            // TODO: Fix this
            int amount = BetterBucketItem.getBucketAmount();
            ItemVariant newVariant = ItemVariant.of(stack);
            if (context.exchange(newVariant, 1, transaction) == 1) {
                return amount;
            }
        }

        return 0;
    }

    @Override
    public boolean isResourceBlank() {
        String stored = BetterBucketItem.getFluid(context.getItemVariant().toStack());
        return stored.equals(BetterBucketItem.emptyMarker());
    }

    @Override
    public FluidVariant getResource() {
        Fluid stored = BetterBucketItem.getFluidFromString(BetterBucketItem.getFluid(context.getItemVariant().toStack()));
        return FluidVariant.of(stored);
    }

    @Override
    public long getAmount() {
        return BetterBucketItem.getAmount(context.getItemVariant().toStack());
    }

    @Override
    public long getCapacity() {
        if (context.getItemVariant().getItem() instanceof BetterBucketItem bucketItem) {
            return bucketItem.getMaximumMillibuckets();
        }

        return BetterBucketItem.getBucketAmount();
    }
}
