package com.grim3212.assorted.tools;

import com.grim3212.assorted.tools.client.data.ToolsLanguageProvider;
import com.grim3212.assorted.lib.data.ForgeBlockTagProvider;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import com.grim3212.assorted.lib.data.ForgeDatapackRegistryProvider;
import com.grim3212.assorted.tools.client.data.ToolsEquipmentAssetProvider;
import com.grim3212.assorted.tools.common.item.BetterBucketItem;
import com.grim3212.assorted.tools.common.item.NeoForgeBetterBucketFluidHandler;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.grim3212.assorted.tools.client.data.ToolsItemModelProvider;
import com.grim3212.assorted.tools.data.ToolsBlockTagProvider;
import com.grim3212.assorted.tools.data.ToolsChestLoot;
import com.grim3212.assorted.tools.data.ToolsEnchantmentData;
import com.grim3212.assorted.tools.data.ToolsEnchantmentTagProvider;
import com.grim3212.assorted.tools.data.ToolsItemTagProvider;
import com.grim3212.assorted.tools.data.ToolsRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedToolsNeoForge {

    /** The mod event bus and container are injected into the {@code @Mod} constructor. */
    public AssortedToolsNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherServerData);
        modBus.addListener(this::gatherClientData);
        modBus.addListener(this::registerCapabilities);

        ToolsCommonMod.init();
    }

    /**
     * Server datagen. The server and client halves are separate events; if the wrong one runs, the
     * build still succeeds, with "All providers took: 0 ms".
     */
    private void gatherServerData(final GatherDataEvent.Server event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Recipe providers are not data providers any more - the Runner owns the output.
        event.addProvider(new ToolsRecipes.Runner(packOutput, lookupProvider));
        ForgeBlockTagProvider blockTagProvider = event.addProvider(new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, new ToolsBlockTagProvider(packOutput, lookupProvider)));
        event.addProvider(new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, new ToolsItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));
        event.addProvider(new LootTableProvider(packOutput, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(registries -> new ToolsChestLoot(), LootContextParamSets.CHEST)), lookupProvider));
        // Enchantments are datapack registry content now, not registered from code.
        event.addProvider(new ForgeDatapackRegistryProvider(Constants.MOD_ID, new ToolsEnchantmentData()).datpackEntriesProvider(packOutput, lookupProvider));
        event.addProvider(new ToolsEnchantmentTagProvider(packOutput, lookupProvider));
    }

    /** Registers each better bucket's {@code ResourceHandler<FluidResource>} item capability. */
    private void registerCapabilities(final RegisterCapabilitiesEvent event) {
        for (Item item : ToolsItems.buckets()) {
            if (item instanceof BetterBucketItem bucket) {
                event.registerItem(Capabilities.Fluid.ITEM, (stack, access) -> new NeoForgeBetterBucketFluidHandler(access, bucket, bucket.getBreakStack(), bucket.getEmptyStack(), bucket.getMaximumMillibuckets()), item);
            }
        }
    }

    private void gatherClientData(final GatherDataEvent.Client event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.addProvider(new ToolsItemModelProvider(packOutput));
        event.addProvider(new ToolsEquipmentAssetProvider(packOutput));
        event.addProvider(new ToolsLanguageProvider(packOutput));
    }
}
