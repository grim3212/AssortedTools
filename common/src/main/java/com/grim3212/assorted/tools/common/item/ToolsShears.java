package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;

/**
 * The one answer to "does this stack count as shears". Vanilla checks {@code is(Items.SHEARS)} by
 * identity; NeoForge patches those sites to {@code canPerformAction(ItemAbilities.SHEARS_*)}, and
 * Fabric's mixins redirect them here. Kept in {@code common} so both loaders agree.
 */
public final class ToolsShears {

    private ToolsShears() {
    }

    /**
     * Whether {@code stack} should satisfy a vanilla {@code is(item)} check. Only widens a check
     * for shears, since the redirected call sites test other items too.
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
