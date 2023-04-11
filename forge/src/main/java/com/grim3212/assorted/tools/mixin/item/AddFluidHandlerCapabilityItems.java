package com.grim3212.assorted.tools.mixin.item;

import com.grim3212.assorted.tools.common.item.BetterBucketItem;
import com.grim3212.assorted.tools.common.item.ForgeBetterBucketFluidHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BetterBucketItem.class)
public class AddFluidHandlerCapabilityItems extends Item {

    public AddFluidHandlerCapabilityItems(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if (!stack.isEmpty()) {
            if (stack.getItem() instanceof BetterBucketItem bucket) {
                return new ForgeBetterBucketFluidHandler(stack, bucket.getBreakStack(), bucket.getEmptyStack(), bucket.getMaximumMillibuckets());
            }
        }
        return null;
    }
}
