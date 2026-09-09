package com.grim3212.assorted.tools.mixin.block;

import com.grim3212.assorted.tools.common.item.ToolsShears;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BeehiveBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Lets the mod's shears harvest a beehive on Fabric.
 * <p>
 * Vanilla hardcodes {@code itemStack.is(Items.SHEARS)}, so modded shears are invisible to it. This
 * redirects that one call rather than reimplementing the branch, which is what the 1.20.1 version
 * did - the old copy has to be kept in step with vanilla by hand, and it went stale the moment
 * {@code Block#use} became {@code useItemOn}.
 * <p>
 * Fabric only: NeoForge patches this same line to
 * {@code canPerformAction(ItemAbilities.SHEARS_HARVEST)}, which {@code MaterialShears} answers
 * through {@code ShearsItem}.
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
