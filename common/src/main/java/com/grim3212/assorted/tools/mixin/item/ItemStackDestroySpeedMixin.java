package com.grim3212.assorted.tools.mixin.item;

import com.grim3212.assorted.tools.api.ToolsTags;
import com.grim3212.assorted.tools.common.enchantment.ToolsEnchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Makes the Coral Cutter enchantment tear through coral.
 * <p>
 * This used to inject into {@code ShearsItem#getDestroySpeed}. That method no longer exists:
 * shears describe their mining speeds through the {@code minecraft:tool} data component, and
 * {@code ShearsItem} overrides nothing about speed at all. The component is static data, so an
 * enchantment cannot add a rule to it.
 * <p>
 * {@link ItemStack#getDestroySpeed(BlockState)} is the one place a stack and a block state meet to
 * produce a speed, which makes it the right and only place for a condition that depends on both
 * plus the stack's enchantments. The guard is a cheap item identity check, so every other stack in
 * the game pays one reference comparison.
 * <p>
 * The enchantment applies to vanilla shears as well as the mod's own, which is why this is a mixin
 * rather than an override on {@code MaterialShears}.
 */
@Mixin(ItemStack.class)
public class ItemStackDestroySpeedMixin {

    @Inject(method = "getDestroySpeed", at = @At("HEAD"), cancellable = true)
    private void assortedtools_coralCutterSpeed(BlockState state, CallbackInfoReturnable<Float> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getItem() instanceof ShearsItem && state.is(ToolsTags.Blocks.ALL_CORALS) && ToolsEnchantments.hasCoralCutter(stack)) {
            cir.setReturnValue(10.0F);
        }
    }
}
