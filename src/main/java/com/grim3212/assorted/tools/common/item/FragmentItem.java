package com.grim3212.assorted.tools.common.item;

import java.util.List;

import com.grim3212.assorted.tools.common.handler.ToolsConfig;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class FragmentItem extends Item {

	public FragmentItem(Item.Properties props) {
		super(props);
	}

	@Override
	protected boolean allowedIn(CreativeModeTab group) {
		return ToolsConfig.COMMON.ultimateFistEnabled.get() ? super.allowedIn(group) : false;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> texts, TooltipFlag flags) {
		texts.add(Component.translatable("tooltip.ultimate.fragment").withStyle(ChatFormatting.GRAY));

		super.appendHoverText(stack, level, texts, flags);
	}

}