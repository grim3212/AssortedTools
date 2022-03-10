package com.grim3212.assorted.tools.common.enchantment;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.item.BetterSpearItem;

import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ToolsEnchantmentTypes {

	public static final EnchantmentCategory SPEAR = EnchantmentCategory.create(AssortedTools.MODID + ":spear", (item) -> {
		return item instanceof BetterSpearItem;
	});
	
	public static final EnchantmentCategory SHEARS = EnchantmentCategory.create(AssortedTools.MODID + ":shears", (item) -> {
		return item instanceof ShearsItem;
	});
}
