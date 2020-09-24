package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.common.entity.BoomerangEntity;
import com.grim3212.assorted.tools.common.entity.DiamondBoomerangEntity;
import com.grim3212.assorted.tools.common.entity.WoodBoomerangEntity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class BoomerangItem extends Item {

	private final boolean isWood;

	public BoomerangItem(boolean isWood, Item.Properties props) {
		super(props);
		this.isWood = isWood;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (playerIn.getHeldItem(handIn).getDamage() == 0) {
			BoomerangEntity boom;
			if (this.isWood) {
				boom = new WoodBoomerangEntity(worldIn, playerIn, playerIn.getHeldItem(handIn), handIn);
			} else {
				boom = new DiamondBoomerangEntity(worldIn, playerIn, playerIn.getHeldItem(handIn), handIn);
			}

			worldIn.addEntity(boom);
			playerIn.setHeldItem(handIn, ItemStack.EMPTY);
		}
		return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
	}

}
