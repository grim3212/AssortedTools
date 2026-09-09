package com.grim3212.assorted.tools.common.item.configurable;

import com.grim3212.assorted.tools.api.item.ITiered;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.world.item.Item;

/**
 * A sword built from a configured tool material.
 * <p>
 * {@code SwordItem} is gone; a sword is a plain {@link Item} whose material is applied through
 * {@code Properties#sword}, which also sets the cobweb mining rule and the blocking-disable weapon
 * component that used to be hardcoded in the class.
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
