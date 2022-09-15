package com.grim3212.assorted.tools.common.item;

import java.util.function.Consumer;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.grim3212.assorted.tools.client.render.item.SpearBEWLR;
import com.grim3212.assorted.tools.common.entity.BetterSpearEntity;
import com.grim3212.assorted.tools.common.handler.ItemTierHolder;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import com.grim3212.assorted.tools.common.util.ToolsItemTier;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.ForgeRegistries;

public class BetterSpearItem extends TridentItem {

	private final ItemTierHolder tierHolder;
	private final boolean isExtraMaterial;

	public BetterSpearItem(Properties props, ItemTierHolder tierHolder) {
		this(props, tierHolder, false);
	}

	public BetterSpearItem(Properties props, ItemTierHolder tierHolder, boolean extraMaterial) {
		super(props);
		this.isExtraMaterial = extraMaterial;
		this.tierHolder = tierHolder;

	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return SpearBEWLR.SPEAR_ITEM_RENDERER;
			}
		});
	}

	@Override
	protected boolean allowedIn(CreativeModeTab group) {
		if (ToolsConfig.COMMON.betterSpearsEnabled.get()) {
			
			if (this.isExtraMaterial) {
				if (!ToolsConfig.COMMON.extraMaterialsEnabled.get()) {
					return false;
				}

				ToolsItemTier tier = (ToolsItemTier) tierHolder.getDefaultTier();
				if (ToolsConfig.COMMON.hideUncraftableItems.get() && ForgeRegistries.ITEMS.tags().getTag(tier.repairTag()).size() <= 0) {
					return false;
				}
			}

			return super.allowedIn(group);
		}
		return false;
	}

	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int ticks) {
		if (entity instanceof Player) {
			Player playerentity = (Player) entity;
			int i = this.getUseDuration(stack) - ticks;
			if (i >= 10) {
				if (!level.isClientSide) {
					stack.hurtAndBreak(1, playerentity, (p_220047_1_) -> {
						p_220047_1_.broadcastBreakEvent(entity.getUsedItemHand());
					});
					BetterSpearEntity spearEntity = new BetterSpearEntity(level, playerentity, stack);
					spearEntity.shootFromRotation(playerentity, playerentity.getXRot(), playerentity.getYRot(), 0.0F, 2.5F, 1.0F);
					if (playerentity.getAbilities().instabuild) {
						spearEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
					}

					level.addFreshEntity(spearEntity);
					level.playSound((Player) null, spearEntity, SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
					if (!playerentity.getAbilities().instabuild) {
						playerentity.getInventory().removeItem(stack);
					}
				}

				playerentity.awardStat(Stats.ITEM_USED.get(this));
			}
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
			return InteractionResultHolder.fail(itemstack);
		} else {
			player.startUsingItem(hand);
			return InteractionResultHolder.consume(itemstack);
		}
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		if (enchantment != Enchantments.CHANNELING && enchantment != Enchantments.RIPTIDE) {
			return super.canApplyAtEnchantingTable(stack, enchantment);
		}

		return false;
	}

	private Multimap<Attribute, AttributeModifier> attribs = null;

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		if (attribs == null) {
			Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", (double) 2.0D + tierHolder.getDamage(), AttributeModifier.Operation.ADDITION));
			builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double) -2.4f, AttributeModifier.Operation.ADDITION));
			this.attribs = builder.build();
		}

		return slot == EquipmentSlot.MAINHAND ? this.attribs : ImmutableMultimap.of();
	}

	public ItemTierHolder getTierHolder() {
		return tierHolder;
	}
	
	@Override
	public boolean isDamageable(ItemStack stack) {
		return true;
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
