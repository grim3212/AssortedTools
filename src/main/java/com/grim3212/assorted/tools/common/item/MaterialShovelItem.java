package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.common.handler.ItemTierHolder;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import com.grim3212.assorted.tools.common.item.configurable.ConfigurableShovelItem;

import net.minecraft.world.item.CreativeModeTab;

import net.minecraft.world.item.Item.Properties;

public class MaterialShovelItem extends ConfigurableShovelItem {

	public MaterialShovelItem(ItemTierHolder tierHolder, Properties builder) {
		super(tierHolder, builder);
	}

	@Override
	protected boolean allowdedIn(CreativeModeTab group) {
		return ToolsConfig.COMMON.extraMaterialsEnabled.get() ? super.allowdedIn(group) : false;
	}

}
