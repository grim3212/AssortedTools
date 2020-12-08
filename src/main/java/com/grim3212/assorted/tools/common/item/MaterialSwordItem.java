package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.common.handler.ItemTierHolder;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import com.grim3212.assorted.tools.common.item.configurable.ConfigurableSwordItem;

import net.minecraft.item.ItemGroup;

public class MaterialSwordItem extends ConfigurableSwordItem {

	public MaterialSwordItem(ItemTierHolder tierHolder, Properties builder) {
		super(tierHolder, builder);
	}

	@Override
	protected boolean isInGroup(ItemGroup group) {
		return ToolsConfig.COMMON.extraMaterialsEnabled.get() ? super.isInGroup(group) : false;
	}

}