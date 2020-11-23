package com.grim3212.assorted.tools.common.item;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.grim3212.assorted.tools.common.handler.ItemTierHolder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraftforge.common.ToolType;

public abstract class ConfigurableToolItem extends ToolItem {

	private final float attackSpeed;
	private final ItemTierHolder tierHolder;
	private Multimap<Attribute, AttributeModifier> modifiers;
	private final Map<ToolType, Integer> toolClasses = Maps.newHashMap();

	public ConfigurableToolItem(ItemTierHolder tierHolder, float attackSpeedIn, Set<Block> effectiveBlocksIn, Properties builderIn) {
		super(tierHolder.getDefaultTier().get().getAttackDamage(), attackSpeedIn, tierHolder.getDefaultTier().get(), effectiveBlocksIn, builderIn);
		this.tierHolder = tierHolder;
		this.attackSpeed = attackSpeedIn;
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
		return this.tierHolder.getDamage();
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

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		return slot == EquipmentSlotType.MAINHAND ? this.getModifiers(slot) : super.getAttributeModifiers(slot);
	}

	public Multimap<Attribute, AttributeModifier> getModifiers(EquipmentSlotType equipmentSlot) {
		if (this.modifiers == null || this.modifiers.isEmpty()) {
			Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", (double) this.getAttackDamage(), AttributeModifier.Operation.ADDITION));
			builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", (double) this.attackSpeed, AttributeModifier.Operation.ADDITION));

			this.modifiers = builder.build();
		}
		return modifiers;
	}
}
