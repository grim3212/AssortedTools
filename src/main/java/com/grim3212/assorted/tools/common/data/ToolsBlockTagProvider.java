package com.grim3212.assorted.tools.common.data;

import com.grim3212.assorted.tools.AssortedTools;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ToolsBlockTagProvider extends BlockTagsProvider {

	public ToolsBlockTagProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
		super(generatorIn, AssortedTools.MODID, existingFileHelper);
	}

	@Override
	protected void registerTags() {

	}

}
