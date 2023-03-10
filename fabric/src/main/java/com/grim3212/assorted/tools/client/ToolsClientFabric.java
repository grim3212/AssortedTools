package com.grim3212.assorted.tools.client;

import net.fabricmc.api.ClientModInitializer;

public class ToolsClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ToolsClient.init();
    }

}
