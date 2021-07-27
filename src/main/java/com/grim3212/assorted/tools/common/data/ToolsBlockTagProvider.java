package com.grim3212.assorted.tools.common.data;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.util.ToolsTags;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ToolsBlockTagProvider extends BlockTagsProvider {

	public ToolsBlockTagProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
		super(generatorIn, AssortedTools.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		this.tag(ToolsTags.Blocks.MINEABLE_MULTITOOL).addTag(BlockTags.MINEABLE_WITH_AXE).addTag(BlockTags.MINEABLE_WITH_HOE).addTag(BlockTags.MINEABLE_WITH_PICKAXE).addTag(BlockTags.MINEABLE_WITH_SHOVEL);
	}

}
