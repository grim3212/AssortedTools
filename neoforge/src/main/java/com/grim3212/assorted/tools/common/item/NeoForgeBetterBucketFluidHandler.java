package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.ToolsCommonMod;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.transfer.ItemAccessResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.Nullable;

/**
 * Exposes a better bucket's contents to NeoForge's fluid transfer API. Not an
 * {@code ItemAccessFluidHandler}, which stores fluid in a {@code SimpleFluidContent} component: the
 * bucket keeps it in its own tag, which the tooltip, milking, dispenser, model and Fabric side all
 * read. {@link #insert} and {@link #extract} keep the {@code allowPartialBucketAmounts} rounding.
 */
public class NeoForgeBetterBucketFluidHandler extends ItemAccessResourceHandler<FluidResource> {

    private final BetterBucketItem bucket;
    private final ItemStack onBroken;
    private final ItemStack empty;
    private final int capacity;

    public NeoForgeBetterBucketFluidHandler(ItemAccess itemAccess, BetterBucketItem bucket, ItemStack onBroken, ItemStack empty, int capacity) {
        super(itemAccess, 1);
        this.bucket = bucket;
        this.onBroken = onBroken;
        this.empty = empty;
        this.capacity = capacity;
    }

    @Override
    protected FluidResource getResourceFrom(ItemResource accessResource, int index) {
        if (!accessResource.is(this.bucket)) {
            return FluidResource.EMPTY;
        }

        Fluid fluid = BetterBucketItem.getFluidFromString(BetterBucketItem.getFluid(accessResource.toStack()));
        return fluid == Fluids.EMPTY ? FluidResource.EMPTY : FluidResource.of(fluid);
    }

    @Override
    protected int getAmountFrom(ItemResource accessResource, int index) {
        return accessResource.is(this.bucket) ? BetterBucketItem.getAmount(accessResource.toStack()) : 0;
    }

    @Override
    protected @Nullable ItemResource update(ItemResource accessResource, int index, FluidResource newResource, int newAmount) {
        // Emptying a bucket that breaks on use hands back the broken remainder instead, which is
        // what setContainerToEmpty used to do.
        if (newAmount <= 0 || newResource.isEmpty()) {
            if (!this.onBroken.isEmpty()) {
                return ItemResource.of(this.onBroken);
            }

            return this.empty.isEmpty() ? ItemResource.of(accessResource.toStack()) : ItemResource.of(this.empty);
        }

        ItemStack updated = accessResource.toStack();
        BetterBucketItem.storeFluid(updated, newResource.getFluid(), newAmount);
        return ItemResource.of(updated);
    }

    /** Any real fluid, as long as the stack is still the bucket this handler was built for. */
    @Override
    public boolean isValid(int index, FluidResource resource) {
        return this.itemAccess.getResource().is(this.bucket) && resource.getFluid() != Fluids.EMPTY;
    }

    @Override
    protected int getCapacity(int index, FluidResource resource) {
        return this.capacity;
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
        return super.insert(index, resource, roundToBuckets(amount), transaction);
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
        return super.extract(index, resource, roundToBuckets(amount), transaction);
    }

    /**
     * Unless partial amounts are enabled, these buckets move fluid a whole bucket at a time.
     * Previously spelled out twice, in overrides of {@code fill} and {@code drain}.
     */
    private static int roundToBuckets(int amount) {
        if (ToolsCommonMod.COMMON_CONFIG.allowPartialBucketAmounts.get()) {
            return amount;
        }

        return amount - (amount % BetterBucketItem.getBucketAmount());
    }
}
