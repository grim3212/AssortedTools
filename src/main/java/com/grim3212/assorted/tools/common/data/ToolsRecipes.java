package com.grim3212.assorted.tools.common.data;

import java.util.function.Consumer;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.handler.EnabledCondition;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.grim3212.assorted.tools.common.util.ToolsTags;

import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.data.SmithingRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;

public class ToolsRecipes extends RecipeProvider {

	public ToolsRecipes(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
		hammerPattern(ToolsItems.NETHERITE_HAMMER.get(), Tags.Items.INGOTS_NETHERITE, consumer);
		hammerPattern(ToolsItems.DIAMOND_HAMMER.get(), Tags.Items.GEMS_DIAMOND, consumer);
		hammerPattern(ToolsItems.IRON_HAMMER.get(), Tags.Items.INGOTS_IRON, consumer);
		hammerPattern(ToolsItems.GOLD_HAMMER.get(), Tags.Items.INGOTS_GOLD, consumer);
		hammerPattern(ToolsItems.STONE_HAMMER.get(), Tags.Items.COBBLESTONE, consumer);
		hammerPattern(ToolsItems.WOOD_HAMMER.get(), ItemTags.PLANKS, consumer);

		ConditionalRecipe.builder().addCondition(new EnabledCondition("boomerangs")).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.WOOD_BOOMERANG.get()).define('X', ItemTags.PLANKS).pattern("XX").pattern("X ").pattern("XX").unlockedBy("has_planks", has(ItemTags.PLANKS))::save).generateAdvancement().build(consumer, ToolsItems.WOOD_BOOMERANG.get().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition("boomerangs")).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.DIAMOND_BOOMERANG.get()).define('X', Tags.Items.GEMS_DIAMOND).define('Y', ToolsItems.WOOD_BOOMERANG.get()).pattern("XX").pattern("XY").pattern("XX").unlockedBy("has_diamonds", has(Tags.Items.GEMS_DIAMOND))::save).generateAdvancement().build(consumer, ToolsItems.DIAMOND_BOOMERANG.get().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition("pokeball")).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.POKEBALL.get()).define('R', Tags.Items.DUSTS_REDSTONE).define('C', ItemTags.COALS).define('I', Tags.Items.INGOTS_IRON).define('B', Items.STONE_BUTTON).pattern("RRR").pattern("CBC").pattern("III").unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON)).unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))::save).generateAdvancement()
				.build(consumer, ToolsItems.POKEBALL.get().getRegistryName());

		ConditionalRecipe.builder().addCondition(new EnabledCondition("wands")).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.BUILDING_WAND.get()).define('X', ItemTags.PLANKS).define('G', Tags.Items.INGOTS_GOLD).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD))::save).generateAdvancement().build(consumer, ToolsItems.BUILDING_WAND.get().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition("wands")).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.BREAKING_WAND.get()).define('X', ItemTags.PLANKS).define('G', Tags.Items.INGOTS_IRON).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))::save).generateAdvancement().build(consumer, ToolsItems.BREAKING_WAND.get().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition("wands")).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.MINING_WAND.get()).define('X', ItemTags.PLANKS).define('G', Tags.Items.GEMS_DIAMOND).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND))::save).generateAdvancement().build(consumer, ToolsItems.MINING_WAND.get().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition("wands")).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.REINFORCED_BUILDING_WAND.get()).define('X', Tags.Items.OBSIDIAN).define('G', Tags.Items.STORAGE_BLOCKS_GOLD).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_obsidian", has(Tags.Items.OBSIDIAN))::save).generateAdvancement().build(consumer, ToolsItems.REINFORCED_BUILDING_WAND.get().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition("wands")).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.REINFORCED_BREAKING_WAND.get()).define('X', Tags.Items.OBSIDIAN).define('G', Tags.Items.STORAGE_BLOCKS_IRON).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_obsidian", has(Tags.Items.OBSIDIAN))::save).generateAdvancement().build(consumer, ToolsItems.REINFORCED_BREAKING_WAND.get().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition("wands")).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.REINFORCED_MINING_WAND.get()).define('X', Tags.Items.OBSIDIAN).define('G', Tags.Items.STORAGE_BLOCKS_DIAMOND).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_obsidian", has(Tags.Items.OBSIDIAN))::save).generateAdvancement().build(consumer, ToolsItems.REINFORCED_MINING_WAND.get().getRegistryName());

		armorSet(ToolsItems.CHICKEN_SUIT_HELMET.get(), ToolsItems.CHICKEN_SUIT_CHESTPLATE.get(), ToolsItems.CHICKEN_SUIT_LEGGINGS.get(), ToolsItems.CHICKEN_SUIT_BOOTS.get(), Tags.Items.FEATHERS, consumer, "chickensuit");

		multiTool(ToolsItems.WOODEN_MULTITOOL.get(), "wooden", ItemTags.PLANKS, consumer);
		multiTool(ToolsItems.STONE_MULTITOOL.get(), "stone", Tags.Items.COBBLESTONE, consumer);
		multiTool(ToolsItems.GOLDEN_MULTITOOL.get(), "golden", Tags.Items.INGOTS_GOLD, consumer);
		multiTool(ToolsItems.IRON_MULTITOOL.get(), "iron", Tags.Items.INGOTS_IRON, consumer);
		multiTool(ToolsItems.DIAMOND_MULTITOOL.get(), "diamond", Tags.Items.GEMS_DIAMOND, consumer);
		multiTool(ToolsItems.NETHERITE_MULTITOOL.get(), "netherite", Tags.Items.INGOTS_NETHERITE, consumer);

		ToolsItems.MATERIAL_GROUPS.forEach((s, group) -> {
			toolSet(group.PICKAXE.get(), group.SHOVEL.get(), group.AXE.get(), group.HOE.get(), group.SWORD.get(), group.material, consumer, "extramaterials");
			hammerPattern(group.HAMMER.get(), group.material, consumer, "extramaterials");
			multiTool(group.MULTITOOL.get(), s, group.material, consumer, "extramaterials");
			armorSet(group.HELMET.get(), group.CHESTPLATE.get(), group.LEGGINGS.get(), group.BOOTS.get(), group.material, consumer, "extramaterials");
		});

		SmithingRecipeBuilder.smithing(Ingredient.of(ToolsItems.DIAMOND_MULTITOOL.get()), Ingredient.of(Blocks.NETHERITE_BLOCK), ToolsItems.NETHERITE_MULTITOOL.get()).unlocks("has_netherite_block", has(Blocks.NETHERITE_BLOCK)).save(consumer, ToolsItems.NETHERITE_MULTITOOL.get().getRegistryName() + "_smithing");
	}

	@Override
	public String getName() {
		return "Assorted Tools recipes";
	}

	private void hammerPattern(IItemProvider output, ITag<Item> input, Consumer<IFinishedRecipe> consumer) {
		hammerPattern(output, input, consumer, "hammers");
	}

	private void hammerPattern(IItemProvider output, ITag<Item> input, Consumer<IFinishedRecipe> consumer, String condition) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(output).define('S', Tags.Items.RODS_WOODEN).define('I', input).pattern("III").pattern("ISI").pattern(" S ").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, output.asItem().getRegistryName());
	}

	private void toolSet(IItemProvider pickaxe, IItemProvider shovel, IItemProvider axe, IItemProvider hoe, IItemProvider sword, ITag<Item> input, Consumer<IFinishedRecipe> consumer, String condition) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(pickaxe).define('X', input).define('S', Tags.Items.RODS_WOODEN).pattern("XXX").pattern(" S ").pattern(" S ").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, pickaxe.asItem().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(shovel).define('X', input).define('S', Tags.Items.RODS_WOODEN).pattern("X").pattern("S").pattern("S").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, shovel.asItem().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(axe).define('X', input).define('S', Tags.Items.RODS_WOODEN).pattern("XX").pattern("XS").pattern(" S").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, axe.asItem().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(axe).define('X', input).define('S', Tags.Items.RODS_WOODEN).pattern("XX").pattern("SX").pattern("S ").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, new ResourceLocation(AssortedTools.MODID, axe.asItem().getRegistryName().getPath() + "_alt"));
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(hoe).define('X', input).define('S', Tags.Items.RODS_WOODEN).pattern("XX").pattern(" S").pattern(" S").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, hoe.asItem().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(hoe).define('X', input).define('S', Tags.Items.RODS_WOODEN).pattern("XX").pattern("S ").pattern("S ").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, new ResourceLocation(AssortedTools.MODID, hoe.asItem().getRegistryName().getPath() + "_alt"));
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(sword).define('X', input).define('S', Tags.Items.RODS_WOODEN).pattern("X").pattern("X").pattern("S").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, sword.asItem().getRegistryName());
	}

	private void armorSet(IItemProvider helmet, IItemProvider chestplate, IItemProvider leggings, IItemProvider boots, ITag<Item> input, Consumer<IFinishedRecipe> consumer, String condition) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(helmet).define('X', input).pattern("XXX").pattern("X X").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, helmet.asItem().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(chestplate).define('X', input).pattern("X X").pattern("XXX").pattern("XXX").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, chestplate.asItem().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(leggings).define('X', input).pattern("XXX").pattern("X X").pattern("X X").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, leggings.asItem().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(boots).define('X', input).pattern("X X").pattern("X X").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, boots.asItem().getRegistryName());
	}

	private void multiTool(IItemProvider output, String materialName, ITag<Item> input, Consumer<IFinishedRecipe> consumer) {
		multiTool(output, materialName, input, consumer, "multitools");
	}

	private void multiTool(IItemProvider output, String materialName, ITag<Item> input, Consumer<IFinishedRecipe> consumer, String condition) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapelessRecipeBuilder.shapeless(output).requires(ToolsTags.Items.forgeTag("swords/" + materialName)).requires(ToolsTags.Items.forgeTag("pickaxes/" + materialName)).requires(ToolsTags.Items.forgeTag("shovels/" + materialName)).requires(ToolsTags.Items.forgeTag("hoes/" + materialName)).requires(ToolsTags.Items.forgeTag("axes/" + materialName)).requires(input)
				.requires(input).requires(input).requires(input).unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, output.asItem().getRegistryName());
	}
}
