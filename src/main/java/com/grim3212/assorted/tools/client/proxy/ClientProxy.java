package com.grim3212.assorted.tools.client.proxy;

import org.lwjgl.glfw.GLFW;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.client.handler.ChickenJumpHandler;
import com.grim3212.assorted.tools.client.handler.KeyBindHandler;
import com.grim3212.assorted.tools.client.render.entity.BoomerangRenderer;
import com.grim3212.assorted.tools.client.render.entity.SpearRenderer;
import com.grim3212.assorted.tools.common.entity.PokeballEntity;
import com.grim3212.assorted.tools.common.entity.ToolsEntities;
import com.grim3212.assorted.tools.common.proxy.IProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientProxy implements IProxy {

	public static KeyBinding TOOL_SWITCH_MODES;

	@Override
	public void starting() {
		final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		modBus.addListener(this::setupClient);

		MinecraftForge.EVENT_BUS.register(new KeyBindHandler());
		MinecraftForge.EVENT_BUS.register(new ChickenJumpHandler());
	}

	private void setupClient(final FMLClientSetupEvent event) {

		RenderingRegistry.registerEntityRenderingHandler(ToolsEntities.WOOD_BOOMERANG.get(), new BoomerangRenderer.Factory());
		RenderingRegistry.registerEntityRenderingHandler(ToolsEntities.DIAMOND_BOOMERANG.get(), new BoomerangRenderer.Factory());
		RenderingRegistry.registerEntityRenderingHandler(ToolsEntities.SPEAR.get(), new SpearRenderer.Factory());
		RenderingRegistry.registerEntityRenderingHandler(ToolsEntities.POKEBALL.get(), new IRenderFactory<PokeballEntity>() {
			@Override
			public EntityRenderer<PokeballEntity> createRenderFor(EntityRendererManager manager) {
				return new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer());
			}
		});

		event.enqueueWork(() -> {
			TOOL_SWITCH_MODES = new KeyBinding("key.assortedtools.tool_switch_modes", KeyConflictContext.IN_GAME, InputMappings.getKey(GLFW.GLFW_KEY_Z, 0), AssortedTools.MODNAME);
			ClientRegistry.registerKeyBinding(TOOL_SWITCH_MODES);
		});
	}
}
