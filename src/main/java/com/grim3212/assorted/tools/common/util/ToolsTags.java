package com.grim3212.assorted.tools.common.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

public class ToolsTags {

	public static class Blocks {
		public static final IOptionalNamedTag<Block> MINEABLE_MULTITOOL = forgeTag("mineable/multitool");

		public static IOptionalNamedTag<Block> forgeTag(String name) {
			return BlockTags.createOptional(new ResourceLocation("forge", name));
		}
	}

	public static class Items {
		public static final IOptionalNamedTag<Item> FEATHERS = forgeTag("feathers");

		public static final IOptionalNamedTag<Item> INGOTS_TIN = forgeTag("ingots/tin");
		public static final IOptionalNamedTag<Item> INGOTS_COPPER = forgeTag("ingots/copper");
		public static final IOptionalNamedTag<Item> INGOTS_SILVER = forgeTag("ingots/silver");
		public static final IOptionalNamedTag<Item> INGOTS_ALUMINUM = forgeTag("ingots/aluminum");
		public static final IOptionalNamedTag<Item> INGOTS_NICKEL = forgeTag("ingots/nickel");
		public static final IOptionalNamedTag<Item> INGOTS_PLATINUM = forgeTag("ingots/platinum");
		public static final IOptionalNamedTag<Item> INGOTS_LEAD = forgeTag("ingots/lead");
		public static final IOptionalNamedTag<Item> INGOTS_BRONZE = forgeTag("ingots/bronze");
		public static final IOptionalNamedTag<Item> INGOTS_ELECTRUM = forgeTag("ingots/electrum");
		public static final IOptionalNamedTag<Item> INGOTS_INVAR = forgeTag("ingots/invar");
		public static final IOptionalNamedTag<Item> INGOTS_STEEL = forgeTag("ingots/steel");
		public static final IOptionalNamedTag<Item> GEMS_RUBY = forgeTag("gems/ruby");
		public static final IOptionalNamedTag<Item> GEMS_AMETHYST = forgeTag("gems/amethyst");
		public static final IOptionalNamedTag<Item> GEMS_SAPPHIRE = forgeTag("gems/sapphire");
		public static final IOptionalNamedTag<Item> GEMS_TOPAZ = forgeTag("gems/topaz");
		public static final IOptionalNamedTag<Item> GEMS_PERIDOT = forgeTag("gems/peridot");

		public static final IOptionalNamedTag<Item> SWORDS = forgeTag("swords");
		public static final IOptionalNamedTag<Item> SWORDS_TIN = forgeTag("swords/tin");
		public static final IOptionalNamedTag<Item> SWORDS_COPPER = forgeTag("swords/copper");
		public static final IOptionalNamedTag<Item> SWORDS_SILVER = forgeTag("swords/silver");
		public static final IOptionalNamedTag<Item> SWORDS_ALUMINUM = forgeTag("swords/aluminum");
		public static final IOptionalNamedTag<Item> SWORDS_NICKEL = forgeTag("swords/nickel");
		public static final IOptionalNamedTag<Item> SWORDS_PLATINUM = forgeTag("swords/platinum");
		public static final IOptionalNamedTag<Item> SWORDS_LEAD = forgeTag("swords/lead");
		public static final IOptionalNamedTag<Item> SWORDS_BRONZE = forgeTag("swords/bronze");
		public static final IOptionalNamedTag<Item> SWORDS_ELECTRUM = forgeTag("swords/electrum");
		public static final IOptionalNamedTag<Item> SWORDS_INVAR = forgeTag("swords/invar");
		public static final IOptionalNamedTag<Item> SWORDS_STEEL = forgeTag("swords/steel");
		public static final IOptionalNamedTag<Item> SWORDS_RUBY = forgeTag("swords/ruby");
		public static final IOptionalNamedTag<Item> SWORDS_AMETHYST = forgeTag("swords/amethyst");
		public static final IOptionalNamedTag<Item> SWORDS_SAPPHIRE = forgeTag("swords/sapphire");
		public static final IOptionalNamedTag<Item> SWORDS_TOPAZ = forgeTag("swords/topaz");
		public static final IOptionalNamedTag<Item> SWORDS_EMERALD = forgeTag("swords/emerald");
		public static final IOptionalNamedTag<Item> SWORDS_PERIDOT = forgeTag("swords/peridot");
		public static final IOptionalNamedTag<Item> SWORDS_WOODEN = forgeTag("swords/wooden");
		public static final IOptionalNamedTag<Item> SWORDS_STONE = forgeTag("swords/stone");
		public static final IOptionalNamedTag<Item> SWORDS_GOLDEN = forgeTag("swords/golden");
		public static final IOptionalNamedTag<Item> SWORDS_IRON = forgeTag("swords/iron");
		public static final IOptionalNamedTag<Item> SWORDS_DIAMOND = forgeTag("swords/diamond");
		public static final IOptionalNamedTag<Item> SWORDS_NETHERITE = forgeTag("swords/netherite");

		public static final IOptionalNamedTag<Item> PICKAXES = forgeTag("pickaxes");
		public static final IOptionalNamedTag<Item> PICKAXES_TIN = forgeTag("pickaxes/tin");
		public static final IOptionalNamedTag<Item> PICKAXES_COPPER = forgeTag("pickaxes/copper");
		public static final IOptionalNamedTag<Item> PICKAXES_SILVER = forgeTag("pickaxes/silver");
		public static final IOptionalNamedTag<Item> PICKAXES_ALUMINUM = forgeTag("pickaxes/aluminum");
		public static final IOptionalNamedTag<Item> PICKAXES_NICKEL = forgeTag("pickaxes/nickel");
		public static final IOptionalNamedTag<Item> PICKAXES_PLATINUM = forgeTag("pickaxes/platinum");
		public static final IOptionalNamedTag<Item> PICKAXES_LEAD = forgeTag("pickaxes/lead");
		public static final IOptionalNamedTag<Item> PICKAXES_BRONZE = forgeTag("pickaxes/bronze");
		public static final IOptionalNamedTag<Item> PICKAXES_ELECTRUM = forgeTag("pickaxes/electrum");
		public static final IOptionalNamedTag<Item> PICKAXES_INVAR = forgeTag("pickaxes/invar");
		public static final IOptionalNamedTag<Item> PICKAXES_STEEL = forgeTag("pickaxes/steel");
		public static final IOptionalNamedTag<Item> PICKAXES_RUBY = forgeTag("pickaxes/ruby");
		public static final IOptionalNamedTag<Item> PICKAXES_AMETHYST = forgeTag("pickaxes/amethyst");
		public static final IOptionalNamedTag<Item> PICKAXES_SAPPHIRE = forgeTag("pickaxes/sapphire");
		public static final IOptionalNamedTag<Item> PICKAXES_TOPAZ = forgeTag("pickaxes/topaz");
		public static final IOptionalNamedTag<Item> PICKAXES_EMERALD = forgeTag("pickaxes/emerald");
		public static final IOptionalNamedTag<Item> PICKAXES_PERIDOT = forgeTag("pickaxes/peridot");
		public static final IOptionalNamedTag<Item> PICKAXES_WOODEN = forgeTag("pickaxes/wooden");
		public static final IOptionalNamedTag<Item> PICKAXES_STONE = forgeTag("pickaxes/stone");
		public static final IOptionalNamedTag<Item> PICKAXES_GOLDEN = forgeTag("pickaxes/golden");
		public static final IOptionalNamedTag<Item> PICKAXES_IRON = forgeTag("pickaxes/iron");
		public static final IOptionalNamedTag<Item> PICKAXES_DIAMOND = forgeTag("pickaxes/diamond");
		public static final IOptionalNamedTag<Item> PICKAXES_NETHERITE = forgeTag("pickaxes/netherite");

		public static final IOptionalNamedTag<Item> SHOVELS = forgeTag("shovels");
		public static final IOptionalNamedTag<Item> SHOVELS_TIN = forgeTag("shovels/tin");
		public static final IOptionalNamedTag<Item> SHOVELS_COPPER = forgeTag("shovels/copper");
		public static final IOptionalNamedTag<Item> SHOVELS_SILVER = forgeTag("shovels/silver");
		public static final IOptionalNamedTag<Item> SHOVELS_ALUMINUM = forgeTag("shovels/aluminum");
		public static final IOptionalNamedTag<Item> SHOVELS_NICKEL = forgeTag("shovels/nickel");
		public static final IOptionalNamedTag<Item> SHOVELS_PLATINUM = forgeTag("shovels/platinum");
		public static final IOptionalNamedTag<Item> SHOVELS_LEAD = forgeTag("shovels/lead");
		public static final IOptionalNamedTag<Item> SHOVELS_BRONZE = forgeTag("shovels/bronze");
		public static final IOptionalNamedTag<Item> SHOVELS_ELECTRUM = forgeTag("shovels/electrum");
		public static final IOptionalNamedTag<Item> SHOVELS_INVAR = forgeTag("shovels/invar");
		public static final IOptionalNamedTag<Item> SHOVELS_STEEL = forgeTag("shovels/steel");
		public static final IOptionalNamedTag<Item> SHOVELS_RUBY = forgeTag("shovels/ruby");
		public static final IOptionalNamedTag<Item> SHOVELS_AMETHYST = forgeTag("shovels/amethyst");
		public static final IOptionalNamedTag<Item> SHOVELS_SAPPHIRE = forgeTag("shovels/sapphire");
		public static final IOptionalNamedTag<Item> SHOVELS_TOPAZ = forgeTag("shovels/topaz");
		public static final IOptionalNamedTag<Item> SHOVELS_EMERALD = forgeTag("shovels/emerald");
		public static final IOptionalNamedTag<Item> SHOVELS_PERIDOT = forgeTag("shovels/peridot");
		public static final IOptionalNamedTag<Item> SHOVELS_WOODEN = forgeTag("shovels/wooden");
		public static final IOptionalNamedTag<Item> SHOVELS_STONE = forgeTag("shovels/stone");
		public static final IOptionalNamedTag<Item> SHOVELS_GOLDEN = forgeTag("shovels/golden");
		public static final IOptionalNamedTag<Item> SHOVELS_IRON = forgeTag("shovels/iron");
		public static final IOptionalNamedTag<Item> SHOVELS_DIAMOND = forgeTag("shovels/diamond");
		public static final IOptionalNamedTag<Item> SHOVELS_NETHERITE = forgeTag("shovels/netherite");

		public static final IOptionalNamedTag<Item> AXES = forgeTag("axes");
		public static final IOptionalNamedTag<Item> AXES_TIN = forgeTag("axes/tin");
		public static final IOptionalNamedTag<Item> AXES_COPPER = forgeTag("axes/copper");
		public static final IOptionalNamedTag<Item> AXES_SILVER = forgeTag("axes/silver");
		public static final IOptionalNamedTag<Item> AXES_ALUMINUM = forgeTag("axes/aluminum");
		public static final IOptionalNamedTag<Item> AXES_NICKEL = forgeTag("axes/nickel");
		public static final IOptionalNamedTag<Item> AXES_PLATINUM = forgeTag("axes/platinum");
		public static final IOptionalNamedTag<Item> AXES_LEAD = forgeTag("axes/lead");
		public static final IOptionalNamedTag<Item> AXES_BRONZE = forgeTag("axes/bronze");
		public static final IOptionalNamedTag<Item> AXES_ELECTRUM = forgeTag("axes/electrum");
		public static final IOptionalNamedTag<Item> AXES_INVAR = forgeTag("axes/invar");
		public static final IOptionalNamedTag<Item> AXES_STEEL = forgeTag("axes/steel");
		public static final IOptionalNamedTag<Item> AXES_RUBY = forgeTag("axes/ruby");
		public static final IOptionalNamedTag<Item> AXES_AMETHYST = forgeTag("axes/amethyst");
		public static final IOptionalNamedTag<Item> AXES_SAPPHIRE = forgeTag("axes/sapphire");
		public static final IOptionalNamedTag<Item> AXES_TOPAZ = forgeTag("axes/topaz");
		public static final IOptionalNamedTag<Item> AXES_EMERALD = forgeTag("axes/emerald");
		public static final IOptionalNamedTag<Item> AXES_PERIDOT = forgeTag("axes/peridot");
		public static final IOptionalNamedTag<Item> AXES_WOODEN = forgeTag("axes/wooden");
		public static final IOptionalNamedTag<Item> AXES_STONE = forgeTag("axes/stone");
		public static final IOptionalNamedTag<Item> AXES_GOLDEN = forgeTag("axes/golden");
		public static final IOptionalNamedTag<Item> AXES_IRON = forgeTag("axes/iron");
		public static final IOptionalNamedTag<Item> AXES_DIAMOND = forgeTag("axes/diamond");
		public static final IOptionalNamedTag<Item> AXES_NETHERITE = forgeTag("axes/netherite");

		public static final IOptionalNamedTag<Item> HOES = forgeTag("hoes");
		public static final IOptionalNamedTag<Item> HOES_TIN = forgeTag("hoes/tin");
		public static final IOptionalNamedTag<Item> HOES_COPPER = forgeTag("hoes/copper");
		public static final IOptionalNamedTag<Item> HOES_SILVER = forgeTag("hoes/silver");
		public static final IOptionalNamedTag<Item> HOES_ALUMINUM = forgeTag("hoes/aluminum");
		public static final IOptionalNamedTag<Item> HOES_NICKEL = forgeTag("hoes/nickel");
		public static final IOptionalNamedTag<Item> HOES_PLATINUM = forgeTag("hoes/platinum");
		public static final IOptionalNamedTag<Item> HOES_LEAD = forgeTag("hoes/lead");
		public static final IOptionalNamedTag<Item> HOES_BRONZE = forgeTag("hoes/bronze");
		public static final IOptionalNamedTag<Item> HOES_ELECTRUM = forgeTag("hoes/electrum");
		public static final IOptionalNamedTag<Item> HOES_INVAR = forgeTag("hoes/invar");
		public static final IOptionalNamedTag<Item> HOES_STEEL = forgeTag("hoes/steel");
		public static final IOptionalNamedTag<Item> HOES_RUBY = forgeTag("hoes/ruby");
		public static final IOptionalNamedTag<Item> HOES_AMETHYST = forgeTag("hoes/amethyst");
		public static final IOptionalNamedTag<Item> HOES_SAPPHIRE = forgeTag("hoes/sapphire");
		public static final IOptionalNamedTag<Item> HOES_TOPAZ = forgeTag("hoes/topaz");
		public static final IOptionalNamedTag<Item> HOES_EMERALD = forgeTag("hoes/emerald");
		public static final IOptionalNamedTag<Item> HOES_PERIDOT = forgeTag("hoes/peridot");
		public static final IOptionalNamedTag<Item> HOES_WOODEN = forgeTag("hoes/wooden");
		public static final IOptionalNamedTag<Item> HOES_STONE = forgeTag("hoes/stone");
		public static final IOptionalNamedTag<Item> HOES_GOLDEN = forgeTag("hoes/golden");
		public static final IOptionalNamedTag<Item> HOES_IRON = forgeTag("hoes/iron");
		public static final IOptionalNamedTag<Item> HOES_DIAMOND = forgeTag("hoes/diamond");
		public static final IOptionalNamedTag<Item> HOES_NETHERITE = forgeTag("hoes/netherite");

		public static final IOptionalNamedTag<Item> HELMETS = forgeTag("helmets");
		public static final IOptionalNamedTag<Item> HELMETS_TIN = forgeTag("helmets/tin");
		public static final IOptionalNamedTag<Item> HELMETS_COPPER = forgeTag("helmets/copper");
		public static final IOptionalNamedTag<Item> HELMETS_SILVER = forgeTag("helmets/silver");
		public static final IOptionalNamedTag<Item> HELMETS_ALUMINUM = forgeTag("helmets/aluminum");
		public static final IOptionalNamedTag<Item> HELMETS_NICKEL = forgeTag("helmets/nickel");
		public static final IOptionalNamedTag<Item> HELMETS_PLATINUM = forgeTag("helmets/platinum");
		public static final IOptionalNamedTag<Item> HELMETS_LEAD = forgeTag("helmets/lead");
		public static final IOptionalNamedTag<Item> HELMETS_BRONZE = forgeTag("helmets/bronze");
		public static final IOptionalNamedTag<Item> HELMETS_ELECTRUM = forgeTag("helmets/electrum");
		public static final IOptionalNamedTag<Item> HELMETS_INVAR = forgeTag("helmets/invar");
		public static final IOptionalNamedTag<Item> HELMETS_STEEL = forgeTag("helmets/steel");
		public static final IOptionalNamedTag<Item> HELMETS_RUBY = forgeTag("helmets/ruby");
		public static final IOptionalNamedTag<Item> HELMETS_AMETHYST = forgeTag("helmets/amethyst");
		public static final IOptionalNamedTag<Item> HELMETS_SAPPHIRE = forgeTag("helmets/sapphire");
		public static final IOptionalNamedTag<Item> HELMETS_TOPAZ = forgeTag("helmets/topaz");
		public static final IOptionalNamedTag<Item> HELMETS_EMERALD = forgeTag("helmets/emerald");
		public static final IOptionalNamedTag<Item> HELMETS_PERIDOT = forgeTag("helmets/peridot");
		public static final IOptionalNamedTag<Item> HELMETS_LEATHER = forgeTag("helmets/leather");
		public static final IOptionalNamedTag<Item> HELMETS_CHAINMAIL = forgeTag("helmets/chainmail");
		public static final IOptionalNamedTag<Item> HELMETS_GOLDEN = forgeTag("helmets/golden");
		public static final IOptionalNamedTag<Item> HELMETS_IRON = forgeTag("helmets/iron");
		public static final IOptionalNamedTag<Item> HELMETS_DIAMOND = forgeTag("helmets/diamond");
		public static final IOptionalNamedTag<Item> HELMETS_NETHERITE = forgeTag("helmets/netherite");
		public static final IOptionalNamedTag<Item> HELMETS_TURTLE = forgeTag("helmets/turtle");

		public static final IOptionalNamedTag<Item> CHESTPLATES = forgeTag("chestplates");
		public static final IOptionalNamedTag<Item> CHESTPLATES_TIN = forgeTag("chestplates/tin");
		public static final IOptionalNamedTag<Item> CHESTPLATES_COPPER = forgeTag("chestplates/copper");
		public static final IOptionalNamedTag<Item> CHESTPLATES_SILVER = forgeTag("chestplates/silver");
		public static final IOptionalNamedTag<Item> CHESTPLATES_ALUMINUM = forgeTag("chestplates/aluminum");
		public static final IOptionalNamedTag<Item> CHESTPLATES_NICKEL = forgeTag("chestplates/nickel");
		public static final IOptionalNamedTag<Item> CHESTPLATES_PLATINUM = forgeTag("chestplates/platinum");
		public static final IOptionalNamedTag<Item> CHESTPLATES_LEAD = forgeTag("chestplates/lead");
		public static final IOptionalNamedTag<Item> CHESTPLATES_BRONZE = forgeTag("chestplates/bronze");
		public static final IOptionalNamedTag<Item> CHESTPLATES_ELECTRUM = forgeTag("chestplates/electrum");
		public static final IOptionalNamedTag<Item> CHESTPLATES_INVAR = forgeTag("chestplates/invar");
		public static final IOptionalNamedTag<Item> CHESTPLATES_STEEL = forgeTag("chestplates/steel");
		public static final IOptionalNamedTag<Item> CHESTPLATES_RUBY = forgeTag("chestplates/ruby");
		public static final IOptionalNamedTag<Item> CHESTPLATES_AMETHYST = forgeTag("chestplates/amethyst");
		public static final IOptionalNamedTag<Item> CHESTPLATES_SAPPHIRE = forgeTag("chestplates/sapphire");
		public static final IOptionalNamedTag<Item> CHESTPLATES_TOPAZ = forgeTag("chestplates/topaz");
		public static final IOptionalNamedTag<Item> CHESTPLATES_EMERALD = forgeTag("chestplates/emerald");
		public static final IOptionalNamedTag<Item> CHESTPLATES_PERIDOT = forgeTag("chestplates/peridot");
		public static final IOptionalNamedTag<Item> CHESTPLATES_LEATHER = forgeTag("chestplates/leather");
		public static final IOptionalNamedTag<Item> CHESTPLATES_CHAINMAIL = forgeTag("chestplates/chainmail");
		public static final IOptionalNamedTag<Item> CHESTPLATES_GOLDEN = forgeTag("chestplates/golden");
		public static final IOptionalNamedTag<Item> CHESTPLATES_IRON = forgeTag("chestplates/iron");
		public static final IOptionalNamedTag<Item> CHESTPLATES_DIAMOND = forgeTag("chestplates/diamond");
		public static final IOptionalNamedTag<Item> CHESTPLATES_NETHERITE = forgeTag("chestplates/netherite");

		public static final IOptionalNamedTag<Item> LEGGINGS = forgeTag("leggings");
		public static final IOptionalNamedTag<Item> LEGGINGS_TIN = forgeTag("leggings/tin");
		public static final IOptionalNamedTag<Item> LEGGINGS_COPPER = forgeTag("leggings/copper");
		public static final IOptionalNamedTag<Item> LEGGINGS_SILVER = forgeTag("leggings/silver");
		public static final IOptionalNamedTag<Item> LEGGINGS_ALUMINUM = forgeTag("leggings/aluminum");
		public static final IOptionalNamedTag<Item> LEGGINGS_NICKEL = forgeTag("leggings/nickel");
		public static final IOptionalNamedTag<Item> LEGGINGS_PLATINUM = forgeTag("leggings/platinum");
		public static final IOptionalNamedTag<Item> LEGGINGS_LEAD = forgeTag("leggings/lead");
		public static final IOptionalNamedTag<Item> LEGGINGS_BRONZE = forgeTag("leggings/bronze");
		public static final IOptionalNamedTag<Item> LEGGINGS_ELECTRUM = forgeTag("leggings/electrum");
		public static final IOptionalNamedTag<Item> LEGGINGS_INVAR = forgeTag("leggings/invar");
		public static final IOptionalNamedTag<Item> LEGGINGS_STEEL = forgeTag("leggings/steel");
		public static final IOptionalNamedTag<Item> LEGGINGS_RUBY = forgeTag("leggings/ruby");
		public static final IOptionalNamedTag<Item> LEGGINGS_AMETHYST = forgeTag("leggings/amethyst");
		public static final IOptionalNamedTag<Item> LEGGINGS_SAPPHIRE = forgeTag("leggings/sapphire");
		public static final IOptionalNamedTag<Item> LEGGINGS_TOPAZ = forgeTag("leggings/topaz");
		public static final IOptionalNamedTag<Item> LEGGINGS_EMERALD = forgeTag("leggings/emerald");
		public static final IOptionalNamedTag<Item> LEGGINGS_PERIDOT = forgeTag("leggings/peridot");
		public static final IOptionalNamedTag<Item> LEGGINGS_LEATHER = forgeTag("leggings/leather");
		public static final IOptionalNamedTag<Item> LEGGINGS_CHAINMAIL = forgeTag("leggings/chainmail");
		public static final IOptionalNamedTag<Item> LEGGINGS_GOLDEN = forgeTag("leggings/golden");
		public static final IOptionalNamedTag<Item> LEGGINGS_IRON = forgeTag("leggings/iron");
		public static final IOptionalNamedTag<Item> LEGGINGS_DIAMOND = forgeTag("leggings/diamond");
		public static final IOptionalNamedTag<Item> LEGGINGS_NETHERITE = forgeTag("leggings/netherite");

		public static final IOptionalNamedTag<Item> BOOTS = forgeTag("boots");
		public static final IOptionalNamedTag<Item> BOOTS_TIN = forgeTag("boots/tin");
		public static final IOptionalNamedTag<Item> BOOTS_COPPER = forgeTag("boots/copper");
		public static final IOptionalNamedTag<Item> BOOTS_SILVER = forgeTag("boots/silver");
		public static final IOptionalNamedTag<Item> BOOTS_ALUMINUM = forgeTag("boots/aluminum");
		public static final IOptionalNamedTag<Item> BOOTS_NICKEL = forgeTag("boots/nickel");
		public static final IOptionalNamedTag<Item> BOOTS_PLATINUM = forgeTag("boots/platinum");
		public static final IOptionalNamedTag<Item> BOOTS_LEAD = forgeTag("boots/lead");
		public static final IOptionalNamedTag<Item> BOOTS_BRONZE = forgeTag("boots/bronze");
		public static final IOptionalNamedTag<Item> BOOTS_ELECTRUM = forgeTag("boots/electrum");
		public static final IOptionalNamedTag<Item> BOOTS_INVAR = forgeTag("boots/invar");
		public static final IOptionalNamedTag<Item> BOOTS_STEEL = forgeTag("boots/steel");
		public static final IOptionalNamedTag<Item> BOOTS_RUBY = forgeTag("boots/ruby");
		public static final IOptionalNamedTag<Item> BOOTS_AMETHYST = forgeTag("boots/amethyst");
		public static final IOptionalNamedTag<Item> BOOTS_SAPPHIRE = forgeTag("boots/sapphire");
		public static final IOptionalNamedTag<Item> BOOTS_TOPAZ = forgeTag("boots/topaz");
		public static final IOptionalNamedTag<Item> BOOTS_EMERALD = forgeTag("boots/emerald");
		public static final IOptionalNamedTag<Item> BOOTS_PERIDOT = forgeTag("boots/peridot");
		public static final IOptionalNamedTag<Item> BOOTS_LEATHER = forgeTag("boots/leather");
		public static final IOptionalNamedTag<Item> BOOTS_CHAINMAIL = forgeTag("boots/chainmail");
		public static final IOptionalNamedTag<Item> BOOTS_GOLDEN = forgeTag("boots/golden");
		public static final IOptionalNamedTag<Item> BOOTS_IRON = forgeTag("boots/iron");
		public static final IOptionalNamedTag<Item> BOOTS_DIAMOND = forgeTag("boots/diamond");
		public static final IOptionalNamedTag<Item> BOOTS_NETHERITE = forgeTag("boots/netherite");

		public static IOptionalNamedTag<Item> forgeTag(String name) {
			return ItemTags.createOptional(new ResourceLocation("forge", name));
		}
	}

}
