package com.grim3212.assorted.tools.gametest;

import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.tools.api.item.HarvestTiers;
import com.grim3212.assorted.tools.common.item.MultiToolItem;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.tools.gametest.ToolsTestSupport.*;

/**
 * Mining tools: the hammer, the multitool, the ultimate fist, harvest tiers and mining speed.
 */
final class MiningToolTests {

    private MiningToolTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("hammer_breaks_and_wears", MiningToolTests::hammerBreaksAndWears);
        out.accept("multitool_mines_every_tool_class", MiningToolTests::multitoolMinesEveryToolClass);
        out.accept("multitool_strips_and_paths", MiningToolTests::multitoolStripsAndPaths);
        out.accept("harvest_tier_gates_drops", MiningToolTests::harvestTierGatesDrops);
        out.accept("ultimate_fist_breaks_obsidian", MiningToolTests::ultimateFistBreaksObsidian);
        out.accept("tools_mine_at_their_tier_speed", MiningToolTests::toolsMineAtTheirTierSpeed);
        out.accept("tools_are_in_the_convention_tool_tags", MiningToolTests::toolsAreInTheConventionToolTags);
    }

    /**
     * The hammer breaks what it hits and refuses the normal destroy path ({@code canDestroyBlock}).
     * Obsidian, because the hammer decides, not the tier.
     */
    private static void hammerBreaksAndWears(GameTestHelper helper) {
        final BlockPos target = new BlockPos(4, 1, 4);
        helper.setBlock(target, Blocks.OBSIDIAN);

        BlockState obsidian = Blocks.OBSIDIAN.defaultBlockState();
        ServerPlayer player = survivalPlayer(helper, new ItemStack(ToolsItems.WOOD_HAMMER.get()));
        stand(helper, player, new BlockPos(4, 1, 2));

        ItemStack hammer = player.getItemInHand(InteractionHand.MAIN_HAND);
        helper.assertTrue(hammer.isCorrectToolForDrops(obsidian), "a hammer should be the correct tool for anything");
        helper.assertValueEqual(hammer.getDestroySpeed(obsidian), 80.0F, "hammer destroy speed");

        boolean destroyed = player.gameMode.destroyBlock(helper.absolutePos(target));

        helper.assertFalse(destroyed, "the hammer should break the block itself and then abort the normal destroy path");
        helper.assertBlockPresent(Blocks.AIR, target);
        helper.assertValueEqual(player.getItemInHand(InteractionHand.MAIN_HAND).getDamageValue(), 1, "hammer durability spent");

        helper.succeed();
    }

    /**
     * One multitool mines pickaxe, axe and shovel blocks and drops all three, which is what the
     * {@code c:mineable/multitool} tag baked into its {@code minecraft:tool} component has to
     * deliver now that the item carries no tool class of its own.
     */
    private static void multitoolMinesEveryToolClass(GameTestHelper helper) {
        final BlockPos stone = new BlockPos(2, 1, 4);
        final BlockPos log = new BlockPos(4, 1, 4);
        final BlockPos dirt = new BlockPos(6, 1, 4);

        helper.setBlock(stone, Blocks.STONE);
        helper.setBlock(log, Blocks.OAK_LOG);
        helper.setBlock(dirt, Blocks.DIRT);

        MultiToolItem multitool = ToolsItems.DIAMOND_MULTITOOL.get();
        ServerPlayer player = survivalPlayer(helper, new ItemStack(multitool));
        stand(helper, player, new BlockPos(4, 1, 2));

        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        // Cobweb is the one block the multitool answers for by hand rather than through the tag.
        helper.assertValueEqual(stack.getDestroySpeed(Blocks.COBWEB.defaultBlockState()), 15.0F, "multitool speed on cobweb");
        float efficiency = multitool.getTierHolder().getEfficiency();
        helper.assertValueEqual(stack.getDestroySpeed(Blocks.STONE.defaultBlockState()), efficiency, "multitool speed on stone");
        helper.assertValueEqual(stack.getDestroySpeed(Blocks.OAK_LOG.defaultBlockState()), efficiency, "multitool speed on a log");
        helper.assertValueEqual(stack.getDestroySpeed(Blocks.DIRT.defaultBlockState()), efficiency, "multitool speed on dirt");

        helper.assertTrue(player.gameMode.destroyBlock(helper.absolutePos(stone)), "the multitool could not break stone");
        helper.assertTrue(player.gameMode.destroyBlock(helper.absolutePos(log)), "the multitool could not break a log");
        helper.assertTrue(player.gameMode.destroyBlock(helper.absolutePos(dirt)), "the multitool could not break dirt");

        helper.assertItemEntityPresent(Items.COBBLESTONE, stone, 2.0D);
        helper.assertItemEntityPresent(Items.OAK_LOG, log, 2.0D);
        helper.assertItemEntityPresent(Items.DIRT, dirt, 2.0D);

        helper.succeed();
    }

    /**
     * The multitool strips and paths by delegating to vanilla's axe and shovel. On NeoForge those
     * refuse unless the stack declares the {@code ItemAbility}, so a missing one leaves the block
     * unchanged.
     */
    private static void multitoolStripsAndPaths(GameTestHelper helper) {
        final BlockPos log = new BlockPos(2, 1, 4);
        final BlockPos grass = new BlockPos(4, 1, 4);

        helper.setBlock(log, Blocks.OAK_LOG);
        helper.setBlock(grass, Blocks.GRASS_BLOCK);

        ServerPlayer player = survivalPlayer(helper, new ItemStack(ToolsItems.DIAMOND_MULTITOOL.get()));
        stand(helper, player, new BlockPos(3, 1, 2));

        helper.assertTrue(useOnTopOf(helper, player, log).consumesAction(), "the multitool did not act as an axe");
        helper.assertBlockPresent(Blocks.STRIPPED_OAK_LOG, log);

        helper.assertTrue(useOnTopOf(helper, player, grass).consumesAction(), "the multitool did not act as a shovel");
        helper.assertBlockPresent(Blocks.DIRT_PATH, grass);

        helper.succeed();
    }

    /**
     * Harvest level used to be an integer compared against the block's; it is a block tag baked
     * into the tool now. Both halves are pinned here: the mapping itself, and that a tier too low
     * really does lose the drops in world.
     */
    private static void harvestTierGatesDrops(GameTestHelper helper) {
        helper.assertTrue(HarvestTiers.incorrectBlocksForDrops(0) == BlockTags.INCORRECT_FOR_WOODEN_TOOL, "harvest level 0 should map to the wooden tag");
        helper.assertTrue(HarvestTiers.incorrectBlocksForDrops(1) == BlockTags.INCORRECT_FOR_STONE_TOOL, "harvest level 1 should map to the stone tag");
        helper.assertTrue(HarvestTiers.incorrectBlocksForDrops(2) == BlockTags.INCORRECT_FOR_IRON_TOOL, "harvest level 2 should map to the iron tag");
        helper.assertTrue(HarvestTiers.incorrectBlocksForDrops(3) == BlockTags.INCORRECT_FOR_DIAMOND_TOOL, "harvest level 3 should map to the diamond tag");
        // Everything above diamond collapses onto netherite: there is nothing stronger to map to.
        helper.assertTrue(HarvestTiers.incorrectBlocksForDrops(7) == BlockTags.INCORRECT_FOR_NETHERITE_TOOL, "harvest levels above 3 should map to the netherite tag");

        final BlockPos oreA = new BlockPos(1, 1, 6);
        final BlockPos oreB = new BlockPos(3, 1, 6);
        final BlockPos obsidianA = new BlockPos(5, 1, 6);
        final BlockPos obsidianB = new BlockPos(7, 1, 6);

        // Diamond ore, not iron: a stone tool mines iron ore perfectly well in vanilla, so iron ore
        // would not tell the two tiers apart.
        helper.setBlock(oreA, Blocks.DIAMOND_ORE);
        helper.setBlock(oreB, Blocks.DIAMOND_ORE);
        helper.setBlock(obsidianA, Blocks.OBSIDIAN);
        helper.setBlock(obsidianB, Blocks.OBSIDIAN);

        ServerPlayer player = survivalPlayer(helper, new ItemStack(ToolsItems.STONE_MULTITOOL.get()));
        stand(helper, player, new BlockPos(4, 1, 4));

        player.gameMode.destroyBlock(helper.absolutePos(oreA));
        helper.assertBlockPresent(Blocks.AIR, oreA);
        helper.assertItemEntityNotPresent(Items.DIAMOND, oreA, 1.5D);

        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ToolsItems.IRON_MULTITOOL.get()));
        player.gameMode.destroyBlock(helper.absolutePos(oreB));
        helper.assertItemEntityPresent(Items.DIAMOND, oreB, 1.5D);

        player.gameMode.destroyBlock(helper.absolutePos(obsidianA));
        helper.assertBlockPresent(Blocks.AIR, obsidianA);
        helper.assertItemEntityNotPresent(Items.OBSIDIAN, obsidianA, 1.5D);

        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ToolsItems.DIAMOND_MULTITOOL.get()));
        player.gameMode.destroyBlock(helper.absolutePos(obsidianB));
        helper.assertItemEntityPresent(Items.OBSIDIAN, obsidianB, 1.5D);

        helper.succeed();
    }

    /**
     * The ultimate fist mines anything and pays a durability point for it. It carries no
     * {@code minecraft:tool} component at all, so both of those are its own overrides rather than
     * anything the component system does for it.
     */
    private static void ultimateFistBreaksObsidian(GameTestHelper helper) {
        final BlockPos target = new BlockPos(4, 1, 4);
        helper.setBlock(target, Blocks.OBSIDIAN);

        ServerPlayer player = survivalPlayer(helper, new ItemStack(ToolsItems.ULTIMATE_FIST.get()));
        stand(helper, player, new BlockPos(4, 1, 2));

        ItemStack fist = player.getItemInHand(InteractionHand.MAIN_HAND);
        helper.assertTrue(fist.isCorrectToolForDrops(Blocks.OBSIDIAN.defaultBlockState()), "the ultimate fist should mine anything");
        helper.assertTrue(fist.getDestroySpeed(Blocks.OBSIDIAN.defaultBlockState()) > 1.0F, "the ultimate fist has no mining speed");

        helper.assertTrue(player.gameMode.destroyBlock(helper.absolutePos(target)), "the ultimate fist could not break obsidian");
        helper.assertItemEntityPresent(Items.OBSIDIAN, target, 2.0D);
        helper.assertValueEqual(player.getItemInHand(InteractionHand.MAIN_HAND).getDamageValue(), 1, "ultimate fist durability spent");

        helper.succeed();
    }

    /**
     * Each tool is measured against its own configured tier, then the set is checked to be ordered
     * like the configured efficiencies, which catches a tool wired to the wrong tier.
     */
    private static void toolsMineAtTheirTierSpeed(GameTestHelper helper) {
        final BlockState stone = Blocks.STONE.defaultBlockState();
        final BlockState dirt = Blocks.DIRT.defaultBlockState();
        final BlockState log = Blocks.OAK_LOG.defaultBlockState();
        final BlockState hay = Blocks.HAY_BLOCK.defaultBlockState();
        // Glass is in none of the mineable tags, so every one of these falls back to bare hands.
        final BlockState glass = Blocks.GLASS.defaultBlockState();

        List<ItemTierConfig> tiers = new ArrayList<>();
        List<Item> pickaxes = new ArrayList<>();

        for (ToolsItems.MaterialGroup group : ToolsItems.MATERIAL_GROUPS.values()) {
            float efficiency = group.tier.getEfficiency();
            String name = group.tier.getName();

            assertSpeed(helper, group.PICKAXE.get(), stone, efficiency, name + " pickaxe on stone");
            assertSpeed(helper, group.SHOVEL.get(), dirt, efficiency, name + " shovel on dirt");
            assertSpeed(helper, group.AXE.get(), log, efficiency, name + " axe on a log");
            assertSpeed(helper, group.HOE.get(), hay, efficiency, name + " hoe on a hay block");
            assertSpeed(helper, group.PICKAXE.get(), glass, 1.0F, name + " pickaxe on a block it does not mine");

            helper.assertTrue(efficiency > 1.0F, name + " is configured to mine no faster than a bare hand");

            tiers.add(group.tier);
            pickaxes.add(group.PICKAXE.get());
        }

        // Sorted by configured efficiency, the measured speeds have to come out sorted too. This is
        // what catches an item handed the wrong tier, which a per tool equality cannot see.
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < tiers.size(); i++) {
            order.add(i);
        }
        order.sort(Comparator.comparingDouble(i -> tiers.get(i).getEfficiency()));

        for (int i = 1; i < order.size(); i++) {
            ItemTierConfig slower = tiers.get(order.get(i - 1));
            ItemTierConfig faster = tiers.get(order.get(i));
            float slowSpeed = new ItemStack(pickaxes.get(order.get(i - 1))).getDestroySpeed(stone);
            float fastSpeed = new ItemStack(pickaxes.get(order.get(i))).getDestroySpeed(stone);
            helper.assertTrue(slowSpeed <= fastSpeed, slower.getName() + " mines faster than " + faster.getName() + ", the wrong way round for their configured efficiencies");
        }

        helper.succeed();
    }

    /**
     * Extra-material swords and axes are in {@code c:tools/melee_weapon}, pickaxes in {@code
     * c:tools/mining_tool}, multitools in both and the ultimate fist in the first; the loaders list
     * only vanilla items. Hammers deal no damage of their own, so they stay out.
     */
    private static void toolsAreInTheConventionToolTags(GameTestHelper helper) {
        List<Item> multitools = new ArrayList<>(List.of(ToolsItems.WOODEN_MULTITOOL.get(), ToolsItems.STONE_MULTITOOL.get(), ToolsItems.GOLDEN_MULTITOOL.get(), ToolsItems.IRON_MULTITOOL.get(), ToolsItems.DIAMOND_MULTITOOL.get(), ToolsItems.NETHERITE_MULTITOOL.get()));
        List<Item> weapons = new ArrayList<>(List.of(ToolsItems.ULTIMATE_FIST.get()));
        List<Item> miningTools = new ArrayList<>();
        for (ToolsItems.MaterialGroup group : ToolsItems.MATERIAL_GROUPS.values()) {
            multitools.add(group.MULTITOOL.get());
            weapons.add(group.SWORD.get());
            weapons.add(group.AXE.get());
            miningTools.add(group.PICKAXE.get());
        }
        weapons.addAll(multitools);
        miningTools.addAll(multitools);

        List<String> missing = new ArrayList<>();
        expect(missing, LibCommonTags.Items.TOOLS_MELEE_WEAPONS, weapons.toArray(Item[]::new));
        expect(missing, LibCommonTags.Items.TOOLS_MINING_TOOLS, miningTools.toArray(Item[]::new));
        helper.assertTrue(missing.isEmpty(), "missing from the convention tool tags: " + String.join(", ", missing));
        helper.assertFalse(new ItemStack(ToolsItems.IRON_HAMMER.get()).is(LibCommonTags.Items.TOOLS_MELEE_WEAPONS), "a hammer is tagged as a melee weapon");
        helper.succeed();
    }
}
