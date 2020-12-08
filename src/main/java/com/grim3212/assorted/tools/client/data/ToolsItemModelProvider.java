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
		tool(ToolsItems.WOOD_HAMMER.get());
		tool(ToolsItems.STONE_HAMMER.get());
		tool(ToolsItems.GOLD_HAMMER.get());
		tool(ToolsItems.IRON_HAMMER.get());
		tool(ToolsItems.DIAMOND_HAMMER.get());
		tool(ToolsItems.NETHERITE_HAMMER.get());
		generatedItem(ToolsItems.WOOD_BOOMERANG.get());
		generatedItem(ToolsItems.DIAMOND_BOOMERANG.get());
		generatedItem(ToolsItems.POKEBALL.get());

		generatedItem(ToolsItems.BUILDING_WAND.get());
		generatedItem(ToolsItems.REINFORCED_BUILDING_WAND.get());
		generatedItem(ToolsItems.BREAKING_WAND.get());
		generatedItem(ToolsItems.REINFORCED_BREAKING_WAND.get());
		generatedItem(ToolsItems.MINING_WAND.get());
		generatedItem(ToolsItems.REINFORCED_MINING_WAND.get());

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

		ToolsItems.MATERIAL_GROUPS.forEach((s, group) -> {
			tool(group.PICKAXE.get());
			tool(group.SHOVEL.get());
			tool(group.AXE.get());
			tool(group.HOE.get());
			tool(group.SWORD.get());
			tool(group.HAMMER.get());
			tool(group.MULTITOOL.get());

			armor(group.HELMET.get());
			armor(group.CHESTPLATE.get());
			armor(group.LEGGINGS.get());
			armor(group.BOOTS.get());
		});
	}

	private ItemModelBuilder tool(Item i) {
		String name = name(i);
		return withExistingParent(name, "item/generated").texture("layer0", prefix("item/tools/" + name));
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

	private static String name(Item i) {
		return Registry.ITEM.getKey(i).getPath();
	}

	private ResourceLocation prefix(String name) {
		return new ResourceLocation(AssortedTools.MODID, name);
	}
}
