package com.grim3212.assorted.tools.common.handler;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.grim3212.assorted.tools.common.util.ConfigurableBlockStates;

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

		public ConfigurableBlockStates destructiveSparedBlocks;
		public ConfigurableBlockStates miningSurfaceBlocks;

		public Common(ForgeConfigSpec.Builder builder) {
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
			chickenSuitArmorMaterial = new ArmorMaterialHolder(builder, "chicken_suit", 5, 15, 0.0F, 0.0F, new int[] { 1, 2, 3, 1 });
			builder.pop();

			builder.push("Item Tiers");

			builder.comment("These are used by Hammers and MultiTools to allow you to override the default vanilla values that they use.", "These will not change the values that vanilla tools use.");
			builder.push("Vanilla Overrides");
			woodItemTier = new ItemTierHolder(builder, "wood", ItemTier.WOOD);
			stoneItemTier = new ItemTierHolder(builder, "stone", ItemTier.STONE);
			goldItemTier = new ItemTierHolder(builder, "gold", ItemTier.GOLD);
			ironItemTier = new ItemTierHolder(builder, "iron", ItemTier.IRON);
			diamondItemTier = new ItemTierHolder(builder, "diamond", ItemTier.DIAMOND);
			netheriteItemTier = new ItemTierHolder(builder, "netherite", ItemTier.NETHERITE);
			builder.pop();

			builder.pop();
		}

		public void init() {
			destructiveSparedBlocks = new ConfigurableBlockStates.Builder().processString(destructiveWandSparedBlocks.get()).build();
			miningSurfaceBlocks = new ConfigurableBlockStates.Builder().processString(miningWandBlocksForSurfaceMining.get()).build();
		}
	}
}
