package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.api.item.ToolsArmorMaterials;
import com.grim3212.assorted.tools.common.item.configurable.ConfigurableArmorItem;
import net.minecraft.world.item.ArmorItem;

public class MaterialArmorItem extends ConfigurableArmorItem {
    
    public MaterialArmorItem(ToolsArmorMaterials material, ArmorItem.Type type, Properties builderIn) {
        super(material, type, builderIn);
    }
}