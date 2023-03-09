package com.grim3212.assorted.decor.common.entity;

import com.google.common.collect.Lists;
import com.grim3212.assorted.decor.api.util.ToolsDamageSources;
import com.grim3212.assorted.decor.common.enchantment.ToolsEnchantments;
import com.grim3212.assorted.decor.common.item.BetterSpearItem;
import com.grim3212.assorted.decor.common.item.ToolsItems;
import com.grim3212.assorted.decor.config.ToolsConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BetterSpearEntity extends AbstractArrow {
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(BetterSpearEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(BetterSpearEntity.class, EntityDataSerializers.BOOLEAN);
    private ItemStack spearItem = new ItemStack(ToolsItems.WOOD_SPEAR.get());
    public int clientSideReturnSpearTickCount;
    private int bounceCount;
    private boolean effectTriggered;
    private boolean dealtDamage;

    public BetterSpearEntity(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    public BetterSpearEntity(Level level, LivingEntity entity, ItemStack stack) {
        super(ToolsEntities.BETTER_SPEAR.get(), entity, level);
        this.spearItem = stack.copy();
        this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(stack));
        this.entityData.set(ID_FOIL, stack.hasFoil());
    }

    public BetterSpearEntity(Level level, double x, double y, double z, ItemStack stack) {
        super(ToolsEntities.BETTER_SPEAR.get(), x, y, z, level);
        this.spearItem = stack.copy();
    }

    private float getDamage(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof BetterSpearItem) {
            return 5.0F + ((BetterSpearItem) item).getTierHolder().getDamage();
        }
        return 5.0F;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_LOYALTY, (byte) 0);
        this.entityData.define(ID_FOIL, false);
    }

    @Override
    protected ItemStack getPickupItem() {
        return spearItem.copy();
    }

    @OnlyIn(Dist.CLIENT)
    public ItemStack getSpearItem() {
        return spearItem.copy();
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isFoil() {
        return this.entityData.get(ID_FOIL);
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity entity = this.getOwner();
        if ((this.dealtDamage || this.isNoPhysics()) && entity != null) {
            int i = this.entityData.get(ID_LOYALTY);
            if (i > 0 && !this.isAcceptibleReturnOwner()) {
                if (!this.level.isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            } else if (i > 0) {
                this.setNoPhysics(true);
                Vec3 vector3d = new Vec3(entity.getX() - this.getX(), entity.getEyeY() - this.getY(), entity.getZ() - this.getZ());
                this.setPosRaw(this.getX(), this.getY() + vector3d.y * 0.015D * (double) i, this.getZ());
                if (this.level.isClientSide) {
                    this.yOld = this.getY();
                }

                double d0 = 0.05D * (double) i;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vector3d.normalize().scale(d0)));
                if (this.clientSideReturnSpearTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.clientSideReturnSpearTickCount;
            }
        }

        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    @Override
    @Nullable
    protected EntityHitResult findHitEntity(Vec3 vector3D, Vec3 vector3D1) {
        return this.dealtDamage ? null : super.findHitEntity(vector3D, vector3D1);
    }

    @Override
    protected void onHitBlock(BlockHitResult rayTrace) {
        if (!this.effectTriggered) {
            int maxBounces = ToolsEnchantments.getMaxBounces(this.spearItem);

            if (this.bounceCount < maxBounces) {
                bounceCount++;
                Vec3 motion = this.getDeltaMovement();

                motion = motion.scale(this.bounceCount == 1 ? 0.42F : 0.99F);
                this.setDeltaMovement(motion.x, motion.y * -1D, motion.z);
                level.playSound((Player) null, this.blockPosition(), SoundEvents.SLIME_SQUISH_SMALL, SoundSource.PLAYERS, 1.0F, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
                this.spawnSlimeParticles();
            } else {
                super.onHitBlock(rayTrace);
                if (maxBounces > 0) {
                    this.spawnSlimeParticles();
                }
                this.tryUnstable(rayTrace.getBlockPos());
                this.tryConductivity(blockPosition());
                this.tryFlammable(rayTrace.getBlockPos());
                this.effectTriggered = true;
            }
        } else {
            super.onHitBlock(rayTrace);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult rayTrace) {
        Entity hitEntity = rayTrace.getEntity();
        float f = this.getDamage(this.spearItem);
        if (hitEntity instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity) hitEntity;
            f += EnchantmentHelper.getDamageBonus(this.spearItem, livingentity.getMobType());
        }

        Entity owner = this.getOwner();
        DamageSource damagesource = ToolsDamageSources.spear(this, (Entity) (owner == null ? this : owner));
        this.dealtDamage = true;
        SoundEvent soundevent = SoundEvents.TRIDENT_HIT;
        if (hitEntity.hurt(damagesource, f)) {
            if (hitEntity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (hitEntity instanceof LivingEntity) {
                LivingEntity livingentity1 = (LivingEntity) hitEntity;
                if (owner instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity1, owner);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) owner, livingentity1);
                }

                this.doPostHurtEffects(livingentity1);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));

        if (!this.effectTriggered) {
            this.tryConductivity(hitEntity.blockPosition());
            this.tryFlammable(hitEntity.blockPosition());
            this.tryUnstable(hitEntity.blockPosition());
            this.effectTriggered = true;
        }

        this.playSound(soundevent, 1.0F, 1.0F);
    }

    private void spawnSlimeParticles() {
        for (int j = 0; j < 8; ++j) {
            float f = this.random.nextFloat() * ((float) Math.PI * 2F);
            float f1 = this.random.nextFloat() * 0.5F + 0.5F;
            float f2 = Mth.sin(f) * (float) 1 * 0.5F * f1;
            float f3 = Mth.cos(f) * (float) 1 * 0.5F * f1;
            this.level.addParticle(ParticleTypes.ITEM_SLIME, this.getX() + (double) f2, this.getY(), this.getZ() + (double) f3, 0.0D, 0.0D, 0.0D);
        }
    }

    private List<Float> conductiveChances() {
        List<Float> defaultChances = Lists.newArrayList(0.6F, 0.3F, 0.1F);
        List<Float> chances = ToolsConfig.Common.getConductivityChances();

        if (chances != null && chances instanceof List<?> && chances.size() == 3) {
            for (double chance : chances) {
                if (chance >= 1.0F && chance < 0.0F) {
                    return defaultChances;
                }
            }
            return chances;
        }

        return defaultChances;
    }

    private void tryConductivity(BlockPos pos) {
        int conductivity = ToolsEnchantments.getConductivity(this.spearItem);
        boolean flag = conductivity > 0 && this.random.nextDouble() <= 1.0D - conductiveChances().get(conductivity - 1);

        if (this.level instanceof ServerLevel && flag) {
            if (this.level.canSeeSky(pos)) {
                Entity owner = this.getOwner();
                LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(this.level);
                lightningboltentity.moveTo(Vec3.atBottomCenterOf(pos));
                lightningboltentity.setCause(owner instanceof ServerPlayer ? (ServerPlayer) owner : null);
                this.level.addFreshEntity(lightningboltentity);
            }
        }
    }

    private void tryUnstable(BlockPos pos) {
        int instability = ToolsEnchantments.getInstability(this.spearItem);

        if (instability > 0) {
            if (!level.isClientSide) {
                level.explode(null, this.getX(), this.getY(), this.getZ(), instability * 2F, Level.ExplosionInteraction.BLOCK);
            }
        }
    }

    private void tryFlammable(BlockPos pos) {
        boolean flammable = ToolsEnchantments.hasFlammable(this.spearItem);

        if (flammable) {
            for (int fire = 0; fire < 6; ++fire) {
                BlockPos blockPos = pos.offset(this.random.nextInt(3) - 1, this.random.nextInt(3) - 1, this.random.nextInt(3) - 1);

                if (this.level.getBlockState(blockPos).isAir() && BaseFireBlock.canBePlacedAt(level, blockPos, getDirection())) {
                    level.setBlockAndUpdate(blockPos, BaseFireBlock.getState(this.level, blockPos));
                }
            }
        }
    }

    @Override
    public boolean fireImmune() {
        return ToolsEnchantments.hasFlammable(this.spearItem) || ToolsEnchantments.getConductivity(this.spearItem) > 0 || super.fireImmune();
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    public void playerTouch(Player player) {
        Entity entity = this.getOwner();
        if (entity == null || entity.getUUID() == player.getUUID()) {
            super.playerTouch(player);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.put("Spear", this.spearItem.save(new CompoundTag()));
        nbt.putBoolean("DealtDamage", this.dealtDamage);
        nbt.putBoolean("EffectTriggered", this.effectTriggered);
        nbt.putInt("BounceCount", this.bounceCount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("Spear", 10)) {
            this.spearItem = ItemStack.of(nbt.getCompound("Spear"));
        }

        this.dealtDamage = nbt.getBoolean("DealtDamage");
        this.effectTriggered = nbt.getBoolean("EffectTriggered");
        this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(this.spearItem));
        this.bounceCount = nbt.getInt("BounceCount");
    }


    @Override
    public void tickDespawn() {
        int i = this.entityData.get(ID_LOYALTY);
        if (this.pickup != AbstractArrow.Pickup.ALLOWED || i <= 0) {
            super.tickDespawn();
        }

    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }
}
