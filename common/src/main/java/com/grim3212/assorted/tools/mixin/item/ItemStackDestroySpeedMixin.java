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
 * Makes the Coral Cutter enchantment tear through coral. The {@code minecraft:tool} component is
 * static and cannot depend on an enchantment, and {@link ItemStack#getDestroySpeed(BlockState)} is
 * where stack and block meet. A mixin rather than a {@code MaterialShears} override because it
 * applies to vanilla shears too.
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
