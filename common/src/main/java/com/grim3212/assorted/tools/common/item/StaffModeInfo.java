package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.Constants;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;

/**
 * The mode line a staff shows in its tooltip, the staffs' {@link WandModeInfo}. The mode itself
 * stays in {@code custom_data}; this only says which staff's modes to read it as.
 */
public record StaffModeInfo(Kind kind) implements TooltipProvider {

    public static final Codec<StaffModeInfo> CODEC = Kind.CODEC.xmap(StaffModeInfo::new, StaffModeInfo::kind);
    public static final StreamCodec<ByteBuf, StaffModeInfo> STREAM_CODEC = Kind.STREAM_CODEC.map(StaffModeInfo::new, StaffModeInfo::kind);

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltip, TooltipFlag flag, DataComponentGetter components) {
        String stored = components.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getStringOr(StaffMode.KEY, "");
        tooltip.accept(Component.translatable(Constants.MOD_ID + ".staff.current", StaffMode.fromString(this.kind, stored).getTranslatedString()));
    }

    public enum Kind implements StringRepresentable {
        NEPTUNE("neptune"),
        PHOENIX("phoenix"),
        POWER("power");

        public static final Codec<Kind> CODEC = StringRepresentable.fromEnum(Kind::values);
        public static final StreamCodec<ByteBuf, Kind> STREAM_CODEC = ByteBufCodecs.idMapper(i -> values()[i], Kind::ordinal);

        private final String name;

        Kind(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
