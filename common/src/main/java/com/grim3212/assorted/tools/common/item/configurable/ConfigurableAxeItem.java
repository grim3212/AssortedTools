package com.grim3212.assorted.tools.common.item.configurable;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.grim3212.assorted.lib.core.item.ExtraPropertyHelper;
import com.grim3212.assorted.lib.core.item.IItemExtraProperties;
import com.grim3212.assorted.lib.mixin.item.DiggerItemAccessor;
import com.grim3212.assorted.tools.api.item.ITiered;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class ConfigurableAxeItem extends AxeItem implements ITiered, IItemExtraProperties {

    private final ItemTierConfig tierHolder;

    public ConfigurableAxeItem(ItemTierConfig tierHolder, Item.Properties builder) {
        super(tierHolder.getDefaultTier(), tierHolder.getDefaultTier().getAttackDamageBonus(), tierHolder.getDefaultTier().getSpeed(), builder);
        this.tierHolder = tierHolder;
    }

    private Multimap<Attribute, AttributeModifier> attribs = null;

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        if (attribs == null) {
            Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", (double) this.getAttackDamage(), AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double) this.tierHolder.getAxeSpeed(), AttributeModifier.Operation.ADDITION));
            this.attribs = builder.build();
        }

        return slot == EquipmentSlot.MAINHAND ? this.attribs : super.getDefaultAttributeModifiers(slot);
    }

    @Override
    public ItemTierConfig getTierHolder() {
        return tierHolder;
    }

    public float getAttackDamage() {
        return this.tierHolder.getAxeDamage() + this.tierHolder.getDamage();
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
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return state.is(((DiggerItemAccessor) this).getBlocks()) ? this.tierHolder.getEfficiency() : 1.0F;
    }

    public int getTierHarvestLevel() {
        return this.tierHolder.getHarvestLevel();
    }
}
