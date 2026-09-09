package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.lib.core.item.IItemEnchantmentCondition;
import com.grim3212.assorted.tools.api.item.ITiered;
import com.grim3212.assorted.tools.common.entity.BetterSpearEntity;
import com.grim3212.assorted.tools.config.ItemTierConfig;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * A throwable spear made of a configured material.
 * <p>
 * Still a {@link TridentItem} - that class survives and still carries the charge-and-throw shape -
 * but everything numeric moved into data components fixed at construction, so the overridden
 * {@code getMaxDamage}, {@code getEnchantmentValue}, {@code isValidRepairItem} and
 * {@code getDefaultAttributeModifiers} are gone, along with the library's
 * {@code IItemExtraProperties} forwarding, which now does exactly what vanilla does natively.
 * <p>
 * Note that 26.2 ships its own spears, built through {@code Item.Properties#spear} on the new
 * kinetic weapon components. Those are a lunge weapon rather than a throwing weapon, so they are
 * not what this is; the trident shape remains the right base.
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
     * Riptide and Channeling are hard wired to the vanilla trident - riptide drives the player's
     * spin attack out of {@code TridentItem}, which this overrides away, and channeling's lightning
     * requires the projectile to literally be a {@code minecraft:trident} - so neither can ever do
     * anything on one of these. That is why 1.20.1 excluded them by identity.
     * <p>
     * There is no way to name an enchantment by id here: {@code Enchantment} is a record built from
     * data and this hook is handed the value rather than its {@code Holder}, so the two are
     * identified by the effect components that make them trident-only. Both markers are unique to
     * those enchantments in vanilla.
     */
    @Override
    public Optional<Boolean> assortedlib_canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment.effects().has(EnchantmentEffectComponents.TRIDENT_SPIN_ATTACK_STRENGTH) || enchantment.effects().has(EnchantmentEffectComponents.HIT_BLOCK)) {
            return Optional.of(false);
        }

        return Optional.of(enchantment.canEnchant(new ItemStack(Items.TRIDENT)));
    }

    @Override
    public ItemTierConfig getTierHolder() {
        return tierHolder;
    }
}
