package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.common.handler.ItemTierHolder;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import com.grim3212.assorted.tools.common.item.configurable.ConfigurableTieredItem;
import com.grim3212.assorted.tools.common.util.ToolsItemTier;

import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

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
	protected boolean allowedIn(CreativeModeTab group) {
		if (ToolsConfig.COMMON.hammersEnabled.get()) {

			if (this.isExtraMaterial) {
				if (!ToolsConfig.COMMON.extraMaterialsEnabled.get()) {
					return false;
				}

				ToolsItemTier tier = (ToolsItemTier) this.getTierHolder().getDefaultTier();
				if (ToolsConfig.COMMON.hideUncraftableItems.get() && ForgeRegistries.ITEMS.tags().getTag(tier.repairTag()).size() <= 0) {
					return false;
				}
			}

			return super.allowedIn(group);
		}
		return false;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		return 80f;
	}

	@Override
	public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
		return true;
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
		Level worldIn = player.level;

		if (!player.isCreative() && player.mayUseItemAt(pos, player.getDirection(), itemstack)) {
			if (!worldIn.isClientSide) {
				player.awardStat(Stats.BLOCK_MINED.get(worldIn.getBlockState(pos).getBlock()));
				player.causeFoodExhaustion(0.005F);

				worldIn.levelEvent(2001, pos, Block.getId(worldIn.getBlockState(pos)));
				worldIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

				itemstack.hurtAndBreak(1, player, (pEnt) -> {
					pEnt.broadcastBreakEvent(EquipmentSlot.MAINHAND);
				});
			}
			return true;
		} else {
			return false;
		}
	}

}