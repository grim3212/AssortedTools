package com.grim3212.assorted.tools.common.handlers;

import com.grim3212.assorted.lib.events.CorrectToolForDropEvent;
import com.grim3212.assorted.lib.events.OnDropStacksEvent;
import com.grim3212.assorted.tools.ToolsCommonMod;
import com.grim3212.assorted.tools.api.ToolsTags;
import com.grim3212.assorted.tools.common.enchantment.ToolsEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
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
                ServerLevel level = event.getWorld();
                ItemStack toolStack = event.getStack();

                // Enchantments::* are ResourceKeys now, so getting to the enchantment itself - or
                // to a level on a stack - needs a lookup against the level's enchantment registry.
                Holder<Enchantment> silkTouch = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH);

                // Silk touch will handle this already
                if (EnchantmentHelper.getItemEnchantmentLevel(silkTouch, toolStack) > 0) {
                    return;
                }

                // Blocks no longer carry a loot table id directly; they carry an Optional
                // ResourceKey, resolved through the server's reloadable registries.
                Optional<ResourceKey<LootTable>> lootTableKey = event.getState().getBlock().getLootTable();
                if (lootTableKey.isEmpty()) {
                    return;
                }

                ItemStack fakeTool = toolStack.copy();
                fakeTool.enchant(silkTouch, 1);
                LootParams fakeContext = new LootParams.Builder(level)
                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(event.getPos()))
                        .withParameter(LootContextParams.BLOCK_STATE, event.getState())
                        .withParameter(LootContextParams.TOOL, fakeTool)
                        .create(LootContextParamSets.BLOCK);
                LootTable loottable = level.getServer().reloadableRegistries().getLootTable(lootTableKey.get());
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
