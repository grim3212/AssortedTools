package com.grim3212.assorted.tools.common.loot;

import com.grim3212.assorted.decor.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ToolsLootConditions {

    public static final DeferredRegister<LootItemConditionType> LOOT_ITEM_CONDITIONS = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, Constants.MOD_ID);

    public static final RegistryObject<LootItemConditionType> BLOCK_TAG = LOOT_ITEM_CONDITIONS.register("block_tag", () -> new LootItemConditionType(new LootItemBlockTagCondition.LootItemBlockTagConditionSerializer()));

}
