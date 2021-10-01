package com.grim3212.assorted.tools.common.util;

import java.util.function.Supplier;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public enum BucketType {
	WOOD("wood", 1, 0, 1000f, () -> {
		return new ItemStack(Items.STICK, 2);
	}),
	STONE("stone", 1, 0, 5000f, () -> {
		return new ItemStack(Blocks.COBBLESTONE, 2);
	}),
	GOLD("gold", 4, 0),
	DIAMOND("diamond", 16, 1),
	OBSIDIAN("obsidian", 32, 2),
	NETHERITE("netherite", 64, 2, 10000f);

	private String registryName;
	private int maxBuckets;
	private int milkingLevel;
	private float maxPickupTemp;
	private Supplier<ItemStack> destroyedAfterUse;

	private BucketType(String registryName, int maxBuckets, int milkingLevel) {
		this(registryName, maxBuckets, milkingLevel, 5000f, () -> ItemStack.EMPTY);
	}

	private BucketType(String registryName, int maxBuckets, int milkingLevel, float maxPickupTemp) {
		this(registryName, maxBuckets, milkingLevel, maxPickupTemp, () -> ItemStack.EMPTY);
	}

	private BucketType(String registryName, int maxBuckets, int milkingLevel, float maxPickupTemp, Supplier<ItemStack> destroyedAfterUse) {
		this.registryName = registryName;
		this.maxBuckets = maxBuckets;
		this.milkingLevel = milkingLevel;
		this.maxPickupTemp = maxPickupTemp;
		this.destroyedAfterUse = destroyedAfterUse;
	}

	public String getRegistryName() {
		return registryName;
	}

	public int getMaxBuckets() {
		return maxBuckets;
	}

	public int getMilkingLevel() {
		return milkingLevel;
	}

	public float getMaxPickupTemp() {
		return maxPickupTemp;
	}

	public ItemStack getDestroyedAfterUse() {
		return destroyedAfterUse.get();
	}
}
