package com.grim3212.assorted.tools.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class UltimateFistItem extends Item {

	public UltimateFistItem(Properties props) {
		super(props.defaultDurability(ToolsConfig.COMMON.ultimateItemTier.getDefaultTier().getUses()).setNoRepair().fireResistant());
	}

	@Override
	protected boolean allowedIn(CreativeModeTab group) {
		return ToolsConfig.COMMON.ultimateFistEnabled.get() ? super.allowedIn(group) : false;
	}

	private Multimap<Attribute, AttributeModifier> attribs = null;

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		if (attribs == null) {
			Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", (double) ToolsConfig.COMMON.ultimateItemTier.getDamage(), AttributeModifier.Operation.ADDITION));
			builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double) -0.5F, AttributeModifier.Operation.ADDITION));
			this.attribs = builder.build();
		}

		return slot == EquipmentSlot.MAINHAND ? this.attribs : super.getDefaultAttributeModifiers(slot);
	}

	@Override
	public boolean isFoil(ItemStack stsack) {
		return true;
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return ToolsConfig.COMMON.ultimateItemTier.getMaxUses();
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public int getEnchantmentValue(ItemStack stack) {
		return 0;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		return ToolsConfig.COMMON.ultimateItemTier.getEfficiency();
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity entity, LivingEntity user) {
		stack.hurtAndBreak(1, user, (ent) -> {
			ent.broadcastBreakEvent(EquipmentSlot.MAINHAND);
		});
		return true;
	}

	@Override
	public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity user) {
		if (!level.isClientSide && state.getDestroySpeed(level, pos) != 0.0F) {
			stack.hurtAndBreak(1, user, (ent) -> {
				ent.broadcastBreakEvent(EquipmentSlot.MAINHAND);
			});
		}

		return true;
	}

	@Override
	public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
		return true;
	}

}
