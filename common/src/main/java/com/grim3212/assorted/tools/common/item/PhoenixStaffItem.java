package com.grim3212.assorted.tools.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

/**
 * The Neptune staff's opposite: places lava or fire, thaws the mobs a Neptune staff froze, or melts
 * the ice around the player. A hit thaws what it hits.
 */
public class PhoenixStaffItem extends StaffItem {

    public static final int PLACE_COST = 10;

    public PhoenixStaffItem(Properties properties) {
        super(StaffModeInfo.Kind.PHOENIX, elemental(properties));
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        FrozenMobs.thaw(target);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }

        return switch (this.getMode(stack)) {
            case PLACE_LAVA -> StaffFluids.place(context, Fluids.LAVA, PLACE_COST);
            case PLACE_FIRE -> placeFire(context, player, stack);
            default -> InteractionResult.PASS;
        };
    }

    /** What flint and steel does to a block face, minus lighting campfires and candles. */
    private static InteractionResult placeFire(UseOnContext context, Player player, ItemStack stack) {
        Level level = context.getLevel();
        BlockPos firePos = context.getClickedPos().relative(context.getClickedFace());
        if (!BaseFireBlock.canBePlacedAt(level, firePos, context.getHorizontalDirection())) {
            return InteractionResult.FAIL;
        }

        level.playSound(player, firePos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
        level.setBlock(firePos, BaseFireBlock.getState(level, firePos), 11);
        if (!level.isClientSide()) {
            stack.hurtAndBreak(1, player, context.getHand());
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        StaffMode mode = this.getMode(stack);
        if (mode != StaffMode.THAW_MOBS && mode != StaffMode.MELT_ICE) {
            return InteractionResult.PASS;
        }

        if (level instanceof ServerLevel serverLevel) {
            int affected = mode == StaffMode.THAW_MOBS ? thawMobs(level, player) : meltIce(serverLevel, player);
            if (affected == 0) {
                return InteractionResult.FAIL;
            }
            stack.hurtAndBreak(affected, player, hand);
        }

        return InteractionResult.SUCCESS;
    }

    private static int thawMobs(Level level, Player player) {
        int thawed = 0;
        for (LivingEntity entity : inRange(level, player, FrozenMobs::isFrozen)) {
            if (FrozenMobs.thaw(entity)) {
                thawed++;
            }
        }
        return thawed;
    }

    /** Melts {@code #minecraft:ice} into water, or into nothing where water boils away. */
    private static int meltIce(ServerLevel level, Player player) {
        int melted = 0;
        for (BlockPos pos : sphere(player)) {
            if (level.getBlockState(pos).is(BlockTags.ICE)) {
                boolean evaporates = level.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, pos);
                level.setBlockAndUpdate(pos, evaporates ? Blocks.AIR.defaultBlockState() : Blocks.WATER.defaultBlockState());
                level.sendParticles(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 1, 0.3D, 0.1D, 0.3D, 0.0D);
                melted++;
            }
        }

        if (melted > 0) {
            level.playSound(null, player.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        return melted;
    }
}
