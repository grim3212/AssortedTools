package com.grim3212.assorted.tools.client.render.entity;

import com.grim3212.assorted.decor.common.entity.BoomerangEntity;
import com.grim3212.assorted.decor.common.entity.ToolsEntities;
import com.grim3212.assorted.decor.common.item.ToolsItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class BoomerangRenderer extends EntityRenderer<BoomerangEntity> {

    private final ItemRenderer itemRenderer;

    public BoomerangRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.shadowRadius = 0.15F;
        this.shadowStrength = 0.75F;
    }

    @Override
    public void render(BoomerangEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(-entityYaw + 90.0f));
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot()) + 90.0F));
        matrixStackIn.mulPose(Axis.YN.rotationDegrees(90.0f));
        matrixStackIn.mulPose(Axis.ZN.rotationDegrees(entityIn.getBoomerangRotation()));
        this.itemRenderer.renderStatic(getItemStackForRender(entityIn), ItemTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, 0);
        matrixStackIn.popPose();

        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private ItemStack getItemStackForRender(BoomerangEntity entityIn) {
        if (entityIn.getType() == ToolsEntities.DIAMOND_BOOMERANG.get()) {
            return new ItemStack(ToolsItems.DIAMOND_BOOMERANG.get());
        }
        return new ItemStack(ToolsItems.WOOD_BOOMERANG.get());
    }

    @Override
    public ResourceLocation getTextureLocation(BoomerangEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
