package com.grim3212.assorted.tools.client.handler;

import com.grim3212.assorted.decor.common.item.WandItem;
import com.grim3212.assorted.tools.client.proxy.ClientProxy;
import com.grim3212.assorted.tools.common.network.PacketHandler;
import com.grim3212.assorted.tools.common.network.ToolCycleModesPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KeyBindHandler {

    private int switchCooldown = 0;

    @SubscribeEvent
    public void tick(ClientTickEvent event) {
        if (switchCooldown > 0)
            --switchCooldown;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (mc.isWindowActive()) {
            if (player != null) {
                if (ClientProxy.TOOL_SWITCH_MODES.consumeClick()) {
                    if (switchCooldown == 0) {
                        if (player.getMainHandItem() != null) {
                            if (player.getMainHandItem().getItem() instanceof WandItem) {
                                switchCooldown = 20;
                                PacketHandler.sendToServer(new ToolCycleModesPacket(InteractionHand.MAIN_HAND));
                            }
                        }

                        if (player.getOffhandItem() != null) {
                            if (player.getOffhandItem().getItem() instanceof WandItem) {
                                switchCooldown = 20;
                                PacketHandler.sendToServer(new ToolCycleModesPacket(InteractionHand.OFF_HAND));
                            }
                        }
                    }
                }
            }
        }
    }

}
