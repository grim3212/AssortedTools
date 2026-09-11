package com.grim3212.assorted.tools.api.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

/**
 * Maps the configured numeric harvest level to the block tag a tool cannot get drops from
 * ({@code incorrectBlocksForDrops}). Levels above netherite (ULTIMATE is 7) map to the netherite
 * tag, the strongest vanilla has.
 */
public final class HarvestTiers {

    private HarvestTiers() {
    }

    public static TagKey<Block> incorrectBlocksForDrops(int harvestLevel) {
        return switch (Math.max(0, harvestLevel)) {
            case 0 -> BlockTags.INCORRECT_FOR_WOODEN_TOOL;
            case 1 -> BlockTags.INCORRECT_FOR_STONE_TOOL;
            case 2 -> BlockTags.INCORRECT_FOR_IRON_TOOL;
            case 3 -> BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
            default -> BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
        };
    }

    /**
     * The inverse, for reading a level back off a vanilla {@code ToolMaterial}. Only used to seed
     * the configuration defaults for the vanilla tiers, which no longer carry a number of their own.
     */
    public static int harvestLevelOf(TagKey<Block> incorrectBlocksForDrops) {
        if (incorrectBlocksForDrops == BlockTags.INCORRECT_FOR_WOODEN_TOOL) return 0;
        // Gold is the odd one out - a weak tool that still gets drops from iron-level blocks, so it
        // does not sit on the ladder at all. Seeded at 0, matching what it could mine in 1.20.1.
        if (incorrectBlocksForDrops == BlockTags.INCORRECT_FOR_GOLD_TOOL) return 0;
        if (incorrectBlocksForDrops == BlockTags.INCORRECT_FOR_STONE_TOOL) return 1;
        if (incorrectBlocksForDrops == BlockTags.INCORRECT_FOR_COPPER_TOOL) return 1;
        if (incorrectBlocksForDrops == BlockTags.INCORRECT_FOR_IRON_TOOL) return 2;
        if (incorrectBlocksForDrops == BlockTags.INCORRECT_FOR_DIAMOND_TOOL) return 3;
        if (incorrectBlocksForDrops == BlockTags.INCORRECT_FOR_NETHERITE_TOOL) return 4;
        return 0;
    }
}
