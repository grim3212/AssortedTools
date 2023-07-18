package com.grim3212.assorted.tools.common.loot;

import com.grim3212.assorted.tools.AssortedTools;

import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ToolsLootConditions {

	public static final DeferredRegister<LootItemConditionType> LOOT_ITEM_CONDITIONS = DeferredRegister.create(Registry.LOOT_ITEM_REGISTRY, AssortedTools.MODID);

	public static final RegistryObject<LootItemConditionType> BLOCK_TAG = LOOT_ITEM_CONDITIONS.register("block_tag", () -> new LootItemConditionType(new LootItemBlockTagCondition.LootItemBlockTagConditionSerializer()));

}
