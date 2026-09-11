package com.grim3212.assorted.tools.common.item.configurable;

import com.grim3212.assorted.tools.api.item.ITiered;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.world.item.Item;

/**
 * A pickaxe built from a configured tool material. {@code Properties#pickaxe} applies the
 * material's speed, harvest level, durability and attributes.
 */
public class ConfigurablePickaxeItem extends Item implements ITiered {

    private final ItemTierConfig tierHolder;

    public ConfigurablePickaxeItem(ItemTierConfig tierHolder, Item.Properties builder) {
        super(builder.pickaxe(tierHolder.material(), ConfigurableTools.PICKAXE_DAMAGE, ConfigurableTools.PICKAXE_SPEED));
        this.tierHolder = tierHolder;
    }

    @Override
    public ItemTierConfig getTierHolder() {
        return tierHolder;
    }
}
