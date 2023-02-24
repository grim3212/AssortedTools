package com.grim3212.assorted.decor.common.item.configurable;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.grim3212.assorted.decor.api.item.ITiered;
import com.grim3212.assorted.tools.common.handler.ItemTierHolder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public abstract class ConfigurableToolItem extends DiggerItem implements ITiered {

    private final ItemTierHolder tierHolder;
    protected final Supplier<Float> speed;
    protected final Supplier<Float> toolDamage;

    public ConfigurableToolItem(ItemTierHolder tierHolder, Supplier<Float> toolDamage, Supplier<Float> attackSpeedIn, TagKey<Block> effectiveBlocksIn, Properties builderIn) {
        super(tierHolder.getDefaultTier().getAttackDamageBonus(), tierHolder.getDefaultTier().getSpeed(), tierHolder.getDefaultTier(), effectiveBlocksIn, builderIn);
        this.tierHolder = tierHolder;
        this.speed = attackSpeedIn;
        this.toolDamage = toolDamage;
    }

    private Multimap<Attribute, AttributeModifier> attribs = null;

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        if (attribs == null) {
            Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", (double) this.getAttackDamage(), AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double) this.speed.get(), AttributeModifier.Operation.ADDITION));
            this.attribs = builder.build();
        }

        return slot == EquipmentSlot.MAINHAND ? this.attribs : super.getDefaultAttributeModifiers(slot);
    }

    @Override
    public ItemTierHolder getTierHolder() {
        return tierHolder;
    }

    public float getAttackDamage() {
        return this.toolDamage.get() + this.tierHolder.getDamage();
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return this.tierHolder.getMaxUses();
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return this.tierHolder.getEnchantability();
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return state.is(this.blocks) ? this.tierHolder.getEfficiency() : 1.0F;
    }

    public int getTierHarvestLevel() {
        return this.tierHolder.getHarvestLevel();
    }
}
