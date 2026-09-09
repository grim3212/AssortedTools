package com.grim3212.assorted.tools.compat.jei;

import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.common.enchantment.ToolsEnchantments;
import com.grim3212.assorted.tools.common.item.ChickenSuitArmor;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;

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

    /**
     * Takes a {@link HolderLookup.Provider} because enchantments are registry content now: the mod
     * holds a {@link net.minecraft.resources.ResourceKey}, and writing one onto a stack needs the
     * {@link Holder} the registry resolves it to. The plugin gets the provider from JEI's context
     * map rather than reaching for a client-side registry access.
     */
    public static List<IJeiAnvilRecipe> chickenEnchantRecipes(IVanillaRecipeFactory recipeFactory, IIngredientManager ingredientManager, HolderLookup.Provider registries) {
        List<IJeiAnvilRecipe> recipes = new ArrayList<>();

        Holder<Enchantment> chickenJump = registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ToolsEnchantments.CHICKEN_JUMP);

        CHICKEN_JUMP_MAP.forEach((tag, item) -> {
            var armors = ingredientManager.getAllItemStacks()
                    .stream()
                    .filter(i -> i.isEnchantable() && i.is(tag) && !(i.getItem() instanceof ChickenSuitArmor)).toList();

            var enchantedArmors = armors.stream().map((stack) -> getChickenEnchanted(stack, chickenJump)).toList();

            // Every JEI recipe carries a unique id of its own now - it used to be derived from the
            // ingredients. Keyed by the armour slot's tag, which is what makes these four distinct.
            Identifier uid = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "chicken_jump/" + tag.location().getNamespace() + "/" + tag.location().getPath().replace('/', '_'));

            recipes.add(recipeFactory.createAnvilRecipe(armors, List.of(new ItemStack(item.get())), enchantedArmors, uid));
        });

        return recipes;
    }

    /**
     * {@code EnchantmentHelper.setEnchantments(Map, stack)} is gone - enchantments are the
     * {@code minecraft:enchantments} data component, an {@link ItemEnchantments}, and the argument
     * order flipped to put the stack first.
     */
    private static ItemStack getChickenEnchanted(ItemStack ingredient, Holder<Enchantment> chickenJump) {
        ItemStack enchantedIngredient = ingredient.copy();

        ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        enchantments.set(chickenJump, 1);
        EnchantmentHelper.setEnchantments(enchantedIngredient, enchantments.toImmutable());

        return enchantedIngredient;
    }
}
