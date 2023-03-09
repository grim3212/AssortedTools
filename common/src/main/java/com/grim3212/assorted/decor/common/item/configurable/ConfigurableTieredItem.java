package com.grim3212.assorted.decor.common.item.configurable;

import com.grim3212.assorted.decor.api.item.ITiered;
import com.grim3212.assorted.decor.config.ItemTierConfig;
import com.grim3212.assorted.lib.core.item.ExtraPropertyHelper;
import com.grim3212.assorted.lib.core.item.IItemExtraProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ConfigurableTieredItem extends Item implements ITiered, IItemExtraProperties {

    private final ItemTierConfig tierHolder;

    public ConfigurableTieredItem(ItemTierConfig tierHolder, Properties builder) {
        super(builder.defaultDurability(tierHolder.getDefaultTier().getUses()));
        this.tierHolder = tierHolder;
    }

    @Override
    public ItemTierConfig getTierHolder() {
        return tierHolder;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return this.tierHolder.getMaxUses();
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
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return this.tierHolder.getDefaultTier().getRepairIngredient().test(repair) || super.isValidRepairItem(toRepair, repair);
    }
}
