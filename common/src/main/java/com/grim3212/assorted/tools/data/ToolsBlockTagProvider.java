package com.grim3212.assorted.tools.data;

import com.grim3212.assorted.lib.data.LibBlockTagProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.tools.api.ToolsTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ToolsBlockTagProvider extends LibBlockTagProvider {
    public ToolsBlockTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup) {
        super(packOutput, lookup);
    }

    @Override
    public void addCommonTags(Function<TagKey<Block>, TagAppender<Block>> appender) {
        // The intrinsic tag appender is gone; TagAppender only accepts ResourceKeys. Wrap it back
        // into something that takes blocks so the tag lists below stay readable.
        Function<TagKey<Block>, BlockTagger> tagger = (tag) -> new BlockTagger(appender.apply(tag));

        tagger.apply(ToolsTags.Blocks.MINEABLE_MULTITOOL)
                .addOptionalTag(BlockTags.MINEABLE_WITH_AXE)
                .addOptionalTag(BlockTags.MINEABLE_WITH_HOE)
                .addOptionalTag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addOptionalTag(BlockTags.MINEABLE_WITH_SHOVEL);

        tagger.apply(ToolsTags.Blocks.DEAD_CORALS)
                .add(Blocks.DEAD_TUBE_CORAL, Blocks.DEAD_BRAIN_CORAL, Blocks.DEAD_BUBBLE_CORAL, Blocks.DEAD_FIRE_CORAL, Blocks.DEAD_HORN_CORAL, Blocks.DEAD_TUBE_CORAL_FAN, Blocks.DEAD_BRAIN_CORAL_FAN, Blocks.DEAD_BUBBLE_CORAL_FAN, Blocks.DEAD_FIRE_CORAL_FAN, Blocks.DEAD_HORN_CORAL_FAN);
        tagger.apply(ToolsTags.Blocks.ALL_CORALS)
                .addTag(ToolsTags.Blocks.DEAD_CORALS)
                .addOptionalTag(BlockTags.CORAL_BLOCKS)
                .addOptionalTag(BlockTags.CORAL_PLANTS)
                .addOptionalTag(BlockTags.WALL_CORALS)
                .addOptionalTag(BlockTags.CORALS);

        tagger.apply(ToolsTags.Blocks.DESTRUCTIVE_SPARED_BLOCKS)
                .add(Blocks.SPAWNER)
                .addOptionalTag(LibCommonTags.Blocks.ORES)
                .addOptionalTag(LibCommonTags.Blocks.CHESTS);
        tagger.apply(ToolsTags.Blocks.MINING_SURFACE_BLOCKS)
                .addOptionalTag(LibCommonTags.Blocks.ORES);
    }

    private record BlockTagger(TagAppender<Block> appender) {

        BlockTagger add(Block... blocks) {
            for (Block block : blocks) {
                this.appender.add(BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow());
            }

            return this;
        }

        BlockTagger addTag(TagKey<Block> tag) {
            this.appender.addTag(tag);
            return this;
        }

        BlockTagger addOptionalTag(TagKey<Block> tag) {
            this.appender.addOptionalTag(tag);
            return this;
        }
    }
}
