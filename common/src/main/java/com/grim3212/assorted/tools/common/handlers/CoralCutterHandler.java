package com.grim3212.assorted.tools.common.handlers;

import com.grim3212.assorted.lib.events.CorrectToolForDropEvent;
import com.grim3212.assorted.lib.events.OnDropStacksEvent;
import com.grim3212.assorted.tools.ToolsCommonMod;
import com.grim3212.assorted.tools.api.ToolsTags;
import com.grim3212.assorted.tools.common.enchantment.ToolsEnchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class CoralCutterHandler {
    public static void handleDrop(final OnDropStacksEvent event) {
        if (ToolsCommonMod.COMMON_CONFIG.moreShearsEnabled.get()) {
            // We only will ever modify corals
            if (event.getState().is(ToolsTags.Blocks.ALL_CORALS)) {
                ItemStack toolStack = event.getStack();

                // Silk touch will handle this already
                if (EnchantmentHelper.getEnchantments(toolStack).containsKey(Enchantments.SILK_TOUCH)) {
                    return;
                }

                ItemStack fakeTool = toolStack.copy();
                fakeTool.enchant(Enchantments.SILK_TOUCH, 1);
                LootContext fakeContext = new LootContext.Builder(event.getWorld())
                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(event.getPos()))
                        .withParameter(LootContextParams.BLOCK_STATE, event.getState())
                        .withParameter(LootContextParams.TOOL, fakeTool)
                        .create(LootContextParamSets.BLOCK);
                LootTable loottable = event.getWorld().getServer().getLootTables().get(event.getState().getBlock().getLootTable());
                List<ItemStack> droppedItems = loottable.getRandomItems(fakeContext);
                event.setDrops(droppedItems);
            }
        }
    }

    public static void handleCorrectTool(final CorrectToolForDropEvent event) {
        ItemStack stack = event.getStack();
        BlockState state = event.getState();

        if (ToolsEnchantments.hasCoralCutter(stack) && state.is(ToolsTags.Blocks.ALL_CORALS)) {
            event.setResponse(Optional.of(true));
        }
    }
}
