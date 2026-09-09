package com.grim3212.assorted.tools.client.model.fluidcontainer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.grim3212.assorted.lib.client.model.CombiningModel;
import com.grim3212.assorted.lib.client.model.IModelBuilder;
import com.grim3212.assorted.lib.client.model.QuadTransformers;
import com.grim3212.assorted.lib.client.model.UnbakedGeometryHelper;
import com.grim3212.assorted.lib.client.model.loaders.IModelSpecification;
import com.grim3212.assorted.lib.client.model.loaders.IModelSpecificationLoader;
import com.grim3212.assorted.lib.client.model.loaders.context.IModelBakingContext;
import com.grim3212.assorted.lib.client.model.state.SimpleModelState;
import com.grim3212.assorted.lib.core.fluid.FluidInformation;
import com.grim3212.assorted.lib.core.fluid.IFluidVariantHandler;
import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.tools.Constants;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.cuboid.CuboidFace;
import net.minecraft.client.resources.model.cuboid.CuboidModelElement;
import net.minecraft.client.resources.model.cuboid.ItemModelGenerator;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.MaterialBaker;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The bucket model: a base layer, a fluid layer masked to the bucket's fluid window, and a cover
 * layer drawn on top.
 * <p>
 * <b>TODO(26.2): this model can no longer vary with the stack it is drawn for.</b> In 1.20.1 the
 * fluid was picked per {@code ItemStack} by an {@code ItemOverrides#resolve} hook - the
 * {@code ContainedFluidOverrideHandler} that used to live at the bottom of this file - which read
 * the stack's fluid, re-baked this specification with it and cached the result. 26.2 deleted
 * {@code ItemOverrides} and {@code ItemOverride} outright; item variation is chosen <em>before</em>
 * baking by data driven {@code net.minecraft.client.renderer.item.ItemModel} types
 * ({@code SelectItemModel} / {@code ConditionalItemModel} / {@code RangeSelectItemModel}) over
 * codec-registered properties under {@code client.renderer.item.properties.**}, named from the
 * item's own model json. There is no hook a model loader can implement to answer "which model for
 * this stack", and AssortedLib's {@code ItemOverridesExtension} is a context holder with no resolve
 * method for exactly that reason.
 * <p>
 * What that means in practice: <b>a bucket renders the fluid its model json names, and nothing
 * else</b>. Every generated bucket json says {@code "fluid": "minecraft:empty"}, so filled buckets
 * currently render as empty ones. Restoring this needs a registered
 * {@code SelectItemModel.UnbakedSwitch} property keyed on the stack's fluid plus one baked case per
 * fluid emitted into the item's json at datagen time - a design change spanning both loaders and
 * the datagen slice, not something this model can do from here.
 * <p>
 * Two smaller losses, both structural:
 * <ul>
 * <li>the per layer {@code RenderTypeGroup} is gone. A quad's chunk/item layer is derived from its
 * sprite now ({@code BakedQuad.MaterialInfo#layer}), so the translucency the fluid and cover layers
 * used to ask for is requested through {@link Material#withForceTranslucent(boolean)} instead;</li>
 * <li>the baking context's {@code isGui3d} / {@code useBlockLight} overrides are gone -
 * {@link IModelBuilder} only carries quads, ambient occlusion and a particle - so those two belong
 * in the model json ({@code gui_light}) now.</li>
 * </ul>
 */
public class FluidContainerModel implements IModelSpecification<FluidContainerModel> {
    public static final Identifier LOADER_NAME = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "fluid_container");

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

    @Override
    public BlockStateModel bake(IModelBakingContext context, ModelBaker baker, ModelState modelState, Identifier modelLocation) {
        final ModelDebugName debugName = modelLocation::toString;
        final MaterialBaker materials = baker.materials();

        Material particleLocation = context.getMaterial("particle").orElse(null);
        Material baseLocation = context.getMaterial("base").orElse(null);
        Material fluidMaskLocation = context.getMaterial("fluid").orElse(null);
        Material coverLocation = context.getMaterial("cover").orElse(null);

        FluidInformation fluidInformation = new FluidInformation(fluid);
        Optional<IFluidVariantHandler> fluidVariant = Services.FLUIDS.getVariantHandlerFor(fluidInformation);

        Material.Baked baseSprite = baseLocation != null ? materials.get(baseLocation, debugName) : null;
        TextureAtlasSprite fluidSprite = fluid != Fluids.EMPTY ? ClientServices.FLUIDS.getSprite(fluidInformation) : null;
        Material.Baked coverSprite = (coverLocation != null && (!coverIsMask || baseLocation != null))
                ? materials.get(coverLocation.withForceTranslucent(true), debugName)
                : null;

        Material.Baked particleSprite = particleLocation != null ? materials.get(particleLocation, debugName) : null;

        if (particleSprite == null && fluidSprite != null) particleSprite = new Material.Baked(fluidSprite, false);
        if (particleSprite == null) particleSprite = baseSprite;
        if (particleSprite == null && !coverIsMask) particleSprite = coverSprite;
        if (particleSprite == null) particleSprite = materials.reportMissingReference("particle", debugName);

        // If the fluid is lighter than air, rotate 180deg to turn it upside down
        if (flipGas && fluid != Fluids.EMPTY && fluidVariant.isPresent() && fluidVariant.get().getDensity(fluidInformation) < 0) {
            modelState = new SimpleModelState(modelState.transformation().compose(new Transformation(null, new Quaternionf(0, 0, 1, 0), null, null)));
        }

        var modelBuilder = CombiningModel.Baked.builder(particleSprite);

        if (baseLocation != null && baseSprite != null) {
            // Base texture. createUnbakedItemElements is gone from the library - the generated item
            // shape is only obtainable by running ItemModelGenerator's own geometry over a layer0
            // slot, which is what this does, so the geometry is identical to what it used to bake.
            var quads = bakeGeneratedItem(baker, baseLocation, modelState, debugName);
            modelBuilder.addLayer(part(particleSprite, quads));
        }

        if (fluidMaskLocation != null && fluidSprite != null) {
            Material.Baked templateSprite = materials.get(fluidMaskLocation, debugName);

            // Fluid layer
            var transformedState = new SimpleModelState(modelState.transformation().compose(FLUID_TRANSFORM));
            var unbaked = UnbakedGeometryHelper.createUnbakedItemMaskElements(1, templateSprite.sprite()); // Use template as mask
            var quads = bakeElements(baker, unbaked, new Material.Baked(fluidSprite, true), transformedState); // Bake with fluid texture

            var emissive = applyFluidLuminosity && fluidVariant.isPresent() && fluidVariant.get().getLuminance(fluidInformation) > 0;
            if (emissive) quads = QuadTransformers.settingMaxEmissivity().process(quads);

            modelBuilder.addLayer(part(particleSprite, quads));
        }

        if (coverSprite != null && !coverIsMask) {
            // Cover/overlay
            var transformedState = new SimpleModelState(modelState.transformation().compose(COVER_TRANSFORM));
            var unbaked = UnbakedGeometryHelper.createUnbakedItemMaskElements(2, coverSprite.sprite()); // Use cover as mask
            var quads = bakeElements(baker, unbaked, coverSprite, transformedState); // Bake with selected texture
            modelBuilder.addLayer(part(particleSprite, quads));
        }

        modelBuilder.setParticle(particleSprite);

        return ClientServices.MODELS.adaptToPlatform(modelBuilder.build());
    }

    /**
     * Bakes one layer's quads into a {@link BlockStateModelPart}. Items are never ambient occluded,
     * which is what the old {@code isGui3d(false) / useBlockLight(false)} baking context stood in
     * for.
     */
    private static BlockStateModelPart part(Material.Baked particle, List<BakedQuad> quads) {
        IModelBuilder<?> builder = IModelBuilder.of(false, particle);
        for (BakedQuad quad : quads) {
            builder.addUnculledFace(quad);
        }
        return builder.build();
    }

    /**
     * Runs vanilla's generated-item geometry over a single {@code layer0} slot. This is the only
     * public way left to obtain the extruded shape {@code UnbakedGeometryHelper.createUnbakedItemElements}
     * used to describe.
     */
    private static List<BakedQuad> bakeGeneratedItem(ModelBaker baker, Material layer0, ModelState modelState, ModelDebugName debugName) {
        TextureSlots slots = new TextureSlots.Resolver()
                .addLast(new TextureSlots.Data.Builder().addTexture("layer0", layer0).build())
                .resolve(debugName);
        return new ItemModelGenerator().geometry().bake(slots, baker, modelState, debugName).getAll();
    }

    /**
     * Bakes every face of every element against one fixed sprite, which is what passing a constant
     * sprite getter used to do. The library's slot-driven {@code bakeElements} cannot be used here
     * because the fluid's sprite is handed over as a {@link TextureAtlasSprite} by the platform's
     * fluid helper rather than as a named texture slot.
     */
    private static List<BakedQuad> bakeElements(ModelBaker baker, List<CuboidModelElement> elements, Material.Baked material, ModelState modelState) {
        List<BakedQuad> quads = new ArrayList<>();
        for (CuboidModelElement element : elements) {
            for (Map.Entry<Direction, CuboidFace> entry : element.faces().entrySet()) {
                quads.add(UnbakedGeometryHelper.bakeElementFace(baker, element, entry.getValue(), material, entry.getKey(), modelState));
            }
        }
        return quads;
    }

    public static final class Loader implements IModelSpecificationLoader<FluidContainerModel> {
        public static final Loader INSTANCE = new Loader();

        public FluidContainerModel read(JsonDeserializationContext deserializationContext, JsonObject jsonObject) {
            if (!jsonObject.has("fluid"))
                throw new RuntimeException("Bucket model requires 'fluid' value.");

            Identifier fluidName = Identifier.parse(jsonObject.get("fluid").getAsString());

            Fluid fluid = Services.PLATFORM.getRegistry(Registries.FLUID).getValue(fluidName).orElse(Fluids.EMPTY);

            boolean flip = GsonHelper.getAsBoolean(jsonObject, "flip_gas", false);
            boolean coverIsMask = GsonHelper.getAsBoolean(jsonObject, "cover_is_mask", true);
            boolean applyFluidLuminosity = GsonHelper.getAsBoolean(jsonObject, "apply_fluid_luminosity", true);

            // create new model with correct liquid
            return new FluidContainerModel(fluid, flip, coverIsMask, applyFluidLuminosity);
        }
    }
}
