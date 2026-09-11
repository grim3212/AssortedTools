package com.grim3212.assorted.tools.gametest;

import com.grim3212.assorted.lib.util.NBTHelper;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.grim3212.assorted.tools.common.item.WandBreakingItem;
import com.grim3212.assorted.tools.common.item.WandBuildingItem;
import com.grim3212.assorted.tools.common.item.WandMiningItem;
import com.grim3212.assorted.tools.common.network.ToolCycleModesPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.tools.gametest.ToolsTestSupport.*;

/**
 * Wands: mining, building and breaking, and their modes.
 */
final class WandTests {

    private WandTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("mining_wand_cycles_and_mines", WandTests::miningWandCyclesAndMines);
        out.accept("building_wand_builds_and_consumes_blocks", WandTests::buildingWandBuildsAndConsumesBlocks);
        out.accept("breaking_wands_clear_their_modes", WandTests::breakingWandsClearTheirModes);
    }

    /**
     * The mining wand: cycling its mode through the packet handler, then the two click selection
     * that mines everything between the corners. The selection anchor moved out of the stack's bare
     * NBT into {@code minecraft:custom_data} during the port, which is what the {@code Start}
     * assertions here are watching.
     */
    private static void miningWandCyclesAndMines(GameTestHelper helper) {
        WandMiningItem wand = ToolsItems.MINING_WAND.get();
        ItemStack stack = new ItemStack(wand);

        ServerPlayer player = survivalPlayer(helper, stack);
        stand(helper, player, new BlockPos(3, 1, 2));
        wand.onCraftedBy(stack, player);
        helper.assertValueEqual(NBTHelper.getString(stack, "Mode"), "mineall", "the mode a freshly crafted mining wand starts in");

        ToolCycleModesPacket.handle(new ToolCycleModesPacket(InteractionHand.MAIN_HAND), player);
        helper.assertValueEqual(NBTHelper.getString(player.getItemInHand(InteractionHand.MAIN_HAND), "Mode"), "minedirt", "the mode after one cycle");

        // Back round to mineall: a plain mining wand only has the three non reinforced modes.
        ToolCycleModesPacket.handle(new ToolCycleModesPacket(InteractionHand.MAIN_HAND), player);
        ToolCycleModesPacket.handle(new ToolCycleModesPacket(InteractionHand.MAIN_HAND), player);
        helper.assertValueEqual(NBTHelper.getString(player.getItemInHand(InteractionHand.MAIN_HAND), "Mode"), "mineall", "the mode after cycling all the way round");

        final BlockPos start = new BlockPos(2, 1, 4);
        final BlockPos middle = new BlockPos(3, 1, 4);
        final BlockPos end = new BlockPos(4, 1, 4);
        helper.setBlock(start, Blocks.STONE);
        helper.setBlock(middle, Blocks.STONE);
        helper.setBlock(end, Blocks.STONE);

        useOnTopOf(helper, player, start);
        helper.assertTrue(NBTHelper.hasTag(player.getItemInHand(InteractionHand.MAIN_HAND), "Start"), "the first click did not record a selection start");

        useOnTopOf(helper, player, end);

        helper.assertBlockPresent(Blocks.AIR, start);
        helper.assertBlockPresent(Blocks.AIR, middle);
        helper.assertBlockPresent(Blocks.AIR, end);
        helper.assertItemEntityPresent(Items.COBBLESTONE, middle, 3.0D);
        helper.assertFalse(NBTHelper.hasTag(player.getItemInHand(InteractionHand.MAIN_HAND), "Start"), "the selection was not cleared after mining");
        helper.assertValueEqual(player.getItemInHand(InteractionHand.MAIN_HAND).getDamageValue(), 1, "wand durability spent");

        helper.succeed();
    }

    /**
     * The building wand fills the gap between two corners, pays for it out of the inventory, and
     * refuses when the inventory cannot cover it.
     */
    private static void buildingWandBuildsAndConsumesBlocks(GameTestHelper helper) {
        final BlockPos start = new BlockPos(2, 1, 2);
        final BlockPos gap = new BlockPos(2, 1, 3);
        final BlockPos end = new BlockPos(2, 1, 4);
        final BlockPos poorStart = new BlockPos(6, 1, 2);
        final BlockPos poorGap = new BlockPos(6, 1, 3);
        final BlockPos poorEnd = new BlockPos(6, 1, 4);

        for (BlockPos corner : List.of(start, end, poorStart, poorEnd)) {
            helper.setBlock(corner, Blocks.STONE);
        }

        WandBuildingItem wand = ToolsItems.BUILDING_WAND.get();
        ItemStack stack = new ItemStack(wand);
        ServerPlayer player = survivalPlayer(helper, stack);
        stand(helper, player, new BlockPos(4, 1, 3));
        wand.onCraftedBy(stack, player);
        helper.assertValueEqual(NBTHelper.getString(stack, "Mode"), "buildbox", "the mode a freshly crafted building wand starts in");

        player.getInventory().add(new ItemStack(Blocks.STONE, 8));

        useOnTopOf(helper, player, start);
        helper.assertTrue(NBTHelper.hasTag(player.getItemInHand(InteractionHand.MAIN_HAND), "Start"), "the first click did not record a selection start");
        useOnTopOf(helper, player, end);

        helper.assertBlockPresent(Blocks.STONE, gap);
        helper.assertValueEqual(countInInventory(player, Blocks.STONE.asItem()), 7, "stone left after the wand filled one gap");
        helper.assertValueEqual(player.getItemInHand(InteractionHand.MAIN_HAND).getDamageValue(), 1, "building wand durability spent");

        // The same two clicks again with nothing to build from.
        removeFromInventory(player, Blocks.STONE.asItem());
        useOnTopOf(helper, player, poorStart);
        useOnTopOf(helper, player, poorEnd);

        helper.assertBlockPresent(Blocks.AIR, poorGap);
        helper.assertValueEqual(player.getItemInHand(InteractionHand.MAIN_HAND).getDamageValue(), 1, "the wand should not wear when it refuses to build");

        helper.succeed();
    }

    /**
     * The breaking wand in its default weak mode clears what a piston would break and nothing else,
     * and the reinforced one cycled to break-all clears stone as well.
     */
    private static void breakingWandsClearTheirModes(GameTestHelper helper) {
        final BlockPos torchStart = new BlockPos(1, 1, 1);
        final BlockPos torchMiddle = new BlockPos(2, 1, 1);
        final BlockPos torchEnd = new BlockPos(3, 1, 1);
        final BlockPos stoneStart = new BlockPos(5, 1, 1);
        final BlockPos stoneMiddle = new BlockPos(6, 1, 1);
        final BlockPos stoneEnd = new BlockPos(7, 1, 1);

        for (BlockPos torch : List.of(torchStart, torchMiddle, torchEnd)) {
            helper.setBlock(torch, Blocks.TORCH);
        }
        for (BlockPos stone : List.of(stoneStart, stoneMiddle, stoneEnd)) {
            helper.setBlock(stone, Blocks.STONE);
        }

        WandBreakingItem wand = ToolsItems.BREAKING_WAND.get();
        ItemStack stack = new ItemStack(wand);
        ServerPlayer player = survivalPlayer(helper, stack);
        stand(helper, player, new BlockPos(4, 1, 3));
        wand.onCraftedBy(stack, player);
        helper.assertValueEqual(NBTHelper.getString(stack, "Mode"), "breakweak", "the mode a freshly crafted breaking wand starts in");

        useOnTopOf(helper, player, torchStart);
        useOnTopOf(helper, player, torchEnd);

        helper.assertBlockPresent(Blocks.AIR, torchStart);
        helper.assertBlockPresent(Blocks.AIR, torchMiddle);
        helper.assertBlockPresent(Blocks.AIR, torchEnd);
        // Weak mode leaves stone alone: the wand decides per block, not per selection.
        helper.assertBlockPresent(Blocks.STONE, stoneMiddle);
        helper.assertValueEqual(player.getItemInHand(InteractionHand.MAIN_HAND).getDamageValue(), 1, "breaking wand durability spent");

        WandBreakingItem reinforced = ToolsItems.REINFORCED_BREAKING_WAND.get();
        ItemStack reinforcedStack = new ItemStack(reinforced);
        reinforced.onCraftedBy(reinforcedStack, player);
        player.setItemInHand(InteractionHand.MAIN_HAND, reinforcedStack);

        ToolCycleModesPacket.handle(new ToolCycleModesPacket(InteractionHand.MAIN_HAND), player);
        helper.assertValueEqual(NBTHelper.getString(player.getItemInHand(InteractionHand.MAIN_HAND), "Mode"), "breakall", "the mode after one cycle on a reinforced breaking wand");

        useOnTopOf(helper, player, stoneStart);
        useOnTopOf(helper, player, stoneEnd);

        helper.assertBlockPresent(Blocks.AIR, stoneStart);
        helper.assertBlockPresent(Blocks.AIR, stoneMiddle);
        helper.assertBlockPresent(Blocks.AIR, stoneEnd);
        helper.assertValueEqual(player.getItemInHand(InteractionHand.MAIN_HAND).getDamageValue(), 1, "reinforced breaking wand durability spent");

        helper.succeed();
    }
}
