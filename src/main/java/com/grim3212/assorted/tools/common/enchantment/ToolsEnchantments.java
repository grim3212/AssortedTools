package com.grim3212.assorted.tools.common.enchantment;

import java.util.function.Supplier;

import com.grim3212.assorted.tools.AssortedTools;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ToolsEnchantments {

	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, AssortedTools.MODID);

	public static final RegistryObject<ChickenJumpEnchantment> CHICKEN_JUMP = register("chicken_jump", () -> new ChickenJumpEnchantment());

	private static <T extends Enchantment> RegistryObject<T> register(final String name, final Supplier<T> sup) {
		return ENCHANTMENTS.register(name, sup);
	}
}
