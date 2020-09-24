package com.grim3212.assorted.tools.common.network;

import java.util.function.Supplier;

import com.grim3212.assorted.tools.common.item.ISwitchModes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

public class ToolCycleModesPacket {

	private final Hand hand;

	public ToolCycleModesPacket(Hand hand) {
		this.hand = hand;
	}

	public static ToolCycleModesPacket decode(PacketBuffer buf) {
		return new ToolCycleModesPacket(buf.readEnumValue(Hand.class));
	}

	public void encode(PacketBuffer buf) {
		buf.writeEnumValue(this.hand);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
			ctx.get().enqueueWork(() -> {
				PlayerEntity player = ctx.get().getSender();

				if (player.getHeldItem(this.hand).getItem() instanceof ISwitchModes) {
					ItemStack stack = player.getHeldItem(this.hand);
					ISwitchModes item = (ISwitchModes) stack.getItem();
					player.setHeldItem(this.hand, item.cycleMode(player, stack));
				}

			});
			ctx.get().setPacketHandled(true);
		}
	}

}
