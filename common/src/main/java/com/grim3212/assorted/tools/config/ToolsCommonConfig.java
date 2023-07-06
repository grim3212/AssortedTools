package com.grim3212.assorted.tools.config;

import com.google.common.collect.Lists;
import com.grim3212.assorted.lib.config.ConfigurationType;
import com.grim3212.assorted.lib.config.IConfigurationBuilder;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.api.item.ToolsArmorMaterials;
import com.grim3212.assorted.tools.api.item.ToolsItemTier;
import net.minecraft.world.item.Tiers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ToolsCommonConfig {
    public final Supplier<Boolean> wandsEnabled;
    public final Supplier<Boolean> boomerangsEnabled;
    public final Supplier<Boolean> hammersEnabled;
    public final Supplier<Boolean> multiToolsEnabled;
    public final Supplier<Boolean> pokeballEnabled;
    public final Supplier<Boolean> chickenSuitEnabled;
    public final Supplier<Boolean> extraMaterialsEnabled;
    public final Supplier<Boolean> spearsEnabled;
    public final Supplier<Boolean> betterSpearsEnabled;
    public final Supplier<Boolean> betterBucketsEnabled;
    public final Supplier<Boolean> moreShearsEnabled;
    public final Supplier<Boolean> ultimateFistEnabled;

    public final Supplier<Boolean> hideUncraftableItems;
    public final Supplier<Boolean> allowPartialBucketAmounts;
    public final Supplier<Boolean> freeBuildMode;
    public final Supplier<Boolean> bedrockBreaking;
    public final Supplier<Boolean> easyMiningObsidian;

    public final Supplier<Boolean> turnAroundItem;
    public final Supplier<Boolean> turnAroundMob;
    public final Supplier<Boolean> breaksTorches;
    public final Supplier<Boolean> breaksPlants;
    public final Supplier<Boolean> hitsButtons;
    public final Supplier<Boolean> turnAroundButton;
    public final Supplier<Integer> woodBoomerangRange;
    public final Supplier<Integer> woodBoomerangDamage;
    public final Supplier<Integer> diamondBoomerangRange;
    public final Supplier<Integer> diamondBoomerangDamage;
    public final Supplier<Boolean> diamondBoomerangFollows;
    public final Supplier<List<? extends Float>> conductivityLightningChances;

    public final ItemTierConfig woodItemTier;
    public final ItemTierConfig stoneItemTier;
    public final ItemTierConfig goldItemTier;
    public final ItemTierConfig ironItemTier;
    public final ItemTierConfig diamondItemTier;
    public final ItemTierConfig netheriteItemTier;

    public final ItemTierConfig ultimateItemTier;
    public final ArmorMaterialConfig chickenSuitArmorMaterial;

    public final Map<String, ModdedItemTierConfig> moddedTiers;
    public final Map<String, ArmorMaterialConfig> moddedArmors;

    public ToolsCommonConfig() {
        final IConfigurationBuilder builder = Services.CONFIG.createBuilder(ConfigurationType.NOT_SYNCED, Constants.MOD_ID + "-common");

        wandsEnabled = builder.defineBoolean("parts.wandsEnabled", true, "Set this to true if you would like wands to be craftable and found in the creative tab.");
        boomerangsEnabled = builder.defineBoolean("parts.boomerangsEnabled", true, "Set this to true if you would like boomerangs to be craftable and found in the creative tab.");
        hammersEnabled = builder.defineBoolean("parts.hammersEnabled", true, "Set this to true if you would like hammers to be craftable and found in the creative tab.");
        multiToolsEnabled = builder.defineBoolean("parts.multiToolsEnabled", true, "Set this to true if you would like multitools to be craftable and found in the creative tab.");
        pokeballEnabled = builder.defineBoolean("parts.pokeballEnabled", true, "Set this to true if you would like the pokeball to be craftable and found in the creative tab.");
        chickenSuitEnabled = builder.defineBoolean("parts.chickenSuitEnabled", true, "Set this to true if you would like the chicken suit to be craftable and found in the creative tab as well as if you want the Chicken Jump enchantment to be able to be applied.");
        extraMaterialsEnabled = builder.defineBoolean("parts.extraMaterialsEnabled", true, "Set this to true if you would like to enable support for crafting the extra tools and armor that this supports. For example, Steel, Copper, or Ruby tools and armor.");
        spearsEnabled = builder.defineBoolean("parts.spearsEnabled", false, "Set this to true if you would like the old DEPRECATED spears to be craftable and found in the creative tab.");
        betterSpearsEnabled = builder.defineBoolean("parts.betterSpearsEnabled", true, "Set this to true if you would like the better spears (the ones that can be enchanted) to be craftable and found in the creative tab as well as the Enchantments for it to be enchanted on books.");
        betterBucketsEnabled = builder.defineBoolean("parts.betterBucketsEnabled", true, "Set this to true if you would like better buckets to be craftable and found in the creative tab.");
        moreShearsEnabled = builder.defineBoolean("parts.moreShearsEnabled", true, "Set this to true if you would like the extra shears to be craftable and found in the creative tab.");
        ultimateFistEnabled = builder.defineBoolean("parts.ultimateFistEnabled", true, "Set this to true if you would like the ultimate fist to be craftable and found in the creative tab as well as the fragments generate in loot.");

        hideUncraftableItems = builder.defineBoolean("general.hideUncraftableItems", false, "For any item that is unobtainable (like missing materials from other mods) hide it from the creative menu / JEI.");

        allowPartialBucketAmounts = builder.defineBoolean("better_buckets.allowPartialBucketAmounts", false, "Set to true if you would like the better buckets to be able to accept partial bucket amounts. Meaning some can get left over after placing all the full buckets.");

        freeBuildMode = builder.defineBoolean("wands.freeBuildMode", false, "Set to true if you would like the wands to not require any blocks to build with.");
        bedrockBreaking = builder.defineBoolean("wands.bedrockBreaking", false, "Set to true if you would like the breaking wands to be able to break bedrock.");
        easyMiningObsidian = builder.defineBoolean("wands.easyMiningObsidian", false, "Set to true if you would like the mining wands to be able to mine obsidian.");

        turnAroundItem = builder.defineBoolean("boomerangs.turnAroundItem", false, "Set this to true if you would like boomerangs to turn around after they have picked up items.");
        turnAroundMob = builder.defineBoolean("boomerangs.turnAroundMob", false, "Set this to true if you would like boomerangs to turn around after they have hit a mob.");
        breaksTorches = builder.defineBoolean("boomerangs.breaksTorches", false, "Set this to true if you would like boomerangs to be able to break torches.");
        breaksPlants = builder.defineBoolean("boomerangs.breaksPlants", false, "Set this to true if you would like boomerangs to be able to break plants and any other weak blocks.");
        hitsButtons = builder.defineBoolean("boomerangs.hitsButtons", true, "Set this to false if you would like boomerangs to not be able to hit buttons or levers.");
        turnAroundButton = builder.defineBoolean("boomerangs.turnAroundButton", true, "Set this to false if you would like boomerangs to not turn around after they have hit a button or a lever.");
        woodBoomerangRange = builder.defineInteger("boomerangs.woodBoomerangRange", 20, 1, 200, "The maximum range away from the player the wood boomerang will travel before turning around.");
        woodBoomerangDamage = builder.defineInteger("boomerangs.woodBoomerangDamage", 1, 1, 500, "The amount of damage the wood boomerang does to mobs.");
        diamondBoomerangRange = builder.defineInteger("boomerangs.diamondBoomerangRange", 30, 1, 200, "The maximum range away from the player the diamond boomerang will travel before turning around.");
        diamondBoomerangDamage = builder.defineInteger("boomerangs.diamondBoomerangDamage", 5, 1, 200, "The amount of damage the diamond boomerang does to mobs.");
        diamondBoomerangFollows = builder.defineBoolean("boomerangs.diamondBoomerangFollows", false, "Set to true if you would like the diamond boomerang to follow where the player is looking.");

        conductivityLightningChances = builder.defineList("better_spears.conductivityLightningChances", Lists.newArrayList(0.6F, 0.3F, 0.1F), Float.class, "The chances modifier for lightning to spawn at each level of conductivity. The smaller the number the higher chance.");

        woodItemTier = new ItemTierConfig(builder, "wood", "vanilla_tool_overrides", Tiers.WOOD, 6.0F, -3.2F, 1, 0, 1000f, true);
        stoneItemTier = new ItemTierConfig(builder, "stone", "vanilla_tool_overrides", Tiers.STONE, 7.0F, -3.2F, 1, 0, 5000f, true);
        goldItemTier = new ItemTierConfig(builder, "gold", "vanilla_tool_overrides", Tiers.GOLD, 6.0F, -3.0F, 4, 0, 5000f, false);
        ironItemTier = new ItemTierConfig(builder, "iron", "vanilla_tool_overrides", Tiers.IRON, 6.0F, -3.1F, 1, 0, 5000f, false);
        diamondItemTier = new ItemTierConfig(builder, "diamond", "vanilla_tool_overrides", Tiers.DIAMOND, 5.0F, -3.0F, 16, 1, 5000f, false);
        netheriteItemTier = new ItemTierConfig(builder, "netherite", "vanilla_tool_overrides", Tiers.NETHERITE, 5.0F, -3.0F, 64, 2, 10000f, false);
        ultimateItemTier = new ItemTierConfig(builder, "ultimate", "ultimate_fist", ToolsItemTier.ULTIMATE);

        chickenSuitArmorMaterial = new ArmorMaterialConfig(builder, "chicken_suit", "chicken_suit", 5, 15, 0.0F, 0.0F, new int[]{1, 2, 3, 1}, () -> ToolsArmorMaterials.CHICKEN_SUIT);

        moddedTiers = new HashMap<>();
        moddedTiers.put("tin", new ModdedItemTierConfig(builder, "tin", "modded_tool_overrides", ToolsItemTier.TIN));
        moddedTiers.put("copper", new ModdedItemTierConfig(builder, "copper", "modded_tool_overrides", ToolsItemTier.COPPER));
        moddedTiers.put("silver", new ModdedItemTierConfig(builder, "silver", "modded_tool_overrides", ToolsItemTier.SILVER));
        moddedTiers.put("aluminum", new ModdedItemTierConfig(builder, "aluminum", "modded_tool_overrides", ToolsItemTier.ALUMINUM));
        moddedTiers.put("nickel", new ModdedItemTierConfig(builder, "nickel", "modded_tool_overrides", ToolsItemTier.NICKEL));
        moddedTiers.put("platinum", new ModdedItemTierConfig(builder, "platinum", "modded_tool_overrides", ToolsItemTier.PLATINUM));
        moddedTiers.put("lead", new ModdedItemTierConfig(builder, "lead", "modded_tool_overrides", ToolsItemTier.LEAD));
        moddedTiers.put("bronze", new ModdedItemTierConfig(builder, "bronze", "modded_tool_overrides", ToolsItemTier.BRONZE));
        moddedTiers.put("electrum", new ModdedItemTierConfig(builder, "electrum", "modded_tool_overrides", ToolsItemTier.ELECTRUM));
        moddedTiers.put("invar", new ModdedItemTierConfig(builder, "invar", "modded_tool_overrides", ToolsItemTier.INVAR));
        moddedTiers.put("steel", new ModdedItemTierConfig(builder, "steel", "modded_tool_overrides", ToolsItemTier.STEEL));
        moddedTiers.put("ruby", new ModdedItemTierConfig(builder, "ruby", "modded_tool_overrides", ToolsItemTier.RUBY));
        moddedTiers.put("amethyst", new ModdedItemTierConfig(builder, "amethyst", "modded_tool_overrides", ToolsItemTier.AMETHYST));
        moddedTiers.put("sapphire", new ModdedItemTierConfig(builder, "sapphire", "modded_tool_overrides", ToolsItemTier.SAPPHIRE));
        moddedTiers.put("topaz", new ModdedItemTierConfig(builder, "topaz", "modded_tool_overrides", ToolsItemTier.TOPAZ));
        moddedTiers.put("emerald", new ModdedItemTierConfig(builder, "emerald", "modded_tool_overrides", ToolsItemTier.EMERALD));
        moddedTiers.put("peridot", new ModdedItemTierConfig(builder, "peridot", "modded_tool_overrides", ToolsItemTier.PERIDOT));

        moddedArmors = new HashMap<>();
        moddedArmors.put("tin", new ArmorMaterialConfig(builder, "tin", "modded_armor_overrides", 8, 14, 0.0F, 0.0F, new int[]{1, 3, 5, 2}, () -> ToolsArmorMaterials.TIN));
        moddedArmors.put("copper", new ArmorMaterialConfig(builder, "copper", "modded_armor_overrides", 11, 14, 0.0F, 0.0F, new int[]{2, 5, 6, 2}, () -> ToolsArmorMaterials.COPPER));
        moddedArmors.put("silver", new ArmorMaterialConfig(builder, "silver", "modded_armor_overrides", 27, 14, 0.0F, 0.0F, new int[]{3, 6, 8, 3}, () -> ToolsArmorMaterials.SILVER));
        moddedArmors.put("aluminum", new ArmorMaterialConfig(builder, "aluminum", "modded_armor_overrides", 13, 10, 0.0F, 0.0F, new int[]{2, 5, 6, 2}, () -> ToolsArmorMaterials.ALUMINUM));
        moddedArmors.put("nickel", new ArmorMaterialConfig(builder, "nickel", "modded_armor_overrides", 13, 10, 0.0F, 0.0F, new int[]{2, 3, 4, 2}, () -> ToolsArmorMaterials.NICKEL));
        moddedArmors.put("platinum", new ArmorMaterialConfig(builder, "platinum", "modded_armor_overrides", 36, 18, 3.0F, 0.2F, new int[]{3, 6, 8, 3}, () -> ToolsArmorMaterials.PLATINUM));
        moddedArmors.put("lead", new ArmorMaterialConfig(builder, "lead", "modded_armor_overrides", 13, 4, 0.0F, 0.0F, new int[]{2, 3, 4, 2}, () -> ToolsArmorMaterials.LEAD));
        moddedArmors.put("bronze", new ArmorMaterialConfig(builder, "bronze", "modded_armor_overrides", 14, 13, 0.0F, 0.1F, new int[]{2, 5, 6, 2}, () -> ToolsArmorMaterials.BRONZE));
        moddedArmors.put("electrum", new ArmorMaterialConfig(builder, "electrum", "modded_armor_overrides", 13, 13, 0.0F, 0.0F, new int[]{2, 5, 6, 2}, () -> ToolsArmorMaterials.ELECTRUM));
        moddedArmors.put("invar", new ArmorMaterialConfig(builder, "invar", "modded_armor_overrides", 15, 11, 0.2F, 0.1F, new int[]{2, 5, 6, 2}, () -> ToolsArmorMaterials.INVAR));
        moddedArmors.put("steel", new ArmorMaterialConfig(builder, "steel", "modded_armor_overrides", 26, 10, 0.5F, 0.3F, new int[]{2, 6, 7, 2}, () -> ToolsArmorMaterials.STEEL));
        moddedArmors.put("ruby", new ArmorMaterialConfig(builder, "ruby", "modded_armor_overrides", 34, 10, 2.0F, 0.0F, new int[]{3, 6, 8, 3}, () -> ToolsArmorMaterials.RUBY));
        moddedArmors.put("amethyst", new ArmorMaterialConfig(builder, "amethyst", "modded_armor_overrides", 31, 14, 1.0F, 0.0F, new int[]{3, 6, 8, 3}, () -> ToolsArmorMaterials.AMETHYST));
        moddedArmors.put("sapphire", new ArmorMaterialConfig(builder, "sapphire", "modded_armor_overrides", 31, 14, 1.0F, 0.0F, new int[]{3, 6, 8, 3}, () -> ToolsArmorMaterials.SAPPHIRE));
        moddedArmors.put("topaz", new ArmorMaterialConfig(builder, "topaz", "modded_armor_overrides", 30, 8, 1.0F, 0.0F, new int[]{3, 6, 8, 3}, () -> ToolsArmorMaterials.TOPAZ));
        moddedArmors.put("emerald", new ArmorMaterialConfig(builder, "emerald", "modded_armor_overrides", 32, 14, 2.0F, 0.0F, new int[]{3, 6, 8, 3}, () -> ToolsArmorMaterials.EMERALD));
        moddedArmors.put("peridot", new ArmorMaterialConfig(builder, "peridot", "modded_armor_overrides", 30, 8, 1.0F, 0.0F, new int[]{3, 6, 8, 3}, () -> ToolsArmorMaterials.PERIDOT));

        builder.setup();
    }
}
