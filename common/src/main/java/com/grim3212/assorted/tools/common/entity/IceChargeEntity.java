package com.grim3212.assorted.tools.common.entity;

import com.grim3212.assorted.tools.common.item.ToolsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * A thrown or dispensed ice charge, the fire charge's opposite: what it hits is chilled like powder
 * snow and put out, and the still water around where it lands freezes while fire there goes out.
 */
public class IceChargeEntity extends ThrowableItemProjectile {

    /** How far from the impact water freezes and fire goes out. */
    public static final int RADIUS = 2;
    /** Ticks past fully frozen a hit leaves a mob at; frost thaws two a tick out of powder snow. */
    public static final int CHILL_TICKS = 140;

    public IceChargeEntity(EntityType<? extends IceChargeEntity> type, Level level) {
        super(type, level);
    }

    public IceChargeEntity(Level level, LivingEntity owner, ItemStack stack) {
        super(ToolsEntities.ICE_CHARGE.get(), owner, level, stack);
    }

    public IceChargeEntity(Level level, double x, double y, double z, ItemStack stack) {
        super(ToolsEntities.ICE_CHARGE.get(), x, y, z, level, stack);
    }

    @Override
    protected Item getDefaultItem() {
        return ToolsItems.ICE_CHARGE.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        // Blazes, magma cubes and striders take extra, as they do from powder snow.
        float damage = entity.is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES) ? 5.0F : 1.0F;
        entity.hurt(this.damageSources().thrown(this, this.getOwner()), damage);
        entity.clearFire();
        if (entity.canFreeze()) {
            entity.setTicksFrozen(Math.max(entity.getTicksFrozen(), entity.getTicksRequiredToFreeze() + CHILL_TICKS));
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (this.level() instanceof ServerLevel level) {
            chill(level, BlockPos.containing(result.getLocation()));
            level.sendParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), 16, 0.4D, 0.4D, 0.4D, 0.02D);
            level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.POWDER_SNOW_BREAK, SoundSource.NEUTRAL, 1.0F, 1.2F);
            this.discard();
        }
    }

    /** Still water only, like the Neptune staff: flowing water would just refill around the ice. */
    public static void chill(ServerLevel level, BlockPos center) {
        BlockPos.betweenClosed(center.offset(-RADIUS, -RADIUS, -RADIUS), center.offset(RADIUS, RADIUS, RADIUS)).forEach(pos -> {
            if (pos.distSqr(center) > RADIUS * RADIUS) {
                return;
            }
            if (level.getBlockState(pos).is(BlockTags.FIRE)) {
                level.removeBlock(pos, false);
            } else if (level.getBlockState(pos).is(Blocks.WATER) && level.getFluidState(pos).isSource()) {
                level.setBlockAndUpdate(pos, Blocks.ICE.defaultBlockState());
            }
        });
    }
}
