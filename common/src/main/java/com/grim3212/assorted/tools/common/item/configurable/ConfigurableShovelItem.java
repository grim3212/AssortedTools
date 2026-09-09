package com.grim3212.assorted.tools.common.item.configurable;

import com.grim3212.assorted.tools.api.item.ITiered;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;

/**
 * A shovel built from a configured tool material. {@link ShovelItem} survives because it still
 * carries the path-making right click; everything numeric comes from the material record.
 */
public class ConfigurableShovelItem extends ShovelItem implements ITiered {

    private final ItemTierConfig tierHolder;

    public ConfigurableShovelItem(ItemTierConfig tierHolder, Item.Properties builder) {
        super(tierHolder.material(), ConfigurableTools.SHOVEL_DAMAGE, ConfigurableTools.SHOVEL_SPEED, builder);
        this.tierHolder = tierHolder;
    }

    @Override
    public ItemTierConfig getTierHolder() {
        return tierHolder;
    }
}
