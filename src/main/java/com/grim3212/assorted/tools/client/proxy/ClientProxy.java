package com.grim3212.assorted.tools.client.proxy;

import org.lwjgl.glfw.GLFW;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.client.handler.ChickenJumpHandler;
import com.grim3212.assorted.tools.client.handler.KeyBindHandler;
import com.grim3212.assorted.tools.client.render.entity.BetterSpearRenderer;
import com.grim3212.assorted.tools.client.render.entity.BoomerangRenderer;
import com.grim3212.assorted.tools.client.render.model.SpearModel;
import com.grim3212.assorted.tools.client.render.model.ToolsModelLayers;
import com.grim3212.assorted.tools.common.entity.ToolsEntities;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.grim3212.assorted.tools.common.proxy.IProxy;
import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmlclient.registry.ClientRegistry;

public class ClientProxy implements IProxy {

	public static KeyMapping TOOL_SWITCH_MODES;

	@Override
	public void starting() {
		final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		modBus.addListener(this::setupClient);
		modBus.addListener(this::registerLayers);
		modBus.addListener(this::registerRenderers);

		MinecraftForge.EVENT_BUS.register(new KeyBindHandler());
		MinecraftForge.EVENT_BUS.register(new ChickenJumpHandler());
	}

	private void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ToolsEntities.WOOD_BOOMERANG.get(), BoomerangRenderer::new);
		event.registerEntityRenderer(ToolsEntities.DIAMOND_BOOMERANG.get(), BoomerangRenderer::new);
		event.registerEntityRenderer(ToolsEntities.BETTER_SPEAR.get(), BetterSpearRenderer::new);
		event.registerEntityRenderer(ToolsEntities.POKEBALL.get(), ThrownItemRenderer::new);
	}

	private void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ToolsModelLayers.SPEAR, SpearModel::createLayer);
	}

	private void setupClient(final FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			TOOL_SWITCH_MODES = new KeyMapping("key.assortedtools.tool_switch_modes", KeyConflictContext.IN_GAME, InputConstants.getKey(GLFW.GLFW_KEY_Z, 0), AssortedTools.MODNAME);
			ClientRegistry.registerKeyBinding(TOOL_SWITCH_MODES);
			ItemPropertyFunction override = (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;

			ItemProperties.register(ToolsItems.WOOD_SPEAR.get(), new ResourceLocation(AssortedTools.MODID, "throwing"), override);
			ItemProperties.register(ToolsItems.STONE_SPEAR.get(), new ResourceLocation(AssortedTools.MODID, "throwing"), override);
			ItemProperties.register(ToolsItems.IRON_SPEAR.get(), new ResourceLocation(AssortedTools.MODID, "throwing"), override);
			ItemProperties.register(ToolsItems.GOLD_SPEAR.get(), new ResourceLocation(AssortedTools.MODID, "throwing"), override);
			ItemProperties.register(ToolsItems.DIAMOND_SPEAR.get(), new ResourceLocation(AssortedTools.MODID, "throwing"), override);
			ItemProperties.register(ToolsItems.NETHERITE_SPEAR.get(), new ResourceLocation(AssortedTools.MODID, "throwing"), override);

			ToolsItems.MATERIAL_GROUPS.forEach((s, group) -> {
				ItemProperties.register(group.SPEAR.get(), new ResourceLocation(AssortedTools.MODID, "throwing"), override);
			});
		});
	}
}
