package com.grim3212.assorted.tools.common.item.configurable;

import com.grim3212.assorted.tools.api.item.ToolsArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;

/**
 * A piece of armour built from a configured armour material. {@code Properties#humanoidArmor}
 * writes the {@code equippable} component, durability, defence and the rest from the record.
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
