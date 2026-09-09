package com.grim3212.assorted.tools.api.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

/**
 * Translates the mod's numeric harvest levels into the tag vanilla actually uses.
 * <p>
 * Harvest level as a number is gone. Since 1.21.2 a tool declares the blocks it <em>cannot</em>
 * get drops from as a {@link TagKey}, baked into its {@code minecraft:tool} data component, and
 * every mining check reads that. The configuration still exposes {@code harvestLevel} as an integer
 * because that is what the option has always been called and what packs are written against, so it
 * is mapped here rather than removed.
 * <p>
 * Levels above netherite have nothing stronger to map to - {@code ULTIMATE} is level 7 and lands on
 * the netherite tag, which is the strongest thing vanilla defines. It mines everything either way.
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
