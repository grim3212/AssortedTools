package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.common.handler.ModdedItemTierHolder;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import com.grim3212.assorted.tools.common.item.configurable.ConfigurableAxeItem;

import net.minecraft.item.ItemGroup;

public class MaterialAxeItem extends ConfigurableAxeItem {

	public MaterialAxeItem(ModdedItemTierHolder tierHolder, Properties builder) {
		super(tierHolder, builder);
	}

	@Override
	protected boolean isInGroup(ItemGroup group) {
		return ToolsConfig.COMMON.extraMaterialsEnabled.get() ? super.isInGroup(group) : false;
	}

}
