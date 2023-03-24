package com.grim3212.assorted.tools;

import com.google.common.collect.Lists;
import com.grim3212.assorted.tools.client.ToolsClient;
import com.grim3212.assorted.tools.client.data.ToolsItemModelProvider;
import com.grim3212.assorted.tools.common.data.ForgeBlockTagProvider;
import com.grim3212.assorted.tools.common.data.ForgeItemTagProvider;
import com.grim3212.assorted.tools.common.item.BetterBucketItem;
import com.grim3212.assorted.tools.common.item.ForgeBetterBucketFluidHandler;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.grim3212.assorted.tools.common.util.TierRegistryHandler;
import com.grim3212.assorted.tools.data.ToolsChestLoot;
import com.grim3212.assorted.tools.data.ToolsCommonRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedToolsForge {

    public AssortedToolsForge() {
        // Initialize client side
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ToolsClient::init);

        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(this::setup);
        modBus.addListener(this::gatherData);
        modBus.addListener(this::sendIMC);
        forgeBus.addGenericListener(ItemStack.class, this::attachBlockCapabilities);

        ToolsCommonMod.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            TierRegistryHandler.registerTiers();
        });
    }

    private void attachBlockCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() instanceof BetterBucketItem bucketItem) {
            event.addCapability(new ResourceLocation(Constants.MOD_ID, "fluid_handler"), new ForgeBetterBucketFluidHandler(event.getObject(), bucketItem.getBreakStack(), bucketItem.getEmptyStack(), bucketItem.getMaximumMillibuckets()));
        }
    }

    private void sendIMC(final InterModEnqueueEvent event) {
        // TODO: Add same IMC functionality for the Fabric side
        event.enqueueWork(() -> {
            InterModComms.sendTo("assorteddecor", "addCageItem", () -> {
                return Lists.newArrayList(new Tuple<ResourceLocation, String>(ToolsItems.POKEBALL.getId(), "StoredEntity"));
            });
        });
    }

    private void gatherData(GatherDataEvent event) {
        DataGenerator datagenerator = event.getGenerator();
        PackOutput packOutput = datagenerator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        ForgeBlockTagProvider blockTagProvider = new ForgeBlockTagProvider(packOutput, lookupProvider, fileHelper);
        datagenerator.addProvider(event.includeServer(), blockTagProvider);
        datagenerator.addProvider(event.includeServer(), new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider, fileHelper));
        datagenerator.addProvider(event.includeServer(), new ToolsCommonRecipeProvider(packOutput));
        datagenerator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(ToolsChestLoot::new, LootContextParamSets.CHEST))));

        datagenerator.addProvider(event.includeClient(), new ToolsItemModelProvider(packOutput, fileHelper));
    }
}
