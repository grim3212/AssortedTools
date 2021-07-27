package com.grim3212.assorted.tools.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class WandCoord3D {

	public BlockPos pos;
	public BlockState state;

	private WandCoord3D() {
		this(BlockPos.ZERO, Blocks.AIR.defaultBlockState());
	}

	public WandCoord3D(BlockPos pos, BlockState state) {
		this.pos = pos;
		this.state = state;
	}

	public WandCoord3D(WandCoord3D a) {
		this(a.pos, a.state);
	}

	public WandCoord3D copy() {
		return new WandCoord3D(this);
	}

	public int getArea(WandCoord3D b) {
		return Math.abs(pos.getX() - b.pos.getX() + 1) * Math.abs(pos.getY() - b.pos.getY() + 1) * Math.abs(pos.getZ() - b.pos.getZ() + 1);
	}

	public float getDistance(WandCoord3D b) {
		float d3 = pos.getX() - b.pos.getX();
		float d4 = pos.getY() - b.pos.getY();
		float d5 = pos.getZ() - b.pos.getZ();
		return Mth.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
	}

	public float getDistanceFlat(WandCoord3D b) {
		float d3 = pos.getX() - b.pos.getX();
		float d5 = pos.getZ() - b.pos.getZ();
		return Mth.sqrt(d3 * d3 + d5 * d5);
	}

	public int getFlatArea(WandCoord3D b) {
		return Math.abs(pos.getX() - b.pos.getX() + 1) * Math.abs(pos.getZ() - b.pos.getZ() + 1);
	}

	public void setPos(BlockPos pos) {
		this.pos = pos;
	}

	public void setTo(WandCoord3D a) {
		setPos(a.pos);
	}

	public void writeToNBT(CompoundTag compound, String key) {
		if (!compound.contains(key)) {
			compound.put(key, new CompoundTag());
		}
		CompoundTag coord = compound.getCompound(key);
		coord.putIntArray("Pos", new int[] { pos.getX(), pos.getY(), pos.getZ() });
		coord.put("BlockState", NbtUtils.writeBlockState(this.state));
	}

	public static void findEnds(WandCoord3D a, WandCoord3D b) {
		WandCoord3D n = new WandCoord3D();
		WandCoord3D m = new WandCoord3D();

		int nx = a.pos.getX() > b.pos.getX() ? b.pos.getX() : a.pos.getX();
		int ny = a.pos.getY() > b.pos.getY() ? b.pos.getY() : a.pos.getY();
		int nz = a.pos.getZ() > b.pos.getZ() ? b.pos.getZ() : a.pos.getZ();
		n.pos = new BlockPos(nx, ny, nz);

		int mx = a.pos.getX() < b.pos.getX() ? b.pos.getX() : a.pos.getX();
		int my = a.pos.getY() < b.pos.getY() ? b.pos.getY() : a.pos.getY();
		int mz = a.pos.getZ() < b.pos.getZ() ? b.pos.getZ() : a.pos.getZ();
		m.pos = new BlockPos(mx, my, mz);

		a.setTo(n);
		b.setTo(m);
	}

	public static int getArea(WandCoord3D a, WandCoord3D b) {
		return Math.abs(a.pos.getX() - b.pos.getX() + 1) * Math.abs(a.pos.getY() - b.pos.getY() + 1) * Math.abs(a.pos.getZ() - b.pos.getZ() + 1);
	}

	public static int getFlatArea(WandCoord3D a, WandCoord3D b) {
		return Math.abs(a.pos.getX() - b.pos.getX() + 1) * Math.abs(a.pos.getZ() - b.pos.getZ() + 1);
	}

	public static WandCoord3D getFromNBT(CompoundTag compound, String key) {
		if (compound.contains(key)) {
			CompoundTag nbt = compound.getCompound(key);
			if (nbt.contains("Pos")) {
				int[] coord = nbt.getIntArray("Pos");
				BlockState state = NbtUtils.readBlockState(nbt.getCompound("BlockState"));
				if (coord.length == 3) {
					return new WandCoord3D(new BlockPos(coord[0], coord[1], coord[2]), state);
				}
			}
		}
		return null;
	}
}
