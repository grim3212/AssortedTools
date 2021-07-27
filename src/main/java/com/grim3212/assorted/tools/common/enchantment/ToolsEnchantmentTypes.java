package com.grim3212.assorted.tools.common.enchantment;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.item.BetterSpearItem;

import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ToolsEnchantmentTypes {

	public static final EnchantmentCategory SPEAR = EnchantmentCategory.create(AssortedTools.MODID + ":spear", (item) -> {
		return item instanceof BetterSpearItem;
	});
}
