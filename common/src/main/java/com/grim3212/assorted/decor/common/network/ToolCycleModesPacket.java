package com.grim3212.assorted.decor.common.network;

import com.grim3212.assorted.decor.api.item.ISwitchModes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ToolCycleModesPacket {

    private final InteractionHand hand;

    public ToolCycleModesPacket(InteractionHand hand) {
        this.hand = hand;
    }

    public static ToolCycleModesPacket decode(FriendlyByteBuf buf) {
        return new ToolCycleModesPacket(buf.readEnum(InteractionHand.class));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(this.hand);
    }

    public static void handle(ToolCycleModesPacket packet, Player player) {
        if (player.getItemInHand(packet.hand).getItem() instanceof ISwitchModes) {
            ItemStack stack = player.getItemInHand(packet.hand);
            ISwitchModes item = (ISwitchModes) stack.getItem();
            player.setItemInHand(packet.hand, item.cycleMode(player, stack));
        }
    }

}
