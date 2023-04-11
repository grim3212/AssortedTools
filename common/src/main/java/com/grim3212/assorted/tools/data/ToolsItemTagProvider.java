package com.grim3212.assorted.tools.data;

import com.grim3212.assorted.lib.data.LibItemTagProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.tools.api.ToolsTags;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ToolsItemTagProvider extends LibItemTagProvider {
    public ToolsItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, TagsProvider<Block> blockTags) {
        super(output, lookup, blockTags);
    }

    @Override
    public void addCommonTags(Function<TagKey<Item>, IntrinsicTagAppender<Item>> tagger, BiConsumer<TagKey<Block>, TagKey<Item>> copier) {
        tagger.apply(LibCommonTags.Items.FLUID_CONTAINERS).add(ToolsItems.WOOD_BUCKET.get(), ToolsItems.STONE_BUCKET.get(), ToolsItems.GOLD_BUCKET.get(), ToolsItems.DIAMOND_BUCKET.get(), ToolsItems.NETHERITE_BUCKET.get());
        tagger.apply(LibCommonTags.Items.BUCKETS_MILK).add(ToolsItems.WOOD_MILK_BUCKET.get(), ToolsItems.STONE_MILK_BUCKET.get(), ToolsItems.GOLD_MILK_BUCKET.get(), Items.MILK_BUCKET, ToolsItems.DIAMOND_MILK_BUCKET.get(), ToolsItems.NETHERITE_MILK_BUCKET.get());
        tagger.apply(LibCommonTags.Items.SHEARS).add(ToolsItems.WOOD_SHEARS.get(), ToolsItems.STONE_SHEARS.get(), ToolsItems.GOLD_SHEARS.get(), ToolsItems.DIAMOND_SHEARS.get(), ToolsItems.NETHERITE_SHEARS.get());

        ToolsItems.MATERIAL_GROUPS.forEach((s, group) -> {
            // Add to top level tags
            tagger.apply(LibCommonTags.Items.TOOLS_SWORDS).add(group.SWORD.get());
            tagger.apply(LibCommonTags.Items.TOOLS_PICKAXES).add(group.PICKAXE.get());
            tagger.apply(LibCommonTags.Items.TOOLS_SHOVELS).add(group.SHOVEL.get());
            tagger.apply(LibCommonTags.Items.TOOLS_AXES).add(group.AXE.get());
            tagger.apply(LibCommonTags.Items.TOOLS_HOES).add(group.HOE.get());
            tagger.apply(LibCommonTags.Items.ARMORS_HELMETS).add(group.HELMET.get());
            tagger.apply(LibCommonTags.Items.ARMORS_CHESTPLATES).add(group.CHESTPLATE.get());
            tagger.apply(LibCommonTags.Items.ARMORS_LEGGINGS).add(group.LEGGINGS.get());
            tagger.apply(LibCommonTags.Items.ARMORS_BOOTS).add(group.BOOTS.get());
            tagger.apply(LibCommonTags.Items.FLUID_CONTAINERS).add(group.BUCKET.get());
            tagger.apply(LibCommonTags.Items.BUCKETS_MILK).add(group.MILK_BUCKET.get());
            tagger.apply(LibCommonTags.Items.SHEARS).add(group.SHEARS.get());
        });
        tagger.apply(ToolsTags.Items.ULTIMATE_FRAGMENTS).add(ToolsItems.U_FRAGMENT.get(), ToolsItems.L_FRAGMENT.get(), ToolsItems.T_FRAGMENT.get(), ToolsItems.I_FRAGMENT.get(), ToolsItems.M_FRAGMENT.get(), ToolsItems.A_FRAGMENT.get(), ToolsItems.MISSING_FRAGMENT.get(), ToolsItems.E_FRAGMENT.get());

        tagger.apply(ItemTags.PIGLIN_LOVED).add(ToolsItems.GOLD_HAMMER.get(), ToolsItems.GOLDEN_MULTITOOL.get(), ToolsItems.GOLD_SPEAR.get(), ToolsItems.BUILDING_WAND.get(), ToolsItems.REINFORCED_BUILDING_WAND.get(), ToolsItems.GOLD_BUCKET.get(), ToolsItems.GOLD_MILK_BUCKET.get(), ToolsItems.GOLD_SHEARS.get());

        tagger.apply(ToolsTags.Items.CAGE_SUPPORTED).add(ToolsItems.POKEBALL.get());
    }
}
