package com.grim3212.assorted.tools.common.enchantment;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.item.BetterSpearItem;

import net.minecraft.enchantment.EnchantmentType;

public class ToolsEnchantmentTypes {

	public static final EnchantmentType SPEAR = EnchantmentType.create(AssortedTools.MODID + ":spear", (item) -> {
		return item instanceof BetterSpearItem;
	});
}
