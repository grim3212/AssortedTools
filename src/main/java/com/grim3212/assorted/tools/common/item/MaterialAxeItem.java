package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.common.handler.ModdedItemTierHolder;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import com.grim3212.assorted.tools.common.item.configurable.ConfigurableAxeItem;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.ForgeRegistries;

public class MaterialAxeItem extends ConfigurableAxeItem {

	public MaterialAxeItem(ModdedItemTierHolder tierHolder, Properties builder) {
		super(tierHolder, builder);
	}

	@Override
	protected boolean allowedIn(CreativeModeTab group) {
		if (!ToolsConfig.COMMON.extraMaterialsEnabled.get()) {
			return false;
		}

		if (ToolsConfig.COMMON.hideUncraftableItems.get() && ForgeRegistries.ITEMS.tags().getTag(((ModdedItemTierHolder) this.getTierHolder()).getMaterial()).size() <= 0) {
			return false;
		}

		return super.allowedIn(group);
	}

}
