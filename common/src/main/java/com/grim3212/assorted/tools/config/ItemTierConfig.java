package com.grim3212.assorted.tools.config;

import com.grim3212.assorted.lib.config.ConfigGroup;
import com.grim3212.assorted.lib.config.ConfigOption;
import com.grim3212.assorted.tools.api.item.ToolsItemTier;
import net.minecraft.world.item.Tier;

public class ItemTierConfig {
    private final String name;
    private final Tier defaultTier;

    private final ConfigOption.ConfigOptionInteger harvestLevel;
    private final ConfigOption.ConfigOptionInteger maxUses;
    private final ConfigOption.ConfigOptionInteger enchantability;
    private final ConfigOption.ConfigOptionFloat efficiency;
    private final ConfigOption.ConfigOptionFloat damage;
    private final ConfigOption.ConfigOptionFloat axeDamage;
    private final ConfigOption.ConfigOptionFloat axeSpeed;

    // Bucket config properties
    private final ConfigOption.ConfigOptionInteger maxBuckets;
    private final ConfigOption.ConfigOptionInteger milkingLevel;
    private final ConfigOption.ConfigOptionFloat maxPickupTemp;
    private final ConfigOption<Boolean> breaksAfterUse;

    public ItemTierConfig(ConfigGroup group, String name, Tier defaultTier, float defaultAxeDamage, float defaultAxeSpeed, int defaultMaxBuckets, int defaultMilkingLevel, float defaultMaxPickupTemp, boolean defaultBreaksAfterUse) {
        this.name = name;
        this.defaultTier = defaultTier;

        ConfigGroup itemTierGroup = new ConfigGroup(name);

        this.maxUses = new ConfigOption.ConfigOptionInteger("maxUses", this.defaultTier.getUses(), "The maximum uses for this item tier", 1, 100000);
        this.enchantability = new ConfigOption.ConfigOptionInteger("enchantability", this.defaultTier.getEnchantmentValue(), "The enchantability for this item tier", 0, 100000);
        this.harvestLevel = new ConfigOption.ConfigOptionInteger("harvestLevel", this.defaultTier.getLevel(), "The harvest level for this item tier", 0, 100);
        this.efficiency = new ConfigOption.ConfigOptionFloat("efficiency", this.defaultTier.getSpeed(), "The efficiency for this item tier", 0F, 100000F);
        this.damage = new ConfigOption.ConfigOptionFloat("damage", this.defaultTier.getAttackDamageBonus(), "The amount of damage this item tier does", 0F, 100000F);

        this.axeDamage = new ConfigOption.ConfigOptionFloat("axeDamage", defaultAxeDamage, "The damage modifier for axes as they are different per material. Will not affect vanilla tools.", 0F, 100000F);
        this.axeSpeed = new ConfigOption.ConfigOptionFloat("axeSpeed", defaultAxeSpeed, "The speed modifier for axes as they are different per material. Will not affect vanilla tools.", -1000F, 100000F);

        this.maxBuckets = new ConfigOption.ConfigOptionInteger("maxBuckets", defaultMaxBuckets, "The maximum number of buckets that this materials bucket can hold.", 1, 1000);
        this.milkingLevel = new ConfigOption.ConfigOptionInteger("milkingLevel", defaultMilkingLevel, "The milking level that will be set for this materials bucket. By default only 0-3", 0, 5);
        this.maxPickupTemp = new ConfigOption.ConfigOptionFloat("maxPickupTemp", defaultMaxPickupTemp, "The maximum temp of a fluid this materials bucket can pickup.", 0F, 1000000F);
        this.breaksAfterUse = new ConfigOption<Boolean>("breaksAfterUse", ConfigOption.OptionType.BOOLEAN, defaultBreaksAfterUse, "Is this material so weak that the bucket will break after placing a fluid.");

        itemTierGroup.addOptions(this.maxUses, this.enchantability, this.harvestLevel, this.efficiency, this.damage, this.axeDamage, this.axeSpeed, this.maxBuckets, this.milkingLevel, this.maxPickupTemp, this.breaksAfterUse);
        group.addSubGroups(itemTierGroup);
    }

    public ItemTierConfig(ConfigGroup group, String name, ToolsItemTier defaultTier) {
        this(group, name, defaultTier, defaultTier.getAxeDamage(), defaultTier.getAxeSpeedIn(), defaultTier.getBucketOptions().maxBuckets, defaultTier.getBucketOptions().milkingLevel, defaultTier.getBucketOptions().maxPickupTemp, defaultTier.getBucketOptions().destroyedAfterUse);
    }

    public String getName() {
        return name;
    }

    public Tier getDefaultTier() {
        return defaultTier;
    }

    public int getHarvestLevel() {
        return harvestLevel.getValue();
    }

    public int getMaxUses() {
        return maxUses.getValue();
    }

    public float getEfficiency() {
        return efficiency.getValue();
    }

    public float getDamage() {
        return damage.getValue();
    }

    public int getEnchantability() {
        return enchantability.getValue();
    }

    public float getAxeDamage() {
        return this.axeDamage.getValue();
    }

    public float getAxeSpeed() {
        return this.axeSpeed.getValue();
    }

    public int getMaxBuckets() {
        return this.maxBuckets.getValue();
    }

    public int getMilkingLevel() {
        return this.milkingLevel.getValue();
    }

    public float getMaxPickupTemp() {
        return this.maxPickupTemp.getValue();
    }

    public boolean getBreaksAfterUse() {
        return this.breaksAfterUse.getValue();
    }

    @Override
    public String toString() {
        return "[Name:" + getName() + ", HarvestLevel:" + getHarvestLevel() + ", MaxUses:" + getMaxUses() + ", Efficiency:" + getEfficiency() + ", Damage:" + getDamage() + ", Enchantability:" + getEnchantability() + ", AxeDamage:" + getAxeDamage() + ", AxeSpeed:" + getAxeSpeed() + ", MaxBuckets:" + getMaxBuckets() + ", MilkingLevel:" + getMilkingLevel() + ", MaxPickupTemp:" + getMaxPickupTemp() + ", BreaksAfterUse:" + getBreaksAfterUse() + "]";
    }
}
