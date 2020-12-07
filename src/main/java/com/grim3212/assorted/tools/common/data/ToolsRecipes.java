package com.grim3212.assorted.tools.common.data;

import java.util.function.Consumer;

import com.grim3212.assorted.tools.common.handler.EnabledCondition;
import com.grim3212.assorted.tools.common.item.ToolsItems;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;

public class ToolsRecipes extends RecipeProvider {

	public ToolsRecipes(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		hammerPattern(ToolsItems.NETHERITE_HAMMER.get(), Tags.Items.INGOTS_NETHERITE, consumer);
		hammerPattern(ToolsItems.DIAMOND_HAMMER.get(), Tags.Items.GEMS_DIAMOND, consumer);
		hammerPattern(ToolsItems.IRON_HAMMER.get(), Tags.Items.INGOTS_IRON, consumer);
		hammerPattern(ToolsItems.GOLD_HAMMER.get(), Tags.Items.INGOTS_GOLD, consumer);
		hammerPattern(ToolsItems.STONE_HAMMER.get(), Tags.Items.COBBLESTONE, consumer);
		hammerPattern(ToolsItems.WOOD_HAMMER.get(), ItemTags.PLANKS, consumer);

		ConditionalRecipe.builder().addCondition(new EnabledCondition("boomerangs")).addRecipe(ShapedRecipeBuilder.shapedRecipe(ToolsItems.WOOD_BOOMERANG.get()).key('X', ItemTags.PLANKS).patternLine("XX").patternLine("X ").patternLine("XX").addCriterion("has_planks", hasItem(ItemTags.PLANKS))::build).generateAdvancement().build(consumer, ToolsItems.WOOD_BOOMERANG.get().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition("boomerangs")).addRecipe(ShapedRecipeBuilder.shapedRecipe(ToolsItems.DIAMOND_BOOMERANG.get()).key('X', Tags.Items.GEMS_DIAMOND).key('Y', ToolsItems.WOOD_BOOMERANG.get()).patternLine("XX").patternLine("XY").patternLine("XX").addCriterion("has_diamonds", hasItem(Tags.Items.GEMS_DIAMOND))::build).generateAdvancement().build(consumer, ToolsItems.DIAMOND_BOOMERANG.get().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition("pokeball")).addRecipe(ShapedRecipeBuilder.shapedRecipe(ToolsItems.POKEBALL.get()).key('R', Tags.Items.DUSTS_REDSTONE).key('C', ItemTags.COALS).key('I', Tags.Items.INGOTS_IRON).key('B', Items.STONE_BUTTON).patternLine("RRR").patternLine("CBC").patternLine("III").addCriterion("has_iron", hasItem(Tags.Items.INGOTS_IRON)).addCriterion("has_redstone", hasItem(Tags.Items.DUSTS_REDSTONE))::build).generateAdvancement()
				.build(consumer, ToolsItems.POKEBALL.get().getRegistryName());

		ConditionalRecipe.builder().addCondition(new EnabledCondition("wands")).addRecipe(ShapedRecipeBuilder.shapedRecipe(ToolsItems.BUILDING_WAND.get()).key('X', ItemTags.PLANKS).key('G', Tags.Items.INGOTS_GOLD).patternLine("XGX").patternLine("XGX").patternLine("XGX").addCriterion("has_gold", hasItem(Tags.Items.INGOTS_GOLD))::build).generateAdvancement().build(consumer, ToolsItems.BUILDING_WAND.get().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition("wands")).addRecipe(ShapedRecipeBuilder.shapedRecipe(ToolsItems.BREAKING_WAND.get()).key('X', ItemTags.PLANKS).key('G', Tags.Items.INGOTS_IRON).patternLine("XGX").patternLine("XGX").patternLine("XGX").addCriterion("has_iron", hasItem(Tags.Items.INGOTS_IRON))::build).generateAdvancement().build(consumer, ToolsItems.BREAKING_WAND.get().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition("wands")).addRecipe(ShapedRecipeBuilder.shapedRecipe(ToolsItems.MINING_WAND.get()).key('X', ItemTags.PLANKS).key('G', Tags.Items.GEMS_DIAMOND).patternLine("XGX").patternLine("XGX").patternLine("XGX").addCriterion("has_diamond", hasItem(Tags.Items.GEMS_DIAMOND))::build).generateAdvancement().build(consumer, ToolsItems.MINING_WAND.get().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition("wands")).addRecipe(ShapedRecipeBuilder.shapedRecipe(ToolsItems.REINFORCED_BUILDING_WAND.get()).key('X', Tags.Items.OBSIDIAN).key('G', Tags.Items.STORAGE_BLOCKS_GOLD).patternLine("XGX").patternLine("XGX").patternLine("XGX").addCriterion("has_obsidian", hasItem(Tags.Items.OBSIDIAN))::build).generateAdvancement().build(consumer, ToolsItems.REINFORCED_BUILDING_WAND.get().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition("wands")).addRecipe(ShapedRecipeBuilder.shapedRecipe(ToolsItems.REINFORCED_BREAKING_WAND.get()).key('X', Tags.Items.OBSIDIAN).key('G', Tags.Items.STORAGE_BLOCKS_IRON).patternLine("XGX").patternLine("XGX").patternLine("XGX").addCriterion("has_obsidian", hasItem(Tags.Items.OBSIDIAN))::build).generateAdvancement().build(consumer, ToolsItems.REINFORCED_BREAKING_WAND.get().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition("wands")).addRecipe(ShapedRecipeBuilder.shapedRecipe(ToolsItems.REINFORCED_MINING_WAND.get()).key('X', Tags.Items.OBSIDIAN).key('G', Tags.Items.STORAGE_BLOCKS_DIAMOND).patternLine("XGX").patternLine("XGX").patternLine("XGX").addCriterion("has_obsidian", hasItem(Tags.Items.OBSIDIAN))::build).generateAdvancement().build(consumer, ToolsItems.REINFORCED_MINING_WAND.get().getRegistryName());

		armorSet(ToolsItems.CHICKEN_SUIT_HELMET.get(), ToolsItems.CHICKEN_SUIT_CHESTPLATE.get(), ToolsItems.CHICKEN_SUIT_LEGGINGS.get(), ToolsItems.CHICKEN_SUIT_BOOTS.get(), Tags.Items.FEATHERS, consumer);

		multiTool(ToolsItems.WOODEN_MULTITOOL.get(), Items.WOODEN_SWORD, Items.WOODEN_PICKAXE, Items.WOODEN_SHOVEL, Items.WOODEN_HOE, Items.WOODEN_AXE, ItemTags.PLANKS, consumer);
		multiTool(ToolsItems.STONE_MULTITOOL.get(), Items.STONE_SWORD, Items.STONE_PICKAXE, Items.STONE_SHOVEL, Items.STONE_HOE, Items.STONE_AXE, Tags.Items.COBBLESTONE, consumer);
		multiTool(ToolsItems.GOLDEN_MULTITOOL.get(), Items.GOLDEN_SWORD, Items.GOLDEN_PICKAXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_HOE, Items.GOLDEN_AXE, Tags.Items.INGOTS_GOLD, consumer);
		multiTool(ToolsItems.IRON_MULTITOOL.get(), Items.IRON_SWORD, Items.IRON_PICKAXE, Items.IRON_SHOVEL, Items.IRON_HOE, Items.IRON_AXE, Tags.Items.INGOTS_IRON, consumer);
		multiTool(ToolsItems.DIAMOND_MULTITOOL.get(), Items.DIAMOND_SWORD, Items.DIAMOND_PICKAXE, Items.DIAMOND_SHOVEL, Items.DIAMOND_HOE, Items.DIAMOND_AXE, Tags.Items.GEMS_DIAMOND, consumer);
		multiTool(ToolsItems.NETHERITE_MULTITOOL.get(), Items.NETHERITE_SWORD, Items.NETHERITE_PICKAXE, Items.NETHERITE_SHOVEL, Items.NETHERITE_HOE, Items.NETHERITE_AXE, Tags.Items.INGOTS_NETHERITE, consumer);
	}

	@Override
	public String getName() {
		return "Assorted Tools recipes";
	}

	private void hammerPattern(IItemProvider output, ITag<Item> input, Consumer<IFinishedRecipe> consumer) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition("hammers")).addRecipe(ShapedRecipeBuilder.shapedRecipe(output).key('S', Tags.Items.RODS_WOODEN).key('I', input).patternLine("III").patternLine("ISI").patternLine(" S ").addCriterion("has_item", hasItem(input))::build).generateAdvancement().build(consumer, output.asItem().getRegistryName());
	}

	private void armorSet(IItemProvider helmet, IItemProvider chestplate, IItemProvider leggings, IItemProvider boots, ITag<Item> input, Consumer<IFinishedRecipe> consumer) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition("chickensuit")).addRecipe(ShapedRecipeBuilder.shapedRecipe(helmet).key('X', input).patternLine("XXX").patternLine("X X").addCriterion("has_item", hasItem(input))::build).generateAdvancement().build(consumer, helmet.asItem().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition("chickensuit")).addRecipe(ShapedRecipeBuilder.shapedRecipe(chestplate).key('X', input).patternLine("X X").patternLine("XXX").patternLine("XXX").addCriterion("has_item", hasItem(input))::build).generateAdvancement().build(consumer, chestplate.asItem().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition("chickensuit")).addRecipe(ShapedRecipeBuilder.shapedRecipe(leggings).key('X', input).patternLine("XXX").patternLine("X X").patternLine("X X").addCriterion("has_item", hasItem(input))::build).generateAdvancement().build(consumer, leggings.asItem().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition("chickensuit")).addRecipe(ShapedRecipeBuilder.shapedRecipe(boots).key('X', input).patternLine("X X").patternLine("X X").addCriterion("has_item", hasItem(input))::build).generateAdvancement().build(consumer, boots.asItem().getRegistryName());
	}

	private void multiTool(IItemProvider output, IItemProvider sword, IItemProvider pick, IItemProvider shovel, IItemProvider hoe, IItemProvider axe, ITag<Item> input, Consumer<IFinishedRecipe> consumer) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition("multitools")).addRecipe(ShapelessRecipeBuilder.shapelessRecipe(output).addIngredient(sword).addIngredient(pick).addIngredient(shovel).addIngredient(hoe).addIngredient(axe).addIngredient(input).addIngredient(input).addIngredient(input).addIngredient(input).addCriterion("has_item", hasItem(input))::build).generateAdvancement().build(consumer, output.asItem().getRegistryName());
	}
}
