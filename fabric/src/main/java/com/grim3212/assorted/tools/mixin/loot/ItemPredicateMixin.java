package com.grim3212.assorted.tools.mixin.loot;

import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Set;

@Mixin(ItemPredicate.class)
public abstract class ItemPredicateMixin {

    @Shadow
    @Final
    private Set<Item> items;

    @ModifyVariable(method = "matches", at = @At("HEAD"), argsOnly = true)
    public ItemStack shearsMatchCheck(ItemStack stack) {
        if (items != null && items.contains(Items.SHEARS) && stack.is(LibCommonTags.Items.SHEARS)) {
            ItemStack itemStack = new ItemStack(Items.SHEARS);
            itemStack.setCount(stack.getCount());
            itemStack.setTag(stack.getOrCreateTag());
            return itemStack;
        }
        return stack;
    }
}
