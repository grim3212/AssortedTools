package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.common.handler.ItemTierHolder;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import com.grim3212.assorted.tools.common.item.configurable.ConfigurableHoeItem;

import net.minecraft.item.ItemGroup;

public class MaterialHoeItem extends ConfigurableHoeItem {

	public MaterialHoeItem(ItemTierHolder tierHolder, Properties builder) {
		super(tierHolder, builder);
	}

	@Override
	protected boolean allowdedIn(ItemGroup group) {
		return ToolsConfig.COMMON.extraMaterialsEnabled.get() ? super.allowdedIn(group) : false;
	}

}
