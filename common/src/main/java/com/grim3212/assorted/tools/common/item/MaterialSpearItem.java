package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.common.item.configurable.ConfigurableSpearItem;
import com.grim3212.assorted.tools.config.ModdedItemTierConfig;

/** The vanilla-style spear of an extra material. The throwing spear is {@link BetterSpearItem}. */
public class MaterialSpearItem extends ConfigurableSpearItem {

    public MaterialSpearItem(ModdedItemTierConfig tierHolder, Properties builder) {
        super(tierHolder, builder);
    }
}
