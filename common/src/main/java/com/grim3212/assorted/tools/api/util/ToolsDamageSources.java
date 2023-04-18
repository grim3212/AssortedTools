package com.grim3212.assorted.tools.api.util;

import com.grim3212.assorted.tools.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class ToolsDamageSources {

    public static final ResourceKey<DamageType> SPEAR = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Constants.MOD_ID, "spear"));
    public static final ResourceKey<DamageType> BOOMERANG = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Constants.MOD_ID, "boomerang"));
}
