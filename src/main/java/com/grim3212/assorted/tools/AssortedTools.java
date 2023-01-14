package com.grim3212.assorted.tools;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.grim3212.assorted.tools.client.data.ToolsItemModelProvider;
import com.grim3212.assorted.tools.client.proxy.ClientProxy;
import com.grim3212.assorted.tools.common.creative.ToolsCreativeTab;
import com.grim3212.assorted.tools.common.data.ToolsBlockTagProvider;
import com.grim3212.assorted.tools.common.data.ToolsChestLoot;
import com.grim3212.assorted.tools.common.data.ToolsItemTagProvider;
import com.grim3212.assorted.tools.common.data.ToolsLootModifierProvider;
import com.grim3212.assorted.tools.common.data.ToolsLootTableProvider;
import com.grim3212.assorted.tools.common.data.ToolsRecipes;
import com.grim3212.assorted.tools.common.enchantment.ToolsEnchantments;
import com.grim3212.assorted.tools.common.entity.ToolsEntities;
import com.grim3212.assorted.tools.common.handler.ChickenSuitConversionHandler;
import com.grim3212.assorted.tools.common.handler.EnabledCondition;
import com.grim3212.assorted.tools.common.handler.MilkingHandler;
import com.grim3212.assorted.tools.common.handler.TagLoadListener;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.grim3212.assorted.tools.common.loot.ToolsLootConditions;
import com.grim3212.assorted.tools.common.loot.ToolsLootModifiers;
import com.grim3212.assorted.tools.common.network.PacketHandler;
import com.grim3212.assorted.tools.common.proxy.IProxy;
import com.grim3212.assorted.tools.common.util.TierRegistryHandler;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AssortedTools.MODID)
public class AssortedTools {
	public static final String MODID = "assortedtools";
	public static final String MODNAME = "Assorted Tools";

	public static IProxy proxy = new IProxy() {
	};

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public AssortedTools() {
		DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> proxy = new ClientProxy());
		proxy.starting();

		final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		modBus.addListener(this::setup);
		modBus.addListener(this::gatherData);
		modBus.addListener(this::sendIMC);
		modBus.addListener(ToolsCreativeTab::registerTabs);

		MinecraftForge.EVENT_BUS.register(new TagLoadListener());
		MinecraftForge.EVENT_BUS.register(new ChickenSuitConversionHandler());
		MinecraftForge.EVENT_BUS.register(new MilkingHandler());

		ToolsItems.ITEMS.register(modBus);
		ToolsEntities.ENTITIES.register(modBus);
		ToolsEnchantments.ENCHANTMENTS.register(modBus);
		ToolsLootModifiers.LOOT_MODIFIERS.register(modBus);
		ToolsLootConditions.LOOT_ITEM_CONDITIONS.register(modBus);

		ModLoadingContext.get().registerConfig(Type.COMMON, ToolsConfig.COMMON_SPEC);

		CraftingHelper.register(EnabledCondition.Serializer.INSTANCE);
	}

	private void setup(final FMLCommonSetupEvent event) {
		PacketHandler.init();
		TierRegistryHandler.registerTiers();
	}

	private void sendIMC(final InterModEnqueueEvent event) {
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

		ToolsBlockTagProvider blockTagProvider = new ToolsBlockTagProvider(packOutput, lookupProvider, fileHelper);
		datagenerator.addProvider(event.includeServer(), blockTagProvider);
		datagenerator.addProvider(event.includeServer(), new ToolsItemTagProvider(packOutput, lookupProvider, blockTagProvider, fileHelper));
		datagenerator.addProvider(event.includeServer(), new ToolsRecipes(packOutput));
		datagenerator.addProvider(event.includeServer(), new ToolsLootModifierProvider(packOutput));
		datagenerator.addProvider(event.includeServer(), new ToolsLootTableProvider(packOutput, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(ToolsChestLoot::new, LootContextParamSets.CHEST))));

		datagenerator.addProvider(event.includeClient(), new ToolsItemModelProvider(packOutput, fileHelper));
	}
}
