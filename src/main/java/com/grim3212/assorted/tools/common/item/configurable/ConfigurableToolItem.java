package com.grim3212.assorted.tools.common.item.configurable;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.grim3212.assorted.tools.common.handler.ItemTierHolder;

import net.minecraft.tags.Tag;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ConfigurableToolItem extends DiggerItem {

	private final ItemTierHolder tierHolder;
	private final float toolDamage;

	public ConfigurableToolItem(ItemTierHolder tierHolder, float attackDamageIn, float attackSpeedIn, Tag<Block> effectiveBlocksIn, Properties builderIn) {
		super(attackDamageIn, attackSpeedIn, tierHolder.getDefaultTier(), effectiveBlocksIn, builderIn);
		this.tierHolder = tierHolder;
		this.toolDamage = attackDamageIn;
		// Set these values again so that our configurable values are the ones used
		this.attackDamageBaseline = attackDamageIn + tierHolder.getDamage();
		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", (double) this.attackDamageBaseline, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double) attackSpeedIn, AttributeModifier.Operation.ADDITION));
		this.defaultModifiers = builder.build();
	}

	public ItemTierHolder getTierHolder() {
		return tierHolder;
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return this.tierHolder.getMaxUses();
	}

	@Override
	public float getAttackDamage() {
		return this.toolDamage + this.tierHolder.getDamage();
	}

	@Override
	public int getItemEnchantability(ItemStack stack) {
		return this.tierHolder.getEnchantability();
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		return this.blocks.contains(state.getBlock()) ? this.tierHolder.getEfficiency() : 1.0F;
	}

	public int getTierHarvestLevel() {
		return this.tierHolder.getHarvestLevel();
	}
}
