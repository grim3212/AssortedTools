package com.grim3212.assorted.tools.client;

import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.client.handlers.ChickenJumpHandler;
import com.grim3212.assorted.tools.client.handlers.KeyBindHandler;
import com.grim3212.assorted.tools.client.render.entity.BetterSpearRenderer;
import com.grim3212.assorted.tools.client.render.entity.BoomerangRenderer;
import com.grim3212.assorted.tools.client.render.model.SpearModel;
import com.grim3212.assorted.tools.client.render.model.ToolsModelLayers;
import com.grim3212.assorted.tools.common.entity.ToolsEntities;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;

public class ToolsClient {

    public static KeyMapping TOOL_SWITCH_MODES;

    public static void init() {
        TOOL_SWITCH_MODES = ClientServices.KEYBINDS.createNew("key.assortedtools.tool_switch_modes", ClientServices.KEYBINDS.getInGameKeyConflictContext(), InputConstants.Type.KEYSYM, InputConstants.KEY_Z, Constants.MOD_NAME);
        ClientServices.CLIENT.registerKeyMapping(TOOL_SWITCH_MODES);

        ClientServices.CLIENT.registerClientTickStart(KeyBindHandler::tick);
        ClientServices.CLIENT.registerClientTickEnd(ChickenJumpHandler::tick);

        ClampedItemPropertyFunction override = (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
        ClientServices.CLIENT.registerItemProperty(() -> ToolsItems.WOOD_SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);
        ClientServices.CLIENT.registerItemProperty(() -> ToolsItems.STONE_SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);
        ClientServices.CLIENT.registerItemProperty(() -> ToolsItems.IRON_SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);
        ClientServices.CLIENT.registerItemProperty(() -> ToolsItems.GOLD_SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);
        ClientServices.CLIENT.registerItemProperty(() -> ToolsItems.DIAMOND_SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);
        ClientServices.CLIENT.registerItemProperty(() -> ToolsItems.NETHERITE_SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);

        ToolsItems.MATERIAL_GROUPS.forEach((s, group) -> {
            ClientServices.CLIENT.registerItemProperty(() -> group.SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);
        });

        ClientServices.CLIENT.registerEntityRenderer(() -> ToolsEntities.WOOD_BOOMERANG.get(), BoomerangRenderer::new);
        ClientServices.CLIENT.registerEntityRenderer(() -> ToolsEntities.DIAMOND_BOOMERANG.get(), BoomerangRenderer::new);
        ClientServices.CLIENT.registerEntityRenderer(() -> ToolsEntities.POKEBALL.get(), ThrownItemRenderer::new);
        ClientServices.CLIENT.registerEntityRenderer(() -> ToolsEntities.BETTER_SPEAR.get(), BetterSpearRenderer::new);

        ClientServices.CLIENT.registerEntityLayer(ToolsModelLayers.SPEAR, SpearModel::createLayer);

        ClientServices.CLIENT.registerItemColor((stack, tintIndex) -> {
            if (tintIndex != 1) return 0xFFFFFFFF;
            return Services.FLUIDS.get(stack).map(x -> Services.FLUIDS.getFluidColor(x)).orElse(0xFFFFFFFF);
        }, () -> ToolsItems.buckets());
    }

}
