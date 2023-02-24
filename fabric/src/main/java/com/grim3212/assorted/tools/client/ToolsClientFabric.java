package com.grim3212.assorted.decor.client;

import com.grim3212.assorted.decor.client.model.ColorizerModelLoaderFabric;
import com.grim3212.assorted.decor.client.model.ColorizerModelVariantLoader;
import com.grim3212.assorted.decor.client.screen.CageScreen;
import com.grim3212.assorted.decor.common.inventory.DecorContainerTypes;
import com.grim3212.assorted.lib.mixin.client.AccessorMinecraft;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.Minecraft;

public class DecorClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(DecorContainerTypes.CAGE.get(), CageScreen::new);

        DecorClient.registerBlockEntityRenderers(BlockEntityRendererRegistry::register);
        DecorClient.registerEntityRenderers(EntityRendererRegistry::register);

        DecorClient.setRenderTypes(BlockRenderLayerMap.INSTANCE::putBlock);

        ClientLifecycleEvents.CLIENT_STARTED.register(this::loadComplete);

        ModelLoadingRegistry.INSTANCE.registerResourceProvider(manager -> ColorizerModelLoaderFabric.INSTANCE);
        ModelLoadingRegistry.INSTANCE.registerVariantProvider(manager -> ColorizerModelVariantLoader.INSTANCE);
    }

    private void loadComplete(Minecraft mc) {
        DecorClient.registerBlockColors(mc.getBlockColors(), ColorProviderRegistry.BLOCK::register);
        DecorClient.registerItemColors(((AccessorMinecraft) mc).assortedlib_getItemColors(), ColorProviderRegistry.ITEM::register);
    }
}
