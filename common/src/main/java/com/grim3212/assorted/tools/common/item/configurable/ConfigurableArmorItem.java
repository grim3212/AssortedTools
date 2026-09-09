package com.grim3212.assorted.tools.common.item.configurable;

import com.grim3212.assorted.tools.api.item.ToolsArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;

/**
 * A piece of armour built from a configured armour material.
 * <p>
 * {@code ArmorItem} is gone. Armour is a plain {@link Item} carrying an {@code equippable}
 * component, which {@code Properties#humanoidArmor} writes along with the durability, defence,
 * toughness, knockback resistance, enchantability and repair material taken from the record. That
 * covers every override this class used to carry, including the per-slot durability multiplier and
 * the armour modifier UUIDs it reached into vanilla with accessor mixins to get.
 */
public class ConfigurableArmorItem extends Item {

    private final ToolsArmorMaterials toolMaterial;
    private final ArmorType armorType;

    public ConfigurableArmorItem(ToolsArmorMaterials toolMaterial, ArmorType type, Properties builderIn) {
        super(builderIn.humanoidArmor(toolMaterial.config().material(), type));
        this.toolMaterial = toolMaterial;
        this.armorType = type;
    }

    public ToolsArmorMaterials getArmorMaterial() {
        return this.toolMaterial;
    }

    public ArmorType getArmorType() {
        return this.armorType;
    }
}
