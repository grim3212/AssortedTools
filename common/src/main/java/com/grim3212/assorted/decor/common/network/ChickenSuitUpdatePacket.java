package com.grim3212.assorted.decor.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class ChickenSuitUpdatePacket {

    private final int glideJumps;

    public ChickenSuitUpdatePacket() {
        this.glideJumps = -1;
    }

    public ChickenSuitUpdatePacket(int glideJumps) {
        this.glideJumps = glideJumps;
    }

    public static ChickenSuitUpdatePacket decode(FriendlyByteBuf buf) {
        return new ChickenSuitUpdatePacket(buf.readInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.glideJumps);
    }

    public static void handle(ChickenSuitUpdatePacket packet, Player player) {
        if (packet.glideJumps != -1) {
            double d = -0.14999999999999999D - 0.14999999999999999D * (1.0D - (double) packet.glideJumps / 5D);
            Vec3 mot = player.getDeltaMovement();

            if (mot.y < d) {
                player.setDeltaMovement(mot.x, d, mot.z);
            }
        } else {
            player.jumpFromGround();
        }
        // Reset fall distance so the double jumps do not hurt the player
        player.fallDistance = 0.0F;
    }

}
