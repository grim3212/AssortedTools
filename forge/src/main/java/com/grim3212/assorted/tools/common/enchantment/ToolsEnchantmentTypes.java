package com.grim3212.assorted.tools.common.enchantment;

import com.grim3212.assorted.decor.Constants;
import com.grim3212.assorted.decor.common.item.BetterSpearItem;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ToolsEnchantmentTypes {

    public static final EnchantmentCategory SPEAR = EnchantmentCategory.create(Constants.MOD_ID + ":spear", (item) -> {
        return item instanceof BetterSpearItem;
    });

    public static final EnchantmentCategory SHEARS = EnchantmentCategory.create(Constants.MOD_ID + ":shears", (item) -> {
        return item instanceof ShearsItem;
    });
}
