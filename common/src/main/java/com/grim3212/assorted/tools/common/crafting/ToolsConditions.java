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
        public static final String BETTER_SPEARS = "betterspears";
        public static final String BETTER_BUCKETS = "betterbuckets";
        public static final String MORE_SHEARS = "moreshears";
        public static final String ULTIMATE_FIST = "ultimatefist";
        public static final String EXTRA_MATERIAL = "extramaterials";
    }


    public static void init() {
        Services.CONDITIONS.registerPartCondition(Parts.WANDS, () -> ToolsCommonMod.COMMON_CONFIG.wandsEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.HAMMERS, () -> ToolsCommonMod.COMMON_CONFIG.hammersEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.MULTITOOL, () -> ToolsCommonMod.COMMON_CONFIG.multiToolsEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.BOOMERANGS, () -> ToolsCommonMod.COMMON_CONFIG.boomerangsEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.POKEBALL, () -> ToolsCommonMod.COMMON_CONFIG.pokeballEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.CHICKEN_SUIT, () -> ToolsCommonMod.COMMON_CONFIG.chickenSuitEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.SPEARS, () -> ToolsCommonMod.COMMON_CONFIG.spearsEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.BETTER_SPEARS, () -> ToolsCommonMod.COMMON_CONFIG.betterSpearsEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.BETTER_BUCKETS, () -> ToolsCommonMod.COMMON_CONFIG.betterBucketsEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.MORE_SHEARS, () -> ToolsCommonMod.COMMON_CONFIG.moreShearsEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.ULTIMATE_FIST, () -> ToolsCommonMod.COMMON_CONFIG.ultimateFistEnabled.get());
        Services.CONDITIONS.registerPartCondition(Parts.EXTRA_MATERIAL, () -> ToolsCommonMod.COMMON_CONFIG.extraMaterialsEnabled.get());
    }

}
