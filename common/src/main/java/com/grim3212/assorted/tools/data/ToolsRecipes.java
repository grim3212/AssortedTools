package com.grim3212.assorted.tools.data;

import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.api.ToolsTags;
import com.grim3212.assorted.tools.common.crafting.ToolsConditions;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class ToolsRecipes extends ConditionalRecipeProvider {

    private final HolderGetter<Item> items;

    public ToolsRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output, Constants.MOD_ID);
        this.items = registries.lookupOrThrow(Registries.ITEM);
    }

    @Override
    public void registerConditions() {
        this.addConditions(partEnabled(ToolsConditions.Parts.BOOMERANGS), ToolsItems.WOOD_BOOMERANG.getId(), ToolsItems.DIAMOND_BOOMERANG.getId());
        this.addConditions(partEnabled(ToolsConditions.Parts.POKEBALL), ToolsItems.POKEBALL.getId());
        this.addConditions(partEnabled(ToolsConditions.Parts.WANDS), ToolsItems.BUILDING_WAND.getId(), ToolsItems.BREAKING_WAND.getId(), ToolsItems.MINING_WAND.getId(), ToolsItems.REINFORCED_BUILDING_WAND.getId(), ToolsItems.REINFORCED_BREAKING_WAND.getId(), ToolsItems.REINFORCED_MINING_WAND.getId());
        this.addConditions(partEnabled(ToolsConditions.Parts.ULTIMATE_FIST), ToolsItems.ULTIMATE_FIST.getId());

        this.addConditions(partEnabled(ToolsConditions.Parts.MULTITOOL), Identifier.parse(ToolsItems.NETHERITE_MULTITOOL.getId() + "_smithing"));
        this.addConditions(partEnabled(ToolsConditions.Parts.HAMMERS), Identifier.parse(ToolsItems.NETHERITE_HAMMER.getId() + "_smithing"));
        this.addConditions(partEnabled(ToolsConditions.Parts.THROWING_SPEARS), Identifier.parse(ToolsItems.NETHERITE_THROWING_SPEAR.getId() + "_smithing"));
        this.addConditions(partEnabled(ToolsConditions.Parts.BETTER_BUCKETS), Identifier.parse(ToolsItems.NETHERITE_BUCKET.getId() + "_smithing"));
        this.addConditions(partEnabled(ToolsConditions.Parts.MORE_SHEARS), Identifier.parse(ToolsItems.NETHERITE_SHEARS.getId() + "_smithing"));

        this.addConditions(partEnabled(ToolsConditions.Parts.BETTER_BUCKETS), Identifier.parse(key(Blocks.CAKE.asItem()) + "_alt"));
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        hammerPattern(ToolsItems.NETHERITE_HAMMER.get(), LibCommonTags.Items.INGOTS_NETHERITE);
        hammerPattern(ToolsItems.DIAMOND_HAMMER.get(), LibCommonTags.Items.GEMS_DIAMOND);
        hammerPattern(ToolsItems.IRON_HAMMER.get(), LibCommonTags.Items.INGOTS_IRON);
        hammerPattern(ToolsItems.GOLD_HAMMER.get(), LibCommonTags.Items.INGOTS_GOLD);
        hammerPattern(ToolsItems.STONE_HAMMER.get(), ItemTags.STONE_TOOL_MATERIALS);
        hammerPattern(ToolsItems.WOOD_HAMMER.get(), ItemTags.PLANKS);

        spearPattern(ToolsItems.WOOD_THROWING_SPEAR.get(), ItemTags.PLANKS);
        spearPattern(ToolsItems.STONE_THROWING_SPEAR.get(), ItemTags.STONE_TOOL_MATERIALS);
        spearPattern(ToolsItems.GOLD_THROWING_SPEAR.get(), LibCommonTags.Items.INGOTS_GOLD);
        spearPattern(ToolsItems.IRON_THROWING_SPEAR.get(), LibCommonTags.Items.INGOTS_IRON);
        spearPattern(ToolsItems.DIAMOND_THROWING_SPEAR.get(), LibCommonTags.Items.GEMS_DIAMOND);
        spearPattern(ToolsItems.NETHERITE_THROWING_SPEAR.get(), LibCommonTags.Items.INGOTS_NETHERITE);

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, ToolsItems.WOOD_BOOMERANG.get()).define('X', ItemTags.PLANKS).pattern("XX").pattern("X ").pattern("XX").unlockedBy("has_planks", has(ItemTags.PLANKS)).save(this.output, recipeKey(ToolsItems.WOOD_BOOMERANG.getId()));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, ToolsItems.DIAMOND_BOOMERANG.get()).define('X', LibCommonTags.Items.GEMS_DIAMOND).define('Y', ToolsItems.WOOD_BOOMERANG.get()).pattern("XX").pattern("XY").pattern("XX").unlockedBy("has_diamonds", has(LibCommonTags.Items.GEMS_DIAMOND)).save(this.output, recipeKey(ToolsItems.DIAMOND_BOOMERANG.getId()));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, ToolsItems.POKEBALL.get()).define('R', LibCommonTags.Items.DUSTS_REDSTONE).define('C', ItemTags.COALS).define('I', LibCommonTags.Items.INGOTS_IRON).define('B', Items.STONE_BUTTON).pattern("RRR").pattern("CBC").pattern("III").unlockedBy("has_iron", has(LibCommonTags.Items.INGOTS_IRON)).unlockedBy("has_redstone", has(LibCommonTags.Items.DUSTS_REDSTONE)).save(this.output, recipeKey(ToolsItems.POKEBALL.getId()));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, ToolsItems.BUILDING_WAND.get()).define('X', ItemTags.PLANKS).define('G', LibCommonTags.Items.INGOTS_GOLD).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_gold", has(LibCommonTags.Items.INGOTS_GOLD)).save(this.output, recipeKey(ToolsItems.BUILDING_WAND.getId()));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, ToolsItems.BREAKING_WAND.get()).define('X', ItemTags.PLANKS).define('G', LibCommonTags.Items.INGOTS_IRON).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_iron", has(LibCommonTags.Items.INGOTS_IRON)).save(this.output, recipeKey(ToolsItems.BREAKING_WAND.getId()));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, ToolsItems.MINING_WAND.get()).define('X', ItemTags.PLANKS).define('G', LibCommonTags.Items.GEMS_DIAMOND).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_diamond", has(LibCommonTags.Items.GEMS_DIAMOND)).save(this.output, recipeKey(ToolsItems.MINING_WAND.getId()));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, ToolsItems.REINFORCED_BUILDING_WAND.get()).define('X', LibCommonTags.Items.OBSIDIAN).define('G', LibCommonTags.Items.STORAGE_BLOCKS_GOLD).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_obsidian", has(LibCommonTags.Items.OBSIDIAN)).save(this.output, recipeKey(ToolsItems.REINFORCED_BUILDING_WAND.getId()));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, ToolsItems.REINFORCED_BREAKING_WAND.get()).define('X', LibCommonTags.Items.OBSIDIAN).define('G', LibCommonTags.Items.STORAGE_BLOCKS_IRON).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_obsidian", has(LibCommonTags.Items.OBSIDIAN)).save(this.output, recipeKey(ToolsItems.REINFORCED_BREAKING_WAND.getId()));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, ToolsItems.REINFORCED_MINING_WAND.get()).define('X', LibCommonTags.Items.OBSIDIAN).define('G', LibCommonTags.Items.STORAGE_BLOCKS_DIAMOND).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_obsidian", has(LibCommonTags.Items.OBSIDIAN)).save(this.output, recipeKey(ToolsItems.REINFORCED_MINING_WAND.getId()));

        armorSet(ToolsItems.CHICKEN_SUIT_HELMET.get(), ToolsItems.CHICKEN_SUIT_CHESTPLATE.get(), ToolsItems.CHICKEN_SUIT_LEGGINGS.get(), ToolsItems.CHICKEN_SUIT_BOOTS.get(), LibCommonTags.Items.FEATHERS, "chickensuit");

        multiTool(ToolsItems.WOODEN_MULTITOOL.get(), Items.WOODEN_PICKAXE, Items.WOODEN_SHOVEL, Items.WOODEN_AXE, Items.WOODEN_HOE, Items.WOODEN_SWORD, ItemTags.PLANKS);
        multiTool(ToolsItems.STONE_MULTITOOL.get(), Items.STONE_PICKAXE, Items.STONE_SHOVEL, Items.STONE_AXE, Items.STONE_HOE, Items.STONE_SWORD, ItemTags.STONE_TOOL_MATERIALS);
        multiTool(ToolsItems.GOLDEN_MULTITOOL.get(), Items.GOLDEN_PICKAXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_AXE, Items.GOLDEN_HOE, Items.GOLDEN_SWORD, LibCommonTags.Items.INGOTS_GOLD);
        multiTool(ToolsItems.IRON_MULTITOOL.get(), Items.IRON_PICKAXE, Items.IRON_SHOVEL, Items.IRON_AXE, Items.IRON_HOE, Items.IRON_SWORD, LibCommonTags.Items.INGOTS_IRON);
        multiTool(ToolsItems.DIAMOND_MULTITOOL.get(), Items.DIAMOND_PICKAXE, Items.DIAMOND_SHOVEL, Items.DIAMOND_AXE, Items.DIAMOND_HOE, Items.DIAMOND_SWORD, LibCommonTags.Items.GEMS_DIAMOND);
        multiTool(ToolsItems.NETHERITE_MULTITOOL.get(), Items.NETHERITE_PICKAXE, Items.NETHERITE_SHOVEL, Items.NETHERITE_AXE, Items.NETHERITE_HOE, Items.NETHERITE_SWORD, LibCommonTags.Items.INGOTS_NETHERITE);

        ToolsItems.MATERIAL_GROUPS.forEach((s, group) -> {
            toolSet(group.PICKAXE.get(), group.SHOVEL.get(), group.AXE.get(), group.HOE.get(), group.SWORD.get(), group.material, ToolsConditions.Parts.EXTRA_MATERIAL);
            hammerPattern(group.HAMMER.get(), group.material, ToolsConditions.Parts.EXTRA_MATERIAL);
            multiTool(group.MULTITOOL.get(), group.PICKAXE.get(), group.SHOVEL.get(), group.AXE.get(), group.HOE.get(), group.SWORD.get(), group.material, ToolsConditions.Parts.EXTRA_MATERIAL);
            armorSet(group.HELMET.get(), group.CHESTPLATE.get(), group.LEGGINGS.get(), group.BOOTS.get(), group.material, ToolsConditions.Parts.EXTRA_MATERIAL);
            spearPattern(group.THROWING_SPEAR.get(), group.material, ToolsConditions.Parts.EXTRA_MATERIAL);
            lungeSpearPattern(group.SPEAR.get(), group.material, ToolsConditions.Parts.EXTRA_MATERIAL);
            bucketPattern(group.BUCKET.get(), group.material, ToolsConditions.Parts.EXTRA_MATERIAL);
            shearPattern(group.SHEARS.get(), group.material, ToolsConditions.Parts.EXTRA_MATERIAL);
        });

        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(ToolsItems.DIAMOND_MULTITOOL.get()), Ingredient.of(Blocks.NETHERITE_BLOCK), RecipeCategory.TOOLS, ToolsItems.NETHERITE_MULTITOOL.get()).unlocks("has_netherite_block", has(Blocks.NETHERITE_BLOCK)).save(this.output, recipeKey(ToolsItems.NETHERITE_MULTITOOL.getId() + "_smithing"));
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(ToolsItems.DIAMOND_HAMMER.get()), this.tag(LibCommonTags.Items.INGOTS_NETHERITE), RecipeCategory.TOOLS, ToolsItems.NETHERITE_HAMMER.get()).unlocks("has_netherite_ingot", has(LibCommonTags.Items.INGOTS_NETHERITE)).save(this.output, recipeKey(ToolsItems.NETHERITE_HAMMER.getId() + "_smithing"));
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(ToolsItems.DIAMOND_THROWING_SPEAR.get()), this.tag(LibCommonTags.Items.INGOTS_NETHERITE), RecipeCategory.COMBAT, ToolsItems.NETHERITE_THROWING_SPEAR.get()).unlocks("has_netherite_ingot", has(LibCommonTags.Items.INGOTS_NETHERITE)).save(this.output, recipeKey(ToolsItems.NETHERITE_THROWING_SPEAR.getId() + "_smithing"));
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(ToolsItems.DIAMOND_BUCKET.get()), this.tag(LibCommonTags.Items.INGOTS_NETHERITE), RecipeCategory.TOOLS, ToolsItems.NETHERITE_BUCKET.get()).unlocks("has_netherite_ingot", has(LibCommonTags.Items.INGOTS_NETHERITE)).save(this.output, recipeKey(ToolsItems.NETHERITE_BUCKET.getId() + "_smithing"));
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(ToolsItems.DIAMOND_SHEARS.get()), this.tag(LibCommonTags.Items.INGOTS_NETHERITE), RecipeCategory.TOOLS, ToolsItems.NETHERITE_SHEARS.get()).unlocks("has_netherite_ingot", has(LibCommonTags.Items.INGOTS_NETHERITE)).save(this.output, recipeKey(ToolsItems.NETHERITE_SHEARS.getId() + "_smithing"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, Blocks.CAKE).define('A', LibCommonTags.Items.BUCKETS_MILK).define('B', Items.SUGAR).define('C', LibCommonTags.Items.CROPS_WHEAT).define('E', LibCommonTags.Items.EGGS).pattern("AAA").pattern("BEB").pattern("CCC").unlockedBy("has_egg", has(LibCommonTags.Items.EGGS)).save(this.output, recipeKey(Identifier.parse(key(Blocks.CAKE.asItem()) + "_alt")));
        bucketPattern(ToolsItems.WOOD_BUCKET.get(), ItemTags.PLANKS);
        bucketPattern(ToolsItems.STONE_BUCKET.get(), ItemTags.STONE_TOOL_MATERIALS);
        bucketPattern(ToolsItems.GOLD_BUCKET.get(), LibCommonTags.Items.INGOTS_GOLD);
        bucketPattern(ToolsItems.DIAMOND_BUCKET.get(), LibCommonTags.Items.GEMS_DIAMOND);
        bucketPattern(ToolsItems.NETHERITE_BUCKET.get(), LibCommonTags.Items.INGOTS_NETHERITE);

        shearPattern(ToolsItems.WOOD_SHEARS.get(), ItemTags.PLANKS);
        shearPattern(ToolsItems.STONE_SHEARS.get(), ItemTags.STONE_TOOL_MATERIALS);
        shearPattern(ToolsItems.GOLD_SHEARS.get(), LibCommonTags.Items.INGOTS_GOLD);
        shearPattern(ToolsItems.DIAMOND_SHEARS.get(), LibCommonTags.Items.GEMS_DIAMOND);
        shearPattern(ToolsItems.NETHERITE_SHEARS.get(), LibCommonTags.Items.INGOTS_NETHERITE);

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.COMBAT, ToolsItems.ULTIMATE_FIST.get()).requires(ToolsItems.U_FRAGMENT.get()).requires(ToolsItems.L_FRAGMENT.get()).requires(ToolsItems.T_FRAGMENT.get()).requires(ToolsItems.I_FRAGMENT.get()).requires(ToolsItems.M_FRAGMENT.get()).requires(ToolsItems.A_FRAGMENT.get()).requires(ToolsItems.MISSING_FRAGMENT.get()).requires(ToolsItems.E_FRAGMENT.get()).requires(LibCommonTags.Items.NETHER_STARS).unlockedBy("has_nether_star", has(LibCommonTags.Items.NETHER_STARS)).unlockedBy("has_fragment", has(ToolsTags.Items.ULTIMATE_FRAGMENTS)).save(this.output, recipeKey(ToolsItems.ULTIMATE_FIST.getId()));
    }

    private void shearPattern(ItemLike output, TagKey<Item> input) {
        this.addConditions(and(partEnabled(ToolsConditions.Parts.MORE_SHEARS), itemTagExists(input)), key(output.asItem()), Identifier.parse(key(output.asItem()) + "_alt"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, output).define('I', input).define('L', LibCommonTags.Items.LEATHER).pattern(" I").pattern("IL").unlockedBy("has_leather", has(LibCommonTags.Items.LEATHER)).unlockedBy("has_item", has(input)).save(this.output, recipeKey(key(output.asItem())));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, output).define('I', input).define('L', LibCommonTags.Items.LEATHER).pattern("LI").pattern("I ").unlockedBy("has_leather", has(LibCommonTags.Items.LEATHER)).unlockedBy("has_item", has(input)).save(this.output, recipeKey(Identifier.parse(key(output.asItem()) + "_alt")));
    }

    private void shearPattern(ItemLike output, TagKey<Item> input, String condition) {
        this.addConditions(and(partEnabled(ToolsConditions.Parts.MORE_SHEARS), itemTagExists(input), partEnabled(condition)), key(output.asItem()), Identifier.parse(key(output.asItem()) + "_alt"));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, output).define('I', input).define('L', LibCommonTags.Items.LEATHER).pattern(" I").pattern("IL").unlockedBy("has_leather", has(LibCommonTags.Items.LEATHER)).unlockedBy("has_item", has(input)).save(this.output, recipeKey(key(output.asItem())));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, output).define('I', input).define('L', LibCommonTags.Items.LEATHER).pattern("LI").pattern("I ").unlockedBy("has_leather", has(LibCommonTags.Items.LEATHER)).unlockedBy("has_item", has(input)).save(this.output, recipeKey(Identifier.parse(key(output.asItem()) + "_alt")));
    }

    private void bucketPattern(ItemLike output, TagKey<Item> input) {
        this.addConditions(and(partEnabled(ToolsConditions.Parts.BETTER_BUCKETS), itemTagExists(input)), key(output.asItem()));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, output).define('I', input).pattern("I I").pattern(" I ").unlockedBy("has_item", has(input)).save(this.output, recipeKey(key(output.asItem())));
    }

    private void bucketPattern(ItemLike output, TagKey<Item> input, String condition) {
        this.addConditions(and(partEnabled(ToolsConditions.Parts.BETTER_BUCKETS), itemTagExists(input), partEnabled(condition)), key(output.asItem()));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, output).define('I', input).pattern("I I").pattern(" I ").unlockedBy("has_item", has(input)).save(this.output, recipeKey(key(output.asItem())));
    }

    /**
     * A throwing spear: the shaft laid flat with the head at its end. That is the only pattern left
     * to it - the old diagonal was the mirror image of vanilla's spear recipe, and the head above
     * two sticks is vanilla's shovel, and shaped recipes match mirrored patterns too.
     */
    private void spearPattern(ItemLike output, TagKey<Item> input) {
        this.addConditions(and(partEnabled(ToolsConditions.Parts.THROWING_SPEARS), itemTagExists(input)), key(output.asItem()));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.COMBAT, output).define('S', LibCommonTags.Items.RODS_WOODEN).define('I', input).pattern("SSI").unlockedBy("has_item", has(input)).save(this.output, recipeKey(key(output.asItem())));
    }

    private void spearPattern(ItemLike output, TagKey<Item> input, String condition) {
        this.addConditions(and(partEnabled(ToolsConditions.Parts.THROWING_SPEARS), itemTagExists(input), partEnabled(condition)), key(output.asItem()));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.COMBAT, output).define('S', LibCommonTags.Items.RODS_WOODEN).define('I', input).pattern("SSI").unlockedBy("has_item", has(input)).save(this.output, recipeKey(key(output.asItem())));
    }

    /** Vanilla's own spear recipe, for a material vanilla has no spear of. */
    private void lungeSpearPattern(ItemLike output, TagKey<Item> input, String condition) {
        this.addConditions(and(partEnabled(ToolsConditions.Parts.SPEARS), itemTagExists(input), partEnabled(condition)), key(output.asItem()));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.COMBAT, output).define('S', LibCommonTags.Items.RODS_WOODEN).define('I', input).pattern("  I").pattern(" S ").pattern("S  ").unlockedBy("has_item", has(input)).save(this.output, recipeKey(key(output.asItem())));
    }

    private void hammerPattern(ItemLike output, TagKey<Item> input) {
        this.addConditions(and(partEnabled(ToolsConditions.Parts.HAMMERS), itemTagExists(input)), key(output.asItem()));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, output).define('S', LibCommonTags.Items.RODS_WOODEN).define('I', input).pattern("III").pattern("ISI").pattern(" S ").unlockedBy("has_item", has(input)).save(this.output, recipeKey(key(output.asItem())));
    }

    private void hammerPattern(ItemLike output, TagKey<Item> input, String condition) {
        this.addConditions(and(partEnabled(ToolsConditions.Parts.HAMMERS), itemTagExists(input), partEnabled(condition)), key(output.asItem()));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, output).define('S', LibCommonTags.Items.RODS_WOODEN).define('I', input).pattern("III").pattern("ISI").pattern(" S ").unlockedBy("has_item", has(input)).save(this.output, recipeKey(key(output.asItem())));
    }

    private void toolSet(ItemLike pickaxe, ItemLike shovel, ItemLike axe, ItemLike hoe, ItemLike sword, TagKey<Item> input, String condition) {
        this.addConditions(and(partEnabled(condition), itemTagExists(input)), key(pickaxe.asItem()), key(shovel.asItem()), key(axe.asItem()), Identifier.fromNamespaceAndPath(Constants.MOD_ID, key(axe.asItem()).getPath() + "_alt"), key(hoe.asItem()), Identifier.fromNamespaceAndPath(Constants.MOD_ID, key(hoe.asItem()).getPath() + "_alt"), key(sword.asItem()));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, pickaxe).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("XXX").pattern(" S ").pattern(" S ").unlockedBy("has_item", has(input)).save(this.output, recipeKey(key(pickaxe.asItem())));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, shovel).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("X").pattern("S").pattern("S").unlockedBy("has_item", has(input)).save(this.output, recipeKey(key(shovel.asItem())));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, axe).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("XX").pattern("XS").pattern(" S").unlockedBy("has_item", has(input)).save(this.output, recipeKey(key(axe.asItem())));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, axe).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("XX").pattern("SX").pattern("S ").unlockedBy("has_item", has(input)).save(this.output, recipeKey(Identifier.fromNamespaceAndPath(Constants.MOD_ID, key(axe.asItem()).getPath() + "_alt")));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, hoe).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("XX").pattern(" S").pattern(" S").unlockedBy("has_item", has(input)).save(this.output, recipeKey(key(hoe.asItem())));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, hoe).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("XX").pattern("S ").pattern("S ").unlockedBy("has_item", has(input)).save(this.output, recipeKey(Identifier.fromNamespaceAndPath(Constants.MOD_ID, key(hoe.asItem()).getPath() + "_alt")));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.COMBAT, sword).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("X").pattern("X").pattern("S").unlockedBy("has_item", has(input)).save(this.output, recipeKey(key(sword.asItem())));
    }

    private void armorSet(ItemLike helmet, ItemLike chestplate, ItemLike leggings, ItemLike boots, TagKey<Item> input, String condition) {
        this.addConditions(and(partEnabled(condition), itemTagExists(input)), key(helmet.asItem()), key(chestplate.asItem()), key(leggings.asItem()), key(boots.asItem()));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.COMBAT, helmet).define('X', input).pattern("XXX").pattern("X X").unlockedBy("has_item", has(input)).save(this.output, recipeKey(key(helmet.asItem())));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.COMBAT, chestplate).define('X', input).pattern("X X").pattern("XXX").pattern("XXX").unlockedBy("has_item", has(input)).save(this.output, recipeKey(key(chestplate.asItem())));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.COMBAT, leggings).define('X', input).pattern("XXX").pattern("X X").pattern("X X").unlockedBy("has_item", has(input)).save(this.output, recipeKey(key(leggings.asItem())));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.COMBAT, boots).define('X', input).pattern("X X").pattern("X X").unlockedBy("has_item", has(input)).save(this.output, recipeKey(key(boots.asItem())));
    }

    private void multiTool(ItemLike output, ItemLike pickaxe, ItemLike shovel, ItemLike axe, ItemLike hoe, ItemLike sword, TagKey<Item> input) {
        this.addConditions(and(partEnabled(ToolsConditions.Parts.MULTITOOL), itemTagExists(input)), key(output.asItem()));

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.TOOLS, output)
                .requires(pickaxe)
                .requires(shovel)
                .requires(axe)
                .requires(hoe)
                .requires(sword)
                .requires(input)
                .requires(input)
                .requires(input)
                .requires(input)
                .unlockedBy("has_item", has(input)).save(this.output, recipeKey(key(output.asItem())));
    }

    private void multiTool(ItemLike output, ItemLike pickaxe, ItemLike shovel, ItemLike axe, ItemLike hoe, ItemLike sword, TagKey<Item> input, String condition) {
        this.addConditions(and(partEnabled(ToolsConditions.Parts.MULTITOOL), itemTagExists(input), partEnabled(condition)), key(output.asItem()));

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.TOOLS, output)
                .requires(pickaxe)
                .requires(shovel)
                .requires(axe)
                .requires(hoe)
                .requires(sword)
                .requires(input)
                .requires(input)
                .requires(input)
                .requires(input)
                .unlockedBy("has_item", has(input)).save(this.output, recipeKey(key(output.asItem())));
    }

    /**
     * The item's registry id. The old body went through the platform registry service; the base
     * provider already exposes this, so it just forwards now.
     */
    private Identifier key(Item item) {
        return id(item);
    }

    /**
     * Recipes are addressed by {@code ResourceKey<Recipe<?>>} rather than a raw {@code Identifier}
     * since 1.21.2 - the recipe manager keys them, and a recipe no longer carries its own id. The
     * conditions map is still keyed by {@code Identifier}, so both forms are needed side by side.
     */
    private static ResourceKey<Recipe<?>> recipeKey(Identifier id) {
        return ResourceKey.create(Registries.RECIPE, id);
    }

    private static ResourceKey<Recipe<?>> recipeKey(String id) {
        return recipeKey(Identifier.parse(id));
    }

    /**
     * Recipe providers are not data providers any more - a {@link RecipeProvider.Runner} owns the
     * file writing and builds a fresh provider around the {@link RecipeOutput} it hands out. This is
     * what the loader datagen entry points register.
     */
    public static class Runner extends ConditionalRecipeProvider.Runner {

        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries, Constants.MOD_ID);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new ToolsRecipes(registries, output);
        }

        @Override
        public String getName() {
            return "Recipes: " + Constants.MOD_ID;
        }
    }
}
