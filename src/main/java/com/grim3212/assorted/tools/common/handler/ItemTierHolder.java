package com.grim3212.assorted.tools.common.handler;

import com.grim3212.assorted.tools.common.util.ToolsItemTier;

import net.minecraft.world.item.Tier;
import net.minecraftforge.common.ForgeConfigSpec;

public class ItemTierHolder {

	private final String name;
	private final Tier defaultTier;

	private final ForgeConfigSpec.IntValue harvestLevel;
	private final ForgeConfigSpec.IntValue maxUses;
	private final ForgeConfigSpec.IntValue enchantability;
	private final ForgeConfigSpec.DoubleValue efficiency;
	private final ForgeConfigSpec.DoubleValue damage;
	protected final ForgeConfigSpec.DoubleValue axeDamage;
	protected final ForgeConfigSpec.DoubleValue axeSpeed;

	// Bucket config properties
	private final ForgeConfigSpec.IntValue maxBuckets;
	private final ForgeConfigSpec.IntValue milkingLevel;
	private final ForgeConfigSpec.DoubleValue maxPickupTemp;
	private final ForgeConfigSpec.BooleanValue breaksAfterUse;

	public ItemTierHolder(ForgeConfigSpec.Builder builder, String name, Tier defaultTier, float defaultAxeDamage, float defaultAxeSpeed, int defaultMaxBuckets, int defaultMilkingLevel, float defaultMaxPickupTemp, boolean defaultBreaksAfterUse) {
		this.name = name;
		this.defaultTier = defaultTier;

		builder.push(name);
		this.maxUses = builder.comment("The maximum uses for this item tier").defineInRange("maxUses", this.defaultTier.getUses(), 1, 100000);
		this.enchantability = builder.comment("The enchantability for this item tier").defineInRange("enchantability", this.defaultTier.getEnchantmentValue(), 0, 100000);
		this.harvestLevel = builder.comment("The harvest level for this item tier").defineInRange("harvestLevel", this.defaultTier.getLevel(), 0, 100);
		this.efficiency = builder.comment("The efficiency for this item tier").defineInRange("efficiency", this.defaultTier.getSpeed(), 0, 100000);
		this.damage = builder.comment("The amount of damage this item tier does").defineInRange("damage", this.defaultTier.getAttackDamageBonus(), 0, 100000);

		this.axeDamage = builder.comment("The damage modifier for axes as they are different per material. Will not affect vanilla tools.").defineInRange("axeDamage", defaultAxeDamage, 0, 100000);
		this.axeSpeed = builder.comment("The speed modifier for axes as they are different per material. Will not affect vanilla tools.").defineInRange("axeSpeed", defaultAxeSpeed, -1000, 100000);

		this.maxBuckets = builder.comment("The maximum number of buckets that this materials bucket can hold.").defineInRange("maxBuckets", defaultMaxBuckets, 1, 1000);
		this.milkingLevel = builder.comment("The milking level that will be set for this materials bucket. By default only 0-3").defineInRange("milkingLevel", defaultMilkingLevel, 0, 5);
		this.maxPickupTemp = builder.comment("The maximum temp of a fluid this materials bucket can pickup.").defineInRange("maxPickupTemp", defaultMaxPickupTemp, 0, 1000000);
		this.breaksAfterUse = builder.comment("Is this material so weak that the bucket will break after placing a fluid.").define("breaksAfterUse", defaultBreaksAfterUse);
		builder.pop();
	}

	public ItemTierHolder(ForgeConfigSpec.Builder builder, String name, ToolsItemTier defaultTier) {
		this.name = name;
		this.defaultTier = defaultTier;

		builder.push(name);
		this.maxUses = builder.comment("The maximum uses for this item tier").defineInRange("maxUses", this.defaultTier.getUses(), 1, 100000);
		this.enchantability = builder.comment("The enchantability for this item tier").defineInRange("enchantability", this.defaultTier.getEnchantmentValue(), 0, 100000);
		this.harvestLevel = builder.comment("The harvest level for this item tier").defineInRange("harvestLevel", this.defaultTier.getLevel(), 0, 100);
		this.efficiency = builder.comment("The efficiency for this item tier").defineInRange("efficiency", this.defaultTier.getSpeed(), 0, 100000);
		this.damage = builder.comment("The amount of damage this item tier does").defineInRange("damage", this.defaultTier.getAttackDamageBonus(), 0, 100000);

		this.axeDamage = builder.comment("The damage modifier for axes as they are different per material. Will not affect vanilla tools.").defineInRange("axeDamage", defaultTier.getAxeDamage(), 0, 100000);
		this.axeSpeed = builder.comment("The speed modifier for axes as they are different per material. Will not affect vanilla tools.").defineInRange("axeSpeed", defaultTier.getAxeSpeedIn(), -1000, 100000);

		this.maxBuckets = builder.comment("The maximum number of buckets that this materials bucket can hold.").defineInRange("maxBuckets", defaultTier.getBucketOptions().maxBuckets, 1, 1000);
		this.milkingLevel = builder.comment("The milking level that will be set for this materials bucket. By default only 0-3").defineInRange("milkingLevel", defaultTier.getBucketOptions().milkingLevel, 0, 5);
		this.maxPickupTemp = builder.comment("The maximum temp of a fluid this materials bucket can pickup.").defineInRange("maxPickupTemp", defaultTier.getBucketOptions().maxPickupTemp, 0, 1000000);
		this.breaksAfterUse = builder.comment("Is this material so weak that the bucket will break after placing a fluid.").define("breaksAfterUse", defaultTier.getBucketOptions().destroyedAfterUse);
		builder.pop();
	}

	protected void extraConfigs(ForgeConfigSpec.Builder builder) {

	}

	public String getName() {
		return name;
	}

	public Tier getDefaultTier() {
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

	public float getAxeDamage() {
		return this.axeDamage.get().floatValue();
	}

	public float getAxeSpeed() {
		return this.axeSpeed.get().floatValue();
	}

	public int getMaxBuckets() {
		return this.maxBuckets.get();
	}

	public int getMilkingLevel() {
		return this.milkingLevel.get();
	}

	public float getMaxPickupTemp() {
		return this.maxPickupTemp.get().floatValue();
	}

	public boolean getBreaksAfterUse() {
		return this.breaksAfterUse.get();
	}

	@Override
	public String toString() {
		return "[Name:" + getName() + ", HarvestLevel:" + getHarvestLevel() + ", MaxUses:" + getMaxUses() + ", Efficiency:" + getEfficiency() + ", Damage:" + getDamage() + ", Enchantability:" + getEnchantability() + ", AxeDamage:" + getAxeDamage() + ", AxeSpeed:" + getAxeSpeed() + ", MaxBuckets:" + getMaxBuckets() + ", MilkingLevel:" + getMilkingLevel() + ", MaxPickupTemp:" + getMaxPickupTemp() + ", BreaksAfterUse:" + getBreaksAfterUse() + "]";
	}
}
