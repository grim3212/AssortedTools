package com.grim3212.assorted.tools;

import com.google.common.collect.Lists;
import com.grim3212.assorted.decor.Constants;
import com.grim3212.assorted.decor.ToolsCommonMod;
import com.grim3212.assorted.decor.common.handlers.ToolsCreativeItems;
import com.grim3212.assorted.decor.common.item.ToolsItems;
import com.grim3212.assorted.decor.data.ToolsCommonRecipeProvider;
import com.grim3212.assorted.tools.client.data.ToolsItemModelProvider;
import com.grim3212.assorted.tools.client.proxy.ClientProxy;
import com.grim3212.assorted.tools.common.data.*;
import com.grim3212.assorted.tools.common.loot.ToolsLootConditions;
import com.grim3212.assorted.tools.common.loot.ToolsLootModifiers;
import com.grim3212.assorted.tools.common.proxy.IProxy;
import com.grim3212.assorted.tools.common.util.TierRegistryHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.CreativeModeTabEvent;
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
public class AssortedTools {

    public static IProxy proxy = new IProxy() {
    };

    public AssortedTools() {
        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> proxy = new ClientProxy());
        proxy.starting();

        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(this::setup);
        modBus.addListener(this::gatherData);
        modBus.addListener(this::sendIMC);
        modBus.addListener(this::registerTabs);

        ToolsCommonMod.init();

        ToolsLootModifiers.LOOT_MODIFIERS.register(modBus);
        ToolsLootConditions.LOOT_ITEM_CONDITIONS.register(modBus);
    }

    private void setup(final FMLCommonSetupEvent event) {
        TierRegistryHandler.registerTiers();
    }

    private void sendIMC(final InterModEnqueueEvent event) {
        event.enqueueWork(() -> {
            InterModComms.sendTo("assorteddecor", "addCageItem", () -> {
                return Lists.newArrayList(new Tuple<ResourceLocation, String>(ToolsItems.POKEBALL.getId(), "StoredEntity"));
            });
        });
    }

    private void registerTabs(final CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(Constants.MOD_ID, "tab"), builder -> builder.title(Component.translatable("itemGroup.assortedtools")).icon(() -> new ItemStack(ToolsItems.IRON_HAMMER.get())).displayItems((enabledFlags, populator, hasPermissions) -> {
            populator.acceptAll(ToolsCreativeItems.getCreativeItems());
        }));
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
        datagenerator.addProvider(event.includeServer(), new ToolsLootModifierProvider(packOutput));
        datagenerator.addProvider(event.includeServer(), new ToolsLootTableProvider(packOutput, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(ToolsChestLoot::new, LootContextParamSets.CHEST))));

        datagenerator.addProvider(event.includeClient(), new ToolsItemModelProvider(packOutput, fileHelper));
    }
}
