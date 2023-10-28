package com.grim3212.assorted.tools.compat.jei;

import com.grim3212.assorted.tools.Constants;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class JEIAssortedTools implements IModPlugin {

    private static final ResourceLocation PLUGIN_ID = new ResourceLocation(Constants.MOD_ID, "assets/assortedtools");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(RecipeTypes.ANVIL, AnvilRecipes.chickenEnchantRecipes(registration.getVanillaRecipeFactory(), registration.getIngredientManager()));
    }
}
