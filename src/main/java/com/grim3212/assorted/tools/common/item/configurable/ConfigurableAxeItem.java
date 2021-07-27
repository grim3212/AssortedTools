package com.grim3212.assorted.tools.common.item.configurable;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;
import com.grim3212.assorted.tools.common.handler.ModdedItemTierHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ToolType;

public class ConfigurableAxeItem extends ConfigurableToolItem {

	private static final Set<Material> EFFECTIVE_ON_MATERIALS = Sets.newHashSet(Material.WOOD, Material.NETHER_WOOD, Material.PLANT, Material.REPLACEABLE_PLANT, Material.BAMBOO, Material.VEGETABLE);

	public ConfigurableAxeItem(ModdedItemTierHolder tierHolder, Item.Properties builder) {
		super(tierHolder, tierHolder.getAxeDamage(), tierHolder.getAxeSpeed(), BlockTags.MINEABLE_WITH_AXE, builder.addToolType(ToolType.AXE, tierHolder.getHarvestLevel()));
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		Material material = state.getMaterial();
		return EFFECTIVE_ON_MATERIALS.contains(material) ? this.speed : super.getDestroySpeed(stack, state);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		BlockState blockstate = world.getBlockState(blockpos);
		BlockState block = blockstate.getToolModifiedState(world, blockpos, context.getPlayer(), context.getItemInHand(), ToolType.AXE);
		if (block != null) {
			Player playerentity = context.getPlayer();
			world.playSound(playerentity, blockpos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
			if (!world.isClientSide) {
				world.setBlock(blockpos, block, 11);
				if (playerentity != null) {
					context.getItemInHand().hurtAndBreak(1, playerentity, (p_220040_1_) -> {
						p_220040_1_.broadcastBreakEvent(context.getHand());
					});
				}
			}

			return InteractionResult.sidedSuccess(world.isClientSide);
		} else {
			return InteractionResult.PASS;
		}
	}

	@Override
	public void addToolTypes(Map<ToolType, Integer> toolClasses, ItemStack stack) {
		toolClasses.put(ToolType.AXE, this.getTierHarvestLevel());
	}
}
