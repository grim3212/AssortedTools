package com.grim3212.assorted.tools.services;

import com.grim3212.assorted.decor.common.item.MultiToolItem;
import com.grim3212.assorted.decor.config.ItemTierConfig;
import com.grim3212.assorted.decor.services.IToolsHelper;
import com.grim3212.assorted.tools.common.item.ForgeMultiToolItem;
import net.minecraft.world.item.Item;

public class ForgeToolsHelper implements IToolsHelper {
    @Override
    public MultiToolItem getMultiToolItem(ItemTierConfig tier, Item.Properties builderIn) {
        return new ForgeMultiToolItem(tier, builderIn);
    }
}
