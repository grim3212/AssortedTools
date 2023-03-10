package com.grim3212.assorted.tools.common.data;

import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.data.ToolsCommonTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ForgeBlockTagProvider extends BlockTagsProvider {

    private final ToolsCommonTagProvider.BlockTagProvider commonBlocks;

    public ForgeBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Constants.MOD_ID, existingFileHelper);
        this.commonBlocks = new ToolsCommonTagProvider.BlockTagProvider(output, lookupProvider);
    }

    @Override
    protected void addTags(Provider provider) {
        this.commonBlocks.addCommonTags(this::tag);
    }

    @Override
    public String getName() {
        return "Assorted Tools block tags";
    }
}
