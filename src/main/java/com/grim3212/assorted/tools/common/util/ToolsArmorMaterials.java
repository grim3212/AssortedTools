package com.grim3212.assorted.tools.common.util;

import java.util.function.Supplier;

import com.grim3212.assorted.tools.common.handler.ArmorMaterialHolder;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.Tags;

public enum ToolsArmorMaterials implements IArmorMaterial {
	CHICKEN_SUIT(() -> ToolsConfig.COMMON.chickenSuitArmorMaterial, () -> SoundEvents.BLOCK_WOOL_PLACE, () -> ToolsTags.Items.FEATHERS),
	TIN(() -> ToolsConfig.COMMON.moddedArmors.get("tin"), () -> SoundEvents.ITEM_ARMOR_EQUIP_IRON, () -> ToolsTags.Items.INGOTS_TIN),
	COPPER(() -> ToolsConfig.COMMON.moddedArmors.get("copper"), () -> SoundEvents.ITEM_ARMOR_EQUIP_IRON, () -> ToolsTags.Items.INGOTS_COPPER),
	SILVER(() -> ToolsConfig.COMMON.moddedArmors.get("silver"), () -> SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, () -> ToolsTags.Items.INGOTS_SILVER),
	ALUMINUM(() -> ToolsConfig.COMMON.moddedArmors.get("aluminum"), () -> SoundEvents.ITEM_ARMOR_EQUIP_IRON, () -> ToolsTags.Items.INGOTS_ALUMINUM),
	NICKEL(() -> ToolsConfig.COMMON.moddedArmors.get("nickel"), () -> SoundEvents.ITEM_ARMOR_EQUIP_IRON, () -> ToolsTags.Items.INGOTS_NICKEL),
	PLATINUM(() -> ToolsConfig.COMMON.moddedArmors.get("platinum"), () -> SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, () -> ToolsTags.Items.INGOTS_PLATINUM),
	LEAD(() -> ToolsConfig.COMMON.moddedArmors.get("lead"), () -> SoundEvents.ITEM_ARMOR_EQUIP_IRON, () -> ToolsTags.Items.INGOTS_LEAD),
	BRONZE(() -> ToolsConfig.COMMON.moddedArmors.get("bronze"), () -> SoundEvents.ITEM_ARMOR_EQUIP_IRON, () -> ToolsTags.Items.INGOTS_BRONZE),
	ELECTRUM(() -> ToolsConfig.COMMON.moddedArmors.get("electrum"), () -> SoundEvents.ITEM_ARMOR_EQUIP_IRON, () -> ToolsTags.Items.INGOTS_ELECTRUM),
	INVAR(() -> ToolsConfig.COMMON.moddedArmors.get("invar"), () -> SoundEvents.ITEM_ARMOR_EQUIP_IRON, () -> ToolsTags.Items.INGOTS_INVAR),
	STEEL(() -> ToolsConfig.COMMON.moddedArmors.get("steel"), () -> SoundEvents.ITEM_ARMOR_EQUIP_IRON, () -> ToolsTags.Items.INGOTS_STEEL),
	RUBY(() -> ToolsConfig.COMMON.moddedArmors.get("ruby"), () -> SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, () -> ToolsTags.Items.GEMS_RUBY),
	AMETHYST(() -> ToolsConfig.COMMON.moddedArmors.get("amethyst"), () -> SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, () -> ToolsTags.Items.GEMS_AMETHYST),
	SAPPHIRE(() -> ToolsConfig.COMMON.moddedArmors.get("sapphire"), () -> SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, () -> ToolsTags.Items.GEMS_SAPPHIRE),
	TOPAZ(() -> ToolsConfig.COMMON.moddedArmors.get("topaz"), () -> SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, () -> ToolsTags.Items.GEMS_TOPAZ),
	EMERALD(() -> ToolsConfig.COMMON.moddedArmors.get("emerald"), () -> SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, () -> Tags.Items.GEMS_EMERALD);

	private final Supplier<ArmorMaterialHolder> material;
	private final Supplier<SoundEvent> equipSound;
	private final LazyValue<ITag<Item>> repairMaterial;

	ToolsArmorMaterials(Supplier<ArmorMaterialHolder> material, Supplier<SoundEvent> equipSound, Supplier<ITag<Item>> repairTagIn) {
		this.material = material;
		this.equipSound = equipSound;
		this.repairMaterial = new LazyValue<ITag<Item>>(repairTagIn);
	}

	private ArmorMaterialHolder getMaterial() {
		return this.material.get();
	}

	@Override
	public int getDurability(EquipmentSlotType slot) {
		return this.getMaterial().getDurability() * ArmorMaterial.MAX_DAMAGE_ARRAY[slot.getIndex()];
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
		return Ingredient.fromTag(repairMaterial.getValue());
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
