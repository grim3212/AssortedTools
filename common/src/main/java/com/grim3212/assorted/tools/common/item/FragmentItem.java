package com.grim3212.assorted.tools.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class FragmentItem extends Item {

    public FragmentItem(Item.Properties props) {
        super(props);
    }

    /**
     * {@code Item.appendHoverText} is marked deprecated in 26.x - tooltips are meant to come from
     * data components implementing {@code TooltipProvider} - but it is still the only per item
     * hook, and vanilla's own items (DiscFragmentItem, HangingEntityItem, SmithingTemplateItem)
     * still override it. The tooltip no longer takes a {@code Level} or a mutable list; it takes a
     * {@link Consumer} and the tooltip context.
     */
    @SuppressWarnings("deprecation")
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> texts, TooltipFlag flags) {
        texts.accept(Component.translatable("tooltip.ultimate.fragment").withStyle(ChatFormatting.GRAY));

        super.appendHoverText(stack, context, display, texts, flags);
    }

}
