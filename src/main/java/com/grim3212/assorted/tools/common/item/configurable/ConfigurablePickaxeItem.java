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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.block.state.BlockState;

public class ConfigurablePickaxeItem extends PickaxeItem implements ITiered {

	private static final float PICKAXE_DAMAGE = 3.0F;
	private static final float PICKAXE_SPEED = -2.4F;

	private final ItemTierHolder tierHolder;

	public ConfigurablePickaxeItem(ItemTierHolder tierHolder, Item.Properties builder) {
		super(tierHolder.getDefaultTier(), (int) PICKAXE_DAMAGE, PICKAXE_SPEED, builder);
		this.tierHolder = tierHolder;
	}

	private Multimap<Attribute, AttributeModifier> attribs = null;

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		if (attribs == null) {
			Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", (double) this.getAttackDamage(), AttributeModifier.Operation.ADDITION));
			builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double) PICKAXE_SPEED, AttributeModifier.Operation.ADDITION));
			this.attribs = builder.build();
		}

		return slot == EquipmentSlot.MAINHAND ? this.attribs : super.getDefaultAttributeModifiers(slot);
	}

	@Override
	public ItemTierHolder getTierHolder() {
		return tierHolder;
	}

	public float getAttackDamage() {
		return PICKAXE_DAMAGE + this.tierHolder.getDamage();
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return this.tierHolder.getMaxUses();
	}

	@Override
	public int getEnchantmentValue(ItemStack stack) {
		return this.tierHolder.getEnchantability();
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		return state.is(this.blocks) ? this.tierHolder.getEfficiency() : 1.0F;
	}

	public int getTierHarvestLevel() {
		return this.tierHolder.getHarvestLevel();
	}
}
