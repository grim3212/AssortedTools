package com.grim3212.assorted.tools;

import com.grim3212.assorted.lib.data.ForgeBlockTagProvider;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import com.grim3212.assorted.tools.client.data.ToolsItemModelProvider;
import com.grim3212.assorted.tools.common.util.TierRegistryHandler;
import com.grim3212.assorted.tools.data.ToolsBlockTagProvider;
import com.grim3212.assorted.tools.data.ToolsChestLoot;
import com.grim3212.assorted.tools.data.ToolsItemTagProvider;
import com.grim3212.assorted.tools.data.ToolsRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedToolsForge {

    public AssortedToolsForge() {
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(this::setup);
        modBus.addListener(this::gatherData);

        ToolsCommonMod.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            TierRegistryHandler.registerTiers();
        });
    }

    private void gatherData(GatherDataEvent event) {
        DataGenerator datagenerator = event.getGenerator();
        PackOutput packOutput = datagenerator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        ForgeBlockTagProvider blockTagProvider = new ForgeBlockTagProvider(packOutput, lookupProvider, fileHelper, Constants.MOD_ID, new ToolsBlockTagProvider(packOutput, lookupProvider));
        datagenerator.addProvider(event.includeServer(), blockTagProvider);
        datagenerator.addProvider(event.includeServer(), new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider, fileHelper, Constants.MOD_ID, new ToolsItemTagProvider(packOutput, lookupProvider, blockTagProvider)));
        datagenerator.addProvider(event.includeServer(), new ToolsRecipes(packOutput));
        datagenerator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(ToolsChestLoot::new, LootContextParamSets.CHEST))));

        datagenerator.addProvider(event.includeClient(), new ToolsItemModelProvider(packOutput, fileHelper));
    }
}
