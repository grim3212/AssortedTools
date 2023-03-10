package com.grim3212.assorted.tools.common.data;

import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import net.minecraft.data.loot.packs.VanillaChestLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public class ToolsChestLoot extends VanillaChestLoot {

    @Override
    public void generate(BiConsumer<ResourceLocation, Builder> builder) {
        LootPool.Builder ultimateFragmentOverworldLootPool = LootPool.lootPool();
        ultimateFragmentOverworldLootPool.setRolls(ConstantValue.exactly(1)).add(addItem(ToolsItems.U_FRAGMENT.get(), 1, 1, 1)).add(addItem(ToolsItems.L_FRAGMENT.get(), 1, 1, 1)).add(addItem(ToolsItems.T_FRAGMENT.get(), 1, 1, 1)).add(EmptyLootItem.emptyItem().setWeight(10));
        LootTable.Builder ultimateFragmentOverworldLootTable = LootTable.lootTable();
        ultimateFragmentOverworldLootTable.withPool(ultimateFragmentOverworldLootPool);
        builder.accept(new ResourceLocation(Constants.MOD_ID, "fragments_overworld_loot"), ultimateFragmentOverworldLootTable);

        LootPool.Builder ultimateFragmentNetherLootPool = LootPool.lootPool();
        ultimateFragmentNetherLootPool.setRolls(ConstantValue.exactly(1)).add(addItem(ToolsItems.I_FRAGMENT.get(), 1, 1, 1)).add(addItem(ToolsItems.M_FRAGMENT.get(), 1, 1, 1)).add(EmptyLootItem.emptyItem().setWeight(15));
        LootTable.Builder ultimateFragmentNetherLootTable = LootTable.lootTable();
        ultimateFragmentNetherLootTable.withPool(ultimateFragmentNetherLootPool);
        builder.accept(new ResourceLocation(Constants.MOD_ID, "fragments_nether_loot"), ultimateFragmentNetherLootTable);

        LootPool.Builder ultimateFragmentEndLootPool = LootPool.lootPool();
        ultimateFragmentEndLootPool.setRolls(ConstantValue.exactly(1)).add(addItem(ToolsItems.A_FRAGMENT.get(), 1, 1, 1)).add(addItem(ToolsItems.MISSING_FRAGMENT.get(), 1, 1, 1)).add(addItem(ToolsItems.E_FRAGMENT.get(), 1, 1, 1)).add(EmptyLootItem.emptyItem().setWeight(15));
        LootTable.Builder ultimateFragmentEndLootTable = LootTable.lootTable();
        ultimateFragmentEndLootTable.withPool(ultimateFragmentEndLootPool);
        builder.accept(new ResourceLocation(Constants.MOD_ID, "fragments_end_loot"), ultimateFragmentEndLootTable);
    }

    private LootPoolEntryContainer.Builder<?> addItem(ItemLike item, int weight, int min, int max) {
        return addItem(new ItemStack(item), weight).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)));
    }

    private LootPoolSingletonContainer.Builder<?> addItem(ItemStack item, int weight) {
        LootPoolSingletonContainer.Builder<?> ret = LootItem.lootTableItem(item.getItem()).setWeight(weight);
        if (item.hasTag())
            ret.apply(SetNbtFunction.setTag(item.getOrCreateTag()));
        return ret;
    }

}
