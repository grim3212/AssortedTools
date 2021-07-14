package com.grim3212.assorted.tools.common.handler;

import com.google.gson.JsonObject;
import com.grim3212.assorted.tools.AssortedTools;

import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class EnabledCondition implements ICondition {

	private static final ResourceLocation NAME = new ResourceLocation(AssortedTools.MODID, "part_enabled");
	private final String part;

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
		case "wands":
			return ToolsConfig.COMMON.wandsEnabled.get();
		case "hammers":
			return ToolsConfig.COMMON.hammersEnabled.get();
		case "multitools":
			return ToolsConfig.COMMON.multiToolsEnabled.get();
		case "boomerangs":
			return ToolsConfig.COMMON.boomerangsEnabled.get();
		case "pokeball":
			return ToolsConfig.COMMON.pokeballEnabled.get();
		case "chickensuit":
			return ToolsConfig.COMMON.chickenSuitEnabled.get();
		case "extramaterials":
			return ToolsConfig.COMMON.extraMaterialsEnabled.get();
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
			return new EnabledCondition(JSONUtils.getAsString(json, "part"));
		}

		@Override
		public ResourceLocation getID() {
			return EnabledCondition.NAME;
		}
	}
}
