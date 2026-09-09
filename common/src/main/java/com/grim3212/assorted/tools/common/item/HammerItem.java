package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.common.item.configurable.ConfigurableTieredItem;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class HammerItem extends ConfigurableTieredItem {

    public HammerItem(ItemTierConfig tierHolder, Properties properties) {
        super(tierHolder, properties);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return 80f;
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return true;
    }

    /**
     * {@code canAttackBlock} is gone; {@code canDestroyBlock} is the hook both game modes now ask
     * before running the normal destroy path, and returning false from it aborts that path. Vanilla
     * uses it exactly this way in {@code DebugStickItem} - do the work here, then say no.
     */
    @Override
    public boolean canDestroyBlock(ItemStack stack, BlockState state, Level level, BlockPos pos, LivingEntity user) {
        if (user instanceof Player player && !player.isCreative() && player.mayUseItemAt(pos, player.getDirection(), stack)) {
            if (!level.isClientSide()) {
                player.awardStat(Stats.BLOCK_MINED.get(level.getBlockState(pos).getBlock()));
                player.causeFoodExhaustion(0.005F);

                level.levelEvent(2001, pos, Block.getId(level.getBlockState(pos)));
                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

                stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            }
            return false;
        }

        return true;
    }
}
