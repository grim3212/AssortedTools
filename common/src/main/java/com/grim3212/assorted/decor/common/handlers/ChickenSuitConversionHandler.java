package com.grim3212.assorted.decor.common.handlers;

import com.grim3212.assorted.decor.common.enchantment.ToolsEnchantments;
import com.grim3212.assorted.decor.common.item.ChickenSuitArmor;
import com.grim3212.assorted.lib.events.AnvilUpdatedEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class ChickenSuitConversionHandler {

    public static void anvilUpdateEvent(AnvilUpdatedEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        if (right.getItem() instanceof ChickenSuitArmor && (left.getItem() instanceof ArmorItem && !(left.getItem() instanceof ChickenSuitArmor))) {
            ArmorItem convertArmor = (ArmorItem) left.getItem();
            ChickenSuitArmor chickenArmor = (ChickenSuitArmor) right.getItem();

            if (chickenArmor.getSlot() == convertArmor.getSlot()) {
                if (ToolsEnchantments.CHICKEN_JUMP.get().canEnchant(left)) {

                    ItemStack output = left.copy();
                    Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(output);
                    enchantments.put(ToolsEnchantments.CHICKEN_JUMP.get(), 1);
                    EnchantmentHelper.setEnchantments(enchantments, output);

                    int cost = 0;
                    if (!StringUtils.isBlank(event.getName()) && !event.getName().equals(output.getHoverName().toString())) {
                        cost++;

                        output.setHoverName(Component.literal(event.getName()));
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
