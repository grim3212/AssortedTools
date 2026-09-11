package com.grim3212.assorted.tools.gametest;

import com.grim3212.assorted.tools.common.item.CapturedEntity;
import com.grim3212.assorted.tools.common.item.ToolsDataComponents;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * The pokeball's tooltip as Fabric builds it, which only happens on the client; the server gametest
 * covers NeoForge. Run with {@code ./gradlew :fabric:runClientGameTest}; it exits non-zero on a
 * failure.
 */
public class ToolsClientGameTests implements FabricClientGameTest {

    @Override
    public void runTest(ClientGameTestContext context) {
        // Inside a world: an ItemStack cannot be made on the title screen, because an item's default
        // components are only bound once a world's registries have loaded.
        try (TestSingleplayerContext world = context.worldBuilder().create()) {
            context.runOnClient(client -> {
                List<String> empty = tooltipKeys(client, new ItemStack(ToolsItems.POKEBALL.get()));
                if (!empty.contains("tooltip.pokeball.empty")) {
                    throw new AssertionError("an empty pokeball's tooltip is " + empty);
                }

                CompoundTag cow = new CompoundTag();
                cow.putString("id", "minecraft:cow");
                cow.putString("pokeball_name", "entity.minecraft.cow");
                ItemStack full = new ItemStack(ToolsItems.POKEBALL.get());
                full.set(ToolsDataComponents.CAPTURED_ENTITY.get(), new CapturedEntity(cow));
                List<String> stored = tooltipKeys(client, full);
                if (!stored.contains("tooltip.pokeball.stored")) {
                    throw new AssertionError("a full pokeball's tooltip is " + stored);
                }
            });
        }
    }

    private static List<String> tooltipKeys(Minecraft client, ItemStack stack) {
        return stack.getTooltipLines(Item.TooltipContext.of(client.level), client.player, TooltipFlag.NORMAL).stream()
                .map(line -> line.getContents() instanceof TranslatableContents translatable ? translatable.getKey() : line.getString())
                .toList();
    }
}
