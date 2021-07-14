package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.common.entity.BoomerangEntity;
import com.grim3212.assorted.tools.common.entity.DiamondBoomerangEntity;
import com.grim3212.assorted.tools.common.entity.WoodBoomerangEntity;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
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
	protected boolean allowdedIn(ItemGroup group) {
		return ToolsConfig.COMMON.boomerangsEnabled.get() ? super.allowdedIn(group) : false;
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
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
		return ActionResult.success(playerIn.getItemInHand(handIn));
	}

}
