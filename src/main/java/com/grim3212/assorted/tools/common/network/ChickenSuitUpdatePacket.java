package com.grim3212.assorted.tools.common.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

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

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
			ctx.get().enqueueWork(() -> {
				Player player = ctx.get().getSender();

				if (this.glideJumps != -1) {
					double d = -0.14999999999999999D - 0.14999999999999999D * (1.0D - (double) glideJumps / 5D);
					Vec3 mot = player.getDeltaMovement();

					if (mot.y < d) {
						player.setDeltaMovement(mot.x, d, mot.z);
					}
					player.fallDistance = 0.0F;
				} else {
					player.jumpFromGround();

					// Calculate fall distance so the double jumps do not hurt the
					// player
					player.fallDistance = 0.0f;
				}

			});
			ctx.get().setPacketHandled(true);
		}
	}

}
