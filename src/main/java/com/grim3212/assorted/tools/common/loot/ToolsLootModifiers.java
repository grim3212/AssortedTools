package com.grim3212.assorted.tools.common.loot;

import java.util.function.Supplier;

import com.grim3212.assorted.tools.AssortedTools;

import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ToolsLootModifiers {

	public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, AssortedTools.MODID);

	public static final RegistryObject<CoralCutterLootModifier.Serializer> CORAL_CUTTER = register("coral_cutter", CoralCutterLootModifier.Serializer::new);

	private static <T extends GlobalLootModifierSerializer<?>> RegistryObject<T> register(final String name, final Supplier<T> sup) {
		return LOOT_MODIFIERS.register(name, sup);
	}
}
