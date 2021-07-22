package com.grim3212.assorted.tools.client.render.model;

import java.util.HashMap;
import java.util.Map;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.item.BetterSpearItem;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpearModel extends Model {
	private final ModelRenderer pole = new ModelRenderer(32, 32, 0, 6);

	public SpearModel() {
		super(RenderType::entitySolid);
		this.pole.addBox(-0.5F, 2F, -0.5F, 1.0F, 25.0F, 1.0F, 0.0F);
		ModelRenderer modelrenderer = new ModelRenderer(32, 32, 4, 0);
		modelrenderer.addBox(-0.5F, -5F, -0.5F, 1.0F, 7.0F, 1.0F);
		this.pole.addChild(modelrenderer);
		ModelRenderer modelrenderer1 = new ModelRenderer(32, 32, 4, 25);
		modelrenderer1.addBox(-1.5F, -4F, -0.5F, 1.0F, 6.0F, 1.0F, 0.0F);
		this.pole.addChild(modelrenderer1);
		ModelRenderer modelrenderer2 = new ModelRenderer(32, 32, 8, 25);
		modelrenderer2.addBox(0.5F, -4F, -0.5F, 1.0F, 6.0F, 1.0F);
		this.pole.addChild(modelrenderer2);
	}

	@Override
	public void renderToBuffer(MatrixStack stack, IVertexBuilder buffer, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
		this.pole.render(stack, buffer, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
	}

	private static final ResourceLocation WOOD_SPEAR = new ResourceLocation(AssortedTools.MODID, "textures/entity/projectiles/wood_spear.png");

	protected final Map<ResourceLocation, ResourceLocation> cache = new HashMap<ResourceLocation, ResourceLocation>();

	public ResourceLocation getTexture(Item item) {
		if (!this.cache.containsKey(item.getRegistryName())) {
			if (item instanceof BetterSpearItem) {
				BetterSpearItem spear = (BetterSpearItem) item;
				this.cache.put(spear.getRegistryName(), new ResourceLocation(AssortedTools.MODID, "textures/entity/projectiles/" + spear.getTierHolder().getName() + "_spear.png"));
			} else {
				AssortedTools.LOGGER.error("Tried to get spear texture for non-spear item");
				return WOOD_SPEAR;
			}
		}

		return this.cache.get(item.getRegistryName());
	}
}