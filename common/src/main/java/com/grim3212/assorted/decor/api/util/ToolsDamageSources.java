package com.grim3212.assorted.decor.api.util;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public class ToolsDamageSources {

    public static DamageSource spear(Entity hitEntity, @Nullable Entity owner) {
        return (new IndirectEntityDamageSource("spear", hitEntity, owner)).setProjectile();
    }
}
