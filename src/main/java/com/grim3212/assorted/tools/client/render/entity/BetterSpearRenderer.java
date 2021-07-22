package com.grim3212.assorted.tools.client.render.entity;

import com.grim3212.assorted.tools.client.render.model.SpearModel;
import com.grim3212.assorted.tools.common.entity.BetterSpearEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class BetterSpearRenderer extends EntityRenderer<BetterSpearEntity> {

	private final SpearModel spearModel = new SpearModel();

	public BetterSpearRenderer(EntityRendererManager renderManager) {
		super(renderManager);
	}

	public void render(BetterSpearEntity entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer renderType, int light) {
		stack.pushPose();
		stack.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entity.yRotO, entity.yRot) - 90.0F));
		stack.mulPose(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entity.xRotO, entity.xRot) + 90.0F));
		IVertexBuilder ivertexbuilder = ItemRenderer.getFoilBufferDirect(renderType, this.spearModel.renderType(this.getTextureLocation(entity)), false, entity.isFoil());
		this.spearModel.renderToBuffer(stack, ivertexbuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		stack.popPose();
		super.render(entity, entityYaw, partialTicks, stack, renderType, light);
	}

	public ResourceLocation getTextureLocation(BetterSpearEntity entity) {
		return spearModel.getTexture(entity.getSpearItem().getItem());
	}

}
