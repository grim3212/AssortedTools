package com.grim3212.assorted.tools.client.model.fluidcontainer;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.grim3212.assorted.lib.client.model.*;
import com.grim3212.assorted.lib.client.model.loaders.IModelSpecification;
import com.grim3212.assorted.lib.client.model.loaders.IModelSpecificationLoader;
import com.grim3212.assorted.lib.client.model.loaders.context.IModelBakingContext;
import com.grim3212.assorted.lib.client.model.loaders.context.SimpleModelBakingContext;
import com.grim3212.assorted.lib.client.model.state.SimpleModelState;
import com.grim3212.assorted.lib.core.fluid.FluidInformation;
import com.grim3212.assorted.lib.core.fluid.IFluidVariantHandler;
import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.tools.Constants;
import com.mojang.math.Transformation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class FluidContainerModel implements IModelSpecification<FluidContainerModel> {
    public static final ResourceLocation LOADER_NAME = new ResourceLocation(Constants.MOD_ID, "fluid_container");

    // Depth offsets to prevent Z-fighting
    private static final Transformation FLUID_TRANSFORM = new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(1, 1, 1.002f), new Quaternionf());
    private static final Transformation COVER_TRANSFORM = new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(1, 1, 1.004f), new Quaternionf());

    private final Fluid fluid;
    private final boolean flipGas;
    private final boolean coverIsMask;
    private final boolean applyFluidLuminosity;

    private FluidContainerModel(Fluid fluid, boolean flipGas, boolean coverIsMask, boolean applyFluidLuminosity) {
        this.fluid = fluid;
        this.flipGas = flipGas;
        this.coverIsMask = coverIsMask;
        this.applyFluidLuminosity = applyFluidLuminosity;
    }

    public FluidContainerModel withFluid(Fluid newFluid) {
        return new FluidContainerModel(newFluid, flipGas, coverIsMask, applyFluidLuminosity);
    }

    public static RenderTypeGroup getLayerRenderTypes(boolean unlit) {
        return new RenderTypeGroup(RenderType.solid(), RenderType.entitySolid(TextureAtlas.LOCATION_BLOCKS));
    }

    @Override
    public BakedModel bake(IModelBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ResourceLocation modelLocation) {
        Material particleLocation = context.getMaterial("particle").orElse(null);
        Material baseLocation = context.getMaterial("base").orElse(null);
        Material fluidMaskLocation = context.getMaterial("fluid").orElse(null);
        Material coverLocation = context.getMaterial("cover").orElse(null);

        FluidInformation fluidInformation = new FluidInformation(fluid);
        Optional<IFluidVariantHandler> fluidVariant = Services.FLUIDS.getVariantHandlerFor(fluidInformation);
        TextureAtlasSprite baseSprite = baseLocation != null ? spriteGetter.apply(baseLocation) : null;
        TextureAtlasSprite fluidSprite = fluid != Fluids.EMPTY ? ClientServices.FLUIDS.getSprite(fluidInformation) : null;
        TextureAtlasSprite coverSprite = (coverLocation != null && (!coverIsMask || baseLocation != null)) ? spriteGetter.apply(coverLocation) : null;

        TextureAtlasSprite particleSprite = particleLocation != null ? spriteGetter.apply(particleLocation) : null;

        if (particleSprite == null) particleSprite = fluidSprite;
        if (particleSprite == null) particleSprite = baseSprite;
        if (particleSprite == null && !coverIsMask) particleSprite = coverSprite;

        // If the fluid is lighter than air, rotate 180deg to turn it upside down
        if (flipGas && fluid != Fluids.EMPTY && fluidVariant.isPresent() && fluidVariant.get().getDensity(fluidInformation) < 0) {
            modelState = new SimpleModelState(modelState.getRotation().compose(new Transformation(null, new Quaternionf(0, 0, 1, 0), null, null)));
        }

        // We need to disable GUI 3D and block lighting for this to render properly
        var itemContext = SimpleModelBakingContext.SimpleModelBakingContextBuilder.builder().withIsGui3d(false).withUseBlockLight(false).build();
        var modelBuilder = CombiningModel.Baked.builder(itemContext, particleSprite, new ContainedFluidOverrideHandler(context, baker, this), context.getTransforms());

        var normalRenderTypes = getLayerRenderTypes(false);

        if (baseLocation != null && baseSprite != null) {
            // Base texture
            var unbaked = UnbakedGeometryHelper.createUnbakedItemElements(0, baseSprite.contents());
            var quads = UnbakedGeometryHelper.bakeElements(unbaked, $ -> baseSprite, modelState, modelLocation);
            modelBuilder.addQuads(normalRenderTypes, quads);
        }

        if (fluidMaskLocation != null && fluidSprite != null) {
            TextureAtlasSprite templateSprite = spriteGetter.apply(fluidMaskLocation);
            if (templateSprite != null) {
                // Fluid layer
                var transformedState = new SimpleModelState(modelState.getRotation().compose(FLUID_TRANSFORM), modelState.isUvLocked());
                var unbaked = UnbakedGeometryHelper.createUnbakedItemMaskElements(1, templateSprite); // Use template as mask

                var quads = UnbakedGeometryHelper.bakeElements(unbaked, $ -> fluidSprite, transformedState, modelLocation); // Bake with fluid texture

                var emissive = applyFluidLuminosity && fluidVariant.isPresent() && fluidVariant.get().getLuminance(fluidInformation) > 0;
                var renderTypes = getLayerRenderTypes(emissive);
                if (emissive) QuadTransformers.settingMaxEmissivity().processInPlace(quads);

                modelBuilder.addQuads(renderTypes, quads);
            }
        }

        if (coverSprite != null) {
            var sprite = coverIsMask ? baseSprite : coverSprite;
            if (sprite != null) {
                // Cover/overlay
                var transformedState = new SimpleModelState(modelState.getRotation().compose(COVER_TRANSFORM), modelState.isUvLocked());
                var unbaked = UnbakedGeometryHelper.createUnbakedItemMaskElements(2, coverSprite); // Use cover as mask
                var quads = UnbakedGeometryHelper.bakeElements(unbaked, $ -> sprite, transformedState, modelLocation); // Bake with selected texture
                modelBuilder.addQuads(normalRenderTypes, quads);
            }
        }

        modelBuilder.setParticle(particleSprite);

        return modelBuilder.build();
    }


    public static final class Loader implements IModelSpecificationLoader<FluidContainerModel> {
        public static final Loader INSTANCE = new Loader();

        public FluidContainerModel read(JsonDeserializationContext deserializationContext, JsonObject jsonObject) {
            if (!jsonObject.has("fluid"))
                throw new RuntimeException("Bucket model requires 'fluid' value.");

            ResourceLocation fluidName = new ResourceLocation(jsonObject.get("fluid").getAsString());

            Fluid fluid = Services.PLATFORM.getRegistry(Registries.FLUID).getValue(fluidName).orElse(Fluids.EMPTY);

            boolean flip = GsonHelper.getAsBoolean(jsonObject, "flip_gas", false);
            boolean coverIsMask = GsonHelper.getAsBoolean(jsonObject, "cover_is_mask", true);
            boolean applyFluidLuminosity = GsonHelper.getAsBoolean(jsonObject, "apply_fluid_luminosity", true);

            // create new model with correct liquid
            return new FluidContainerModel(fluid, flip, coverIsMask, applyFluidLuminosity);
        }
    }

    private static final class ContainedFluidOverrideHandler extends ItemOverridesExtension {
        private final Map<String, BakedModel> cache = Maps.newHashMap(); // contains all the baked models since they'll never change
        private final ModelBaker baker;
        private final FluidContainerModel parent;

        private ContainedFluidOverrideHandler(IModelBakingContext context, ModelBaker baker, FluidContainerModel parent) {
            super(context);
            this.baker = baker;
            this.parent = parent;
        }

        @Override
        public BakedModel resolve(BakedModel originalModel, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
            BakedModel overridden = context.getItemOverrides(this.baker).resolve(originalModel, stack, level, entity, seed);
            if (overridden != originalModel) return overridden;
            return Services.FLUIDS.get(stack)
                    .map(fluidStack -> {
                        Fluid fluid = fluidStack.fluid();
                        String name = Services.PLATFORM.getRegistry(Registries.FLUID).getRegistryName(fluid).toString();

                        if (!cache.containsKey(name)) {
                            FluidContainerModel unbaked = this.parent.withFluid(fluid);
                            BakedModel bakedModel = unbaked.bake(context, baker, Material::sprite, BlockModelRotation.X0_Y0, new ResourceLocation(Constants.MOD_ID, "bucket_override"));
                            cache.put(name, bakedModel);
                            return bakedModel;
                        }

                        return cache.get(name);
                    })
                    // not a fluid item apparently
                    .orElse(originalModel); // empty bucket
        }
    }
}
