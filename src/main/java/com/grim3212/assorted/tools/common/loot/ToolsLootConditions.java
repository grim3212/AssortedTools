package com.grim3212.assorted.tools.common.loot;

import com.grim3212.assorted.tools.AssortedTools;

import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class ToolsLootConditions {

	public static LootItemConditionType BLOCK_TAG;

	public static void register() {
		BLOCK_TAG = Registry.register(Registry.LOOT_CONDITION_TYPE, prefix("block_tag"), new LootItemConditionType(new LootItemBlockTagCondition.LootItemBlockTagConditionSerializer()));
	}

	private static String prefix(String s) {
		return AssortedTools.MODID + ":" + s;
	}

}
