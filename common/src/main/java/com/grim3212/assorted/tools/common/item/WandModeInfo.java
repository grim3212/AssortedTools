package com.grim3212.assorted.tools.common.item;

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
 * The mode line a wand shows in its tooltip. The mode itself stays in the stack's
 * {@code custom_data} under {@code Mode}; this only says which wand's modes to read it as.
 *
 * @param kind which wand this is
 */
public record WandModeInfo(Kind kind) implements TooltipProvider {

    public static final Codec<WandModeInfo> CODEC = Kind.CODEC.xmap(WandModeInfo::new, WandModeInfo::kind);
    public static final StreamCodec<ByteBuf, WandModeInfo> STREAM_CODEC = Kind.STREAM_CODEC.map(WandModeInfo::new, WandModeInfo::kind);

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltip, TooltipFlag flag, DataComponentGetter components) {
        String mode = components.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getStringOr("Mode", "");
        tooltip.accept(switch (this.kind) {
            case BUILDING -> WandBuildingItem.describeMode(mode);
            case BREAKING -> WandBreakingItem.describeMode(mode);
            case MINING -> WandMiningItem.describeMode(mode);
        });
    }

    public enum Kind implements StringRepresentable {
        BUILDING("building"),
        BREAKING("breaking"),
        MINING("mining");

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
