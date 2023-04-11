package com.grim3212.assorted.tools.client;

import com.grim3212.assorted.tools.Constants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AssortedToolsForgeClient {

    @SubscribeEvent
    public static void initClientSide(final FMLConstructModEvent event) {
        ToolsClient.init();
    }
}
