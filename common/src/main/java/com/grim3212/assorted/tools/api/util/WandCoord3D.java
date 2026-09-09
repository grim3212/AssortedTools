package com.grim3212.assorted.tools.api.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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
        // CompoundTag#getCompound returns an Optional now, so the old
        // "make sure the child exists, then mutate it in place" shape does not work.
        // The child is written whole instead, which is what every caller wanted anyway.
        CompoundTag coord = new CompoundTag();
        coord.putIntArray("Pos", new int[]{pos.getX(), pos.getY(), pos.getZ()});
        coord.put("BlockState", NbtUtils.writeBlockState(this.state));
        compound.put(key, coord);
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

    public static WandCoord3D getFromNBT(Level level, CompoundTag compound, String key) {
        CompoundTag nbt = compound.getCompound(key).orElse(null);
        if (nbt == null) {
            return null;
        }

        int[] coord = nbt.getIntArray("Pos").orElse(null);
        if (coord == null || coord.length != 3) {
            return null;
        }

        // Registry is a HolderLookup.RegistryLookup itself now, so BuiltInRegistries.BLOCK is
        // already the HolderGetter the old asLookup() call produced.
        HolderGetter<Block> holdergetter = level != null ? level.holderLookup(Registries.BLOCK) : BuiltInRegistries.BLOCK;

        BlockState state = NbtUtils.readBlockState(holdergetter, nbt.getCompoundOrEmpty("BlockState"));
        return new WandCoord3D(new BlockPos(coord[0], coord[1], coord[2]), state);
    }
}
