package com.grim3212.assorted.tools.client.render.item;

import java.util.concurrent.Callable;

import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;

public class ItemRendererProvider {

	public static Callable<ItemStackTileEntityRenderer> spear() {
		return SpearISTER::new;
	}

}
