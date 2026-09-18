package com.grim3212.assorted.tools.platform;

import net.minecraft.world.entity.Entity;

/**
 * Whether a mob is frozen by a Neptune staff, kept in each loader's own entity data attachment so it
 * saves with the mob and syncs to clients, which draw frozen mobs as ice.
 */
public interface IFrozenStorage {

    boolean isFrozen(Entity entity);

    void setFrozen(Entity entity, boolean frozen);
}
