package com.grim3212.assorted.tools.client.data;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.tools.client.model.fluidcontainer.FluidContainerModel;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class FluidContainerBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {

    public static <T extends ModelBuilder<T>> FluidContainerBuilder<T> begin(T parent, ExistingFileHelper existingFileHelper) {
        return new FluidContainerBuilder<>(parent, existingFileHelper);
    }

    private ResourceLocation fluid;
    private Boolean flipGas;
    private Boolean applyTint;
    private Boolean coverIsMask;
    private Boolean applyFluidLuminosity;

    protected FluidContainerBuilder(T parent, ExistingFileHelper existingFileHelper) {
        super(FluidContainerModel.LOADER_NAME, parent, existingFileHelper);
    }

    public FluidContainerBuilder fluid(Fluid fluid) {
        Preconditions.checkNotNull(fluid, "fluid must not be null");
        this.fluid = Services.PLATFORM.getRegistry(Registries.FLUID).getRegistryName(fluid);
        return this;
    }

    public FluidContainerBuilder flipGas(boolean flip) {
        this.flipGas = flip;
        return this;
    }

    public FluidContainerBuilder applyTint(boolean tint) {
        this.applyTint = tint;
        return this;
    }

    public FluidContainerBuilder coverIsMask(boolean coverIsMask) {
        this.coverIsMask = coverIsMask;
        return this;
    }

    public FluidContainerBuilder applyFluidLuminosity(boolean applyFluidLuminosity) {
        this.applyFluidLuminosity = applyFluidLuminosity;
        return this;
    }

    public JsonObject toJson(JsonObject json) {
        json = super.toJson(json);

        Preconditions.checkNotNull(fluid, "fluid must not be null");

        json.addProperty("fluid", fluid.toString());

        if (flipGas != null)
            json.addProperty("flip_gas", flipGas);

        if (applyTint != null)
            json.addProperty("apply_tint", applyTint);

        if (coverIsMask != null)
            json.addProperty("cover_is_mask", coverIsMask);

        if (applyFluidLuminosity != null)
            json.addProperty("apply_fluid_luminosity", applyFluidLuminosity);

        return json;
    }
}
