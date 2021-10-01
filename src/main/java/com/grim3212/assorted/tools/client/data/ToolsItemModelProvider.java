package com.grim3212.assorted.tools.client.data;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.item.ToolsItems;

import net.minecraft.client.renderer.block.model.BlockModel.GuiLight;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder.Perspective;
import net.minecraftforge.client.model.generators.loaders.DynamicBucketModelBuilder;
import net.minecraftforge.client.model.generators.loaders.SeparatePerspectiveModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ToolsItemModelProvider extends ItemModelProvider {

	public ToolsItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, AssortedTools.MODID, existingFileHelper);
	}

	@Override
	public String getName() {
		return "Assorted Tools item models";
	}

	@Override
	protected void registerModels() {
		tool(ToolsItems.WOOD_HAMMER.get());
		tool(ToolsItems.STONE_HAMMER.get());
		tool(ToolsItems.GOLD_HAMMER.get());
		tool(ToolsItems.IRON_HAMMER.get());
		tool(ToolsItems.DIAMOND_HAMMER.get());
		tool(ToolsItems.NETHERITE_HAMMER.get());
		handheldItem(ToolsItems.WOOD_BOOMERANG.get());
		handheldItem(ToolsItems.DIAMOND_BOOMERANG.get());
		generatedItem(ToolsItems.POKEBALL.get());

		handheldItem(ToolsItems.BUILDING_WAND.get());
		handheldItem(ToolsItems.REINFORCED_BUILDING_WAND.get());
		handheldItem(ToolsItems.BREAKING_WAND.get());
		handheldItem(ToolsItems.REINFORCED_BREAKING_WAND.get());
		handheldItem(ToolsItems.MINING_WAND.get());
		handheldItem(ToolsItems.REINFORCED_MINING_WAND.get());

		armor(ToolsItems.CHICKEN_SUIT_HELMET.get());
		armor(ToolsItems.CHICKEN_SUIT_CHESTPLATE.get());
		armor(ToolsItems.CHICKEN_SUIT_LEGGINGS.get());
		armor(ToolsItems.CHICKEN_SUIT_BOOTS.get());

		tool(ToolsItems.WOODEN_MULTITOOL.get());
		tool(ToolsItems.STONE_MULTITOOL.get());
		tool(ToolsItems.GOLDEN_MULTITOOL.get());
		tool(ToolsItems.IRON_MULTITOOL.get());
		tool(ToolsItems.DIAMOND_MULTITOOL.get());
		tool(ToolsItems.NETHERITE_MULTITOOL.get());

		generateSpear(ToolsItems.WOOD_SPEAR.get());
		generateSpear(ToolsItems.STONE_SPEAR.get());
		generateSpear(ToolsItems.IRON_SPEAR.get());
		generateSpear(ToolsItems.GOLD_SPEAR.get());
		generateSpear(ToolsItems.DIAMOND_SPEAR.get());
		generateSpear(ToolsItems.NETHERITE_SPEAR.get());

		bucketItem(ToolsItems.DIAMOND_BUCKET.get(), ToolsItems.DIAMOND_MILK_BUCKET.get());

		ToolsItems.MATERIAL_GROUPS.forEach((s, group) -> {
			tool(group.PICKAXE.get());
			tool(group.SHOVEL.get());
			tool(group.AXE.get());
			tool(group.HOE.get());
			tool(group.SWORD.get());
			tool(group.HAMMER.get());
			tool(group.MULTITOOL.get());
			generateSpear(group.SPEAR.get());

			armor(group.HELMET.get());
			armor(group.CHESTPLATE.get());
			armor(group.LEGGINGS.get());
			armor(group.BOOTS.get());
		});
	}

	private ItemModelBuilder tool(Item i) {
		String name = name(i);
		return withExistingParent(name, "item/handheld").texture("layer0", prefix("item/tools/" + name));
	}

	private ItemModelBuilder armor(Item i) {
		String name = name(i);
		return withExistingParent(name, "item/generated").texture("layer0", prefix("item/armors/" + name));
	}

	private ItemModelBuilder generatedItem(String name) {
		return withExistingParent(name, "item/generated").texture("layer0", prefix("item/" + name));
	}

	private ItemModelBuilder generatedItem(Item i) {
		return generatedItem(name(i));
	}

	private void bucketItem(Item bucket, Item milkBucket) {
		String name = name(bucket);
		getBuilder(name).parent(getExistingFile(new ResourceLocation("forge:item/default"))).texture("base", prefix("item/" + name)).texture("fluid", prefix("item/bucket_fluid")).texture("cover", prefix("item/" + name + "_covered")).customLoader(DynamicBucketModelBuilder::begin).fluid(Fluids.EMPTY).flipGas(true).applyTint(true).applyFluidLuminosity(true).coverIsMask(true).end();
		withExistingParent(name(milkBucket), "item/generated").texture("layer0", prefix("item/" + name)).texture("layer1", prefix("item/overlay_milk"));
	}

	private ItemModelBuilder handheldItem(String name) {
		return withExistingParent(name, "item/handheld").texture("layer0", prefix("item/" + name));
	}

	private ItemModelBuilder handheldItem(Item i) {
		return handheldItem(name(i));
	}

	private static String name(Item i) {
		return Registry.ITEM.getKey(i).getPath();
	}

	private ResourceLocation prefix(String name) {
		return new ResourceLocation(AssortedTools.MODID, name);
	}

	private void generateSpear(Item item) {
		String name = name(item);
		ItemModelBuilder guiModel = nested().parent(withExistingParent(name + "_gui", "item/generated").texture("layer0", prefix("item/tools/" + name)));
		ItemModelBuilder throwingModel = getBuilder(name + "_throwing").guiLight(GuiLight.FRONT).texture("particle", prefix("item/tools/" + name)).customLoader(SeparatePerspectiveModelBuilder::begin)
				// Throwing model is "base" so that we can have our transforms
				.base(nested().parent(getExistingFile(mcLoc("trident_throwing"))).texture("particle", prefix("item/tools/" + name)))
				// Gui, ground, and fixed all use the normal "item model"
				.perspective(TransformType.GUI, guiModel).perspective(TransformType.GROUND, guiModel).perspective(TransformType.FIXED, guiModel).end();

		getBuilder(name).guiLight(GuiLight.FRONT).texture("particle", prefix("item/tools/" + name))
				// Override when throwing to the throwing model to ensure we have the correct
				// transforms
				.override().predicate(prefix("throwing"), 1).model(throwingModel).end().customLoader(SeparatePerspectiveModelBuilder::begin)
				// In hand model is base
				.base(nested().parent(getExistingFile(mcLoc("trident_in_hand"))).texture("particle", prefix("item/tools/" + name))
						// Add head transformation
						.transforms().transform(Perspective.HEAD).rotation(0, 180, 120).translation(8, 10, -11).scale(1.5F).end().end())
				// Gui, ground, and fixed all use the normal "item model"
				.perspective(TransformType.GUI, guiModel).perspective(TransformType.GROUND, guiModel).perspective(TransformType.FIXED, guiModel);
	}
}
