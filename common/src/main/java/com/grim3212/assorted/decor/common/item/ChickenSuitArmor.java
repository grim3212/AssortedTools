package com.grim3212.assorted.decor.common.item;

import com.grim3212.assorted.decor.Constants;
import com.grim3212.assorted.decor.api.item.ToolsArmorMaterials;
import com.grim3212.assorted.decor.common.item.configurable.ConfigurableArmorItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class ChickenSuitArmor extends ConfigurableArmorItem {

    public ChickenSuitArmor(EquipmentSlot slot, Properties builderIn) {
        super(ToolsArmorMaterials.CHICKEN_SUIT, slot, builderIn);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        if (slot == EquipmentSlot.LEGS) {
            return Constants.MOD_ID + ":textures/models/armor/chicken_suit_layer_2.png";
        } else {
            return Constants.MOD_ID + ":textures/models/armor/chicken_suit_layer_1.png";
        }
    }
}
