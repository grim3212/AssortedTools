package com.grim3212.assorted.tools.common.entity;

import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.grim3212.assorted.tools.common.util.SpearType;

import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

public class SpearEntity extends AbstractArrowEntity implements IEntityAdditionalSpawnData {

	private SpearType spearType;
	private int bounceCount;

	public SpearEntity(EntityType<? extends AbstractArrowEntity> type, World level) {
		super(type, level);
	}

	public SpearEntity(double x, double y, double z, World level, SpearType spearType) {
		super(ToolsEntities.SPEAR.get(), x, y, z, level);
		this.spearType = spearType;
		setBaseDamage(this.spearType.getDamage());
	}

	public SpearEntity(LivingEntity entity, World level, SpearType spearType) {
		super(ToolsEntities.SPEAR.get(), entity, level);
		this.spearType = spearType;
		setBaseDamage(this.spearType.getDamage());
	}

	@Override
	protected ItemStack getPickupItem() {
		switch (this.spearType) {
			case FLINT:
				return new ItemStack(ToolsItems.FLINT_SPEAR.get());
			case IRON:
				return new ItemStack(ToolsItems.IRON_SPEAR_BASIC.get());
			case DIAMOND:
				return new ItemStack(ToolsItems.DIAMOND_SPEAR_BASIC.get());
			case EXPLOSIVE:
				return new ItemStack(ToolsItems.EXPLOSIVE_SPEAR.get());
			case FIRE:
				return new ItemStack(ToolsItems.FIRE_SPEAR.get());
			case LIGHT:
				return new ItemStack(ToolsItems.FLINT_SPEAR.get());
			case LIGHTNING:
				return new ItemStack(ToolsItems.LIGHTNING_SPEAR.get());
			case SLIME:
				return new ItemStack(ToolsItems.SLIME_SPEAR.get());
			default:
				return new ItemStack(ToolsItems.FLINT_SPEAR.get());
		}
	}

	@Override
	protected void onHitEntity(EntityRayTraceResult rayTrace) {
		super.onHitEntity(rayTrace);
		if (this.spearType == SpearType.EXPLOSIVE) {
			if (!level.isClientSide) {
				level.explode(null, this.getX(), this.getY(), this.getZ(), 3F, Explosion.Mode.BREAK);
			}
			remove();
		} else if (this.spearType == SpearType.LIGHTNING) {
			LightningBoltEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(level);
			lightningEntity.moveTo(Vector3d.atBottomCenterOf(blockPosition()));
			level.addFreshEntity(lightningEntity);
			remove();
		} else if (this.spearType == SpearType.FIRE) {
			for (int fire = 0; fire < 6; ++fire) {
				BlockPos blockPos = this.blockPosition().offset(this.random.nextInt(3) - 1, this.random.nextInt(3) - 1, this.random.nextInt(3) - 1);

				if (this.level.getBlockState(blockPos).isAir() && this.level.getBlockState(blockPos.below()).isSolidRender(this.level, blockPos.below())) {
					level.setBlockAndUpdate(blockPos, AbstractFireBlock.getState(this.level, blockPos));
				}
			}
			remove();
		}
	}

	@Override
	protected void onHitBlock(BlockRayTraceResult rayTrace) {
		if (this.spearType == SpearType.LIGHT) {
			super.onHitBlock(rayTrace);
			BlockState state = this.level.getBlockState(rayTrace.getBlockPos());
			if (rayTrace.getDirection() == Direction.UP) {
				if (!level.isClientSide && state.getBlock() != Blocks.AIR && this.level.getBlockState(rayTrace.getBlockPos().above(2)).getBlock() == Blocks.AIR) {
					level.setBlockAndUpdate(rayTrace.getBlockPos().above(), Blocks.TORCH.defaultBlockState());
				}
			} else {
				if (!level.isClientSide && state.getBlock() != Blocks.AIR && this.level.getBlockState(rayTrace.getBlockPos().relative(rayTrace.getDirection(), 2)).getBlock() == Blocks.AIR) {
					level.setBlockAndUpdate(rayTrace.getBlockPos().relative(rayTrace.getDirection()), Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, rayTrace.getDirection()));
				}
			}
		} else if (this.spearType == SpearType.LIGHTNING) {
			super.onHitBlock(rayTrace);
			LightningBoltEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(level);
			lightningEntity.moveTo(Vector3d.atBottomCenterOf(blockPosition()));
			level.addFreshEntity(lightningEntity);
			remove();
		} else if (this.spearType == SpearType.FIRE) {
			super.onHitBlock(rayTrace);
			for (int fire = 0; fire < 6; ++fire) {
				BlockPos blockPos = rayTrace.getBlockPos().offset(this.random.nextInt(3) - 1, this.random.nextInt(3) - 1, this.random.nextInt(3) - 1);

				if (this.level.getBlockState(blockPos).isAir() && AbstractFireBlock.canBePlacedAt(level, blockPos, getDirection())) {
					level.setBlockAndUpdate(blockPos, AbstractFireBlock.getState(this.level, blockPos));
				}
			}
			remove();
		} else if (this.spearType == SpearType.EXPLOSIVE) {
			super.onHitBlock(rayTrace);
			if (!level.isClientSide) {
				level.explode(null, this.getX(), this.getY(), this.getZ(), 3F, Explosion.Mode.BREAK);
			}
			remove();
		} else if (this.spearType == SpearType.SLIME) {
			if (bounceCount == 3) {
				super.onHitBlock(rayTrace);
				this.spawnSlimeParticles();
			} else {
				bounceCount++;
				Vector3d motion = this.getDeltaMovement();
				this.setDeltaMovement(motion.x, motion.y * -1D, motion.z);
				level.playSound((PlayerEntity) null, this.blockPosition(), SoundEvents.SLIME_SQUISH_SMALL, SoundCategory.PLAYERS, 1.0F, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
				this.spawnSlimeParticles();
				this.playSound(SoundEvents.ARROW_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
			}
		} else {
			super.onHitBlock(rayTrace);
		}
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

	@Override
	public void addAdditionalSaveData(CompoundNBT nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.putString("SpearType", this.spearType.name());
		nbt.putInt("BounceCount", this.bounceCount);
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT nbt) {
		super.readAdditionalSaveData(nbt);
		this.spearType = SpearType.valueOf(nbt.getString("SpearType"));
		this.bounceCount = nbt.getInt("BounceCount");
	}
	
	@Override
	public void writeSpawnData(PacketBuffer buffer) {
		buffer.writeEnum(spearType);
		buffer.writeInt(bounceCount);
	}

	@Override
	public void readSpawnData(PacketBuffer additionalData) {
		this.spearType = additionalData.readEnum(SpearType.class);
		this.bounceCount = additionalData.readInt();
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public boolean isCritArrow() {
		return false;
	}

	@Override
	public boolean shotFromCrossbow() {
		return false;
	}

	@Override
	public void setEnchantmentEffectsFromEntity(LivingEntity entity, float modifier) {
	}
}
