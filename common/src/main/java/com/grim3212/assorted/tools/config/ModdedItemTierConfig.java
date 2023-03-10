package com.grim3212.assorted.tools.config;

import com.grim3212.assorted.lib.config.ConfigGroup;
import com.grim3212.assorted.tools.api.item.BucketOptions;
import com.grim3212.assorted.tools.api.item.ToolsItemTier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModdedItemTierConfig extends ItemTierConfig {
    private final ToolsItemTier moddedTier;

    public ModdedItemTierConfig(ConfigGroup group, String name, ToolsItemTier defaultTier) {
        super(group, name, defaultTier);
        this.moddedTier = defaultTier;
    }

    public TagKey<Item> getMaterial() {
        return this.moddedTier.repairTag();
    }

    public BucketOptions getBucketOptions() {
        return this.moddedTier.getBucketOptions();
    }
}
