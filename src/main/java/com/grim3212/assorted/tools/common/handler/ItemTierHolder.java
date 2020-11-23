package com.grim3212.assorted.tools.common.handler;

import java.util.function.Supplier;

import net.minecraft.item.IItemTier;
import net.minecraftforge.common.ForgeConfigSpec;

public class ItemTierHolder {
	private final String name;
	private final Supplier<IItemTier> defaultTier;

	private final ForgeConfigSpec.IntValue harvestLevel;
	private final ForgeConfigSpec.IntValue maxUses;
	private final ForgeConfigSpec.IntValue enchantability;
	private final ForgeConfigSpec.DoubleValue efficiency;
	private final ForgeConfigSpec.DoubleValue damage;

	public ItemTierHolder(ForgeConfigSpec.Builder builder, String name, int harvestLevel, int maxUses, double efficiency, double damage, int enchantability, Supplier<IItemTier> defaultTier) {
		this.name = name;
		this.defaultTier = defaultTier;

		builder.push(name);
		this.maxUses = builder.comment("The maximum uses for this item tier").defineInRange("maxUses", maxUses, 1, 10000);
		this.enchantability = builder.comment("The enchantability for this item tier").defineInRange("enchantability", enchantability, 0, 10000);
		this.harvestLevel = builder.comment("The harvest level for this item tier").defineInRange("harvestLevel", harvestLevel, 0, 100);
		this.efficiency = builder.comment("The efficiency for this item tier").defineInRange("efficiency", efficiency, 0, 10000);
		this.damage = builder.comment("The amount of damage this item tier does").defineInRange("damage", damage, 0, 10000);
		builder.pop();
	}

	public String getName() {
		return name;
	}
	
	public Supplier<IItemTier> getDefaultTier() {
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
