package com.grim3212.assorted.tools.common.item.configurable;

import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.world.item.Item;

/**
 * The attack damage and speed baselines the mod's tools are built with.
 * <p>
 * These used to live as constants on each {@code Configurable*Item}, feeding an overridden
 * {@code getDefaultAttributeModifiers}. Attributes are a data component now, so the numbers are
 * handed to {@code Item.Properties} at construction instead and the overrides are gone. They are
 * collected here so the values stay in one readable place.
 * <p>
 * Several differ from vanilla's - the pickaxe baseline is 3.0 rather than 1.0, and the hoe derives
 * its damage from the harvest level rather than using a fixed negative. Both are carried over
 * unchanged from 1.20.1 on purpose; they are what these tools have always hit for.
 */
public final class ConfigurableTools {

    public static final float PICKAXE_DAMAGE = 3.0F;
    public static final float PICKAXE_SPEED = -2.4F;
    public static final float SHOVEL_DAMAGE = 1.5F;
    public static final float SHOVEL_SPEED = -3.0F;
    public static final float SWORD_DAMAGE = 3.0F;
    public static final float SWORD_SPEED = -2.4F;
    public static final float HOE_SPEED = -0.0F;

    private ConfigurableTools() {
    }

    /**
     * Durability, repair material and enchantability, without any of the mining or attack
     * behaviour. For the tools that are not one of vanilla's shapes.
     */
    public static Item.Properties tiered(ItemTierConfig tier, Item.Properties properties) {
        return properties.durability(tier.getMaxUses()).repairable(tier.material().repairItems()).enchantable(tier.getEnchantability());
    }
}
