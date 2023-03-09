package com.grim3212.assorted.decor.common.network;

import com.grim3212.assorted.decor.Constants;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.platform.services.INetworkHelper;
import net.minecraft.resources.ResourceLocation;

public class ToolsPackets {

    public static void init() {
        Services.NETWORK.register(new INetworkHelper.MessageHandler<>(resource("chicken_suit_jump"), ChickenSuitUpdatePacket.class, ChickenSuitUpdatePacket::encode, ChickenSuitUpdatePacket::decode, ChickenSuitUpdatePacket::handle, INetworkHelper.MessageBoundSide.SERVER));
        Services.NETWORK.register(new INetworkHelper.MessageHandler<>(resource("tools_cycle_mode"), ToolCycleModesPacket.class, ToolCycleModesPacket::encode, ToolCycleModesPacket::decode, ToolCycleModesPacket::handle, INetworkHelper.MessageBoundSide.SERVER));
    }

    private static ResourceLocation resource(String name) {
        return new ResourceLocation(Constants.MOD_ID, name);
    }

}
