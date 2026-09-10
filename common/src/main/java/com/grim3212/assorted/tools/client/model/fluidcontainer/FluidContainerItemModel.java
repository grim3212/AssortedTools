package com.grim3212.assorted.tools.client.model.fluidcontainer;

import com.grim3212.assorted.lib.client.model.QuadTransformers;
import com.grim3212.assorted.lib.client.model.UnbakedGeometryHelper;
import com.grim3212.assorted.lib.client.model.state.SimpleModelState;
import com.grim3212.assorted.lib.core.fluid.FluidInformation;
import com.grim3212.assorted.lib.core.fluid.IFluidVariantHandler;
import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.client.color.FluidContainerTintSource;
import com.google.common.base.Suppliers;
import com.mojang.math.Transformation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.cuboid.CuboidFace;
import net.minecraft.client.resources.model.cuboid.CuboidModelElement;
import net.minecraft.client.resources.model.cuboid.ItemModelGenerator;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.MaterialBaker;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * The bucket item model: a base layer, the contained fluid masked to the bucket's fluid window, and a
 * cover layer drawn on top.
 * <p>
 * <b>This has to be an {@link ItemModel} type, not a model json loader.</b> A fluid's appearance is a
 * baked {@code FluidModel} that {@code ModelManager} only publishes once <em>all</em> baking has
 * finished - {@code ModelManager#getFluidStateModelSet} throws "Fluid models not yet initialized"
 * before that - while a model json loader is asked for a finished {@code QuadCollection} during
 * baking. There is therefore no way for a json loader to know what a fluid looks like, on either
 * loader. An {@code ItemModel} is handed the {@link ItemStack} in {@link #update} instead, which runs
 * at render time, so the fluid's sprite is both available and stack accurate; the result is cached
 * per fluid because it never changes for the life of a bake.
 * <p>
 * That also restores what 1.20.1 did with {@code ItemOverrides#resolve} and the port briefly lost: a
 * bucket draws whatever fluid it holds, including fluids from other mods, rather than the fixed set
 * of cases datagen could enumerate.
 * <p>
 * Two structural differences from the 1.20.1 model remain:
 * <ul>
 * <li>the per layer {@code RenderTypeGroup} is gone. A quad's chunk/item layer is derived from its
 * sprite ({@code BakedQuad.MaterialInfo#layer}), so the translucency the fluid and cover layers used
 * to ask for is requested through {@link Material#withForceTranslucent(boolean)} instead;</li>
 * <li>{@code isGui3d} / {@code useBlockLight} are not model loader decisions any more - they come off
 * the named model's json ({@code gui_light}) through {@link ModelRenderProperties}.</li>
 * </ul>
 * <p>
 * AssortedDecor's {@code ColorizerItemModel} is the same shape - a stack aware item model reached
 * through {@code ClientServices.CLIENT.registerItemModelType} - for the same reason.
 */
public class FluidContainerItemModel implements ItemModel {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "fluid_container");

    // Depth offsets to prevent Z-fighting
    private static final Transformation FLUID_TRANSFORM = new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(1, 1, 1.002f), new Quaternionf());
    private static final Transformation COVER_TRANSFORM = new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(1, 1, 1.004f), new Quaternionf());

    private final Unbaked unbaked;
    private final BakingContext context;
    private final Matrix4fc transformation;
    private final ModelDebugName debugName;
    private final TextureSlots textures;
    private final ModelRenderProperties properties;

    /**
     * One baked model per fluid. Baking cannot happen before the first render (see the class note), so
     * this fills in as fluids are drawn; the whole model is thrown away and rebuilt on a resource
     * reload, so nothing here can go stale.
     */
    private final Map<Fluid, List<Layer>> byFluid = new IdentityHashMap<>();

    private FluidContainerItemModel(Unbaked unbaked, BakingContext context, Matrix4fc transformation, ModelDebugName debugName, TextureSlots textures, ModelRenderProperties properties) {
        this.unbaked = unbaked;
        this.context = context;
        this.transformation = transformation;
        this.debugName = debugName;
        this.textures = textures;
        this.properties = properties;
    }

    @Override
    public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver, ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        Fluid fluid = Services.FLUIDS.get(item).map(FluidInformation::fluid).orElse(Fluids.EMPTY);
        List<Layer> layers = this.byFluid.computeIfAbsent(fluid, this::bakeForFluid);

        output.appendModelIdentityElement(this);
        // Two buckets holding different fluids draw differently, so the fluid is part of the identity
        // the render state is cached under.
        output.appendModelIdentityElement(fluid);
        output.ensureCapacity(layers.size());

        for (Layer baked : layers) {
            ItemStackRenderState.LayerRenderState layer = output.newLayer();

            if (item.hasFoil()) {
                layer.setFoilType(ItemStackRenderState.FoilType.STANDARD);
                output.setAnimated();
                output.appendModelIdentityElement(ItemStackRenderState.FoilType.STANDARD);
            }

            for (ItemTintSource tint : baked.tints()) {
                int color = tint.calculate(item, level, owner == null ? null : owner.asLivingEntity());
                layer.tintLayers().add(color);
                output.appendModelIdentityElement(color);
            }

            layer.prepareQuadList().addAll(baked.quads().getAll());

            // A render state is reused between frames unless it says it is animated, and a fluid's
            // still texture usually is.
            if (baked.quads().hasMaterialFlag(BakedQuad.FLAG_ANIMATED)) {
                output.setAnimated();
            }

            layer.setExtents(baked.extents());
            layer.setLocalTransform(this.transformation);
            this.properties.applyToLayer(layer, displayContext);
        }
    }

    /**
     * Each layer is drawn as its own render state layer rather than one flattened quad list, because
     * the bucket's own textures stitch onto the item atlas while a fluid's sprite comes off the block
     * atlas and a single layer may not span both.
     */
    private List<Layer> bakeForFluid(Fluid fluid) {
        ModelBaker baker = this.context.blockModelBaker();
        MaterialBaker materials = baker.materials();

        Material baseLocation = this.textures.getMaterial("base");
        Material fluidMaskLocation = this.textures.getMaterial("fluid");
        Material coverLocation = this.textures.getMaterial("cover");

        FluidInformation fluidInformation = new FluidInformation(fluid);
        Optional<IFluidVariantHandler> fluidVariant = Services.FLUIDS.getVariantHandlerFor(fluidInformation);

        Material.Baked baseSprite = baseLocation != null ? materials.get(baseLocation, this.debugName) : null;
        TextureAtlasSprite fluidSprite = fluid != Fluids.EMPTY ? ClientServices.FLUIDS.getSprite(fluidInformation) : null;
        Material.Baked coverSprite = (coverLocation != null && (!this.unbaked.coverIsMask() || baseLocation != null))
                ? materials.get(coverLocation.withForceTranslucent(true), this.debugName)
                : null;

        // If the fluid is lighter than air, rotate 180deg to turn it upside down
        ModelState modelState = BlockModelRotation.IDENTITY;
        if (this.unbaked.flipGas() && fluidSprite != null && fluidVariant.isPresent() && fluidVariant.get().getDensity(fluidInformation) < 0) {
            modelState = new SimpleModelState(modelState.transformation().compose(new Transformation(null, new Quaternionf(0, 0, 1, 0), null, null)));
        }

        List<Layer> layers = new ArrayList<>();

        if (baseSprite != null) {
            layers.add(Layer.of(bakeGeneratedItem(baker, baseLocation, modelState), List.of()));
        }

        if (fluidMaskLocation != null && fluidSprite != null) {
            Material.Baked mask = materials.get(fluidMaskLocation, this.debugName);
            ModelState fluidState = new SimpleModelState(modelState.transformation().compose(FLUID_TRANSFORM));

            boolean emissive = this.unbaked.applyFluidLuminosity() && fluidVariant.isPresent() && fluidVariant.get().getLuminance(fluidInformation) > 0;
            QuadCollection quads = bakeMasked(baker, mask.sprite(), new Material.Baked(fluidSprite, true), fluidState, emissive);

            // The fluid layer's quads carry tint index 0, so its own - and only - tint source is the
            // colour of whatever it holds. Tints are per item model now, not per item, which is why
            // this is listed here rather than registered against the bucket.
            layers.add(Layer.of(quads, List.of(new FluidContainerTintSource())));
        }

        if (coverSprite != null) {
            // The cover is always the mask; what it is filled with is the base texture when the cover
            // is a mask (the bucket's own lip drawn back over the fluid) and the cover texture itself
            // otherwise.
            Material.Baked fill = this.unbaked.coverIsMask() ? baseSprite : coverSprite;
            ModelState coverState = new SimpleModelState(modelState.transformation().compose(COVER_TRANSFORM));

            layers.add(Layer.of(bakeMasked(baker, coverSprite.sprite(), fill, coverState, false), List.of()));
        }

        return List.copyOf(layers);
    }

    /**
     * One drawn layer: {@code CuboidItemModelWrapper}, which is what a {@code minecraft:model} item
     * would use for this, has a private constructor in the vanilla jar and only NeoForge opens it, so
     * common carries the two fields it would have held and {@link #update} does the render state work
     * itself.
     */
    private record Layer(QuadCollection quads, List<ItemTintSource> tints, Supplier<Vector3fc[]> extents) {
        private static Layer of(QuadCollection quads, List<ItemTintSource> tints) {
            return new Layer(quads, tints, Suppliers.memoize(() -> CuboidItemModelWrapper.computeExtents(quads.getAll()))::get);
        }
    }

    /**
     * Runs vanilla's generated-item geometry over a single {@code layer0} slot. This is the only
     * public way left to obtain the extruded shape {@code UnbakedGeometryHelper.createUnbakedItemElements}
     * used to describe.
     */
    // UnbakedGeometry#bake is deprecated by NeoForge in favour of a ContextMap taking overload that
    // lives on its own UnbakedGeometryExtension, which does not exist in the vanilla jar common is
    // compiled against. This four argument form is the only one declared here.
    @SuppressWarnings("deprecation")
    private QuadCollection bakeGeneratedItem(ModelBaker baker, Material layer0, ModelState modelState) {
        TextureSlots slots = new TextureSlots.Resolver()
                .addLast(new TextureSlots.Data.Builder().addTexture("layer0", layer0).build())
                .resolve(this.debugName);
        return new ItemModelGenerator().geometry().bake(slots, baker, modelState, this.debugName);
    }

    /**
     * Cuts {@code mask}'s opaque pixels out as flat elements and bakes every face of them against one
     * fixed material. The library's slot-driven {@code bakeElements} cannot be used here because a
     * fluid's sprite arrives as a {@link TextureAtlasSprite} from the platform's fluid helper rather
     * than as a named texture slot.
     */
    private QuadCollection bakeMasked(ModelBaker baker, TextureAtlasSprite mask, Material.Baked material, ModelState modelState, boolean emissive) {
        List<BakedQuad> quads = new ArrayList<>();
        for (CuboidModelElement element : UnbakedGeometryHelper.createUnbakedItemMaskElements(0, mask)) {
            for (Map.Entry<Direction, CuboidFace> entry : element.faces().entrySet()) {
                quads.add(UnbakedGeometryHelper.bakeElementFace(baker, element, entry.getValue(), material, entry.getKey(), modelState));
            }
        }

        if (emissive) {
            quads = QuadTransformers.settingMaxEmissivity().process(quads);
        }

        QuadCollection.Builder builder = new QuadCollection.Builder();
        quads.forEach(builder::addUnculledFace);
        return builder.build();
    }

    /**
     * @param model                The <em>geometry</em> json naming the {@code base} / {@code fluid} /
     *                             {@code cover} / {@code particle} texture slots, plus the display
     *                             transforms and gui light every bucket layer is drawn with. It is a
     *                             plain vanilla model json - the loader block it used to carry is gone
     *                             along with the json loader.
     * @param flipGas              Turn the model upside down when the fluid is lighter than air.
     * @param coverIsMask          Fill the cover's shape with the base texture rather than the cover
     *                             texture, which is how a bucket draws its own lip back over the fluid.
     * @param applyFluidLuminosity Draw the fluid layer fullbright when the fluid emits light.
     */
    public record Unbaked(Identifier model, boolean flipGas, boolean coverIsMask, boolean applyFluidLuminosity) implements ItemModel.Unbaked {

        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Identifier.CODEC.fieldOf("model").forGetter(Unbaked::model),
                Codec.BOOL.optionalFieldOf("flip_gas", false).forGetter(Unbaked::flipGas),
                Codec.BOOL.optionalFieldOf("cover_is_mask", true).forGetter(Unbaked::coverIsMask),
                Codec.BOOL.optionalFieldOf("apply_fluid_luminosity", true).forGetter(Unbaked::applyFluidLuminosity)
        ).apply(instance, Unbaked::new));

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public void resolveDependencies(ResolvableModel.Resolver resolver) {
            resolver.markDependency(this.model);
        }

        @Override
        public ItemModel bake(BakingContext context, Matrix4fc transformation) {
            ModelBaker baker = context.blockModelBaker();
            ResolvedModel resolved = baker.getModel(this.model);
            TextureSlots textures = resolved.getTopTextureSlots();

            return new FluidContainerItemModel(this, context, transformation, this.model::toString, textures,
                    ModelRenderProperties.fromResolvedModel(baker, resolved, textures));
        }
    }
}
