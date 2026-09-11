package com.grim3212.assorted.tools.common.item.configurable;

import com.grim3212.assorted.tools.api.item.ITiered;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.world.item.Item;

/**
 * A sword built from a configured tool material. {@code Properties#sword} also adds the cobweb
 * mining rule and the blocking-disable weapon component.
 */
public class ConfigurableSwordItem extends Item implements ITiered {

    private final ItemTierConfig tierHolder;

    public ConfigurableSwordItem(ItemTierConfig tierHolder, Properties builder) {
        super(builder.sword(tierHolder.material(), ConfigurableTools.SWORD_DAMAGE, ConfigurableTools.SWORD_SPEED));
        this.tierHolder = tierHolder;
    }

    @Override
    public ItemTierConfig getTierHolder() {
        return tierHolder;
    }
}
