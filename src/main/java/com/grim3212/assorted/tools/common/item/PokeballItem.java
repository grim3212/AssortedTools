package com.grim3212.assorted.tools.common.item;

import java.util.List;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.entity.PokeballEntity;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
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
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class PokeballItem extends Item {

	public PokeballItem(Properties properties) {
		super(properties.durability(10));
	}

	@Override
	protected boolean allowedIn(CreativeModeTab group) {
		return ToolsConfig.COMMON.pokeballEnabled.get() ? super.allowedIn(group) : false;
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

		if (!playerIn.isCreative() || NBTHelper.hasTag(itemStackIn, "StoredEntity")) {
			return InteractionResultHolder.success(ItemStack.EMPTY);
		}

		return InteractionResultHolder.success(itemStackIn);
	}

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if (NBTHelper.hasTag(stack, "StoredEntity")) {
			CompoundTag stored = NBTHelper.getTag(stack, "StoredEntity");

			String entityName = stored.getString("id");
			if (stored.contains("pokeball_name")) {
				entityName = stored.getString("pokeball_name");
			}

			if (stored.contains("CustomName")) {
				String s = stored.getString("CustomName");

				try {
					MutableComponent customName = Component.Serializer.fromJson(stored.getString("CustomName"));
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

}
