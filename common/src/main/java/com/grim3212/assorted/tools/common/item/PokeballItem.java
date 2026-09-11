package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.common.entity.PokeballEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


/**
 * Throws itself: an empty ball catches the first mob it hits, and a full one lets it out where it
 * lands. What it holds, and so its tooltip, is its {@link CapturedEntity} component.
 */
public class PokeballItem extends Item {

    public PokeballItem(Properties properties) {
        super(properties.durability(10).component(ToolsDataComponents.CAPTURED_ENTITY.get(), CapturedEntity.EMPTY));
    }

    @Override
    public InteractionResult use(Level worldIn, Player playerIn, InteractionHand hand) {
        ItemStack itemStackIn = playerIn.getItemInHand(hand);

        if (!worldIn.isClientSide()) {
            PokeballEntity pokeball = new PokeballEntity(playerIn, worldIn, itemStackIn.copy());
            pokeball.shoot(playerIn.getLookAngle().x, playerIn.getLookAngle().y, playerIn.getLookAngle().z, 1.5F, 1.0F);
            worldIn.addFreshEntity(pokeball);
        }

        worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 0.5F, 0.4F / (worldIn.getRandom().nextFloat() * 0.4F + 0.8F));

        if (!playerIn.isCreative() || !CapturedEntity.of(itemStackIn).isEmpty()) {
            // The ball itself becomes the thrown entity, so the hand is emptied.
            return InteractionResult.SUCCESS.heldItemTransformedTo(ItemStack.EMPTY);
        }

        return InteractionResult.SUCCESS;
    }
}
