package com.grim3212.assorted.tools;

import com.grim3212.assorted.tools.client.ToolsClient;
import net.fabricmc.api.ClientModInitializer;

public class AssortedToolsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ToolsClient.init();
    }

}
