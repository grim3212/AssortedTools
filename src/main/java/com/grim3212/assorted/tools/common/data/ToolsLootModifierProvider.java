package com.grim3212.assorted.tools.common.data;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.enchantment.ToolsEnchantments;
import com.grim3212.assorted.tools.common.loot.CoralCutterLootModifier;
import com.grim3212.assorted.tools.common.loot.LootItemBlockTagCondition;
import com.grim3212.assorted.tools.common.util.ToolsTags;

import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class ToolsLootModifierProvider extends GlobalLootModifierProvider {

	public ToolsLootModifierProvider(DataGenerator gen) {
		super(gen, AssortedTools.MODID);
	}

	@Override
	protected void start() {
		add("coral_cutter", 
				new CoralCutterLootModifier(
					new LootItemCondition[] { 
							MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(ToolsEnchantments.CORAL_CUTTER.get(), MinMaxBounds.Ints.atLeast(1)))).build(), 
							LootItemBlockTagCondition.isInTag(ToolsTags.Blocks.ALL_CORALS).build() 
					}));
	}
}
