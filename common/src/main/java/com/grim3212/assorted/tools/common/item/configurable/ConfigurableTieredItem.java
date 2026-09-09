package com.grim3212.assorted.tools.common.item.configurable;

import com.grim3212.assorted.tools.api.item.ITiered;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.world.item.Item;

/**
 * An item that carries a configured tool material but none of the tool behaviour.
 * <p>
 * Everything this class used to override - max damage, enchantability, the repair check, and the
 * damage accessors it inherited from the library's {@code IItemExtraProperties} - is a data
 * component now, applied by {@link ConfigurableTools#tiered}. What is left is the tier reference
 * itself, which the mod reads for things vanilla has no concept of, such as bucket capacity.
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
