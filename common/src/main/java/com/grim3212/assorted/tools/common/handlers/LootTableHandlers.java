package com.grim3212.assorted.tools.common.handlers;

import com.grim3212.assorted.lib.events.LootTableModifyEvent;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.ToolsCommonMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class LootTableHandlers {

    // LootTableReference is NestedLootTable now, and it references a loot table by
    // ResourceKey rather than by a bare Identifier.
    private static final ResourceKey<LootTable> OVERWORLD_UF_LOOT = lootTable("fragments_overworld_loot");
    private static final ResourceKey<LootTable> NETHER_UF_LOOT = lootTable("fragments_nether_loot");
    private static final ResourceKey<LootTable> END_UF_LOOT = lootTable("fragments_end_loot");
    private static final ResourceKey<LootTable> NEPTUNE_STAFF_LOOT = lootTable("staffs_neptune_loot");
    private static final ResourceKey<LootTable> PHOENIX_STAFF_LOOT = lootTable("staffs_phoenix_loot");
    private static final ResourceKey<LootTable> POWER_STAFF_LOOT = lootTable("staffs_power_loot");
    private static final ResourceKey<LootTable> FROST_ROD_STRAY_LOOT = lootTable("frost_rod_stray");
    private static final ResourceKey<LootTable> FROST_ROD_SNOWY_LOOT = lootTable("frost_rod_snowy_biome");

    private static final List<Identifier> OVERWORLD_UF_CHESTS = chests("stronghold_corridor", "stronghold_crossing", "stronghold_library", "woodland_mansion", "underwater_ruin_big", "underwater_ruin_small", "ancient_city");
    private static final List<Identifier> NETHER_UF_CHESTS = chests("nether_bridge", "ruined_portal");
    private static final List<Identifier> END_UF_CHESTS = chests("end_city_treasure");
    // Each staff turns up where its element does.
    private static final List<Identifier> NEPTUNE_STAFF_CHESTS = chests("underwater_ruin_big", "shipwreck_treasure", "buried_treasure", "igloo_chest");
    private static final List<Identifier> PHOENIX_STAFF_CHESTS = chests("nether_bridge", "bastion_treasure", "ruined_portal");
    private static final List<Identifier> POWER_STAFF_CHESTS = chests("stronghold_library", "woodland_mansion", "ancient_city", "desert_pyramid", "jungle_temple");

    /** Every monster's loot table but the stray's, which has its own rod pool. Built on first use, once the registries are done. */
    private static Set<Identifier> monsterTables;

    private static ResourceKey<LootTable> lootTable(String name) {
        return ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
    }

    private static List<Identifier> chests(String... names) {
        return Arrays.stream(names).map(name -> Identifier.withDefaultNamespace("chests/" + name)).toList();
    }

    public static void init(LootTableModifyEvent event) {
        if (ToolsCommonMod.COMMON_CONFIG.ultimateFistEnabled.get()) {
            inject(event, OVERWORLD_UF_CHESTS, OVERWORLD_UF_LOOT);
            inject(event, NETHER_UF_CHESTS, NETHER_UF_LOOT);
            inject(event, END_UF_CHESTS, END_UF_LOOT);
        }

        if (ToolsCommonMod.COMMON_CONFIG.staffsEnabled.get()) {
            inject(event, NEPTUNE_STAFF_CHESTS, NEPTUNE_STAFF_LOOT);
            inject(event, PHOENIX_STAFF_CHESTS, PHOENIX_STAFF_LOOT);
            inject(event, EntityTypes.STRAY.getDefaultLootTable().map(ResourceKey::identifier).stream().toList(), FROST_ROD_STRAY_LOOT);
            inject(event, monsterTables(), FROST_ROD_SNOWY_LOOT);
        }

        if (ToolsCommonMod.COMMON_CONFIG.powerStaffEnabled.get()) {
            inject(event, POWER_STAFF_CHESTS, POWER_STAFF_LOOT);
        }
    }

    private static void inject(LootTableModifyEvent event, Collection<Identifier> tables, ResourceKey<LootTable> loot) {
        if (tables.contains(event.getId())) {
            event.getContext().addPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(loot).setWeight(1)));
        }
    }

    private static Set<Identifier> monsterTables() {
        if (monsterTables == null) {
            monsterTables = BuiltInRegistries.ENTITY_TYPE.stream()
                    .filter(type -> type.getCategory() == MobCategory.MONSTER && type != EntityTypes.STRAY)
                    .map(EntityType::getDefaultLootTable)
                    .flatMap(Optional::stream)
                    .map(ResourceKey::identifier)
                    .collect(Collectors.toUnmodifiableSet());
        }
        return monsterTables;
    }
}
