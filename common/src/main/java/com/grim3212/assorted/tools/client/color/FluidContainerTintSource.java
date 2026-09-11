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
 * Tints a bucket's fluid layer with the colour of the fluid it holds. {@code
 * FluidContainerItemModel} lists it as the only tint on the fluid layer; it is registered by id so
 * a resource pack can name it from a plain {@code minecraft:model} json.
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
