package com.grim3212.assorted.tools;

import com.grim3212.assorted.decor.Constants;
import com.grim3212.assorted.decor.ToolsCommonMod;
import com.grim3212.assorted.decor.common.handlers.ToolsCreativeItems;
import com.grim3212.assorted.decor.common.item.ToolsItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class AssortedToolsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ToolsCommonMod.init();

        CreativeModeTab ASSORTED_TOOLS_GROUP = FabricItemGroup.builder(new ResourceLocation(Constants.MOD_ID, "tab")).icon(() -> new ItemStack(ToolsItems.IRON_HAMMER.get())).build();
        ItemGroupEvents.modifyEntriesEvent(ASSORTED_TOOLS_GROUP).register(populator -> {
            populator.acceptAll(ToolsCreativeItems.getCreativeItems());
        });
    }
}
