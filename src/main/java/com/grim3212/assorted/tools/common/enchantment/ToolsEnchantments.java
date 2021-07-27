package com.grim3212.assorted.tools.common.enchantment;

import java.util.function.Supplier;

import com.grim3212.assorted.tools.AssortedTools;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ToolsEnchantments {

	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, AssortedTools.MODID);

	public static final RegistryObject<ChickenJumpEnchantment> CHICKEN_JUMP = register("chicken_jump", () -> new ChickenJumpEnchantment());
	public static final RegistryObject<BouncinessEnchantment> BOUNCINESS = register("bounciness", () -> new BouncinessEnchantment());
	public static final RegistryObject<ConductiveEnchantment> CONDUCTIVE = register("conductive", () -> new ConductiveEnchantment());
	public static final RegistryObject<FlammableEnchantment> FLAMMABLE = register("flammable", () -> new FlammableEnchantment());
	public static final RegistryObject<UnstableEnchantment> UNSTABLE = register("unstable", () -> new UnstableEnchantment());

	private static <T extends Enchantment> RegistryObject<T> register(final String name, final Supplier<T> sup) {
		return ENCHANTMENTS.register(name, sup);
	}

	public static int getConductivity(ItemStack stack) {
		return EnchantmentHelper.getItemEnchantmentLevel(CONDUCTIVE.get(), stack);
	}

	public static boolean hasFlammable(ItemStack stack) {
		return EnchantmentHelper.getItemEnchantmentLevel(FLAMMABLE.get(), stack) > 0;
	}

	public static int getInstability(ItemStack stack) {
		return EnchantmentHelper.getItemEnchantmentLevel(UNSTABLE.get(), stack);
	}

	public static int getMaxBounces(ItemStack stack) {
		return EnchantmentHelper.getItemEnchantmentLevel(BOUNCINESS.get(), stack);
	}
}
