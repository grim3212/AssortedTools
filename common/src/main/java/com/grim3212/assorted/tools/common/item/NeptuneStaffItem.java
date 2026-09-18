package com.grim3212.assorted.tools.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

/**
 * Places water, freezes the mobs around the player, or freezes the still water around them. A hit
 * freezes what it hits.
 */
public class NeptuneStaffItem extends StaffItem {

    /** What placing a water source costs, the same as the staff's original. */
    public static final int PLACE_COST = 10;

    public NeptuneStaffItem(Properties properties) {
        super(StaffModeInfo.Kind.NEPTUNE, elemental(properties));
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        FrozenMobs.freeze(target);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        if (this.getMode(stack) != StaffMode.PLACE_WATER || context.getPlayer() == null) {
            return InteractionResult.PASS;
        }

        return StaffFluids.place(context, Fluids.WATER, PLACE_COST);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        StaffMode mode = this.getMode(stack);
        if (mode == StaffMode.PLACE_WATER) {
            return InteractionResult.PASS;
        }

        if (level instanceof ServerLevel serverLevel) {
            int affected = mode == StaffMode.FREEZE_MOBS ? freezeMobs(level, player) : freezeWater(serverLevel, player);
            if (affected == 0) {
                return InteractionResult.FAIL;
            }
            stack.hurtAndBreak(affected, player, hand);
        }

        return InteractionResult.SUCCESS;
    }

    private static int freezeMobs(Level level, Player player) {
        int frozen = 0;
        for (LivingEntity entity : inRange(level, player, entity -> true)) {
            if (FrozenMobs.freeze(entity)) {
                frozen++;
            }
        }
        return frozen;
    }

    /** Still water only, like a frosted walker: flowing water would just refill around the ice. */
    private static int freezeWater(ServerLevel level, Player player) {
        int frozen = 0;
        for (BlockPos pos : sphere(player)) {
            if (level.getBlockState(pos).is(Blocks.WATER) && level.getFluidState(pos).isSource() && level.setBlockAndUpdate(pos, Blocks.ICE.defaultBlockState())) {
                level.sendParticles(ParticleTypes.SNOWFLAKE, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 2, 0.3D, 0.1D, 0.3D, 0.0D);
                frozen++;
            }
        }

        if (frozen > 0) {
            level.playSound(null, player.blockPosition(), SoundEvents.GLASS_PLACE, SoundSource.PLAYERS, 1.0F, 0.8F);
        }
        return frozen;
    }
}
