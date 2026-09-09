package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.lib.util.NBTHelper;
import com.grim3212.assorted.tools.common.entity.PokeballEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

public class PokeballItem extends Item {

    public PokeballItem(Properties properties) {
        super(properties.durability(10));
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

        if (!playerIn.isCreative() || this.getEntityCompound(itemStackIn) != null) {
            // The ball itself becomes the thrown entity, so the hand is emptied.
            return InteractionResult.SUCCESS.heldItemTransformedTo(ItemStack.EMPTY);
        }

        return InteractionResult.SUCCESS;
    }

    /**
     * {@code Item.appendHoverText} is marked deprecated in 26.x - tooltips are meant to come from
     * data components implementing {@code TooltipProvider} - but it is still the only per item
     * hook, and vanilla's own items still override it.
     * <p>
     * TODO(26.2): moving this onto a component would mean giving the stored entity its own
     * DataComponentType instead of the CUSTOM_DATA tag {@code PokeballEntity} writes.
     */
    @SuppressWarnings("deprecation")
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flagIn) {
        CompoundTag entityTag = this.getEntityCompound(stack);

        if (entityTag == null) {
            tooltip.accept(Component.translatable("tooltip.pokeball.empty").withStyle(ChatFormatting.GRAY));
            return;
        }

        String entityName = entityTag.getStringOr("pokeball_name", entityTag.getStringOr("id", ""));
        Component storedName = Component.translatable(entityName).withStyle(ChatFormatting.AQUA);

        // An entity's custom name is written through ComponentSerialization now rather than as a
        // JSON string, so Component.Serializer - which no longer exists - is not what reads it back.
        Optional<Component> customName = entityTag.read("CustomName", ComponentSerialization.CODEC);

        if (customName.isPresent()) {
            tooltip.accept(Component.translatable("tooltip.pokeball.stored_custom_name", customName.get().copy().withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC), storedName));
        } else {
            tooltip.accept(Component.translatable("tooltip.pokeball.stored", storedName));
        }
    }

    public @Nullable CompoundTag getEntityCompound(ItemStack stack) {
        return NBTHelper.hasTag(stack, "StoredEntity") ? NBTHelper.getTag(stack, "StoredEntity") : null;
    }
}
