package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.common.handler.ItemTierHolder;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HammerItem extends ConfigurableTieredItem {

	public HammerItem(ItemTierHolder tierHolder, Properties properties) {
		super(tierHolder, properties);
	}

	@Override
	protected boolean isInGroup(ItemGroup group) {
		return ToolsConfig.COMMON.hammersEnabled.get() ? super.isInGroup(group) : false;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		return 80f;
	}

	@Override
	public boolean canHarvestBlock(ItemStack stack, BlockState state) {
		return true;
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
		World worldIn = player.world;

		if (!player.isCreative() && player.canPlayerEdit(pos, player.getHorizontalFacing(), itemstack)) {
			if (!worldIn.isRemote) {
				player.addStat(Stats.BLOCK_MINED.get(worldIn.getBlockState(pos).getBlock()));
				player.addExhaustion(0.005F);

				worldIn.playEvent(2001, pos, Block.getStateId(worldIn.getBlockState(pos)));
				worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());

				itemstack.damageItem(1, player, (pEnt) -> {
					pEnt.sendBreakAnimation(EquipmentSlotType.MAINHAND);
				});
			}
			return true;
		} else {
			return false;
		}
	}

}