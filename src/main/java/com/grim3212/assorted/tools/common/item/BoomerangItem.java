package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.common.entity.BoomerangEntity;
import com.grim3212.assorted.tools.common.entity.DiamondBoomerangEntity;
import com.grim3212.assorted.tools.common.entity.WoodBoomerangEntity;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;

public class BoomerangItem extends Item {

	private final boolean isWood;

	public BoomerangItem(boolean isWood, Item.Properties props) {
		super(props);
		this.isWood = isWood;
	}

	@Override
	protected boolean allowdedIn(CreativeModeTab group) {
		return ToolsConfig.COMMON.boomerangsEnabled.get() ? super.allowdedIn(group) : false;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		if (playerIn.getItemInHand(handIn).getDamageValue() == 0) {
			BoomerangEntity boom;
			if (this.isWood) {
				boom = new WoodBoomerangEntity(worldIn, playerIn, playerIn.getItemInHand(handIn), handIn);
			} else {
				boom = new DiamondBoomerangEntity(worldIn, playerIn, playerIn.getItemInHand(handIn), handIn);
			}

			worldIn.addFreshEntity(boom);
			playerIn.setItemInHand(handIn, ItemStack.EMPTY);
		}
		return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
	}

}
