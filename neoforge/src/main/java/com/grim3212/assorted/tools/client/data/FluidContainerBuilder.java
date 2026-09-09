package com.grim3212.assorted.tools.client.data;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.grim3212.assorted.tools.client.model.fluidcontainer.FluidContainerModel;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.model.generators.template.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;
import org.jetbrains.annotations.Nullable;

/**
 * Writes the {@code assortedtools:fluid_container} loader block into a bucket's model json.
 * <p>
 * Forge's {@code ModelBuilder} / {@code ItemModelProvider} pair is gone, so this is no longer a
 * builder hanging off an {@code ItemModelBuilder}. {@link CustomLoaderBuilder} is still the hook,
 * but it now plugs into {@link ExtendedModelTemplateBuilder#customLoader} and contributes to the
 * json a {@link net.minecraft.client.data.models.model.ModelTemplate} emits, so it is constructed
 * with the loader id plus whether the loader tolerates inline vanilla elements (it does not - it
 * replaces the geometry outright) and it has to be able to deep copy itself, because a
 * {@code ModelTemplate} is immutable.
 * <p>
 * The {@code loader} key itself is unchanged; {@code UnbakedModelParser} still reads it, and the
 * {@code Identifier} it names is still what {@code ModelEvent.RegisterLoaders} is keyed by.
 * <p>
 * <b>{@code apply_tint} is deliberately no longer written.</b> In 1.20.1 it decided whether the
 * fluid layer's quads were given tint index 1 or -1, and an {@code ItemColor} registered against the
 * bucket answered for that index. Tinting is data now: the fluid layer's quads always carry tint
 * index 1 and whether anything colours them is decided by the {@code tints} list on the <i>item</i>
 * model, so the field would be read by nobody - {@code FluidContainerModel.Loader#read} does not
 * look at it. {@code ToolsItemModelProvider} lists
 * {@link com.grim3212.assorted.tools.client.color.FluidContainerTintSource} at index 1 instead,
 * which is the same decision expressed where it now belongs.
 */
public class FluidContainerBuilder extends CustomLoaderBuilder {

    public static FluidContainerBuilder begin() {
        return new FluidContainerBuilder();
    }

    private @Nullable Identifier fluid;
    private @Nullable Boolean flipGas;
    private @Nullable Boolean coverIsMask;
    private @Nullable Boolean applyFluidLuminosity;

    protected FluidContainerBuilder() {
        super(FluidContainerModel.LOADER_NAME, false);
    }

    /**
     * The fluid is named by id rather than by {@code Fluid} instance on purpose: the milk case is
     * NeoForge's opt-in {@code minecraft:milk}, which is not a registered fluid on Fabric at all, and
     * datagen must be able to write the case out either way. An id the loader cannot resolve falls
     * back to {@code minecraft:empty} in {@code FluidContainerModel.Loader#read}, which is exactly
     * the old behaviour for an unknown fluid.
     */
    public FluidContainerBuilder fluid(Identifier fluid) {
        Preconditions.checkNotNull(fluid, "fluid must not be null");
        this.fluid = fluid;
        return this;
    }

    public FluidContainerBuilder flipGas(boolean flip) {
        this.flipGas = flip;
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

    @Override
    protected CustomLoaderBuilder copyInternal() {
        FluidContainerBuilder copy = new FluidContainerBuilder();
        copy.fluid = this.fluid;
        copy.flipGas = this.flipGas;
        copy.coverIsMask = this.coverIsMask;
        copy.applyFluidLuminosity = this.applyFluidLuminosity;
        return copy;
    }

    @Override
    public JsonObject toJson(JsonObject json) {
        json = super.toJson(json);

        Preconditions.checkNotNull(fluid, "fluid must not be null");

        json.addProperty("fluid", fluid.toString());

        if (flipGas != null)
            json.addProperty("flip_gas", flipGas);

        if (coverIsMask != null)
            json.addProperty("cover_is_mask", coverIsMask);

        if (applyFluidLuminosity != null)
            json.addProperty("apply_fluid_luminosity", applyFluidLuminosity);

        return json;
    }
}
