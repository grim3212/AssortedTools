package com.grim3212.assorted.tools.client.render.entity;

import com.grim3212.assorted.tools.AssortedTools;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class SpearRenderer extends ArrowRenderer<AbstractArrowEntity> {

	public static final ResourceLocation NORMAL_SPEAR_LOCATION = new ResourceLocation(AssortedTools.MODID, "textures/entity/projectiles/spears.png");

	public SpearRenderer(EntityRendererManager manager) {
		super(manager);
	}

	@Override
	public ResourceLocation getTextureLocation(AbstractArrowEntity p_110775_1_) {
		return NORMAL_SPEAR_LOCATION;
	}

	public static class Factory implements IRenderFactory<AbstractArrowEntity> {
		@Override
		public EntityRenderer<? super AbstractArrowEntity> createRenderFor(EntityRendererManager manager) {
			return new SpearRenderer(manager);
		}

	}
}
