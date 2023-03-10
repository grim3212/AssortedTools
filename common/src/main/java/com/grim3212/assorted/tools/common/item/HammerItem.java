package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.common.item.configurable.ConfigurableTieredItem;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EquipmentSlot;
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
    public boolean isCorrectToolForDrops(BlockState state) {
        return true;
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        ItemStack mainHandItem = player.getMainHandItem();
        if (!player.isCreative() && player.mayUseItemAt(pos, player.getDirection(), mainHandItem)) {
            if (!level.isClientSide) {
                player.awardStat(Stats.BLOCK_MINED.get(level.getBlockState(pos).getBlock()));
                player.causeFoodExhaustion(0.005F);

                level.levelEvent(2001, pos, Block.getId(level.getBlockState(pos)));
                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

                mainHandItem.hurtAndBreak(1, player, (pEnt) -> {
                    pEnt.broadcastBreakEvent(EquipmentSlot.MAINHAND);
                });
            }
            return false;
        } else {
            return true;
        }
    }
}