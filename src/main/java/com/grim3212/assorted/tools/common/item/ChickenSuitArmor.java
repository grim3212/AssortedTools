package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import com.grim3212.assorted.tools.common.item.configurable.ConfigurableArmorItem;
import com.grim3212.assorted.tools.common.util.ToolsArmorMaterials;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ChickenSuitArmor extends ConfigurableArmorItem {

	public ChickenSuitArmor(EquipmentSlotType slot, Properties builderIn) {
		super(ToolsArmorMaterials.CHICKEN_SUIT, slot, builderIn);
	}

	@Override
	protected boolean allowdedIn(ItemGroup group) {
		return ToolsConfig.COMMON.chickenSuitEnabled.get() ? super.allowdedIn(group) : false;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		if (slot == EquipmentSlotType.LEGS) {
			return AssortedTools.MODID + ":textures/models/armor/chicken_suit_layer_2.png";
		} else {
			return AssortedTools.MODID + ":textures/models/armor/chicken_suit_layer_1.png";
		}
	}
}
