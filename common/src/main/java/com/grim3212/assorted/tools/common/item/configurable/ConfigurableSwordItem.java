package com.grim3212.assorted.tools.common.item.configurable;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.grim3212.assorted.lib.core.item.ExtraPropertyHelper;
import com.grim3212.assorted.lib.core.item.IItemExtraProperties;
import com.grim3212.assorted.tools.api.item.ITiered;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

public class ConfigurableSwordItem extends SwordItem implements ITiered, IItemExtraProperties {

    private final static int swordDamage = 3;
    private final static float swordSpeed = -2.4F;
    private final ItemTierConfig tierHolder;

    public ConfigurableSwordItem(ItemTierConfig tierHolder, Properties builder) {
        super(tierHolder.getDefaultTier(), swordDamage, swordSpeed, builder);
        this.tierHolder = tierHolder;
    }

    @Override
    public ItemTierConfig getTierHolder() {
        return tierHolder;
    }

    @Override
    public float getDamage() {
        return swordDamage + this.tierHolder.getDamage();
    }

    @Override
    public int getEnchantmentValue() {
        return this.tierHolder.getEnchantability();
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

    protected Multimap<Attribute, AttributeModifier> attribs;

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        if (attribs == null) {
            Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (double) this.getDamage(), AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", (double) swordSpeed, AttributeModifier.Operation.ADDITION));
            this.attribs = builder.build();
        }

        return slot == EquipmentSlot.MAINHAND ? this.attribs : super.getDefaultAttributeModifiers(slot);
    }
}
