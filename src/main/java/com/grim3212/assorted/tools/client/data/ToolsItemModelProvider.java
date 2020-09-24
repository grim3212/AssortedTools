package com.grim3212.assorted.tools.client.data;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.item.ToolsItems;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
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
		generatedItem(ToolsItems.WOOD_HAMMER.get());
		generatedItem(ToolsItems.STONE_HAMMER.get());
		generatedItem(ToolsItems.GOLD_HAMMER.get());
		generatedItem(ToolsItems.IRON_HAMMER.get());
		generatedItem(ToolsItems.DIAMOND_HAMMER.get());
		generatedItem(ToolsItems.NETHERITE_HAMMER.get());
		generatedItem(ToolsItems.WOOD_BOOMERANG.get());
		generatedItem(ToolsItems.DIAMOND_BOOMERANG.get());

		generatedItem(ToolsItems.BUILDING_WAND.get());
		generatedItem(ToolsItems.REINFORCED_BUILDING_WAND.get());
		generatedItem(ToolsItems.BREAKING_WAND.get());
		generatedItem(ToolsItems.REINFORCED_BREAKING_WAND.get());
		generatedItem(ToolsItems.MINING_WAND.get());
		generatedItem(ToolsItems.REINFORCED_MINING_WAND.get());
	}

	private ItemModelBuilder generatedItem(String name) {
		return withExistingParent(name, "item/generated").texture("layer0", prefix("item/" + name));
	}

	private ItemModelBuilder generatedItem(Item i) {
		return generatedItem(name(i));
	}

	private static String name(Item i) {
		return Registry.ITEM.getKey(i).getPath();
	}

	private ResourceLocation prefix(String name) {
		return new ResourceLocation(AssortedTools.MODID, name);
	}
}
