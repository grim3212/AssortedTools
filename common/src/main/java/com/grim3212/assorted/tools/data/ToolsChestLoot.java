package com.grim3212.assorted.tools.data;

import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

/**
 * The three Ultimate Fist fragment pools, injected into vanilla chest loot by
 * {@code LootTableHandlers}.
 * <p>
 * This used to extend {@code VanillaChestLoot}, which was a plain class. It is a record now and
 * cannot be subclassed, so this implements {@link LootTableSubProvider} directly - which is all the
 * inheritance ever bought, since none of the vanilla tables were being reused.
 * <p>
 * Loot tables are addressed by {@code ResourceKey<LootTable>} rather than a raw {@code Identifier}.
 */
public class ToolsChestLoot implements LootTableSubProvider {

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, Builder> builder) {
        LootPool.Builder overworldPool = LootPool.lootPool();
        overworldPool.setRolls(ConstantValue.exactly(1)).add(addItem(ToolsItems.U_FRAGMENT.get(), 1, 1, 1)).add(addItem(ToolsItems.L_FRAGMENT.get(), 1, 1, 1)).add(addItem(ToolsItems.T_FRAGMENT.get(), 1, 1, 1)).add(EmptyLootItem.emptyItem().setWeight(10));
        builder.accept(key("fragments_overworld_loot"), LootTable.lootTable().withPool(overworldPool));

        LootPool.Builder netherPool = LootPool.lootPool();
        netherPool.setRolls(ConstantValue.exactly(1)).add(addItem(ToolsItems.I_FRAGMENT.get(), 1, 1, 1)).add(addItem(ToolsItems.M_FRAGMENT.get(), 1, 1, 1)).add(EmptyLootItem.emptyItem().setWeight(15));
        builder.accept(key("fragments_nether_loot"), LootTable.lootTable().withPool(netherPool));

        LootPool.Builder endPool = LootPool.lootPool();
        endPool.setRolls(ConstantValue.exactly(1)).add(addItem(ToolsItems.A_FRAGMENT.get(), 1, 1, 1)).add(addItem(ToolsItems.MISSING_FRAGMENT.get(), 1, 1, 1)).add(addItem(ToolsItems.E_FRAGMENT.get(), 1, 1, 1)).add(EmptyLootItem.emptyItem().setWeight(15));
        builder.accept(key("fragments_end_loot"), LootTable.lootTable().withPool(endPool));
    }

    /**
     * The two-argument form collapsed into one.
     * <p>
     * There used to be a second overload taking an {@code ItemStack} so it could copy the stack's
     * NBT onto the entry with {@code SetNbtFunction}. Stack NBT is gone, and none of these entries
     * ever carried any - every caller passed a bare item - so the branch was dead in 1.20.1 too.
     */
    private LootPoolEntryContainer.Builder<?> addItem(ItemLike item, int weight, int min, int max) {
        return LootItem.lootTableItem(item).setWeight(weight).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)));
    }

    private static ResourceKey<LootTable> key(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, path));
    }
}
