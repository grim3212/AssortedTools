package com.grim3212.assorted.tools.client.render.item;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.client.render.model.SpearModel;
import com.grim3212.assorted.tools.client.render.model.ToolsModelLayers;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class SpearBEWLR extends BlockEntityWithoutLevelRenderer {
    private SpearModel spearModel;
    private ItemRenderer itemRenderer;
    private Map<Item, BakedModel> inventorySpearModels;

    public SpearBEWLR() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        Minecraft mc = Minecraft.getInstance();
        ModelManager modelManager = mc.getModelManager();
        this.itemRenderer = mc.getItemRenderer();
        this.spearModel = new SpearModel(mc.getEntityModels().bakeLayer(ToolsModelLayers.SPEAR));
        this.inventorySpearModels = new HashMap<>();

        // Add all the current possible models
        this.inventorySpearModels.put(ToolsItems.WOOD_SPEAR.get(), modelManager.getModel(new ModelResourceLocation(new ResourceLocation(Constants.MOD_ID, "wood_spear_gui"), "inventory")));
        this.inventorySpearModels.put(ToolsItems.STONE_SPEAR.get(), modelManager.getModel(new ModelResourceLocation(new ResourceLocation(Constants.MOD_ID, "stone_spear_gui"), "inventory")));
        this.inventorySpearModels.put(ToolsItems.IRON_SPEAR.get(), modelManager.getModel(new ModelResourceLocation(new ResourceLocation(Constants.MOD_ID, "iron_spear_gui"), "inventory")));
        this.inventorySpearModels.put(ToolsItems.GOLD_SPEAR.get(), modelManager.getModel(new ModelResourceLocation(new ResourceLocation(Constants.MOD_ID, "gold_spear_gui"), "inventory")));
        this.inventorySpearModels.put(ToolsItems.DIAMOND_SPEAR.get(), modelManager.getModel(new ModelResourceLocation(new ResourceLocation(Constants.MOD_ID, "diamond_spear_gui"), "inventory")));
        this.inventorySpearModels.put(ToolsItems.NETHERITE_SPEAR.get(), modelManager.getModel(new ModelResourceLocation(new ResourceLocation(Constants.MOD_ID, "netherite_spear_gui"), "inventory")));

        ToolsItems.MATERIAL_GROUPS.forEach((s, group) -> {
            this.inventorySpearModels.put(group.SPEAR.get(), modelManager.getModel(new ModelResourceLocation(new ResourceLocation(Constants.MOD_ID, s + "_spear_gui"), "inventory")));
        });
    }

    @Override
    public void renderByItem(@Nonnull ItemStack stack, @Nonnull TransformType transformType, @Nonnull PoseStack matrix, @Nonnull MultiBufferSource renderer, int light, int overlayLight) {
        if (transformType == TransformType.GUI || transformType == TransformType.GROUND || transformType == TransformType.FIXED) {
            matrix.popPose();
            matrix.pushPose();
            this.itemRenderer.render(stack, transformType, false, matrix, renderer, light, overlayLight, inventorySpearModels.get(stack.getItem()));
        } else {
            matrix.pushPose();
            matrix.scale(1, -1, -1);
            VertexConsumer builder = ItemRenderer.getFoilBufferDirect(renderer, spearModel.renderType(getTexture(stack)), false, stack.hasFoil());
            spearModel.renderToBuffer(matrix, builder, light, overlayLight, 1, 1, 1, 1);
            matrix.popPose();
        }
    }

    private ResourceLocation getTexture(ItemStack stack) {
        return spearModel.getTexture(stack.getItem());
    }

    private ResourceLocation key(Item item) {
        return Services.PLATFORM.getRegistry(Registries.ITEM).getRegistryName(item);
    }
}
