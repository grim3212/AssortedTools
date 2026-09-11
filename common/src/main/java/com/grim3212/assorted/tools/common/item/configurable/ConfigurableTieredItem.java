package com.grim3212.assorted.tools.common.item.configurable;

import com.grim3212.assorted.tools.api.item.ITiered;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.world.item.Item;

/**
 * An item that carries a configured tool material but no tool behaviour. Its stats come from
 * {@link ConfigurableTools#tiered}; the tier is kept for things vanilla has no concept of, such as
 * bucket capacity.
 */
public class ConfigurableTieredItem extends Item implements ITiered {

    private final ItemTierConfig tierHolder;

    public ConfigurableTieredItem(ItemTierConfig tierHolder, Properties builder) {
        super(ConfigurableTools.tiered(tierHolder, builder));
        this.tierHolder = tierHolder;
    }

    @Override
    public ItemTierConfig getTierHolder() {
        return tierHolder;
    }
}
