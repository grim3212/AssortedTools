package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

import java.util.Arrays;
import java.util.List;

/**
 * What a staff does on right click. The mode is kept in the stack's {@code custom_data} under
 * {@code Mode}; a stack without one is in its staff's first mode.
 */
public enum StaffMode implements StringRepresentable {
    PLACE_WATER(StaffModeInfo.Kind.NEPTUNE, "place_water", ChatFormatting.BLUE),
    FREEZE_MOBS(StaffModeInfo.Kind.NEPTUNE, "freeze_mobs", ChatFormatting.AQUA),
    FREEZE_WATER(StaffModeInfo.Kind.NEPTUNE, "freeze_water", ChatFormatting.WHITE),

    PLACE_LAVA(StaffModeInfo.Kind.PHOENIX, "place_lava", ChatFormatting.GOLD),
    PLACE_FIRE(StaffModeInfo.Kind.PHOENIX, "place_fire", ChatFormatting.RED),
    THAW_MOBS(StaffModeInfo.Kind.PHOENIX, "thaw_mobs", ChatFormatting.YELLOW),
    MELT_ICE(StaffModeInfo.Kind.PHOENIX, "melt_ice", ChatFormatting.AQUA),

    FLOAT_PUSH(StaffModeInfo.Kind.POWER, "float_push", ChatFormatting.LIGHT_PURPLE),
    FLOAT_PULL(StaffModeInfo.Kind.POWER, "float_pull", ChatFormatting.LIGHT_PURPLE),
    DROP_PUSH(StaffModeInfo.Kind.POWER, "drop_push", ChatFormatting.DARK_PURPLE),
    DROP_PULL(StaffModeInfo.Kind.POWER, "drop_pull", ChatFormatting.DARK_PURPLE);

    public static final String KEY = "Mode";

    private final StaffModeInfo.Kind kind;
    private final String name;
    private final ChatFormatting color;

    StaffMode(StaffModeInfo.Kind kind, String name, ChatFormatting color) {
        this.kind = kind;
        this.name = name;
        this.color = color;
    }

    public static List<StaffMode> of(StaffModeInfo.Kind kind) {
        return Arrays.stream(values()).filter(mode -> mode.kind == kind).toList();
    }

    /** The stored mode, or the kind's first when nothing valid is stored. */
    public static StaffMode fromString(StaffModeInfo.Kind kind, String stored) {
        List<StaffMode> modes = of(kind);
        return modes.stream().filter(mode -> mode.name.equals(stored)).findFirst().orElse(modes.getFirst());
    }

    public StaffMode next() {
        List<StaffMode> modes = of(this.kind);
        return modes.get((modes.indexOf(this) + 1) % modes.size());
    }

    public boolean pulls() {
        return this == FLOAT_PULL || this == DROP_PULL;
    }

    public boolean drops() {
        return this == DROP_PUSH || this == DROP_PULL;
    }

    public Component getTranslatedString() {
        return Component.translatable(Constants.MOD_ID + ".staff.mode." + this.name).withStyle(this.color);
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
