package com.grim3212.assorted.tools.common.entity;

import com.grim3212.assorted.tools.common.handler.ToolsConfig;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class DiamondBoomerangEntity extends BoomerangEntity {

	public DiamondBoomerangEntity(EntityType<BoomerangEntity> type, World world) {
		super(type, world);
		this.timeBeforeTurnAround = ToolsConfig.COMMON.diamondBoomerangRange.get() <= 0 ? 20 : ToolsConfig.COMMON.diamondBoomerangRange.get();
	}

	public DiamondBoomerangEntity(World worldIn, PlayerEntity entity, ItemStack itemstack, Hand hand) {
		super(ToolsEntities.DIAMOND_BOOMERANG.get(), worldIn, entity, itemstack, hand);
		this.timeBeforeTurnAround = ToolsConfig.COMMON.diamondBoomerangRange.get() <= 0 ? 20 : ToolsConfig.COMMON.diamondBoomerangRange.get();
	}

	@Override
	protected int getDamage(Entity hitEntity, PlayerEntity player) {
		if (ToolsConfig.COMMON.diamondBoomerangDamage.get() > 0) {
			return ToolsConfig.COMMON.diamondBoomerangDamage.get();
		}

		return 0;
	}

	@Override
	public DamageSource causeNewDamage(BoomerangEntity entityboomerang, Entity entity) {
		return (new IndirectEntityDamageSource("diamond_boomerang", entityboomerang, entity)).setProjectile();
	}

	@Override
	public void beforeTurnAround(PlayerEntity player) {
		// Following is diamond boomerang only
		// Follows where the entity is looking
		if (!isBouncing && ToolsConfig.COMMON.diamondBoomerangFollows.get()) {
			double x = -MathHelper.sin((player.rotationYaw * 3.141593F) / 180F);
			double z = MathHelper.cos((player.rotationYaw * 3.141593F) / 180F);

			double motionX = 0.5D * x * (double) MathHelper.cos((player.rotationPitch / 180F) * 3.141593F);
			double motionY = -0.5D * (double) MathHelper.sin((player.rotationPitch / 180F) * 3.141593F);
			double motionZ = 0.5D * z * (double) MathHelper.cos((player.rotationPitch / 180F) * 3.141593F);
			this.setMotion(motionX, motionY, motionZ);
		}
	}
}
