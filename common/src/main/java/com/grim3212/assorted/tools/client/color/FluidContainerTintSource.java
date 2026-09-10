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
 * applies to is now the position of its entry in a model's {@code tints} list.
 * <p>
 * {@link com.grim3212.assorted.tools.client.model.fluidcontainer.FluidContainerItemModel} lists this
 * as the only tint on its fluid layer, whose quads carry tint index 0. It is registered by id as well
 * so a resource pack can name it from a plain {@code minecraft:model} json.
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
