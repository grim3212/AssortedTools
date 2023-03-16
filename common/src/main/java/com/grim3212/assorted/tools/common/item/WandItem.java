package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.util.NBTHelper;
import com.grim3212.assorted.tools.ToolsCommonMod;
import com.grim3212.assorted.tools.api.item.ISwitchModes;
import com.grim3212.assorted.tools.api.util.WandCoord3D;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public abstract class WandItem extends Item implements ISwitchModes {

    protected final boolean reinforced;
    protected Random rand;

    protected BlockState stateOrig = Blocks.AIR.defaultBlockState();
    protected BlockState stateClicked = Blocks.AIR.defaultBlockState();

    public WandItem(boolean reinforced, Properties properties) {
        super(properties);
        this.reinforced = reinforced;
        this.rand = new Random();
    }

    @Override
    public abstract void onCraftedBy(ItemStack stack, Level worldIn, Player playerIn);

    protected abstract boolean canBreak(Level worldIn, BlockPos pos, ItemStack stack);

    public ItemStack getNeededItem(Level world, BlockState state, Player player) {
        return Services.LEVEL_PROPERTIES.getCloneItemStack(state, null, world, BlockPos.ZERO, player);
    }

    public int getNeededCount(BlockState state) {
        if (state.getBlock() instanceof SlabBlock) {
            return 2;
        }
        return 1;
    }

    public boolean isSurface(BlockState state) {
        return (state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.GRASS || state.getBlock() == Blocks.STONE || state.getBlock() == Blocks.GRAVEL || state.getBlock() == Blocks.SANDSTONE || state.getBlock() == Blocks.SAND || state.getBlock() == Blocks.BEDROCK || state.getBlock() == Blocks.COAL_ORE || state.getBlock() == Blocks.IRON_ORE || state.getBlock() == Blocks.GOLD_ORE || state.getBlock() == Blocks.DIAMOND_ORE || state.getBlock() == Blocks.LAPIS_ORE);
    }

    protected abstract boolean isTooFar(int range, int maxDiff, int range2D, ItemStack stack);

    public boolean isTooFar(WandCoord3D a, WandCoord3D b, ItemStack stack) {
        if (ToolsCommonMod.COMMON_CONFIG.freeBuildMode.get()) {
            return a.getDistance(b) > 1500.0D;
        }
        return this.isTooFar((int) a.getDistance(b), 10, (int) a.getDistanceFlat(b), stack);
    }

    protected abstract boolean doEffect(Level world, Player entityplayer, InteractionHand hand, WandCoord3D start, WandCoord3D end, BlockState state);

    protected void sendMessage(Player player, Component message) {
        if (!player.level.isClientSide) {
            player.sendSystemMessage(message);
        }
    }

    protected void error(Player entityplayer, WandCoord3D p, String reason) {
        entityplayer.level.playSound(entityplayer, p.pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, (entityplayer.level.random.nextFloat() + 0.7F) / 2.0F, 0.5F + entityplayer.level.random.nextFloat() * 0.3F);
        sendMessage(entityplayer, Component.translatable("error.wand." + reason));
        particles(entityplayer.level, p.pos, 3);
    }

    protected abstract double[] getParticleColor();

    private void particles(Level world, WandCoord3D c, int effect) {
        particles(world, c.pos, effect);
    }

    protected void particles(Level world, BlockPos pos, int effect) {
        double d = 0.0625D;

        if (effect == 1) {
            world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
            return;
        }
        if (effect == 2) {
            world.addParticle(ParticleTypes.SPLASH, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
            return;
        }

        double R = 0.0D;
        double G = 0.0D;
        double B = 0.0D;

        if (effect == 0) {
            double[] color = getParticleColor();
            R = color[0];
            G = color[1];
            B = color[2];
        } else {
            R = 0.8D;
        }

        for (int l = 0; l < 6; l++) {
            double d1 = pos.getX() + this.rand.nextFloat();
            double d2 = pos.getY() + this.rand.nextFloat();
            double d3 = pos.getZ() + this.rand.nextFloat();

            if ((l == 0) && (!world.getBlockState(pos.above()).isSolidRender(world, pos))) {
                d2 = pos.getY() + 1 + d;
            }
            if ((l == 1) && (!world.getBlockState(pos.below()).isSolidRender(world, pos))) {
                d2 = pos.getY() + 0 - d;
            }
            if ((l == 2) && (!world.getBlockState(pos.south()).isSolidRender(world, pos))) {
                d3 = pos.getZ() + 1 + d;
            }
            if ((l == 3) && (!world.getBlockState(pos.north()).isSolidRender(world, pos))) {
                d3 = pos.getZ() + 0 - d;
            }
            if ((l == 4) && (!world.getBlockState(pos.east()).isSolidRender(world, pos))) {
                d1 = pos.getX() + 1 + d;
            }
            if ((l == 5) && (!world.getBlockState(pos.west()).isSolidRender(world, pos))) {
                d1 = pos.getX() + 0 - d;
            }
            if ((d1 < pos.getX()) || (d1 > pos.getX() + 1) || (d2 < 0.0D) || (d2 > pos.getY() + 1) || (d3 < pos.getZ()) || (d3 > pos.getZ() + 1))
                world.addParticle(DustParticleOptions.REDSTONE, d1, d2, d3, R, G, B);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level worldIn = context.getLevel();
        Player playerIn = context.getPlayer();
        InteractionHand hand = context.getHand();
        boolean isFree = ToolsCommonMod.COMMON_CONFIG.freeBuildMode.get() || playerIn.isCreative();

        this.stateOrig = worldIn.getBlockState(pos);
        BlockState state = this.stateOrig;

        if (state.getBlock() == Blocks.GRASS) {
            state = Blocks.DIRT.defaultBlockState();
        }

        WandCoord3D clicked_current = new WandCoord3D(pos, state);

        if (isIncompatible(state)) {
            error(playerIn, clicked_current, "cantbuild");
            return InteractionResult.SUCCESS;
        }

        ItemStack stack = playerIn.getItemInHand(hand);
        WandCoord3D start = WandCoord3D.getFromNBT(worldIn, stack.getTag(), "Start");

        if (start == null) {
            SoundType soundType = Services.LEVEL_PROPERTIES.getSoundType(worldIn, pos, null);
            worldIn.playSound((Player) null, pos, soundType.getBreakSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);

            this.stateClicked = state;
            clicked_current.writeToNBT(stack.getTag(), "Start");

            particles(worldIn, clicked_current, 0);

            NBTHelper.putBoolean(stack, "firstUse", false);
            return InteractionResult.SUCCESS;
        } else {

            if (NBTHelper.getBoolean(stack, "firstUse")) {
                NBTHelper.removeTag(stack, "Start");
                error(playerIn, clicked_current, "nostart");
                return InteractionResult.SUCCESS;
            }

            WandCoord3D.findEnds(start, clicked_current);

            if (isTooFar(start, clicked_current, stack)) {
                NBTHelper.removeTag(stack, "Start");
                error(playerIn, clicked_current, "toofar");
                return InteractionResult.SUCCESS;
            }

            boolean damage = this.doEffect(worldIn, playerIn, hand, start, clicked_current, state);

            if (damage) {
                NBTHelper.putBoolean(stack, "firstUse", true);
                if (!isFree) {
                    NBTHelper.removeTag(stack, "Start");
                    stack.hurtAndBreak(1, playerIn, (s) -> {
                        s.broadcastBreakEvent(hand);
                    });
                    return InteractionResult.SUCCESS;
                }
            }
        }

        // Clear out start if we somehow pop out one of the ifs above
        NBTHelper.removeTag(stack, "Start");
        return InteractionResult.SUCCESS;
    }

    protected boolean isIncompatible(BlockState state) {
        return false;
    }
}
