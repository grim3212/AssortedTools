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
 * Exposes a better bucket's contents to NeoForge's fluid transfer API.
 * <p>
 * Rebuilt on {@link ItemAccessResourceHandler}. The old class extended
 * {@code FluidHandlerItemStack} and implemented {@code IFluidHandlerItem}, handing itself out
 * through a {@code LazyOptional} from {@code Item#initCapabilities} - and every one of those is
 * gone. Capabilities are transactional {@code ResourceHandler}s now, registered per item from
 * {@code RegisterCapabilitiesEvent}, so the mixin that used to add {@code initCapabilities} to
 * {@code BetterBucketItem} was deleted with it.
 * <p>
 * Deliberately <em>not</em> extending {@code ItemAccessFluidHandler}, which stores its contents in a
 * {@code SimpleFluidContent} component. This bucket keeps its fluid in its own tag, read by the
 * item's tooltip, the milking handler, the dispenser behaviour and the model dispatch, and the
 * Fabric side reads the same place. One storage, two loaders.
 * <p>
 * The superclass runs the transaction; these five methods only describe how a stack carries a
 * fluid. {@link #insert} and {@link #extract} are wrapped to keep the whole-bucket rounding that
 * {@code allowPartialBucketAmounts} controls.
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

    /**
     * Any real fluid, as long as the stack is still the bucket this handler was built for.
     * <p>
     * This was {@code canFillFluidType}, whose milk-bucket branch was unreachable:
     * {@code BetterMilkBucketItem} does not extend {@code BetterBucketItem}, and the capability is
     * only ever attached to the latter, so a milk bucket never had a fluid handler to ask. Dropped
     * rather than carried over, since the compiler rejects the {@code instanceof} outright.
     */
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
