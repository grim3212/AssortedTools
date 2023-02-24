package com.grim3212.assorted.tools.client.render.item;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.grim3212.assorted.tools.client.render.model.SpearModel;
import com.grim3212.assorted.tools.client.render.model.ToolsModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class SpearBEWLR extends BlockEntityWithoutLevelRenderer {

	public static final BlockEntityWithoutLevelRenderer SPEAR_ITEM_RENDERER = new SpearBEWLR(() -> Minecraft.getInstance().getBlockEntityRenderDispatcher(), () -> Minecraft.getInstance().getEntityModels());

	private final SpearModel spearModel;

	public SpearBEWLR(Supplier<BlockEntityRenderDispatcher> dispatcher, Supplier<EntityModelSet> modelSet) {
		super(dispatcher.get(), modelSet.get());
		this.spearModel = new SpearModel(modelSet.get().bakeLayer(ToolsModelLayers.SPEAR));
	}

	@Override
	public void renderByItem(@Nonnull ItemStack stack, @Nonnull TransformType transformType, @Nonnull PoseStack matrix, @Nonnull MultiBufferSource renderer, int light, int overlayLight) {
		matrix.pushPose();
		matrix.scale(1, -1, -1);
		VertexConsumer builder = ItemRenderer.getFoilBufferDirect(renderer, spearModel.renderType(getTexture(stack)), false, stack.hasFoil());
		spearModel.renderToBuffer(matrix, builder, light, overlayLight, 1, 1, 1, 1);
		matrix.popPose();
	}

	private ResourceLocation getTexture(ItemStack stack) {
		return spearModel.getTexture(stack.getItem());
	}
}
