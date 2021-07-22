package com.grim3212.assorted.tools.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.grim3212.assorted.tools.client.render.item.ItemRendererProvider;
import com.grim3212.assorted.tools.common.entity.BetterSpearEntity;
import com.grim3212.assorted.tools.common.handler.ItemTierHolder;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class BetterSpearItem extends TridentItem {

	private final ItemTierHolder tierHolder;
	private final boolean isExtraMaterial;
	private final Multimap<Attribute, AttributeModifier> defaultModifiers;

	public BetterSpearItem(Properties props, ItemTierHolder tierHolder) {
		this(props, tierHolder, false);
	}

	public BetterSpearItem(Properties props, ItemTierHolder tierHolder, boolean extraMaterial) {
		super(props.defaultDurability(tierHolder.getMaxUses()).setISTER(ItemRendererProvider::spear));
		this.isExtraMaterial = extraMaterial;
		this.tierHolder = tierHolder;
		
		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", (double) 2.0D + tierHolder.getDamage(), AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double) -2.4f, AttributeModifier.Operation.ADDITION));
		this.defaultModifiers = builder.build();
	}

	@Override
	protected boolean allowdedIn(ItemGroup group) {
		if (this.isExtraMaterial) {
			return ToolsConfig.COMMON.betterSpearsEnabled.get() && ToolsConfig.COMMON.extraMaterialsEnabled.get() ? super.allowdedIn(group) : false;
		}

		return ToolsConfig.COMMON.betterSpearsEnabled.get() ? super.allowdedIn(group) : false;
	}

	@Override
	public void releaseUsing(ItemStack stack, World level, LivingEntity entity, int ticks) {
		if (entity instanceof PlayerEntity) {
			PlayerEntity playerentity = (PlayerEntity) entity;
			int i = this.getUseDuration(stack) - ticks;
			if (i >= 10) {
				if (!level.isClientSide) {
					stack.hurtAndBreak(1, playerentity, (p_220047_1_) -> {
						p_220047_1_.broadcastBreakEvent(entity.getUsedItemHand());
					});
					BetterSpearEntity spearEntity = new BetterSpearEntity(level, playerentity, stack);
					spearEntity.shootFromRotation(playerentity, playerentity.xRot, playerentity.yRot, 0.0F, 2.5F, 1.0F);
					if (playerentity.abilities.instabuild) {
						spearEntity.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
					}

					level.addFreshEntity(spearEntity);
					level.playSound((PlayerEntity) null, spearEntity, SoundEvents.ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F);
					if (!playerentity.abilities.instabuild) {
						playerentity.inventory.removeItem(stack);
					}
				}

				playerentity.awardStat(Stats.ITEM_USED.get(this));
			}
		}
	}

	@Override
	public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
			return ActionResult.fail(itemstack);
		} else {
			player.startUsingItem(hand);
			return ActionResult.consume(itemstack);
		}
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		if (enchantment != Enchantments.CHANNELING && enchantment != Enchantments.RIPTIDE) {
			return super.canApplyAtEnchantingTable(stack, enchantment);
		}

		return false;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType slot) {
		return slot == EquipmentSlotType.MAINHAND ? this.defaultModifiers : ImmutableMultimap.of();
	}

	public ItemTierHolder getTierHolder() {
		return tierHolder;
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return this.tierHolder.getMaxUses();
	}

	@Override
	public int getEnchantmentValue() {
		return this.tierHolder.getEnchantability();
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
		return this.tierHolder.getDefaultTier().getRepairIngredient().test(repair) || super.isValidRepairItem(toRepair, repair);
	}
}
