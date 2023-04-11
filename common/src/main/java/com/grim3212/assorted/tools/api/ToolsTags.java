package com.grim3212.assorted.tools.api;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.tools.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ToolsTags {

    public static class Blocks {
        public static final TagKey<Block> MINEABLE_MULTITOOL = commonTag("mineable/multitool");

        public static final TagKey<Block> ALL_CORALS = commonTag("corals/all");
        public static final TagKey<Block> DEAD_CORALS = commonTag("corals/dead");

        public static final TagKey<Block> DESTRUCTIVE_SPARED_BLOCKS = toolsTag("wands/destructive_spared_blocks");
        public static final TagKey<Block> MINING_SURFACE_BLOCKS = toolsTag("wands/mining_surface_blocks");

        private static TagKey<Block> toolsTag(String name) {
            return TagKey.create(Registries.BLOCK, new ResourceLocation(Constants.MOD_ID, name));
        }

        private static TagKey<Block> commonTag(String name) {
            return TagKey.create(Registries.BLOCK, new ResourceLocation(Services.PLATFORM.getCommonTagPrefix(), name));
        }
    }

    public static class Items {
        public static final TagKey<Item> INGOTS_TIN = commonTag("ingots/tin");
        public static final TagKey<Item> INGOTS_SILVER = commonTag("ingots/silver");
        public static final TagKey<Item> INGOTS_ALUMINUM = commonTag("ingots/aluminum");
        public static final TagKey<Item> INGOTS_NICKEL = commonTag("ingots/nickel");
        public static final TagKey<Item> INGOTS_PLATINUM = commonTag("ingots/platinum");
        public static final TagKey<Item> INGOTS_LEAD = commonTag("ingots/lead");
        public static final TagKey<Item> INGOTS_BRONZE = commonTag("ingots/bronze");
        public static final TagKey<Item> INGOTS_ELECTRUM = commonTag("ingots/electrum");
        public static final TagKey<Item> INGOTS_INVAR = commonTag("ingots/invar");
        public static final TagKey<Item> INGOTS_STEEL = commonTag("ingots/steel");
        public static final TagKey<Item> GEMS_RUBY = commonTag("gems/ruby");
        public static final TagKey<Item> GEMS_SAPPHIRE = commonTag("gems/sapphire");
        public static final TagKey<Item> GEMS_TOPAZ = commonTag("gems/topaz");
        public static final TagKey<Item> GEMS_PERIDOT = commonTag("gems/peridot");

        public static final TagKey<Item> ULTIMATE_FRAGMENTS = toolsTag("ultimate_fragments");
        public static final TagKey<Item> CAGE_SUPPORTED = TagKey.create(Registries.ITEM, new ResourceLocation("assorteddecor", "cage_supported"));

        private static TagKey<Item> toolsTag(String name) {
            return TagKey.create(Registries.ITEM, new ResourceLocation(Constants.MOD_ID, name));
        }

        private static TagKey<Item> commonTag(String name) {
            return TagKey.create(Registries.ITEM, new ResourceLocation(Services.PLATFORM.getCommonTagPrefix(), name));
        }
    }
}
