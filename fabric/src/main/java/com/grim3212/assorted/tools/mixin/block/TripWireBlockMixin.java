package com.grim3212.assorted.tools.mixin.block;

import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TripWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TripWireBlock.class)
public abstract class TripWireBlockMixin extends Block {
    public TripWireBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "playerWillDestroy", at = @At("HEAD"), cancellable = true)
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player, CallbackInfo ci) {
        if (!level.isClientSide && !player.getMainHandItem().isEmpty() && (player.getMainHandItem().getItem() instanceof ShearsItem || player.getMainHandItem().is(LibCommonTags.Items.SHEARS))) {
            level.setBlock(pos, state.setValue(TripWireBlock.DISARMED, true), Block.UPDATE_INVISIBLE);
            level.gameEvent(player, GameEvent.SHEAR, pos);
        }
        super.playerWillDestroy(level, pos, state, player);
        ci.cancel();
    }
}
