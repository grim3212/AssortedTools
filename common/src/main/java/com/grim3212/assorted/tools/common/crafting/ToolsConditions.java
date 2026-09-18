package com.grim3212.assorted.tools.common.crafting;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.tools.ToolsCommonMod;

public class ToolsConditions {

    public static class Parts {
        public static final String WANDS = "wands";
        public static final String HAMMERS = "hammers";
        public static final String MULTITOOL = "multitools";
        public static final String BOOMERANGS = "boomerangs";
        public static final String POKEBALL = "pokeball";
        public static final String CHICKEN_SUIT = "chickensuit";
        public static final String SPEARS = "spears";
        public static final String THROWING_SPEARS = "throwingspears";
        public static final String BETTER_BUCKETS = "betterbuckets";
        public static final String MORE_SHEARS = "moreshears";
        public static final String ULTIMATE_FIST = "ultimatefist";
        public static final String EXTRA_MATERIAL = "extramaterials";
        public static final String MACHETES = "machetes";
        public static final String PORTABLE_WORKBENCH = "portableworkbench";
        public static final String STAFFS = "staffs";
        public static final String POWER_STAFF = "powerstaff";
    }


    public static void init() {
        Services.CONDITIONS.registerPartCondition(Parts.WANDS, () -> ToolsCommonMod.COMMON_CONFIG.wandsEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.HAMMERS, () -> ToolsCommonMod.COMMON_CONFIG.hammersEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.MULTITOOL, () -> ToolsCommonMod.COMMON_CONFIG.multiToolsEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.BOOMERANGS, () -> ToolsCommonMod.COMMON_CONFIG.boomerangsEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.POKEBALL, () -> ToolsCommonMod.COMMON_CONFIG.pokeballEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.CHICKEN_SUIT, () -> ToolsCommonMod.COMMON_CONFIG.chickenSuitEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.SPEARS, () -> ToolsCommonMod.COMMON_CONFIG.spearsEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.THROWING_SPEARS, () -> ToolsCommonMod.COMMON_CONFIG.throwingSpearsEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.BETTER_BUCKETS, () -> ToolsCommonMod.COMMON_CONFIG.betterBucketsEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.MORE_SHEARS, () -> ToolsCommonMod.COMMON_CONFIG.moreShearsEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.ULTIMATE_FIST, () -> ToolsCommonMod.COMMON_CONFIG.ultimateFistEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.EXTRA_MATERIAL, () -> ToolsCommonMod.COMMON_CONFIG.extraMaterialsEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.MACHETES, () -> ToolsCommonMod.COMMON_CONFIG.machetesEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.PORTABLE_WORKBENCH, () -> ToolsCommonMod.COMMON_CONFIG.portableWorkbenchEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.STAFFS, () -> ToolsCommonMod.COMMON_CONFIG.staffsEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.POWER_STAFF, () -> ToolsCommonMod.COMMON_CONFIG.powerStaffEnabled.get());
    }

}
