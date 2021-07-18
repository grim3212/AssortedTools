package com.grim3212.assorted.tools.common.handler;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.grim3212.assorted.tools.common.util.ConfigurableBlockStates;
import com.grim3212.assorted.tools.common.util.ToolsArmorMaterials;
import com.grim3212.assorted.tools.common.util.ToolsItemTier;

import net.minecraft.item.ItemTier;
import net.minecraftforge.common.ForgeConfigSpec;

public final class ToolsConfig {

	public static final Common COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;
	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	public static class Common {
		public final ForgeConfigSpec.BooleanValue turnAroundItem;
		public final ForgeConfigSpec.BooleanValue turnAroundMob;
		public final ForgeConfigSpec.BooleanValue breaksTorches;
		public final ForgeConfigSpec.BooleanValue breaksPlants;
		public final ForgeConfigSpec.BooleanValue hitsButtons;
		public final ForgeConfigSpec.BooleanValue turnAroundButton;
		public final ForgeConfigSpec.IntValue woodBoomerangRange;
		public final ForgeConfigSpec.IntValue woodBoomerangDamage;
		public final ForgeConfigSpec.IntValue diamondBoomerangRange;
		public final ForgeConfigSpec.IntValue diamondBoomerangDamage;
		public final ForgeConfigSpec.BooleanValue diamondBoomerangFollows;

		public final ForgeConfigSpec.BooleanValue freeBuildMode;
		public final ForgeConfigSpec.BooleanValue bedrockBreaking;
		public final ForgeConfigSpec.BooleanValue easyMiningObsidian;
		public final ForgeConfigSpec.ConfigValue<List<String>> destructiveWandSparedBlocks;
		public final ForgeConfigSpec.ConfigValue<List<String>> miningWandBlocksForSurfaceMining;

		public final ArmorMaterialHolder chickenSuitArmorMaterial;

		public final ItemTierHolder woodItemTier;
		public final ItemTierHolder stoneItemTier;
		public final ItemTierHolder goldItemTier;
		public final ItemTierHolder ironItemTier;
		public final ItemTierHolder diamondItemTier;
		public final ItemTierHolder netheriteItemTier;

		public final Map<String, ModdedItemTierHolder> moddedTiers = Maps.newHashMap();
		public final Map<String, ArmorMaterialHolder> moddedArmors = Maps.newHashMap();

		public ConfigurableBlockStates destructiveSparedBlocks;
		public ConfigurableBlockStates miningSurfaceBlocks;

		public final ForgeConfigSpec.BooleanValue wandsEnabled;
		public final ForgeConfigSpec.BooleanValue boomerangsEnabled;
		public final ForgeConfigSpec.BooleanValue hammersEnabled;
		public final ForgeConfigSpec.BooleanValue multiToolsEnabled;
		public final ForgeConfigSpec.BooleanValue pokeballEnabled;
		public final ForgeConfigSpec.BooleanValue chickenSuitEnabled;
		public final ForgeConfigSpec.BooleanValue extraMaterialsEnabled;
		public final ForgeConfigSpec.BooleanValue spearsEnabled;

		public Common(ForgeConfigSpec.Builder builder) {
			builder.push("Parts");
			wandsEnabled = builder.comment("Set this to true if you would like wands to be craftable and found in the creative tab.").define("wandsEnabled", true);
			boomerangsEnabled = builder.comment("Set this to true if you would like boomerangs to be craftable and found in the creative tab.").define("boomerangsEnabled", true);
			hammersEnabled = builder.comment("Set this to true if you would like hammers to be craftable and found in the creative tab.").define("hammersEnabled", true);
			multiToolsEnabled = builder.comment("Set this to true if you would like multitools to be craftable and found in the creative tab.").define("multiToolsEnabled", true);
			pokeballEnabled = builder.comment("Set this to true if you would like the pokeball to be craftable and found in the creative tab.").define("pokeballEnabled", true);
			chickenSuitEnabled = builder.comment("Set this to true if you would like the chicken suit to be craftable and found in the creative tab as well as if you want the Chicken Jump enchantment to be able to be applied.").define("chickenSuitEnabled", true);
			extraMaterialsEnabled = builder.comment("Set this to true if you would like to enable support for crafting the extra tools and armor that this supports. For example, Steel, Copper, or Ruby tools and armor.").define("extraMaterialsEnabled", true);
			spearsEnabled = builder.comment("Set this to true if you would like the spears to be craftable and found in the creative tab.").define("spearsEnabled", true);
			builder.pop();

			builder.push("Boomerangs");
			turnAroundItem = builder.comment("Set this to true if you would like boomerangs to turn around after they have picked up items.").define("turnAroundItem", false);
			turnAroundMob = builder.comment("Set this to true if you would like boomerangs to turn around after they have hit a mob.").define("turnAroundMob", false);
			turnAroundButton = builder.comment("Set this to false if you would like boomerangs to not turn around after they have hit a button or a lever.").define("turnAroundButton", true);
			breaksTorches = builder.comment("Set this to true if you would like boomerangs to be able to break torches.").define("breaksTorches", false);
			breaksPlants = builder.comment("Set this to true if you would like boomerangs to be able to break plants.").define("breaksPlants", false);
			hitsButtons = builder.comment("Set this to false if you would like boomerangs to not be able to hit buttons or levers.").define("hitsButtons", true);

			woodBoomerangRange = builder.comment("The maximum range away from the player the wood boomerang will travel before turning around.").defineInRange("woodBoomerangRange", 20, 1, 200);
			woodBoomerangDamage = builder.comment("The amount of damage the wood boomerang does to mobs.").defineInRange("woodBoomerangDamage", 1, 1, 500);
			diamondBoomerangRange = builder.comment("The maximum range away from the player the diamond boomerang will travel before turning around.").defineInRange("diamondBoomerangRange", 30, 1, 200);
			diamondBoomerangDamage = builder.comment("The amount of damage the diamond boomerang does to mobs.").defineInRange("diamondBoomerangDamage", 5, 1, 500);
			diamondBoomerangFollows = builder.comment("Set to true if you would like the diamond boomerang to follow where the player is looking.").define("diamondBoomerangFollows", false);
			builder.pop();

			builder.push("Wands");
			freeBuildMode = builder.comment("Set to true if you would like the wands to not require any blocks to build with.").define("freeBuildMode", false);
			bedrockBreaking = builder.comment("Set to true if you would like the breaking wands to be able to break bedrock.").define("bedrockBreaking", false);
			easyMiningObsidian = builder.comment("Set to true if you would like the mining wands to be able to mine obsidian.").define("easyMiningObsidian", false);

			destructiveWandSparedBlocks = builder.comment("A list of blocks that the wand breaking wand will not break. Usually used for ores.").define("destructiveWandSparedBlocks", Lists.newArrayList("tag|forge:ores", "tag|forge:chests", "block|minecraft:spawner"));
			miningWandBlocksForSurfaceMining = builder.comment("A list of blocks that the mining wand can break from the surface.").define("miningWandBlocksForSurfaceMining", Lists.newArrayList("tag|forge:ores"));
			builder.pop();

			builder.push("Armors");
			chickenSuitArmorMaterial = new ArmorMaterialHolder(builder, "chicken_suit", 5, 15, 0.0F, 0.0F, new int[] { 1, 2, 3, 1 }, () -> ToolsArmorMaterials.CHICKEN_SUIT);
			moddedArmors.clear();
			moddedArmors.put("tin", new ArmorMaterialHolder(builder, "tin", 8, 14, 0.0F, 0.0F, new int[] { 1, 3, 5, 2 }, () -> ToolsArmorMaterials.TIN));
			moddedArmors.put("copper", new ArmorMaterialHolder(builder, "copper", 11, 14, 0.0F, 0.0F, new int[] { 2, 5, 6, 2 }, () -> ToolsArmorMaterials.COPPER));
			moddedArmors.put("silver", new ArmorMaterialHolder(builder, "silver", 27, 14, 0.0F, 0.0F, new int[] { 3, 6, 8, 3 }, () -> ToolsArmorMaterials.SILVER));
			moddedArmors.put("aluminum", new ArmorMaterialHolder(builder, "aluminum", 13, 10, 0.0F, 0.0F, new int[] { 2, 5, 6, 2 }, () -> ToolsArmorMaterials.ALUMINUM));
			moddedArmors.put("nickel", new ArmorMaterialHolder(builder, "nickel", 13, 10, 0.0F, 0.0F, new int[] { 2, 3, 4, 2 }, () -> ToolsArmorMaterials.NICKEL));
			moddedArmors.put("platinum", new ArmorMaterialHolder(builder, "platinum", 36, 18, 3.0F, 0.2F, new int[] { 3, 6, 8, 3 }, () -> ToolsArmorMaterials.PLATINUM));
			moddedArmors.put("lead", new ArmorMaterialHolder(builder, "lead", 13, 4, 0.0F, 0.0F, new int[] { 2, 3, 4, 2 }, () -> ToolsArmorMaterials.LEAD));
			moddedArmors.put("bronze", new ArmorMaterialHolder(builder, "bronze", 14, 13, 0.0F, 0.1F, new int[] { 2, 5, 6, 2 }, () -> ToolsArmorMaterials.BRONZE));
			moddedArmors.put("electrum", new ArmorMaterialHolder(builder, "electrum", 13, 13, 0.0F, 0.0F, new int[] { 2, 5, 6, 2 }, () -> ToolsArmorMaterials.ELECTRUM));
			moddedArmors.put("invar", new ArmorMaterialHolder(builder, "invar", 15, 11, 0.2F, 0.1F, new int[] { 2, 5, 6, 2 }, () -> ToolsArmorMaterials.INVAR));
			moddedArmors.put("steel", new ArmorMaterialHolder(builder, "steel", 26, 10, 0.5F, 0.3F, new int[] { 2, 6, 7, 2 }, () -> ToolsArmorMaterials.STEEL));
			moddedArmors.put("ruby", new ArmorMaterialHolder(builder, "ruby", 34, 10, 2.0F, 0.0F, new int[] { 3, 6, 8, 3 }, () -> ToolsArmorMaterials.RUBY));
			moddedArmors.put("amethyst", new ArmorMaterialHolder(builder, "amethyst", 31, 14, 1.0F, 0.0F, new int[] { 3, 6, 8, 3 }, () -> ToolsArmorMaterials.AMETHYST));
			moddedArmors.put("sapphire", new ArmorMaterialHolder(builder, "sapphire", 31, 14, 1.0F, 0.0F, new int[] { 3, 6, 8, 3 }, () -> ToolsArmorMaterials.SAPPHIRE));
			moddedArmors.put("topaz", new ArmorMaterialHolder(builder, "topaz", 30, 8, 1.0F, 0.0F, new int[] { 3, 6, 8, 3 }, () -> ToolsArmorMaterials.TOPAZ));
			moddedArmors.put("emerald", new ArmorMaterialHolder(builder, "emerald", 32, 14, 2.0F, 0.0F, new int[] { 3, 6, 8, 3 }, () -> ToolsArmorMaterials.EMERALD));
			builder.pop();

			builder.push("Item Tiers");

			builder.comment("These are used by Hammers and MultiTools to allow you to override the default vanilla values that they use.", "These will not change the values that vanilla tools use.");
			builder.push("Vanilla Overrides");
			woodItemTier = new ItemTierHolder(builder, "wood", ItemTier.WOOD, 6.0F, -3.2F);
			stoneItemTier = new ItemTierHolder(builder, "stone", ItemTier.STONE, 7.0F, -3.2F);
			goldItemTier = new ItemTierHolder(builder, "gold", ItemTier.GOLD, 6.0F, -3.0F);
			ironItemTier = new ItemTierHolder(builder, "iron", ItemTier.IRON, 6.0F, -3.1F);
			diamondItemTier = new ItemTierHolder(builder, "diamond", ItemTier.DIAMOND, 5.0F, -3.0F);
			netheriteItemTier = new ItemTierHolder(builder, "netherite", ItemTier.NETHERITE, 5.0F, -3.0F);
			builder.pop();

			builder.comment("These are used by Hammers, MultiTools, and the normal tool sets to allow you to override the default values that are used.");
			builder.push("Modded Overrides");
			moddedTiers.clear();
			moddedTiers.put("tin", new ModdedItemTierHolder(builder, "tin", ToolsItemTier.TIN));
			moddedTiers.put("copper", new ModdedItemTierHolder(builder, "copper", ToolsItemTier.COPPER));
			moddedTiers.put("silver", new ModdedItemTierHolder(builder, "silver", ToolsItemTier.SILVER));
			moddedTiers.put("aluminum", new ModdedItemTierHolder(builder, "aluminum", ToolsItemTier.ALUMINUM));
			moddedTiers.put("nickel", new ModdedItemTierHolder(builder, "nickel", ToolsItemTier.NICKEL));
			moddedTiers.put("platinum", new ModdedItemTierHolder(builder, "platinum", ToolsItemTier.PLATINUM));
			moddedTiers.put("lead", new ModdedItemTierHolder(builder, "lead", ToolsItemTier.LEAD));
			moddedTiers.put("bronze", new ModdedItemTierHolder(builder, "bronze", ToolsItemTier.BRONZE));
			moddedTiers.put("electrum", new ModdedItemTierHolder(builder, "electrum", ToolsItemTier.ELECTRUM));
			moddedTiers.put("invar", new ModdedItemTierHolder(builder, "invar", ToolsItemTier.INVAR));
			moddedTiers.put("steel", new ModdedItemTierHolder(builder, "steel", ToolsItemTier.STEEL));
			moddedTiers.put("ruby", new ModdedItemTierHolder(builder, "ruby", ToolsItemTier.RUBY));
			moddedTiers.put("amethyst", new ModdedItemTierHolder(builder, "amethyst", ToolsItemTier.AMETHYST));
			moddedTiers.put("sapphire", new ModdedItemTierHolder(builder, "sapphire", ToolsItemTier.SAPPHIRE));
			moddedTiers.put("topaz", new ModdedItemTierHolder(builder, "topaz", ToolsItemTier.TOPAZ));
			moddedTiers.put("emerald", new ModdedItemTierHolder(builder, "emerald", ToolsItemTier.EMERALD));
			builder.pop();

			builder.pop();
		}

		public void init() {
			destructiveSparedBlocks = new ConfigurableBlockStates.Builder().processString(destructiveWandSparedBlocks.get()).build();
			miningSurfaceBlocks = new ConfigurableBlockStates.Builder().processString(miningWandBlocksForSurfaceMining.get()).build();
		}
	}
}
