package com.grim3212.assorted.tools.services;

import com.grim3212.assorted.tools.common.item.FabricMultiToolItem;
import com.grim3212.assorted.tools.common.item.MultiToolItem;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.world.item.Item;

public class FabricToolsHelper implements IToolsHelper {
    @Override
    public MultiToolItem getMultiToolItem(ItemTierConfig tier, Item.Properties builderIn) {
        return new FabricMultiToolItem(tier, builderIn);
    }
}
