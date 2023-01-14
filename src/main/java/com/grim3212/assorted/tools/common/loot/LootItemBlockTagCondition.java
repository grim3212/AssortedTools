package com.grim3212.assorted.tools.common.loot;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class LootItemBlockTagCondition implements LootItemCondition {
	final TagKey<Block> tag;

	LootItemBlockTagCondition(TagKey<Block> tag) {
		this.tag = tag;
	}

	public LootItemConditionType getType() {
		return ToolsLootConditions.BLOCK_TAG.get();
	}

	public Set<LootContextParam<?>> getReferencedContextParams() {
		return ImmutableSet.of(LootContextParams.BLOCK_STATE);
	}

	public boolean test(LootContext p_81772_) {
		BlockState blockstate = p_81772_.getParamOrNull(LootContextParams.BLOCK_STATE);
		return blockstate != null && blockstate.is(this.tag);
	}

	public static LootItemBlockTagCondition.Builder isInTag(TagKey<Block> tag) {
		return new LootItemBlockTagCondition.Builder(tag);
	}

	public static class Builder implements LootItemCondition.Builder {
		private final TagKey<Block> tag;

		public Builder(TagKey<Block> tag) {
			this.tag = tag;
		}

		public LootItemCondition build() {
			return new LootItemBlockTagCondition(this.tag);
		}
	}

	public static class LootItemBlockTagConditionSerializer implements Serializer<LootItemBlockTagCondition> {
		public void serialize(JsonObject json, LootItemBlockTagCondition condition, JsonSerializationContext context) {
			json.addProperty("tag", condition.tag.location().toString());
		}

		public LootItemBlockTagCondition deserialize(JsonObject json, JsonDeserializationContext context) {
			ResourceLocation resourcelocation = new ResourceLocation(GsonHelper.getAsString(json, "tag"));
			return new LootItemBlockTagCondition(TagKey.create(Registries.BLOCK, resourcelocation));
		}
	}

}
