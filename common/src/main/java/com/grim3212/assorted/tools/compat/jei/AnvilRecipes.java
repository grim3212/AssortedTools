package com.grim3212.assorted.tools.compat.jei;

import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.tools.common.enchantment.ToolsEnchantments;
import com.grim3212.assorted.tools.common.item.ChickenSuitArmor;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class AnvilRecipes {

    public static Map<TagKey<Item>, Supplier<ChickenSuitArmor>> CHICKEN_JUMP_MAP = Map.ofEntries(
            Map.entry(LibCommonTags.Items.ARMORS_HELMETS, ToolsItems.CHICKEN_SUIT_HELMET),
            Map.entry(LibCommonTags.Items.ARMORS_CHESTPLATES, ToolsItems.CHICKEN_SUIT_CHESTPLATE),
            Map.entry(LibCommonTags.Items.ARMORS_LEGGINGS, ToolsItems.CHICKEN_SUIT_LEGGINGS),
            Map.entry(LibCommonTags.Items.ARMORS_BOOTS, ToolsItems.CHICKEN_SUIT_BOOTS)
    );


    public static List<IJeiAnvilRecipe> chickenEnchantRecipes(IVanillaRecipeFactory recipeFactory, IIngredientManager ingredientManager) {
        List<IJeiAnvilRecipe> recipes = new ArrayList<>();

        CHICKEN_JUMP_MAP.forEach((tag, item) -> {
            var armors = ingredientManager.getAllItemStacks()
                    .stream()
                    .filter(i -> i.isEnchantable() && i.is(tag) && !(i.getItem() instanceof ChickenSuitArmor)).toList();

            var enchantedArmors = armors.stream().map(AnvilRecipes::getChickenEnchanted).toList();

            recipes.add(recipeFactory.createAnvilRecipe(armors, List.of(new ItemStack(item.get())), enchantedArmors));
        });


        return recipes;
    }

    private static ItemStack getChickenEnchanted(ItemStack ingredient) {
        ItemStack enchantedIngredient = ingredient.copy();
        Map<Enchantment, Integer> chickenEnchant = Map.of(ToolsEnchantments.CHICKEN_JUMP.get(), 1);
        EnchantmentHelper.setEnchantments(chickenEnchant, enchantedIngredient);
        return enchantedIngredient;
    }
}
