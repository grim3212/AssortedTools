package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import com.grim3212.assorted.tools.common.item.configurable.ConfigurableArmorItem;
import com.grim3212.assorted.tools.common.util.ToolsArmorMaterials;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class MaterialArmorItem extends ConfigurableArmorItem {

	private final ToolsArmorMaterials material;

	public MaterialArmorItem(ToolsArmorMaterials material, EquipmentSlotType slot, Properties builderIn) {
		super(material, slot, builderIn);
		this.material = material;
	}

	@Override
	protected boolean isInGroup(ItemGroup group) {
		return ToolsConfig.COMMON.extraMaterialsEnabled.get() ? super.isInGroup(group) : false;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		if (slot == EquipmentSlotType.LEGS) {
			return AssortedTools.MODID + ":textures/models/armor/" + this.material.getName() + "_layer_2.png";
		} else {
			return AssortedTools.MODID + ":textures/models/armor/" + this.material.getName() + "_layer_1.png";
		}
	}
}