package com.grim3212.assorted.tools.client.data;

import com.grim3212.assorted.lib.LibConstants;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.client.color.FluidContainerTintSource;
import com.grim3212.assorted.tools.client.render.item.SpearSpecialRenderer;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.renderer.item.properties.select.CustomModelDataProperty;
import net.minecraft.client.renderer.special.TridentSpecialRenderer;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Forge's {@code ItemModelProvider} / {@code ItemModelBuilder} / {@code ExistingFileHelper} /
 * {@code ForgeRegistries} are all gone, and so is the idea that an item model is one json. An item
 * points at a data driven {@code ItemModel} in {@code assets/<ns>/items/} - a
 * {@code minecraft:model} / {@code select} / {@code condition} / {@code special} / {@code composite}
 * tree - which names the {@code assets/<ns>/models/} geometry to draw. {@link ModelProvider} writes
 * both halves.
 * <p>
 * {@code generateFlatItem(item, template)} cannot be used for most of this mod: it derives the
 * texture from the item's id, and almost every texture here lives in a subfolder
 * ({@code item/tools/}, {@code item/armors/}, {@code item/shears/}, {@code item/buckets/}). So the
 * flat models are built with {@code ModelTemplate#create(Identifier, TextureMapping, output)} and
 * handed to {@code itemModelOutput} explicitly.
 * <p>
 * Three things here are not plain flat items:
 * <ul>
 * <li>{@link #spear} - a {@code select} on the display context between a flat gui model and a
 * {@code condition} on {@code minecraft:using_item} over two {@code minecraft:special} entries
 * naming {@link SpearSpecialRenderer}; vanilla's {@code ItemModelGenerators#generateTrident} is the
 * template, because a spear is a trident shaped item drawn by a {@code SpecialModelRenderer}. (
 * {@code generateSpear} - vanilla's new copper spear - is <em>not</em> the right shape: it has no
 * special renderer and no throwing pose.)</li>
 * <li>{@link #bucket} - a {@code select} on {@code minecraft:custom_model_data} with one case per
 * fluid, which is what replaced the deleted {@code ItemOverrides} handler.</li>
 * <li>{@link #bucket}'s fluid layer, which is a separate {@code composite} member for atlas
 * reasons - see below.</li>
 * </ul>
 */
public class ToolsItemModelProvider extends ModelProvider {

    /**
     * The extra texture slots {@code FluidContainerModel} reads. {@link TextureSlot} has no
     * {@code equals}, so each of these has to be created exactly once and shared.
     */
    private static final TextureSlot BASE = TextureSlot.create("base");
    private static final TextureSlot FLUID = TextureSlot.create("fluid");
    private static final TextureSlot COVER = TextureSlot.create("cover");

    private static final Identifier LIB_DEFAULT_ITEM = Identifier.fromNamespaceAndPath(LibConstants.MOD_ID, "item/default");

    /**
     * The in hand spear model, and the throwing pose it swaps to. Both keep the vanilla trident
     * parents they had in 1.20.1; the {@code head} transform was an inline {@code transforms()} block
     * on the old builder and is a template transform now. {@code display} entries are still merged
     * per perspective down the parent chain, so naming only {@code head} leaves every other
     * perspective coming from {@code minecraft:item/trident_in_hand} exactly as before.
     */
    private static final ModelTemplate SPEAR_IN_HAND = ExtendedModelTemplateBuilder.builder()
            .parent(Identifier.withDefaultNamespace("item/trident_in_hand"))
            .requiredTextureSlot(TextureSlot.PARTICLE)
            .transform(ItemDisplayContext.HEAD, t -> t.rotation(0.0F, 180.0F, 120.0F).translation(8.0F, 10.0F, -11.0F).scale(1.5F))
            .build();

    private static final ModelTemplate SPEAR_THROWING = ExtendedModelTemplateBuilder.builder()
            .parent(Identifier.withDefaultNamespace("item/trident_throwing"))
            .requiredTextureSlot(TextureSlot.PARTICLE)
            .build();

    /**
     * The bucket body: the same four slots and the same loader flags the 1.20.1 json carried, with
     * {@code fluid: minecraft:empty} so no fluid layer is baked. The fluid itself is a second model.
     */
    private static final ModelTemplate BUCKET_BODY = ExtendedModelTemplateBuilder.builder()
            .parent(LIB_DEFAULT_ITEM)
            .requiredTextureSlot(TextureSlot.PARTICLE)
            .requiredTextureSlot(BASE)
            .requiredTextureSlot(FLUID)
            .requiredTextureSlot(COVER)
            .customLoader(FluidContainerBuilder::begin, b -> b
                    .fluid(Identifier.withDefaultNamespace("empty"))
                    .flipGas(true)
                    .applyFluidLuminosity(true)
                    .coverIsMask(true))
            .build();

    /**
     * The fluids a bucket's contents can be drawn as. Datagen has to enumerate them - a
     * {@code select} is a fixed list of cases - so this is every fluid this mod can actually put in
     * one of its buckets: the two vanilla ones and NeoForge's opt-in milk.
     * <p>
     * The {@code String}s are the values {@code BetterBucketItem#getFluid} produces, which is what
     * has to reach {@code minecraft:custom_model_data}. Milk carries two: {@code MilkingHandler}
     * stores the bare string {@code "milk"} while everything else stores a full fluid id, and both
     * round trip through {@code getFluidFromString} to {@code minecraft:milk}.
     */
    private record BucketFluid(String modelSuffix, Identifier fluid, List<String> componentValues) {
    }

    private static final List<BucketFluid> BUCKET_FLUIDS = List.of(
            new BucketFluid("water", Identifier.withDefaultNamespace("water"), List.of("minecraft:water")),
            new BucketFluid("lava", Identifier.withDefaultNamespace("lava"), List.of("minecraft:lava")),
            new BucketFluid("milk", Identifier.withDefaultNamespace("milk"), List.of("milk", "minecraft:milk")));

    public ToolsItemModelProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    public String getName() {
        return "Assorted Tools item models";
    }

    /**
     * This mod registers no blocks at all, so there is nothing for {@link BlockModelGenerators} to
     * do and nothing for {@code ModelProvider}'s blockstate validation to miss.
     */
    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        tool(itemModels, ToolsItems.WOOD_HAMMER.get());
        tool(itemModels, ToolsItems.STONE_HAMMER.get());
        tool(itemModels, ToolsItems.GOLD_HAMMER.get());
        tool(itemModels, ToolsItems.IRON_HAMMER.get());
        tool(itemModels, ToolsItems.DIAMOND_HAMMER.get());
        tool(itemModels, ToolsItems.NETHERITE_HAMMER.get());
        handheldItem(itemModels, ToolsItems.WOOD_BOOMERANG.get());
        handheldItem(itemModels, ToolsItems.DIAMOND_BOOMERANG.get());
        generatedItem(itemModels, ToolsItems.POKEBALL.get());

        handheldItem(itemModels, ToolsItems.ULTIMATE_FIST.get());
        generatedItem(itemModels, ToolsItems.U_FRAGMENT.get());
        generatedItem(itemModels, ToolsItems.L_FRAGMENT.get());
        generatedItem(itemModels, ToolsItems.T_FRAGMENT.get());
        generatedItem(itemModels, ToolsItems.I_FRAGMENT.get());
        generatedItem(itemModels, ToolsItems.M_FRAGMENT.get());
        generatedItem(itemModels, ToolsItems.A_FRAGMENT.get());
        generatedItem(itemModels, ToolsItems.MISSING_FRAGMENT.get());
        generatedItem(itemModels, ToolsItems.E_FRAGMENT.get());

        handheldItem(itemModels, ToolsItems.BUILDING_WAND.get());
        handheldItem(itemModels, ToolsItems.REINFORCED_BUILDING_WAND.get());
        handheldItem(itemModels, ToolsItems.BREAKING_WAND.get());
        handheldItem(itemModels, ToolsItems.REINFORCED_BREAKING_WAND.get());
        handheldItem(itemModels, ToolsItems.MINING_WAND.get());
        handheldItem(itemModels, ToolsItems.REINFORCED_MINING_WAND.get());

        armor(itemModels, ToolsItems.CHICKEN_SUIT_HELMET.get());
        armor(itemModels, ToolsItems.CHICKEN_SUIT_CHESTPLATE.get());
        armor(itemModels, ToolsItems.CHICKEN_SUIT_LEGGINGS.get());
        armor(itemModels, ToolsItems.CHICKEN_SUIT_BOOTS.get());

        tool(itemModels, ToolsItems.WOODEN_MULTITOOL.get());
        tool(itemModels, ToolsItems.STONE_MULTITOOL.get());
        tool(itemModels, ToolsItems.GOLDEN_MULTITOOL.get());
        tool(itemModels, ToolsItems.IRON_MULTITOOL.get());
        tool(itemModels, ToolsItems.DIAMOND_MULTITOOL.get());
        tool(itemModels, ToolsItems.NETHERITE_MULTITOOL.get());

        shear(itemModels, ToolsItems.WOOD_SHEARS.get());
        shear(itemModels, ToolsItems.STONE_SHEARS.get());
        shear(itemModels, ToolsItems.GOLD_SHEARS.get());
        shear(itemModels, ToolsItems.DIAMOND_SHEARS.get());
        shear(itemModels, ToolsItems.NETHERITE_SHEARS.get());

        spear(itemModels, ToolsItems.WOOD_SPEAR.get());
        spear(itemModels, ToolsItems.STONE_SPEAR.get());
        spear(itemModels, ToolsItems.IRON_SPEAR.get());
        spear(itemModels, ToolsItems.GOLD_SPEAR.get());
        spear(itemModels, ToolsItems.DIAMOND_SPEAR.get());
        spear(itemModels, ToolsItems.NETHERITE_SPEAR.get());

        // One shared fluid layer per fluid - the layer is the same whatever bucket it sits in.
        List<ItemModel.Unbaked> fluidLayers = BUCKET_FLUIDS.stream().map(f -> fluidLayer(itemModels, f)).toList();

        bucket(itemModels, fluidLayers, ToolsItems.WOOD_BUCKET.get(), ToolsItems.WOOD_MILK_BUCKET.get());
        bucket(itemModels, fluidLayers, ToolsItems.STONE_BUCKET.get(), ToolsItems.STONE_MILK_BUCKET.get());
        bucket(itemModels, fluidLayers, ToolsItems.GOLD_BUCKET.get(), ToolsItems.GOLD_MILK_BUCKET.get());
        bucket(itemModels, fluidLayers, ToolsItems.DIAMOND_BUCKET.get(), ToolsItems.DIAMOND_MILK_BUCKET.get());
        bucket(itemModels, fluidLayers, ToolsItems.NETHERITE_BUCKET.get(), ToolsItems.NETHERITE_MILK_BUCKET.get());

        ToolsItems.MATERIAL_GROUPS.forEach((s, group) -> {
            tool(itemModels, group.PICKAXE.get());
            tool(itemModels, group.SHOVEL.get());
            tool(itemModels, group.AXE.get());
            tool(itemModels, group.HOE.get());
            tool(itemModels, group.SWORD.get());
            tool(itemModels, group.HAMMER.get());
            tool(itemModels, group.MULTITOOL.get());
            spear(itemModels, group.SPEAR.get());

            armor(itemModels, group.HELMET.get());
            armor(itemModels, group.CHESTPLATE.get());
            armor(itemModels, group.LEGGINGS.get());
            armor(itemModels, group.BOOTS.get());

            bucket(itemModels, fluidLayers, group.BUCKET.get(), group.MILK_BUCKET.get());
            shear(itemModels, group.SHEARS.get());
        });
    }

    // ------------------------------------------------------------------ flat items

    private void shear(ItemModelGenerators itemModels, Item item) {
        flatItem(itemModels, item, ModelTemplates.FLAT_HANDHELD_ITEM, "item/shears/" + name(item));
    }

    private void tool(ItemModelGenerators itemModels, Item item) {
        flatItem(itemModels, item, ModelTemplates.FLAT_HANDHELD_ITEM, "item/tools/" + name(item));
    }

    private void armor(ItemModelGenerators itemModels, Item item) {
        flatItem(itemModels, item, ModelTemplates.FLAT_ITEM, "item/armors/" + name(item));
    }

    private void generatedItem(ItemModelGenerators itemModels, Item item) {
        flatItem(itemModels, item, ModelTemplates.FLAT_ITEM, "item/" + name(item));
    }

    private void handheldItem(ItemModelGenerators itemModels, Item item) {
        flatItem(itemModels, item, ModelTemplates.FLAT_HANDHELD_ITEM, "item/" + name(item));
    }

    private void flatItem(ItemModelGenerators itemModels, Item item, ModelTemplate template, String texture) {
        Identifier model = template.create(modelId(name(item)), TextureMapping.layer0(prefixed(texture)), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(model));
    }

    // ------------------------------------------------------------------ spears

    /**
     * Modelled on vanilla's {@code generateTrident}, which is the same shape of item: flat in the
     * inventory, a {@code SpecialModelRenderer} everywhere else, and a second special entry for the
     * throwing pose.
     * <p>
     * The three things the deleted {@code SpearBEWLR} did in code all land here:
     * {@code createFlatModelDispatch} is the GUI/GROUND/FIXED branch, {@code isUsingItem()} is the
     * old {@code assortedtools:throwing} {@code ClampedItemPropertyFunction}, and
     * {@code TridentSpecialRenderer.DEFAULT_TRANSFORMATION} is its {@code scale(1, -1, -1)}. The
     * texture the renderer resolved per stack is baked into each {@code Unbaked} instead.
     */
    private void spear(ItemModelGenerators itemModels, Item item) {
        String name = name(item);
        Material particle = prefixed("item/tools/" + name);

        Identifier guiModel = ModelTemplates.FLAT_ITEM.create(modelId(name + "_gui"), TextureMapping.layer0(particle), itemModels.modelOutput);
        Identifier inHandModel = SPEAR_IN_HAND.create(modelId(name), particleMapping(particle), itemModels.modelOutput);
        Identifier throwingModel = SPEAR_THROWING.create(modelId(name + "_throwing"), particleMapping(particle), itemModels.modelOutput);

        // The entity texture, not the atlas sprite - SpearSpecialRenderer draws a Model, and
        // SpearModel resolved exactly this path per item before.
        SpearSpecialRenderer.Unbaked renderer = new SpearSpecialRenderer.Unbaked(
                Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/projectiles/" + name + ".png"));

        ItemModel.Unbaked flat = ItemModelUtils.plainModel(guiModel);
        ItemModel.Unbaked inHand = ItemModelUtils.conditional(
                TridentSpecialRenderer.DEFAULT_TRANSFORMATION,
                ItemModelUtils.isUsingItem(),
                ItemModelUtils.specialModel(throwingModel, renderer),
                ItemModelUtils.specialModel(inHandModel, renderer));

        itemModels.itemModelOutput.accept(item, ItemModelGenerators.createFlatModelDispatch(flat, inHand));
    }

    // ------------------------------------------------------------------ buckets

    /**
     * The bucket, and the milk bucket item that shares its texture.
     * <p>
     * {@code ItemOverrides} was deleted outright, so the 1.20.1 {@code ContainedFluidOverrideHandler}
     * - which re-baked the model with the stack's fluid - has no equivalent: a model is chosen
     * <em>before</em> baking now. The replacement is a plain vanilla
     * {@code minecraft:custom_model_data} {@code select} with one case per fluid, keyed on the string
     * {@code BetterBucketItem} stores. That is pure vanilla, needs no loader side property
     * registration ({@code SelectItemModelProperties.ID_MAPPER} is private and vanilla only) and
     * behaves identically on both loaders.
     * <p>
     * <b>The body and the fluid are separate models joined by {@code minecraft:composite}, and that
     * is not cosmetic.</b> {@code CuboidItemModelWrapper} refuses to bake a model whose quads span
     * two texture atlases, and they would: the bucket's own textures resolve on the item atlas while
     * the fluid sprite comes from the fluid's baked {@code FluidModel}, which
     * {@code ModelManager} bakes with a block-atlas-only material baker. Each composite member bakes
     * on its own, so each stays single atlas. The fluid layer's depth offset lives inside its own
     * model, so the two still stack the way they always did.
     */
    private void bucket(ItemModelGenerators itemModels, List<ItemModel.Unbaked> fluidLayers, Item bucket, Item milkBucket) {
        String name = name(bucket);
        Material bucketTexture = prefixed("item/buckets/" + name);

        Identifier bodyModel = BUCKET_BODY.create(modelId(name), new TextureMapping()
                .put(TextureSlot.PARTICLE, bucketTexture)
                .put(BASE, bucketTexture)
                .put(FLUID, prefixed("item/buckets/bucket_fluid"))
                .put(COVER, prefixed("item/buckets/bucket_covered")), itemModels.modelOutput);

        ItemModel.Unbaked body = ItemModelUtils.plainModel(bodyModel);

        List<SelectItemModel.SwitchCase<String>> cases = IntStream.range(0, BUCKET_FLUIDS.size())
                .mapToObj(i -> ItemModelUtils.when(BUCKET_FLUIDS.get(i).componentValues(), ItemModelUtils.composite(body, fluidLayers.get(i))))
                .toList();

        itemModels.itemModelOutput.accept(bucket, ItemModelUtils.select(new CustomModelDataProperty(0), body, cases));

        // The milk bucket is its own item and has always been a plain two layer flat model over the
        // matching bucket's texture.
        Identifier milkModel = ModelTemplates.TWO_LAYERED_ITEM.create(modelId(name(milkBucket)),
                TextureMapping.layered(bucketTexture, prefixed("item/buckets/overlay_milk")), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(milkBucket, ItemModelUtils.plainModel(milkModel));
    }

    /**
     * One fluid layer model, shared by every bucket: the loader masks the fluid's own sprite with
     * {@code bucket_fluid} and the mask is the same whatever the bucket is made of. No {@code base}
     * and no {@code cover} slot, so the only quads it produces are the fluid's.
     * <p>
     * {@code FluidContainerTintSource} is listed at tint index 1 because that is the index
     * {@code createUnbakedItemMaskElements(1, ...)} stamps on the fluid layer's quads; index 0 is the
     * blank constant vanilla uses for an untinted slot.
     */
    private ItemModel.Unbaked fluidLayer(ItemModelGenerators itemModels, BucketFluid fluid) {
        ModelTemplate template = ExtendedModelTemplateBuilder.builder()
                .parent(LIB_DEFAULT_ITEM)
                .requiredTextureSlot(FLUID)
                .customLoader(FluidContainerBuilder::begin, b -> b
                        .fluid(fluid.fluid())
                        .flipGas(true)
                        .applyFluidLuminosity(true)
                        .coverIsMask(true))
                .build();

        Identifier model = template.create(modelId("buckets/fluid_" + fluid.modelSuffix()),
                new TextureMapping().put(FLUID, prefixed("item/buckets/bucket_fluid")), itemModels.modelOutput);

        return ItemModelUtils.tintedModel(model, ItemModelUtils.constantTint(-1), new FluidContainerTintSource());
    }

    // ------------------------------------------------------------------ helpers

    private static TextureMapping particleMapping(Material particle) {
        return new TextureMapping().put(TextureSlot.PARTICLE, particle);
    }

    private static String name(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).getPath();
    }

    private static Identifier modelId(String path) {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, "item/" + path);
    }

    private static Material prefixed(String texture) {
        return new Material(Identifier.fromNamespaceAndPath(Constants.MOD_ID, texture));
    }
}
