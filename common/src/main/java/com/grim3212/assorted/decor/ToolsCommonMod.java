package com.grim3212.assorted.decor;

import com.grim3212.assorted.decor.common.crafting.ToolsConditions;
import com.grim3212.assorted.decor.common.enchantment.ToolsEnchantments;
import com.grim3212.assorted.decor.common.entity.ToolsEntities;
import com.grim3212.assorted.decor.common.handlers.ChickenSuitConversionHandler;
import com.grim3212.assorted.decor.common.handlers.MilkingHandler;
import com.grim3212.assorted.decor.common.item.ToolsItems;
import com.grim3212.assorted.decor.common.network.ToolsPackets;
import com.grim3212.assorted.decor.config.ToolsConfig;
import com.grim3212.assorted.lib.events.AnvilUpdatedEvent;
import com.grim3212.assorted.lib.events.EntityInteractEvent;
import com.grim3212.assorted.lib.platform.Services;

public class ToolsCommonMod {

    public static void init() {
        ToolsItems.init();
        ToolsEntities.init();
        ToolsEnchantments.init();
        ToolsPackets.init();
        ToolsConditions.init();

        Services.PLATFORM.setupCommonConfig(Constants.MOD_ID, ToolsConfig.Common.COMMON_CONFIG);

        Services.EVENTS.registerEvent(AnvilUpdatedEvent.class, (final AnvilUpdatedEvent event) -> ChickenSuitConversionHandler.anvilUpdateEvent(event));
        Services.EVENTS.registerEvent(EntityInteractEvent.class, (final EntityInteractEvent event) -> MilkingHandler.interact(event));
    }
}
