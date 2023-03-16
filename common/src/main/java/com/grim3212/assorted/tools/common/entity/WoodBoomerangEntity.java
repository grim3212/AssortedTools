package com.grim3212.assorted.tools.common.entity;

import com.grim3212.assorted.tools.ToolsCommonMod;
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
        this.timeBeforeTurnAround = ToolsCommonMod.COMMON_CONFIG.woodBoomerangRange.get() <= 0 ? 20 : ToolsCommonMod.COMMON_CONFIG.woodBoomerangRange.get();
    }

    public WoodBoomerangEntity(Level worldIn, Player entity, ItemStack itemstack, InteractionHand hand) {
        super(ToolsEntities.WOOD_BOOMERANG.get(), worldIn, entity, itemstack, hand);
        this.timeBeforeTurnAround = ToolsCommonMod.COMMON_CONFIG.woodBoomerangRange.get() <= 0 ? 20 : ToolsCommonMod.COMMON_CONFIG.woodBoomerangRange.get();
    }

    @Override
    protected int getDamage(Entity hitEntity, Player player) {
        if (ToolsCommonMod.COMMON_CONFIG.woodBoomerangDamage.get() > 0) {
            return ToolsCommonMod.COMMON_CONFIG.woodBoomerangDamage.get();
        }

        return 0;
    }

    @Override
    public DamageSource causeNewDamage(BoomerangEntity entityboomerang, Entity entity) {
        return (new IndirectEntityDamageSource("wood_boomerang", entityboomerang, entity)).setProjectile();
    }

}
