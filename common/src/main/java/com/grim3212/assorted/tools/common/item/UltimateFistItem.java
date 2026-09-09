package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.ToolsCommonMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The ultimate fist - the reward for assembling all eight fragments.
 * <p>
 * It is not a tiered item and never was; it just reads its numbers off the ultimate tier's
 * configuration. Those numbers are data components fixed at construction now, so the overridden
 * {@code getMaxDamage}, {@code getEnchantmentValue}, {@code isEnchantable} and
 * {@code getDefaultAttributeModifiers} are gone: no {@code enchantable} component at all is what
 * "not enchantable" means, and the attributes are handed to the properties instead.
 */
public class UltimateFistItem extends Item {

    private static final float ATTACK_SPEED = -0.5F;

    public UltimateFistItem(Properties props) {
        super(props.durability(ToolsCommonMod.COMMON_CONFIG.ultimateItemTier.getMaxUses())
                .fireResistant()
                .attributes(attributes())
                // The fist costs one durability per swing, which used to be an overridden hurtEnemy.
                .component(DataComponents.WEAPON, new Weapon(1)));
    }

    private static ItemAttributeModifiers attributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, ToolsCommonMod.COMMON_CONFIG.ultimateItemTier.getDamage(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, ATTACK_SPEED, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return ToolsCommonMod.COMMON_CONFIG.ultimateItemTier.getEfficiency();
    }

    /**
     * The fist carries no {@code minecraft:tool} component, so vanilla's mineBlock would do nothing
     * at all. This keeps the durability cost per mined block it has always had.
     */
    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity user) {
        if (!level.isClientSide() && state.getDestroySpeed(level, pos) != 0.0F) {
            stack.hurtAndBreak(1, user, EquipmentSlot.MAINHAND);
        }

        return true;
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return true;
    }

}
