package com.grim3212.assorted.tools.common.item.configurable;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.grim3212.assorted.tools.common.handler.ItemTierHolder;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.SwordItem;

import net.minecraft.world.item.Item.Properties;

public class ConfigurableSwordItem extends SwordItem {

	private final static int swordDamage = 3;
	private final static float swordSpeed = -2.4F;
	private final ItemTierHolder tierHolder;

	public ConfigurableSwordItem(ItemTierHolder tierHolder, Properties builder) {
		super(tierHolder.getDefaultTier(), swordDamage, swordSpeed, builder);
		this.tierHolder = tierHolder;
		this.attackDamage = swordDamage + tierHolder.getDamage();
		Builder<Attribute, AttributeModifier> attributeBuilder = ImmutableMultimap.builder();
		attributeBuilder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (double) this.attackDamage, AttributeModifier.Operation.ADDITION));
		attributeBuilder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", (double) swordSpeed, AttributeModifier.Operation.ADDITION));
		this.defaultModifiers = attributeBuilder.build();
	}

	@Override
	public float getDamage() {
		return swordDamage + this.tierHolder.getDamage();
	}
}
