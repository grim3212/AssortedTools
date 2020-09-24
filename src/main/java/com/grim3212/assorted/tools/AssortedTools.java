package com.grim3212.assorted.tools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.grim3212.assorted.tools.client.data.ToolsItemModelProvider;
import com.grim3212.assorted.tools.client.proxy.ClientProxy;
import com.grim3212.assorted.tools.common.data.ToolsRecipes;
import com.grim3212.assorted.tools.common.entity.ToolsEntities;
import com.grim3212.assorted.tools.common.handler.TagLoadListener;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.grim3212.assorted.tools.common.network.PacketHandler;
import com.grim3212.assorted.tools.common.proxy.IProxy;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AssortedTools.MODID)
public class AssortedTools {
	public static final String MODID = "assortedtools";
	public static final String MODNAME = "Assorted Tools";

	public static IProxy proxy = new IProxy() {
	};

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static final ItemGroup ASSORTED_TOOLS_ITEM_GROUP = (new ItemGroup("assortedtools") {
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack createIcon() {
			return new ItemStack(ToolsItems.IRON_HAMMER.get());
		}
	});

	public AssortedTools() {
		DistExecutor.callWhenOn(Dist.CLIENT, () -> () -> proxy = new ClientProxy());
		proxy.starting();

		final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		modBus.addListener(this::setup);
		modBus.addListener(this::gatherData);

		MinecraftForge.EVENT_BUS.register(new TagLoadListener());

		ToolsItems.ITEMS.register(modBus);
		ToolsEntities.ENTITIES.register(modBus);

		ModLoadingContext.get().registerConfig(Type.COMMON, ToolsConfig.COMMON_SPEC);
	}

	private void setup(final FMLCommonSetupEvent event) {
		PacketHandler.init();
	}

	private void gatherData(GatherDataEvent event) {
		DataGenerator datagenerator = event.getGenerator();

		if (event.includeServer()) {
			datagenerator.addProvider(new ToolsRecipes(datagenerator));
		}

		if (event.includeClient()) {
			datagenerator.addProvider(new ToolsItemModelProvider(datagenerator, event.getExistingFileHelper()));
		}
	}
}
