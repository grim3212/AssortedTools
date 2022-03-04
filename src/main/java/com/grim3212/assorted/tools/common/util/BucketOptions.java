package com.grim3212.assorted.tools.common.util;

public class BucketOptions {
	public final int maxBuckets;
	public final int milkingLevel;
	public final float maxPickupTemp;
	public final boolean destroyedAfterUse;

	public BucketOptions(int maxBuckets, int milkingLevel) {
		this(maxBuckets, milkingLevel, 5000f);
	}

	public BucketOptions(int maxBuckets, int milkingLevel, float maxPickupTemp) {
		this(maxBuckets, milkingLevel, maxPickupTemp, false);
	}

	public BucketOptions(int maxBuckets, int milkingLevel, float maxPickupTemp, boolean destroyedAfterUse) {
		this.maxBuckets = maxBuckets;
		this.milkingLevel = milkingLevel;
		this.maxPickupTemp = maxPickupTemp;
		this.destroyedAfterUse = destroyedAfterUse;
	}
}
