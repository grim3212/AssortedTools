package com.grim3212.assorted.tools.common.item.configurable;

import com.grim3212.assorted.tools.api.item.ITiered;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;

/**
 * A hoe built from a configured tool material. {@link HoeItem} survives for tilling.
 * <p>
 * The attack damage baseline is the configured harvest level, not a fixed negative as vanilla uses.
 * That is carried over from 1.20.1, where the overridden {@code getAttackDamage} did the same.
 */
public class ConfigurableHoeItem extends HoeItem implements ITiered {

    private final ItemTierConfig tierHolder;

    public ConfigurableHoeItem(ItemTierConfig tierHolder, Item.Properties properties) {
        super(tierHolder.material(), tierHolder.getHarvestLevel(), ConfigurableTools.HOE_SPEED, properties);
        this.tierHolder = tierHolder;
    }

    @Override
    public ItemTierConfig getTierHolder() {
        return tierHolder;
    }
}
