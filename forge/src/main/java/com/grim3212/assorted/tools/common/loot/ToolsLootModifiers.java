package com.grim3212.assorted.tools.common.loot;

import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.common.loot.UltimateFragmentLootModifier.EndUltimateFragmentLootModifier;
import com.grim3212.assorted.tools.common.loot.UltimateFragmentLootModifier.NetherUltimateFragmentLootModifier;
import com.grim3212.assorted.tools.common.loot.UltimateFragmentLootModifier.OverworldUltimateFragmentLootModifier;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ToolsLootModifiers {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Constants.MOD_ID);

    public static final RegistryObject<Codec<CoralCutterLootModifier>> CORAL_CUTTER = register("coral_cutter", CoralCutterLootModifier.CODEC);
    public static final RegistryObject<Codec<OverworldUltimateFragmentLootModifier>> OVERWORLD_ULTIMATE_FRAGMENTS = register("overworld_ultimate_fragments", OverworldUltimateFragmentLootModifier.CODEC);
    public static final RegistryObject<Codec<NetherUltimateFragmentLootModifier>> NETHER_ULTIMATE_FRAGMENTS = register("nether_ultimate_fragments", NetherUltimateFragmentLootModifier.CODEC);
    public static final RegistryObject<Codec<EndUltimateFragmentLootModifier>> END_ULTIMATE_FRAGMENTS = register("end_ultimate_fragments", EndUltimateFragmentLootModifier.CODEC);

    private static <T extends Codec<? extends IGlobalLootModifier>> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return LOOT_MODIFIERS.register(name, sup);
    }
}
