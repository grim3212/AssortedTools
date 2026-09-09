package com.grim3212.assorted.tools.compat.jei;

import com.grim3212.assorted.tools.Constants;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;

@JeiPlugin
public class JEIAssortedTools implements IModPlugin {

    private static final Identifier PLUGIN_ID = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "assets/assortedtools");

    @Override
    public Identifier getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        // Enchantments live in a registry now, so building an enchanted display stack needs a
        // lookup. JEI hands one over in its context map under the vanilla slot-display key.
        HolderLookup.Provider registries = registration.getContextMap().getOptional(SlotDisplayContext.REGISTRIES);
        if (registries == null) {
            return;
        }

        registration.addRecipes(RecipeTypes.ANVIL, AnvilRecipes.chickenEnchantRecipes(registration.getVanillaRecipeFactory(), registration.getIngredientManager(), registries));
    }
}
