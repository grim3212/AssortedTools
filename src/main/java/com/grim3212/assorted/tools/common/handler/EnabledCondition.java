package com.grim3212.assorted.tools.common.handler;

import com.google.gson.JsonObject;
import com.grim3212.assorted.tools.AssortedTools;

import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class EnabledCondition implements ICondition {

	private static final ResourceLocation NAME = new ResourceLocation(AssortedTools.MODID, "part_enabled");
	private final String part;

	public static final String WANDS_CONDITION = "wands";
	public static final String HAMMERS_CONDITION = "hammers";
	public static final String MULTITOOL_CONDITION = "multitools";
	public static final String BOOMERANGS_CONDITION = "boomerangs";
	public static final String POKEBALL_CONDITION = "pokeball";
	public static final String CHICKEN_SUIT_CONDITION = "chickensuit";
	public static final String SPEARS_CONDITION = "spears";
	public static final String BETTER_SPEARS_CONDITION = "betterspears";
	public static final String BETTER_BUCKETS_CONDITION = "betterbuckets";
	public static final String EXTRA_MATERIAL_CONDITION = "extramaterials";

	public EnabledCondition(String part) {
		this.part = part;
	}

	@Override
	public ResourceLocation getID() {
		return NAME;
	}

	@Override
	public boolean test() {
		switch (part) {
			case WANDS_CONDITION:
				return ToolsConfig.COMMON.wandsEnabled.get();
			case HAMMERS_CONDITION:
				return ToolsConfig.COMMON.hammersEnabled.get();
			case MULTITOOL_CONDITION:
				return ToolsConfig.COMMON.multiToolsEnabled.get();
			case BOOMERANGS_CONDITION:
				return ToolsConfig.COMMON.boomerangsEnabled.get();
			case POKEBALL_CONDITION:
				return ToolsConfig.COMMON.pokeballEnabled.get();
			case CHICKEN_SUIT_CONDITION:
				return ToolsConfig.COMMON.chickenSuitEnabled.get();
			case EXTRA_MATERIAL_CONDITION:
				return ToolsConfig.COMMON.extraMaterialsEnabled.get();
			case SPEARS_CONDITION:
				return ToolsConfig.COMMON.spearsEnabled.get();
			case BETTER_SPEARS_CONDITION:
				return ToolsConfig.COMMON.betterSpearsEnabled.get();
			case BETTER_BUCKETS_CONDITION:
				return ToolsConfig.COMMON.betterBucketsEnabled.get();
			default:
				return false;
		}
	}

	public static class Serializer implements IConditionSerializer<EnabledCondition> {
		public static final Serializer INSTANCE = new Serializer();

		@Override
		public void write(JsonObject json, EnabledCondition value) {
			json.addProperty("part", value.part);
		}

		@Override
		public EnabledCondition read(JsonObject json) {
			return new EnabledCondition(GsonHelper.getAsString(json, "part"));
		}

		@Override
		public ResourceLocation getID() {
			return EnabledCondition.NAME;
		}
	}
}
