package com.grim3212.assorted.tools.mixin.item;

import com.grim3212.assorted.tools.common.item.MultiToolItem;
import net.minecraft.world.item.ItemInstance;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Tells NeoForge the multitool can do everything an axe, shovel, hoe, pickaxe or sword can.
 * NeoForge's patched axe, shovel and hoe {@code useOn} return early unless
 * {@code canPerformAction} allows the ability, so without this the right-click does nothing on
 * NeoForge. A mixin because {@code canPerformAction} is not on the vanilla jar.
 */
@Mixin(MultiToolItem.class)
public class MultiToolItemMixin {

    public boolean canPerformAction(ItemInstance stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_AXE_ACTIONS.contains(itemAbility)
                || ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(itemAbility)
                || ItemAbilities.DEFAULT_HOE_ACTIONS.contains(itemAbility)
                // The 1.20.1 version also claimed DEFAULT_PICKAXE_ACTIONS and DEFAULT_SWORD_ACTIONS.
                // Neither set exists any more - there are no pickaxe abilities, and the only sword
                // one is sweeping, which is named directly so the multitool keeps sweeping.
                || ItemAbilities.SWORD_SWEEP == itemAbility;
    }
}
