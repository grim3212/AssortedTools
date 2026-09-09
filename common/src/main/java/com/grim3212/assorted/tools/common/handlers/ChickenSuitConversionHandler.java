package com.grim3212.assorted.tools.common.handlers;

import com.grim3212.assorted.lib.events.AnvilUpdatedEvent;
import com.grim3212.assorted.tools.common.enchantment.ToolsEnchantments;
import com.grim3212.assorted.tools.common.item.ChickenSuitArmor;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.equipment.Equippable;
import org.apache.commons.lang3.StringUtils;

public class ChickenSuitConversionHandler {

    public static void anvilUpdateEvent(AnvilUpdatedEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        if (!(right.getItem() instanceof ChickenSuitArmor chickenArmor) || left.getItem() instanceof ChickenSuitArmor) {
            return;
        }

        // ArmorItem is gone; a piece of armour is any item carrying an equippable component whose
        // slot is an armour slot, so the "same armour type" test compares EquipmentSlots.
        Equippable equippable = left.get(DataComponents.EQUIPPABLE);
        if (equippable == null || !equippable.slot().isArmor()) {
            return;
        }

        EquipmentSlot slot = equippable.slot();
        if (slot != chickenArmor.getArmorType().getSlot()) {
            return;
        }

        // Enchantments are data now, so what an enchantment may be applied to lives in its
        // definition and has to be resolved through the registry rather than a canEnchant override.
        Holder<Enchantment> chickenJump = event.getPlayer().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ToolsEnchantments.CHICKEN_JUMP);
        if (!chickenJump.value().canEnchant(left)) {
            return;
        }

        ItemStack output = left.copy();
        EnchantmentHelper.updateEnchantments(output, mutable -> mutable.set(chickenJump, 1));

        int cost = 0;
        if (!StringUtils.isBlank(event.getName()) && !event.getName().equals(output.getHoverName().toString())) {
            cost++;

            // ItemStack#setHoverName is gone; the display name is the custom_name component.
            output.set(DataComponents.CUSTOM_NAME, Component.literal(event.getName()));
        }

        event.setOutput(output);
        event.setMaterialCost(1);

        switch (slot) {
            case HEAD -> event.setCost(cost + 2);
            case CHEST -> event.setCost(cost + 5);
            case LEGS -> event.setCost(cost + 4);
            case FEET -> event.setCost(cost + 2);
            default -> event.setCost(cost + 2);
        }
    }

}
