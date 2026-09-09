package com.grim3212.assorted.tools.common.enchantment;

import com.grim3212.assorted.tools.Constants;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

/**
 * Enchantments are a data driven registry now - {@link Enchantment} is a record loaded from JSON,
 * so there is nothing to subclass and nothing to register from code. All this class holds is the
 * {@link ResourceKey}s the rest of the mod looks enchantments up by; the JSON itself is written by
 * {@code com.grim3212.assorted.tools.data.ToolsEnchantmentData}.
 */
public class ToolsEnchantments {

    public static final ResourceKey<Enchantment> CHICKEN_JUMP = key("chicken_jump");
    public static final ResourceKey<Enchantment> BOUNCINESS = key("bounciness");
    public static final ResourceKey<Enchantment> CONDUCTIVE = key("conductive");
    public static final ResourceKey<Enchantment> FLAMMABLE = key("flammable");
    public static final ResourceKey<Enchantment> UNSTABLE = key("unstable");
    public static final ResourceKey<Enchantment> CORAL_CUTTER = key("coral_cutter");

    /**
     * What an enchantment may be applied to is the {@code supported_items} item {@link TagKey} of
     * its definition now, so the old {@code canEnchant} instanceof checks become these tags.
     */
    public static final TagKey<Item> SPEAR_ENCHANTABLE = itemTag("enchantable/spear");
    public static final TagKey<Item> SHEARS_ENCHANTABLE = itemTag("enchantable/shears");

    private static ResourceKey<Enchantment> key(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
    }

    private static TagKey<Item> itemTag(String name) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
    }

    public static int getConductivity(ItemStack stack) {
        return getLevel(stack, CONDUCTIVE);
    }

    public static boolean hasFlammable(ItemStack stack) {
        return getLevel(stack, FLAMMABLE) > 0;
    }

    public static int getInstability(ItemStack stack) {
        return getLevel(stack, UNSTABLE);
    }

    public static int getMaxBounces(ItemStack stack) {
        return getLevel(stack, BOUNCINESS);
    }

    public static boolean hasCoralCutter(ItemStack stack) {
        return getLevel(stack, CORAL_CUTTER) > 0;
    }

    public static boolean hasChickenJump(ItemStack stack) {
        return getLevel(stack, CHICKEN_JUMP) > 0;
    }

    /**
     * {@code EnchantmentHelper#getItemEnchantmentLevel} wants a {@code Holder<Enchantment>}, which
     * cannot be resolved from a static context without a registry lookup, so the stack's own
     * enchantment component is read directly instead.
     */
    public static int getLevel(ItemStack stack, ResourceKey<Enchantment> enchantment) {
        ItemEnchantments enchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

        for (Holder<Enchantment> holder : enchantments.keySet()) {
            if (holder.is(enchantment)) {
                return enchantments.getLevel(holder);
            }
        }

        return 0;
    }

    public static void init() {
    }
}
