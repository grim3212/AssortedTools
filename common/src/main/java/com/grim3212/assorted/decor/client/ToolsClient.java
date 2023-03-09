package com.grim3212.assorted.decor.client;

import com.grim3212.assorted.decor.Constants;
import com.grim3212.assorted.decor.client.handlers.ChickenJumpHandler;
import com.grim3212.assorted.decor.client.handlers.KeyBindHandler;
import com.grim3212.assorted.decor.client.render.entity.BetterSpearRenderer;
import com.grim3212.assorted.decor.client.render.entity.BoomerangRenderer;
import com.grim3212.assorted.decor.common.entity.ToolsEntities;
import com.grim3212.assorted.lib.client.render.entity.EntityRenderers;
import com.grim3212.assorted.lib.events.ClientTickEvent;
import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.lib.platform.Services;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class ToolsClient {

    public static KeyMapping TOOL_SWITCH_MODES;
    private static KeyBindHandler keyBindHandler = new KeyBindHandler();
    private static ChickenJumpHandler chickenJumpHandler = new ChickenJumpHandler();

    public static void registerKeys() {
        TOOL_SWITCH_MODES = ClientServices.KEYBINDS.createNew("key.assortedtools.tool_switch_modes", ClientServices.KEYBINDS.getInGameKeyConflictContext(), InputConstants.Type.KEYSYM, InputConstants.KEY_Z, Constants.MOD_NAME);
        ClientServices.KEYBINDS.register(TOOL_SWITCH_MODES);

        Services.EVENTS.registerEvent(ClientTickEvent.StartClientTickEvent.class, (final ClientTickEvent.StartClientTickEvent event) -> {
            keyBindHandler.tick();
        });

        Services.EVENTS.registerEvent(ClientTickEvent.EndClientTickEvent.class, (final ClientTickEvent.EndClientTickEvent event) -> {
            chickenJumpHandler.tick();
        });
    }

    public static void registerEntityRenderers(EntityRenderers.EntityRendererConsumer consumer) {
        consumer.accept(ToolsEntities.WOOD_BOOMERANG.get(), BoomerangRenderer::new);
        consumer.accept(ToolsEntities.DIAMOND_BOOMERANG.get(), BoomerangRenderer::new);
        consumer.accept(ToolsEntities.POKEBALL.get(), ThrownItemRenderer::new);
        consumer.accept(ToolsEntities.BETTER_SPEAR.get(), BetterSpearRenderer::new);
    }

}
