package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.lib.core.item.IItemEnchantmentCondition;
import com.grim3212.assorted.tools.api.ToolsTags;
import com.grim3212.assorted.tools.common.item.configurable.ConfigurableToolItem;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class MultiToolItem extends ConfigurableToolItem implements IItemEnchantmentCondition {

    public MultiToolItem(ItemTierConfig tier, Item.Properties builderIn) {
        super(tier, () -> tier.getAxeDamage() > tier.getDamage() ? tier.getAxeDamage() : tier.getDamage() + tier.getDamage(), () -> -2.8f, ToolsTags.Blocks.MINEABLE_MULTITOOL, builderIn);
    }

    @Override
    public Optional<Boolean> assortedlib_canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return Optional.of(enchantment.canEnchant(new ItemStack(Items.IRON_SWORD)) ||
                enchantment.canEnchant(new ItemStack(Items.IRON_SHOVEL)) ||
                enchantment.canEnchant(new ItemStack(Items.IRON_PICKAXE)) ||
                enchantment.canEnchant(new ItemStack(Items.IRON_HOE)) ||
                enchantment.canEnchant(new ItemStack(Items.IRON_AXE)));
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level worldIn, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, (entity) -> {
            entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return (int) (super.getMaxDamage(stack) * this.getTierHolder().getMultiToolModifier());
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.is(Blocks.COBWEB)) {
            return 15.0F;
        }

        boolean validBlock = state.is(BlockTags.MINEABLE_WITH_SHOVEL) ||
                state.is(BlockTags.MINEABLE_WITH_PICKAXE) ||
                state.is(BlockTags.MINEABLE_WITH_AXE) ||
                state.is(BlockTags.MINEABLE_WITH_HOE);

        return validBlock ? this.getTierHolder().getEfficiency() : state.is(BlockTags.SWORD_EFFICIENT) ? 1.5F : super.getDestroySpeed(stack, state);
    }
}
