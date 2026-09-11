package com.grim3212.assorted.tools.data;

import com.grim3212.assorted.lib.data.LibEnchantmentTagProvider;
import com.grim3212.assorted.tools.common.enchantment.ToolsEnchantments;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * In 1.20.1 all six of this mod's enchantments were discoverable and tradeable while their part was
 * enabled. The part switch is a condition on each definition now ({@link ToolsEnchantmentData#conditions()}),
 * so these tag entries only have to say "obtainable" - a disabled part's enchantments are not
 * registered and drop out of the tags on their own.
 */
public class ToolsEnchantmentTagProvider extends LibEnchantmentTagProvider {

    public ToolsEnchantmentTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        obtainable(List.of(
                ToolsEnchantments.CHICKEN_JUMP, ToolsEnchantments.BOUNCINESS, ToolsEnchantments.CONDUCTIVE,
                ToolsEnchantments.FLAMMABLE, ToolsEnchantments.UNSTABLE, ToolsEnchantments.CORAL_CUTTER));
    }
}
