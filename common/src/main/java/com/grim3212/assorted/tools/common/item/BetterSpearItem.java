package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.lib.core.item.IItemEnchantmentCondition;
import com.grim3212.assorted.tools.api.item.ITiered;
import com.grim3212.assorted.tools.common.entity.BetterSpearEntity;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;


/**
 * A throwable spear made of a configured material; its stats are data components fixed at
 * construction. It extends {@link TridentItem} for the charge-and-throw: vanilla's own spears
 * ({@code Item.Properties#spear}) are lunge weapons, not thrown ones.
 */
public class BetterSpearItem extends TridentItem implements ITiered, IItemEnchantmentCondition {

    private static final float ATTACK_DAMAGE_BASE = 2.0F;
    private static final float ATTACK_SPEED = -2.4F;

    private final ItemTierConfig tierHolder;

    public BetterSpearItem(Properties props, ItemTierConfig tierHolder) {
        super(props.durability(tierHolder.getMaxUses())
                .repairable(tierHolder.material().repairItems())
                .enchantable(tierHolder.getEnchantability())
                .attributes(attributes(tierHolder))
                .component(DataComponents.TOOL, TridentItem.createToolProperties())
                .component(DataComponents.WEAPON, new Weapon(1)));
        this.tierHolder = tierHolder;
    }

    private static ItemAttributeModifiers attributes(ItemTierConfig tierHolder) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, ATTACK_DAMAGE_BASE + tierHolder.getDamage(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, ATTACK_SPEED, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity entity, int ticks) {
        if (!(entity instanceof Player playerentity)) {
            return false;
        }

        int i = this.getUseDuration(stack, entity) - ticks;
        if (i < 10) {
            return false;
        }

        if (!level.isClientSide()) {
            stack.hurtAndBreak(1, playerentity, entity.getUsedItemHand());

            BetterSpearEntity spearEntity = new BetterSpearEntity(level, playerentity, stack);
            spearEntity.shootFromRotation(playerentity, playerentity.getXRot(), playerentity.getYRot(), 0.0F, 2.5F, 1.0F);
            if (playerentity.getAbilities().instabuild) {
                spearEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }

            level.addFreshEntity(spearEntity);
            level.playSound(null, spearEntity, SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
            if (!playerentity.getAbilities().instabuild) {
                playerentity.getInventory().removeItem(stack);
            }
        }

        playerentity.awardStat(Stats.ITEM_USED.get(this));
        return true;
    }

    /**
     * Riptide and Channeling only work on the vanilla trident (the spin attack lives in
     * {@code TridentItem#releaseUsing}, which this overrides; the lightning needs a
     * {@code minecraft:trident} projectile), so they are vetoed. Everything else follows
     * {@code #minecraft:enchantable/trident}.
     */
    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return !isTridentOnly(enchantment) && IItemEnchantmentCondition.supportedByDefault(stack, enchantment);
    }

    @Override
    public boolean isPrimaryItemFor(ItemStack stack, Holder<Enchantment> enchantment) {
        return !isTridentOnly(enchantment) && IItemEnchantmentCondition.primaryByDefault(stack, enchantment);
    }

    private static boolean isTridentOnly(Holder<Enchantment> enchantment) {
        return enchantment.is(Enchantments.RIPTIDE) || enchantment.is(Enchantments.CHANNELING);
    }

    @Override
    public ItemTierConfig getTierHolder() {
        return tierHolder;
    }
}
