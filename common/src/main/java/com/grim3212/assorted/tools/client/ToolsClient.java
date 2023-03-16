package com.grim3212.assorted.tools.client;

import com.google.common.collect.Lists;
import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.client.handlers.ChickenJumpHandler;
import com.grim3212.assorted.tools.client.handlers.KeyBindHandler;
import com.grim3212.assorted.tools.client.render.entity.BetterSpearRenderer;
import com.grim3212.assorted.tools.client.render.entity.BoomerangRenderer;
import com.grim3212.assorted.tools.client.render.item.SpearBEWLR;
import com.grim3212.assorted.tools.client.render.model.SpearModel;
import com.grim3212.assorted.tools.client.render.model.ToolsModelLayers;
import com.grim3212.assorted.tools.common.entity.ToolsEntities;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ToolsClient {

    public static KeyMapping TOOL_SWITCH_MODES;

    public static void init() {
        TOOL_SWITCH_MODES = ClientServices.KEYBINDS.createNew("key.assortedtools.tool_switch_modes", ClientServices.KEYBINDS.getInGameKeyConflictContext(), InputConstants.Type.KEYSYM, InputConstants.KEY_Z, Constants.MOD_NAME);
        ClientServices.CLIENT.registerKeyMapping(TOOL_SWITCH_MODES);

        ClientServices.CLIENT.registerClientTickStart(KeyBindHandler::tick);
        ClientServices.CLIENT.registerClientTickEnd(ChickenJumpHandler::tick);

        ClientServices.CLIENT.registerEntityLayer(ToolsModelLayers.SPEAR, SpearModel::createLayer);

        SpearBEWLR spear = new SpearBEWLR();
        ClientServices.CLIENT.addReloadListener(new ResourceLocation(Constants.MOD_ID, "spear_hand_renderer"), spear);

        ClientServices.CLIENT.registerBEWLR((register) -> {
            register.registerBlockEntityWithoutLevelRenderer(ToolsItems.WOOD_SPEAR.get(), spear);
            register.registerBlockEntityWithoutLevelRenderer(ToolsItems.STONE_SPEAR.get(), spear);
            register.registerBlockEntityWithoutLevelRenderer(ToolsItems.IRON_SPEAR.get(), spear);
            register.registerBlockEntityWithoutLevelRenderer(ToolsItems.GOLD_SPEAR.get(), spear);
            register.registerBlockEntityWithoutLevelRenderer(ToolsItems.DIAMOND_SPEAR.get(), spear);
            register.registerBlockEntityWithoutLevelRenderer(ToolsItems.NETHERITE_SPEAR.get(), spear);

            ToolsItems.MATERIAL_GROUPS.forEach((s, group) -> {
                register.registerBlockEntityWithoutLevelRenderer(group.SPEAR.get(), spear);
            });
        });

        ClampedItemPropertyFunction override = (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
        List<ResourceLocation> extraModels = Lists.newArrayList(
                new ModelResourceLocation(new ResourceLocation(Constants.MOD_ID, "wood_spear_gui"), "inventory"),
                new ModelResourceLocation(new ResourceLocation(Constants.MOD_ID, "stone_spear_gui"), "inventory"),
                new ModelResourceLocation(new ResourceLocation(Constants.MOD_ID, "iron_spear_gui"), "inventory"),
                new ModelResourceLocation(new ResourceLocation(Constants.MOD_ID, "gold_spear_gui"), "inventory"),
                new ModelResourceLocation(new ResourceLocation(Constants.MOD_ID, "diamond_spear_gui"), "inventory"),
                new ModelResourceLocation(new ResourceLocation(Constants.MOD_ID, "netherite_spear_gui"), "inventory"));
        ClientServices.CLIENT.registerItemProperty(() -> ToolsItems.WOOD_SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);
        ClientServices.CLIENT.registerItemProperty(() -> ToolsItems.STONE_SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);
        ClientServices.CLIENT.registerItemProperty(() -> ToolsItems.IRON_SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);
        ClientServices.CLIENT.registerItemProperty(() -> ToolsItems.GOLD_SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);
        ClientServices.CLIENT.registerItemProperty(() -> ToolsItems.DIAMOND_SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);
        ClientServices.CLIENT.registerItemProperty(() -> ToolsItems.NETHERITE_SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);

        ToolsItems.MATERIAL_GROUPS.forEach((s, group) -> {
            ClientServices.CLIENT.registerItemProperty(() -> group.SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);
            extraModels.add(new ModelResourceLocation(new ResourceLocation(Constants.MOD_ID, s + "_spear_gui"), "inventory"));
        });

        ClientServices.CLIENT.registerAdditionalModel(extraModels);

        ClientServices.CLIENT.registerEntityRenderer(() -> ToolsEntities.WOOD_BOOMERANG.get(), BoomerangRenderer::new);
        ClientServices.CLIENT.registerEntityRenderer(() -> ToolsEntities.DIAMOND_BOOMERANG.get(), BoomerangRenderer::new);
        ClientServices.CLIENT.registerEntityRenderer(() -> ToolsEntities.POKEBALL.get(), ThrownItemRenderer::new);
        ClientServices.CLIENT.registerEntityRenderer(() -> ToolsEntities.BETTER_SPEAR.get(), BetterSpearRenderer::new);

        ClientServices.CLIENT.registerItemColor((stack, tintIndex) -> {
            if (tintIndex != 1) return 0xFFFFFFFF;
            return Services.FLUIDS.get(stack).map(x -> Services.FLUIDS.getFluidColor(x)).orElse(0xFFFFFFFF);
        }, () -> ToolsItems.buckets());
    }

}
