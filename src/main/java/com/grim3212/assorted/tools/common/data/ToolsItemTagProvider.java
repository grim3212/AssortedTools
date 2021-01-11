package com.grim3212.assorted.tools.common.data;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.grim3212.assorted.tools.common.util.ToolsTags;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ToolsItemTagProvider extends ItemTagsProvider {

	public ToolsItemTagProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, ExistingFileHelper existingFileHelper) {
		super(dataGenerator, blockTagProvider, AssortedTools.MODID, existingFileHelper);
	}

	@Override
	protected void registerTags() {
		this.getOrCreateBuilder(ToolsTags.Items.FEATHERS).add(Items.FEATHER);

		ToolsItems.MATERIAL_GROUPS.forEach((s, group) -> {
			// Add to top level tags
			this.getOrCreateBuilder(ToolsTags.Items.SWORDS).add(group.SWORD.get());
			this.getOrCreateBuilder(ToolsTags.Items.PICKAXES).add(group.PICKAXE.get());
			this.getOrCreateBuilder(ToolsTags.Items.SHOVELS).add(group.SHOVEL.get());
			this.getOrCreateBuilder(ToolsTags.Items.AXES).add(group.AXE.get());
			this.getOrCreateBuilder(ToolsTags.Items.HOES).add(group.HOE.get());
			this.getOrCreateBuilder(ToolsTags.Items.HELMETS).add(group.HELMET.get());
			this.getOrCreateBuilder(ToolsTags.Items.CHESTPLATES).add(group.CHESTPLATE.get());
			this.getOrCreateBuilder(ToolsTags.Items.LEGGINGS).add(group.LEGGINGS.get());
			this.getOrCreateBuilder(ToolsTags.Items.BOOTS).add(group.BOOTS.get());

			// Add to specific material tags
			this.getOrCreateBuilder(ToolsTags.Items.forgeTag("swords/" + s)).add(group.SWORD.get());
			this.getOrCreateBuilder(ToolsTags.Items.forgeTag("pickaxes/" + s)).add(group.PICKAXE.get());
			this.getOrCreateBuilder(ToolsTags.Items.forgeTag("shovels/" + s)).add(group.SHOVEL.get());
			this.getOrCreateBuilder(ToolsTags.Items.forgeTag("axes/" + s)).add(group.AXE.get());
			this.getOrCreateBuilder(ToolsTags.Items.forgeTag("hoes/" + s)).add(group.HOE.get());
			this.getOrCreateBuilder(ToolsTags.Items.forgeTag("helmets/" + s)).add(group.HELMET.get());
			this.getOrCreateBuilder(ToolsTags.Items.forgeTag("chestplates/" + s)).add(group.CHESTPLATE.get());
			this.getOrCreateBuilder(ToolsTags.Items.forgeTag("leggings/" + s)).add(group.LEGGINGS.get());
			this.getOrCreateBuilder(ToolsTags.Items.forgeTag("boots/" + s)).add(group.BOOTS.get());
		});

		// Add in vanilla tools and armors
		this.getOrCreateBuilder(ToolsTags.Items.SWORDS).add(Items.WOODEN_SWORD, Items.STONE_SWORD, Items.GOLDEN_SWORD, Items.IRON_SWORD, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD);
		this.getOrCreateBuilder(ToolsTags.Items.SWORDS_WOODEN).add(Items.WOODEN_SWORD);
		this.getOrCreateBuilder(ToolsTags.Items.SWORDS_STONE).add(Items.STONE_SWORD);
		this.getOrCreateBuilder(ToolsTags.Items.SWORDS_GOLDEN).add(Items.GOLDEN_SWORD);
		this.getOrCreateBuilder(ToolsTags.Items.SWORDS_IRON).add(Items.IRON_SWORD);
		this.getOrCreateBuilder(ToolsTags.Items.SWORDS_DIAMOND).add(Items.DIAMOND_SWORD);
		this.getOrCreateBuilder(ToolsTags.Items.SWORDS_NETHERITE).add(Items.NETHERITE_SWORD);

		this.getOrCreateBuilder(ToolsTags.Items.PICKAXES).add(Items.WOODEN_PICKAXE, Items.STONE_PICKAXE, Items.GOLDEN_PICKAXE, Items.IRON_PICKAXE, Items.DIAMOND_PICKAXE, Items.NETHERITE_PICKAXE);
		this.getOrCreateBuilder(ToolsTags.Items.PICKAXES_WOODEN).add(Items.WOODEN_PICKAXE);
		this.getOrCreateBuilder(ToolsTags.Items.PICKAXES_STONE).add(Items.STONE_PICKAXE);
		this.getOrCreateBuilder(ToolsTags.Items.PICKAXES_GOLDEN).add(Items.GOLDEN_PICKAXE);
		this.getOrCreateBuilder(ToolsTags.Items.PICKAXES_IRON).add(Items.IRON_PICKAXE);
		this.getOrCreateBuilder(ToolsTags.Items.PICKAXES_DIAMOND).add(Items.DIAMOND_PICKAXE);
		this.getOrCreateBuilder(ToolsTags.Items.PICKAXES_NETHERITE).add(Items.NETHERITE_PICKAXE);

		this.getOrCreateBuilder(ToolsTags.Items.SHOVELS).add(Items.WOODEN_SHOVEL, Items.STONE_SHOVEL, Items.GOLDEN_SHOVEL, Items.IRON_SHOVEL, Items.DIAMOND_SHOVEL, Items.NETHERITE_SHOVEL);
		this.getOrCreateBuilder(ToolsTags.Items.SHOVELS_WOODEN).add(Items.WOODEN_SHOVEL);
		this.getOrCreateBuilder(ToolsTags.Items.SHOVELS_STONE).add(Items.STONE_SHOVEL);
		this.getOrCreateBuilder(ToolsTags.Items.SHOVELS_GOLDEN).add(Items.GOLDEN_SHOVEL);
		this.getOrCreateBuilder(ToolsTags.Items.SHOVELS_IRON).add(Items.IRON_SHOVEL);
		this.getOrCreateBuilder(ToolsTags.Items.SHOVELS_DIAMOND).add(Items.DIAMOND_SHOVEL);
		this.getOrCreateBuilder(ToolsTags.Items.SHOVELS_NETHERITE).add(Items.NETHERITE_SHOVEL);

		this.getOrCreateBuilder(ToolsTags.Items.AXES).add(Items.WOODEN_AXE, Items.STONE_AXE, Items.GOLDEN_AXE, Items.IRON_AXE, Items.DIAMOND_AXE, Items.NETHERITE_AXE);
		this.getOrCreateBuilder(ToolsTags.Items.AXES_WOODEN).add(Items.WOODEN_AXE);
		this.getOrCreateBuilder(ToolsTags.Items.AXES_STONE).add(Items.STONE_AXE);
		this.getOrCreateBuilder(ToolsTags.Items.AXES_GOLDEN).add(Items.GOLDEN_AXE);
		this.getOrCreateBuilder(ToolsTags.Items.AXES_IRON).add(Items.IRON_AXE);
		this.getOrCreateBuilder(ToolsTags.Items.AXES_DIAMOND).add(Items.DIAMOND_AXE);
		this.getOrCreateBuilder(ToolsTags.Items.AXES_NETHERITE).add(Items.NETHERITE_AXE);

		this.getOrCreateBuilder(ToolsTags.Items.HOES).add(Items.WOODEN_HOE, Items.STONE_HOE, Items.GOLDEN_HOE, Items.IRON_HOE, Items.DIAMOND_HOE, Items.NETHERITE_HOE);
		this.getOrCreateBuilder(ToolsTags.Items.HOES_WOODEN).add(Items.WOODEN_HOE);
		this.getOrCreateBuilder(ToolsTags.Items.HOES_STONE).add(Items.STONE_HOE);
		this.getOrCreateBuilder(ToolsTags.Items.HOES_GOLDEN).add(Items.GOLDEN_HOE);
		this.getOrCreateBuilder(ToolsTags.Items.HOES_IRON).add(Items.IRON_HOE);
		this.getOrCreateBuilder(ToolsTags.Items.HOES_DIAMOND).add(Items.DIAMOND_HOE);
		this.getOrCreateBuilder(ToolsTags.Items.HOES_NETHERITE).add(Items.NETHERITE_HOE);

		this.getOrCreateBuilder(ToolsTags.Items.HELMETS).add(Items.LEATHER_HELMET, Items.CHAINMAIL_HELMET, Items.GOLDEN_HELMET, Items.IRON_HELMET, Items.TURTLE_HELMET, Items.DIAMOND_HELMET, Items.NETHERITE_HELMET);
		this.getOrCreateBuilder(ToolsTags.Items.HELMETS_LEATHER).add(Items.LEATHER_HELMET);
		this.getOrCreateBuilder(ToolsTags.Items.HELMETS_CHAINMAIL).add(Items.CHAINMAIL_HELMET);
		this.getOrCreateBuilder(ToolsTags.Items.HELMETS_TURTLE).add(Items.TURTLE_HELMET);
		this.getOrCreateBuilder(ToolsTags.Items.HELMETS_GOLDEN).add(Items.GOLDEN_HELMET);
		this.getOrCreateBuilder(ToolsTags.Items.HELMETS_IRON).add(Items.IRON_HELMET);
		this.getOrCreateBuilder(ToolsTags.Items.HELMETS_DIAMOND).add(Items.DIAMOND_HELMET);
		this.getOrCreateBuilder(ToolsTags.Items.HELMETS_NETHERITE).add(Items.NETHERITE_HELMET);

		this.getOrCreateBuilder(ToolsTags.Items.CHESTPLATES).add(Items.LEATHER_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.IRON_CHESTPLATE, Items.DIAMOND_CHESTPLATE, Items.NETHERITE_CHESTPLATE);
		this.getOrCreateBuilder(ToolsTags.Items.CHESTPLATES_LEATHER).add(Items.LEATHER_CHESTPLATE);
		this.getOrCreateBuilder(ToolsTags.Items.CHESTPLATES_CHAINMAIL).add(Items.CHAINMAIL_CHESTPLATE);
		this.getOrCreateBuilder(ToolsTags.Items.CHESTPLATES_GOLDEN).add(Items.GOLDEN_CHESTPLATE);
		this.getOrCreateBuilder(ToolsTags.Items.CHESTPLATES_IRON).add(Items.IRON_CHESTPLATE);
		this.getOrCreateBuilder(ToolsTags.Items.CHESTPLATES_DIAMOND).add(Items.DIAMOND_CHESTPLATE);
		this.getOrCreateBuilder(ToolsTags.Items.CHESTPLATES_NETHERITE).add(Items.NETHERITE_CHESTPLATE);

		this.getOrCreateBuilder(ToolsTags.Items.LEGGINGS).add(Items.LEATHER_LEGGINGS, Items.CHAINMAIL_LEGGINGS, Items.GOLDEN_LEGGINGS, Items.IRON_LEGGINGS, Items.DIAMOND_LEGGINGS, Items.NETHERITE_LEGGINGS);
		this.getOrCreateBuilder(ToolsTags.Items.LEGGINGS_LEATHER).add(Items.LEATHER_LEGGINGS);
		this.getOrCreateBuilder(ToolsTags.Items.LEGGINGS_CHAINMAIL).add(Items.CHAINMAIL_LEGGINGS);
		this.getOrCreateBuilder(ToolsTags.Items.LEGGINGS_GOLDEN).add(Items.GOLDEN_LEGGINGS);
		this.getOrCreateBuilder(ToolsTags.Items.LEGGINGS_IRON).add(Items.IRON_LEGGINGS);
		this.getOrCreateBuilder(ToolsTags.Items.LEGGINGS_DIAMOND).add(Items.DIAMOND_LEGGINGS);
		this.getOrCreateBuilder(ToolsTags.Items.LEGGINGS_NETHERITE).add(Items.NETHERITE_LEGGINGS);

		this.getOrCreateBuilder(ToolsTags.Items.BOOTS).add(Items.LEATHER_BOOTS, Items.CHAINMAIL_BOOTS, Items.GOLDEN_BOOTS, Items.IRON_BOOTS, Items.DIAMOND_BOOTS, Items.NETHERITE_BOOTS);
		this.getOrCreateBuilder(ToolsTags.Items.BOOTS_LEATHER).add(Items.LEATHER_BOOTS);
		this.getOrCreateBuilder(ToolsTags.Items.BOOTS_CHAINMAIL).add(Items.CHAINMAIL_BOOTS);
		this.getOrCreateBuilder(ToolsTags.Items.BOOTS_GOLDEN).add(Items.GOLDEN_BOOTS);
		this.getOrCreateBuilder(ToolsTags.Items.BOOTS_IRON).add(Items.IRON_BOOTS);
		this.getOrCreateBuilder(ToolsTags.Items.BOOTS_DIAMOND).add(Items.DIAMOND_BOOTS);
		this.getOrCreateBuilder(ToolsTags.Items.BOOTS_NETHERITE).add(Items.NETHERITE_BOOTS);

		this.getOrCreateBuilder(ItemTags.PIGLIN_LOVED).add(ToolsItems.GOLD_HAMMER.get(), ToolsItems.GOLDEN_MULTITOOL.get(), ToolsItems.BUILDING_WAND.get(), ToolsItems.REINFORCED_BUILDING_WAND.get());
	}

	@Override
	public String getName() {
		return "Assorted Tools item tags";
	}
}
