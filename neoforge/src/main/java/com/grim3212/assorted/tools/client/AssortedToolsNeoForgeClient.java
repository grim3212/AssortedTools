package com.grim3212.assorted.tools.client;

import com.grim3212.assorted.tools.Constants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

/**
 * The client-only entry point: a second {@code @Mod} for the same mod id, constructed only on the
 * client. Client datagen constructs it before the registration events fire, so the codecs the
 * generated item models name are registered by the time they are encoded.
 */
@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class AssortedToolsNeoForgeClient {

    public AssortedToolsNeoForgeClient(IEventBus modBus, ModContainer modContainer) {
        ToolsClient.init();
    }
}
