package com.grim3212.assorted.tools.common.handlers;

import com.grim3212.assorted.lib.core.creative.CreativeTabItems;
import com.grim3212.assorted.lib.util.NBTHelper;
import com.grim3212.assorted.tools.ToolsCommonMod;
import com.grim3212.assorted.tools.api.item.ToolsItemTier;
import com.grim3212.assorted.tools.common.item.BetterBucketItem;
import com.grim3212.assorted.tools.common.item.BetterMilkBucketItem;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.grim3212.assorted.tools.common.item.WandBreakingItem.BreakingMode;
import com.grim3212.assorted.tools.common.item.WandBuildingItem.BuildingMode;
import com.grim3212.assorted.tools.common.item.WandMiningItem.MiningMode;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ToolsCreativeItems {

    public static List<ItemStack> getCreativeItems() {
        CreativeTabItems items = new CreativeTabItems();

        if (ToolsCommonMod.COMMON_CONFIG.pokeballEnabled.get()) {
            items.add(ToolsItems.POKEBALL.get());
        }

        if (ToolsCommonMod.COMMON_CONFIG.ultimateFistEnabled.get()) {
            items.add(ToolsItems.ULTIMATE_FIST.get());
            items.add(ToolsItems.A_FRAGMENT.get());
            items.add(ToolsItems.E_FRAGMENT.get());
            items.add(ToolsItems.I_FRAGMENT.get());
            items.add(ToolsItems.L_FRAGMENT.get());
            items.add(ToolsItems.M_FRAGMENT.get());
            items.add(ToolsItems.T_FRAGMENT.get());
            items.add(ToolsItems.U_FRAGMENT.get());
            items.add(ToolsItems.MISSING_FRAGMENT.get());
        }

        if (ToolsCommonMod.COMMON_CONFIG.chickenSuitEnabled.get()) {
            items.add(ToolsItems.CHICKEN_SUIT_HELMET.get());
            items.add(ToolsItems.CHICKEN_SUIT_CHESTPLATE.get());
            items.add(ToolsItems.CHICKEN_SUIT_LEGGINGS.get());
            items.add(ToolsItems.CHICKEN_SUIT_BOOTS.get());
        }

        if (ToolsCommonMod.COMMON_CONFIG.boomerangsEnabled.get()) {
            items.add(ToolsItems.WOOD_BOOMERANG.get());
            items.add(ToolsItems.DIAMOND_BOOMERANG.get());
        }

        if (ToolsCommonMod.COMMON_CONFIG.wandsEnabled.get()) {
            items.add(NBTHelper.putStringItemStack(new ItemStack(ToolsItems.BREAKING_WAND.get()), "Mode", BreakingMode.BREAK_WEAK.getSerializedName()));
            items.add(NBTHelper.putStringItemStack(new ItemStack(ToolsItems.BUILDING_WAND.get()), "Mode", BuildingMode.BUILD_BOX.getSerializedName()));
            items.add(NBTHelper.putStringItemStack(new ItemStack(ToolsItems.MINING_WAND.get()), "Mode", MiningMode.MINE_ALL.getSerializedName()));
            items.add(NBTHelper.putStringItemStack(new ItemStack(ToolsItems.REINFORCED_BREAKING_WAND.get()), "Mode", BreakingMode.BREAK_WEAK.getSerializedName()));
            items.add(NBTHelper.putStringItemStack(new ItemStack(ToolsItems.REINFORCED_BUILDING_WAND.get()), "Mode", BuildingMode.BUILD_BOX.getSerializedName()));
            items.add(NBTHelper.putStringItemStack(new ItemStack(ToolsItems.REINFORCED_MINING_WAND.get()), "Mode", MiningMode.MINE_ALL.getSerializedName()));
        }

        if (ToolsCommonMod.COMMON_CONFIG.betterBucketsEnabled.get()) {
            registerBucket(items, ToolsItems.WOOD_BUCKET.get(), ToolsItems.WOOD_MILK_BUCKET.get());
            registerBucket(items, ToolsItems.STONE_BUCKET.get(), ToolsItems.STONE_MILK_BUCKET.get());
            registerBucket(items, ToolsItems.GOLD_BUCKET.get(), ToolsItems.GOLD_MILK_BUCKET.get());
            registerBucket(items, ToolsItems.DIAMOND_BUCKET.get(), ToolsItems.DIAMOND_MILK_BUCKET.get());
            registerBucket(items, ToolsItems.NETHERITE_BUCKET.get(), ToolsItems.NETHERITE_MILK_BUCKET.get());
        }

        if (ToolsCommonMod.COMMON_CONFIG.betterSpearsEnabled.get()) {
            items.add(ToolsItems.WOOD_SPEAR.get());
            items.add(ToolsItems.STONE_SPEAR.get());
            items.add(ToolsItems.IRON_SPEAR.get());
            items.add(ToolsItems.GOLD_SPEAR.get());
            items.add(ToolsItems.DIAMOND_SPEAR.get());
            items.add(ToolsItems.NETHERITE_SPEAR.get());
        }

        if (ToolsCommonMod.COMMON_CONFIG.hammersEnabled.get()) {
            items.add(ToolsItems.WOOD_HAMMER.get());
            items.add(ToolsItems.STONE_HAMMER.get());
            items.add(ToolsItems.IRON_HAMMER.get());
            items.add(ToolsItems.GOLD_HAMMER.get());
            items.add(ToolsItems.DIAMOND_HAMMER.get());
            items.add(ToolsItems.NETHERITE_HAMMER.get());
        }

        if (ToolsCommonMod.COMMON_CONFIG.multiToolsEnabled.get()) {
            items.add(ToolsItems.WOODEN_MULTITOOL.get());
            items.add(ToolsItems.STONE_MULTITOOL.get());
            items.add(ToolsItems.IRON_MULTITOOL.get());
            items.add(ToolsItems.GOLDEN_MULTITOOL.get());
            items.add(ToolsItems.DIAMOND_MULTITOOL.get());
            items.add(ToolsItems.NETHERITE_MULTITOOL.get());
        }

        if (ToolsCommonMod.COMMON_CONFIG.moreShearsEnabled.get()) {
            items.add(ToolsItems.WOOD_SHEARS.get());
            items.add(ToolsItems.STONE_SHEARS.get());
            items.add(ToolsItems.GOLD_SHEARS.get());
            items.add(ToolsItems.DIAMOND_SHEARS.get());
            items.add(ToolsItems.NETHERITE_SHEARS.get());
        }

        if (ToolsCommonMod.COMMON_CONFIG.extraMaterialsEnabled.get()) {
            ToolsItems.MATERIAL_GROUPS.forEach((s, group) -> {
                ToolsItemTier tier = (ToolsItemTier) group.tier.getDefaultTier();
                if (ToolsCommonMod.COMMON_CONFIG.hideUncraftableItems.get() && BuiltInRegistries.ITEM.getTag(tier.repairTag()).isPresent() && BuiltInRegistries.ITEM.getTag(tier.repairTag()).get().stream().count() < 1) {
                    return;
                }

                items.add(group.SWORD.get());
                items.add(group.PICKAXE.get());
                items.add(group.AXE.get());
                items.add(group.SHOVEL.get());
                items.add(group.HOE.get());
                items.add(group.HELMET.get());
                items.add(group.CHESTPLATE.get());
                items.add(group.LEGGINGS.get());
                items.add(group.BOOTS.get());

                if (ToolsCommonMod.COMMON_CONFIG.moreShearsEnabled.get()) {
                    items.add(group.SHEARS.get());
                }

                if (ToolsCommonMod.COMMON_CONFIG.hammersEnabled.get()) {
                    items.add(group.HAMMER.get());
                }

                if (ToolsCommonMod.COMMON_CONFIG.multiToolsEnabled.get()) {
                    items.add(group.MULTITOOL.get());
                }

                if (ToolsCommonMod.COMMON_CONFIG.betterSpearsEnabled.get()) {
                    items.add(group.SPEAR.get());
                }

                if (ToolsCommonMod.COMMON_CONFIG.betterBucketsEnabled.get()) {
                    registerBucket(items, group.BUCKET.get(), group.MILK_BUCKET.get());
                }

            });
        }

        return items.getItems();
    }

    private static ItemStack genMilkBucket(BetterMilkBucketItem milkBucket) {
        ItemStack stack = new ItemStack(milkBucket);
        BetterBucketItem.setFluid(stack, "milk");
        BetterBucketItem.setAmount(stack, milkBucket.getParent().getMaximumMillibuckets());
        return stack;
    }

    private static void registerBucket(CreativeTabItems output, BetterBucketItem bucket, BetterMilkBucketItem milkBucket) {
        output.add(bucket);
        output.add(genMilkBucket(milkBucket));
    }
}
