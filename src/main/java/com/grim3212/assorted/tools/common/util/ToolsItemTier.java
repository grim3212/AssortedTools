package com.grim3212.assorted.tools.common.util;

import java.util.function.Supplier;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.LazyValue;
import net.minecraftforge.common.Tags;

public enum ToolsItemTier implements IItemTier {
	TIN(1, 80, 2.5F, 0.4F, 14, () -> {
		return ToolsTags.Items.INGOTS_TIN;
	}, 6.0F, -3.2F),

	COPPER(2, 150, 4.3F, 1.0F, 14, () -> {
		return ToolsTags.Items.INGOTS_COPPER;
	}, 7.0F, -3.2F),

	SILVER(3, 1456, 8.0F, 2.8F, 14, () -> {
		return ToolsTags.Items.INGOTS_SILVER;
	}, 6.5F, -3.2F),

	ALUMINUM(1, 215, 5.4F, 1.5F, 10, () -> {
		return ToolsTags.Items.INGOTS_ALUMINUM;
	}, 6.2F, -3.1F),

	NICKEL(2, 230, 5.8F, 1.8F, 10, () -> {
		return ToolsTags.Items.INGOTS_NICKEL;
	}, 6.0F, -3.2F),

	PLATINUM(3, 1865, 7.8F, 3.2F, 18, () -> {
		return ToolsTags.Items.INGOTS_PLATINUM;
	}, 6.0F, -3.0F),

	LEAD(2, 212, 4.2F, 1.2F, 4, () -> {
		return ToolsTags.Items.INGOTS_LEAD;
	}, 6.0F, -3.2F),

	BRONZE(2, 245, 6.0F, 2.0F, 13, () -> {
		return ToolsTags.Items.INGOTS_BRONZE;
	}, 6.0F, -3.1F),

	ELECTRUM(3, 213, 10.0F, 2.0F, 13, () -> {
		return ToolsTags.Items.INGOTS_ELECTRUM;
	}, 6.0F, -3.5F),

	INVAR(2, 264, 6.2F, 2.2F, 11, () -> {
		return ToolsTags.Items.INGOTS_INVAR;
	}, 6.0F, -3.1F),

	STEEL(3, 1356, 6.9F, 2.5F, 10, () -> {
		return ToolsTags.Items.INGOTS_STEEL;
	}, 7.0F, -3.0F),

	RUBY(2, 1612, 8.4F, 3.2F, 10, () -> {
		return ToolsTags.Items.GEMS_RUBY;
	}, 6.0F, -3.2F),

	AMETHYST(2, 1532, 7.9F, 2.9F, 12, () -> {
		return ToolsTags.Items.GEMS_AMETHYST;
	}, 5.2F, -3.1F),

	SAPPHIRE(2, 1524, 7.8F, 2.8F, 12, () -> {
		return ToolsTags.Items.GEMS_SAPPHIRE;
	}, 5.2F, -3.1F),

	TOPAZ(2, 1426, 7.6F, 2.6F, 8, () -> {
		return ToolsTags.Items.GEMS_TOPAZ;
	}, 5.0F, -3.0F),

	EMERALD(2, 1547, 8.2F, 3.0F, 14, () -> {
		return Tags.Items.GEMS_EMERALD;
	}, 5.2F, -3.2F);

	private final int harvestLevel;
	private final int maxUses;
	private final float efficiency;
	private final float attackDamage;
	private final int enchantability;
	private final LazyValue<ITag<Item>> repairMaterial;
	private final float axeDamage;
	private final float axeSpeedIn;
	
	private ToolsItemTier(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<ITag<Item>> repairTagIn, float axeDamageIn, float axeSpeedIn) {
		this.harvestLevel = harvestLevelIn;
		this.maxUses = maxUsesIn;
		this.efficiency = efficiencyIn;
		this.attackDamage = attackDamageIn;
		this.enchantability = enchantabilityIn;
		this.repairMaterial = new LazyValue<ITag<Item>>(repairTagIn);
		this.axeDamage = axeDamageIn;
		this.axeSpeedIn = axeSpeedIn;
	}
	
	public float getAxeDamage() {
		return this.axeDamage;
	}
	
	public float getAxeSpeedIn() {
		return this.axeSpeedIn;
	}

	@Override
	public int getMaxUses() {
		return this.maxUses;
	}

	@Override
	public float getEfficiency() {
		return this.efficiency;
	}

	@Override
	public float getAttackDamage() {
		return this.attackDamage;
	}

	@Override
	public int getHarvestLevel() {
		return this.harvestLevel;
	}

	@Override
	public int getEnchantability() {
		return this.enchantability;
	}

	public ITag<Item> repairTag() {
		return this.repairMaterial.getValue();
	}

	@Override
	public Ingredient getRepairMaterial() {
		return Ingredient.fromTag(this.repairMaterial.getValue());
	}

}
