package com.grim3212.assorted.tools.common.item.configurable;

import com.grim3212.assorted.tools.api.item.ITiered;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import com.grim3212.assorted.tools.config.ModdedItemTierConfig;
import net.minecraft.world.item.Item;

/**
 * A spear as vanilla makes them - a lunge weapon, not a thrown one - from a configured tool
 * material. {@code Properties#spear} builds every component vanilla's own spears carry (kinetic and
 * piercing weapon, attack range, minimum charge, the stab swing, the spear damage type, the
 * attribute modifiers) from the material plus the nine lunge values, which come from the
 * material's configuration like its other numbers ({@link ModdedItemTierConfig#getSpearStats}).
 */
public class ConfigurableSpearItem extends Item implements ITiered {

    private final ModdedItemTierConfig tierHolder;

    public ConfigurableSpearItem(ModdedItemTierConfig tierHolder, Properties props) {
        super(tierHolder.getSpearStats().apply(props, tierHolder.material()));
        this.tierHolder = tierHolder;
    }

    @Override
    public ItemTierConfig getTierHolder() {
        return tierHolder;
    }
}
