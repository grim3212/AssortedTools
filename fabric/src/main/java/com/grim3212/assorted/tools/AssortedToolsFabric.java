package com.grim3212.assorted.tools;

import net.fabricmc.api.ModInitializer;

public class AssortedToolsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ToolsCommonMod.init();
    }
}
