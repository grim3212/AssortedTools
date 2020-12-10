package com.grim3212.assorted.tools.common.handler;

import javax.annotation.Nullable;

import com.grim3212.assorted.tools.common.util.ToolsItemTier;

import net.minecraft.item.IItemTier;
import net.minecraftforge.common.ForgeConfigSpec;

public class ItemTierHolder {
	
	private final String name;
	private final IItemTier defaultTier;

	private final ForgeConfigSpec.IntValue harvestLevel;
	private final ForgeConfigSpec.IntValue maxUses;
	private final ForgeConfigSpec.IntValue enchantability;
	private final ForgeConfigSpec.DoubleValue efficiency;
	private final ForgeConfigSpec.DoubleValue damage;
	@Nullable
	protected ForgeConfigSpec.DoubleValue axeDamage;
	@Nullable
	protected ForgeConfigSpec.DoubleValue axeSpeed;

	public ItemTierHolder(ForgeConfigSpec.Builder builder, String name, IItemTier defaultTier) {
		this.name = name;
		this.defaultTier = defaultTier;

		builder.push(name);
		this.maxUses = builder.comment("The maximum uses for this item tier").defineInRange("maxUses", this.defaultTier.getMaxUses(), 1, 100000);
		this.enchantability = builder.comment("The enchantability for this item tier").defineInRange("enchantability", this.defaultTier.getEnchantability(), 0, 100000);
		this.harvestLevel = builder.comment("The harvest level for this item tier").defineInRange("harvestLevel", this.defaultTier.getHarvestLevel(), 0, 100);
		this.efficiency = builder.comment("The efficiency for this item tier").defineInRange("efficiency", this.defaultTier.getEfficiency(), 0, 100000);
		this.damage = builder.comment("The amount of damage this item tier does").defineInRange("damage", this.defaultTier.getAttackDamage(), 0, 100000);

		if (defaultTier instanceof ToolsItemTier) {
			ToolsItemTier moddedTier = (ToolsItemTier) defaultTier;
			this.axeDamage = builder.comment("The damage modifier for axes as they are different per material. Only used for modded item tier materials.").defineInRange("axeDamage", moddedTier.getAxeDamage(), 0, 100000);
			this.axeSpeed = builder.comment("The speed modifier for axes as they are different per material. Only used for modded item tier materials.").defineInRange("axeSpeed", moddedTier.getAxeSpeedIn(), 0, 100000);
		}
		builder.pop();
	}

	protected void extraConfigs(ForgeConfigSpec.Builder builder) {

	}

	public String getName() {
		return name;
	}

	public IItemTier getDefaultTier() {
		return defaultTier;
	}

	public int getHarvestLevel() {
		return harvestLevel.get();
	}

	public int getMaxUses() {
		return maxUses.get();
	}

	public float getEfficiency() {
		return efficiency.get().floatValue();
	}

	public float getDamage() {
		return damage.get().floatValue();
	}

	public int getEnchantability() {
		return enchantability.get();
	}

	@Override
	public String toString() {
		return "[Name:" + getName() + ", HarvestLevel:" + getHarvestLevel() + ", MaxUses:" + getMaxUses() + ", Efficiency:" + getEfficiency() + ", Damage:" + getDamage() + ", Enchantability:" + getEnchantability() + "]";
	}
}
