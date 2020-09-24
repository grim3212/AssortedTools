package com.grim3212.assorted.tools.common.entity;

import com.grim3212.assorted.tools.common.handler.ToolsConfig;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.world.World;

public class WoodBoomerangEntity extends BoomerangEntity {

	public WoodBoomerangEntity(EntityType<BoomerangEntity> type, World world) {
		super(type, world);
		this.timeBeforeTurnAround = ToolsConfig.COMMON.woodBoomerangRange.get() <= 0 ? 20 : ToolsConfig.COMMON.woodBoomerangRange.get();
	}

	public WoodBoomerangEntity(World worldIn, PlayerEntity entity, ItemStack itemstack, Hand hand) {
		super(ToolsEntities.WOOD_BOOMERANG.get(), worldIn, entity, itemstack, hand);
		this.timeBeforeTurnAround = ToolsConfig.COMMON.woodBoomerangRange.get() <= 0 ? 20 : ToolsConfig.COMMON.woodBoomerangRange.get();
	}

	@Override
	protected int getDamage(Entity hitEntity, PlayerEntity player) {
		if (ToolsConfig.COMMON.woodBoomerangDamage.get() > 0) {
			return ToolsConfig.COMMON.woodBoomerangDamage.get();
		}

		return 0;
	}

	@Override
	public DamageSource causeNewDamage(BoomerangEntity entityboomerang, Entity entity) {
		return (new IndirectEntityDamageSource("wood_boomerang", entityboomerang, entity)).setProjectile();
	}

}
