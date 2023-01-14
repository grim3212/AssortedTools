package com.grim3212.assorted.tools.common.data;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;

public class ToolsLootTableProvider extends LootTableProvider {

	public ToolsLootTableProvider(PackOutput output, Set<ResourceLocation> requiredTables, List<LootTableProvider.SubProviderEntry> subProviders) {
		super(output, requiredTables, subProviders);
	}

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
		map.forEach((location, lootTable) -> {
			LootTables.validate(validationtracker, location, lootTable);
		});
	}
}
