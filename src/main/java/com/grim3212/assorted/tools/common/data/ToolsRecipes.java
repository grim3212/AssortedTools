package com.grim3212.assorted.tools.common.data;

import java.util.function.Consumer;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.handler.EnabledCondition;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.grim3212.assorted.tools.common.util.ToolsTags;

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
import net.minecraft.util.ResourceLocation;
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
	}

	@Override
	public String getName() {
		return "Assorted Tools recipes";
	}

	private void hammerPattern(IItemProvider output, ITag<Item> input, Consumer<IFinishedRecipe> consumer) {
		hammerPattern(output, input, consumer, "hammers");
	}

	private void hammerPattern(IItemProvider output, ITag<Item> input, Consumer<IFinishedRecipe> consumer, String condition) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shapedRecipe(output).key('S', Tags.Items.RODS_WOODEN).key('I', input).patternLine("III").patternLine("ISI").patternLine(" S ").addCriterion("has_item", hasItem(input))::build).generateAdvancement().build(consumer, output.asItem().getRegistryName());
	}

	private void toolSet(IItemProvider pickaxe, IItemProvider shovel, IItemProvider axe, IItemProvider hoe, IItemProvider sword, ITag<Item> input, Consumer<IFinishedRecipe> consumer, String condition) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shapedRecipe(pickaxe).key('X', input).key('S', Tags.Items.RODS_WOODEN).patternLine("XXX").patternLine(" S ").patternLine(" S ").addCriterion("has_item", hasItem(input))::build).generateAdvancement().build(consumer, pickaxe.asItem().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shapedRecipe(shovel).key('X', input).key('S', Tags.Items.RODS_WOODEN).patternLine("X").patternLine("S").patternLine("S").addCriterion("has_item", hasItem(input))::build).generateAdvancement().build(consumer, shovel.asItem().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shapedRecipe(axe).key('X', input).key('S', Tags.Items.RODS_WOODEN).patternLine("XX").patternLine("XS").patternLine(" S").addCriterion("has_item", hasItem(input))::build).generateAdvancement().build(consumer, axe.asItem().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shapedRecipe(axe).key('X', input).key('S', Tags.Items.RODS_WOODEN).patternLine("XX").patternLine("SX").patternLine("S ").addCriterion("has_item", hasItem(input))::build).generateAdvancement().build(consumer, new ResourceLocation(AssortedTools.MODID, axe.asItem().getRegistryName().getPath() + "_alt"));
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shapedRecipe(hoe).key('X', input).key('S', Tags.Items.RODS_WOODEN).patternLine("XX").patternLine(" S").patternLine(" S").addCriterion("has_item", hasItem(input))::build).generateAdvancement().build(consumer, hoe.asItem().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shapedRecipe(hoe).key('X', input).key('S', Tags.Items.RODS_WOODEN).patternLine("XX").patternLine("S ").patternLine("S ").addCriterion("has_item", hasItem(input))::build).generateAdvancement().build(consumer, new ResourceLocation(AssortedTools.MODID, hoe.asItem().getRegistryName().getPath() + "_alt"));
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shapedRecipe(sword).key('X', input).key('S', Tags.Items.RODS_WOODEN).patternLine("X").patternLine("X").patternLine("S").addCriterion("has_item", hasItem(input))::build).generateAdvancement().build(consumer, sword.asItem().getRegistryName());
	}

	private void armorSet(IItemProvider helmet, IItemProvider chestplate, IItemProvider leggings, IItemProvider boots, ITag<Item> input, Consumer<IFinishedRecipe> consumer, String condition) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shapedRecipe(helmet).key('X', input).patternLine("XXX").patternLine("X X").addCriterion("has_item", hasItem(input))::build).generateAdvancement().build(consumer, helmet.asItem().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shapedRecipe(chestplate).key('X', input).patternLine("X X").patternLine("XXX").patternLine("XXX").addCriterion("has_item", hasItem(input))::build).generateAdvancement().build(consumer, chestplate.asItem().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shapedRecipe(leggings).key('X', input).patternLine("XXX").patternLine("X X").patternLine("X X").addCriterion("has_item", hasItem(input))::build).generateAdvancement().build(consumer, leggings.asItem().getRegistryName());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shapedRecipe(boots).key('X', input).patternLine("X X").patternLine("X X").addCriterion("has_item", hasItem(input))::build).generateAdvancement().build(consumer, boots.asItem().getRegistryName());
	}

	private void multiTool(IItemProvider output, String materialName, ITag<Item> input, Consumer<IFinishedRecipe> consumer) {
		multiTool(output, materialName, input, consumer, "multitools");
	}

	private void multiTool(IItemProvider output, String materialName, ITag<Item> input, Consumer<IFinishedRecipe> consumer, String condition) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapelessRecipeBuilder.shapelessRecipe(output).addIngredient(ToolsTags.Items.forgeTag("swords/" + materialName)).addIngredient(ToolsTags.Items.forgeTag("pickaxes/" + materialName)).addIngredient(ToolsTags.Items.forgeTag("shovels/" + materialName)).addIngredient(ToolsTags.Items.forgeTag("hoes/" + materialName)).addIngredient(ToolsTags.Items.forgeTag("axes/" + materialName)).addIngredient(input)
				.addIngredient(input).addIngredient(input).addIngredient(input).addCriterion("has_item", hasItem(input))::build).generateAdvancement().build(consumer, output.asItem().getRegistryName());
	}
}
