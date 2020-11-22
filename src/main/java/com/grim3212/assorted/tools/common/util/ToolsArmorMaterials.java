package com.grim3212.assorted.tools.common.util;

import java.util.function.Supplier;

import com.grim3212.assorted.tools.common.handler.ArmorMaterialHolder;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public enum ToolsArmorMaterials implements IArmorMaterial {
	CHICKEN_SUIT(() -> ToolsConfig.COMMON.chickenSuitArmorMaterial, () -> SoundEvents.BLOCK_WOOL_PLACE, () -> Ingredient.fromItems(Items.FEATHER));

	private final Supplier<ArmorMaterialHolder> material;
	private final Supplier<SoundEvent> equipSound;
	private final LazyValue<Ingredient> repairItem;
	private static final int[] MAX_DAMAGE_ARRAY = new int[] { 13, 15, 16, 11 };

	ToolsArmorMaterials(Supplier<ArmorMaterialHolder> material, Supplier<SoundEvent> equipSound, Supplier<Ingredient> repairItem) {
		this.material = material;
		this.equipSound = equipSound;
		this.repairItem = new LazyValue<>(repairItem);
	}

	private ArmorMaterialHolder getMaterial() {
		return this.material.get();
	}

	@Override
	public int getDurability(EquipmentSlotType slot) {
		return this.getMaterial().getDurability() * MAX_DAMAGE_ARRAY[slot.getIndex()];
	}

	@Override
	public int getDamageReductionAmount(EquipmentSlotType slot) {
		return this.getMaterial().getReductionAmounts()[slot.getIndex()];
	}

	@Override
	public int getEnchantability() {
		return this.getMaterial().getEnchantability();
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
		return this.getMaterial().getName();
	}

	@Override
	public float getToughness() {
		return this.getMaterial().getToughness();
	}

	@Override
	public float getKnockbackResistance() {
		return this.getMaterial().getKnockbackResistance();
	}

}
