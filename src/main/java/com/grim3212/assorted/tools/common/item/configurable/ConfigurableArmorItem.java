package com.grim3212.assorted.tools.common.item.configurable;

import java.util.UUID;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;

public class ConfigurableArmorItem extends ArmorItem {

	private Multimap<Attribute, AttributeModifier> modifiers;

	public ConfigurableArmorItem(IArmorMaterial material, EquipmentSlotType slot, Properties builderIn) {
		super(material, slot, builderIn);
	}

	@Override
	public int getEnchantmentValue() {
		return this.getMaterial().getEnchantmentValue();
	}

	@Override
	public int getDefense() {
		return this.getMaterial().getDefenseForSlot(this.getSlot());
	}

	@Override
	public float getToughness() {
		return this.getMaterial().getToughness();
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return this.getMaterial().getDurabilityForSlot(this.getSlot());
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot) {
		if (equipmentSlot == this.slot) {
			return getModifiers(equipmentSlot);
		}
		return super.getDefaultAttributeModifiers(equipmentSlot);
	}

	public Multimap<Attribute, AttributeModifier> getModifiers(EquipmentSlotType equipmentSlot) {
		if (this.modifiers == null || this.modifiers.isEmpty()) {
			Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			UUID uuid = ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()];
			builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "Armor modifier", (double) getDefense(), AttributeModifier.Operation.ADDITION));
			builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Armor toughness", (double) getToughness(), AttributeModifier.Operation.ADDITION));
			if (this.knockbackResistance > 0) {
				builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Armor knockback resistance", (double) this.getMaterial().getKnockbackResistance(), AttributeModifier.Operation.ADDITION));
			}

			this.modifiers = builder.build();
		}
		return modifiers;
	}
}
