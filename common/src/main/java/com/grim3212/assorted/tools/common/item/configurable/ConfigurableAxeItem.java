package com.grim3212.assorted.tools.common.item.configurable;

import com.grim3212.assorted.tools.api.item.ITiered;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;

/**
 * An axe built from a configured tool material; {@link AxeItem} supplies stripping, scraping and
 * waxing off. Axe damage and speed are per material, hence {@code axeDamage} and {@code axeSpeed}.
 */
public class ConfigurableAxeItem extends AxeItem implements ITiered {

    private final ItemTierConfig tierHolder;

    public ConfigurableAxeItem(ItemTierConfig tierHolder, Item.Properties builder) {
        super(tierHolder.material(), tierHolder.getAxeDamage(), tierHolder.getAxeSpeed(), builder);
        this.tierHolder = tierHolder;
    }

    @Override
    public ItemTierConfig getTierHolder() {
        return tierHolder;
    }
}
