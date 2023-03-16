package com.grim3212.assorted.tools.common.handlers;

import com.grim3212.assorted.lib.events.LootTableModifyEvent;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.ToolsCommonMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;

import java.util.Arrays;
import java.util.List;

public class LootTableHandlers {

    private static final ResourceLocation OVERWORLD_UF_LOOT = new ResourceLocation(Constants.MOD_ID, "fragments_overworld_loot");
    private static final ResourceLocation NETHER_UF_LOOT = new ResourceLocation(Constants.MOD_ID, "fragments_nether_loot");
    private static final ResourceLocation END_UF_LOOT = new ResourceLocation(Constants.MOD_ID, "fragments_end_loot");

    public static void init(LootTableModifyEvent event) {
        if (ToolsCommonMod.COMMON_CONFIG.ultimateFistEnabled.get()) {
            initOverworldUltimateFist(event);
            initNetherUltimateFist(event);
            initEndUltimateFist(event);
        }
    }

    private static void initOverworldUltimateFist(LootTableModifyEvent event) {
        List<ResourceLocation> tablesToModify = Arrays.asList(
                new ResourceLocation("chests/stronghold_corridor"),
                new ResourceLocation("chests/stronghold_crossing"),
                new ResourceLocation("chests/stronghold_library"),
                new ResourceLocation("chests/woodland_mansion"),
                new ResourceLocation("chests/underwater_ruin_big"),
                new ResourceLocation("chests/underwater_ruin_small"),
                new ResourceLocation("chests/ancient_city"));

        LootPool.Builder pool = LootPool.lootPool().add(LootTableReference.lootTableReference(OVERWORLD_UF_LOOT).setWeight(1));
        if (tablesToModify.contains(event.getId())) {
            event.getContext().addPool(pool);
        }
    }

    private static void initNetherUltimateFist(LootTableModifyEvent event) {
        List<ResourceLocation> tablesToModify = Arrays.asList(
                new ResourceLocation("chests/nether_bridge"),
                new ResourceLocation("chests/ruined_portal"));

        LootPool.Builder pool = LootPool.lootPool().add(LootTableReference.lootTableReference(NETHER_UF_LOOT).setWeight(1));
        if (tablesToModify.contains(event.getId())) {
            event.getContext().addPool(pool);
        }
    }

    private static void initEndUltimateFist(LootTableModifyEvent event) {
        List<ResourceLocation> tablesToModify = Arrays.asList(new ResourceLocation("chests/end_city_treasure"));

        LootPool.Builder pool = LootPool.lootPool().add(LootTableReference.lootTableReference(END_UF_LOOT).setWeight(1));
        if (tablesToModify.contains(event.getId())) {
            event.getContext().addPool(pool);
        }
    }
}
