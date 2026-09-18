package com.grim3212.assorted.tools.client;

import com.grim3212.assorted.tools.client.render.FrozenLayer;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

/** Draws frozen mobs as ice; the flag is put on the render state by {@code LivingEntityRendererMixin}. */
public final class FabricFrozenClient {

    public static final RenderStateDataKey<Boolean> FROZEN = RenderStateDataKey.create(() -> "assortedtools:frozen");

    private FabricFrozenClient() {
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void init() {
        LivingEntityRenderLayerRegistrationCallback.EVENT.register((type, renderer, helper, context) ->
                helper.register(new FrozenLayer(renderer, state -> ((LivingEntityRenderState) state).getDataOrDefault(FROZEN, false))));
    }
}
