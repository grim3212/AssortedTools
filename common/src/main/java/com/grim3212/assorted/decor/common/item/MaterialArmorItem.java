package com.grim3212.assorted.decor.common.item;

import com.grim3212.assorted.decor.Constants;
import com.grim3212.assorted.decor.api.item.ToolsArmorMaterials;
import com.grim3212.assorted.decor.common.item.configurable.ConfigurableArmorItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class MaterialArmorItem extends ConfigurableArmorItem {

    private final ToolsArmorMaterials material;

    public MaterialArmorItem(ToolsArmorMaterials material, EquipmentSlot slot, Properties builderIn) {
        super(material, slot, builderIn);
        this.material = material;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        if (slot == EquipmentSlot.LEGS) {
            return Constants.MOD_ID + ":textures/models/armor/" + this.material.getName() + "_layer_2.png";
        } else {
            return Constants.MOD_ID + ":textures/models/armor/" + this.material.getName() + "_layer_1.png";
        }
    }
}