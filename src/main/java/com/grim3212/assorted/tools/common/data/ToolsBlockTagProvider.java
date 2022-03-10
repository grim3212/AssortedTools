package com.grim3212.assorted.tools.common.data;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.util.ToolsTags;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ToolsBlockTagProvider extends BlockTagsProvider {

	public ToolsBlockTagProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
		super(generatorIn, AssortedTools.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		this.tag(ToolsTags.Blocks.MINEABLE_MULTITOOL).addTag(BlockTags.MINEABLE_WITH_AXE).addTag(BlockTags.MINEABLE_WITH_HOE).addTag(BlockTags.MINEABLE_WITH_PICKAXE).addTag(BlockTags.MINEABLE_WITH_SHOVEL);

		this.tag(ToolsTags.Blocks.DEAD_CORALS).add(Blocks.DEAD_TUBE_CORAL, Blocks.DEAD_BRAIN_CORAL, Blocks.DEAD_BUBBLE_CORAL, Blocks.DEAD_FIRE_CORAL, Blocks.DEAD_HORN_CORAL, Blocks.DEAD_TUBE_CORAL_FAN, Blocks.DEAD_BRAIN_CORAL_FAN, Blocks.DEAD_BUBBLE_CORAL_FAN, Blocks.DEAD_FIRE_CORAL_FAN, Blocks.DEAD_HORN_CORAL_FAN);
		this.tag(ToolsTags.Blocks.ALL_CORALS).addTag(BlockTags.CORAL_BLOCKS).addTag(BlockTags.CORAL_PLANTS).addTag(BlockTags.WALL_CORALS).addTag(BlockTags.CORALS).addTag(ToolsTags.Blocks.DEAD_CORALS);
	}

}
