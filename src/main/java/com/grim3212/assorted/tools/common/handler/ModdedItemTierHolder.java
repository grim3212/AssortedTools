package com.grim3212.assorted.tools.common.handler;

import com.grim3212.assorted.tools.common.util.ToolsItemTier;

import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec.Builder;

public class ModdedItemTierHolder extends ItemTierHolder {

	private final ToolsItemTier moddedTier;

	public ModdedItemTierHolder(Builder builder, String name, ToolsItemTier defaultTier) {
		super(builder, name, defaultTier);
		this.moddedTier = defaultTier;
	}

	public Tag<Item> getMaterial() {
		return this.moddedTier.repairTag();
	}
}
