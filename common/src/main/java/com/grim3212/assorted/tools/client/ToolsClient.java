package com.grim3212.assorted.tools.client;

import com.grim3212.assorted.lib.client.events.*;
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
    private static KeyBindHandler keyBindHandler = new KeyBindHandler();
    private static ChickenJumpHandler chickenJumpHandler = new ChickenJumpHandler();

    public static void init() {
        TOOL_SWITCH_MODES = ClientServices.KEYBINDS.createNew("key.assortedtools.tool_switch_modes", ClientServices.KEYBINDS.getInGameKeyConflictContext(), InputConstants.Type.KEYSYM, InputConstants.KEY_Z, Constants.MOD_NAME);
        Services.EVENTS.registerEvent(RegisterKeyBindEvent.class, (final RegisterKeyBindEvent event) -> {
            event.register(TOOL_SWITCH_MODES);
        });

        Services.EVENTS.registerEvent(ClientTickEvent.StartClientTickEvent.class, (final ClientTickEvent.StartClientTickEvent event) -> {
            keyBindHandler.tick();
        });

        Services.EVENTS.registerEvent(ClientTickEvent.EndClientTickEvent.class, (final ClientTickEvent.EndClientTickEvent event) -> {
            chickenJumpHandler.tick();
        });

        Services.EVENTS.registerEvent(ClientSetupEvent.class, (final ClientSetupEvent event) -> {
            ClampedItemPropertyFunction override = (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;

            event.registerItemProperty(ToolsItems.WOOD_SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);
            event.registerItemProperty(ToolsItems.STONE_SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);
            event.registerItemProperty(ToolsItems.IRON_SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);
            event.registerItemProperty(ToolsItems.GOLD_SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);
            event.registerItemProperty(ToolsItems.DIAMOND_SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);
            event.registerItemProperty(ToolsItems.NETHERITE_SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);

            ToolsItems.MATERIAL_GROUPS.forEach((s, group) -> {
                event.registerItemProperty(group.SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);
            });
        });

        Services.EVENTS.registerEvent(RegisterRenderersEvent.class, (final RegisterRenderersEvent event) -> {
            event.registerEntityRenderer(ToolsEntities.WOOD_BOOMERANG.get(), BoomerangRenderer::new);
            event.registerEntityRenderer(ToolsEntities.DIAMOND_BOOMERANG.get(), BoomerangRenderer::new);
            event.registerEntityRenderer(ToolsEntities.POKEBALL.get(), ThrownItemRenderer::new);
            event.registerEntityRenderer(ToolsEntities.BETTER_SPEAR.get(), BetterSpearRenderer::new);
        });

        Services.EVENTS.registerEvent(RegisterEntityLayersEvent.class, (final RegisterEntityLayersEvent event) -> {
            event.register(ToolsModelLayers.SPEAR, SpearModel::createLayer);
        });

        Services.EVENTS.registerEvent(RegisterItemColorEvent.class, (final RegisterItemColorEvent event) -> {
            event.register((stack, tintIndex) -> {
                if (tintIndex != 1) return 0xFFFFFFFF;
                return Services.FLUIDS.get(stack).map(x -> Services.FLUIDS.getFluidColor(x)).orElse(0xFFFFFFFF);
            }, ToolsItems.buckets());
        });
    }

}
