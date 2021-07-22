package com.grim3212.assorted.tools.common.entity;

import javax.annotation.Nullable;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.enchantment.ToolsEnchantments;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import com.grim3212.assorted.tools.common.item.BetterSpearItem;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.grim3212.assorted.tools.common.util.ToolsDamageSources;

import net.minecraft.block.AbstractFireBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

public class BetterSpearEntity extends AbstractArrowEntity implements IEntityAdditionalSpawnData {
	private static final DataParameter<Byte> ID_LOYALTY = EntityDataManager.defineId(BetterSpearEntity.class, DataSerializers.BYTE);
	private static final DataParameter<Boolean> ID_FOIL = EntityDataManager.defineId(BetterSpearEntity.class, DataSerializers.BOOLEAN);
	private ItemStack spearItem = new ItemStack(ToolsItems.WOOD_SPEAR.get());
	public int clientSideReturnSpearTickCount;
	private int bounceCount;
	private boolean effectTriggered;
	private boolean dealtDamage;

	public BetterSpearEntity(EntityType<? extends AbstractArrowEntity> type, World level) {
		super(type, level);
	}

	public BetterSpearEntity(World level, LivingEntity entity, ItemStack stack) {
		super(ToolsEntities.BETTER_SPEAR.get(), entity, level);
		this.spearItem = stack.copy();
		this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(stack));
		this.entityData.set(ID_FOIL, stack.hasFoil());
	}

	public BetterSpearEntity(World level, double x, double y, double z, ItemStack stack) {
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
				if (!this.level.isClientSide && this.pickup == AbstractArrowEntity.PickupStatus.ALLOWED) {
					this.spawnAtLocation(this.getPickupItem(), 0.1F);
				}

				this.remove();
			} else if (i > 0) {
				this.setNoPhysics(true);
				Vector3d vector3d = new Vector3d(entity.getX() - this.getX(), entity.getEyeY() - this.getY(), entity.getZ() - this.getZ());
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
			return !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
		} else {
			return false;
		}
	}

	@Override
	@Nullable
	protected EntityRayTraceResult findHitEntity(Vector3d vector3D, Vector3d vector3D1) {
		return this.dealtDamage ? null : super.findHitEntity(vector3D, vector3D1);
	}

	@Override
	protected void onHitBlock(BlockRayTraceResult rayTrace) {
		if (!this.effectTriggered) {
			int maxBounces = ToolsEnchantments.getMaxBounces(this.spearItem);

			if (this.bounceCount < maxBounces) {
				bounceCount++;
				Vector3d motion = this.getDeltaMovement();

				motion = motion.scale(this.bounceCount == 1 ? 0.42F : 0.99F);
				this.setDeltaMovement(motion.x, motion.y * -1D, motion.z);
				level.playSound((PlayerEntity) null, this.blockPosition(), SoundEvents.SLIME_SQUISH_SMALL, SoundCategory.PLAYERS, 1.0F, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
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
	protected void onHitEntity(EntityRayTraceResult rayTrace) {
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
		AssortedTools.LOGGER.info("Damage: " + f);
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
			float f2 = MathHelper.sin(f) * (float) 1 * 0.5F * f1;
			float f3 = MathHelper.cos(f) * (float) 1 * 0.5F * f1;
			this.level.addParticle(ParticleTypes.ITEM_SLIME, this.getX() + (double) f2, this.getY(), this.getZ() + (double) f3, 0.0D, 0.0D, 0.0D);
		}
	}

	private void tryConductivity(BlockPos pos) {
		int conductivity = ToolsEnchantments.getConductivity(this.spearItem);
		boolean flag = conductivity > 0 && this.random.nextDouble() <= 1.0D - ToolsConfig.COMMON.conductivityLightningChances.get().get(conductivity - 1);

		if (this.level instanceof ServerWorld && flag) {
			if (this.level.canSeeSky(pos)) {
				Entity owner = this.getOwner();
				LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(this.level);
				lightningboltentity.moveTo(Vector3d.atBottomCenterOf(pos));
				lightningboltentity.setCause(owner instanceof ServerPlayerEntity ? (ServerPlayerEntity) owner : null);
				this.level.addFreshEntity(lightningboltentity);
			}
		}
	}

	private void tryUnstable(BlockPos pos) {
		int instability = ToolsEnchantments.getInstability(this.spearItem);

		if (instability > 0) {
			if (!level.isClientSide) {
				level.explode(null, this.getX(), this.getY(), this.getZ(), instability * 2F, Explosion.Mode.BREAK);
			}
		}
	}

	private void tryFlammable(BlockPos pos) {
		boolean flammable = ToolsEnchantments.hasFlammable(this.spearItem);

		if (flammable) {
			for (int fire = 0; fire < 6; ++fire) {
				BlockPos blockPos = pos.offset(this.random.nextInt(3) - 1, this.random.nextInt(3) - 1, this.random.nextInt(3) - 1);

				if (this.level.getBlockState(blockPos).isAir() && AbstractFireBlock.canBePlacedAt(level, blockPos, getDirection())) {
					level.setBlockAndUpdate(blockPos, AbstractFireBlock.getState(this.level, blockPos));
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
	public void playerTouch(PlayerEntity player) {
		Entity entity = this.getOwner();
		if (entity == null || entity.getUUID() == player.getUUID()) {
			super.playerTouch(player);
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.put("Spear", this.spearItem.save(new CompoundNBT()));
		nbt.putBoolean("DealtDamage", this.dealtDamage);
		nbt.putBoolean("EffectTriggered", this.effectTriggered);
		nbt.putInt("BounceCount", this.bounceCount);
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT nbt) {
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
	public void writeSpawnData(PacketBuffer buffer) {
		buffer.writeInt(bounceCount);
		buffer.writeBoolean(dealtDamage);
		buffer.writeBoolean(effectTriggered);
		buffer.writeItem(spearItem);
	}

	@Override
	public void readSpawnData(PacketBuffer additionalData) {
		this.bounceCount = additionalData.readInt();
		this.dealtDamage = additionalData.readBoolean();
		this.effectTriggered = additionalData.readBoolean();
		this.spearItem = additionalData.readItem();
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void tickDespawn() {
		int i = this.entityData.get(ID_LOYALTY);
		if (this.pickup != AbstractArrowEntity.PickupStatus.ALLOWED || i <= 0) {
			super.tickDespawn();
		}

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean shouldRender(double x, double y, double z) {
		return true;
	}
}
