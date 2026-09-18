package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.api.ToolsTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Pushes the clicked block one step away from the clicked face, or pulls it one step toward it. A
 * floating move leaves the block where it lands; a dropping move lets it fall from there. A player
 * standing on the block rides along.
 */
public class PowerStaffItem extends StaffItem {

    /**
     * The {@code custom_model_data} string the item model selects the pull texture on. Kept in step
     * with the mode, since a model cannot read {@code custom_data}.
     */
    public static final String PULL_MODEL = "pull";

    public PowerStaffItem(Properties properties) {
        super(StaffModeInfo.Kind.POWER, properties);
    }

    @Override
    protected void setMode(ItemStack stack, StaffMode mode) {
        super.setMode(stack, mode);
        stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), List.of(mode.pulls() ? PULL_MODEL : "push"), List.of()));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        StaffMode mode = this.getMode(context.getItemInHand());
        // A push moves the block away from the face that was clicked, a pull toward it.
        Direction direction = mode.pulls() ? context.getClickedFace() : context.getClickedFace().getOpposite();

        if (!canMove(level, pos, state, direction, mode.pulls())) {
            return InteractionResult.FAIL;
        }

        BlockPos target = pos.relative(direction);
        Player rider = riderOf(context.getPlayer(), pos);
        if (!level.getBlockState(target).isAir() || !level.isUnobstructed(rider, state.getCollisionShape(level, target, CollisionContext.empty()).move(target))) {
            return InteractionResult.FAIL;
        }

        if (!level.isClientSide()) {
            if (rider != null) {
                rider.teleportTo(rider.getX() + direction.getStepX(), rider.getY() + direction.getStepY(), rider.getZ() + direction.getStepZ());
            }

            // The water a waterlogged block held stays where it was rather than moving with it.
            BlockState moved = state.hasProperty(BlockStateProperties.WATERLOGGED) ? state.setValue(BlockStateProperties.WATERLOGGED, false) : state;
            level.setBlock(pos, state.getFluidState().createLegacyBlock(), Block.UPDATE_ALL);
            level.setBlock(target, moved, Block.UPDATE_ALL);

            // A falling block already falls by itself once it is placed.
            if (mode.drops() && !(moved.getBlock() instanceof FallingBlock) && FallingBlock.isFree(level.getBlockState(target.below()))) {
                FallingBlockEntity.fall(level, target, moved);
            }
        }

        return InteractionResult.SUCCESS;
    }

    /**
     * The piston rules decide what moves, with a pull treated as a sticky piston's retraction, so
     * glazed terracotta only goes one way. Air and {@code #assortedtools:power_staff_immovable} are
     * refused on top of that.
     */
    public static boolean canMove(Level level, BlockPos pos, BlockState state, Direction direction, boolean pulls) {
        if (state.isAir() || state.is(ToolsTags.Blocks.POWER_STAFF_IMMOVABLE)) {
            return false;
        }
        return PistonBaseBlock.isPushable(state, level, pos, direction, false, pulls ? direction.getOpposite() : direction);
    }

    private static @Nullable Player riderOf(@Nullable Player player, BlockPos pos) {
        return player != null && player.onGround() && player.blockPosition().below().equals(pos) ? player : null;
    }
}
