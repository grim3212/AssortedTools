package com.grim3212.assorted.decor.data;

import com.grim3212.assorted.decor.Constants;
import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

public class ToolsCommonRecipeProvider extends ConditionalRecipeProvider {

    public ToolsCommonRecipeProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    public void registerConditions() {

    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        super.buildRecipes(consumer);
    }

}
