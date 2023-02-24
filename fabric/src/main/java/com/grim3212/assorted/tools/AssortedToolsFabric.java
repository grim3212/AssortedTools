package com.grim3212.assorted.decor;

import com.grim3212.assorted.decor.common.helpers.DecorCreativeItems;
import com.grim3212.assorted.decor.common.items.DecorItems;
import com.grim3212.assorted.decor.services.FabricConfigHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class AssortedDecorFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        FabricConfigHelper.setup();
        ToolsCommonMod.init();

        CreativeModeTab ASSORTED_DECOR_GROUP = FabricItemGroup.builder(new ResourceLocation(Constants.MOD_ID, "tab")).icon(() -> new ItemStack(DecorItems.WALLPAPER.get())).build();
        ItemGroupEvents.modifyEntriesEvent(ASSORTED_DECOR_GROUP).register(populator -> {
            populator.acceptAll(DecorCreativeItems.getCreativeItems());
        });
    }
}
