package com.grim3212.assorted.tools.mixin.item;

import com.grim3212.assorted.tools.common.item.MultiToolItem;
import net.minecraft.world.item.ItemInstance;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Tells NeoForge that the multitool can do everything an axe, shovel, hoe, pickaxe or sword can.
 * <p>
 * Without this the multitool's right-click behaviour is silently dead on NeoForge.
 * {@code MultiToolItem#useOn} delegates to vanilla's axe, shovel and hoe, and NeoForge patches all
 * three to go through {@code BlockState#getToolModifiedState}, whose very first line is
 * {@code if (!itemStack.canPerformAction(itemAbility)) return null;}. The default
 * {@code canPerformAction} answers false to everything except sword sweeping, so every strip,
 * scrape, wax off, path and till would return {@code PASS} and nothing would happen. On Fabric,
 * where vanilla's own implementations run unpatched, the delegation works on its own.
 * <p>
 * This is all that survives of the old NeoForge {@code MultiToolItemMixin}, which reimplemented the
 * whole of {@code useOn} on top of {@code ToolActions}. That class is gone; {@link ItemAbility} is
 * its successor, and the behaviour itself now lives in {@code common}.
 * <p>
 * It stays a mixin because {@code canPerformAction} is a NeoForge extension method that does not
 * exist on the vanilla jar {@code common} compiles against.
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
