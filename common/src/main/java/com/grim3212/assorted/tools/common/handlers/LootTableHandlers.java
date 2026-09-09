package com.grim3212.assorted.tools.common.handlers;

import com.grim3212.assorted.lib.events.LootTableModifyEvent;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.ToolsCommonMod;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;

import java.util.Arrays;
import java.util.List;

public class LootTableHandlers {

    // LootTableReference is NestedLootTable now, and it references a loot table by
    // ResourceKey rather than by a bare Identifier.
    private static final ResourceKey<LootTable> OVERWORLD_UF_LOOT = lootTable("fragments_overworld_loot");
    private static final ResourceKey<LootTable> NETHER_UF_LOOT = lootTable("fragments_nether_loot");
    private static final ResourceKey<LootTable> END_UF_LOOT = lootTable("fragments_end_loot");

    private static ResourceKey<LootTable> lootTable(String name) {
        return ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
    }

    public static void init(LootTableModifyEvent event) {
        if (ToolsCommonMod.COMMON_CONFIG.ultimateFistEnabled.get()) {
            initOverworldUltimateFist(event);
            initNetherUltimateFist(event);
            initEndUltimateFist(event);
        }
    }

    private static void initOverworldUltimateFist(LootTableModifyEvent event) {
        List<Identifier> tablesToModify = Arrays.asList(
                Identifier.parse("chests/stronghold_corridor"),
                Identifier.parse("chests/stronghold_crossing"),
                Identifier.parse("chests/stronghold_library"),
                Identifier.parse("chests/woodland_mansion"),
                Identifier.parse("chests/underwater_ruin_big"),
                Identifier.parse("chests/underwater_ruin_small"),
                Identifier.parse("chests/ancient_city"));

        LootPool.Builder pool = LootPool.lootPool().add(NestedLootTable.lootTableReference(OVERWORLD_UF_LOOT).setWeight(1));
        if (tablesToModify.contains(event.getId())) {
            event.getContext().addPool(pool);
        }
    }

    private static void initNetherUltimateFist(LootTableModifyEvent event) {
        List<Identifier> tablesToModify = Arrays.asList(
                Identifier.parse("chests/nether_bridge"),
                Identifier.parse("chests/ruined_portal"));

        LootPool.Builder pool = LootPool.lootPool().add(NestedLootTable.lootTableReference(NETHER_UF_LOOT).setWeight(1));
        if (tablesToModify.contains(event.getId())) {
            event.getContext().addPool(pool);
        }
    }

    private static void initEndUltimateFist(LootTableModifyEvent event) {
        List<Identifier> tablesToModify = Arrays.asList(Identifier.parse("chests/end_city_treasure"));

        LootPool.Builder pool = LootPool.lootPool().add(NestedLootTable.lootTableReference(END_UF_LOOT).setWeight(1));
        if (tablesToModify.contains(event.getId())) {
            event.getContext().addPool(pool);
        }
    }
}
