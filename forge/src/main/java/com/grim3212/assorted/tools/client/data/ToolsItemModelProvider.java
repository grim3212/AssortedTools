package com.grim3212.assorted.tools.client.data;

import com.grim3212.assorted.lib.LibConstants;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ToolsItemModelProvider extends ItemModelProvider {

    public ToolsItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Tools item models";
    }

    @Override
    protected void registerModels() {
        tool(ToolsItems.WOOD_HAMMER.get());
        tool(ToolsItems.STONE_HAMMER.get());
        tool(ToolsItems.GOLD_HAMMER.get());
        tool(ToolsItems.IRON_HAMMER.get());
        tool(ToolsItems.DIAMOND_HAMMER.get());
        tool(ToolsItems.NETHERITE_HAMMER.get());
        handheldItem(ToolsItems.WOOD_BOOMERANG.get());
        handheldItem(ToolsItems.DIAMOND_BOOMERANG.get());
        generatedItem(ToolsItems.POKEBALL.get());

        handheldItem(ToolsItems.ULTIMATE_FIST.get());
        generatedItem(ToolsItems.U_FRAGMENT.get());
        generatedItem(ToolsItems.L_FRAGMENT.get());
        generatedItem(ToolsItems.T_FRAGMENT.get());
        generatedItem(ToolsItems.I_FRAGMENT.get());
        generatedItem(ToolsItems.M_FRAGMENT.get());
        generatedItem(ToolsItems.A_FRAGMENT.get());
        generatedItem(ToolsItems.MISSING_FRAGMENT.get());
        generatedItem(ToolsItems.E_FRAGMENT.get());

        handheldItem(ToolsItems.BUILDING_WAND.get());
        handheldItem(ToolsItems.REINFORCED_BUILDING_WAND.get());
        handheldItem(ToolsItems.BREAKING_WAND.get());
        handheldItem(ToolsItems.REINFORCED_BREAKING_WAND.get());
        handheldItem(ToolsItems.MINING_WAND.get());
        handheldItem(ToolsItems.REINFORCED_MINING_WAND.get());

        armor(ToolsItems.CHICKEN_SUIT_HELMET.get());
        armor(ToolsItems.CHICKEN_SUIT_CHESTPLATE.get());
        armor(ToolsItems.CHICKEN_SUIT_LEGGINGS.get());
        armor(ToolsItems.CHICKEN_SUIT_BOOTS.get());

        tool(ToolsItems.WOODEN_MULTITOOL.get());
        tool(ToolsItems.STONE_MULTITOOL.get());
        tool(ToolsItems.GOLDEN_MULTITOOL.get());
        tool(ToolsItems.IRON_MULTITOOL.get());
        tool(ToolsItems.DIAMOND_MULTITOOL.get());
        tool(ToolsItems.NETHERITE_MULTITOOL.get());

        shear(ToolsItems.WOOD_SHEARS.get());
        shear(ToolsItems.STONE_SHEARS.get());
        shear(ToolsItems.GOLD_SHEARS.get());
        shear(ToolsItems.DIAMOND_SHEARS.get());
        shear(ToolsItems.NETHERITE_SHEARS.get());

        generateSpear(ToolsItems.WOOD_SPEAR.get());
        generateSpear(ToolsItems.STONE_SPEAR.get());
        generateSpear(ToolsItems.IRON_SPEAR.get());
        generateSpear(ToolsItems.GOLD_SPEAR.get());
        generateSpear(ToolsItems.DIAMOND_SPEAR.get());
        generateSpear(ToolsItems.NETHERITE_SPEAR.get());

        bucketItem(ToolsItems.WOOD_BUCKET.get(), ToolsItems.WOOD_MILK_BUCKET.get());
        bucketItem(ToolsItems.STONE_BUCKET.get(), ToolsItems.STONE_MILK_BUCKET.get());
        bucketItem(ToolsItems.GOLD_BUCKET.get(), ToolsItems.GOLD_MILK_BUCKET.get());
        bucketItem(ToolsItems.DIAMOND_BUCKET.get(), ToolsItems.DIAMOND_MILK_BUCKET.get());
        bucketItem(ToolsItems.NETHERITE_BUCKET.get(), ToolsItems.NETHERITE_MILK_BUCKET.get());

        ToolsItems.MATERIAL_GROUPS.forEach((s, group) -> {
            tool(group.PICKAXE.get());
            tool(group.SHOVEL.get());
            tool(group.AXE.get());
            tool(group.HOE.get());
            tool(group.SWORD.get());
            tool(group.HAMMER.get());
            tool(group.MULTITOOL.get());
            generateSpear(group.SPEAR.get());

            armor(group.HELMET.get());
            armor(group.CHESTPLATE.get());
            armor(group.LEGGINGS.get());
            armor(group.BOOTS.get());

            bucketItem(group.BUCKET.get(), group.MILK_BUCKET.get());
            shear(group.SHEARS.get());
        });
    }

    private ItemModelBuilder shear(Item i) {
        String name = name(i);
        return withExistingParent(name, "item/handheld").texture("layer0", prefix("item/shears/" + name));
    }

    private ItemModelBuilder tool(Item i) {
        String name = name(i);
        return withExistingParent(name, "item/handheld").texture("layer0", prefix("item/tools/" + name));
    }

    private ItemModelBuilder armor(Item i) {
        String name = name(i);
        return withExistingParent(name, "item/generated").texture("layer0", prefix("item/armors/" + name));
    }

    private ItemModelBuilder generatedItem(String name) {
        return withExistingParent(name, "item/generated").texture("layer0", prefix("item/" + name));
    }

    private ItemModelBuilder generatedItem(Item i) {
        return generatedItem(name(i));
    }

    private void bucketItem(Item bucket, Item milkBucket) {
        String name = name(bucket);
        getBuilder(name).parent(getExistingFile(new ResourceLocation(LibConstants.MOD_ID, "item/default")))
                .texture("particle", prefix("item/buckets/" + name))
                .texture("base", prefix("item/buckets/" + name))
                .texture("fluid", prefix("item/buckets/bucket_fluid"))
                .texture("cover", prefix("item/buckets/bucket_covered"))
                .customLoader(FluidContainerBuilder::begin)
                .fluid(Fluids.EMPTY)
                .flipGas(true)
                .applyTint(true)
                .applyFluidLuminosity(true)
                .coverIsMask(true)
                .end();
        withExistingParent(name(milkBucket), "item/generated")
                .texture("layer0", prefix("item/buckets/" + name))
                .texture("layer1", prefix("item/buckets/overlay_milk"));
    }

    private ItemModelBuilder handheldItem(String name) {
        return withExistingParent(name, "item/handheld").texture("layer0", prefix("item/" + name));
    }

    private ItemModelBuilder handheldItem(Item i) {
        return handheldItem(name(i));
    }

    private static String name(Item i) {
        return ForgeRegistries.ITEMS.getKey(i).getPath();
    }

    private ResourceLocation prefix(String name) {
        return new ResourceLocation(Constants.MOD_ID, name);
    }

    private void generateSpear(Item item) {
        String name = name(item);
        withExistingParent(name + "_gui", "item/generated").texture("layer0", prefix("item/tools/" + name));
        ItemModelBuilder throwingModel = withExistingParent(name + "_throwing", "item/trident_throwing")
                .texture("particle", prefix("item/tools/" + name));

        withExistingParent(name, "item/trident_in_hand")
                .texture("particle", prefix("item/tools/" + name))
                // Add head transformation
                .transforms().transform(ItemDisplayContext.HEAD).rotation(0, 180, 120).translation(8, 10, -11).scale(1.5F).end().end()
                .override().predicate(prefix("throwing"), 1).model(throwingModel).end();
    }
}
