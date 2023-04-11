package com.grim3212.assorted.tools.data;

import com.grim3212.assorted.lib.data.LibBlockTagProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.tools.api.ToolsTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
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
