package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import com.grim3212.assorted.tools.common.item.configurable.ConfigurableArmorItem;
import com.grim3212.assorted.tools.common.util.ToolsArmorMaterials;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.Item.Properties;

public class ChickenSuitArmor extends ConfigurableArmorItem {

	public ChickenSuitArmor(EquipmentSlot slot, Properties builderIn) {
		super(ToolsArmorMaterials.CHICKEN_SUIT, slot, builderIn);
	}

	@Override
	protected boolean allowdedIn(CreativeModeTab group) {
		return ToolsConfig.COMMON.chickenSuitEnabled.get() ? super.allowdedIn(group) : false;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		if (slot == EquipmentSlot.LEGS) {
			return AssortedTools.MODID + ":textures/models/armor/chicken_suit_layer_2.png";
		} else {
			return AssortedTools.MODID + ":textures/models/armor/chicken_suit_layer_1.png";
		}
	}
}
