package com.grim3212.assorted.tools.common.data;

import com.grim3212.assorted.decor.Constants;
import com.grim3212.assorted.decor.api.ToolsTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ToolsBlockTagProvider extends BlockTagsProvider {

    public ToolsBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(Provider provider) {
        this.tag(ToolsTags.Blocks.MINEABLE_MULTITOOL).addTag(BlockTags.MINEABLE_WITH_AXE).addTag(BlockTags.MINEABLE_WITH_HOE).addTag(BlockTags.MINEABLE_WITH_PICKAXE).addTag(BlockTags.MINEABLE_WITH_SHOVEL);

        this.tag(ToolsTags.Blocks.DEAD_CORALS).add(Blocks.DEAD_TUBE_CORAL, Blocks.DEAD_BRAIN_CORAL, Blocks.DEAD_BUBBLE_CORAL, Blocks.DEAD_FIRE_CORAL, Blocks.DEAD_HORN_CORAL, Blocks.DEAD_TUBE_CORAL_FAN, Blocks.DEAD_BRAIN_CORAL_FAN, Blocks.DEAD_BUBBLE_CORAL_FAN, Blocks.DEAD_FIRE_CORAL_FAN, Blocks.DEAD_HORN_CORAL_FAN);
        this.tag(ToolsTags.Blocks.ALL_CORALS).addTag(BlockTags.CORAL_BLOCKS).addTag(BlockTags.CORAL_PLANTS).addTag(BlockTags.WALL_CORALS).addTag(BlockTags.CORALS).addTag(ToolsTags.Blocks.DEAD_CORALS);
    }

}
