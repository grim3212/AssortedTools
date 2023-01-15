package com.grim3212.assorted.tools.client.render.entity;

import com.grim3212.assorted.tools.client.render.model.SpearModel;
import com.grim3212.assorted.tools.client.render.model.ToolsModelLayers;
import com.grim3212.assorted.tools.common.entity.BetterSpearEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class BetterSpearRenderer extends EntityRenderer<BetterSpearEntity> {

	private final SpearModel spearModel;

	public BetterSpearRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.spearModel = new SpearModel(context.getModelSet().bakeLayer(ToolsModelLayers.SPEAR));
	}

	public void render(BetterSpearEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource renderType, int light) {
		stack.pushPose();

		stack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
		stack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) + 90.0F));
		VertexConsumer ivertexbuilder = ItemRenderer.getFoilBufferDirect(renderType, this.spearModel.renderType(this.getTextureLocation(entity)), false, entity.isFoil());
		this.spearModel.renderToBuffer(stack, ivertexbuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		stack.popPose();
		super.render(entity, entityYaw, partialTicks, stack, renderType, light);
	}

	public ResourceLocation getTextureLocation(BetterSpearEntity entity) {
		return spearModel.getTexture(entity.getSpearItem().getItem());
	}

}
