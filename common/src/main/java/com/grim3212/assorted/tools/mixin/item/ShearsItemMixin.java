package com.grim3212.assorted.tools.mixin.item;

import com.grim3212.assorted.tools.api.ToolsTags;
import com.grim3212.assorted.tools.common.enchantment.ToolsEnchantments;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShearsItem.class)
public class ShearsItemMixin {

    @Inject(method = "getDestroySpeed", at = @At("HEAD"), cancellable = true)
    public void assortedtools_getDestroySpeed(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
        if (ToolsEnchantments.hasCoralCutter(stack) && state.is(ToolsTags.Blocks.ALL_CORALS)) {
            cir.setReturnValue(10.0F);
        }
    }

    @Inject(method = "mineBlock", at = @At("HEAD"), cancellable = true)
    public void assortedtools_mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (ToolsEnchantments.hasCoralCutter(stack) && state.is(ToolsTags.Blocks.ALL_CORALS)) {
            if (!level.isClientSide && !state.is(BlockTags.FIRE)) {
                stack.hurtAndBreak(1, entity, (e) -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            }
            cir.setReturnValue(true);
        }
    }
}
