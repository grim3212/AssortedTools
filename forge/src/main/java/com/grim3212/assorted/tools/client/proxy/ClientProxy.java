package com.grim3212.assorted.tools.client.proxy;

import com.grim3212.assorted.decor.Constants;
import com.grim3212.assorted.decor.client.ToolsClient;
import com.grim3212.assorted.decor.client.handlers.ChickenJumpHandler;
import com.grim3212.assorted.decor.client.handlers.KeyBindHandler;
import com.grim3212.assorted.decor.client.render.model.SpearModel;
import com.grim3212.assorted.decor.client.render.model.ToolsModelLayers;
import com.grim3212.assorted.decor.common.item.ToolsItems;
import com.grim3212.assorted.tools.common.proxy.IProxy;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.model.DynamicFluidContainerModel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientProxy implements IProxy {

    @Override
    public void starting() {
        ToolsClient.registerKeys();

        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::setupClient);
        modBus.addListener(this::registerLayers);
        modBus.addListener(this::registerItemColorHandles);

        MinecraftForge.EVENT_BUS.register(new KeyBindHandler());
        MinecraftForge.EVENT_BUS.register(new ChickenJumpHandler());
    }

    private void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ToolsModelLayers.SPEAR, SpearModel::createLayer);
    }

    private void setupClient(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemPropertyFunction override = (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
            ItemProperties.register(ToolsItems.WOOD_SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);
            ItemProperties.register(ToolsItems.STONE_SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);
            ItemProperties.register(ToolsItems.IRON_SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);
            ItemProperties.register(ToolsItems.GOLD_SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);
            ItemProperties.register(ToolsItems.DIAMOND_SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);
            ItemProperties.register(ToolsItems.NETHERITE_SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);

            ToolsItems.MATERIAL_GROUPS.forEach((s, group) -> {
                ItemProperties.register(group.SPEAR.get(), new ResourceLocation(Constants.MOD_ID, "throwing"), override);
            });
        });
    }

    public void registerItemColorHandles(final RegisterColorHandlersEvent.Item event) {
        event.register(new DynamicFluidContainerModel.Colors(), ToolsItems.buckets());
    }
}
