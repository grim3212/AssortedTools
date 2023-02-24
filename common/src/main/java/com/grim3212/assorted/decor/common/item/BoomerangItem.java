package com.grim3212.assorted.decor.common.item;

import com.grim3212.assorted.tools.common.entity.BoomerangEntity;
import com.grim3212.assorted.tools.common.entity.DiamondBoomerangEntity;
import com.grim3212.assorted.tools.common.entity.WoodBoomerangEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BoomerangItem extends Item {

    private final boolean isWood;

    public BoomerangItem(boolean isWood, Item.Properties props) {
        super(props);
        this.isWood = isWood;
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
