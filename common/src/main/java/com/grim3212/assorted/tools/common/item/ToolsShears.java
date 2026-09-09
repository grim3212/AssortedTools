package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;

/**
 * The one place that answers "should this stack count as shears".
 * <p>
 * Vanilla asks {@code itemStack.is(Items.SHEARS)} by identity in every place shears do something -
 * carving a pumpkin, harvesting a hive, disarming a tripwire, shearing a mob - so a modded pair is
 * invisible to all of it. NeoForge patches those call sites to
 * {@code canPerformAction(ItemAbilities.SHEARS_*)}, which {@code MaterialShears} inherits from
 * {@code ShearsItem}; Fabric has no equivalent, so the mixins there redirect those calls here.
 * <p>
 * Kept in {@code common} so both loaders agree on the answer even though only one of them asks.
 */
public final class ToolsShears {

    private ToolsShears() {
    }

    /**
     * Whether {@code stack} should satisfy a vanilla {@code is(item)} check.
     * <p>
     * Only widens the check when vanilla was asking about shears specifically. The redirected call
     * sites also test other items, so answering for those would change unrelated behaviour.
     */
    public static boolean matches(ItemStack stack, Object item) {
        // Object rather than Item because `is` is declared on the generic TypedInstance<T>: the
        // redirected call sites carry the erased `is(Object)` descriptor, and Mixin matches the
        // handler against that. Only `is(T rawType)` erases this way - `is(TagKey)`, `is(Holder)`
        // and the rest keep their own descriptors - so the argument is always an Item here.
        Item asItem = (Item) item;

        if (asItem != Items.SHEARS) {
            return stack.is(asItem);
        }

        return stack.is(asItem) || stack.getItem() instanceof ShearsItem || stack.is(LibCommonTags.Items.SHEARS);
    }
}
