package com.grim3212.assorted.decor.common.crafting;

import com.grim3212.assorted.decor.config.ToolsConfig;
import com.grim3212.assorted.lib.platform.Services;

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
        Services.CONDITIONS.registerPartCondition(Parts.WANDS, () -> ToolsConfig.Common.wandsEnabled.getValue());
        Services.CONDITIONS.registerPartCondition(Parts.HAMMERS, () -> ToolsConfig.Common.hammersEnabled.getValue());
        Services.CONDITIONS.registerPartCondition(Parts.MULTITOOL, () -> ToolsConfig.Common.multiToolsEnabled.getValue());
        Services.CONDITIONS.registerPartCondition(Parts.BOOMERANGS, () -> ToolsConfig.Common.boomerangsEnabled.getValue());
        Services.CONDITIONS.registerPartCondition(Parts.POKEBALL, () -> ToolsConfig.Common.pokeballEnabled.getValue());
        Services.CONDITIONS.registerPartCondition(Parts.CHICKEN_SUIT, () -> ToolsConfig.Common.chickenSuitEnabled.getValue());
        Services.CONDITIONS.registerPartCondition(Parts.SPEARS, () -> ToolsConfig.Common.spearsEnabled.getValue());
        Services.CONDITIONS.registerPartCondition(Parts.BETTER_SPEARS, () -> ToolsConfig.Common.betterSpearsEnabled.getValue());
        Services.CONDITIONS.registerPartCondition(Parts.BETTER_BUCKETS, () -> ToolsConfig.Common.betterBucketsEnabled.getValue());
        Services.CONDITIONS.registerPartCondition(Parts.MORE_SHEARS, () -> ToolsConfig.Common.moreShearsEnabled.getValue());
        Services.CONDITIONS.registerPartCondition(Parts.ULTIMATE_FIST, () -> ToolsConfig.Common.ultimateFistEnabled.getValue());
        Services.CONDITIONS.registerPartCondition(Parts.EXTRA_MATERIAL, () -> ToolsConfig.Common.extraMaterialsEnabled.getValue());
    }

}
