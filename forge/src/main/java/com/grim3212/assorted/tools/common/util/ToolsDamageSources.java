package com.grim3212.assorted.tools.common.util;

import javax.annotation.Nullable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;

public class ToolsDamageSources {

	public static DamageSource spear(Entity hitEntity, @Nullable Entity owner) {
		return (new IndirectEntityDamageSource("spear", hitEntity, owner)).setProjectile();
	}
}
