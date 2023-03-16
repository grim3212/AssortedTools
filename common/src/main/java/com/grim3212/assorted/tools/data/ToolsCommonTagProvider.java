package com.grim3212.assorted.tools.data;

import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.tools.api.ToolsTags;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.data.tags.VanillaBlockTagsProvider;
import net.minecraft.data.tags.VanillaItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.apache.commons.lang3.NotImplementedException;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class ToolsCommonTagProvider {

    public static class BlockTagProvider extends VanillaBlockTagsProvider {

        public BlockTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup) {
            super(packOutput, lookup);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            throw new NotImplementedException();
        }

        @Override
        protected IntrinsicTagAppender<Block> tag(TagKey<Block> tag) {
            throw new NotImplementedException();
        }

        public void addCommonTags(Function<TagKey<Block>, IntrinsicTagAppender<Block>> tagger) {
            tagger.apply(ToolsTags.Blocks.MINEABLE_MULTITOOL)
                    .addOptionalTag(BlockTags.MINEABLE_WITH_AXE.location())
                    .addOptionalTag(BlockTags.MINEABLE_WITH_HOE.location())
                    .addOptionalTag(BlockTags.MINEABLE_WITH_PICKAXE.location())
                    .addOptionalTag(BlockTags.MINEABLE_WITH_SHOVEL.location());

            tagger.apply(ToolsTags.Blocks.DEAD_CORALS)
                    .add(Blocks.DEAD_TUBE_CORAL, Blocks.DEAD_BRAIN_CORAL, Blocks.DEAD_BUBBLE_CORAL, Blocks.DEAD_FIRE_CORAL, Blocks.DEAD_HORN_CORAL, Blocks.DEAD_TUBE_CORAL_FAN, Blocks.DEAD_BRAIN_CORAL_FAN, Blocks.DEAD_BUBBLE_CORAL_FAN, Blocks.DEAD_FIRE_CORAL_FAN, Blocks.DEAD_HORN_CORAL_FAN);
            tagger.apply(ToolsTags.Blocks.ALL_CORALS)
                    .addTag(ToolsTags.Blocks.DEAD_CORALS)
                    .addOptionalTag(BlockTags.CORAL_BLOCKS.location())
                    .addOptionalTag(BlockTags.CORAL_PLANTS.location())
                    .addOptionalTag(BlockTags.WALL_CORALS.location())
                    .addOptionalTag(BlockTags.CORALS.location());

            tagger.apply(ToolsTags.Blocks.DESTRUCTIVE_SPARED_BLOCKS)
                    .add(Blocks.SPAWNER)
                    .addOptionalTag(LibCommonTags.Blocks.ORES.location())
                    .addOptionalTag(LibCommonTags.Blocks.CHESTS.location());
            tagger.apply(ToolsTags.Blocks.MINING_SURFACE_BLOCKS)
                    .addOptionalTag(LibCommonTags.Blocks.ORES.location());
        }
    }

    public static class ItemTagProvider extends VanillaItemTagsProvider {

        public ItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, TagsProvider<Block> blockTags) {
            super(output, lookup, blockTags);
        }

        @Override
        protected void addTags(HolderLookup.Provider lookup) {
            throw new NotImplementedException();
        }

        @Override
        protected IntrinsicTagAppender<Item> tag(TagKey<Item> tag) {
            throw new NotImplementedException();
        }

        @Override
        protected void copy(TagKey<Block> blockTag, TagKey<Item> itemTag) {
            throw new NotImplementedException();
        }

        public void addCommonTags(Function<TagKey<Item>, IntrinsicTagAppender<Item>> tagger, Consumer<Tuple<TagKey<Block>, TagKey<Item>>> copier) {
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

        }
    }
}
