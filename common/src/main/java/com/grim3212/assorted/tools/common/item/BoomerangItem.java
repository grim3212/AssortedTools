package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.common.entity.BoomerangEntity;
import com.grim3212.assorted.tools.common.entity.DiamondBoomerangEntity;
import com.grim3212.assorted.tools.common.entity.WoodBoomerangEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
    public InteractionResult use(Level worldIn, Player playerIn, InteractionHand handIn) {
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

        // InteractionResultHolder is gone; the held stack was already replaced above, and a plain
        // SUCCESS leaves whatever is in the hand alone, which is the same outcome as returning it.
        return InteractionResult.SUCCESS;
    }

}
