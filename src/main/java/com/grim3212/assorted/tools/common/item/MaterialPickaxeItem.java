package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.common.handler.ItemTierHolder;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import com.grim3212.assorted.tools.common.item.configurable.ConfigurablePickaxeItem;

import net.minecraft.world.item.CreativeModeTab;

public class MaterialPickaxeItem extends ConfigurablePickaxeItem {

	public MaterialPickaxeItem(ItemTierHolder tierHolder, Properties builder) {
		super(tierHolder, builder);
	}

	@Override
	protected boolean allowdedIn(CreativeModeTab group) {
		return ToolsConfig.COMMON.extraMaterialsEnabled.get() ? super.allowdedIn(group) : false;
	}
}
