package com.grim3212.assorted.tools.client.render.entity;

import com.grim3212.assorted.tools.common.entity.BoomerangEntity;
import com.grim3212.assorted.tools.common.entity.ToolsEntities;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * Draws a boomerang. The item is resolved into an {@link ItemStackRenderState} at extract time, and
 * the yaw and pitch travel on the state, since the submit pass has no entity.
 */
public class BoomerangRenderer extends EntityRenderer<BoomerangEntity, BoomerangRenderer.BoomerangRenderState> {

    private final ItemModelResolver itemModelResolver;

    public BoomerangRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemModelResolver = context.getItemModelResolver();
        this.shadowRadius = 0.15F;
        this.shadowStrength = 0.75F;
    }

    @Override
    public BoomerangRenderState createRenderState() {
        return new BoomerangRenderState();
    }

    @Override
    public void extractRenderState(BoomerangEntity entity, BoomerangRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);

        state.yRot = entity.getYRot(partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.boomerangRotation = entity.getBoomerangRotation();

        this.itemModelResolver.updateForNonLiving(state.item, getItemStackForRender(entity), ItemDisplayContext.GROUND, entity);
    }

    @Override
    public void submit(BoomerangRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-state.yRot + 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot + 90.0F));
        poseStack.mulPose(Axis.YN.rotationDegrees(90.0F));
        poseStack.mulPose(Axis.ZN.rotationDegrees(state.boomerangRotation));
        state.item.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();

        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    private ItemStack getItemStackForRender(BoomerangEntity entityIn) {
        if (entityIn.getType() == ToolsEntities.DIAMOND_BOOMERANG.get()) {
            return new ItemStack(ToolsItems.DIAMOND_BOOMERANG.get());
        }
        return new ItemStack(ToolsItems.WOOD_BOOMERANG.get());
    }

    public static class BoomerangRenderState extends EntityRenderState {
        public final ItemStackRenderState item = new ItemStackRenderState();
        public float yRot;
        public float xRot;
        public float boomerangRotation;
    }
}
