package com.grim3212.assorted.tools.common.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

public class NBTHelper {

	public static boolean hasTag(ItemStack itemStack, String keyName) {
		return !itemStack.isEmpty() && itemStack.getTag() != null && itemStack.getTag().contains(keyName);
	}

	public static void removeTag(ItemStack itemStack, String keyName) {
		if (itemStack.getTag() != null) {
			itemStack.getTag().remove(keyName);
		}
	}

	/**
	 * Initializes the NBT Tag Compound for the given ItemStack if it is null
	 * 
	 * @param itemStack The ItemStack for which its NBT Tag Compound is being
	 *                  checked for initialization
	 */
	private static void initCompoundNBT(ItemStack itemStack) {
		if (itemStack.getTag() == null) {
			itemStack.setTag(new CompoundNBT());
		}
	}

	private static void initCompoundNBT(CompoundNBT compound) {
		if (compound == null) {
			compound = new CompoundNBT();
		}
	}

	public static CompoundNBT getTag(CompoundNBT compound, String keyName) {
		if (compound == null || !compound.contains(keyName)) {
			return new CompoundNBT();
		}

		return compound.getCompound(keyName);
	}

	public static CompoundNBT getTag(ItemStack stack, String keyName) {
		initCompoundNBT(stack);

		if (!stack.getTag().contains(keyName)) {
			putTag(stack, keyName, new CompoundNBT());
		}

		return stack.getTag().getCompound(keyName);
	}

	public static void putTag(ItemStack stack, String keyName, CompoundNBT compound) {
		initCompoundNBT(stack);

		stack.getTag().put(keyName, compound);
	}

	// =============== STRING ===============
	public static String getString(CompoundNBT compound, String keyName) {
		initCompoundNBT(compound);

		if (compound == null || !compound.contains(keyName)) {
			putString(compound, keyName, "");
		}

		return compound.getString(keyName);
	}

	public static void putString(CompoundNBT compound, String keyName, String keyValue) {
		initCompoundNBT(compound);

		compound.putString(keyName, keyValue);
	}

	public static String getString(ItemStack itemStack, String keyName) {
		initCompoundNBT(itemStack);

		if (!itemStack.getTag().contains(keyName)) {
			putString(itemStack, keyName, "");
		}

		return itemStack.getTag().getString(keyName);
	}

	public static void putString(ItemStack itemStack, String keyName, String keyValue) {
		initCompoundNBT(itemStack);

		itemStack.getTag().putString(keyName, keyValue);
	}

	public static ItemStack putStringItemStack(ItemStack itemStack, String keyName, String keyValue) {
		initCompoundNBT(itemStack);

		itemStack.getTag().putString(keyName, keyValue);

		return itemStack;
	}
	// =============== END STRING ===============

	// =============== BOOLEAN ===============
	public static boolean getBoolean(ItemStack itemStack, String keyName) {
		initCompoundNBT(itemStack);

		if (!itemStack.getTag().contains(keyName)) {
			putBoolean(itemStack, keyName, false);
		}

		return itemStack.getTag().getBoolean(keyName);
	}

	public static void putBoolean(ItemStack itemStack, String keyName, boolean keyValue) {
		initCompoundNBT(itemStack);

		itemStack.getTag().putBoolean(keyName, keyValue);
	}

	public static ItemStack putBooleanItemStack(ItemStack itemStack, String keyName, boolean keyValue) {
		initCompoundNBT(itemStack);

		itemStack.getTag().putBoolean(keyName, keyValue);

		return itemStack;
	}
	// =============== END BOOLEAN ===============

	// =============== BYTE ===============
	public static byte getByte(ItemStack itemStack, String keyName) {
		initCompoundNBT(itemStack);

		if (!itemStack.getTag().contains(keyName)) {
			putByte(itemStack, keyName, (byte) 0);
		}

		return itemStack.getTag().getByte(keyName);
	}

	public static void putByte(ItemStack itemStack, String keyName, byte keyValue) {
		initCompoundNBT(itemStack);

		itemStack.getTag().putByte(keyName, keyValue);
	}

	public static ItemStack putByteItemStack(ItemStack itemStack, String keyName, byte keyValue) {
		initCompoundNBT(itemStack);

		itemStack.getTag().putByte(keyName, keyValue);

		return itemStack;
	}
	// =============== END BYTE ===============

	// =============== SHORT ===============
	public static short getShort(ItemStack itemStack, String keyName) {
		initCompoundNBT(itemStack);

		if (!itemStack.getTag().contains(keyName)) {
			putShort(itemStack, keyName, (short) 0);
		}

		return itemStack.getTag().getShort(keyName);
	}

	public static void putShort(ItemStack itemStack, String keyName, short keyValue) {
		initCompoundNBT(itemStack);

		itemStack.getTag().putShort(keyName, keyValue);
	}

	public static ItemStack putShortItemStack(ItemStack itemStack, String keyName, short keyValue) {
		initCompoundNBT(itemStack);

		itemStack.getTag().putShort(keyName, keyValue);
		return itemStack;
	}
	// =============== END SHORT ===============

	// =============== INTEGER ===============
	public static int getInt(CompoundNBT compound, String keyName) {
		initCompoundNBT(compound);

		if (compound == null || !compound.contains(keyName)) {
			putInt(compound, keyName, 0);
		}

		return compound.getInt(keyName);
	}

	public static void putInt(CompoundNBT compound, String keyName, int keyValue) {
		initCompoundNBT(compound);

		compound.putInt(keyName, keyValue);
	}

	public static int getInt(ItemStack itemStack, String keyName) {
		initCompoundNBT(itemStack);

		if (!itemStack.getTag().contains(keyName)) {
			putInt(itemStack, keyName, 0);
		}

		return itemStack.getTag().getInt(keyName);
	}

	public static int getInt(ItemStack itemStack, String keyName, int fallback) {
		initCompoundNBT(itemStack);

		if (!itemStack.getTag().contains(keyName)) {
			putInt(itemStack, keyName, fallback);
		}

		return itemStack.getTag().getInt(keyName);
	}

	public static void putInt(ItemStack itemStack, String keyName, int keyValue) {
		initCompoundNBT(itemStack);

		itemStack.getTag().putInt(keyName, keyValue);
	}

	public static ItemStack putIntItemStack(ItemStack itemStack, String keyName, int keyValue) {
		initCompoundNBT(itemStack);

		itemStack.getTag().putInt(keyName, keyValue);

		return itemStack;
	}
	// =============== END INTEGER ===============

	// =============== LONG ===============
	public static long getLong(ItemStack itemStack, String keyName) {
		initCompoundNBT(itemStack);

		if (!itemStack.getTag().contains(keyName)) {
			putLong(itemStack, keyName, 0);
		}

		return itemStack.getTag().getLong(keyName);
	}

	public static void putLong(ItemStack itemStack, String keyName, long keyValue) {
		initCompoundNBT(itemStack);

		itemStack.getTag().putLong(keyName, keyValue);
	}

	public static ItemStack putLongItemStack(ItemStack itemStack, String keyName, long keyValue) {
		initCompoundNBT(itemStack);

		itemStack.getTag().putLong(keyName, keyValue);

		return itemStack;
	}
	// =============== END LONG ===============

	// =============== FLOAT ===============
	public static float getFloat(ItemStack itemStack, String keyName) {
		initCompoundNBT(itemStack);

		if (!itemStack.getTag().contains(keyName)) {
			putFloat(itemStack, keyName, 0);
		}

		return itemStack.getTag().getFloat(keyName);
	}

	public static void putFloat(ItemStack itemStack, String keyName, float keyValue) {
		initCompoundNBT(itemStack);

		itemStack.getTag().putFloat(keyName, keyValue);
	}

	public static ItemStack putFloatItemStack(ItemStack itemStack, String keyName, float keyValue) {
		initCompoundNBT(itemStack);

		itemStack.getTag().putFloat(keyName, keyValue);

		return itemStack;
	}
	// =============== END FLOAT ===============

	// =============== DOUBLE ===============
	public static double getDouble(ItemStack itemStack, String keyName) {
		initCompoundNBT(itemStack);

		if (!itemStack.getTag().contains(keyName)) {
			putDouble(itemStack, keyName, 0);
		}

		return itemStack.getTag().getDouble(keyName);
	}

	public static void putDouble(ItemStack itemStack, String keyName, double keyValue) {
		initCompoundNBT(itemStack);

		itemStack.getTag().putDouble(keyName, keyValue);
	}

	public static ItemStack putDoubleItemStack(ItemStack itemStack, String keyName, double keyValue) {
		initCompoundNBT(itemStack);

		itemStack.getTag().putDouble(keyName, keyValue);

		return itemStack;
	}
	// =============== END DOUBLE ===============

	// =============== BLOCK POS ===============
	public static BlockPos getBlockPos(ItemStack itemStack, String keyName) {
		initCompoundNBT(itemStack);

		if (!itemStack.getTag().contains(keyName)) {
			putBlockPos(itemStack, keyName, new BlockPos(0, 0, 0));
		}

		int[] pos = itemStack.getTag().getIntArray(keyName);
		return new BlockPos(pos[0], pos[1], pos[2]);
	}

	public static BlockPos getBlockPos(CompoundNBT tag, String keyName) {
		initCompoundNBT(tag);

		if (!tag.contains(keyName)) {
			putBlockPos(tag, keyName, new BlockPos(0, 0, 0));
		}

		int[] pos = tag.getIntArray(keyName);
		return new BlockPos(pos[0], pos[1], pos[2]);
	}

	public static void putBlockPos(CompoundNBT tag, String keyName, BlockPos keyValue) {
		initCompoundNBT(tag);

		tag.putIntArray(keyName, new int[] { keyValue.getX(), keyValue.getY(), keyValue.getZ() });
	}

	public static void putBlockPos(ItemStack itemStack, String keyName, BlockPos keyValue) {
		initCompoundNBT(itemStack);

		itemStack.getTag().putIntArray(keyName, new int[] { keyValue.getX(), keyValue.getY(), keyValue.getZ() });
	}

	public static ItemStack putBlockPosItemStack(ItemStack itemStack, String keyName, BlockPos keyValue) {
		initCompoundNBT(itemStack);

		itemStack.getTag().putIntArray(keyName, new int[] { keyValue.getX(), keyValue.getY(), keyValue.getZ() });

		return itemStack;
	}
	// =============== END BLOCK POS ===============
}