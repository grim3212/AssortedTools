package com.grim3212.assorted.tools.common.entity;

import com.grim3212.assorted.lib.mixin.entity.EntityAccessor;
import com.grim3212.assorted.lib.util.NBTHelper;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Optional;

public class PokeballEntity extends ThrowableItemProjectile {

    private boolean hasEntity;
    private ItemStack currentPokeball = new ItemStack(ToolsItems.POKEBALL.get(), 1);

    public PokeballEntity(EntityType<? extends PokeballEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    public PokeballEntity(EntityType<? extends PokeballEntity> type, double x, double y, double z, Level worldIn) {
        super(type, x, y, z, worldIn);
    }

    public PokeballEntity(LivingEntity livingEntityIn, Level worldIn, ItemStack stack) {
        super(ToolsEntities.POKEBALL.get(), livingEntityIn, worldIn);
        this.currentPokeball = stack;
        this.hasEntity = false;
        if (this.currentPokeball.hasTag()) {
            this.hasEntity = NBTHelper.hasTag(stack, "StoredEntity");
        }
    }

    @Override
    protected void onHit(HitResult result) {
        if (!level.isClientSide) {
            if (result.getType() == HitResult.Type.BLOCK) {
                if (this.hasEntity) {
                    Optional<Entity> loadEntity = EntityType.create(NBTHelper.getTag(currentPokeball, "StoredEntity"), this.level);
                    if (loadEntity.isPresent()) {
                        Entity spawnEntity = loadEntity.get();
                        spawnEntity.moveTo(this.getX(), this.getY() + 1.0D, this.getZ(), this.getYRot(), 0.0F);
                        this.level.addFreshEntity(spawnEntity);
                    }

                    // Always reset pokeball
                    this.currentPokeball = new ItemStack(ToolsItems.POKEBALL.get());
                }
            } else if (result.getType() == HitResult.Type.ENTITY) {
                EntityHitResult entityResult = (EntityHitResult) result;

                if (entityResult != null) {
                    Entity hitEntity = entityResult.getEntity();
                    if (hitEntity != null && !this.hasEntity && !(hitEntity instanceof Player || hitEntity instanceof EnderDragon || hitEntity instanceof EnderDragonPart)) {
                        if (hitEntity instanceof LivingEntity) {

                            LivingEntity livingEntity = (LivingEntity) hitEntity;
                            CompoundTag entity = livingEntity.saveWithoutId(new CompoundTag());
                            String id = ((EntityAccessor) livingEntity).callGetEncodeId();
                            if (id != null) {
                                entity.putString("id", id);
                            }
                            entity.putString("pokeball_name", livingEntity.getType().getDescriptionId());

                            NBTHelper.putTag(currentPokeball, "StoredEntity", entity);
                            this.currentPokeball.hurtAndBreak(1, livingEntity, (ent) -> {
                            });
                            this.currentPokeball.setCount(1);

                            hitEntity.discard();
                        }
                    }
                }
            }

            this.spawnAtLocation(this.currentPokeball, 0.2F);
            this.level.broadcastEntityEvent(this, (byte) 3);
            this.removeAfterChangingDimensions();
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; ++i) {
                this.level.addParticle(ParticleTypes.ITEM_SNOWBALL, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    protected Item getDefaultItem() {
        return ToolsItems.POKEBALL.get();
    }

}
