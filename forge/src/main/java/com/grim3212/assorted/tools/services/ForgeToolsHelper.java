package com.grim3212.assorted.tools.services;

import com.grim3212.assorted.tools.common.item.ForgeMultiToolItem;
import com.grim3212.assorted.tools.common.item.MultiToolItem;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.world.item.Item;

public class ForgeToolsHelper implements IToolsHelper {
    @Override
    public MultiToolItem getMultiToolItem(ItemTierConfig tier, Item.Properties builderIn) {
        return new ForgeMultiToolItem(tier, builderIn);
    }
}
