package com.grim3212.assorted.tools.client;

import com.grim3212.assorted.decor.client.ToolsClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ToolsClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ToolsClient.registerEntityRenderers(EntityRendererRegistry::register);
        ToolsClient.registerKeys();
    }

}
