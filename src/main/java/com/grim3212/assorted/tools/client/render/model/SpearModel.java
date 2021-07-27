package com.grim3212.assorted.tools.client.render.model;

import java.util.HashMap;
import java.util.Map;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.item.BetterSpearItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpearModel extends Model {
	private final ModelPart root;

	public SpearModel(ModelPart root) {
		super(RenderType::entitySolid);
		this.root = root;
	}

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition partdefinition1 = partdefinition.addOrReplaceChild("pole", CubeListBuilder.create().texOffs(0, 6).addBox(-0.5F, 2F, -0.5F, 1.0F, 25.0F, 1.0F), PartPose.ZERO);
		partdefinition1.addOrReplaceChild("left_point", CubeListBuilder.create().texOffs(4, 0).addBox(-0.5F, -5F, -0.5F, 1.0F, 7.0F, 1.0F), PartPose.ZERO);
		partdefinition1.addOrReplaceChild("middle_point", CubeListBuilder.create().texOffs(4, 25).addBox(-1.5F, -4F, -0.5F, 1.0F, 6.0F, 1.0F), PartPose.ZERO);
		partdefinition1.addOrReplaceChild("right_point", CubeListBuilder.create().texOffs(8, 25).addBox(0.5F, -4F, -0.5F, 1.0F, 6.0F, 1.0F), PartPose.ZERO);
		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer buffer, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
		this.root.render(stack, buffer, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
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