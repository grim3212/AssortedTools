package com.grim3212.assorted.tools.client.render;

import com.grim3212.assorted.tools.Constants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

import java.util.function.Predicate;

/**
 * Draws a frozen mob's own model again in ice, as GrimPack did. The ice texture is opaque and the
 * copy has the same geometry, so it covers the mob's skin the way vanilla's eye layers cover theirs.
 * Each loader says which render states are frozen; see {@code IFrozenStorage}.
 */
public class FrozenLayer<S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends RenderLayer<S, M> {

    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/frozen.png");

    private final Predicate<S> frozen;

    public FrozenLayer(RenderLayerParent<S, M> renderer, Predicate<S> frozen) {
        super(renderer);
        this.frozen = frozen;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
        if (this.frozen.test(state)) {
            coloredCutoutModelCopyLayerRender(this.getParentModel(), TEXTURE, poseStack, submitNodeCollector, lightCoords, state, -1, 1);
        }
    }
}
