package com.grim3212.assorted.tools.common.handler;

import com.grim3212.assorted.decor.api.item.BucketOptions;
import com.grim3212.assorted.decor.api.item.ToolsItemTier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec.Builder;

public class ModdedItemTierHolder extends ItemTierHolder {

    private final ToolsItemTier moddedTier;

    public ModdedItemTierHolder(Builder builder, String name, ToolsItemTier defaultTier) {
        super(builder, name, defaultTier);
        this.moddedTier = defaultTier;
    }

    public TagKey<Item> getMaterial() {
        return this.moddedTier.repairTag();
    }

    public BucketOptions getBucketOptions() {
        return this.moddedTier.getBucketOptions();
    }
}
