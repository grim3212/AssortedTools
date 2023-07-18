package com.grim3212.assorted.tools.common.loot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.google.common.base.Suppliers;
import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

public abstract class UltimateFragmentLootModifier extends LootModifier {

	private final ResourceLocation extraLootTable;

	public UltimateFragmentLootModifier(LootItemCondition[] conditionsIn, ResourceLocation extraLootTable) {
		super(conditionsIn);
		this.extraLootTable = extraLootTable;
	}

	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		if (ToolsConfig.COMMON.ultimateFistEnabled.get()) {
			List<ItemStack> stacks = new ArrayList<>();
			LootPool.lootPool().add(LootTableReference.lootTableReference(extraLootTable).setWeight(1)).name(extraLootTable.getPath()).build().addRandomItems(stacks::add, context);

			generatedLoot.addAll(stacks);
		}
		return generatedLoot;
	}

	public static class OverworldUltimateFragmentLootModifier extends UltimateFragmentLootModifier {

		public static final Supplier<Codec<OverworldUltimateFragmentLootModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, OverworldUltimateFragmentLootModifier::new)));

		public OverworldUltimateFragmentLootModifier(LootItemCondition[] conditionsIn) {
			super(conditionsIn, new ResourceLocation(AssortedTools.MODID, "fragments_overworld_loot"));
		}

		@Override
		public Codec<? extends IGlobalLootModifier> codec() {
			return CODEC.get();
		}
	}

	public static class NetherUltimateFragmentLootModifier extends UltimateFragmentLootModifier {

		public static final Supplier<Codec<NetherUltimateFragmentLootModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, NetherUltimateFragmentLootModifier::new)));

		public NetherUltimateFragmentLootModifier(LootItemCondition[] conditionsIn) {
			super(conditionsIn, new ResourceLocation(AssortedTools.MODID, "fragments_nether_loot"));
		}

		@Override
		public Codec<? extends IGlobalLootModifier> codec() {
			return CODEC.get();
		}
	}

	public static class EndUltimateFragmentLootModifier extends UltimateFragmentLootModifier {

		public static final Supplier<Codec<EndUltimateFragmentLootModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, EndUltimateFragmentLootModifier::new)));

		public EndUltimateFragmentLootModifier(LootItemCondition[] conditionsIn) {
			super(conditionsIn, new ResourceLocation(AssortedTools.MODID, "fragments_end_loot"));
		}

		@Override
		public Codec<? extends IGlobalLootModifier> codec() {
			return CODEC.get();
		}
	}
}
