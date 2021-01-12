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
		super(properties.maxDamage(10));
	}

	@Override
	protected boolean isInGroup(ItemGroup group) {
		return ToolsConfig.COMMON.pokeballEnabled.get() ? super.isInGroup(group) : false;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand) {
		ItemStack itemStackIn = playerIn.getHeldItem(hand);

		if (!worldIn.isRemote) {
			PokeballEntity pokeball = new PokeballEntity(playerIn, worldIn, itemStackIn.copy());
			pokeball.shoot(playerIn.getLookVec().x, playerIn.getLookVec().y, playerIn.getLookVec().z, 1.5F, 1.0F);
			worldIn.addEntity(pokeball);
		}

		worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / (Item.random.nextFloat() * 0.4F + 0.8F));

		if (!playerIn.isCreative() || NBTHelper.hasTag(itemStackIn, "StoredEntity")) {
			return ActionResult.resultSuccess(ItemStack.EMPTY);
		}

		return ActionResult.resultSuccess(itemStackIn);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (NBTHelper.hasTag(stack, "StoredEntity")) {
			CompoundNBT stored = NBTHelper.getTag(stack, "StoredEntity");

			String entityName = stored.getString("id");
			if (stored.contains("pokeball_name")) {
				entityName = stored.getString("pokeball_name");
			}

			if (stored.contains("CustomName")) {
				String s = stored.getString("CustomName");

				try {
					IFormattableTextComponent customName = ITextComponent.Serializer.getComponentFromJson(stored.getString("CustomName"));
					customName.mergeStyle(TextFormatting.BLUE, TextFormatting.ITALIC);
					tooltip.add(new TranslationTextComponent("tooltip.pokeball.stored_custom_name", customName, new TranslationTextComponent(entityName).mergeStyle(TextFormatting.AQUA)));
				} catch (Exception exception) {
					AssortedTools.LOGGER.warn("Failed to parse entity custom name {}", s, exception);
					tooltip.add(new TranslationTextComponent("tooltip.pokeball.stored", new TranslationTextComponent(entityName).mergeStyle(TextFormatting.AQUA)));
				}
			} else {
				tooltip.add(new TranslationTextComponent("tooltip.pokeball.stored", new TranslationTextComponent(entityName).mergeStyle(TextFormatting.AQUA)));
			}
		} else {
			tooltip.add(new TranslationTextComponent("tooltip.pokeball.empty").mergeStyle(TextFormatting.GRAY));
		}
	}

}
