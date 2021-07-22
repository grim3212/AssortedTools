package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.common.handler.ItemTierHolder;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import com.grim3212.assorted.tools.common.item.configurable.ConfigurableTieredItem;

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

	private final boolean isExtraMaterial;

	public HammerItem(ItemTierHolder tierHolder, Properties properties, boolean isExtraMaterial) {
		super(tierHolder, properties);
		this.isExtraMaterial = isExtraMaterial;
	}

	public HammerItem(ItemTierHolder tierHolder, Properties properties) {
		this(tierHolder, properties, false);
	}

	@Override
	protected boolean allowdedIn(ItemGroup group) {
		if (this.isExtraMaterial) {
			return ToolsConfig.COMMON.hammersEnabled.get() && ToolsConfig.COMMON.extraMaterialsEnabled.get() ? super.allowdedIn(group) : false;
		}

		return ToolsConfig.COMMON.hammersEnabled.get() ? super.allowdedIn(group) : false;
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
		World worldIn = player.level;

		if (!player.isCreative() && player.mayUseItemAt(pos, player.getDirection(), itemstack)) {
			if (!worldIn.isClientSide) {
				player.awardStat(Stats.BLOCK_MINED.get(worldIn.getBlockState(pos).getBlock()));
				player.causeFoodExhaustion(0.005F);

				worldIn.levelEvent(2001, pos, Block.getId(worldIn.getBlockState(pos)));
				worldIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

				itemstack.hurtAndBreak(1, player, (pEnt) -> {
					pEnt.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
				});
			}
			return true;
		} else {
			return false;
		}
	}

}