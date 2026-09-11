package com.grim3212.assorted.tools.mixin.loot;

import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Makes an item predicate for vanilla shears also accept the mod's, so loot tables and advancements
 * treat them alike. Only the item check is redirected, so the count and component checks still see
 * the real stack. Fabric only; NeoForge uses item abilities.
 */
@Mixin(ItemPredicate.class)
public abstract class ItemPredicateMixin {

    @Redirect(method = "test", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemInstance;is(Lnet/minecraft/core/HolderSet;)Z"))
    private boolean assortedtools_shearsMatch(ItemInstance stack, HolderSet<Item> items) {
        if (stack.is(items)) {
            return true;
        }

        // Tag only: ItemInstance exposes no item accessor, just is(...) overloads, so the
        // instanceof ShearsItem half of the check has no equivalent here. The common shears tag is
        // populated with every one of the mod's shears at datagen, so it covers the same set.
        boolean wantsShears = items.contains(Items.SHEARS.builtInRegistryHolder());
        return wantsShears && stack.is(LibCommonTags.Items.SHEARS);
    }
}
