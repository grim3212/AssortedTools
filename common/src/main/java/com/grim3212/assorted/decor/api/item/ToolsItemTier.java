package com.grim3212.assorted.decor.api.item;

import com.grim3212.assorted.decor.api.ToolsTags;
import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum ToolsItemTier implements Tier {
    TIN(1, 80, 2.5F, 0.4F, 14, () -> {
        return ToolsTags.Items.INGOTS_TIN;
    }, 6.0F, -3.2F, () -> new BucketOptions(4, 0)),

    COPPER(2, 150, 4.3F, 1.0F, 14, () -> {
        return LibCommonTags.Items.INGOTS_COPPER;
    }, 7.0F, -3.2F, () -> new BucketOptions(8, 0)),

    SILVER(3, 1456, 8.0F, 2.8F, 14, () -> {
        return ToolsTags.Items.INGOTS_SILVER;
    }, 6.5F, -3.2F, () -> new BucketOptions(12, 1)),

    ALUMINUM(1, 215, 5.4F, 1.5F, 10, () -> {
        return ToolsTags.Items.INGOTS_ALUMINUM;
    }, 6.2F, -3.1F, () -> new BucketOptions(2, 0)),

    NICKEL(2, 230, 5.8F, 1.8F, 10, () -> {
        return ToolsTags.Items.INGOTS_NICKEL;
    }, 6.0F, -3.2F, () -> new BucketOptions(8, 1)),

    PLATINUM(3, 1865, 7.8F, 3.2F, 18, () -> {
        return ToolsTags.Items.INGOTS_PLATINUM;
    }, 6.0F, -3.0F, () -> new BucketOptions(24, 2)),

    LEAD(2, 212, 4.2F, 1.2F, 4, () -> {
        return ToolsTags.Items.INGOTS_LEAD;
    }, 6.0F, -3.2F, () -> new BucketOptions(4, 0)),

    BRONZE(2, 245, 6.0F, 2.0F, 13, () -> {
        return ToolsTags.Items.INGOTS_BRONZE;
    }, 6.0F, -3.1F, () -> new BucketOptions(8, 1)),

    ELECTRUM(3, 213, 10.0F, 2.0F, 13, () -> {
        return ToolsTags.Items.INGOTS_ELECTRUM;
    }, 6.0F, -3.5F, () -> new BucketOptions(18, 1)),

    INVAR(2, 264, 6.2F, 2.2F, 11, () -> {
        return ToolsTags.Items.INGOTS_INVAR;
    }, 6.0F, -3.1F, () -> new BucketOptions(10, 1)),

    STEEL(3, 1356, 6.9F, 2.5F, 10, () -> {
        return ToolsTags.Items.INGOTS_STEEL;
    }, 7.0F, -3.0F, () -> new BucketOptions(16, 1)),

    RUBY(2, 1612, 8.4F, 3.2F, 10, () -> {
        return ToolsTags.Items.GEMS_RUBY;
    }, 6.0F, -3.2F, () -> new BucketOptions(8, 1)),

    AMETHYST(2, 1532, 7.9F, 2.9F, 12, () -> {
        return LibCommonTags.Items.GEMS_AMETHYST;
    }, 5.2F, -3.1F, () -> new BucketOptions(8, 0)),

    SAPPHIRE(2, 1524, 7.8F, 2.8F, 12, () -> {
        return ToolsTags.Items.GEMS_SAPPHIRE;
    }, 5.2F, -3.1F, () -> new BucketOptions(8, 0)),

    TOPAZ(2, 1426, 7.6F, 2.6F, 8, () -> {
        return ToolsTags.Items.GEMS_TOPAZ;
    }, 5.0F, -3.0F, () -> new BucketOptions(8, 0)),

    EMERALD(3, 1547, 8.2F, 3.0F, 14, () -> {
        return LibCommonTags.Items.GEMS_EMERALD;
    }, 5.2F, -3.2F, () -> new BucketOptions(10, 1)),

    PERIDOT(2, 1456, 7.7F, 2.7F, 9, () -> {
        return ToolsTags.Items.GEMS_PERIDOT;
    }, 5.0F, -3.0F, () -> new BucketOptions(8, 0)),

    ULTIMATE(7, 1561, 64F, 64F, 0, () -> {
        return LibCommonTags.Items.NETHER_STARS;
    }, 0F, 0F, () -> new BucketOptions(0, 0));

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<TagKey<Item>> repairMaterial;
    private final float axeDamage;
    private final float axeSpeedIn;
    private final Supplier<BucketOptions> bucketType;

    private ToolsItemTier(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<TagKey<Item>> repairTagIn, float axeDamageIn, float axeSpeedIn, Supplier<BucketOptions> bucketType) {
        this.harvestLevel = harvestLevelIn;
        this.maxUses = maxUsesIn;
        this.efficiency = efficiencyIn;
        this.attackDamage = attackDamageIn;
        this.enchantability = enchantabilityIn;
        this.repairMaterial = repairTagIn;
        this.axeDamage = axeDamageIn;
        this.axeSpeedIn = axeSpeedIn;
        this.bucketType = bucketType;
    }

    public float getAxeDamage() {
        return this.axeDamage;
    }

    public float getAxeSpeedIn() {
        return this.axeSpeedIn;
    }

    @Override
    public int getUses() {
        return this.maxUses;
    }

    @Override
    public float getSpeed() {
        return this.efficiency;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.attackDamage;
    }

    @Override
    public int getLevel() {
        return this.harvestLevel;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    public TagKey<Item> repairTag() {
        return this.repairMaterial.get();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(this.repairMaterial.get());
    }

    public BucketOptions getBucketOptions() {
        return bucketType.get();
    }
}
