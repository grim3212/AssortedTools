package com.grim3212.assorted.decor.common.entity;

import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DiamondBoomerangEntity extends BoomerangEntity {

    public DiamondBoomerangEntity(EntityType<BoomerangEntity> type, Level world) {
        super(type, world);
        this.timeBeforeTurnAround = ToolsConfig.COMMON.diamondBoomerangRange.get() <= 0 ? 20 : ToolsConfig.COMMON.diamondBoomerangRange.get();
    }

    public DiamondBoomerangEntity(Level worldIn, Player entity, ItemStack itemstack, InteractionHand hand) {
        super(ToolsEntities.DIAMOND_BOOMERANG.get(), worldIn, entity, itemstack, hand);
        this.timeBeforeTurnAround = ToolsConfig.COMMON.diamondBoomerangRange.get() <= 0 ? 20 : ToolsConfig.COMMON.diamondBoomerangRange.get();
    }

    @Override
    protected int getDamage(Entity hitEntity, Player player) {
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
    public void beforeTurnAround(Player player) {
        // Following is diamond boomerang only
        // Follows where the entity is looking
        if (!isBouncing && ToolsConfig.COMMON.diamondBoomerangFollows.get()) {
            double x = -Mth.sin((player.getYRot() * 3.141593F) / 180F);
            double z = Mth.cos((player.getYRot() * 3.141593F) / 180F);

            double motionX = 0.5D * x * (double) Mth.cos((player.getXRot() / 180F) * 3.141593F);
            double motionY = -0.5D * (double) Mth.sin((player.getXRot() / 180F) * 3.141593F);
            double motionZ = 0.5D * z * (double) Mth.cos((player.getXRot() / 180F) * 3.141593F);
            this.setDeltaMovement(motionX, motionY, motionZ);
        }
    }
}
