package com.grim3212.assorted.decor.common.item;

import com.grim3212.assorted.decor.api.ToolsTags;
import com.grim3212.assorted.decor.api.item.ITiered;
import com.grim3212.assorted.decor.common.enchantment.ToolsEnchantments;
import com.grim3212.assorted.decor.config.ItemTierConfig;
import com.grim3212.assorted.lib.core.item.ExtraPropertyHelper;
import com.grim3212.assorted.lib.core.item.IItemCorrectDrops;
import com.grim3212.assorted.lib.core.item.IItemEnchantmentCondition;
import com.grim3212.assorted.lib.core.item.IItemExtraProperties;
import net.minecraft.core.dispenser.ShearsDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MaterialShears extends ShearsItem implements ITiered, IItemExtraProperties, IItemCorrectDrops, IItemEnchantmentCondition {

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
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == ToolsEnchantments.CORAL_CUTTER.get() || enchantment == Enchantments.BLOCK_EFFICIENCY || ExtraPropertyHelper.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        if (ToolsEnchantments.hasCoralCutter(stack)) {
            return state.is(ToolsTags.Blocks.ALL_CORALS) ? true : ExtraPropertyHelper.isCorrectToolForDrops(stack, state);
        }

        return ExtraPropertyHelper.isCorrectToolForDrops(stack, state);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (ToolsEnchantments.hasCoralCutter(stack)) {
            return state.is(ToolsTags.Blocks.ALL_CORALS) ? 10.0F : super.getDestroySpeed(stack, state);
        }

        return super.getDestroySpeed(stack, state);
    }

    @Override
    public ItemTierConfig getTierHolder() {
        return this.tierHolder;
    }
}
