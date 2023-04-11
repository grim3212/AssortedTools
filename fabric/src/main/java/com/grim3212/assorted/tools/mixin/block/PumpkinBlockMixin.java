package com.grim3212.assorted.tools.mixin.block;

import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.PumpkinBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PumpkinBlock.class)
public abstract class PumpkinBlockMixin {
    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 0), cancellable = true)
    private void use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof ShearsItem || stack.is(LibCommonTags.Items.SHEARS)) {
            if (!level.isClientSide) {
                Direction hitDir = hit.getDirection();
                Direction faceDir = hitDir.getAxis() == Direction.Axis.Y ? player.getDirection().getOpposite() : hitDir;
                level.playSound((Player) null, pos, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.setBlock(pos, Blocks.CARVED_PUMPKIN.defaultBlockState().setValue(CarvedPumpkinBlock.FACING, faceDir), 11);
                ItemEntity seeds = new ItemEntity(level, (double) pos.getX() + 0.5D + (double) faceDir.getStepX() * 0.65D, (double) pos.getY() + 0.1D, (double) pos.getZ() + 0.5D + (double) faceDir.getStepZ() * 0.65D, new ItemStack(Items.PUMPKIN_SEEDS, 4));
                seeds.setDeltaMovement(0.05D * (double) faceDir.getStepX() + level.random.nextDouble() * 0.02D, 0.05D, 0.05D * (double) faceDir.getStepZ() + level.random.nextDouble() * 0.02D);
                level.addFreshEntity(seeds);
                stack.hurtAndBreak(1, player, (levelx) -> levelx.broadcastBreakEvent(hand));
                level.gameEvent(player, GameEvent.SHEAR, pos);
                player.awardStat(Stats.ITEM_USED.get(Items.SHEARS));
            }

            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
        }
    }
}
