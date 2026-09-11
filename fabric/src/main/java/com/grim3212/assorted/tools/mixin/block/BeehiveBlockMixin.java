package com.grim3212.assorted.tools.mixin.block;

import com.grim3212.assorted.tools.common.item.ToolsShears;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BeehiveBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Lets the mod's shears harvest a beehive on Fabric. Redirects vanilla's {@code is(Items.SHEARS)}
 * rather than copying the branch, so it stays in step with vanilla. NeoForge already patches this
 * line to {@code canPerformAction(ItemAbilities.SHEARS_HARVEST)}.
 */
@Mixin(BeehiveBlock.class)
public abstract class BeehiveBlockMixin {

    // The descriptor is `is(Object)`, not `is(Item)`: `is` lives on the generic
    // `TypedInstance<T>` interface now, so T erases to Object in the bytecode. Targeting
    // `is(Item)` compiles and then fails at load with a critical injection failure.
    // Ordinal 0 is the shears branch; ordinal 1 is the glass bottle one right after it.
    @Redirect(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z", ordinal = 0))
    private boolean assortedtools_shearsHarvest(ItemStack stack, Object item) {
        return ToolsShears.matches(stack, item);
    }
}
