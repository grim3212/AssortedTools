package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.lib.util.NBTHelper;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.api.item.ISwitchModes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.function.Predicate;

/**
 * A staff with modes that the tool mode key cycles. The Neptune and Phoenix staffs also freeze or
 * thaw what they hit; see {@link #elemental}.
 */
public abstract class StaffItem extends Item implements ISwitchModes {

    public static final int DURABILITY = 500;
    /** How far the area modes of the Neptune and Phoenix staffs reach from the player. */
    public static final int RANGE = 8;

    private final StaffModeInfo.Kind kind;

    protected StaffItem(StaffModeInfo.Kind kind, Properties properties) {
        super(properties.component(ToolsDataComponents.STAFF_MODE_INFO.get(), new StaffModeInfo(kind)));
        this.kind = kind;
    }

    /**
     * The Neptune and Phoenix staffs: a hit freezes or thaws, for one durability, but does no more
     * damage than a bare hand. They are not a weapon.
     */
    protected static Properties elemental(Properties properties) {
        return properties.component(DataComponents.WEAPON, new Weapon(1));
    }

    public StaffMode getMode(ItemStack stack) {
        return StaffMode.fromString(this.kind, NBTHelper.getString(stack, StaffMode.KEY));
    }

    protected void setMode(ItemStack stack, StaffMode mode) {
        NBTHelper.putString(stack, StaffMode.KEY, mode.getSerializedName());
    }

    @Override
    public ItemStack cycleMode(Player player, ItemStack stack) {
        StaffMode next = this.getMode(stack).next();
        this.setMode(stack, next);
        if (!player.level().isClientSide()) {
            player.sendSystemMessage(Component.translatable(Constants.MOD_ID + ".staff.switched", next.getTranslatedString()));
        }
        return stack;
    }

    /** Every living entity but the user within {@link #RANGE} of them that {@code filter} accepts. */
    protected static List<LivingEntity> inRange(Level level, Player player, Predicate<LivingEntity> filter) {
        return level.getEntitiesOfClass(LivingEntity.class, new AABB(player.blockPosition()).inflate(RANGE), entity -> entity != player && entity.distanceToSqr(player) <= RANGE * RANGE && filter.test(entity));
    }

    /** Every position within {@link #RANGE} of the player, as a sphere. */
    protected static Iterable<BlockPos> sphere(Player player) {
        BlockPos center = player.blockPosition();
        return () -> BlockPos.betweenClosedStream(center.offset(-RANGE, -RANGE, -RANGE), center.offset(RANGE, RANGE, RANGE))
                .filter(pos -> pos.distSqr(center) <= RANGE * RANGE)
                .map(BlockPos::immutable)
                .iterator();
    }
}
