package com.grim3212.assorted.tools.common.entity;

import com.grim3212.assorted.tools.config.ToolsConfig;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WoodBoomerangEntity extends BoomerangEntity {

    public WoodBoomerangEntity(EntityType<BoomerangEntity> type, Level world) {
        super(type, world);
        this.timeBeforeTurnAround = ToolsConfig.Common.woodBoomerangRange.getValue() <= 0 ? 20 : ToolsConfig.Common.woodBoomerangRange.getValue();
    }

    public WoodBoomerangEntity(Level worldIn, Player entity, ItemStack itemstack, InteractionHand hand) {
        super(ToolsEntities.WOOD_BOOMERANG.get(), worldIn, entity, itemstack, hand);
        this.timeBeforeTurnAround = ToolsConfig.Common.woodBoomerangRange.getValue() <= 0 ? 20 : ToolsConfig.Common.woodBoomerangRange.getValue();
    }

    @Override
    protected int getDamage(Entity hitEntity, Player player) {
        if (ToolsConfig.Common.woodBoomerangDamage.getValue() > 0) {
            return ToolsConfig.Common.woodBoomerangDamage.getValue();
        }

        return 0;
    }

    @Override
    public DamageSource causeNewDamage(BoomerangEntity entityboomerang, Entity entity) {
        return (new IndirectEntityDamageSource("wood_boomerang", entityboomerang, entity)).setProjectile();
    }

}
