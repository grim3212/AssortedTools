package com.grim3212.assorted.decor.common.item;

import com.grim3212.assorted.decor.api.item.ToolsArmorMaterials;
import com.grim3212.assorted.decor.common.item.configurable.ConfigurableArmorItem;
import net.minecraft.world.entity.EquipmentSlot;

public class MaterialArmorItem extends ConfigurableArmorItem {

    private final ToolsArmorMaterials material;

    public MaterialArmorItem(ToolsArmorMaterials material, EquipmentSlot slot, Properties builderIn) {
        super(material, slot, builderIn);
        this.material = material;
    }
}