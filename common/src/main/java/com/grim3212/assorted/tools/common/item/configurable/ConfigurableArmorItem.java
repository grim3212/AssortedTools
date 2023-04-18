package com.grim3212.assorted.tools.common.item.configurable;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.grim3212.assorted.lib.core.item.ExtraPropertyHelper;
import com.grim3212.assorted.lib.core.item.IItemExtraProperties;
import com.grim3212.assorted.lib.mixin.item.ArmorItemAccessor;
import com.grim3212.assorted.tools.api.item.ToolsArmorMaterials;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class ConfigurableArmorItem extends ArmorItem implements IItemExtraProperties {

    private Multimap<Attribute, AttributeModifier> modifiers;
    private final ToolsArmorMaterials material;

    public ConfigurableArmorItem(ToolsArmorMaterials material, ArmorItem.Type type, Properties builderIn) {
        super(material.defaultMaterial(), type, builderIn);
        this.material = material;
    }

    @Override
    public int getEnchantmentValue() {
        return this.material.getEnchantmentValue();
    }

    @Override
    public int getDefense() {
        return this.material.getDefenseForType(this.type);
    }

    @Override
    public float getToughness() {
        return this.material.getToughness();
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return this.material.getDurabilityForType(this.type);
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
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        if (equipmentSlot == this.type.getSlot()) {
            return getModifiers(equipmentSlot);
        }
        return super.getDefaultAttributeModifiers(equipmentSlot);
    }

    public Multimap<Attribute, AttributeModifier> getModifiers(EquipmentSlot equipmentSlot) {
        if (this.modifiers == null || this.modifiers.isEmpty()) {
            Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            UUID uuid = ArmorItemAccessor.assortedlib_getArmorModPerSlot().get(this.type);
            builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "Armor modifier", getDefense(), AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Armor toughness", getToughness(), AttributeModifier.Operation.ADDITION));
            if (this.knockbackResistance > 0) {
                builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Armor knockback resistance", (double) this.material.getKnockbackResistance(), AttributeModifier.Operation.ADDITION));
            }

            this.modifiers = builder.build();
        }
        return modifiers;
    }
}
