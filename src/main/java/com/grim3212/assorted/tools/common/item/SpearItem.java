package com.grim3212.assorted.tools.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.grim3212.assorted.tools.common.entity.SpearEntity;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import com.grim3212.assorted.tools.common.util.SpearType;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class SpearItem extends Item {

	private final SpearType type;
	private final Multimap<Attribute, AttributeModifier> defaultModifiers;

	public SpearItem(Properties props, SpearType type) {
		super(props.stacksTo(16));
		this.type = type;
		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.type.getItemDamage(), AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.8f, AttributeModifier.Operation.ADDITION));
		this.defaultModifiers = builder.build();
	}

	@Override
	protected boolean allowdedIn(ItemGroup group) {
		return ToolsConfig.COMMON.spearsEnabled.get() ? super.allowdedIn(group) : false;
	}

	@Override
	public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
		level.playSound(player, player.blockPosition(), SoundEvents.ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 0.8F));
		if (!level.isClientSide) {
			SpearEntity spear = new SpearEntity(player, level, type);
			spear.shootFromRotation(player, player.xRot, player.yRot, 0.0F, type == SpearType.SLIME ? 1.4F : 1.4F + (float) (type.getDamage() / 2), 1.2F);
			level.addFreshEntity(spear);
		}

		if (!player.isCreative()) {
			player.getItemInHand(hand).shrink(1);
		}

		return ActionResult.success(player.getItemInHand(hand));
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType slot) {
		return slot == EquipmentSlotType.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
	}
}
