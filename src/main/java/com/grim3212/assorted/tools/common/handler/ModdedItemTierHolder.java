package com.grim3212.assorted.tools.common.handler;

import javax.annotation.Nullable;

import com.grim3212.assorted.tools.common.util.ToolsItemTier;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.ForgeConfigSpec.Builder;

public class ModdedItemTierHolder extends ItemTierHolder {

	private final ToolsItemTier moddedTier;

	public ModdedItemTierHolder(Builder builder, String name, ToolsItemTier defaultTier) {
		super(builder, name, defaultTier);
		this.moddedTier = defaultTier;
	}

	@Nullable
	public float getAxeDamage() {
		return this.axeDamage.get().floatValue();
	}

	@Nullable
	public float getAxeSpeed() {
		return this.axeSpeed.get().floatValue();
	}

	public ITag<Item> getMaterial() {
		return this.moddedTier.repairTag();
	}

	@Override
	public String toString() {
		return "[Name:" + getName() + ", HarvestLevel:" + getHarvestLevel() + ", MaxUses:" + getMaxUses() + ", Efficiency:" + getEfficiency() + ", Damage:" + getDamage() + ", Enchantability:" + getEnchantability() + ", AxeDamage:" + getAxeDamage() + ", AxeSpeed:" + getAxeSpeed() + "]";
	}
}
