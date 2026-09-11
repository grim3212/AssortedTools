package com.grim3212.assorted.tools.common.item.configurable;

import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.world.item.Item;

/**
 * The attack damage and speed baselines the mod's tools are built with. Some differ from vanilla on
 * purpose: the pickaxe baseline is 3.0 rather than 1.0, and the hoe's damage follows its harvest
 * level.
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
