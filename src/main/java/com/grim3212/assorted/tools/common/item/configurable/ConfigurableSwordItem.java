package com.grim3212.assorted.tools.common.item.configurable;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.grim3212.assorted.tools.common.handler.ItemTierHolder;
import com.grim3212.assorted.tools.common.item.ITiered;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

public class ConfigurableSwordItem extends SwordItem implements ITiered {

	private final static int swordDamage = 3;
	private final static float swordSpeed = -2.4F;
	private final ItemTierHolder tierHolder;

	public ConfigurableSwordItem(ItemTierHolder tierHolder, Properties builder) {
		super(tierHolder.getDefaultTier(), swordDamage, swordSpeed, builder);
		this.tierHolder = tierHolder;
	}

	@Override
	public ItemTierHolder getTierHolder() {
		return tierHolder;
	}

	@Override
	public float getDamage() {
		return swordDamage + this.tierHolder.getDamage();
	}

	@Override
	public int getEnchantmentValue() {
		return this.tierHolder.getEnchantability();
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return this.tierHolder.getMaxUses();
	}

	protected Multimap<Attribute, AttributeModifier> attribs;

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		if (attribs == null) {
			Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (double) this.getDamage(), AttributeModifier.Operation.ADDITION));
			builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", (double) swordSpeed, AttributeModifier.Operation.ADDITION));
			this.attribs = builder.build();
		}

		return slot == EquipmentSlot.MAINHAND ? this.attribs : super.getDefaultAttributeModifiers(slot);
	}
}
