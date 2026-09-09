package com.grim3212.assorted.tools.common.item.configurable;

import com.grim3212.assorted.tools.api.item.ITiered;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.world.item.Item;

/**
 * A pickaxe built from a configured tool material.
 * <p>
 * There is no {@code PickaxeItem} to extend any more - a pickaxe is a plain {@link Item} whose
 * {@code minecraft:tool} component names {@code mineable/pickaxe}. {@code Properties#pickaxe} does
 * exactly that, so the mining speed, harvest level, durability, enchantability, repair material and
 * attack attributes this class used to compute by hand all come from the material record.
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
