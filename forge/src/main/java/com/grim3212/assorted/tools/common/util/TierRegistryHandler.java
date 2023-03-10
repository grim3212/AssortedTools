package com.grim3212.assorted.tools.common.util;

import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.config.ToolsConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class TierRegistryHandler {

    public static void registerTiers() {
        ToolsConfig.Common.moddedTiers.forEach((s, tier) -> {
            Tier equivalent = getEquivalentTier(tier.getHarvestLevel());
            ResourceLocation tierLoc = new ResourceLocation(Constants.MOD_ID, s);
            if (equivalent != null && TierSortingRegistry.byName(tierLoc) == null) {
                Tier nextTier = getNextTier(equivalent);
                TierSortingRegistry.registerTier(tier.getDefaultTier(), tierLoc, List.of(equivalent), nextTier != null ? List.of(nextTier) : List.of());
            }
        });

    }

    private static Tier getEquivalentTier(int level) {
        switch (level) {
            case 1:
                return Tiers.STONE;
            case 2:
                return Tiers.IRON;
            case 3:
                return Tiers.DIAMOND;
            case 4:
                return Tiers.NETHERITE;
            case 0:
                return Tiers.WOOD;
        }
        return null;
    }

    private static Tier getNextTier(Tier tier) {
        if (tier == Tiers.WOOD) {
            return Tiers.STONE;
        } else if (tier == Tiers.STONE) {
            return Tiers.IRON;
        } else if (tier == Tiers.IRON) {
            return Tiers.DIAMOND;
        } else if (tier == Tiers.DIAMOND) {
            return Tiers.NETHERITE;
        }
        return null;
    }
}
