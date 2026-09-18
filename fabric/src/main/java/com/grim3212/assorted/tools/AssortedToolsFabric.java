package com.grim3212.assorted.tools;

import com.grim3212.assorted.tools.common.item.FabricBetterBucketFluidHandler;
import com.grim3212.assorted.tools.common.item.FrozenMobs;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.world.item.Item;

public class AssortedToolsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ToolsCommonMod.init();

        // Fabric has no per-entity tick event, so the level's entities are walked once a tick instead.
        ServerTickEvents.END_LEVEL_TICK.register(level -> level.getAllEntities().forEach(FrozenMobs::thawIfBurning));

        FluidStorage.ITEM.registerForItems((i, c) -> new FabricBetterBucketFluidHandler(c), ToolsItems.buckets().toArray(new Item[0]));
    }
}
