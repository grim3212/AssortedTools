package com.grim3212.assorted.decor.api.item;

import net.minecraft.world.item.Tier;

public abstract class ItemTierHolder {

    private final String name;
    private final Tier defaultTier;

    protected final SuppliedProperty<Integer> harvestLevel;
    protected final SuppliedProperty<Integer> maxUses;
    protected final SuppliedProperty<Integer> enchantability;
    protected final SuppliedProperty<Float> efficiency;
    protected final SuppliedProperty<Float> damage;
    protected final SuppliedProperty<Float> axeDamage;
    protected final SuppliedProperty<Float> axeSpeed;

    // Bucket config properties
    protected final SuppliedProperty<Integer> maxBuckets;
    protected final SuppliedProperty<Integer> milkingLevel;
    protected final SuppliedProperty<Float> maxPickupTemp;
    protected final SuppliedProperty<Boolean> breaksAfterUse;

    private ItemTierHolder(String name, Tier defaultTier, float defaultAxeDamage, float defaultAxeSpeed, int defaultMaxBuckets, int defaultMilkingLevel, float defaultMaxPickupTemp, boolean defaultBreaksAfterUse) {
        this.name = name;
        this.defaultTier = defaultTier;

        this.harvestLevel = new SuppliedProperty<>(defaultTier.getLevel());
        this.maxUses = new SuppliedProperty<>(defaultTier.getUses());
        this.enchantability = new SuppliedProperty<>(defaultTier.getEnchantmentValue());
        this.efficiency = new SuppliedProperty<>(defaultTier.getSpeed());
        this.damage = new SuppliedProperty<>(defaultTier.getAttackDamageBonus());

        this.axeDamage = new SuppliedProperty<>(defaultAxeDamage);
        this.axeSpeed = new SuppliedProperty<>(defaultAxeSpeed);

        this.maxBuckets = new SuppliedProperty<>(defaultMaxBuckets);
        this.milkingLevel = new SuppliedProperty<>(defaultMilkingLevel);
        this.maxPickupTemp = new SuppliedProperty<>(defaultMaxPickupTemp);
        this.breaksAfterUse = new SuppliedProperty<>(defaultBreaksAfterUse);

        this.buildOptions(defaultTier, defaultAxeDamage, defaultAxeSpeed, defaultMaxBuckets, defaultMilkingLevel, defaultMaxPickupTemp, defaultBreaksAfterUse);
    }

    public ItemTierHolder(String name, ToolsItemTier defaultTier) {
        this(name, defaultTier, defaultTier.getAxeDamage(), defaultTier.getAxeSpeedIn(), defaultTier.getBucketOptions().maxBuckets, defaultTier.getBucketOptions().milkingLevel, defaultTier.getBucketOptions().maxPickupTemp, defaultTier.getBucketOptions().destroyedAfterUse);
    }

    /**
     * Used to set the SuppliedProperties value suppliers that we can pull the current values from Config values
     *
     * @param defaultTier
     * @param defaultAxeDamage
     * @param defaultAxeSpeed
     * @param defaultMaxBuckets
     * @param defaultMilkingLevel
     * @param defaultMaxPickupTemp
     * @param defaultBreaksAfterUse
     */
    public abstract void buildOptions(Tier defaultTier, float defaultAxeDamage, float defaultAxeSpeed, int defaultMaxBuckets, int defaultMilkingLevel, float defaultMaxPickupTemp, boolean defaultBreaksAfterUse);

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
