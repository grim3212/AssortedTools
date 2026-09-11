package com.grim3212.assorted.tools.config;

import com.grim3212.assorted.lib.config.IConfigurationBuilder;
import com.grim3212.assorted.tools.api.item.HarvestTiers;
import com.grim3212.assorted.tools.api.item.ToolsItemTier;
import net.minecraft.world.item.ToolMaterial;

import java.util.function.Supplier;

/**
 * A tool material whose numbers come from the configuration file. {@link #material()} is read once
 * at registration and baked into the items' data components, so <b>changes need a restart</b>.
 */
public class ItemTierConfig {
    private final String name;
    private final ToolMaterial defaultTier;

    public final Supplier<Integer> harvestLevel;
    public final Supplier<Integer> maxUses;
    public final Supplier<Integer> enchantability;
    public final Supplier<Double> efficiency;
    public final Supplier<Double> damage;
    public final Supplier<Double> axeDamage;
    public final Supplier<Double> axeSpeed;

    // Bucket config properties
    public final Supplier<Integer> maxBuckets;
    public final Supplier<Integer> milkingLevel;
    public final Supplier<Double> maxPickupTemp;
    public final Supplier<Boolean> breaksAfterUse;

    // MultiTool config properties
    public final Supplier<Double> multiToolModifier;

    public ItemTierConfig(IConfigurationBuilder builder, String name, String path, ToolMaterial defaultTier, float defaultAxeDamage, float defaultAxeSpeed, int defaultMaxBuckets, int defaultMilkingLevel, float defaultMaxPickupTemp, boolean defaultBreaksAfterUse, float defaultMultiToolModifier) {
        this.name = name;
        this.defaultTier = defaultTier;

        this.maxUses = builder.defineInteger(path + "." + name + ".maxUses", this.defaultTier.durability(), 1, 100000, "The maximum uses for this item tier");
        this.enchantability = builder.defineInteger(path + "." + name + ".enchantability", this.defaultTier.enchantmentValue(), 0, 100000, "The enchantability for this item tier");
        this.harvestLevel = builder.defineInteger(path + "." + name + ".harvestLevel", HarvestTiers.harvestLevelOf(this.defaultTier.incorrectBlocksForDrops()), 0, 100, "The harvest level for this item tier. 0 is wood, 1 stone, 2 iron, 3 diamond, 4 and above netherite.");
        this.efficiency = builder.defineDouble(path + "." + name + ".efficiency", this.defaultTier.speed(), 0F, 100000F, "The efficiency for this item tier");
        this.damage = builder.defineDouble(path + "." + name + ".damage", this.defaultTier.attackDamageBonus(), 0F, 100000F, "The amount of damage this item tier does");

        this.axeDamage = builder.defineDouble(path + "." + name + ".axeDamage", defaultAxeDamage, 0F, 100000F, "The damage modifier for axes as they are different per material. Will not affect vanilla tools.");
        this.axeSpeed = builder.defineDouble(path + "." + name + ".axeSpeed", defaultAxeSpeed, -1000F, 100000F, "The speed modifier for axes as they are different per material. Will not affect vanilla tools.");

        // Floor of 0, not 1: not every tier has a bucket. ULTIMATE defaults to 0 and the config
        // system rejects an option whose validator will not accept its own default, which took mod
        // construction down on NeoForge. 0 reads naturally as "no capacity".
        this.maxBuckets = builder.defineInteger(path + "." + name + ".maxBuckets", defaultMaxBuckets, 0, 1000, "The maximum number of buckets that this materials bucket can hold.");
        this.milkingLevel = builder.defineInteger(path + "." + name + ".milkingLevel", defaultMilkingLevel, 0, 5, "The milking level that will be set for this materials bucket. By default only 0-3");
        this.maxPickupTemp = builder.defineDouble(path + "." + name + ".maxPickupTemp", defaultMaxPickupTemp, 0F, 1000000F, "The maximum temp of a fluid this materials bucket can pickup.");
        this.breaksAfterUse = builder.defineBoolean(path + "." + name + ".breaksAfterUse", defaultBreaksAfterUse, "Is this material so weak that the bucket will break after placing a fluid.");

        this.multiToolModifier = builder.defineDouble(path + "." + name + ".multiToolModifier", defaultMultiToolModifier, 0F, 1000F, "The modifier that will be used to calculate the multitool maximum uses. Normal tool material maxUses * this modifier.");
    }

    public ItemTierConfig(IConfigurationBuilder builder, String name, String path, ToolsItemTier defaultTier) {
        this(builder, name, path, defaultTier.material(), defaultTier.getAxeDamage(), defaultTier.getAxeSpeedIn(), defaultTier.getBucketOptions().maxBuckets, defaultTier.getBucketOptions().milkingLevel, defaultTier.getBucketOptions().maxPickupTemp, defaultTier.getBucketOptions().destroyedAfterUse, defaultTier.getMultiToolMod());
    }

    public String getName() {
        return name;
    }

    public ToolMaterial getDefaultTier() {
        return defaultTier;
    }

    /**
     * Cached: several items share a tier, and caching keeps every item of one material agreeing
     * even if the configuration reloads underneath, since the values are baked.
     */
    private ToolMaterial cachedMaterial;

    public ToolMaterial material() {
        if (this.cachedMaterial == null) {
            this.cachedMaterial = new ToolMaterial(HarvestTiers.incorrectBlocksForDrops(getHarvestLevel()), getMaxUses(), getEfficiency(), getDamage(), getEnchantability(), this.defaultTier.repairItems());
        }

        return this.cachedMaterial;
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

    public float getMultiToolModifier() {
        return multiToolModifier.get().floatValue();
    }

    @Override
    public String toString() {
        return "[Name:" + getName() + ", HarvestLevel:" + getHarvestLevel() + ", MaxUses:" + getMaxUses() + ", Efficiency:" + getEfficiency() + ", Damage:" + getDamage() + ", Enchantability:" + getEnchantability() + ", AxeDamage:" + getAxeDamage() + ", AxeSpeed:" + getAxeSpeed() + ", MaxBuckets:" + getMaxBuckets() + ", MilkingLevel:" + getMilkingLevel() + ", MaxPickupTemp:" + getMaxPickupTemp() + ", BreaksAfterUse:" + getBreaksAfterUse() + "]";
    }
}
