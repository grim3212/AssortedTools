package com.grim3212.assorted.decor.data;

import com.grim3212.assorted.decor.Constants;
import com.grim3212.assorted.decor.api.ToolsTags;
import com.grim3212.assorted.decor.common.crafting.ToolsConditions;
import com.grim3212.assorted.decor.common.item.ToolsItems;
import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

public class ToolsCommonRecipeProvider extends ConditionalRecipeProvider {

    public ToolsCommonRecipeProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    public void registerConditions() {
        this.addConditions(partEnabled(ToolsConditions.Parts.BOOMERANGS), ToolsItems.WOOD_BOOMERANG.getId(), ToolsItems.DIAMOND_BOOMERANG.getId());
        this.addConditions(partEnabled(ToolsConditions.Parts.POKEBALL), ToolsItems.POKEBALL.getId());
        this.addConditions(partEnabled(ToolsConditions.Parts.WANDS), ToolsItems.BUILDING_WAND.getId(), ToolsItems.BREAKING_WAND.getId(), ToolsItems.MINING_WAND.getId(), ToolsItems.REINFORCED_BUILDING_WAND.getId(), ToolsItems.REINFORCED_BREAKING_WAND.getId(), ToolsItems.REINFORCED_MINING_WAND.getId());
        this.addConditions(partEnabled(ToolsConditions.Parts.ULTIMATE_FIST), ToolsItems.ULTIMATE_FIST.getId());

        this.addConditions(partEnabled(ToolsConditions.Parts.MULTITOOL), new ResourceLocation(ToolsItems.NETHERITE_MULTITOOL.getId() + "_smithing"));
        this.addConditions(partEnabled(ToolsConditions.Parts.HAMMERS), new ResourceLocation(ToolsItems.NETHERITE_HAMMER.getId() + "_smithing"));
        this.addConditions(partEnabled(ToolsConditions.Parts.BETTER_SPEARS), new ResourceLocation(ToolsItems.NETHERITE_SPEAR.getId() + "_smithing"));
        this.addConditions(partEnabled(ToolsConditions.Parts.BETTER_BUCKETS), new ResourceLocation(ToolsItems.NETHERITE_BUCKET.getId() + "_smithing"));
        this.addConditions(partEnabled(ToolsConditions.Parts.MORE_SHEARS), new ResourceLocation(ToolsItems.NETHERITE_SHEARS.getId() + "_smithing"));

        this.addConditions(partEnabled(ToolsConditions.Parts.BETTER_BUCKETS), new ResourceLocation(key(Blocks.CAKE.asItem()) + "_alt"));
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        super.buildRecipes(consumer);

        hammerPattern(ToolsItems.NETHERITE_HAMMER.get(), LibCommonTags.Items.INGOTS_NETHERITE, consumer);
        hammerPattern(ToolsItems.DIAMOND_HAMMER.get(), LibCommonTags.Items.GEMS_DIAMOND, consumer);
        hammerPattern(ToolsItems.IRON_HAMMER.get(), LibCommonTags.Items.INGOTS_IRON, consumer);
        hammerPattern(ToolsItems.GOLD_HAMMER.get(), LibCommonTags.Items.INGOTS_GOLD, consumer);
        hammerPattern(ToolsItems.STONE_HAMMER.get(), ItemTags.STONE_TOOL_MATERIALS, consumer);
        hammerPattern(ToolsItems.WOOD_HAMMER.get(), ItemTags.PLANKS, consumer);

        spearPattern(ToolsItems.WOOD_SPEAR.get(), ItemTags.PLANKS, consumer);
        spearPattern(ToolsItems.STONE_SPEAR.get(), ItemTags.STONE_TOOL_MATERIALS, consumer);
        spearPattern(ToolsItems.GOLD_SPEAR.get(), LibCommonTags.Items.INGOTS_GOLD, consumer);
        spearPattern(ToolsItems.IRON_SPEAR.get(), LibCommonTags.Items.INGOTS_IRON, consumer);
        spearPattern(ToolsItems.DIAMOND_SPEAR.get(), LibCommonTags.Items.GEMS_DIAMOND, consumer);
        spearPattern(ToolsItems.NETHERITE_SPEAR.get(), LibCommonTags.Items.INGOTS_NETHERITE, consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ToolsItems.WOOD_BOOMERANG.get()).define('X', ItemTags.PLANKS).pattern("XX").pattern("X ").pattern("XX").unlockedBy("has_planks", has(ItemTags.PLANKS)).save(consumer, ToolsItems.WOOD_BOOMERANG.getId());
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ToolsItems.DIAMOND_BOOMERANG.get()).define('X', LibCommonTags.Items.GEMS_DIAMOND).define('Y', ToolsItems.WOOD_BOOMERANG.get()).pattern("XX").pattern("XY").pattern("XX").unlockedBy("has_diamonds", has(LibCommonTags.Items.GEMS_DIAMOND)).save(consumer, ToolsItems.DIAMOND_BOOMERANG.getId());
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ToolsItems.POKEBALL.get()).define('R', LibCommonTags.Items.DUSTS_REDSTONE).define('C', ItemTags.COALS).define('I', LibCommonTags.Items.INGOTS_IRON).define('B', Items.STONE_BUTTON).pattern("RRR").pattern("CBC").pattern("III").unlockedBy("has_iron", has(LibCommonTags.Items.INGOTS_IRON)).unlockedBy("has_redstone", has(LibCommonTags.Items.DUSTS_REDSTONE)).save(consumer, ToolsItems.POKEBALL.getId());

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ToolsItems.BUILDING_WAND.get()).define('X', ItemTags.PLANKS).define('G', LibCommonTags.Items.INGOTS_GOLD).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_gold", has(LibCommonTags.Items.INGOTS_GOLD)).save(consumer, ToolsItems.BUILDING_WAND.getId());
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ToolsItems.BREAKING_WAND.get()).define('X', ItemTags.PLANKS).define('G', LibCommonTags.Items.INGOTS_IRON).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_iron", has(LibCommonTags.Items.INGOTS_IRON)).save(consumer, ToolsItems.BREAKING_WAND.getId());
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ToolsItems.MINING_WAND.get()).define('X', ItemTags.PLANKS).define('G', LibCommonTags.Items.GEMS_DIAMOND).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_diamond", has(LibCommonTags.Items.GEMS_DIAMOND)).save(consumer, ToolsItems.MINING_WAND.getId());
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ToolsItems.REINFORCED_BUILDING_WAND.get()).define('X', LibCommonTags.Items.OBSIDIAN).define('G', LibCommonTags.Items.STORAGE_BLOCKS_GOLD).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_obsidian", has(LibCommonTags.Items.OBSIDIAN)).save(consumer, ToolsItems.REINFORCED_BUILDING_WAND.getId());
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ToolsItems.REINFORCED_BREAKING_WAND.get()).define('X', LibCommonTags.Items.OBSIDIAN).define('G', LibCommonTags.Items.STORAGE_BLOCKS_IRON).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_obsidian", has(LibCommonTags.Items.OBSIDIAN)).save(consumer, ToolsItems.REINFORCED_BREAKING_WAND.getId());
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ToolsItems.REINFORCED_MINING_WAND.get()).define('X', LibCommonTags.Items.OBSIDIAN).define('G', LibCommonTags.Items.STORAGE_BLOCKS_DIAMOND).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_obsidian", has(LibCommonTags.Items.OBSIDIAN)).save(consumer, ToolsItems.REINFORCED_MINING_WAND.getId());

        armorSet(ToolsItems.CHICKEN_SUIT_HELMET.get(), ToolsItems.CHICKEN_SUIT_CHESTPLATE.get(), ToolsItems.CHICKEN_SUIT_LEGGINGS.get(), ToolsItems.CHICKEN_SUIT_BOOTS.get(), LibCommonTags.Items.FEATHERS, consumer, "chickensuit");

        multiTool(ToolsItems.WOODEN_MULTITOOL.get(), Items.WOODEN_PICKAXE, Items.WOODEN_SHOVEL, Items.WOODEN_AXE, Items.WOODEN_HOE, Items.WOODEN_SWORD, ItemTags.PLANKS, consumer);
        multiTool(ToolsItems.STONE_MULTITOOL.get(), Items.STONE_PICKAXE, Items.STONE_SHOVEL, Items.STONE_AXE, Items.STONE_HOE, Items.STONE_SWORD, ItemTags.STONE_TOOL_MATERIALS, consumer);
        multiTool(ToolsItems.GOLDEN_MULTITOOL.get(), Items.GOLDEN_PICKAXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_AXE, Items.GOLDEN_HOE, Items.GOLDEN_SWORD, LibCommonTags.Items.INGOTS_GOLD, consumer);
        multiTool(ToolsItems.IRON_MULTITOOL.get(), Items.IRON_PICKAXE, Items.IRON_SHOVEL, Items.IRON_AXE, Items.IRON_HOE, Items.IRON_SWORD, LibCommonTags.Items.INGOTS_IRON, consumer);
        multiTool(ToolsItems.DIAMOND_MULTITOOL.get(), Items.DIAMOND_PICKAXE, Items.DIAMOND_SHOVEL, Items.DIAMOND_AXE, Items.DIAMOND_HOE, Items.DIAMOND_SWORD, LibCommonTags.Items.GEMS_DIAMOND, consumer);
        multiTool(ToolsItems.NETHERITE_MULTITOOL.get(), Items.NETHERITE_PICKAXE, Items.NETHERITE_SHOVEL, Items.NETHERITE_AXE, Items.NETHERITE_HOE, Items.NETHERITE_SWORD, LibCommonTags.Items.INGOTS_NETHERITE, consumer);

        ToolsItems.MATERIAL_GROUPS.forEach((s, group) -> {
            toolSet(group.PICKAXE.get(), group.SHOVEL.get(), group.AXE.get(), group.HOE.get(), group.SWORD.get(), group.material, consumer, ToolsConditions.Parts.EXTRA_MATERIAL);
            hammerPattern(group.HAMMER.get(), group.material, consumer, ToolsConditions.Parts.EXTRA_MATERIAL);
            multiTool(group.MULTITOOL.get(), group.PICKAXE.get(), group.SHOVEL.get(), group.AXE.get(), group.HOE.get(), group.SWORD.get(), group.material, consumer, ToolsConditions.Parts.EXTRA_MATERIAL);
            armorSet(group.HELMET.get(), group.CHESTPLATE.get(), group.LEGGINGS.get(), group.BOOTS.get(), group.material, consumer, ToolsConditions.Parts.EXTRA_MATERIAL);
            spearPattern(group.SPEAR.get(), group.material, consumer, ToolsConditions.Parts.EXTRA_MATERIAL);
            bucketPattern(group.BUCKET.get(), group.material, consumer, ToolsConditions.Parts.EXTRA_MATERIAL);
            shearPattern(group.SHEARS.get(), group.material, consumer, ToolsConditions.Parts.EXTRA_MATERIAL);
        });

        UpgradeRecipeBuilder.smithing(Ingredient.of(ToolsItems.DIAMOND_MULTITOOL.get()), Ingredient.of(Blocks.NETHERITE_BLOCK), RecipeCategory.TOOLS, ToolsItems.NETHERITE_MULTITOOL.get()).unlocks("has_netherite_block", has(Blocks.NETHERITE_BLOCK)).save(consumer, ToolsItems.NETHERITE_MULTITOOL.getId() + "_smithing");
        UpgradeRecipeBuilder.smithing(Ingredient.of(ToolsItems.DIAMOND_HAMMER.get()), Ingredient.of(LibCommonTags.Items.INGOTS_NETHERITE), RecipeCategory.TOOLS, ToolsItems.NETHERITE_HAMMER.get()).unlocks("has_netherite_ingot", has(LibCommonTags.Items.INGOTS_NETHERITE)).save(consumer, ToolsItems.NETHERITE_HAMMER.getId() + "_smithing");
        UpgradeRecipeBuilder.smithing(Ingredient.of(ToolsItems.DIAMOND_SPEAR.get()), Ingredient.of(LibCommonTags.Items.INGOTS_NETHERITE), RecipeCategory.COMBAT, ToolsItems.NETHERITE_SPEAR.get()).unlocks("has_netherite_ingot", has(LibCommonTags.Items.INGOTS_NETHERITE)).save(consumer, ToolsItems.NETHERITE_SPEAR.getId() + "_smithing");
        UpgradeRecipeBuilder.smithing(Ingredient.of(ToolsItems.DIAMOND_BUCKET.get()), Ingredient.of(LibCommonTags.Items.INGOTS_NETHERITE), RecipeCategory.TOOLS, ToolsItems.NETHERITE_BUCKET.get()).unlocks("has_netherite_ingot", has(LibCommonTags.Items.INGOTS_NETHERITE)).save(consumer, ToolsItems.NETHERITE_BUCKET.getId() + "_smithing");
        UpgradeRecipeBuilder.smithing(Ingredient.of(ToolsItems.DIAMOND_SHEARS.get()), Ingredient.of(LibCommonTags.Items.INGOTS_NETHERITE), RecipeCategory.TOOLS, ToolsItems.NETHERITE_SHEARS.get()).unlocks("has_netherite_ingot", has(LibCommonTags.Items.INGOTS_NETHERITE)).save(consumer, ToolsItems.NETHERITE_SHEARS.getId() + "_smithing");

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, Blocks.CAKE).define('A', LibCommonTags.Items.BUCKETS_MILK).define('B', Items.SUGAR).define('C', LibCommonTags.Items.CROPS_WHEAT).define('E', LibCommonTags.Items.EGGS).pattern("AAA").pattern("BEB").pattern("CCC").unlockedBy("has_egg", has(LibCommonTags.Items.EGGS)).save(consumer, new ResourceLocation(key(Blocks.CAKE.asItem()) + "_alt"));

        bucketPattern(ToolsItems.WOOD_BUCKET.get(), ItemTags.PLANKS, consumer);
        bucketPattern(ToolsItems.STONE_BUCKET.get(), ItemTags.STONE_TOOL_MATERIALS, consumer);
        bucketPattern(ToolsItems.GOLD_BUCKET.get(), LibCommonTags.Items.INGOTS_GOLD, consumer);
        bucketPattern(ToolsItems.DIAMOND_BUCKET.get(), LibCommonTags.Items.GEMS_DIAMOND, consumer);
        bucketPattern(ToolsItems.NETHERITE_BUCKET.get(), LibCommonTags.Items.INGOTS_NETHERITE, consumer);

        shearPattern(ToolsItems.WOOD_SHEARS.get(), ItemTags.PLANKS, consumer);
        shearPattern(ToolsItems.STONE_SHEARS.get(), ItemTags.STONE_TOOL_MATERIALS, consumer);
        shearPattern(ToolsItems.GOLD_SHEARS.get(), LibCommonTags.Items.INGOTS_GOLD, consumer);
        shearPattern(ToolsItems.DIAMOND_SHEARS.get(), LibCommonTags.Items.GEMS_DIAMOND, consumer);
        shearPattern(ToolsItems.NETHERITE_SHEARS.get(), LibCommonTags.Items.INGOTS_NETHERITE, consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ToolsItems.ULTIMATE_FIST.get()).requires(ToolsItems.U_FRAGMENT.get()).requires(ToolsItems.L_FRAGMENT.get()).requires(ToolsItems.T_FRAGMENT.get()).requires(ToolsItems.I_FRAGMENT.get()).requires(ToolsItems.M_FRAGMENT.get()).requires(ToolsItems.A_FRAGMENT.get()).requires(ToolsItems.MISSING_FRAGMENT.get()).requires(ToolsItems.E_FRAGMENT.get()).requires(LibCommonTags.Items.NETHER_STARS).unlockedBy("has_nether_star", has(LibCommonTags.Items.NETHER_STARS)).unlockedBy("has_fragment", has(ToolsTags.Items.ULTIMATE_FRAGMENTS)).save(consumer, ToolsItems.ULTIMATE_FIST.getId());
    }

    private void shearPattern(ItemLike output, TagKey<Item> input, Consumer<FinishedRecipe> consumer) {
        this.addConditions(and(partEnabled(ToolsConditions.Parts.MORE_SHEARS), itemTagExists(input)), key(output.asItem()), new ResourceLocation(key(output.asItem()) + "_alt"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, output).define('I', input).define('L', LibCommonTags.Items.LEATHER).pattern(" I").pattern("IL").unlockedBy("has_leather", has(LibCommonTags.Items.LEATHER)).unlockedBy("has_item", has(input)).save(consumer, key(output.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, output).define('I', input).define('L', LibCommonTags.Items.LEATHER).pattern("LI").pattern("I ").unlockedBy("has_leather", has(LibCommonTags.Items.LEATHER)).unlockedBy("has_item", has(input)).save(consumer, new ResourceLocation(key(output.asItem()) + "_alt"));
    }

    private void shearPattern(ItemLike output, TagKey<Item> input, Consumer<FinishedRecipe> consumer, String condition) {
        this.addConditions(and(partEnabled(ToolsConditions.Parts.MORE_SHEARS), itemTagExists(input), partEnabled(condition)), key(output.asItem()), new ResourceLocation(key(output.asItem()) + "_alt"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, output).define('I', input).define('L', LibCommonTags.Items.LEATHER).pattern(" I").pattern("IL").unlockedBy("has_leather", has(LibCommonTags.Items.LEATHER)).unlockedBy("has_item", has(input)).save(consumer, key(output.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, output).define('I', input).define('L', LibCommonTags.Items.LEATHER).pattern("LI").pattern("I ").unlockedBy("has_leather", has(LibCommonTags.Items.LEATHER)).unlockedBy("has_item", has(input)).save(consumer, new ResourceLocation(key(output.asItem()) + "_alt"));
    }

    private void bucketPattern(ItemLike output, TagKey<Item> input, Consumer<FinishedRecipe> consumer) {
        this.addConditions(and(partEnabled(ToolsConditions.Parts.BETTER_BUCKETS), itemTagExists(input)), key(output.asItem()));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, output).define('I', input).pattern("I I").pattern(" I ").unlockedBy("has_item", has(input)).save(consumer, key(output.asItem()));
    }

    private void bucketPattern(ItemLike output, TagKey<Item> input, Consumer<FinishedRecipe> consumer, String condition) {
        this.addConditions(and(partEnabled(ToolsConditions.Parts.BETTER_BUCKETS), itemTagExists(input), partEnabled(condition)), key(output.asItem()));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, output).define('I', input).pattern("I I").pattern(" I ").unlockedBy("has_item", has(input)).save(consumer, key(output.asItem()));
    }

    private void spearPattern(ItemLike output, TagKey<Item> input, Consumer<FinishedRecipe> consumer) {
        this.addConditions(and(partEnabled(ToolsConditions.Parts.BETTER_SPEARS), itemTagExists(input)), key(output.asItem()), new ResourceLocation(key(output.asItem()) + "_alt"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output).define('S', LibCommonTags.Items.RODS_WOODEN).define('I', input).pattern("I  ").pattern(" S ").pattern("  S").unlockedBy("has_item", has(input)).save(consumer, key(output.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output).define('S', LibCommonTags.Items.RODS_WOODEN).define('I', input).pattern("SSI").unlockedBy("has_item", has(input)).save(consumer, new ResourceLocation(key(output.asItem()) + "_alt"));
    }

    private void spearPattern(ItemLike output, TagKey<Item> input, Consumer<FinishedRecipe> consumer, String condition) {
        this.addConditions(and(partEnabled(ToolsConditions.Parts.BETTER_SPEARS), itemTagExists(input), partEnabled(condition)), key(output.asItem()), new ResourceLocation(key(output.asItem()) + "_alt"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output).define('S', LibCommonTags.Items.RODS_WOODEN).define('I', input).pattern("I  ").pattern(" S ").pattern("  S").unlockedBy("has_item", has(input)).save(consumer, key(output.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output).define('S', LibCommonTags.Items.RODS_WOODEN).define('I', input).pattern("SSI").unlockedBy("has_item", has(input)).save(consumer, new ResourceLocation(key(output.asItem()) + "_alt"));
    }

    private void hammerPattern(ItemLike output, TagKey<Item> input, Consumer<FinishedRecipe> consumer) {
        this.addConditions(and(partEnabled(ToolsConditions.Parts.HAMMERS), itemTagExists(input)), key(output.asItem()));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, output).define('S', LibCommonTags.Items.RODS_WOODEN).define('I', input).pattern("III").pattern("ISI").pattern(" S ").unlockedBy("has_item", has(input)).save(consumer, key(output.asItem()));
    }

    private void hammerPattern(ItemLike output, TagKey<Item> input, Consumer<FinishedRecipe> consumer, String condition) {
        this.addConditions(and(partEnabled(ToolsConditions.Parts.HAMMERS), itemTagExists(input), partEnabled(condition)), key(output.asItem()));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, output).define('S', LibCommonTags.Items.RODS_WOODEN).define('I', input).pattern("III").pattern("ISI").pattern(" S ").unlockedBy("has_item", has(input)).save(consumer, key(output.asItem()));
    }

    private void toolSet(ItemLike pickaxe, ItemLike shovel, ItemLike axe, ItemLike hoe, ItemLike sword, TagKey<Item> input, Consumer<FinishedRecipe> consumer, String condition) {
        this.addConditions(and(partEnabled(condition), itemTagExists(input)), key(pickaxe.asItem()), key(shovel.asItem()), key(axe.asItem()), new ResourceLocation(Constants.MOD_ID, key(axe.asItem()).getPath() + "_alt"), key(hoe.asItem()), new ResourceLocation(Constants.MOD_ID, key(hoe.asItem()).getPath() + "_alt"), key(sword.asItem()));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, pickaxe).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("XXX").pattern(" S ").pattern(" S ").unlockedBy("has_item", has(input)).save(consumer, key(pickaxe.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, shovel).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("X").pattern("S").pattern("S").unlockedBy("has_item", has(input)).save(consumer, key(shovel.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, axe).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("XX").pattern("XS").pattern(" S").unlockedBy("has_item", has(input)).save(consumer, key(axe.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, axe).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("XX").pattern("SX").pattern("S ").unlockedBy("has_item", has(input)).save(consumer, new ResourceLocation(Constants.MOD_ID, key(axe.asItem()).getPath() + "_alt"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, hoe).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("XX").pattern(" S").pattern(" S").unlockedBy("has_item", has(input)).save(consumer, key(hoe.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, hoe).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("XX").pattern("S ").pattern("S ").unlockedBy("has_item", has(input)).save(consumer, new ResourceLocation(Constants.MOD_ID, key(hoe.asItem()).getPath() + "_alt"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, sword).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("X").pattern("X").pattern("S").unlockedBy("has_item", has(input)).save(consumer, key(sword.asItem()));
    }

    private void armorSet(ItemLike helmet, ItemLike chestplate, ItemLike leggings, ItemLike boots, TagKey<Item> input, Consumer<FinishedRecipe> consumer, String condition) {
        this.addConditions(and(partEnabled(condition), itemTagExists(input)), key(helmet.asItem()), key(chestplate.asItem()), key(leggings.asItem()), key(boots.asItem()));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, helmet).define('X', input).pattern("XXX").pattern("X X").unlockedBy("has_item", has(input)).save(consumer, key(helmet.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, chestplate).define('X', input).pattern("X X").pattern("XXX").pattern("XXX").unlockedBy("has_item", has(input)).save(consumer, key(chestplate.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, leggings).define('X', input).pattern("XXX").pattern("X X").pattern("X X").unlockedBy("has_item", has(input)).save(consumer, key(leggings.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, boots).define('X', input).pattern("X X").pattern("X X").unlockedBy("has_item", has(input)).save(consumer, key(boots.asItem()));
    }

    private void multiTool(ItemLike output, ItemLike pickaxe, ItemLike shovel, ItemLike axe, ItemLike hoe, ItemLike sword, TagKey<Item> input, Consumer<FinishedRecipe> consumer) {
        this.addConditions(and(partEnabled(ToolsConditions.Parts.MULTITOOL), itemTagExists(input)), key(output.asItem()));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, output)
                .requires(pickaxe)
                .requires(shovel)
                .requires(axe)
                .requires(hoe)
                .requires(sword)
                .requires(input)
                .requires(input)
                .requires(input)
                .requires(input)
                .unlockedBy("has_item", has(input)).save(consumer, key(output.asItem()));
    }

    private void multiTool(ItemLike output, ItemLike pickaxe, ItemLike shovel, ItemLike axe, ItemLike hoe, ItemLike sword, TagKey<Item> input, Consumer<FinishedRecipe> consumer, String condition) {
        this.addConditions(and(partEnabled(ToolsConditions.Parts.MULTITOOL), itemTagExists(input), partEnabled(condition)), key(output.asItem()));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, output)
                .requires(pickaxe)
                .requires(shovel)
                .requires(axe)
                .requires(hoe)
                .requires(sword)
                .requires(input)
                .requires(input)
                .requires(input)
                .requires(input)
                .unlockedBy("has_item", has(input)).save(consumer, key(output.asItem()));
    }

    private ResourceLocation key(Item item) {
        return Services.PLATFORM.getRegistry(Registries.ITEM).getRegistryName(item);
    }
}
