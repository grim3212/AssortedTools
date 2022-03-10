package com.grim3212.assorted.tools.common.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ToolsTags {

	public static class Blocks {
		public static final TagKey<Block> MINEABLE_MULTITOOL = forgeTag("mineable/multitool");
		
		public static final TagKey<Block> ALL_CORALS = forgeTag("corals/all");
		public static final TagKey<Block> DEAD_CORALS = forgeTag("corals/dead");

		public static TagKey<Block> forgeTag(String name) {
			return BlockTags.create(new ResourceLocation("forge", name));
		}
	}

	public static class Items {
		public static final TagKey<Item> FEATHERS = forgeTag("feathers");
		
		public static final TagKey<Item> BUCKETS_MILK = forgeTag("buckets/milk");

		public static final TagKey<Item> INGOTS_TIN = forgeTag("ingots/tin");
		public static final TagKey<Item> INGOTS_COPPER = forgeTag("ingots/copper");
		public static final TagKey<Item> INGOTS_SILVER = forgeTag("ingots/silver");
		public static final TagKey<Item> INGOTS_ALUMINUM = forgeTag("ingots/aluminum");
		public static final TagKey<Item> INGOTS_NICKEL = forgeTag("ingots/nickel");
		public static final TagKey<Item> INGOTS_PLATINUM = forgeTag("ingots/platinum");
		public static final TagKey<Item> INGOTS_LEAD = forgeTag("ingots/lead");
		public static final TagKey<Item> INGOTS_BRONZE = forgeTag("ingots/bronze");
		public static final TagKey<Item> INGOTS_ELECTRUM = forgeTag("ingots/electrum");
		public static final TagKey<Item> INGOTS_INVAR = forgeTag("ingots/invar");
		public static final TagKey<Item> INGOTS_STEEL = forgeTag("ingots/steel");
		public static final TagKey<Item> GEMS_RUBY = forgeTag("gems/ruby");
		public static final TagKey<Item> GEMS_AMETHYST = forgeTag("gems/amethyst");
		public static final TagKey<Item> GEMS_SAPPHIRE = forgeTag("gems/sapphire");
		public static final TagKey<Item> GEMS_TOPAZ = forgeTag("gems/topaz");
		public static final TagKey<Item> GEMS_PERIDOT = forgeTag("gems/peridot");

		public static final TagKey<Item> SWORDS = forgeTag("swords");
		public static final TagKey<Item> SWORDS_TIN = forgeTag("swords/tin");
		public static final TagKey<Item> SWORDS_COPPER = forgeTag("swords/copper");
		public static final TagKey<Item> SWORDS_SILVER = forgeTag("swords/silver");
		public static final TagKey<Item> SWORDS_ALUMINUM = forgeTag("swords/aluminum");
		public static final TagKey<Item> SWORDS_NICKEL = forgeTag("swords/nickel");
		public static final TagKey<Item> SWORDS_PLATINUM = forgeTag("swords/platinum");
		public static final TagKey<Item> SWORDS_LEAD = forgeTag("swords/lead");
		public static final TagKey<Item> SWORDS_BRONZE = forgeTag("swords/bronze");
		public static final TagKey<Item> SWORDS_ELECTRUM = forgeTag("swords/electrum");
		public static final TagKey<Item> SWORDS_INVAR = forgeTag("swords/invar");
		public static final TagKey<Item> SWORDS_STEEL = forgeTag("swords/steel");
		public static final TagKey<Item> SWORDS_RUBY = forgeTag("swords/ruby");
		public static final TagKey<Item> SWORDS_AMETHYST = forgeTag("swords/amethyst");
		public static final TagKey<Item> SWORDS_SAPPHIRE = forgeTag("swords/sapphire");
		public static final TagKey<Item> SWORDS_TOPAZ = forgeTag("swords/topaz");
		public static final TagKey<Item> SWORDS_EMERALD = forgeTag("swords/emerald");
		public static final TagKey<Item> SWORDS_PERIDOT = forgeTag("swords/peridot");
		public static final TagKey<Item> SWORDS_WOODEN = forgeTag("swords/wooden");
		public static final TagKey<Item> SWORDS_STONE = forgeTag("swords/stone");
		public static final TagKey<Item> SWORDS_GOLDEN = forgeTag("swords/golden");
		public static final TagKey<Item> SWORDS_IRON = forgeTag("swords/iron");
		public static final TagKey<Item> SWORDS_DIAMOND = forgeTag("swords/diamond");
		public static final TagKey<Item> SWORDS_NETHERITE = forgeTag("swords/netherite");

		public static final TagKey<Item> PICKAXES = forgeTag("pickaxes");
		public static final TagKey<Item> PICKAXES_TIN = forgeTag("pickaxes/tin");
		public static final TagKey<Item> PICKAXES_COPPER = forgeTag("pickaxes/copper");
		public static final TagKey<Item> PICKAXES_SILVER = forgeTag("pickaxes/silver");
		public static final TagKey<Item> PICKAXES_ALUMINUM = forgeTag("pickaxes/aluminum");
		public static final TagKey<Item> PICKAXES_NICKEL = forgeTag("pickaxes/nickel");
		public static final TagKey<Item> PICKAXES_PLATINUM = forgeTag("pickaxes/platinum");
		public static final TagKey<Item> PICKAXES_LEAD = forgeTag("pickaxes/lead");
		public static final TagKey<Item> PICKAXES_BRONZE = forgeTag("pickaxes/bronze");
		public static final TagKey<Item> PICKAXES_ELECTRUM = forgeTag("pickaxes/electrum");
		public static final TagKey<Item> PICKAXES_INVAR = forgeTag("pickaxes/invar");
		public static final TagKey<Item> PICKAXES_STEEL = forgeTag("pickaxes/steel");
		public static final TagKey<Item> PICKAXES_RUBY = forgeTag("pickaxes/ruby");
		public static final TagKey<Item> PICKAXES_AMETHYST = forgeTag("pickaxes/amethyst");
		public static final TagKey<Item> PICKAXES_SAPPHIRE = forgeTag("pickaxes/sapphire");
		public static final TagKey<Item> PICKAXES_TOPAZ = forgeTag("pickaxes/topaz");
		public static final TagKey<Item> PICKAXES_EMERALD = forgeTag("pickaxes/emerald");
		public static final TagKey<Item> PICKAXES_PERIDOT = forgeTag("pickaxes/peridot");
		public static final TagKey<Item> PICKAXES_WOODEN = forgeTag("pickaxes/wooden");
		public static final TagKey<Item> PICKAXES_STONE = forgeTag("pickaxes/stone");
		public static final TagKey<Item> PICKAXES_GOLDEN = forgeTag("pickaxes/golden");
		public static final TagKey<Item> PICKAXES_IRON = forgeTag("pickaxes/iron");
		public static final TagKey<Item> PICKAXES_DIAMOND = forgeTag("pickaxes/diamond");
		public static final TagKey<Item> PICKAXES_NETHERITE = forgeTag("pickaxes/netherite");

		public static final TagKey<Item> SHOVELS = forgeTag("shovels");
		public static final TagKey<Item> SHOVELS_TIN = forgeTag("shovels/tin");
		public static final TagKey<Item> SHOVELS_COPPER = forgeTag("shovels/copper");
		public static final TagKey<Item> SHOVELS_SILVER = forgeTag("shovels/silver");
		public static final TagKey<Item> SHOVELS_ALUMINUM = forgeTag("shovels/aluminum");
		public static final TagKey<Item> SHOVELS_NICKEL = forgeTag("shovels/nickel");
		public static final TagKey<Item> SHOVELS_PLATINUM = forgeTag("shovels/platinum");
		public static final TagKey<Item> SHOVELS_LEAD = forgeTag("shovels/lead");
		public static final TagKey<Item> SHOVELS_BRONZE = forgeTag("shovels/bronze");
		public static final TagKey<Item> SHOVELS_ELECTRUM = forgeTag("shovels/electrum");
		public static final TagKey<Item> SHOVELS_INVAR = forgeTag("shovels/invar");
		public static final TagKey<Item> SHOVELS_STEEL = forgeTag("shovels/steel");
		public static final TagKey<Item> SHOVELS_RUBY = forgeTag("shovels/ruby");
		public static final TagKey<Item> SHOVELS_AMETHYST = forgeTag("shovels/amethyst");
		public static final TagKey<Item> SHOVELS_SAPPHIRE = forgeTag("shovels/sapphire");
		public static final TagKey<Item> SHOVELS_TOPAZ = forgeTag("shovels/topaz");
		public static final TagKey<Item> SHOVELS_EMERALD = forgeTag("shovels/emerald");
		public static final TagKey<Item> SHOVELS_PERIDOT = forgeTag("shovels/peridot");
		public static final TagKey<Item> SHOVELS_WOODEN = forgeTag("shovels/wooden");
		public static final TagKey<Item> SHOVELS_STONE = forgeTag("shovels/stone");
		public static final TagKey<Item> SHOVELS_GOLDEN = forgeTag("shovels/golden");
		public static final TagKey<Item> SHOVELS_IRON = forgeTag("shovels/iron");
		public static final TagKey<Item> SHOVELS_DIAMOND = forgeTag("shovels/diamond");
		public static final TagKey<Item> SHOVELS_NETHERITE = forgeTag("shovels/netherite");

		public static final TagKey<Item> AXES = forgeTag("axes");
		public static final TagKey<Item> AXES_TIN = forgeTag("axes/tin");
		public static final TagKey<Item> AXES_COPPER = forgeTag("axes/copper");
		public static final TagKey<Item> AXES_SILVER = forgeTag("axes/silver");
		public static final TagKey<Item> AXES_ALUMINUM = forgeTag("axes/aluminum");
		public static final TagKey<Item> AXES_NICKEL = forgeTag("axes/nickel");
		public static final TagKey<Item> AXES_PLATINUM = forgeTag("axes/platinum");
		public static final TagKey<Item> AXES_LEAD = forgeTag("axes/lead");
		public static final TagKey<Item> AXES_BRONZE = forgeTag("axes/bronze");
		public static final TagKey<Item> AXES_ELECTRUM = forgeTag("axes/electrum");
		public static final TagKey<Item> AXES_INVAR = forgeTag("axes/invar");
		public static final TagKey<Item> AXES_STEEL = forgeTag("axes/steel");
		public static final TagKey<Item> AXES_RUBY = forgeTag("axes/ruby");
		public static final TagKey<Item> AXES_AMETHYST = forgeTag("axes/amethyst");
		public static final TagKey<Item> AXES_SAPPHIRE = forgeTag("axes/sapphire");
		public static final TagKey<Item> AXES_TOPAZ = forgeTag("axes/topaz");
		public static final TagKey<Item> AXES_EMERALD = forgeTag("axes/emerald");
		public static final TagKey<Item> AXES_PERIDOT = forgeTag("axes/peridot");
		public static final TagKey<Item> AXES_WOODEN = forgeTag("axes/wooden");
		public static final TagKey<Item> AXES_STONE = forgeTag("axes/stone");
		public static final TagKey<Item> AXES_GOLDEN = forgeTag("axes/golden");
		public static final TagKey<Item> AXES_IRON = forgeTag("axes/iron");
		public static final TagKey<Item> AXES_DIAMOND = forgeTag("axes/diamond");
		public static final TagKey<Item> AXES_NETHERITE = forgeTag("axes/netherite");

		public static final TagKey<Item> HOES = forgeTag("hoes");
		public static final TagKey<Item> HOES_TIN = forgeTag("hoes/tin");
		public static final TagKey<Item> HOES_COPPER = forgeTag("hoes/copper");
		public static final TagKey<Item> HOES_SILVER = forgeTag("hoes/silver");
		public static final TagKey<Item> HOES_ALUMINUM = forgeTag("hoes/aluminum");
		public static final TagKey<Item> HOES_NICKEL = forgeTag("hoes/nickel");
		public static final TagKey<Item> HOES_PLATINUM = forgeTag("hoes/platinum");
		public static final TagKey<Item> HOES_LEAD = forgeTag("hoes/lead");
		public static final TagKey<Item> HOES_BRONZE = forgeTag("hoes/bronze");
		public static final TagKey<Item> HOES_ELECTRUM = forgeTag("hoes/electrum");
		public static final TagKey<Item> HOES_INVAR = forgeTag("hoes/invar");
		public static final TagKey<Item> HOES_STEEL = forgeTag("hoes/steel");
		public static final TagKey<Item> HOES_RUBY = forgeTag("hoes/ruby");
		public static final TagKey<Item> HOES_AMETHYST = forgeTag("hoes/amethyst");
		public static final TagKey<Item> HOES_SAPPHIRE = forgeTag("hoes/sapphire");
		public static final TagKey<Item> HOES_TOPAZ = forgeTag("hoes/topaz");
		public static final TagKey<Item> HOES_EMERALD = forgeTag("hoes/emerald");
		public static final TagKey<Item> HOES_PERIDOT = forgeTag("hoes/peridot");
		public static final TagKey<Item> HOES_WOODEN = forgeTag("hoes/wooden");
		public static final TagKey<Item> HOES_STONE = forgeTag("hoes/stone");
		public static final TagKey<Item> HOES_GOLDEN = forgeTag("hoes/golden");
		public static final TagKey<Item> HOES_IRON = forgeTag("hoes/iron");
		public static final TagKey<Item> HOES_DIAMOND = forgeTag("hoes/diamond");
		public static final TagKey<Item> HOES_NETHERITE = forgeTag("hoes/netherite");

		public static final TagKey<Item> HELMETS = forgeTag("helmets");
		public static final TagKey<Item> HELMETS_TIN = forgeTag("helmets/tin");
		public static final TagKey<Item> HELMETS_COPPER = forgeTag("helmets/copper");
		public static final TagKey<Item> HELMETS_SILVER = forgeTag("helmets/silver");
		public static final TagKey<Item> HELMETS_ALUMINUM = forgeTag("helmets/aluminum");
		public static final TagKey<Item> HELMETS_NICKEL = forgeTag("helmets/nickel");
		public static final TagKey<Item> HELMETS_PLATINUM = forgeTag("helmets/platinum");
		public static final TagKey<Item> HELMETS_LEAD = forgeTag("helmets/lead");
		public static final TagKey<Item> HELMETS_BRONZE = forgeTag("helmets/bronze");
		public static final TagKey<Item> HELMETS_ELECTRUM = forgeTag("helmets/electrum");
		public static final TagKey<Item> HELMETS_INVAR = forgeTag("helmets/invar");
		public static final TagKey<Item> HELMETS_STEEL = forgeTag("helmets/steel");
		public static final TagKey<Item> HELMETS_RUBY = forgeTag("helmets/ruby");
		public static final TagKey<Item> HELMETS_AMETHYST = forgeTag("helmets/amethyst");
		public static final TagKey<Item> HELMETS_SAPPHIRE = forgeTag("helmets/sapphire");
		public static final TagKey<Item> HELMETS_TOPAZ = forgeTag("helmets/topaz");
		public static final TagKey<Item> HELMETS_EMERALD = forgeTag("helmets/emerald");
		public static final TagKey<Item> HELMETS_PERIDOT = forgeTag("helmets/peridot");
		public static final TagKey<Item> HELMETS_LEATHER = forgeTag("helmets/leather");
		public static final TagKey<Item> HELMETS_CHAINMAIL = forgeTag("helmets/chainmail");
		public static final TagKey<Item> HELMETS_GOLDEN = forgeTag("helmets/golden");
		public static final TagKey<Item> HELMETS_IRON = forgeTag("helmets/iron");
		public static final TagKey<Item> HELMETS_DIAMOND = forgeTag("helmets/diamond");
		public static final TagKey<Item> HELMETS_NETHERITE = forgeTag("helmets/netherite");
		public static final TagKey<Item> HELMETS_TURTLE = forgeTag("helmets/turtle");

		public static final TagKey<Item> CHESTPLATES = forgeTag("chestplates");
		public static final TagKey<Item> CHESTPLATES_TIN = forgeTag("chestplates/tin");
		public static final TagKey<Item> CHESTPLATES_COPPER = forgeTag("chestplates/copper");
		public static final TagKey<Item> CHESTPLATES_SILVER = forgeTag("chestplates/silver");
		public static final TagKey<Item> CHESTPLATES_ALUMINUM = forgeTag("chestplates/aluminum");
		public static final TagKey<Item> CHESTPLATES_NICKEL = forgeTag("chestplates/nickel");
		public static final TagKey<Item> CHESTPLATES_PLATINUM = forgeTag("chestplates/platinum");
		public static final TagKey<Item> CHESTPLATES_LEAD = forgeTag("chestplates/lead");
		public static final TagKey<Item> CHESTPLATES_BRONZE = forgeTag("chestplates/bronze");
		public static final TagKey<Item> CHESTPLATES_ELECTRUM = forgeTag("chestplates/electrum");
		public static final TagKey<Item> CHESTPLATES_INVAR = forgeTag("chestplates/invar");
		public static final TagKey<Item> CHESTPLATES_STEEL = forgeTag("chestplates/steel");
		public static final TagKey<Item> CHESTPLATES_RUBY = forgeTag("chestplates/ruby");
		public static final TagKey<Item> CHESTPLATES_AMETHYST = forgeTag("chestplates/amethyst");
		public static final TagKey<Item> CHESTPLATES_SAPPHIRE = forgeTag("chestplates/sapphire");
		public static final TagKey<Item> CHESTPLATES_TOPAZ = forgeTag("chestplates/topaz");
		public static final TagKey<Item> CHESTPLATES_EMERALD = forgeTag("chestplates/emerald");
		public static final TagKey<Item> CHESTPLATES_PERIDOT = forgeTag("chestplates/peridot");
		public static final TagKey<Item> CHESTPLATES_LEATHER = forgeTag("chestplates/leather");
		public static final TagKey<Item> CHESTPLATES_CHAINMAIL = forgeTag("chestplates/chainmail");
		public static final TagKey<Item> CHESTPLATES_GOLDEN = forgeTag("chestplates/golden");
		public static final TagKey<Item> CHESTPLATES_IRON = forgeTag("chestplates/iron");
		public static final TagKey<Item> CHESTPLATES_DIAMOND = forgeTag("chestplates/diamond");
		public static final TagKey<Item> CHESTPLATES_NETHERITE = forgeTag("chestplates/netherite");

		public static final TagKey<Item> LEGGINGS = forgeTag("leggings");
		public static final TagKey<Item> LEGGINGS_TIN = forgeTag("leggings/tin");
		public static final TagKey<Item> LEGGINGS_COPPER = forgeTag("leggings/copper");
		public static final TagKey<Item> LEGGINGS_SILVER = forgeTag("leggings/silver");
		public static final TagKey<Item> LEGGINGS_ALUMINUM = forgeTag("leggings/aluminum");
		public static final TagKey<Item> LEGGINGS_NICKEL = forgeTag("leggings/nickel");
		public static final TagKey<Item> LEGGINGS_PLATINUM = forgeTag("leggings/platinum");
		public static final TagKey<Item> LEGGINGS_LEAD = forgeTag("leggings/lead");
		public static final TagKey<Item> LEGGINGS_BRONZE = forgeTag("leggings/bronze");
		public static final TagKey<Item> LEGGINGS_ELECTRUM = forgeTag("leggings/electrum");
		public static final TagKey<Item> LEGGINGS_INVAR = forgeTag("leggings/invar");
		public static final TagKey<Item> LEGGINGS_STEEL = forgeTag("leggings/steel");
		public static final TagKey<Item> LEGGINGS_RUBY = forgeTag("leggings/ruby");
		public static final TagKey<Item> LEGGINGS_AMETHYST = forgeTag("leggings/amethyst");
		public static final TagKey<Item> LEGGINGS_SAPPHIRE = forgeTag("leggings/sapphire");
		public static final TagKey<Item> LEGGINGS_TOPAZ = forgeTag("leggings/topaz");
		public static final TagKey<Item> LEGGINGS_EMERALD = forgeTag("leggings/emerald");
		public static final TagKey<Item> LEGGINGS_PERIDOT = forgeTag("leggings/peridot");
		public static final TagKey<Item> LEGGINGS_LEATHER = forgeTag("leggings/leather");
		public static final TagKey<Item> LEGGINGS_CHAINMAIL = forgeTag("leggings/chainmail");
		public static final TagKey<Item> LEGGINGS_GOLDEN = forgeTag("leggings/golden");
		public static final TagKey<Item> LEGGINGS_IRON = forgeTag("leggings/iron");
		public static final TagKey<Item> LEGGINGS_DIAMOND = forgeTag("leggings/diamond");
		public static final TagKey<Item> LEGGINGS_NETHERITE = forgeTag("leggings/netherite");

		public static final TagKey<Item> BOOTS = forgeTag("boots");
		public static final TagKey<Item> BOOTS_TIN = forgeTag("boots/tin");
		public static final TagKey<Item> BOOTS_COPPER = forgeTag("boots/copper");
		public static final TagKey<Item> BOOTS_SILVER = forgeTag("boots/silver");
		public static final TagKey<Item> BOOTS_ALUMINUM = forgeTag("boots/aluminum");
		public static final TagKey<Item> BOOTS_NICKEL = forgeTag("boots/nickel");
		public static final TagKey<Item> BOOTS_PLATINUM = forgeTag("boots/platinum");
		public static final TagKey<Item> BOOTS_LEAD = forgeTag("boots/lead");
		public static final TagKey<Item> BOOTS_BRONZE = forgeTag("boots/bronze");
		public static final TagKey<Item> BOOTS_ELECTRUM = forgeTag("boots/electrum");
		public static final TagKey<Item> BOOTS_INVAR = forgeTag("boots/invar");
		public static final TagKey<Item> BOOTS_STEEL = forgeTag("boots/steel");
		public static final TagKey<Item> BOOTS_RUBY = forgeTag("boots/ruby");
		public static final TagKey<Item> BOOTS_AMETHYST = forgeTag("boots/amethyst");
		public static final TagKey<Item> BOOTS_SAPPHIRE = forgeTag("boots/sapphire");
		public static final TagKey<Item> BOOTS_TOPAZ = forgeTag("boots/topaz");
		public static final TagKey<Item> BOOTS_EMERALD = forgeTag("boots/emerald");
		public static final TagKey<Item> BOOTS_PERIDOT = forgeTag("boots/peridot");
		public static final TagKey<Item> BOOTS_LEATHER = forgeTag("boots/leather");
		public static final TagKey<Item> BOOTS_CHAINMAIL = forgeTag("boots/chainmail");
		public static final TagKey<Item> BOOTS_GOLDEN = forgeTag("boots/golden");
		public static final TagKey<Item> BOOTS_IRON = forgeTag("boots/iron");
		public static final TagKey<Item> BOOTS_DIAMOND = forgeTag("boots/diamond");
		public static final TagKey<Item> BOOTS_NETHERITE = forgeTag("boots/netherite");

		public static TagKey<Item> forgeTag(String name) {
			return ItemTags.create(new ResourceLocation("forge", name));
		}
	}

}
