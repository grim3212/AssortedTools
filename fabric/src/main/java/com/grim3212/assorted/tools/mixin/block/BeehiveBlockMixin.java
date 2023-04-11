package com.grim3212.assorted.tools.mixin.block;

import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeehiveBlock.class)
public abstract class BeehiveBlockMixin {
    @Shadow
    protected abstract void resetHoneyLevel(Level level, BlockState state, BlockPos pos);

    @Shadow
    protected abstract void releaseBeesAndResetHoneyLevel(Level level, BlockState state, BlockPos pos, @Nullable Player player, BeehiveBlockEntity.BeeReleaseStatus beeReleaseStatus);

    @Shadow
    protected abstract boolean hiveContainsBees(Level level, BlockPos pos);

    @Shadow
    protected abstract void angerNearbyBees(Level level, BlockPos pos);

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 0), cancellable = true)
    private void use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof ShearsItem || stack.is(LibCommonTags.Items.SHEARS)) {
            level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
            BeehiveBlock.dropHoneycomb(level, pos);
            stack.hurtAndBreak(1, player, (playerx) -> {
                playerx.broadcastBreakEvent(hand);
            });
            level.gameEvent(player, GameEvent.SHEAR, pos);
            if (!level.isClientSide()) {
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            }

            if (!CampfireBlock.isSmokeyPos(level, pos)) {
                if (this.hiveContainsBees(level, pos)) {
                    this.angerNearbyBees(level, pos);
                }

                this.releaseBeesAndResetHoneyLevel(level, state, pos, player, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
            } else {
                this.resetHoneyLevel(level, state, pos);
            }

            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
        }
    }
}
