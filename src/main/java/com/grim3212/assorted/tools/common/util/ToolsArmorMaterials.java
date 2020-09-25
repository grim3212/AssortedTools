package com.grim3212.assorted.tools.common.util;

import java.util.function.Supplier;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public enum ToolsArmorMaterials implements IArmorMaterial {
	CHICKEN_SUIT("chicken_suit", 5, new int[] { 1, 2, 3, 1 }, 15, () -> SoundEvents.BLOCK_WOOL_PLACE, () -> Ingredient.fromItems(Items.FEATHER), 0.0f, 0.0f);

	private final String name;
	private final int durabilityMultiplier;
	private final int[] damageReduction;
	private final int enchantability;
	private final Supplier<SoundEvent> equipSound;
	private final LazyValue<Ingredient> repairItem;
	private final float toughness;
	private final float knockbackResistance;
	private static final int[] MAX_DAMAGE_ARRAY = new int[] { 13, 15, 16, 11 };

	ToolsArmorMaterials(String name, int durabilityMultiplier, int[] damageReduction, int enchantability, Supplier<SoundEvent> equipSound, Supplier<Ingredient> repairItem, float toughness, float knockbackResistance) {
		this.name = name;
		this.durabilityMultiplier = durabilityMultiplier;
		this.damageReduction = damageReduction;
		this.enchantability = enchantability;
		this.equipSound = equipSound;
		this.repairItem = new LazyValue<>(repairItem);
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
	}

	@Override
	public int getDurability(EquipmentSlotType slot) {
		return durabilityMultiplier * MAX_DAMAGE_ARRAY[slot.getIndex()];
	}

	@Override
	public int getDamageReductionAmount(EquipmentSlotType slot) {
		return damageReduction[slot.getIndex()];
	}

	@Override
	public int getEnchantability() {
		return enchantability;
	}

	@Override
	public SoundEvent getSoundEvent() {
		return equipSound.get();
	}

	@Override
	public Ingredient getRepairMaterial() {
		return repairItem.getValue();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public float getToughness() {
		return toughness;
	}

	@Override
	public float getKnockbackResistance() {
		return knockbackResistance;
	}

}
