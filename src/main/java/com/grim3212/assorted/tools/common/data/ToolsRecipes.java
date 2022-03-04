package com.grim3212.assorted.tools.common.data;

import java.util.function.Consumer;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.handler.EnabledCondition;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.grim3212.assorted.tools.common.util.ToolsTags;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.UpgradeRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;

public class ToolsRecipes extends RecipeProvider {

	public ToolsRecipes(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		hammerPattern(ToolsItems.NETHERITE_HAMMER.get(), Tags.Items.INGOTS_NETHERITE, consumer);
		hammerPattern(ToolsItems.DIAMOND_HAMMER.get(), Tags.Items.GEMS_DIAMOND, consumer);
		hammerPattern(ToolsItems.IRON_HAMMER.get(), Tags.Items.INGOTS_IRON, consumer);
		hammerPattern(ToolsItems.GOLD_HAMMER.get(), Tags.Items.INGOTS_GOLD, consumer);
		hammerPattern(ToolsItems.STONE_HAMMER.get(), ItemTags.STONE_TOOL_MATERIALS, consumer);
		hammerPattern(ToolsItems.WOOD_HAMMER.get(), ItemTags.PLANKS, consumer);

		spearPattern(ToolsItems.WOOD_SPEAR.get(), ItemTags.PLANKS, consumer);
		spearPattern(ToolsItems.STONE_SPEAR.get(),  ItemTags.STONE_TOOL_MATERIALS, consumer);
		spearPattern(ToolsItems.GOLD_SPEAR.get(), Tags.Items.INGOTS_GOLD, consumer);
		spearPattern(ToolsItems.IRON_SPEAR.get(), Tags.Items.INGOTS_IRON, consumer);
		spearPattern(ToolsItems.DIAMOND_SPEAR.get(), Tags.Items.GEMS_DIAMOND, consumer);
		spearPattern(ToolsItems.NETHERITE_SPEAR.get(), Tags.Items.INGOTS_NETHERITE, consumer);

		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.BOOMERANGS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.WOOD_BOOMERANG.get()).define('X', ItemTags.PLANKS).pattern("XX").pattern("X ").pattern("XX").unlockedBy("has_planks", has(ItemTags.PLANKS))::save).generateAdvancement().build(consumer, ToolsItems.WOOD_BOOMERANG.get().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.BOOMERANGS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.DIAMOND_BOOMERANG.get()).define('X', Tags.Items.GEMS_DIAMOND).define('Y', ToolsItems.WOOD_BOOMERANG.get()).pattern("XX").pattern("XY").pattern("XX").unlockedBy("has_diamonds", has(Tags.Items.GEMS_DIAMOND))::save).generateAdvancement().build(consumer, ToolsItems.DIAMOND_BOOMERANG.get().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.POKEBALL_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.POKEBALL.get()).define('R', Tags.Items.DUSTS_REDSTONE).define('C', ItemTags.COALS).define('I', Tags.Items.INGOTS_IRON).define('B', Items.STONE_BUTTON).pattern("RRR").pattern("CBC").pattern("III").unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON)).unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))::save).generateAdvancement()
				.build(consumer, ToolsItems.POKEBALL.get().getRegistryName());

		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.WANDS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.BUILDING_WAND.get()).define('X', ItemTags.PLANKS).define('G', Tags.Items.INGOTS_GOLD).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD))::save).generateAdvancement().build(consumer, ToolsItems.BUILDING_WAND.get().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.WANDS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.BREAKING_WAND.get()).define('X', ItemTags.PLANKS).define('G', Tags.Items.INGOTS_IRON).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))::save).generateAdvancement().build(consumer, ToolsItems.BREAKING_WAND.get().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.WANDS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.MINING_WAND.get()).define('X', ItemTags.PLANKS).define('G', Tags.Items.GEMS_DIAMOND).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND))::save).generateAdvancement().build(consumer, ToolsItems.MINING_WAND.get().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.WANDS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.REINFORCED_BUILDING_WAND.get()).define('X', Tags.Items.OBSIDIAN).define('G', Tags.Items.STORAGE_BLOCKS_GOLD).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_obsidian", has(Tags.Items.OBSIDIAN))::save).generateAdvancement().build(consumer, ToolsItems.REINFORCED_BUILDING_WAND.get().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.WANDS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.REINFORCED_BREAKING_WAND.get()).define('X', Tags.Items.OBSIDIAN).define('G', Tags.Items.STORAGE_BLOCKS_IRON).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_obsidian", has(Tags.Items.OBSIDIAN))::save).generateAdvancement().build(consumer, ToolsItems.REINFORCED_BREAKING_WAND.get().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.WANDS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.REINFORCED_MINING_WAND.get()).define('X', Tags.Items.OBSIDIAN).define('G', Tags.Items.STORAGE_BLOCKS_DIAMOND).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_obsidian", has(Tags.Items.OBSIDIAN))::save).generateAdvancement().build(consumer, ToolsItems.REINFORCED_MINING_WAND.get().getRegistryName());

		armorSet(ToolsItems.CHICKEN_SUIT_HELMET.get(), ToolsItems.CHICKEN_SUIT_CHESTPLATE.get(), ToolsItems.CHICKEN_SUIT_LEGGINGS.get(), ToolsItems.CHICKEN_SUIT_BOOTS.get(), Tags.Items.FEATHERS, consumer, "chickensuit");

		multiTool(ToolsItems.WOODEN_MULTITOOL.get(), "wooden", ItemTags.PLANKS, consumer);
		multiTool(ToolsItems.STONE_MULTITOOL.get(), "stone",  ItemTags.STONE_TOOL_MATERIALS, consumer);
		multiTool(ToolsItems.GOLDEN_MULTITOOL.get(), "golden", Tags.Items.INGOTS_GOLD, consumer);
		multiTool(ToolsItems.IRON_MULTITOOL.get(), "iron", Tags.Items.INGOTS_IRON, consumer);
		multiTool(ToolsItems.DIAMOND_MULTITOOL.get(), "diamond", Tags.Items.GEMS_DIAMOND, consumer);
		multiTool(ToolsItems.NETHERITE_MULTITOOL.get(), "netherite", Tags.Items.INGOTS_NETHERITE, consumer);

		ToolsItems.MATERIAL_GROUPS.forEach((s, group) -> {
			toolSet(group.PICKAXE.get(), group.SHOVEL.get(), group.AXE.get(), group.HOE.get(), group.SWORD.get(), group.material, consumer, EnabledCondition.EXTRA_MATERIAL_CONDITION);
			hammerPattern(group.HAMMER.get(), group.material, consumer, EnabledCondition.EXTRA_MATERIAL_CONDITION);
			multiTool(group.MULTITOOL.get(), s, group.material, consumer, EnabledCondition.EXTRA_MATERIAL_CONDITION);
			armorSet(group.HELMET.get(), group.CHESTPLATE.get(), group.LEGGINGS.get(), group.BOOTS.get(), group.material, consumer, EnabledCondition.EXTRA_MATERIAL_CONDITION);
			spearPattern(group.SPEAR.get(), group.material, consumer, EnabledCondition.EXTRA_MATERIAL_CONDITION);
		});

		UpgradeRecipeBuilder.smithing(Ingredient.of(ToolsItems.DIAMOND_MULTITOOL.get()), Ingredient.of(Blocks.NETHERITE_BLOCK), ToolsItems.NETHERITE_MULTITOOL.get()).unlocks("has_netherite_block", has(Blocks.NETHERITE_BLOCK)).save(consumer, ToolsItems.NETHERITE_MULTITOOL.get().getRegistryName() + "_smithing");
		UpgradeRecipeBuilder.smithing(Ingredient.of(ToolsItems.DIAMOND_HAMMER.get()), Ingredient.of(Tags.Items.INGOTS_NETHERITE), ToolsItems.NETHERITE_HAMMER.get()).unlocks("has_netherite_ingot", has(Tags.Items.INGOTS_NETHERITE)).save(consumer, ToolsItems.NETHERITE_HAMMER.get().getRegistryName() + "_smithing");
		UpgradeRecipeBuilder.smithing(Ingredient.of(ToolsItems.DIAMOND_SPEAR.get()), Ingredient.of(Tags.Items.INGOTS_NETHERITE), ToolsItems.NETHERITE_SPEAR.get()).unlocks("has_netherite_ingot", has(Tags.Items.INGOTS_NETHERITE)).save(consumer, ToolsItems.NETHERITE_SPEAR.get().getRegistryName() + "_smithing");
	
		
		
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.BETTER_BUCKETS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(Blocks.CAKE).define('A', ToolsTags.Items.BUCKETS_MILK).define('B', Items.SUGAR).define('C', Tags.Items.CROPS_WHEAT).define('E', Tags.Items.EGGS).pattern("AAA").pattern("BEB").pattern("CCC").unlockedBy("has_egg", has(Tags.Items.EGGS))::save).generateAdvancement().build(consumer, new ResourceLocation(Blocks.CAKE.getRegistryName() + "_alt"));
		bucketPattern(ToolsItems.WOOD_BUCKET.get(), ItemTags.PLANKS, consumer);
		bucketPattern(ToolsItems.STONE_BUCKET.get(), ItemTags.STONE_TOOL_MATERIALS, consumer);
		bucketPattern(ToolsItems.GOLD_BUCKET.get(), Tags.Items.INGOTS_GOLD, consumer);
		bucketPattern(ToolsItems.COPPER_BUCKET.get(), Tags.Items.INGOTS_COPPER, consumer);
		bucketPattern(ToolsItems.DIAMOND_BUCKET.get(), Tags.Items.GEMS_DIAMOND, consumer);
		bucketPattern(ToolsItems.NETHERITE_BUCKET.get(), Tags.Items.INGOTS_NETHERITE, consumer);
		bucketPattern(ToolsItems.OBSIDIAN_BUCKET.get(), Tags.Items.OBSIDIAN, consumer);
	}

	@Override
	public String getName() {
		return "Assorted Tools recipes";
	}
	
	private void bucketPattern(ItemLike output, TagKey<Item> input, Consumer<FinishedRecipe> consumer) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.BETTER_BUCKETS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(output).define('I', input).pattern("I I").pattern(" I ").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, output.asItem().getRegistryName());
	}

	private void spearPattern(ItemLike output, TagKey<Item> input, Consumer<FinishedRecipe> consumer) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.BETTER_SPEARS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(output).define('S', Tags.Items.RODS_WOODEN).define('I', input).pattern("I  ").pattern(" S ").pattern("  S").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, output.asItem().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.BETTER_SPEARS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(output).define('S', Tags.Items.RODS_WOODEN).define('I', input).pattern("SSI").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, new ResourceLocation(output.asItem().getRegistryName() + "_alt"));
	}

	private void spearPattern(ItemLike output, TagKey<Item> input, Consumer<FinishedRecipe> consumer, String condition) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.BETTER_SPEARS_CONDITION)).addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(output).define('S', Tags.Items.RODS_WOODEN).define('I', input).pattern("I  ").pattern(" S ").pattern("  S").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, output.asItem().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.BETTER_SPEARS_CONDITION)).addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(output).define('S', Tags.Items.RODS_WOODEN).define('I', input).pattern("SSI").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, new ResourceLocation(output.asItem().getRegistryName() + "_alt"));
	}

	private void hammerPattern(ItemLike output, TagKey<Item> input, Consumer<FinishedRecipe> consumer) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.HAMMERS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(output).define('S', Tags.Items.RODS_WOODEN).define('I', input).pattern("III").pattern("ISI").pattern(" S ").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, output.asItem().getRegistryName());
	}

	private void hammerPattern(ItemLike output, TagKey<Item> input, Consumer<FinishedRecipe> consumer, String condition) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.HAMMERS_CONDITION)).addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(output).define('S', Tags.Items.RODS_WOODEN).define('I', input).pattern("III").pattern("ISI").pattern(" S ").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, output.asItem().getRegistryName());
	}

	private void toolSet(ItemLike pickaxe, ItemLike shovel, ItemLike axe, ItemLike hoe, ItemLike sword, TagKey<Item> input, Consumer<FinishedRecipe> consumer, String condition) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(pickaxe).define('X', input).define('S', Tags.Items.RODS_WOODEN).pattern("XXX").pattern(" S ").pattern(" S ").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, pickaxe.asItem().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(shovel).define('X', input).define('S', Tags.Items.RODS_WOODEN).pattern("X").pattern("S").pattern("S").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, shovel.asItem().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(axe).define('X', input).define('S', Tags.Items.RODS_WOODEN).pattern("XX").pattern("XS").pattern(" S").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, axe.asItem().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(axe).define('X', input).define('S', Tags.Items.RODS_WOODEN).pattern("XX").pattern("SX").pattern("S ").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, new ResourceLocation(AssortedTools.MODID, axe.asItem().getRegistryName().getPath() + "_alt"));
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(hoe).define('X', input).define('S', Tags.Items.RODS_WOODEN).pattern("XX").pattern(" S").pattern(" S").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, hoe.asItem().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(hoe).define('X', input).define('S', Tags.Items.RODS_WOODEN).pattern("XX").pattern("S ").pattern("S ").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, new ResourceLocation(AssortedTools.MODID, hoe.asItem().getRegistryName().getPath() + "_alt"));
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(sword).define('X', input).define('S', Tags.Items.RODS_WOODEN).pattern("X").pattern("X").pattern("S").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, sword.asItem().getRegistryName());
	}

	private void armorSet(ItemLike helmet, ItemLike chestplate, ItemLike leggings, ItemLike boots, TagKey<Item> input, Consumer<FinishedRecipe> consumer, String condition) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(helmet).define('X', input).pattern("XXX").pattern("X X").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, helmet.asItem().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(chestplate).define('X', input).pattern("X X").pattern("XXX").pattern("XXX").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, chestplate.asItem().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(leggings).define('X', input).pattern("XXX").pattern("X X").pattern("X X").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, leggings.asItem().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(boots).define('X', input).pattern("X X").pattern("X X").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, boots.asItem().getRegistryName());
	}

	private void multiTool(ItemLike output, String materialName, TagKey<Item> input, Consumer<FinishedRecipe> consumer) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.MULTITOOL_CONDITION))
				.addRecipe(ShapelessRecipeBuilder.shapeless(output).requires(ToolsTags.Items.forgeTag("swords/" + materialName)).requires(ToolsTags.Items.forgeTag("pickaxes/" + materialName)).requires(ToolsTags.Items.forgeTag("shovels/" + materialName)).requires(ToolsTags.Items.forgeTag("hoes/" + materialName)).requires(ToolsTags.Items.forgeTag("axes/" + materialName)).requires(input).requires(input).requires(input).requires(input).unlockedBy("has_item", has(input))::save).generateAdvancement()
				.build(consumer, output.asItem().getRegistryName());
	}

	private void multiTool(ItemLike output, String materialName, TagKey<Item> input, Consumer<FinishedRecipe> consumer, String condition) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.MULTITOOL_CONDITION)).addCondition(new EnabledCondition(condition))
				.addRecipe(ShapelessRecipeBuilder.shapeless(output).requires(ToolsTags.Items.forgeTag("swords/" + materialName)).requires(ToolsTags.Items.forgeTag("pickaxes/" + materialName)).requires(ToolsTags.Items.forgeTag("shovels/" + materialName)).requires(ToolsTags.Items.forgeTag("hoes/" + materialName)).requires(ToolsTags.Items.forgeTag("axes/" + materialName)).requires(input).requires(input).requires(input).requires(input).unlockedBy("has_item", has(input))::save).generateAdvancement()
				.build(consumer, output.asItem().getRegistryName());
	}
}
