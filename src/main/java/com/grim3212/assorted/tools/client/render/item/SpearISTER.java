package com.grim3212.assorted.tools.client.render.item;

import javax.annotation.Nonnull;

import com.grim3212.assorted.tools.client.render.model.SpearModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class SpearISTER extends ItemStackTileEntityRenderer {

	private final SpearModel spearModel = new SpearModel();

	@Override
	public void renderByItem(@Nonnull ItemStack stack, @Nonnull TransformType transformType, @Nonnull MatrixStack matrix, @Nonnull IRenderTypeBuffer renderer, int light, int overlayLight) {
		matrix.pushPose();
		matrix.scale(1, -1, -1);
		IVertexBuilder builder = ItemRenderer.getFoilBufferDirect(renderer, spearModel.renderType(getTexture(stack)), false, stack.hasFoil());
		spearModel.renderToBuffer(matrix, builder, light, overlayLight, 1, 1, 1, 1);
		matrix.popPose();
	}

	private ResourceLocation getTexture(ItemStack stack) {
		return spearModel.getTexture(stack.getItem());
	}
}
