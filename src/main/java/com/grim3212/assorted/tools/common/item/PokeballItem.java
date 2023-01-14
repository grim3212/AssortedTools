package com.grim3212.assorted.tools.common.item;

import java.util.List;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.entity.PokeballEntity;
import com.grim3212.assorted.tools.common.util.NBTHelper;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class PokeballItem extends Item {

	public PokeballItem(Properties properties) {
		super(properties.durability(10));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand hand) {
		ItemStack itemStackIn = playerIn.getItemInHand(hand);

		if (!worldIn.isClientSide) {
			PokeballEntity pokeball = new PokeballEntity(playerIn, worldIn, itemStackIn.copy());
			pokeball.shoot(playerIn.getLookAngle().x, playerIn.getLookAngle().y, playerIn.getLookAngle().z, 1.5F, 1.0F);
			worldIn.addFreshEntity(pokeball);
		}

		worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 0.5F, 0.4F / (worldIn.getRandom().nextFloat() * 0.4F + 0.8F));

		if (!playerIn.isCreative() || this.getEntityCompound(itemStackIn) != null) {
			return InteractionResultHolder.success(ItemStack.EMPTY);
		}

		return InteractionResultHolder.success(itemStackIn);
	}

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		CompoundTag entityTag = this.getEntityCompound(stack);

		if (entityTag != null) {
			String entityName = entityTag.getString("id");
			if (entityTag.contains("pokeball_name")) {
				entityName = entityTag.getString("pokeball_name");
			}

			if (entityTag.contains("CustomName")) {
				String s = entityTag.getString("CustomName");

				try {
					MutableComponent customName = Component.Serializer.fromJson(entityTag.getString("CustomName"));
					customName.withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC);
					tooltip.add(Component.translatable("tooltip.pokeball.stored_custom_name", customName, Component.translatable(entityName).withStyle(ChatFormatting.AQUA)));
				} catch (Exception exception) {
					AssortedTools.LOGGER.warn("Failed to parse entity custom name {}", s, exception);
					tooltip.add(Component.translatable("tooltip.pokeball.stored", Component.translatable(entityName).withStyle(ChatFormatting.AQUA)));
				}
			} else {
				tooltip.add(Component.translatable("tooltip.pokeball.stored", Component.translatable(entityName).withStyle(ChatFormatting.AQUA)));
			}
		} else {
			tooltip.add(Component.translatable("tooltip.pokeball.empty").withStyle(ChatFormatting.GRAY));
		}
	}

	public CompoundTag getEntityCompound(ItemStack stack) {
		return NBTHelper.hasTag(stack, "StoredEntity") ? NBTHelper.getTag(stack, "StoredEntity") : null;
	}
}
