package com.grim3212.assorted.tools.client.render.entity;

import com.grim3212.assorted.tools.client.render.model.SpearModel;
import com.grim3212.assorted.tools.client.render.model.ToolsModelLayers;
import com.grim3212.assorted.tools.common.entity.BetterSpearEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;

/**
 * Split into an extract pass and a submit pass like every other {@code EntityRenderer} in 26.2. The
 * texture depends on the spear's item, which the submit pass has no entity to ask, so it is resolved
 * into the render state up front.
 * <p>
 * {@code ItemRenderer#getFoilBufferDirect} is gone: the glint is a second submit of the same model
 * against {@link RenderTypes#entityGlint()}, ordered after the solid pass, which is how vanilla's
 * {@code ThrownTridentRenderer} draws an enchanted trident.
 */
public class BetterSpearRenderer extends EntityRenderer<BetterSpearEntity, BetterSpearRenderer.SpearRenderState> {

    private final SpearModel spearModel;

    public BetterSpearRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.spearModel = new SpearModel(context.bakeLayer(ToolsModelLayers.SPEAR));
    }

    @Override
    public SpearRenderState createRenderState() {
        return new SpearRenderState();
    }

    @Override
    public void extractRenderState(BetterSpearEntity entity, SpearRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);

        state.yRot = entity.getYRot(partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.isFoil = entity.isFoil();
        state.texture = this.spearModel.getTexture(entity.getSpearStack().getItem());
    }

    @Override
    public void submit(SpearRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot + 90.0F));

        submitNodeCollector.order(0).submitModel(this.spearModel, Unit.INSTANCE, poseStack, state.texture, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        if (state.isFoil) {
            submitNodeCollector.order(1).submitModel(this.spearModel, Unit.INSTANCE, poseStack, RenderTypes.entityGlint(), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        }

        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    public static class SpearRenderState extends EntityRenderState {
        public float yRot;
        public float xRot;
        public boolean isFoil;
        public Identifier texture = SpearModel.DEFAULT_TEXTURE;
    }
}
