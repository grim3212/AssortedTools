package com.grim3212.assorted.tools.client.data;

import com.grim3212.assorted.lib.LibConstants;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.client.model.fluidcontainer.FluidContainerItemModel;
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

import java.util.stream.Stream;

/**
 * Item models for the whole mod. Most textures live in subfolders ({@code item/tools/}, {@code
 * item/armors/}, ...), so flat models are built with an explicit {@code TextureMapping} rather than
 * {@code generateFlatItem}. {@link #spear} and {@link #bucket} are not plain flat items.
 */
public class ToolsItemModelProvider extends ModelProvider {

    /**
     * The extra texture slots {@link FluidContainerItemModel} reads. {@link TextureSlot} has no
     * {@code equals}, so each of these has to be created exactly once and shared.
     */
    private static final TextureSlot BASE = TextureSlot.create("base");
    private static final TextureSlot FLUID = TextureSlot.create("fluid");
    private static final TextureSlot COVER = TextureSlot.create("cover");

    private static final Identifier LIB_DEFAULT_ITEM = Identifier.fromNamespaceAndPath(LibConstants.MOD_ID, "item/default");

    /**
     * The in-hand spear model and its throwing pose, on the vanilla trident parents. Only {@code
     * head} is set; every other perspective is inherited from {@code
     * minecraft:item/trident_in_hand}.
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
     * The bucket body: the four texture slots {@link FluidContainerItemModel} masks and fills, plus
     * the display transforms every layer uses. A plain model json, not a loader.
     */
    private static final ModelTemplate BUCKET_BODY = ExtendedModelTemplateBuilder.builder()
            .parent(LIB_DEFAULT_ITEM)
            .requiredTextureSlot(TextureSlot.PARTICLE)
            .requiredTextureSlot(BASE)
            .requiredTextureSlot(FLUID)
            .requiredTextureSlot(COVER)
            .build();

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

        bucket(itemModels, ToolsItems.WOOD_BUCKET.get(), ToolsItems.WOOD_MILK_BUCKET.get());
        bucket(itemModels, ToolsItems.STONE_BUCKET.get(), ToolsItems.STONE_MILK_BUCKET.get());
        bucket(itemModels, ToolsItems.GOLD_BUCKET.get(), ToolsItems.GOLD_MILK_BUCKET.get());
        bucket(itemModels, ToolsItems.DIAMOND_BUCKET.get(), ToolsItems.DIAMOND_MILK_BUCKET.get());
        bucket(itemModels, ToolsItems.NETHERITE_BUCKET.get(), ToolsItems.NETHERITE_MILK_BUCKET.get());

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

            bucket(itemModels, group.BUCKET.get(), group.MILK_BUCKET.get());
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
     * Modelled on vanilla's {@code generateTrident}: flat in the GUI, ground and fixed contexts, a
     * {@code SpecialModelRenderer} elsewhere, and a second special entry for the throwing pose. The
     * texture is baked into each {@code Unbaked}. Vanilla's {@code generateSpear} has no special
     * renderer or throwing pose, so it is the wrong shape.
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
     * The bucket and the milk bucket that shares its texture: an {@code
     * assortedtools:fluid_container} item model, which draws whatever fluid the stack holds at
     * render time.
     */
    private void bucket(ItemModelGenerators itemModels, Item bucket, Item milkBucket) {
        String name = name(bucket);
        Material bucketTexture = prefixed("item/buckets/" + name);

        Identifier bodyModel = BUCKET_BODY.create(modelId(name), new TextureMapping()
                .put(TextureSlot.PARTICLE, bucketTexture)
                .put(BASE, bucketTexture)
                .put(FLUID, prefixed("item/buckets/bucket_fluid"))
                .put(COVER, prefixed("item/buckets/bucket_covered")), itemModels.modelOutput);

        itemModels.itemModelOutput.accept(bucket, new FluidContainerItemModel.Unbaked(bodyModel, true, true, true));

        // The milk bucket is its own item and has always been a plain two layer flat model over the
        // matching bucket's texture.
        Identifier milkModel = ModelTemplates.TWO_LAYERED_ITEM.create(modelId(name(milkBucket)),
                TextureMapping.layered(bucketTexture, prefixed("item/buckets/overlay_milk")), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(milkBucket, ItemModelUtils.plainModel(milkModel));
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
