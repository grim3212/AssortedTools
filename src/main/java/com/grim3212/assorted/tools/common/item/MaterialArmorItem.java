package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.item.configurable.ConfigurableArmorItem;
import com.grim3212.assorted.tools.common.util.ToolsArmorMaterials;

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
			return AssortedTools.MODID + ":textures/models/armor/" + this.material.getName() + "_layer_2.png";
		} else {
			return AssortedTools.MODID + ":textures/models/armor/" + this.material.getName() + "_layer_1.png";
		}
	}
}