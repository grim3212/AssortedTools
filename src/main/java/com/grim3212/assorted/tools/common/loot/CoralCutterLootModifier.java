package com.grim3212.assorted.tools.common.loot;

import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

public class CoralCutterLootModifier extends LootModifier {

	public CoralCutterLootModifier(LootItemCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		ItemStack ctxTool = context.getParamOrNull(LootContextParams.TOOL);
		if (EnchantmentHelper.getEnchantments(ctxTool).containsKey(Enchantments.SILK_TOUCH))
			return generatedLoot;

		ItemStack fakeTool = ctxTool.copy();
		fakeTool.enchant(Enchantments.SILK_TOUCH, 1);
		LootContext.Builder builder = new LootContext.Builder(context);
		builder.withParameter(LootContextParams.TOOL, fakeTool);
		LootContext ctx = builder.create(LootContextParamSets.BLOCK);
		LootTable loottable = context.getLevel().getServer().getLootTables().get(context.getParamOrNull(LootContextParams.BLOCK_STATE).getBlock().getLootTable());
		return loottable.getRandomItems(ctx);
	}

	public static class Serializer extends GlobalLootModifierSerializer<CoralCutterLootModifier> {
		@Override
		public CoralCutterLootModifier read(ResourceLocation name, JsonObject json, LootItemCondition[] conditionsIn) {
			return new CoralCutterLootModifier(conditionsIn);
		}

		@Override
		public JsonObject write(CoralCutterLootModifier instance) {
			return makeConditions(instance.conditions);
		}
	}
}
