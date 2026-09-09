package com.grim3212.assorted.tools.client.color;

import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.tools.Constants;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Tints a bucket's fluid layer with the colour of the fluid it holds.
 * <p>
 * {@code ItemColor} / {@code ItemColors} were removed in 26.2, so tints are no longer registered per
 * item in code. An item model json lists its tint layers, each naming an {@link ItemTintSource} type
 * by id, and code only registers the id to {@link MapCodec} pair - see {@code ToolsClient}. The
 * {@code tintIndex != 1} check the old {@code ItemColor} did is gone with it: which layer a source
 * applies to is now the position of its entry in the model json's {@code tints} list, so this source
 * belongs at index 1, opposite the fluid layer.
 * <p>
 * TODO(26.2): nothing references this yet. The bucket item models are still in 1.20.1 shape (no
 *  {@code assets/assortedtools/items/*.json}, no {@code tints} list), so until those are regenerated
 *  this is registered but never asked for a colour.
 */
public record FluidContainerTintSource() implements ItemTintSource {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "fluid_container");
    private static final int NO_TINT = 0xFFFFFFFF;

    public static final MapCodec<FluidContainerTintSource> MAP_CODEC = MapCodec.unit(new FluidContainerTintSource());

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        return Services.FLUIDS.get(itemStack).map(ClientServices.FLUIDS::getFluidColor).orElse(NO_TINT);
    }

    @Override
    public MapCodec<FluidContainerTintSource> type() {
        return MAP_CODEC;
    }
}
