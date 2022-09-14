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
import net.minecraftforge.registries.ForgeRegistries;

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
		spearPattern(ToolsItems.STONE_SPEAR.get(), ItemTags.STONE_TOOL_MATERIALS, consumer);
		spearPattern(ToolsItems.GOLD_SPEAR.get(), Tags.Items.INGOTS_GOLD, consumer);
		spearPattern(ToolsItems.IRON_SPEAR.get(), Tags.Items.INGOTS_IRON, consumer);
		spearPattern(ToolsItems.DIAMOND_SPEAR.get(), Tags.Items.GEMS_DIAMOND, consumer);
		spearPattern(ToolsItems.NETHERITE_SPEAR.get(), Tags.Items.INGOTS_NETHERITE, consumer);

		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.BOOMERANGS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.WOOD_BOOMERANG.get()).define('X', ItemTags.PLANKS).pattern("XX").pattern("X ").pattern("XX").unlockedBy("has_planks", has(ItemTags.PLANKS))::save).generateAdvancement().build(consumer, ToolsItems.WOOD_BOOMERANG.getId());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.BOOMERANGS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.DIAMOND_BOOMERANG.get()).define('X', Tags.Items.GEMS_DIAMOND).define('Y', ToolsItems.WOOD_BOOMERANG.get()).pattern("XX").pattern("XY").pattern("XX").unlockedBy("has_diamonds", has(Tags.Items.GEMS_DIAMOND))::save).generateAdvancement().build(consumer, ToolsItems.DIAMOND_BOOMERANG.getId());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.POKEBALL_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.POKEBALL.get()).define('R', Tags.Items.DUSTS_REDSTONE).define('C', ItemTags.COALS).define('I', Tags.Items.INGOTS_IRON).define('B', Items.STONE_BUTTON).pattern("RRR").pattern("CBC").pattern("III").unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON)).unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))::save).generateAdvancement()
				.build(consumer, ToolsItems.POKEBALL.getId());

		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.WANDS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.BUILDING_WAND.get()).define('X', ItemTags.PLANKS).define('G', Tags.Items.INGOTS_GOLD).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD))::save).generateAdvancement().build(consumer, ToolsItems.BUILDING_WAND.getId());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.WANDS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.BREAKING_WAND.get()).define('X', ItemTags.PLANKS).define('G', Tags.Items.INGOTS_IRON).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))::save).generateAdvancement().build(consumer, ToolsItems.BREAKING_WAND.getId());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.WANDS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.MINING_WAND.get()).define('X', ItemTags.PLANKS).define('G', Tags.Items.GEMS_DIAMOND).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND))::save).generateAdvancement().build(consumer, ToolsItems.MINING_WAND.getId());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.WANDS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.REINFORCED_BUILDING_WAND.get()).define('X', Tags.Items.OBSIDIAN).define('G', Tags.Items.STORAGE_BLOCKS_GOLD).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_obsidian", has(Tags.Items.OBSIDIAN))::save).generateAdvancement().build(consumer, ToolsItems.REINFORCED_BUILDING_WAND.getId());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.WANDS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.REINFORCED_BREAKING_WAND.get()).define('X', Tags.Items.OBSIDIAN).define('G', Tags.Items.STORAGE_BLOCKS_IRON).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_obsidian", has(Tags.Items.OBSIDIAN))::save).generateAdvancement().build(consumer, ToolsItems.REINFORCED_BREAKING_WAND.getId());
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.WANDS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(ToolsItems.REINFORCED_MINING_WAND.get()).define('X', Tags.Items.OBSIDIAN).define('G', Tags.Items.STORAGE_BLOCKS_DIAMOND).pattern("XGX").pattern("XGX").pattern("XGX").unlockedBy("has_obsidian", has(Tags.Items.OBSIDIAN))::save).generateAdvancement().build(consumer, ToolsItems.REINFORCED_MINING_WAND.getId());

		armorSet(ToolsItems.CHICKEN_SUIT_HELMET.get(), ToolsItems.CHICKEN_SUIT_CHESTPLATE.get(), ToolsItems.CHICKEN_SUIT_LEGGINGS.get(), ToolsItems.CHICKEN_SUIT_BOOTS.get(), Tags.Items.FEATHERS, consumer, "chickensuit");

		multiTool(ToolsItems.WOODEN_MULTITOOL.get(), "wooden", ItemTags.PLANKS, consumer);
		multiTool(ToolsItems.STONE_MULTITOOL.get(), "stone", ItemTags.STONE_TOOL_MATERIALS, consumer);
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
			bucketPattern(group.BUCKET.get(), group.material, consumer, EnabledCondition.EXTRA_MATERIAL_CONDITION);
			shearPattern(group.SHEARS.get(), group.material, consumer, EnabledCondition.EXTRA_MATERIAL_CONDITION);
		});

		UpgradeRecipeBuilder.smithing(Ingredient.of(ToolsItems.DIAMOND_MULTITOOL.get()), Ingredient.of(Blocks.NETHERITE_BLOCK), ToolsItems.NETHERITE_MULTITOOL.get()).unlocks("has_netherite_block", has(Blocks.NETHERITE_BLOCK)).save(consumer, ToolsItems.NETHERITE_MULTITOOL.getId() + "_smithing");
		UpgradeRecipeBuilder.smithing(Ingredient.of(ToolsItems.DIAMOND_HAMMER.get()), Ingredient.of(Tags.Items.INGOTS_NETHERITE), ToolsItems.NETHERITE_HAMMER.get()).unlocks("has_netherite_ingot", has(Tags.Items.INGOTS_NETHERITE)).save(consumer, ToolsItems.NETHERITE_HAMMER.getId() + "_smithing");
		UpgradeRecipeBuilder.smithing(Ingredient.of(ToolsItems.DIAMOND_SPEAR.get()), Ingredient.of(Tags.Items.INGOTS_NETHERITE), ToolsItems.NETHERITE_SPEAR.get()).unlocks("has_netherite_ingot", has(Tags.Items.INGOTS_NETHERITE)).save(consumer, ToolsItems.NETHERITE_SPEAR.getId() + "_smithing");
		UpgradeRecipeBuilder.smithing(Ingredient.of(ToolsItems.DIAMOND_BUCKET.get()), Ingredient.of(Tags.Items.INGOTS_NETHERITE), ToolsItems.NETHERITE_BUCKET.get()).unlocks("has_netherite_ingot", has(Tags.Items.INGOTS_NETHERITE)).save(consumer, ToolsItems.NETHERITE_BUCKET.getId() + "_smithing");
		UpgradeRecipeBuilder.smithing(Ingredient.of(ToolsItems.DIAMOND_SHEARS.get()), Ingredient.of(Tags.Items.INGOTS_NETHERITE), ToolsItems.NETHERITE_SHEARS.get()).unlocks("has_netherite_ingot", has(Tags.Items.INGOTS_NETHERITE)).save(consumer, ToolsItems.NETHERITE_SHEARS.getId() + "_smithing");

		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.BETTER_BUCKETS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(Blocks.CAKE).define('A', ToolsTags.Items.BUCKETS_MILK).define('B', Items.SUGAR).define('C', Tags.Items.CROPS_WHEAT).define('E', Tags.Items.EGGS).pattern("AAA").pattern("BEB").pattern("CCC").unlockedBy("has_egg", has(Tags.Items.EGGS))::save).generateAdvancement().build(consumer, new ResourceLocation(key(Blocks.CAKE.asItem()) + "_alt"));
		bucketPattern(ToolsItems.WOOD_BUCKET.get(), ItemTags.PLANKS, consumer);
		bucketPattern(ToolsItems.STONE_BUCKET.get(), ItemTags.STONE_TOOL_MATERIALS, consumer);
		bucketPattern(ToolsItems.GOLD_BUCKET.get(), Tags.Items.INGOTS_GOLD, consumer);
		bucketPattern(ToolsItems.DIAMOND_BUCKET.get(), Tags.Items.GEMS_DIAMOND, consumer);
		bucketPattern(ToolsItems.NETHERITE_BUCKET.get(), Tags.Items.INGOTS_NETHERITE, consumer);

		shearPattern(ToolsItems.WOOD_SHEARS.get(), ItemTags.PLANKS, consumer);
		shearPattern(ToolsItems.STONE_SHEARS.get(), ItemTags.STONE_TOOL_MATERIALS, consumer);
		shearPattern(ToolsItems.GOLD_SHEARS.get(), Tags.Items.INGOTS_GOLD, consumer);
		shearPattern(ToolsItems.DIAMOND_SHEARS.get(), Tags.Items.GEMS_DIAMOND, consumer);
		shearPattern(ToolsItems.NETHERITE_SHEARS.get(), Tags.Items.INGOTS_NETHERITE, consumer);

		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.ULTIMATE_FIST_CONDITION)).addRecipe(ShapelessRecipeBuilder.shapeless(ToolsItems.ULTIMATE_FIST.get()).requires(ToolsItems.U_FRAGMENT.get()).requires(ToolsItems.L_FRAGMENT.get()).requires(ToolsItems.T_FRAGMENT.get()).requires(ToolsItems.I_FRAGMENT.get()).requires(ToolsItems.M_FRAGMENT.get()).requires(ToolsItems.A_FRAGMENT.get()).requires(ToolsItems.MISSING_FRAGMENT.get()).requires(ToolsItems.E_FRAGMENT.get())
				.requires(Tags.Items.NETHER_STARS).unlockedBy("has_nether_star", has(Tags.Items.NETHER_STARS)).unlockedBy("has_fragment", has(ToolsTags.Items.ULTIMATE_FRAGMENTS))::save).generateAdvancement().build(consumer, ToolsItems.ULTIMATE_FIST.getId());
	}

	@Override
	public String getName() {
		return "Assorted Tools recipes";
	}

	private void shearPattern(ItemLike output, TagKey<Item> input, Consumer<FinishedRecipe> consumer) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.MORE_SHEARS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(output).define('I', input).define('L', Tags.Items.LEATHER).pattern(" I").pattern("IL").unlockedBy("has_leather", has(Tags.Items.LEATHER)).unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, key(output.asItem()));
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.MORE_SHEARS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(output).define('I', input).define('L', Tags.Items.LEATHER).pattern("LI").pattern("I ").unlockedBy("has_leather", has(Tags.Items.LEATHER)).unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, new ResourceLocation(key(output.asItem()) + "_alt"));
	}

	private void shearPattern(ItemLike output, TagKey<Item> input, Consumer<FinishedRecipe> consumer, String condition) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.MORE_SHEARS_CONDITION)).addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(output).define('I', input).define('L', Tags.Items.LEATHER).pattern(" I").pattern("IL").unlockedBy("has_leather", has(Tags.Items.LEATHER)).unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, key(output.asItem()));
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.MORE_SHEARS_CONDITION)).addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(output).define('I', input).define('L', Tags.Items.LEATHER).pattern("LI").pattern("I ").unlockedBy("has_leather", has(Tags.Items.LEATHER)).unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, new ResourceLocation(key(output.asItem()) + "_alt"));
	}

	private void bucketPattern(ItemLike output, TagKey<Item> input, Consumer<FinishedRecipe> consumer) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.BETTER_BUCKETS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(output).define('I', input).pattern("I I").pattern(" I ").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, key(output.asItem()));
	}

	private void bucketPattern(ItemLike output, TagKey<Item> input, Consumer<FinishedRecipe> consumer, String condition) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.BETTER_BUCKETS_CONDITION)).addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(output).define('I', input).pattern("I I").pattern(" I ").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, key(output.asItem()));
	}

	private void spearPattern(ItemLike output, TagKey<Item> input, Consumer<FinishedRecipe> consumer) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.BETTER_SPEARS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(output).define('S', Tags.Items.RODS_WOODEN).define('I', input).pattern("I  ").pattern(" S ").pattern("  S").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, key(output.asItem()));
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.BETTER_SPEARS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(output).define('S', Tags.Items.RODS_WOODEN).define('I', input).pattern("SSI").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, new ResourceLocation(key(output.asItem()) + "_alt"));
	}

	private void spearPattern(ItemLike output, TagKey<Item> input, Consumer<FinishedRecipe> consumer, String condition) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.BETTER_SPEARS_CONDITION)).addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(output).define('S', Tags.Items.RODS_WOODEN).define('I', input).pattern("I  ").pattern(" S ").pattern("  S").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, key(output.asItem()));
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.BETTER_SPEARS_CONDITION)).addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(output).define('S', Tags.Items.RODS_WOODEN).define('I', input).pattern("SSI").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, new ResourceLocation(key(output.asItem()) + "_alt"));
	}

	private void hammerPattern(ItemLike output, TagKey<Item> input, Consumer<FinishedRecipe> consumer) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.HAMMERS_CONDITION)).addRecipe(ShapedRecipeBuilder.shaped(output).define('S', Tags.Items.RODS_WOODEN).define('I', input).pattern("III").pattern("ISI").pattern(" S ").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, key(output.asItem()));
	}

	private void hammerPattern(ItemLike output, TagKey<Item> input, Consumer<FinishedRecipe> consumer, String condition) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.HAMMERS_CONDITION)).addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(output).define('S', Tags.Items.RODS_WOODEN).define('I', input).pattern("III").pattern("ISI").pattern(" S ").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, key(output.asItem()));
	}

	private void toolSet(ItemLike pickaxe, ItemLike shovel, ItemLike axe, ItemLike hoe, ItemLike sword, TagKey<Item> input, Consumer<FinishedRecipe> consumer, String condition) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(pickaxe).define('X', input).define('S', Tags.Items.RODS_WOODEN).pattern("XXX").pattern(" S ").pattern(" S ").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, key(pickaxe.asItem()));
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(shovel).define('X', input).define('S', Tags.Items.RODS_WOODEN).pattern("X").pattern("S").pattern("S").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, key(shovel.asItem()));
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(axe).define('X', input).define('S', Tags.Items.RODS_WOODEN).pattern("XX").pattern("XS").pattern(" S").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, key(axe.asItem()));
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(axe).define('X', input).define('S', Tags.Items.RODS_WOODEN).pattern("XX").pattern("SX").pattern("S ").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, new ResourceLocation(AssortedTools.MODID, key(axe.asItem()).getPath() + "_alt"));
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(hoe).define('X', input).define('S', Tags.Items.RODS_WOODEN).pattern("XX").pattern(" S").pattern(" S").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, key(hoe.asItem()));
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(hoe).define('X', input).define('S', Tags.Items.RODS_WOODEN).pattern("XX").pattern("S ").pattern("S ").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, new ResourceLocation(AssortedTools.MODID, key(hoe.asItem()).getPath() + "_alt"));
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(sword).define('X', input).define('S', Tags.Items.RODS_WOODEN).pattern("X").pattern("X").pattern("S").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, key(sword.asItem()));
	}

	private void armorSet(ItemLike helmet, ItemLike chestplate, ItemLike leggings, ItemLike boots, TagKey<Item> input, Consumer<FinishedRecipe> consumer, String condition) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(helmet).define('X', input).pattern("XXX").pattern("X X").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, key(helmet.asItem()));
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(chestplate).define('X', input).pattern("X X").pattern("XXX").pattern("XXX").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, key(chestplate.asItem()));
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(leggings).define('X', input).pattern("XXX").pattern("X X").pattern("X X").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, key(leggings.asItem()));
		ConditionalRecipe.builder().addCondition(new EnabledCondition(condition)).addRecipe(ShapedRecipeBuilder.shaped(boots).define('X', input).pattern("X X").pattern("X X").unlockedBy("has_item", has(input))::save).generateAdvancement().build(consumer, key(boots.asItem()));
	}

	private void multiTool(ItemLike output, String materialName, TagKey<Item> input, Consumer<FinishedRecipe> consumer) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.MULTITOOL_CONDITION))
				.addRecipe(ShapelessRecipeBuilder.shapeless(output).requires(ToolsTags.Items.forgeTag("swords/" + materialName)).requires(ToolsTags.Items.forgeTag("pickaxes/" + materialName)).requires(ToolsTags.Items.forgeTag("shovels/" + materialName)).requires(ToolsTags.Items.forgeTag("hoes/" + materialName)).requires(ToolsTags.Items.forgeTag("axes/" + materialName)).requires(input).requires(input).requires(input).requires(input).unlockedBy("has_item", has(input))::save).generateAdvancement()
				.build(consumer, key(output.asItem()));
	}

	private void multiTool(ItemLike output, String materialName, TagKey<Item> input, Consumer<FinishedRecipe> consumer, String condition) {
		ConditionalRecipe.builder().addCondition(new EnabledCondition(EnabledCondition.MULTITOOL_CONDITION)).addCondition(new EnabledCondition(condition))
				.addRecipe(ShapelessRecipeBuilder.shapeless(output).requires(ToolsTags.Items.forgeTag("swords/" + materialName)).requires(ToolsTags.Items.forgeTag("pickaxes/" + materialName)).requires(ToolsTags.Items.forgeTag("shovels/" + materialName)).requires(ToolsTags.Items.forgeTag("hoes/" + materialName)).requires(ToolsTags.Items.forgeTag("axes/" + materialName)).requires(input).requires(input).requires(input).requires(input).unlockedBy("has_item", has(input))::save).generateAdvancement()
				.build(consumer, key(output.asItem()));
	}

	private ResourceLocation key(Item item) {
		return ForgeRegistries.ITEMS.getKey(item);
	}
}
