package com.grim3212.assorted.tools.common.item;

import com.google.common.collect.Lists;
import com.grim3212.assorted.lib.util.NBTHelper;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.ToolsCommonMod;
import com.grim3212.assorted.tools.api.ToolsTags;
import com.grim3212.assorted.tools.api.util.WandCoord3D;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class WandBreakingItem extends WandItem {

    public WandBreakingItem(boolean reinforced, Properties props) {
        super(reinforced, props);
    }

    protected static boolean isOre(BlockState state) {
        return state.is(ToolsTags.Blocks.DESTRUCTIVE_SPARED_BLOCKS);
    }

    // BlockStateBase.liquid() is deprecated with no replacement - it is a legacy block level flag -
    // but it is what vanilla itself asks to tell a fluid block from a waterlogged one, which is the
    // distinction this needs.
    @SuppressWarnings("deprecation")
    @Override
    protected boolean canBreak(Level worldIn, BlockPos pos, ItemStack stack) {
        BlockState state = worldIn.getBlockState(pos);

        switch (BreakingMode.fromString(NBTHelper.getString(stack, "Mode"))) {
            case BREAK_WEAK:
                return (state.canBeReplaced() || state.getPistonPushReaction() == PushReaction.DESTROY || state.liquid());
            case BREAK_ALL:
                return (state.getBlock() != Blocks.BEDROCK || ToolsCommonMod.COMMON_CONFIG.bedrockBreaking.get());
            case BREAK_XORES:
                return (state.getBlock() != Blocks.BEDROCK || ToolsCommonMod.COMMON_CONFIG.bedrockBreaking.get()) && !isOre(state);
        }
        return false;
    }

    @Override
    protected boolean isTooFar(int range, int maxDiff, int range2d, ItemStack stack) {
        return range - 250 > maxDiff;
    }

    @Override
    protected double[] getParticleColor() {
        return new double[]{0.5D, 0.5D, 0.5D};
    }

    @Override
    protected boolean doEffect(Level world, Player entityplayer, InteractionHand hand, WandCoord3D start, WandCoord3D end, BlockState state) {
        boolean damage = doBreaking(world, start, end, entityplayer, hand);
        if (damage)
            world.playSound((Player) null, end.pos, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 2.5F, 0.5F + world.getRandom().nextFloat() * 0.3F);
        return damage;
    }

    private boolean doBreaking(Level world, WandCoord3D start, WandCoord3D end, Player entityplayer, InteractionHand hand) {
        int cnt = 0;
        for (int X = start.pos.getX(); X <= end.pos.getX(); X++) {
            for (int Y = start.pos.getY(); Y <= end.pos.getY(); Y++) {
                for (int Z = start.pos.getZ(); Z <= end.pos.getZ(); Z++) {
                    BlockPos newPos = new BlockPos(X, Y, Z);
                    BlockState stateAt = world.getBlockState(newPos);
                    if ((stateAt.getBlock() != Blocks.AIR) && (canBreak(world, newPos, entityplayer.getItemInHand(hand)))) {
                        cnt++;
                    }
                }
            }
        }

        if (cnt == 0) {
            if (!world.isClientSide())
                error(entityplayer, end, "nowork");
            return false;
        }

        for (int X = start.pos.getX(); X <= end.pos.getX(); X++) {
            for (int Y = start.pos.getY(); Y <= end.pos.getY(); Y++) {
                for (int Z = start.pos.getZ(); Z <= end.pos.getZ(); Z++) {
                    BlockPos newPos = new BlockPos(X, Y, Z);
                    BlockState stateAt = world.getBlockState(newPos);

                    if (stateAt.getBlock() != Blocks.AIR) {
                        if (canBreak(world, newPos, entityplayer.getItemInHand(hand))) {
                            stateAt.getBlock().playerWillDestroy(world, newPos, stateAt, entityplayer);
                            world.setBlockAndUpdate(newPos, Blocks.AIR.defaultBlockState());
                            if (this.rand.nextInt(cnt / 50 + 1) == 0)
                                particles(world, newPos, 1);
                        }
                    }
                }
            }
        }

        return true;
    }

    @Override
    public ItemStack cycleMode(Player player, ItemStack stack) {
        BreakingMode mode = BreakingMode.fromString(NBTHelper.getString(stack, "Mode"));
        BreakingMode next = BreakingMode.getNext(mode, stack, reinforced);
        NBTHelper.putString(stack, "Mode", next.getSerializedName());
        this.sendMessage(player, Component.translatable(Constants.MOD_ID + ".wand.switched", next.getTranslatedString()));
        return stack;
    }

    /**
     * {@code Item.appendHoverText} is marked deprecated in 26.x - tooltips are meant to come from
     * data components implementing {@code TooltipProvider} - but it is still the only per item
     * hook, and vanilla's own items still override it.
     */
    @SuppressWarnings("deprecation")
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flagIn) {
        BreakingMode mode = BreakingMode.fromString(NBTHelper.getString(stack, "Mode"));
        if (mode != null)
            tooltip.accept(Component.translatable(Constants.MOD_ID + ".wand.current", mode.getTranslatedString()));
        else
            tooltip.accept(Component.translatable(Constants.MOD_ID + ".broken"));
    }

    @Override
    public void onCraftedBy(ItemStack stack, Player playerIn) {
        NBTHelper.putString(stack, "Mode", BreakingMode.BREAK_WEAK.getSerializedName());
    }

    public static enum BreakingMode implements StringRepresentable {
        BREAK_WEAK("breakweak", 0),
        BREAK_ALL("breakall", 1),
        BREAK_XORES("breakxores", 2);

        private final String name;
        private final int order;
        private final boolean reinforcedOnly;

        private BreakingMode(String name, int order) {
            this(name, order, false);
        }

        private BreakingMode(String name, int order, boolean reinforcedOnly) {
            this.name = name;
            this.order = order;
            this.reinforcedOnly = reinforcedOnly;
        }

        private static final List<BreakingMode> values = Lists.newArrayList(values()).stream().sorted(Comparator.comparingInt(BreakingMode::getOrder)).collect(Collectors.toList());
        private static final List<BreakingMode> notReinforced = values.stream().filter((mode) -> !mode.reinforcedOnly).sorted(Comparator.comparingInt(BreakingMode::getOrder)).collect(Collectors.toList());

        private static BreakingMode getNext(BreakingMode current, ItemStack stack, boolean reinforced) {
            if (reinforced) {
                int i = values.indexOf(current) + 1;
                if (i >= values.size()) {
                    i = 0;
                }
                return values.get(i);
            } else {
                int i = notReinforced.indexOf(current) + 1;
                if (i >= notReinforced.size()) {
                    i = 0;
                }
                return notReinforced.get(i);
            }
        }

        private static BreakingMode fromString(String type) {
            for (BreakingMode mode : BreakingMode.values) {
                if (mode.getSerializedName().equalsIgnoreCase(type)) {
                    return mode;
                }
            }
            return null;
        }

        private int getOrder() {
            return order;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        public Component getTranslatedString() {
            return Component.translatable(Constants.MOD_ID + ".wand.mode." + this.name);
        }
    }
}
