package com.grim3212.assorted.tools.client;

import com.google.common.reflect.TypeToken;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.client.render.FrozenLayer;
import com.grim3212.assorted.tools.common.item.FrozenMobs;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
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

    /** Filled in for every living entity as it is extracted, for {@link FrozenLayer} to read. */
    private static final ContextKey<Boolean> FROZEN = new ContextKey<>(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "frozen"));

    public AssortedToolsNeoForgeClient(IEventBus modBus, ModContainer modContainer) {
        ToolsClient.init();

        modBus.addListener(AssortedToolsNeoForgeClient::registerRenderStateModifiers);
        modBus.addListener(AssortedToolsNeoForgeClient::addLayers);
    }

    private static void registerRenderStateModifiers(RegisterRenderStateModifiersEvent event) {
        event.<LivingEntity, LivingEntityRenderState>registerEntityModifier(new TypeToken<LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?>>() {
        }, (entity, state) -> state.setRenderData(FROZEN, FrozenMobs.isFrozen(entity)));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addLayers(EntityRenderersEvent.AddLayers event) {
        for (EntityType<?> type : event.getEntityTypes()) {
            if (event.getRenderer(type) instanceof LivingEntityRenderer renderer) {
                renderer.addLayer(new FrozenLayer<>(renderer, state -> ((LivingEntityRenderState) state).getRenderDataOrDefault(FROZEN, false)));
            }
        }
    }
}
