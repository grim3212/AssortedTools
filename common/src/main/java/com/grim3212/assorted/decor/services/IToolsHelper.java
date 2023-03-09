package com.grim3212.assorted.decor.services;

import com.grim3212.assorted.decor.common.item.MultiToolItem;
import com.grim3212.assorted.decor.config.ItemTierConfig;
import net.minecraft.world.item.Item;

public interface IToolsHelper {

    MultiToolItem getMultiToolItem(ItemTierConfig tier, Item.Properties builderIn);
}
