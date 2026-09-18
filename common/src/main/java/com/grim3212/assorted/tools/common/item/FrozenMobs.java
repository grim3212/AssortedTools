package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.tools.platform.IFrozenStorage;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

/**
 * Mobs the Neptune staff freezes and the Phoenix staff thaws. Frozen is vanilla's no-AI and silent
 * flags plus the loader's {@link IFrozenStorage} marker, which clients draw as ice and which lets a
 * thaw wake only mobs a staff froze, never one a map maker made still.
 */
public final class FrozenMobs {

    private static final IFrozenStorage STORAGE = Services.load(IFrozenStorage.class);

    private FrozenMobs() {
    }

    public static boolean isFrozen(Entity entity) {
        return STORAGE.isFrozen(entity);
    }

    /** Freezes a mob that is not already still. Players and other non-mobs are left alone. */
    public static boolean freeze(Entity entity) {
        if (!(entity instanceof Mob mob) || mob.isNoAi() || isFrozen(mob) || !mob.isAlive()) {
            return false;
        }

        STORAGE.setFrozen(mob, true);
        mob.setNoAi(true);
        mob.setSilent(true);
        effects(mob, ParticleTypes.SNOWFLAKE, SoundEvents.POWDER_SNOW_PLACE);
        return true;
    }

    public static boolean thaw(Entity entity) {
        if (!(entity instanceof Mob mob) || !isFrozen(mob)) {
            return false;
        }

        STORAGE.setFrozen(mob, false);
        mob.setNoAi(false);
        mob.setSilent(false);
        effects(mob, ParticleTypes.FLAME, SoundEvents.FIRE_EXTINGUISH);
        return true;
    }

    /**
     * Fire thaws: called each server tick for every entity by the loader's tick hook. A fire-immune
     * mob never counts as on fire, so it stays frozen in lava.
     */
    public static void thawIfBurning(Entity entity) {
        if (entity.isOnFire() && isFrozen(entity)) {
            thaw(entity);
        }
    }

    private static void effects(Mob mob, ParticleOptions particle, SoundEvent sound) {
        if (mob.level() instanceof ServerLevel level) {
            level.sendParticles(particle, mob.getX(), mob.getY(0.5D), mob.getZ(), 12, mob.getBbWidth() / 2.0D, mob.getBbHeight() / 2.0D, mob.getBbWidth() / 2.0D, 0.02D);
            level.playSound(null, mob.getX(), mob.getY(), mob.getZ(), sound, SoundSource.NEUTRAL, 1.0F, 1.0F);
        }
    }
}
