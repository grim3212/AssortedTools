package com.grim3212.assorted.decor.common.enchantment;

import com.grim3212.assorted.decor.Constants;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.function.Supplier;

public class ToolsEnchantments {

    public static final RegistryProvider<Enchantment> ENCHANTMENTS = RegistryProvider.create(Registries.ENCHANTMENT, Constants.MOD_ID);

    public static final IRegistryObject<ChickenJumpEnchantment> CHICKEN_JUMP = register("chicken_jump", () -> new ChickenJumpEnchantment());
    public static final IRegistryObject<BouncinessEnchantment> BOUNCINESS = register("bounciness", () -> new BouncinessEnchantment());
    public static final IRegistryObject<ConductiveEnchantment> CONDUCTIVE = register("conductive", () -> new ConductiveEnchantment());
    public static final IRegistryObject<FlammableEnchantment> FLAMMABLE = register("flammable", () -> new FlammableEnchantment());
    public static final IRegistryObject<UnstableEnchantment> UNSTABLE = register("unstable", () -> new UnstableEnchantment());
    public static final IRegistryObject<CoralCutterEnchantment> CORAL_CUTTER = register("coral_cutter", () -> new CoralCutterEnchantment());

    private static <T extends Enchantment> IRegistryObject<T> register(final String name, final Supplier<T> sup) {
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

    public static boolean hasCoralCutter(ItemStack stack) {
        return EnchantmentHelper.getItemEnchantmentLevel(CORAL_CUTTER.get(), stack) > 0;
    }

    public static void init() {
    }
}
