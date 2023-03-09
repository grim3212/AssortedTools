package com.grim3212.assorted.decor.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.grim3212.assorted.decor.api.item.ITiered;
import com.grim3212.assorted.decor.common.entity.BetterSpearEntity;
import com.grim3212.assorted.decor.config.ItemTierConfig;
import com.grim3212.assorted.lib.core.item.ExtraPropertyHelper;
import com.grim3212.assorted.lib.core.item.IItemEnchantmentCondition;
import com.grim3212.assorted.lib.core.item.IItemExtraProperties;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class BetterSpearItem extends TridentItem implements ITiered, IItemExtraProperties, IItemEnchantmentCondition {

    private final ItemTierConfig tierHolder;

    public BetterSpearItem(Properties props, ItemTierConfig tierHolder) {
        super(props.defaultDurability(tierHolder.getDefaultTier().getUses()));
        this.tierHolder = tierHolder;
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
            return ExtraPropertyHelper.canApplyAtEnchantingTable(stack, enchantment);
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

    @Override
    public ItemTierConfig getTierHolder() {
        return tierHolder;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return this.tierHolder.getMaxUses();
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return ExtraPropertyHelper.isDamaged(stack);
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        ExtraPropertyHelper.setDamage(stack, damage);
    }

    @Override
    public int getDamage(ItemStack stack) {
        return ExtraPropertyHelper.getDamage(stack);
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
