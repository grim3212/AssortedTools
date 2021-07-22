package com.grim3212.assorted.tools.common.util;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;

public class ToolsDamageSources {

	public static DamageSource spear(Entity hitEntity, @Nullable Entity owner) {
	      return (new IndirectEntityDamageSource("spear", hitEntity, owner)).setProjectile();
	   }
}
