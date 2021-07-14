package com.grim3212.assorted.tools.common.item;

import java.util.List;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.entity.PokeballEntity;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import com.grim3212.assorted.tools.common.util.NBTHelper;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class PokeballItem extends Item {

	public PokeballItem(Properties properties) {
		super(properties.durability(10));
	}

	@Override
	protected boolean allowdedIn(ItemGroup group) {
		return ToolsConfig.COMMON.pokeballEnabled.get() ? super.allowdedIn(group) : false;
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand hand) {
		ItemStack itemStackIn = playerIn.getItemInHand(hand);

		if (!worldIn.isClientSide) {
			PokeballEntity pokeball = new PokeballEntity(playerIn, worldIn, itemStackIn.copy());
			pokeball.shoot(playerIn.getLookAngle().x, playerIn.getLookAngle().y, playerIn.getLookAngle().z, 1.5F, 1.0F);
			worldIn.addFreshEntity(pokeball);
		}

		worldIn.playSound(playerIn, playerIn.blockPosition(), SoundEvents.ARROW_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / (Item.random.nextFloat() * 0.4F + 0.8F));

		if (!playerIn.isCreative() || NBTHelper.hasTag(itemStackIn, "StoredEntity")) {
			return ActionResult.success(ItemStack.EMPTY);
		}

		return ActionResult.success(itemStackIn);
	}

	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (NBTHelper.hasTag(stack, "StoredEntity")) {
			CompoundNBT stored = NBTHelper.getTag(stack, "StoredEntity");

			String entityName = stored.getString("id");
			if (stored.contains("pokeball_name")) {
				entityName = stored.getString("pokeball_name");
			}

			if (stored.contains("CustomName")) {
				String s = stored.getString("CustomName");

				try {
					IFormattableTextComponent customName = ITextComponent.Serializer.fromJson(stored.getString("CustomName"));
					customName.withStyle(TextFormatting.BLUE, TextFormatting.ITALIC);
					tooltip.add(new TranslationTextComponent("tooltip.pokeball.stored_custom_name", customName, new TranslationTextComponent(entityName).withStyle(TextFormatting.AQUA)));
				} catch (Exception exception) {
					AssortedTools.LOGGER.warn("Failed to parse entity custom name {}", s, exception);
					tooltip.add(new TranslationTextComponent("tooltip.pokeball.stored", new TranslationTextComponent(entityName).withStyle(TextFormatting.AQUA)));
				}
			} else {
				tooltip.add(new TranslationTextComponent("tooltip.pokeball.stored", new TranslationTextComponent(entityName).withStyle(TextFormatting.AQUA)));
			}
		} else {
			tooltip.add(new TranslationTextComponent("tooltip.pokeball.empty").withStyle(TextFormatting.GRAY));
		}
	}

}
