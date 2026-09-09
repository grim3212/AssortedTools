package com.grim3212.assorted.tools.client;

import com.grim3212.assorted.tools.Constants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

/**
 * {@code @Mod.EventBusSubscriber} no longer nests under {@code @Mod} and no longer picks a bus, and
 * hanging client setup off {@code FMLConstructModEvent} is not the idiom any more: {@code @Mod}
 * takes a {@code dist} now, so a client-only entry point is simply a second {@code @Mod} class for
 * the same mod id whose constructor runs only on the client.
 * <p>
 * The timing matters for datagen as well as for the game. {@code ToolsClient.init()} is what queues
 * the {@code assortedtools:spear} special model renderer, the {@code assortedtools:fluid_container}
 * tint source and the model loader onto the mod bus, and the item models this mod generates name all
 * three by id. The client data generator constructs mods before it calls
 * {@code ClientBootstrap.bootstrap()} - which is what posts
 * {@code RegisterSpecialModelRendererEvent} and {@code RegisterColorHandlersEvent.ItemTintSources} -
 * so the codecs are in place by the time {@code ClientItem.CODEC} has to encode them.
 */
@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class AssortedToolsNeoForgeClient {

    public AssortedToolsNeoForgeClient(IEventBus modBus, ModContainer modContainer) {
        ToolsClient.init();
    }
}
