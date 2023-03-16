package com.grim3212.assorted.tools.config;

import com.grim3212.assorted.lib.config.IConfigurationBuilder;
import com.grim3212.assorted.tools.api.item.BucketOptions;
import com.grim3212.assorted.tools.api.item.ToolsItemTier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModdedItemTierConfig extends ItemTierConfig {
    private final ToolsItemTier moddedTier;

    public ModdedItemTierConfig(IConfigurationBuilder builder, String name, String path, ToolsItemTier defaultTier) {
        super(builder, name, path, defaultTier);
        this.moddedTier = defaultTier;
    }

    public TagKey<Item> getMaterial() {
        return this.moddedTier.repairTag();
    }

    public BucketOptions getBucketOptions() {
        return this.moddedTier.getBucketOptions();
    }
}
