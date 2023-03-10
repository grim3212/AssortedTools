package com.grim3212.assorted.tools.config;

import com.grim3212.assorted.lib.config.ConfigBuilder;
import com.grim3212.assorted.lib.config.ConfigGroup;
import com.grim3212.assorted.lib.config.ConfigOption;
import com.grim3212.assorted.lib.config.ConfigType;
import com.grim3212.assorted.tools.api.item.ToolsArmorMaterials;
import com.grim3212.assorted.tools.api.item.ToolsItemTier;
import net.minecraft.world.item.Tiers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToolsConfig {
    public static class Common {

        public static final ConfigOption<Boolean> wandsEnabled = new ConfigOption<>("wandsEnabled", ConfigOption.OptionType.BOOLEAN, true, "Set this to true if you would like wands to be craftable and found in the creative tab.");
        public static final ConfigOption<Boolean> boomerangsEnabled = new ConfigOption<>("boomerangsEnabled", ConfigOption.OptionType.BOOLEAN, true, "Set this to true if you would like boomerangs to be craftable and found in the creative tab.");
        public static final ConfigOption<Boolean> hammersEnabled = new ConfigOption<>("hammersEnabled", ConfigOption.OptionType.BOOLEAN, true, "Set this to true if you would like hammers to be craftable and found in the creative tab.");
        public static final ConfigOption<Boolean> multiToolsEnabled = new ConfigOption<>("multiToolsEnabled", ConfigOption.OptionType.BOOLEAN, true, "Set this to true if you would like multitools to be craftable and found in the creative tab.");
        public static final ConfigOption<Boolean> pokeballEnabled = new ConfigOption<>("pokeballEnabled", ConfigOption.OptionType.BOOLEAN, true, "Set this to true if you would like the pokeball to be craftable and found in the creative tab.");
        public static final ConfigOption<Boolean> chickenSuitEnabled = new ConfigOption<>("chickenSuitEnabled", ConfigOption.OptionType.BOOLEAN, true, "Set this to true if you would like the chicken suit to be craftable and found in the creative tab as well as if you want the Chicken Jump enchantment to be able to be applied.");
        public static final ConfigOption<Boolean> extraMaterialsEnabled = new ConfigOption<>("extraMaterialsEnabled", ConfigOption.OptionType.BOOLEAN, true, "Set this to true if you would like to enable support for crafting the extra tools and armor that this supports. For example, Steel, Copper, or Ruby tools and armor.");
        public static final ConfigOption<Boolean> spearsEnabled = new ConfigOption<>("spearsEnabled", ConfigOption.OptionType.BOOLEAN, false, "Set this to true if you would like the old DEPRECATED spears to be craftable and found in the creative tab.");
        public static final ConfigOption<Boolean> betterSpearsEnabled = new ConfigOption<>("betterSpearsEnabled", ConfigOption.OptionType.BOOLEAN, true, "Set this to true if you would like the better spears (the ones that can be enchanted) to be craftable and found in the creative tab as well as the Enchantments for it to be enchanted on books.");
        public static final ConfigOption<Boolean> betterBucketsEnabled = new ConfigOption<>("betterBucketsEnabled", ConfigOption.OptionType.BOOLEAN, true, "Set this to true if you would like better buckets to be craftable and found in the creative tab.");
        public static final ConfigOption<Boolean> moreShearsEnabled = new ConfigOption<>("moreShearsEnabled", ConfigOption.OptionType.BOOLEAN, true, "Set this to true if you would like the extra shears to be craftable and found in the creative tab.");
        public static final ConfigOption<Boolean> ultimateFistEnabled = new ConfigOption<>("ultimateFistEnabled", ConfigOption.OptionType.BOOLEAN, true, "Set this to true if you would like the ultimate fist to be craftable and found in the creative tab as well as the fragements generate in loot.");

        public static final ConfigOption<Boolean> hideUncraftableItems = new ConfigOption<>("hideUncraftableItems", ConfigOption.OptionType.BOOLEAN, false, "For any item that is unobtainable (like missing materials from other mods) hide it from the creative menu / JEI.");

        public static final ConfigOption<Boolean> allowPartialBucketAmounts = new ConfigOption<>("allowPartialBucketAmounts", ConfigOption.OptionType.BOOLEAN, false, "Set to true if you would like the better buckets to be able to accept partial bucket amounts. Meaning some can get left over after placing all the full buckets.");

        public static final ConfigOption<Boolean> freeBuildMode = new ConfigOption<>("freeBuildMode", ConfigOption.OptionType.BOOLEAN, false, "Set to true if you would like the wands to not require any blocks to build with.");
        public static final ConfigOption<Boolean> bedrockBreaking = new ConfigOption<>("bedrockBreaking", ConfigOption.OptionType.BOOLEAN, false, "Set to true if you would like the breaking wands to be able to break bedrock.");
        public static final ConfigOption<Boolean> easyMiningObsidian = new ConfigOption<>("easyMiningObsidian", ConfigOption.OptionType.BOOLEAN, false, "Set to true if you would like the mining wands to be able to mine obsidian.");

        public static final ConfigOption<Boolean> turnAroundItem = new ConfigOption<>("turnAroundItem", ConfigOption.OptionType.BOOLEAN, false, "Set this to true if you would like boomerangs to turn around after they have picked up items.");
        public static final ConfigOption<Boolean> turnAroundMob = new ConfigOption<>("turnAroundMob", ConfigOption.OptionType.BOOLEAN, false, "Set this to true if you would like boomerangs to turn around after they have hit a mob.");
        public static final ConfigOption<Boolean> breaksTorches = new ConfigOption<>("breaksTorches", ConfigOption.OptionType.BOOLEAN, false, "Set this to true if you would like boomerangs to be able to break torches.");
        public static final ConfigOption<Boolean> breaksPlants = new ConfigOption<>("breaksPlants", ConfigOption.OptionType.BOOLEAN, false, "Set this to true if you would like boomerangs to be able to break plants.");
        public static final ConfigOption<Boolean> hitsButtons = new ConfigOption<>("hitsButtons", ConfigOption.OptionType.BOOLEAN, true, "Set this to false if you would like boomerangs to not be able to hit buttons or levers.");
        public static final ConfigOption<Boolean> turnAroundButton = new ConfigOption<>("turnAroundButton", ConfigOption.OptionType.BOOLEAN, true, "Set this to false if you would like boomerangs to not turn around after they have hit a button or a lever.");
        public static final ConfigOption.ConfigOptionInteger woodBoomerangRange = new ConfigOption.ConfigOptionInteger("woodBoomerangRange", 20, "The maximum range away from the player the wood boomerang will travel before turning around.", 1, 200);
        public static final ConfigOption.ConfigOptionInteger woodBoomerangDamage = new ConfigOption.ConfigOptionInteger("woodBoomerangDamage", 1, "The amount of damage the wood boomerang does to mobs.", 1, 500);
        public static final ConfigOption.ConfigOptionInteger diamondBoomerangRange = new ConfigOption.ConfigOptionInteger("diamondBoomerangRange", 30, "The maximum range away from the player the diamond boomerang will travel before turning around.", 1, 200);
        public static final ConfigOption.ConfigOptionInteger diamondBoomerangDamage = new ConfigOption.ConfigOptionInteger("diamondBoomerangDamage", 5, "The amount of damage the diamond boomerang does to mobs.", 1, 500);
        public static final ConfigOption<Boolean> diamondBoomerangFollows = new ConfigOption<>("diamondBoomerangFollows", ConfigOption.OptionType.BOOLEAN, false, "Set to true if you would like the diamond boomerang to follow where the player is looking.");

        private static final ConfigOption.ConfigOptionFloat conductivityLightningChanceLevel1 = new ConfigOption.ConfigOptionFloat("conductivityLightningChanceLevel1", 0.6F, "The chances modifier for lightning to spawn at level 1 of conductivity. The smaller the number the higher chance.", 0.0F, 1.0F);
        private static final ConfigOption.ConfigOptionFloat conductivityLightningChanceLevel2 = new ConfigOption.ConfigOptionFloat("conductivityLightningChanceLevel2", 0.3F, "The chances modifier for lightning to spawn at level 2 of conductivity. The smaller the number the higher chance.", 0.0F, 1.0F);
        private static final ConfigOption.ConfigOptionFloat conductivityLightningChanceLevel3 = new ConfigOption.ConfigOptionFloat("conductivityLightningChanceLevel3", 0.1F, "The chances modifier for lightning to spawn at level 3 of conductivity. The smaller the number the higher chance.", 0.0F, 1.0F);

        private static final ConfigGroup vanillaOverrides = new ConfigGroup("Vanilla Tool Overrides");
        public static final ItemTierConfig woodItemTier = new ItemTierConfig(vanillaOverrides, "wood", Tiers.WOOD, 6.0F, -3.2F, 1, 0, 1000f, true);
        public static final ItemTierConfig stoneItemTier = new ItemTierConfig(vanillaOverrides, "stone", Tiers.STONE, 7.0F, -3.2F, 1, 0, 5000f, true);
        public static final ItemTierConfig goldItemTier = new ItemTierConfig(vanillaOverrides, "gold", Tiers.GOLD, 6.0F, -3.0F, 4, 0, 5000f, false);
        public static final ItemTierConfig ironItemTier = new ItemTierConfig(vanillaOverrides, "iron", Tiers.IRON, 6.0F, -3.1F, 1, 0, 5000f, false);
        public static final ItemTierConfig diamondItemTier = new ItemTierConfig(vanillaOverrides, "diamond", Tiers.DIAMOND, 5.0F, -3.0F, 16, 1, 5000f, false);
        public static final ItemTierConfig netheriteItemTier = new ItemTierConfig(vanillaOverrides, "netherite", Tiers.NETHERITE, 5.0F, -3.0F, 64, 2, 10000f, false);

        private static final ConfigGroup ultimateFist = new ConfigGroup("Ultimate Fist");
        public static final ItemTierConfig ultimateItemTier = new ItemTierConfig(ultimateFist, "ultimate", ToolsItemTier.ULTIMATE);

        private static final ConfigGroup moddedToolOverrides = new ConfigGroup("Modded Tool Overrides");
        public static final Map<String, ModdedItemTierConfig> moddedTiers = new HashMap<>();

        static {
            moddedTiers.clear();
            moddedTiers.put("tin", new ModdedItemTierConfig(moddedToolOverrides, "tin", ToolsItemTier.TIN));
            moddedTiers.put("copper", new ModdedItemTierConfig(moddedToolOverrides, "copper", ToolsItemTier.COPPER));
            moddedTiers.put("silver", new ModdedItemTierConfig(moddedToolOverrides, "silver", ToolsItemTier.SILVER));
            moddedTiers.put("aluminum", new ModdedItemTierConfig(moddedToolOverrides, "aluminum", ToolsItemTier.ALUMINUM));
            moddedTiers.put("nickel", new ModdedItemTierConfig(moddedToolOverrides, "nickel", ToolsItemTier.NICKEL));
            moddedTiers.put("platinum", new ModdedItemTierConfig(moddedToolOverrides, "platinum", ToolsItemTier.PLATINUM));
            moddedTiers.put("lead", new ModdedItemTierConfig(moddedToolOverrides, "lead", ToolsItemTier.LEAD));
            moddedTiers.put("bronze", new ModdedItemTierConfig(moddedToolOverrides, "bronze", ToolsItemTier.BRONZE));
            moddedTiers.put("electrum", new ModdedItemTierConfig(moddedToolOverrides, "electrum", ToolsItemTier.ELECTRUM));
            moddedTiers.put("invar", new ModdedItemTierConfig(moddedToolOverrides, "invar", ToolsItemTier.INVAR));
            moddedTiers.put("steel", new ModdedItemTierConfig(moddedToolOverrides, "steel", ToolsItemTier.STEEL));
            moddedTiers.put("ruby", new ModdedItemTierConfig(moddedToolOverrides, "ruby", ToolsItemTier.RUBY));
            moddedTiers.put("amethyst", new ModdedItemTierConfig(moddedToolOverrides, "amethyst", ToolsItemTier.AMETHYST));
            moddedTiers.put("sapphire", new ModdedItemTierConfig(moddedToolOverrides, "sapphire", ToolsItemTier.SAPPHIRE));
            moddedTiers.put("topaz", new ModdedItemTierConfig(moddedToolOverrides, "topaz", ToolsItemTier.TOPAZ));
            moddedTiers.put("emerald", new ModdedItemTierConfig(moddedToolOverrides, "emerald", ToolsItemTier.EMERALD));
            moddedTiers.put("peridot", new ModdedItemTierConfig(moddedToolOverrides, "peridot", ToolsItemTier.PERIDOT));
        }

        private static final ConfigGroup moddedArmorOverrides = new ConfigGroup("Modded Armor Overrides");
        public static final ArmorMaterialConfig chickenSuitArmorMaterial = new ArmorMaterialConfig(moddedArmorOverrides, "chicken_suit", 5, 15, 0.0F, 0.0F, new int[]{1, 2, 3, 1}, () -> ToolsArmorMaterials.CHICKEN_SUIT);
        public static final Map<String, ArmorMaterialConfig> moddedArmors = new HashMap<>();

        static {
            moddedArmors.clear();
            moddedArmors.put("tin", new ArmorMaterialConfig(moddedArmorOverrides, "tin", 8, 14, 0.0F, 0.0F, new int[]{1, 3, 5, 2}, () -> ToolsArmorMaterials.TIN));
            moddedArmors.put("copper", new ArmorMaterialConfig(moddedArmorOverrides, "copper", 11, 14, 0.0F, 0.0F, new int[]{2, 5, 6, 2}, () -> ToolsArmorMaterials.COPPER));
            moddedArmors.put("silver", new ArmorMaterialConfig(moddedArmorOverrides, "silver", 27, 14, 0.0F, 0.0F, new int[]{3, 6, 8, 3}, () -> ToolsArmorMaterials.SILVER));
            moddedArmors.put("aluminum", new ArmorMaterialConfig(moddedArmorOverrides, "aluminum", 13, 10, 0.0F, 0.0F, new int[]{2, 5, 6, 2}, () -> ToolsArmorMaterials.ALUMINUM));
            moddedArmors.put("nickel", new ArmorMaterialConfig(moddedArmorOverrides, "nickel", 13, 10, 0.0F, 0.0F, new int[]{2, 3, 4, 2}, () -> ToolsArmorMaterials.NICKEL));
            moddedArmors.put("platinum", new ArmorMaterialConfig(moddedArmorOverrides, "platinum", 36, 18, 3.0F, 0.2F, new int[]{3, 6, 8, 3}, () -> ToolsArmorMaterials.PLATINUM));
            moddedArmors.put("lead", new ArmorMaterialConfig(moddedArmorOverrides, "lead", 13, 4, 0.0F, 0.0F, new int[]{2, 3, 4, 2}, () -> ToolsArmorMaterials.LEAD));
            moddedArmors.put("bronze", new ArmorMaterialConfig(moddedArmorOverrides, "bronze", 14, 13, 0.0F, 0.1F, new int[]{2, 5, 6, 2}, () -> ToolsArmorMaterials.BRONZE));
            moddedArmors.put("electrum", new ArmorMaterialConfig(moddedArmorOverrides, "electrum", 13, 13, 0.0F, 0.0F, new int[]{2, 5, 6, 2}, () -> ToolsArmorMaterials.ELECTRUM));
            moddedArmors.put("invar", new ArmorMaterialConfig(moddedArmorOverrides, "invar", 15, 11, 0.2F, 0.1F, new int[]{2, 5, 6, 2}, () -> ToolsArmorMaterials.INVAR));
            moddedArmors.put("steel", new ArmorMaterialConfig(moddedArmorOverrides, "steel", 26, 10, 0.5F, 0.3F, new int[]{2, 6, 7, 2}, () -> ToolsArmorMaterials.STEEL));
            moddedArmors.put("ruby", new ArmorMaterialConfig(moddedArmorOverrides, "ruby", 34, 10, 2.0F, 0.0F, new int[]{3, 6, 8, 3}, () -> ToolsArmorMaterials.RUBY));
            moddedArmors.put("amethyst", new ArmorMaterialConfig(moddedArmorOverrides, "amethyst", 31, 14, 1.0F, 0.0F, new int[]{3, 6, 8, 3}, () -> ToolsArmorMaterials.AMETHYST));
            moddedArmors.put("sapphire", new ArmorMaterialConfig(moddedArmorOverrides, "sapphire", 31, 14, 1.0F, 0.0F, new int[]{3, 6, 8, 3}, () -> ToolsArmorMaterials.SAPPHIRE));
            moddedArmors.put("topaz", new ArmorMaterialConfig(moddedArmorOverrides, "topaz", 30, 8, 1.0F, 0.0F, new int[]{3, 6, 8, 3}, () -> ToolsArmorMaterials.TOPAZ));
            moddedArmors.put("emerald", new ArmorMaterialConfig(moddedArmorOverrides, "emerald", 32, 14, 2.0F, 0.0F, new int[]{3, 6, 8, 3}, () -> ToolsArmorMaterials.EMERALD));
            moddedArmors.put("peridot", new ArmorMaterialConfig(moddedArmorOverrides, "peridot", 30, 8, 1.0F, 0.0F, new int[]{3, 6, 8, 3}, () -> ToolsArmorMaterials.PERIDOT));
        }


        public static final ConfigBuilder COMMON_CONFIG = new ConfigBuilder(ConfigType.COMMON)
                .addGroups(new ConfigGroup("Parts").addOptions(wandsEnabled, boomerangsEnabled, hammersEnabled, multiToolsEnabled, pokeballEnabled, chickenSuitEnabled, extraMaterialsEnabled, spearsEnabled, betterSpearsEnabled, betterBucketsEnabled, moreShearsEnabled, ultimateFistEnabled))
                .addGroups(new ConfigGroup("General").addOptions(hideUncraftableItems))
                .addGroups(new ConfigGroup("Wands").addOptions(freeBuildMode, bedrockBreaking, easyMiningObsidian))
                .addGroups(new ConfigGroup("Better Buckets").addOptions(allowPartialBucketAmounts))
                .addGroups(new ConfigGroup("Boomerangs").addOptions(turnAroundItem, turnAroundMob, breaksTorches, breaksPlants, hitsButtons, turnAroundButton, woodBoomerangRange, woodBoomerangDamage, diamondBoomerangRange, diamondBoomerangDamage, diamondBoomerangFollows))
                .addGroups(new ConfigGroup("Better Spears").addOptions(conductivityLightningChanceLevel1, conductivityLightningChanceLevel2, conductivityLightningChanceLevel3))
                .addGroups(ultimateFist, vanillaOverrides, moddedToolOverrides, moddedArmorOverrides);

        public static List<Float> getConductivityChances() {
            return List.of(conductivityLightningChanceLevel1.getValue(), conductivityLightningChanceLevel2.getValue(), conductivityLightningChanceLevel3.getValue());
        }
    }
}
