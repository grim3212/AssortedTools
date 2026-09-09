package com.grim3212.assorted.tools.mixin.block;

import com.grim3212.assorted.tools.common.item.ToolsShears;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.PumpkinBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Lets the mod's shears carve a pumpkin on Fabric. See {@link BeehiveBlockMixin} for why this is a
 * redirect of vanilla's hardcoded shears check rather than a copy of the branch, and why it is
 * Fabric only.
 */
@Mixin(PumpkinBlock.class)
public abstract class PumpkinBlockMixin {

    // The descriptor is `is(Object)`, not `is(Item)`: `is` lives on the generic
    // `TypedInstance<T>` interface now, so T erases to Object in the bytecode. Targeting
    // `is(Item)` compiles and then fails at load with a critical injection failure.
    @Redirect(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z", ordinal = 0))
    private boolean assortedtools_shearsCarve(ItemStack stack, Object item) {
        return ToolsShears.matches(stack, item);
    }
}
