package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.lib.core.item.ItemDescription;
import com.grim3212.assorted.lib.core.item.LibDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

/**
 * A fragment of the ultimate fist. Its tooltip is a default {@link ItemDescription} component.
 */
public class FragmentItem extends Item {

    public static final String DESCRIPTION_KEY = "tooltip.ultimate.fragment";

    public FragmentItem(Item.Properties props) {
        super(props.component(LibDataComponents.DESCRIPTION.get(), new ItemDescription(Component.translatable(DESCRIPTION_KEY).withStyle(ChatFormatting.GRAY))));
    }

}
