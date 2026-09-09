package com.grim3212.assorted.tools.common.item.configurable;

import com.grim3212.assorted.tools.api.item.ITiered;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;

/**
 * An axe built from a configured tool material. {@link AxeItem} survives for stripping, scraping
 * and waxing off.
 * <p>
 * Axes are the one shape whose attack damage and speed are per material rather than fixed, which is
 * why the tier configuration carries {@code axeDamage} and {@code axeSpeed} of its own.
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
