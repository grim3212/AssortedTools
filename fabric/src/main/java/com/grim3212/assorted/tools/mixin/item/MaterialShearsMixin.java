package com.grim3212.assorted.tools.mixin.item;

import com.grim3212.assorted.tools.common.item.MaterialShears;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MaterialShears.class)
public class MaterialShearsMixin extends ShearsItem {
    public MaterialShearsMixin(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        LivingEntity entity = interactionTarget;
        if (entity instanceof Shearable shearable) {
            if (shearable.readyForShearing()) {
                shearable.shear(SoundSource.PLAYERS);
                entity.gameEvent(GameEvent.SHEAR, player);

                if (!entity.level().isClientSide) {
                    stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(usedHand));
                }
            }
        }

        return InteractionResult.sidedSuccess(player.level().isClientSide);
    }
}
