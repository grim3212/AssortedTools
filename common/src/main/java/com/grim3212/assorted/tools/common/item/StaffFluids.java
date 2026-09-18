package com.grim3212.assorted.tools.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Pours a fluid the way a bucket of it would, through the vanilla bucket's own {@code
 * emptyContents}: waterlogging, water boiling away in the Nether and the sounds all come with it.
 */
final class StaffFluids {

    private StaffFluids() {
    }

    static InteractionResult place(UseOnContext context, Fluid fluid, int cost) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos clicked = context.getClickedPos();
        BlockHitResult hit = new BlockHitResult(context.getClickLocation(), context.getClickedFace(), clicked, context.isInside());
        BlockPos placePos = fluid == Fluids.WATER && level.getBlockState(clicked).getBlock() instanceof LiquidBlockContainer ? clicked : clicked.relative(context.getClickedFace());

        if (!level.mayInteract(player, clicked) || !player.mayUseItemAt(placePos, context.getClickedFace(), context.getItemInHand())) {
            return InteractionResult.FAIL;
        }

        if (!level.isClientSide()) {
            BucketItem bucket = (BucketItem) (fluid == Fluids.WATER ? Items.WATER_BUCKET : Items.LAVA_BUCKET);
            if (!bucket.emptyContents(player, level, placePos, hit)) {
                return InteractionResult.FAIL;
            }

            ItemStack stack = context.getItemInHand();
            stack.hurtAndBreak(cost, player, context.getHand());
        }

        return InteractionResult.SUCCESS;
    }
}
