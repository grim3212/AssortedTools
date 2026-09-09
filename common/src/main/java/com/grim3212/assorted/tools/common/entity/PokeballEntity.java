package com.grim3212.assorted.tools.common.entity;

import com.grim3212.assorted.lib.util.NBTHelper;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntitySpawnRequest;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Optional;

public class PokeballEntity extends ThrowableItemProjectile {

    private boolean hasEntity;

    public PokeballEntity(EntityType<? extends PokeballEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    public PokeballEntity(EntityType<? extends PokeballEntity> type, double x, double y, double z, Level worldIn, ItemStack stack) {
        super(type, x, y, z, worldIn, stack);
    }

    public PokeballEntity(LivingEntity livingEntityIn, Level worldIn, ItemStack stack) {
        // ThrowableItemProjectile carries and syncs the thrown stack itself now, so the entity no
        // longer keeps its own copy - getItem()/setItem() are the pokeball.
        super(ToolsEntities.POKEBALL.get(), livingEntityIn, worldIn, stack);
        this.hasEntity = NBTHelper.hasTag(stack, "StoredEntity");
    }

    @Override
    protected void onHit(HitResult result) {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        // getItem() hands back the live synched stack, so it is copied before being changed.
        ItemStack currentPokeball = this.getItem().copy();

        if (result.getType() == HitResult.Type.BLOCK) {
            if (this.hasEntity) {
                // Entities deserialize from a ValueInput now, so the stored tag is wrapped in one.
                try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(this.problemPath(), Constants.LOG)) {
                    Optional<Entity> loadEntity = EntityType.create(
                            TagValueInput.create(reporter, serverLevel.registryAccess(), NBTHelper.getTag(currentPokeball, "StoredEntity")),
                            serverLevel,
                            new EntitySpawnRequest(EntitySpawnReason.BUCKET, true));
                    if (loadEntity.isPresent()) {
                        Entity spawnEntity = loadEntity.get();
                        spawnEntity.snapTo(this.getX(), this.getY() + 1.0D, this.getZ(), this.getYRot(), 0.0F);
                        serverLevel.addFreshEntity(spawnEntity);
                    }
                }

                // Always reset pokeball
                currentPokeball = new ItemStack(ToolsItems.POKEBALL.get());
            }
        } else if (result.getType() == HitResult.Type.ENTITY) {
            Entity hitEntity = ((EntityHitResult) result).getEntity();
            if (hitEntity != null && !this.hasEntity && !(hitEntity instanceof Player || hitEntity instanceof EnderDragon || hitEntity instanceof EnderDragonPart)) {
                if (hitEntity instanceof LivingEntity livingEntity) {
                    try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(livingEntity.problemPath(), Constants.LOG)) {
                        TagValueOutput entityOutput = TagValueOutput.createWithContext(reporter, serverLevel.registryAccess());
                        // Entity#save writes the encode id itself, so the accessor mixin the old
                        // code needed to reach getEncodeId() is no longer used here.
                        if (livingEntity.save(entityOutput)) {
                            CompoundTag entity = entityOutput.buildResult();
                            entity.putString("pokeball_name", livingEntity.getType().getDescriptionId());

                            NBTHelper.putTag(currentPokeball, "StoredEntity", entity);
                            currentPokeball.hurtAndBreak(1, serverLevel, null, item -> {
                            });
                            currentPokeball.setCount(1);

                            hitEntity.discard();
                        }
                    }
                }
            }
        }

        this.spawnAtLocation(serverLevel, currentPokeball, 0.2F);
        serverLevel.broadcastEntityEvent(this, (byte) 3);
        this.discard();
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; ++i) {
                this.level().addParticle(ParticleTypes.ITEM_SNOWBALL, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    protected Item getDefaultItem() {
        return ToolsItems.POKEBALL.get();
    }

}
