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
 * Makes a predicate that asks for vanilla shears also accept the mod's, so loot tables and
 * advancements written against {@code minecraft:shears} behave the same with a modded pair.
 * <p>
 * The 1.20.1 version substituted a whole fake {@code ItemStack} of vanilla shears and copied the
 * original's NBT onto it. That cannot work now - stack data is components, and the predicate's
 * {@code components} matcher would have been tested against the substitute rather than the real
 * stack. Redirecting the single item check instead leaves the count and component checks looking at
 * the stack the player is actually holding.
 * <p>
 * {@code items} is an {@code Optional<HolderSet<Item>>} on the record now, not a {@code Set<Item>},
 * so the widening is expressed against the holder set.
 * <p>
 * Fabric only, matching the block mixins: NeoForge routes shears behaviour through item abilities.
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
