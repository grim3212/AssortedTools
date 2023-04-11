package com.grim3212.assorted.tools;

import com.grim3212.assorted.lib.events.*;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.tools.common.crafting.ToolsConditions;
import com.grim3212.assorted.tools.common.enchantment.ToolsEnchantments;
import com.grim3212.assorted.tools.common.entity.ToolsEntities;
import com.grim3212.assorted.tools.common.handlers.*;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.grim3212.assorted.tools.common.network.ToolsPackets;
import com.grim3212.assorted.tools.config.ToolsCommonConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ToolsCommonMod {

    public static final ToolsCommonConfig COMMON_CONFIG = new ToolsCommonConfig();

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");

        ToolsItems.init();
        ToolsEntities.init();
        ToolsEnchantments.init();
        ToolsPackets.init();
        ToolsConditions.init();

        Services.PLATFORM.registerCreativeTab(new ResourceLocation(Constants.MOD_ID, "tab"), Component.translatable("itemGroup.assortedtools"), () -> new ItemStack(ToolsItems.IRON_HAMMER.get()), () -> ToolsCreativeItems.getCreativeItems());

        Services.EVENTS.registerEvent(AnvilUpdatedEvent.class, (final AnvilUpdatedEvent event) -> ChickenSuitConversionHandler.anvilUpdateEvent(event));
        Services.EVENTS.registerEvent(EntityInteractEvent.class, (final EntityInteractEvent event) -> MilkingHandler.interact(event));
        Services.EVENTS.registerEvent(LootTableModifyEvent.class, (final LootTableModifyEvent event) -> LootTableHandlers.init(event));
        Services.EVENTS.registerEvent(OnDropStacksEvent.class, (final OnDropStacksEvent event) -> CoralCutterHandler.handleDrop(event));
        Services.EVENTS.registerEvent(CorrectToolForDropEvent.class, (final CorrectToolForDropEvent event) -> CoralCutterHandler.handleCorrectTool(event));
    }
}
