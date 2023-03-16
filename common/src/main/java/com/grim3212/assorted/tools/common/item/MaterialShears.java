package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.lib.core.item.ExtraPropertyHelper;
import com.grim3212.assorted.lib.core.item.IItemExtraProperties;
import com.grim3212.assorted.tools.api.item.ITiered;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.core.dispenser.ShearsDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.block.DispenserBlock;

public class MaterialShears extends ShearsItem implements ITiered, IItemExtraProperties {

    private final ItemTierConfig tierHolder;

    public MaterialShears(Properties props, ItemTierConfig tierHolder) {
        super(props.defaultDurability(tierHolder.getDefaultTier().getUses()));
        this.tierHolder = tierHolder;

        DispenserBlock.registerBehavior(this, new ShearsDispenseItemBehavior());
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        // This is the current durability between Iron tools and the Iron Shears
        return (int) Math.rint(this.tierHolder.getMaxUses() * 0.952F);
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return ExtraPropertyHelper.isDamaged(stack);
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        ExtraPropertyHelper.setDamage(stack, damage);
    }

    @Override
    public int getDamage(ItemStack stack) {
        return ExtraPropertyHelper.getDamage(stack);
    }

    @Override
    public int getEnchantmentValue() {
        return this.tierHolder.getEnchantability();
    }

    @Override
    public ItemTierConfig getTierHolder() {
        return this.tierHolder;
    }
}
