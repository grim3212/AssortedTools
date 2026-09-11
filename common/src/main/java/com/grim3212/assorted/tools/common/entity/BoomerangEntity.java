package com.grim3212.assorted.tools.common.entity;

import com.grim3212.assorted.tools.ToolsCommonMod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class BoomerangEntity extends Entity {

    private BlockPos activatedPos;
    protected boolean isBouncing;
    private double bounceFactor;
    private float prevBoomerangRotation;
    private boolean turningAround;
    protected int timeBeforeTurnAround;
    List<ItemEntity> itemsPickedUp;
    private ItemStack selfStack = ItemStack.EMPTY;
    private InteractionHand hand;
    private static final EntityDataAccessor<Float> ROTATION = SynchedEntityData.defineId(BoomerangEntity.class, EntityDataSerializers.FLOAT);
    /**
     * There is no UUID entity data serializer any more; owners are tracked as an
     * {@link EntityReference}, which carries the UUID and resolves it lazily against the level.
     */
    private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> RETURN_TO = SynchedEntityData.defineId(BoomerangEntity.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);

    public BoomerangEntity(EntityType<BoomerangEntity> type, Level world) {
        super(type, world);
        this.bounceFactor = 0.84999999999999998D;
        this.turningAround = false;
        this.timeBeforeTurnAround = 30;
        this.itemsPickedUp = new ArrayList<ItemEntity>();
        this.hand = InteractionHand.MAIN_HAND;
    }

    public BoomerangEntity(EntityType<BoomerangEntity> type, Level worldIn, Player entity, ItemStack itemstack, InteractionHand hand) {
        this(type, worldIn);
        this.selfStack = itemstack;
        this.setRot(entity.getYRot(), entity.getXRot());
        double x = -Mth.sin((entity.getYRot() * 3.141593F) / 180F);
        double z = Mth.cos((entity.getYRot() * 3.141593F) / 180F);

        double motionX = 0.5D * x * (double) Mth.cos((entity.getXRot() / 180F) * 3.141593F);
        double motionY = -0.5D * (double) Mth.sin((entity.getXRot() / 180F) * 3.141593F);
        double motionZ = 0.5D * z * (double) Mth.cos((entity.getXRot() / 180F) * 3.141593F);
        this.setDeltaMovement(new Vec3(motionX, motionY, motionZ));
        setPos(entity.getX(), this.getReturnEntityY(entity), entity.getZ());
        xo = getX();
        yo = getY();
        zo = getZ();
        this.isBouncing = false;
        this.turningAround = false;
        this.hand = hand;
        this.setReturnTo(entity);
    }

    public double getReturnEntityY(Player entity) {
        return entity.getY() + entity.getEyeHeight() - 0.10000000149011612D;
    }

    @Override
    public void tick() {
        Player player = this.getReturnTo();

        Vec3 vec3d1 = this.position();
        Vec3 vec3d = this.position().add(this.getDeltaMovement());
        HitResult raytraceresult = this.level().clip(new ClipContext(vec3d1, vec3d, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, this));

        if (raytraceresult != null) {
            if (raytraceresult.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = BlockPos.containing(raytraceresult.getLocation());
                BlockState state = level().getBlockState(pos);

                if (state.canBeReplaced() && ToolsCommonMod.COMMON_CONFIG.breaksPlants.get() || state.getBlock() == Blocks.TORCH && ToolsCommonMod.COMMON_CONFIG.breaksTorches.get()) {
                    level().destroyBlock(pos, true);
                }

                if ((state.getBlock() instanceof LeverBlock || state.getBlock() instanceof ButtonBlock) && ToolsCommonMod.COMMON_CONFIG.hitsButtons.get()) {
                    if (timeBeforeTurnAround > 0 && ToolsCommonMod.COMMON_CONFIG.turnAroundButton.get()) {
                        timeBeforeTurnAround = 0;
                    }
                    if (player != null && (activatedPos == null || !activatedPos.equals(pos))) {
                        activatedPos = pos;
                        // Block#use is gone; the no-item half of the interaction is
                        // BlockState#useWithoutItem, and it no longer takes a hand.
                        state.useWithoutItem(level(), player, (BlockHitResult) raytraceresult);
                    }
                }
            }
        }

        if (!turningAround) {
            Vec3 motionBefore = this.getDeltaMovement();
            this.move(MoverType.SELF, motionBefore);

            Vec3 motionAfter = this.getDeltaMovement();
            double newX = motionAfter.x;
            double newY = motionAfter.y;
            double newZ = motionAfter.z;

            boolean flag = false;
            if (motionAfter.x != motionBefore.x) {
                newX = -motionBefore.x;
                flag = true;
            }
            if (motionAfter.y != motionBefore.y) {
                newY = -motionBefore.y;
                flag = true;
            }
            if (motionAfter.z != motionBefore.z) {
                newZ = -motionBefore.z;
                flag = true;
            }
            if (flag) {
                isBouncing = true;
                this.setDeltaMovement(new Vec3(newX, newY, newZ).multiply(bounceFactor, bounceFactor, bounceFactor));
            }

            this.beforeTurnAround(player);

            if (timeBeforeTurnAround-- <= 0) {
                turningAround = true;
            }
        } else if (player != null) {
            double x = player.getX() - this.getX();
            double y = this.getReturnEntityY(player) - this.getY();
            double z = player.getZ() - this.getZ();
            double d = Math.sqrt(x * x + y * y + z * z);
            if (d < 1.5D) {
                setEntityDead();
            }
            this.setDeltaMovement((0.5D * x) / d, (0.5D * y) / d, (0.5D * z) / d);
            this.setPos(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);
        }

        determineRotation();
        prevBoomerangRotation = getBoomerangRotation();
        for (this.setBoomerangRotation(this.getBoomerangRotation() + 36F); this.getBoomerangRotation() > 360F; this.setBoomerangRotation(this.getBoomerangRotation() - 360F)) {
        }
        List<Entity> list = level().getEntities(this, this.getBoundingBox().expandTowards(0.5D, 0.5D, 0.5D));
        for (int i = 0; i < list.size(); i++) {
            Entity entity = list.get(i);
            if (entity instanceof ItemEntity) {
                itemsPickedUp.add((ItemEntity) entity);
                if (timeBeforeTurnAround > 0 && ToolsCommonMod.COMMON_CONFIG.turnAroundItem.get()) {
                    timeBeforeTurnAround = 0;
                }
                continue;
            }
            if (!(entity instanceof LivingEntity) || entity == player) {
                continue;
            }

            this.onEntityHit(entity, player);

            if (timeBeforeTurnAround > 0 && ToolsCommonMod.COMMON_CONFIG.turnAroundMob.get()) {
                timeBeforeTurnAround = 0;
            }
        }

        Iterator<ItemEntity> iterator = itemsPickedUp.iterator();
        while (iterator.hasNext()) {
            ItemEntity item = iterator.next();
            item.setDeltaMovement(0, 0, 0);
            if (item.isAlive()) {
                Vec3 pos = this.position();
                item.setPos(pos.x, pos.y, pos.z);
            }
        }

        super.tick();
    }

    public void beforeTurnAround(Player player) {
        // NO-OP
    }

    public void onEntityHit(Entity hitEntity, Player player) {
        // Entity#hurt and #hurtOrSimulate are both @Deprecated now; hurtServer is the real entry
        // point and only exists on the server, which is where damage was always applied anyway.
        if (this.level() instanceof ServerLevel serverLevel) {
            hitEntity.hurtServer(serverLevel, causeNewDamage(this, player), getDamage(hitEntity, player));
        }
    }

    /**
     * Abstract on {@link Entity} now. A boomerang cannot be attacked, like vanilla's own
     * non-living projectiles.
     */
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        return false;
    }

    protected abstract int getDamage(Entity hitEntity, Player player);

    public abstract DamageSource causeNewDamage(BoomerangEntity entityboomerang, Entity entity);

    public void setEntityDead() {
        Player returnTo = this.getReturnTo();
        if (returnTo != null && !this.selfStack.isEmpty()) {
            if (this.hand == InteractionHand.OFF_HAND && returnTo.getOffhandItem().isEmpty()) {
                returnTo.setItemInHand(InteractionHand.OFF_HAND, selfStack);
            } else {
                returnTo.getInventory().add(selfStack);
            }
        }
        super.removeAfterChangingDimensions();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(ROTATION, 0.0F);
        builder.define(RETURN_TO, Optional.empty());
    }

    public float getBoomerangRotation() {
        return this.getEntityData().get(ROTATION);
    }

    public void setBoomerangRotation(float rotationIn) {
        this.getEntityData().set(ROTATION, rotationIn);
    }

    @Nullable
    public UUID getReturnToId() {
        return this.entityData.get(RETURN_TO).map(EntityReference::getUUID).orElse(null);
    }

    public void setReturnTo(@Nullable Player player) {
        this.entityData.set(RETURN_TO, Optional.ofNullable(EntityReference.<LivingEntity>of(player)));
    }

    private void setReturnTo(@Nullable EntityReference<LivingEntity> reference) {
        this.entityData.set(RETURN_TO, Optional.ofNullable(reference));
    }

    @Nullable
    public Player getReturnTo() {
        EntityReference<LivingEntity> reference = this.entityData.get(RETURN_TO).orElse(null);
        if (reference == null) {
            return null;
        }

        LivingEntity entity = reference.getEntity(this.level(), LivingEntity.class);
        return entity instanceof Player player ? player : null;
    }

    public boolean isReturnTo(LivingEntity entityIn) {
        return entityIn == this.getReturnTo();
    }

    public void determineRotation() {
        Vec3 motion = this.getDeltaMovement();

        this.setYRot(-57.29578F * (float) Math.atan2(motion.x, motion.z));
        double d1 = Math.sqrt(motion.z * motion.z + motion.x * motion.x);
        this.setXRot(-57.29578F * (float) Math.atan2(motion.y, d1));
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        this.isBouncing = input.getBooleanOr("IsBouncing", false);
        this.bounceFactor = input.getDoubleOr("BounceFactor", 0.84999999999999998D);
        this.prevBoomerangRotation = input.getFloatOr("PrevBoomerangRotation", 0.0F);
        this.setBoomerangRotation(input.getFloatOr("BoomerangRotation", 0.0F));
        this.turningAround = input.getBooleanOr("TurningAround", false);
        this.timeBeforeTurnAround = input.getIntOr("TimeBeforeTurnAround", 30);

        int[] activated = input.getIntArray("ActivatedPos").orElse(null);
        this.activatedPos = activated != null && activated.length == 3 ? new BlockPos(activated[0], activated[1], activated[2]) : null;

        this.setReturnTo(EntityReference.<LivingEntity>read(input, "ReturnTo"));

        this.selfStack = input.read("SelfStack", ItemStack.CODEC).orElse(ItemStack.EMPTY);

        this.hand = "OFF_HAND".equals(input.getStringOr("hand", InteractionHand.MAIN_HAND.name())) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putBoolean("IsBouncing", isBouncing);
        output.putDouble("BounceFactor", bounceFactor);
        output.putFloat("PrevBoomerangRotation", prevBoomerangRotation);
        output.putFloat("BoomerangRotation", this.getBoomerangRotation());
        output.putBoolean("TurningAround", turningAround);
        output.putInt("TimeBeforeTurnAround", timeBeforeTurnAround);
        if (activatedPos != null) {
            output.putIntArray("ActivatedPos", new int[]{activatedPos.getX(), activatedPos.getY(), activatedPos.getZ()});
        }

        EntityReference.store(this.entityData.get(RETURN_TO).orElse(null), output, "ReturnTo");

        // ItemStack.CODEC refuses an empty stack, which a boomerang spawned without one still has.
        if (!this.selfStack.isEmpty()) {
            output.store("SelfStack", ItemStack.CODEC, this.selfStack);
        }
        output.putString("hand", this.hand.name());
    }
}
