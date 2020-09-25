package com.grim3212.assorted.tools.common.network;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

public class ChickenSuitUpdatePacket {

	private final int glideJumps;

	public ChickenSuitUpdatePacket() {
		this.glideJumps = -1;
	}

	public ChickenSuitUpdatePacket(int glideJumps) {
		this.glideJumps = glideJumps;
	}

	public static ChickenSuitUpdatePacket decode(PacketBuffer buf) {
		return new ChickenSuitUpdatePacket(buf.readInt());
	}

	public void encode(PacketBuffer buf) {
		buf.writeInt(this.glideJumps);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
			ctx.get().enqueueWork(() -> {
				PlayerEntity player = ctx.get().getSender();

				if (this.glideJumps != -1) {
					double d = -0.14999999999999999D - 0.14999999999999999D * (1.0D - (double) glideJumps / 5D);
					Vector3d mot = player.getMotion();

					if (mot.y < d) {
						player.setMotion(mot.x, d, mot.z);
					}
					player.fallDistance = 0.0F;
				} else {
					player.jump();

					// Calculate fall distance so the double jumps do not hurt the
					// player
					player.fallDistance = 0.0f;
				}

			});
			ctx.get().setPacketHandled(true);
		}
	}

}
