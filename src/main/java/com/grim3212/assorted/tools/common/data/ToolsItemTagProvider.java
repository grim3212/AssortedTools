package com.grim3212.assorted.tools.common.data;

import java.util.concurrent.CompletableFuture;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.grim3212.assorted.tools.common.util.ToolsTags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ToolsItemTagProvider extends ItemTagsProvider {

	public ToolsItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, TagsProvider<Block> blockTags, ExistingFileHelper existingFileHelper) {
		super(output, lookup, blockTags, AssortedTools.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(Provider provider) {
		this.tag(ToolsTags.Items.FEATHERS).add(Items.FEATHER);
		this.tag(ToolsTags.Items.INGOTS_COPPER).add(Items.COPPER_INGOT);
		this.tag(ToolsTags.Items.GEMS_AMETHYST).add(Items.AMETHYST_SHARD);

		this.tag(ToolsTags.Items.BUCKETS_MILK).add(ToolsItems.WOOD_MILK_BUCKET.get(), ToolsItems.STONE_MILK_BUCKET.get(), ToolsItems.GOLD_MILK_BUCKET.get(), Items.MILK_BUCKET, ToolsItems.DIAMOND_MILK_BUCKET.get(), ToolsItems.NETHERITE_MILK_BUCKET.get());
		this.tag(Tags.Items.SHEARS).add(ToolsItems.WOOD_SHEARS.get(), ToolsItems.STONE_SHEARS.get(), ToolsItems.GOLD_SHEARS.get(), ToolsItems.DIAMOND_SHEARS.get(), ToolsItems.NETHERITE_SHEARS.get());

		ToolsItems.MATERIAL_GROUPS.forEach((s, group) -> {
			// Add to top level tags
			this.tag(ToolsTags.Items.SWORDS).add(group.SWORD.get());
			this.tag(ToolsTags.Items.PICKAXES).add(group.PICKAXE.get());
			this.tag(ToolsTags.Items.SHOVELS).add(group.SHOVEL.get());
			this.tag(ToolsTags.Items.AXES).add(group.AXE.get());
			this.tag(ToolsTags.Items.HOES).add(group.HOE.get());
			this.tag(ToolsTags.Items.HELMETS).add(group.HELMET.get());
			this.tag(ToolsTags.Items.CHESTPLATES).add(group.CHESTPLATE.get());
			this.tag(ToolsTags.Items.LEGGINGS).add(group.LEGGINGS.get());
			this.tag(ToolsTags.Items.BOOTS).add(group.BOOTS.get());
			this.tag(ToolsTags.Items.BUCKETS_MILK).add(group.MILK_BUCKET.get());
			this.tag(Tags.Items.SHEARS).add(group.SHEARS.get());

			// Add to specific material tags
			this.tag(ToolsTags.Items.forgeTag("swords/" + s)).add(group.SWORD.get());
			this.tag(ToolsTags.Items.forgeTag("pickaxes/" + s)).add(group.PICKAXE.get());
			this.tag(ToolsTags.Items.forgeTag("shovels/" + s)).add(group.SHOVEL.get());
			this.tag(ToolsTags.Items.forgeTag("axes/" + s)).add(group.AXE.get());
			this.tag(ToolsTags.Items.forgeTag("hoes/" + s)).add(group.HOE.get());
			this.tag(ToolsTags.Items.forgeTag("helmets/" + s)).add(group.HELMET.get());
			this.tag(ToolsTags.Items.forgeTag("chestplates/" + s)).add(group.CHESTPLATE.get());
			this.tag(ToolsTags.Items.forgeTag("leggings/" + s)).add(group.LEGGINGS.get());
			this.tag(ToolsTags.Items.forgeTag("boots/" + s)).add(group.BOOTS.get());
		});

		// Add in vanilla tools and armors
		this.tag(ToolsTags.Items.SWORDS).add(Items.WOODEN_SWORD, Items.STONE_SWORD, Items.GOLDEN_SWORD, Items.IRON_SWORD, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD);
		this.tag(ToolsTags.Items.SWORDS_WOODEN).add(Items.WOODEN_SWORD);
		this.tag(ToolsTags.Items.SWORDS_STONE).add(Items.STONE_SWORD);
		this.tag(ToolsTags.Items.SWORDS_GOLDEN).add(Items.GOLDEN_SWORD);
		this.tag(ToolsTags.Items.SWORDS_IRON).add(Items.IRON_SWORD);
		this.tag(ToolsTags.Items.SWORDS_DIAMOND).add(Items.DIAMOND_SWORD);
		this.tag(ToolsTags.Items.SWORDS_NETHERITE).add(Items.NETHERITE_SWORD);

		this.tag(ToolsTags.Items.PICKAXES).add(Items.WOODEN_PICKAXE, Items.STONE_PICKAXE, Items.GOLDEN_PICKAXE, Items.IRON_PICKAXE, Items.DIAMOND_PICKAXE, Items.NETHERITE_PICKAXE);
		this.tag(ToolsTags.Items.PICKAXES_WOODEN).add(Items.WOODEN_PICKAXE);
		this.tag(ToolsTags.Items.PICKAXES_STONE).add(Items.STONE_PICKAXE);
		this.tag(ToolsTags.Items.PICKAXES_GOLDEN).add(Items.GOLDEN_PICKAXE);
		this.tag(ToolsTags.Items.PICKAXES_IRON).add(Items.IRON_PICKAXE);
		this.tag(ToolsTags.Items.PICKAXES_DIAMOND).add(Items.DIAMOND_PICKAXE);
		this.tag(ToolsTags.Items.PICKAXES_NETHERITE).add(Items.NETHERITE_PICKAXE);

		this.tag(ToolsTags.Items.SHOVELS).add(Items.WOODEN_SHOVEL, Items.STONE_SHOVEL, Items.GOLDEN_SHOVEL, Items.IRON_SHOVEL, Items.DIAMOND_SHOVEL, Items.NETHERITE_SHOVEL);
		this.tag(ToolsTags.Items.SHOVELS_WOODEN).add(Items.WOODEN_SHOVEL);
		this.tag(ToolsTags.Items.SHOVELS_STONE).add(Items.STONE_SHOVEL);
		this.tag(ToolsTags.Items.SHOVELS_GOLDEN).add(Items.GOLDEN_SHOVEL);
		this.tag(ToolsTags.Items.SHOVELS_IRON).add(Items.IRON_SHOVEL);
		this.tag(ToolsTags.Items.SHOVELS_DIAMOND).add(Items.DIAMOND_SHOVEL);
		this.tag(ToolsTags.Items.SHOVELS_NETHERITE).add(Items.NETHERITE_SHOVEL);

		this.tag(ToolsTags.Items.AXES).add(Items.WOODEN_AXE, Items.STONE_AXE, Items.GOLDEN_AXE, Items.IRON_AXE, Items.DIAMOND_AXE, Items.NETHERITE_AXE);
		this.tag(ToolsTags.Items.AXES_WOODEN).add(Items.WOODEN_AXE);
		this.tag(ToolsTags.Items.AXES_STONE).add(Items.STONE_AXE);
		this.tag(ToolsTags.Items.AXES_GOLDEN).add(Items.GOLDEN_AXE);
		this.tag(ToolsTags.Items.AXES_IRON).add(Items.IRON_AXE);
		this.tag(ToolsTags.Items.AXES_DIAMOND).add(Items.DIAMOND_AXE);
		this.tag(ToolsTags.Items.AXES_NETHERITE).add(Items.NETHERITE_AXE);

		this.tag(ToolsTags.Items.HOES).add(Items.WOODEN_HOE, Items.STONE_HOE, Items.GOLDEN_HOE, Items.IRON_HOE, Items.DIAMOND_HOE, Items.NETHERITE_HOE);
		this.tag(ToolsTags.Items.HOES_WOODEN).add(Items.WOODEN_HOE);
		this.tag(ToolsTags.Items.HOES_STONE).add(Items.STONE_HOE);
		this.tag(ToolsTags.Items.HOES_GOLDEN).add(Items.GOLDEN_HOE);
		this.tag(ToolsTags.Items.HOES_IRON).add(Items.IRON_HOE);
		this.tag(ToolsTags.Items.HOES_DIAMOND).add(Items.DIAMOND_HOE);
		this.tag(ToolsTags.Items.HOES_NETHERITE).add(Items.NETHERITE_HOE);

		this.tag(ToolsTags.Items.HELMETS).add(Items.LEATHER_HELMET, Items.CHAINMAIL_HELMET, Items.GOLDEN_HELMET, Items.IRON_HELMET, Items.TURTLE_HELMET, Items.DIAMOND_HELMET, Items.NETHERITE_HELMET);
		this.tag(ToolsTags.Items.HELMETS_LEATHER).add(Items.LEATHER_HELMET);
		this.tag(ToolsTags.Items.HELMETS_CHAINMAIL).add(Items.CHAINMAIL_HELMET);
		this.tag(ToolsTags.Items.HELMETS_TURTLE).add(Items.TURTLE_HELMET);
		this.tag(ToolsTags.Items.HELMETS_GOLDEN).add(Items.GOLDEN_HELMET);
		this.tag(ToolsTags.Items.HELMETS_IRON).add(Items.IRON_HELMET);
		this.tag(ToolsTags.Items.HELMETS_DIAMOND).add(Items.DIAMOND_HELMET);
		this.tag(ToolsTags.Items.HELMETS_NETHERITE).add(Items.NETHERITE_HELMET);

		this.tag(ToolsTags.Items.CHESTPLATES).add(Items.LEATHER_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.IRON_CHESTPLATE, Items.DIAMOND_CHESTPLATE, Items.NETHERITE_CHESTPLATE);
		this.tag(ToolsTags.Items.CHESTPLATES_LEATHER).add(Items.LEATHER_CHESTPLATE);
		this.tag(ToolsTags.Items.CHESTPLATES_CHAINMAIL).add(Items.CHAINMAIL_CHESTPLATE);
		this.tag(ToolsTags.Items.CHESTPLATES_GOLDEN).add(Items.GOLDEN_CHESTPLATE);
		this.tag(ToolsTags.Items.CHESTPLATES_IRON).add(Items.IRON_CHESTPLATE);
		this.tag(ToolsTags.Items.CHESTPLATES_DIAMOND).add(Items.DIAMOND_CHESTPLATE);
		this.tag(ToolsTags.Items.CHESTPLATES_NETHERITE).add(Items.NETHERITE_CHESTPLATE);

		this.tag(ToolsTags.Items.LEGGINGS).add(Items.LEATHER_LEGGINGS, Items.CHAINMAIL_LEGGINGS, Items.GOLDEN_LEGGINGS, Items.IRON_LEGGINGS, Items.DIAMOND_LEGGINGS, Items.NETHERITE_LEGGINGS);
		this.tag(ToolsTags.Items.LEGGINGS_LEATHER).add(Items.LEATHER_LEGGINGS);
		this.tag(ToolsTags.Items.LEGGINGS_CHAINMAIL).add(Items.CHAINMAIL_LEGGINGS);
		this.tag(ToolsTags.Items.LEGGINGS_GOLDEN).add(Items.GOLDEN_LEGGINGS);
		this.tag(ToolsTags.Items.LEGGINGS_IRON).add(Items.IRON_LEGGINGS);
		this.tag(ToolsTags.Items.LEGGINGS_DIAMOND).add(Items.DIAMOND_LEGGINGS);
		this.tag(ToolsTags.Items.LEGGINGS_NETHERITE).add(Items.NETHERITE_LEGGINGS);

		this.tag(ToolsTags.Items.BOOTS).add(Items.LEATHER_BOOTS, Items.CHAINMAIL_BOOTS, Items.GOLDEN_BOOTS, Items.IRON_BOOTS, Items.DIAMOND_BOOTS, Items.NETHERITE_BOOTS);
		this.tag(ToolsTags.Items.BOOTS_LEATHER).add(Items.LEATHER_BOOTS);
		this.tag(ToolsTags.Items.BOOTS_CHAINMAIL).add(Items.CHAINMAIL_BOOTS);
		this.tag(ToolsTags.Items.BOOTS_GOLDEN).add(Items.GOLDEN_BOOTS);
		this.tag(ToolsTags.Items.BOOTS_IRON).add(Items.IRON_BOOTS);
		this.tag(ToolsTags.Items.BOOTS_DIAMOND).add(Items.DIAMOND_BOOTS);
		this.tag(ToolsTags.Items.BOOTS_NETHERITE).add(Items.NETHERITE_BOOTS);

		this.tag(ToolsTags.Items.ULTIMATE_FRAGMENTS).add(ToolsItems.U_FRAGMENT.get(), ToolsItems.L_FRAGMENT.get(), ToolsItems.T_FRAGMENT.get(), ToolsItems.I_FRAGMENT.get(), ToolsItems.M_FRAGMENT.get(), ToolsItems.A_FRAGMENT.get(), ToolsItems.MISSING_FRAGMENT.get(), ToolsItems.E_FRAGMENT.get());

		this.tag(ItemTags.PIGLIN_LOVED).add(ToolsItems.GOLD_HAMMER.get(), ToolsItems.GOLDEN_MULTITOOL.get(), ToolsItems.GOLD_SPEAR.get(), ToolsItems.BUILDING_WAND.get(), ToolsItems.REINFORCED_BUILDING_WAND.get(), ToolsItems.GOLD_BUCKET.get(), ToolsItems.GOLD_MILK_BUCKET.get(), ToolsItems.GOLD_SHEARS.get());
	}

	@Override
	public String getName() {
		return "Assorted Tools item tags";
	}
}
