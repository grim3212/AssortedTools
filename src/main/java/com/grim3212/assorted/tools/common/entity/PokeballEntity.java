package com.grim3212.assorted.tools.common.entity;

import java.util.Optional;

import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.grim3212.assorted.tools.common.util.NBTHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class PokeballEntity extends ProjectileItemEntity {

	private boolean hasEntity;
	private ItemStack currentPokeball = new ItemStack(ToolsItems.POKEBALL.get(), 1);

	public PokeballEntity(EntityType<? extends PokeballEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public PokeballEntity(EntityType<? extends PokeballEntity> type, double x, double y, double z, World worldIn) {
		super(type, x, y, z, worldIn);
	}

	public PokeballEntity(LivingEntity livingEntityIn, World worldIn, ItemStack stack) {
		super(ToolsEntities.POKEBALL.get(), livingEntityIn, worldIn);
		this.currentPokeball = stack;
		this.hasEntity = false;
		if (this.currentPokeball.hasTag()) {
			this.hasEntity = NBTHelper.hasTag(stack, "StoredEntity");
		}
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!world.isRemote) {
			if (result.getType() == RayTraceResult.Type.BLOCK) {
				if (this.hasEntity) {
					Optional<Entity> loadEntity = EntityType.loadEntityUnchecked(NBTHelper.getTag(currentPokeball, "StoredEntity"), this.world);
					if (loadEntity.isPresent()) {
						Entity spawnEntity = loadEntity.get();
						spawnEntity.setLocationAndAngles(this.getPosX(), this.getPosY() + 1.0D, this.getPosZ(), this.rotationYaw, 0.0F);
						this.world.addEntity(spawnEntity);
					}

					// Always reset pokeball
					this.currentPokeball = new ItemStack(ToolsItems.POKEBALL.get());
				}
			} else if (result.getType() == RayTraceResult.Type.ENTITY) {
				EntityRayTraceResult entityResult = (EntityRayTraceResult) result;

				if (entityResult != null) {
					Entity hitEntity = entityResult.getEntity();
					if (hitEntity != null && !this.hasEntity && !(hitEntity instanceof PlayerEntity || hitEntity instanceof EnderDragonEntity || hitEntity instanceof EnderDragonPartEntity)) {
						if (hitEntity instanceof LivingEntity) {

							LivingEntity livingEntity = (LivingEntity) hitEntity;
							CompoundNBT entity = livingEntity.serializeNBT();
							entity.putString("pokeball_name", livingEntity.getType().getTranslationKey());

							NBTHelper.putTag(currentPokeball, "StoredEntity", entity);
							this.currentPokeball.damageItem(1, livingEntity, (ent) -> {
							});
							this.currentPokeball.setCount(1);

							hitEntity.remove();
						}
					}
				}
			}

			this.entityDropItem(this.currentPokeball, 0.2F);
			this.world.setEntityState(this, (byte) 3);
			this.setDead();
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handleStatusUpdate(byte id) {
		if (id == 3) {
			for (int i = 0; i < 8; ++i) {
				this.world.addParticle(ParticleTypes.ITEM_SNOWBALL, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	protected Item getDefaultItem() {
		return ToolsItems.POKEBALL.get();
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
