package com.grim3212.assorted.tools.mixin.block;

import com.grim3212.assorted.tools.common.item.ToolsShears;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.TripWireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Lets the mod's shears disarm a tripwire on Fabric. See {@link BeehiveBlockMixin} for why this is
 * a redirect rather than a copy of the branch, and why it is Fabric only.
 * <p>
 * The 1.20.1 version overrode {@code playerWillDestroy} wholesale and called {@code super} itself,
 * which meant its return value drifted from vanilla's when the signature changed.
 */
@Mixin(TripWireBlock.class)
public abstract class TripWireBlockMixin {

    // The descriptor is `is(Object)`, not `is(Item)`: `is` lives on the generic
    // `TypedInstance<T>` interface now, so T erases to Object in the bytecode. Targeting
    // `is(Item)` compiles and then fails at load with a critical injection failure.
    @Redirect(method = "playerWillDestroy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z", ordinal = 0))
    private boolean assortedtools_shearsDisarm(ItemStack stack, Object item) {
        return ToolsShears.matches(stack, item);
    }
}
