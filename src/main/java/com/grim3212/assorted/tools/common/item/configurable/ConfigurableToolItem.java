package com.grim3212.assorted.tools.common.item.configurable;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Maps;
import com.grim3212.assorted.tools.common.handler.ItemTierHolder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraftforge.common.ToolType;

public abstract class ConfigurableToolItem extends ToolItem {

	private final ItemTierHolder tierHolder;
	private final float toolDamage;
	private final Map<ToolType, Integer> toolClasses = Maps.newHashMap();

	public ConfigurableToolItem(ItemTierHolder tierHolder, float attackDamageIn, float attackSpeedIn, Set<Block> effectiveBlocksIn, Properties builderIn) {
		super(attackDamageIn, attackSpeedIn, tierHolder.getDefaultTier(), effectiveBlocksIn, builderIn);
		this.tierHolder = tierHolder;
		this.toolDamage = attackDamageIn;
		// Set these values again so that our configurable values are the ones used
		this.attackDamage = attackDamageIn + tierHolder.getDamage();
		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", (double) this.attackDamage, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", (double) attackSpeedIn, AttributeModifier.Operation.ADDITION));
		this.toolAttributes = builder.build();
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
		if (getToolTypes(stack).stream().anyMatch(e -> state.isToolEffective(e)))
			return this.tierHolder.getEfficiency();
		return this.effectiveBlocks.contains(state.getBlock()) ? this.tierHolder.getEfficiency() : 1.0F;
	}

	public int getTierHarvestLevel() {
		return this.tierHolder.getHarvestLevel();
	}

	public abstract void addToolTypes(Map<ToolType, Integer> toolClasses, ItemStack stack);

	@Override
	public Set<ToolType> getToolTypes(ItemStack stack) {
		if (this.toolClasses.isEmpty()) {
			addToolTypes(toolClasses, stack);
		}
		return this.toolClasses.keySet();
	}

	@Override
	public int getHarvestLevel(ItemStack stack, ToolType tool, PlayerEntity player, BlockState blockState) {
		if (this.toolClasses.isEmpty()) {
			addToolTypes(toolClasses, stack);
		}
		return this.toolClasses.getOrDefault(tool, -1);
	}
}
