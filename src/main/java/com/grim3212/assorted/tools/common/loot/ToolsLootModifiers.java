package com.grim3212.assorted.tools.common.loot;

import java.util.function.Supplier;

import com.grim3212.assorted.tools.AssortedTools;
import com.mojang.serialization.Codec;

import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ToolsLootModifiers {

	public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, AssortedTools.MODID);

	public static final RegistryObject<Codec<CoralCutterLootModifier>> CORAL_CUTTER = register("coral_cutter", CoralCutterLootModifier.CODEC);

	private static <T extends Codec<? extends IGlobalLootModifier>> RegistryObject<T> register(final String name, final Supplier<T> sup) {
		return LOOT_MODIFIERS.register(name, sup);
	}
}
