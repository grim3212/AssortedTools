package com.grim3212.assorted.tools;

import com.grim3212.assorted.lib.events.AnvilUpdatedEvent;
import com.grim3212.assorted.lib.events.EntityInteractEvent;
import com.grim3212.assorted.lib.events.RegisterCreativeTabEvent;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.tools.common.crafting.ToolsConditions;
import com.grim3212.assorted.tools.common.enchantment.ToolsEnchantments;
import com.grim3212.assorted.tools.common.entity.ToolsEntities;
import com.grim3212.assorted.tools.common.handlers.ChickenSuitConversionHandler;
import com.grim3212.assorted.tools.common.handlers.MilkingHandler;
import com.grim3212.assorted.tools.common.handlers.ToolsCreativeItems;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.grim3212.assorted.tools.common.network.ToolsPackets;
import com.grim3212.assorted.tools.config.ToolsConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

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
        Services.EVENTS.registerEvent(RegisterCreativeTabEvent.class, (final RegisterCreativeTabEvent event) -> {
            event.getCreator().create(new ResourceLocation(Constants.MOD_ID, "tab"), Component.translatable("itemGroup.assortedtools"), () -> new ItemStack(ToolsItems.IRON_HAMMER.get()), () -> ToolsCreativeItems.getCreativeItems());
        });
    }
}
