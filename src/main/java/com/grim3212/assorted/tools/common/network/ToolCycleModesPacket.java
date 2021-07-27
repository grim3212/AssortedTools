package com.grim3212.assorted.tools.common.network;

import java.util.function.Supplier;

import com.grim3212.assorted.tools.api.item.ISwitchModes;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

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

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
			ctx.get().enqueueWork(() -> {
				Player player = ctx.get().getSender();

				if (player.getItemInHand(this.hand).getItem() instanceof ISwitchModes) {
					ItemStack stack = player.getItemInHand(this.hand);
					ISwitchModes item = (ISwitchModes) stack.getItem();
					player.setItemInHand(this.hand, item.cycleMode(player, stack));
				}

			});
			ctx.get().setPacketHandled(true);
		}
	}

}
