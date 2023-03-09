package com.grim3212.assorted.decor.common.item;

import com.grim3212.assorted.decor.api.item.ToolsArmorMaterials;
import com.grim3212.assorted.decor.common.item.configurable.ConfigurableArmorItem;
import net.minecraft.world.entity.EquipmentSlot;

public class ChickenSuitArmor extends ConfigurableArmorItem {

    public ChickenSuitArmor(EquipmentSlot slot, Properties builderIn) {
        super(ToolsArmorMaterials.CHICKEN_SUIT, slot, builderIn);
    }
}
