package com.grim3212.assorted.tools.common.data;

import org.apache.commons.lang3.Validate;

import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.enchantment.ToolsEnchantments;
import com.grim3212.assorted.tools.common.loot.CoralCutterLootModifier;
import com.grim3212.assorted.tools.common.loot.LootItemBlockTagCondition;
import com.grim3212.assorted.tools.common.loot.UltimateFragmentLootModifier.EndUltimateFragmentLootModifier;
import com.grim3212.assorted.tools.common.loot.UltimateFragmentLootModifier.NetherUltimateFragmentLootModifier;
import com.grim3212.assorted.tools.common.loot.UltimateFragmentLootModifier.OverworldUltimateFragmentLootModifier;
import com.grim3212.assorted.tools.common.util.ToolsTags;

import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class ToolsLootModifierProvider extends GlobalLootModifierProvider {

	public ToolsLootModifierProvider(PackOutput output) {
		super(output, AssortedTools.MODID);
	}

	@Override
	protected void start() {
		add("coral_cutter", new CoralCutterLootModifier(new LootItemCondition[] { MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(ToolsEnchantments.CORAL_CUTTER.get(), MinMaxBounds.Ints.atLeast(1)))).build(), LootItemBlockTagCondition.isInTag(ToolsTags.Blocks.ALL_CORALS).build() }));

		add("overworld_ultimate_fragments", new OverworldUltimateFragmentLootModifier(new LootItemCondition[] { getList(new String[] { "stronghold_corridor", "stronghold_crossing", "stronghold_library", "woodland_mansion", "underwater_ruin_big", "underwater_ruin_small", "ancient_city" }) }));

		add("nether_ultimate_fragments", new NetherUltimateFragmentLootModifier(new LootItemCondition[] { getList(new String[] { "nether_bridge", "ruined_portal" }) }));

		add("end_ultimate_fragments", new EndUltimateFragmentLootModifier(new LootItemCondition[] { getList(new String[] { "end_city_treasure" }) }));
	}

	private LootItemCondition getList(String[] chests) {
		Validate.isTrue(chests.length > 0);
		LootItemCondition.Builder condition = null;
		for (String s : chests) {
			LootTableIdCondition.Builder b = LootTableIdCondition.builder(new ResourceLocation("chests/" + s));
			condition = condition == null ? b : condition.or(b);
		}
		return condition.build();
	}
}
