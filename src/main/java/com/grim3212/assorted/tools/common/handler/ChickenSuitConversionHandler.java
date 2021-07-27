package com.grim3212.assorted.tools.common.handler;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.grim3212.assorted.tools.common.enchantment.ToolsEnchantments;
import com.grim3212.assorted.tools.common.item.ChickenSuitArmor;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChickenSuitConversionHandler {

	@SubscribeEvent
	public void anvilUpdateEvent(AnvilUpdateEvent event) {
		ItemStack left = event.getLeft();
		ItemStack right = event.getRight();

		if (right.getItem() instanceof ChickenSuitArmor && (left.getItem() instanceof ArmorItem && !(left.getItem() instanceof ChickenSuitArmor))) {
			ArmorItem convertArmor = (ArmorItem) left.getItem();
			ChickenSuitArmor chickenArmor = (ChickenSuitArmor) right.getItem();

			if (chickenArmor.getSlot() == convertArmor.getSlot()) {
				if (convertArmor.canApplyAtEnchantingTable(left, ToolsEnchantments.CHICKEN_JUMP.get())) {

					ItemStack output = left.copy();
					Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(output);
					enchantments.put(ToolsEnchantments.CHICKEN_JUMP.get(), 1);
					EnchantmentHelper.setEnchantments(enchantments, output);

					int cost = 0;
					if (!StringUtils.isBlank(event.getName()) && !event.getName().equals(output.getHoverName().toString())) {
						cost++;

						output.setHoverName(new TextComponent(event.getName()));
					}

					event.setOutput(output);
					event.setMaterialCost(1);

					if (convertArmor.getSlot() == EquipmentSlot.HEAD) {
						event.setCost(cost + 2);
					} else if (convertArmor.getSlot() == EquipmentSlot.CHEST) {
						event.setCost(cost + 5);
					} else if (convertArmor.getSlot() == EquipmentSlot.LEGS) {
						event.setCost(cost + 4);
					} else if (convertArmor.getSlot() == EquipmentSlot.FEET) {
						event.setCost(cost + 2);
					}
				}
			}
		}

	}

}
