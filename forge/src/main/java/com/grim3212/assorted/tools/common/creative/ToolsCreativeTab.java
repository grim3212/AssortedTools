package com.grim3212.assorted.tools.common.creative;

import com.grim3212.assorted.decor.Constants;
import com.grim3212.assorted.decor.api.item.ToolsItemTier;
import com.grim3212.assorted.decor.common.item.BetterBucketItem;
import com.grim3212.assorted.decor.common.item.BetterMilkBucketItem;
import com.grim3212.assorted.decor.common.item.ToolsItems;
import com.grim3212.assorted.decor.common.item.WandBreakingItem.BreakingMode;
import com.grim3212.assorted.decor.common.item.WandBuildingItem.BuildingMode;
import com.grim3212.assorted.decor.common.item.WandMiningItem.MiningMode;
import com.grim3212.assorted.lib.util.NBTHelper;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ToolsCreativeTab {

    public static void registerTabs(final CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(Constants.MOD_ID, "tab"), builder -> builder.title(Component.translatable("itemGroup.assortedtools")).icon(() -> new ItemStack(ToolsItems.IRON_HAMMER.get())).displayItems((enabledFlags, populator, hasPermissions) -> {

            if (ToolsConfig.COMMON.pokeballEnabled.get()) {
                populator.accept(ToolsItems.POKEBALL.get());
            }

            if (ToolsConfig.COMMON.ultimateFistEnabled.get()) {
                populator.accept(ToolsItems.ULTIMATE_FIST.get());
                populator.accept(ToolsItems.A_FRAGMENT.get());
                populator.accept(ToolsItems.E_FRAGMENT.get());
                populator.accept(ToolsItems.I_FRAGMENT.get());
                populator.accept(ToolsItems.L_FRAGMENT.get());
                populator.accept(ToolsItems.M_FRAGMENT.get());
                populator.accept(ToolsItems.T_FRAGMENT.get());
                populator.accept(ToolsItems.U_FRAGMENT.get());
                populator.accept(ToolsItems.MISSING_FRAGMENT.get());
            }

            if (ToolsConfig.COMMON.chickenSuitEnabled.get()) {
                populator.accept(ToolsItems.CHICKEN_SUIT_HELMET.get());
                populator.accept(ToolsItems.CHICKEN_SUIT_CHESTPLATE.get());
                populator.accept(ToolsItems.CHICKEN_SUIT_LEGGINGS.get());
                populator.accept(ToolsItems.CHICKEN_SUIT_BOOTS.get());
            }

            if (ToolsConfig.COMMON.boomerangsEnabled.get()) {
                populator.accept(ToolsItems.WOOD_BOOMERANG.get());
                populator.accept(ToolsItems.DIAMOND_BOOMERANG.get());
            }

            if (ToolsConfig.COMMON.wandsEnabled.get()) {
                populator.accept(NBTHelper.putStringItemStack(new ItemStack(ToolsItems.BREAKING_WAND.get()), "Mode", BreakingMode.BREAK_WEAK.getSerializedName()));
                populator.accept(NBTHelper.putStringItemStack(new ItemStack(ToolsItems.BUILDING_WAND.get()), "Mode", BuildingMode.BUILD_BOX.getSerializedName()));
                populator.accept(NBTHelper.putStringItemStack(new ItemStack(ToolsItems.MINING_WAND.get()), "Mode", MiningMode.MINE_ALL.getSerializedName()));
                populator.accept(NBTHelper.putStringItemStack(new ItemStack(ToolsItems.REINFORCED_BREAKING_WAND.get()), "Mode", BreakingMode.BREAK_WEAK.getSerializedName()));
                populator.accept(NBTHelper.putStringItemStack(new ItemStack(ToolsItems.REINFORCED_BUILDING_WAND.get()), "Mode", BuildingMode.BUILD_BOX.getSerializedName()));
                populator.accept(NBTHelper.putStringItemStack(new ItemStack(ToolsItems.REINFORCED_MINING_WAND.get()), "Mode", MiningMode.MINE_ALL.getSerializedName()));
            }

            if (ToolsConfig.COMMON.betterBucketsEnabled.get()) {
                registerBucket(populator, ToolsItems.WOOD_BUCKET.get(), ToolsItems.WOOD_MILK_BUCKET.get());
                registerBucket(populator, ToolsItems.STONE_BUCKET.get(), ToolsItems.STONE_MILK_BUCKET.get());
                registerBucket(populator, ToolsItems.GOLD_BUCKET.get(), ToolsItems.GOLD_MILK_BUCKET.get());
                registerBucket(populator, ToolsItems.DIAMOND_BUCKET.get(), ToolsItems.DIAMOND_MILK_BUCKET.get());
                registerBucket(populator, ToolsItems.NETHERITE_BUCKET.get(), ToolsItems.NETHERITE_MILK_BUCKET.get());
            }

            if (ToolsConfig.COMMON.betterSpearsEnabled.get()) {
                populator.accept(ToolsItems.WOOD_SPEAR.get());
                populator.accept(ToolsItems.STONE_SPEAR.get());
                populator.accept(ToolsItems.IRON_SPEAR.get());
                populator.accept(ToolsItems.GOLD_SPEAR.get());
                populator.accept(ToolsItems.DIAMOND_SPEAR.get());
                populator.accept(ToolsItems.NETHERITE_SPEAR.get());
            }

            if (ToolsConfig.COMMON.hammersEnabled.get()) {
                populator.accept(ToolsItems.WOOD_HAMMER.get());
                populator.accept(ToolsItems.STONE_HAMMER.get());
                populator.accept(ToolsItems.IRON_HAMMER.get());
                populator.accept(ToolsItems.GOLD_HAMMER.get());
                populator.accept(ToolsItems.DIAMOND_HAMMER.get());
                populator.accept(ToolsItems.NETHERITE_HAMMER.get());
            }

            if (ToolsConfig.COMMON.multiToolsEnabled.get()) {
                populator.accept(ToolsItems.WOODEN_MULTITOOL.get());
                populator.accept(ToolsItems.STONE_MULTITOOL.get());
                populator.accept(ToolsItems.IRON_MULTITOOL.get());
                populator.accept(ToolsItems.GOLDEN_MULTITOOL.get());
                populator.accept(ToolsItems.DIAMOND_MULTITOOL.get());
                populator.accept(ToolsItems.NETHERITE_MULTITOOL.get());
            }

            if (ToolsConfig.COMMON.moreShearsEnabled.get()) {
                populator.accept(ToolsItems.WOOD_SHEARS.get());
                populator.accept(ToolsItems.STONE_SHEARS.get());
                populator.accept(ToolsItems.GOLD_SHEARS.get());
                populator.accept(ToolsItems.DIAMOND_SHEARS.get());
                populator.accept(ToolsItems.NETHERITE_SHEARS.get());
            }

            if (ToolsConfig.COMMON.extraMaterialsEnabled.get()) {
                ToolsItems.MATERIAL_GROUPS.forEach((s, group) -> {
                    ToolsItemTier tier = (ToolsItemTier) group.tier.getDefaultTier();
                    if (ToolsConfig.COMMON.hideUncraftableItems.get() && ForgeRegistries.ITEMS.tags().getTag(tier.repairTag()).size() <= 0) {
                        return;
                    }

                    populator.accept(group.SWORD.get());
                    populator.accept(group.PICKAXE.get());
                    populator.accept(group.AXE.get());
                    populator.accept(group.SHOVEL.get());
                    populator.accept(group.HOE.get());
                    populator.accept(group.HELMET.get());
                    populator.accept(group.CHESTPLATE.get());
                    populator.accept(group.LEGGINGS.get());
                    populator.accept(group.BOOTS.get());

                    if (ToolsConfig.COMMON.moreShearsEnabled.get()) {
                        populator.accept(group.SHEARS.get());
                    }

                    if (ToolsConfig.COMMON.hammersEnabled.get()) {
                        populator.accept(group.HAMMER.get());
                    }

                    if (ToolsConfig.COMMON.multiToolsEnabled.get()) {
                        populator.accept(group.MULTITOOL.get());
                    }

                    if (ToolsConfig.COMMON.betterSpearsEnabled.get()) {
                        populator.accept(group.SPEAR.get());
                    }

                    if (ToolsConfig.COMMON.betterBucketsEnabled.get()) {
                        registerBucket(populator, group.BUCKET.get(), group.MILK_BUCKET.get());
                    }

                });
            }
        }));
    }

    private static ItemStack genMilkBucket(BetterMilkBucketItem milkBucket) {
        ItemStack stack = new ItemStack(milkBucket);
        BetterBucketItem.setFluid(stack, "milk");
        BetterBucketItem.setAmount(stack, milkBucket.getParent().getMaximumMillibuckets());
        return stack;
    }

    private static void registerBucket(CreativeModeTab.Output output, BetterBucketItem bucket, BetterMilkBucketItem milkBucket) {
        output.accept(bucket);
        output.accept(genMilkBucket(milkBucket));
    }
}
