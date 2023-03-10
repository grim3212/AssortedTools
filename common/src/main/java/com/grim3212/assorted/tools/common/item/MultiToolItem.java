package com.grim3212.assorted.tools.common.item;

import com.google.common.collect.Sets;
import com.grim3212.assorted.lib.core.item.ExtraPropertyHelper;
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
import net.minecraft.world.level.material.Material;

import java.util.Set;

public abstract class MultiToolItem extends ConfigurableToolItem implements IItemEnchantmentCondition {

    private static final Set<Material> EFFECTIVE_ON_MATERIALS = Sets.newHashSet(Material.WOOD, Material.NETHER_WOOD, Material.PLANT, Material.REPLACEABLE_PLANT, Material.BAMBOO, Material.VEGETABLE, Material.METAL, Material.HEAVY_METAL, Material.STONE);

    public MultiToolItem(ItemTierConfig tier, Item.Properties builderIn) {
        super(tier, () -> tier.getAxeDamage() > tier.getDamage() ? tier.getAxeDamage() : tier.getDamage() + tier.getDamage(), () -> -2.8f, ToolsTags.Blocks.MINEABLE_MULTITOOL, builderIn);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        // Support any enchantment that one of the 5 types of tools that make a multitool can
        return ExtraPropertyHelper.canApplyAtEnchantingTable(stack, enchantment) ||
                ExtraPropertyHelper.canApplyAtEnchantingTable(new ItemStack(Items.IRON_SWORD), enchantment) ||
                ExtraPropertyHelper.canApplyAtEnchantingTable(new ItemStack(Items.IRON_SHOVEL), enchantment) ||
                ExtraPropertyHelper.canApplyAtEnchantingTable(new ItemStack(Items.IRON_PICKAXE), enchantment) ||
                ExtraPropertyHelper.canApplyAtEnchantingTable(new ItemStack(Items.IRON_HOE), enchantment) ||
                ExtraPropertyHelper.canApplyAtEnchantingTable(new ItemStack(Items.IRON_AXE), enchantment);
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
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.is(Blocks.COBWEB)) {
            return 15.0F;
        }
        Material material = state.getMaterial();
        return EFFECTIVE_ON_MATERIALS.contains(material) ? this.getTierHolder().getEfficiency() : material == Material.PLANT || material == Material.REPLACEABLE_PLANT || material == Material.VEGETABLE || state.is(BlockTags.LEAVES) ? 1.5F : super.getDestroySpeed(stack, state);
    }
}
