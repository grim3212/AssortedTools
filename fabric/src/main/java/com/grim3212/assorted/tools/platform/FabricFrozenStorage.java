package com.grim3212.assorted.tools.platform;

import com.grim3212.assorted.tools.Constants;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

/**
 * Frozen as a Fabric data attachment: saved with the mob, synced to every client tracking it. Only a
 * frozen mob carries it; removing it syncs like any other change.
 */
public class FabricFrozenStorage implements IFrozenStorage {

    public static final AttachmentType<Boolean> FROZEN = AttachmentRegistry.create(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "frozen"),
            builder -> builder.persistent(Codec.BOOL).syncWith(ByteBufCodecs.BOOL, AttachmentSyncPredicate.all()));

    /**
     * Registers {@link #FROZEN} from mod init. A client only accepts synced attachment types it had
     * registered when it connected, so one first created when a mob is frozen never reaches it.
     */
    public static void init() {
    }

    @Override
    public boolean isFrozen(Entity entity) {
        return entity.getAttachedOrElse(FROZEN, false);
    }

    @Override
    public void setFrozen(Entity entity, boolean frozen) {
        if (frozen) {
            entity.setAttached(FROZEN, true);
        } else {
            entity.removeAttached(FROZEN);
        }
    }
}
