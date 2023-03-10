package com.grim3212.assorted.tools.client.handlers;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.tools.client.ToolsClient;
import com.grim3212.assorted.tools.common.item.WandItem;
import com.grim3212.assorted.tools.common.network.ToolCycleModesPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class KeyBindHandler {

    private int switchCooldown = 0;

    public void tick() {
        if (switchCooldown > 0)
            --switchCooldown;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (mc.isWindowActive()) {
            if (player != null) {
                if (ToolsClient.TOOL_SWITCH_MODES.consumeClick()) {
                    if (switchCooldown == 0) {
                        if (player.getMainHandItem() != null) {
                            if (player.getMainHandItem().getItem() instanceof WandItem) {
                                switchCooldown = 20;
                                Services.NETWORK.sendToServer(new ToolCycleModesPacket(InteractionHand.MAIN_HAND));
                            }
                        }

                        if (player.getOffhandItem() != null) {
                            if (player.getOffhandItem().getItem() instanceof WandItem) {
                                switchCooldown = 20;
                                Services.NETWORK.sendToServer(new ToolCycleModesPacket(InteractionHand.OFF_HAND));
                            }
                        }
                    }
                }
            }
        }
    }

}
