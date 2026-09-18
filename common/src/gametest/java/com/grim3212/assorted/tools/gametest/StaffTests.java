package com.grim3212.assorted.tools.gametest;

import com.grim3212.assorted.lib.util.NBTHelper;
import com.grim3212.assorted.tools.common.item.FrozenMobs;
import com.grim3212.assorted.tools.common.item.PowerStaffItem;
import com.grim3212.assorted.tools.common.item.StaffMode;
import com.grim3212.assorted.tools.common.item.ToolsDataComponents;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.grim3212.assorted.tools.common.network.ToolCycleModesPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.util.ProblemReporter;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.lib.test.TestSupport.*;

/**
 * The Neptune, Phoenix and power staffs: their modes, what each mode does, and freezing mobs.
 */
final class StaffTests {

    private StaffTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("staff_modes_cycle_and_show_in_the_tooltip", StaffTests::staffModesCycleAndShowInTheTooltip);
        out.accept("neptune_staff_freezes_and_phoenix_staff_thaws", StaffTests::neptuneStaffFreezesAndPhoenixStaffThaws);
        out.accept("frozen_mobs_stay_frozen_through_a_reload", StaffTests::frozenMobsStayFrozenThroughAReload);
        out.accept("burning_thaws_a_frozen_mob", StaffTests::burningThawsAFrozenMob);
        out.accept("neptune_staff_places_and_freezes_water", StaffTests::neptuneStaffPlacesAndFreezesWater);
        out.accept("phoenix_staff_places_lava_and_fire_and_melts_ice", StaffTests::phoenixStaffPlacesLavaAndFireAndMeltsIce);
        out.accept("power_staff_pushes_and_pulls_like_a_piston", StaffTests::powerStaffPushesAndPullsLikeAPiston);
        out.accept("power_staff_drop_mode_lets_the_block_fall", StaffTests::powerStaffDropModeLetsTheBlockFall);
    }

    /**
     * Cycling through the tool mode packet, as the key does. A fresh staff is in its first mode, and
     * the power staff keeps its model data in step with the push or pull it is set to.
     */
    private static void staffModesCycleAndShowInTheTooltip(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper, new ItemStack(ToolsItems.NEPTUNE_STAFF.get()));
        stand(helper, player, new BlockPos(4, 1, 4));

        helper.assertValueEqual(ToolsItems.NEPTUNE_STAFF.get().getMode(player.getMainHandItem()), StaffMode.PLACE_WATER, "a fresh Neptune staff's mode");
        helper.assertTrue(tooltipKeys(helper, player.getMainHandItem(), ToolsDataComponents.STAFF_MODE_INFO.get()).contains("assortedtools.staff.current"), "the staff tooltip has no mode line");

        for (StaffMode expected : List.of(StaffMode.FREEZE_MOBS, StaffMode.FREEZE_WATER, StaffMode.PLACE_WATER)) {
            ToolCycleModesPacket.handle(new ToolCycleModesPacket(InteractionHand.MAIN_HAND), player);
            helper.assertValueEqual(NBTHelper.getString(player.getMainHandItem(), StaffMode.KEY), expected.getSerializedName(), "the Neptune staff's mode after a cycle");
        }

        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ToolsItems.POWER_STAFF.get()));
        ToolCycleModesPacket.handle(new ToolCycleModesPacket(InteractionHand.MAIN_HAND), player);
        helper.assertValueEqual(ToolsItems.POWER_STAFF.get().getMode(player.getMainHandItem()), StaffMode.FLOAT_PULL, "the power staff's mode after a cycle");
        helper.assertValueEqual(player.getMainHandItem().getOrDefault(DataComponents.CUSTOM_MODEL_DATA, CustomModelData.EMPTY).getString(0), PowerStaffItem.PULL_MODEL, "the pull model flag");

        helper.succeed();
    }

    /**
     * Freezing is no AI plus the staff's entity tag, so a thaw wakes what the staff froze and leaves
     * a mob that was already still alone.
     */
    private static void neptuneStaffFreezesAndPhoenixStaffThaws(GameTestHelper helper) {
        Cow cow = helper.spawn(EntityTypes.COW, new BlockPos(4, 1, 5));
        Cow statue = helper.spawn(EntityTypes.COW, new BlockPos(6, 1, 5));
        statue.setNoAi(true);

        ItemStack neptune = new ItemStack(ToolsItems.NEPTUNE_STAFF.get());
        NBTHelper.putString(neptune, StaffMode.KEY, StaffMode.FREEZE_MOBS.getSerializedName());
        ServerPlayer player = survivalPlayer(helper, neptune);
        stand(helper, player, new BlockPos(4, 1, 3));

        helper.assertTrue(neptune.use(helper.getLevel(), player, InteractionHand.MAIN_HAND).consumesAction(), "Freeze Mobs found nothing to freeze");
        helper.assertTrue(FrozenMobs.isFrozen(cow) && cow.isNoAi() && cow.isSilent(), "the cow was not frozen");
        helper.assertFalse(FrozenMobs.isFrozen(statue), "a mob that already had no AI was frozen again");
        helper.assertFalse(FrozenMobs.isFrozen(player), "the player froze themselves");
        helper.assertValueEqual(player.getMainHandItem().getDamageValue(), 1, "durability spent freezing one mob");

        ItemStack phoenix = new ItemStack(ToolsItems.PHOENIX_STAFF.get());
        NBTHelper.putString(phoenix, StaffMode.KEY, StaffMode.THAW_MOBS.getSerializedName());
        player.setItemInHand(InteractionHand.MAIN_HAND, phoenix);
        helper.assertTrue(phoenix.use(helper.getLevel(), player, InteractionHand.MAIN_HAND).consumesAction(), "Thaw Mobs found nothing to thaw");
        helper.assertTrue(!FrozenMobs.isFrozen(cow) && !cow.isNoAi() && !cow.isSilent(), "the cow was not thawed");
        helper.assertTrue(statue.isNoAi(), "the thaw woke a mob the staff never froze");

        // The same again through hits rather than the area modes.
        ToolsItems.NEPTUNE_STAFF.get().hurtEnemy(neptune, cow, player);
        helper.assertTrue(FrozenMobs.isFrozen(cow), "a Neptune staff hit did not freeze the cow");
        ToolsItems.PHOENIX_STAFF.get().hurtEnemy(phoenix, cow, player);
        helper.assertFalse(FrozenMobs.isFrozen(cow), "a Phoenix staff hit did not thaw the cow");

        helper.succeed();
    }

    /** Fire thaws a frozen mob through each loader's tick hook, not only a Phoenix staff. */
    private static void burningThawsAFrozenMob(GameTestHelper helper) {
        Cow cow = helper.spawn(EntityTypes.COW, new BlockPos(4, 1, 4));
        helper.assertTrue(FrozenMobs.freeze(cow), "the cow did not freeze");
        cow.setRemainingFireTicks(100);

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(!FrozenMobs.isFrozen(cow) && !cow.isNoAi(), "the burning cow is still frozen"))
                .thenSucceed();
    }

    /** The frozen marker is the loader's data attachment now, so it has to save with the mob. */
    private static void frozenMobsStayFrozenThroughAReload(GameTestHelper helper) {
        Cow cow = helper.spawn(EntityTypes.COW, new BlockPos(4, 1, 4));
        FrozenMobs.freeze(cow);

        TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, helper.getLevel().registryAccess());
        cow.saveWithoutId(output);
        Cow reloaded = helper.spawn(EntityTypes.COW, new BlockPos(6, 1, 4));
        reloaded.load(TagValueInput.create(ProblemReporter.DISCARDING, helper.getLevel().registryAccess(), output.buildResult()));

        helper.assertTrue(FrozenMobs.isFrozen(reloaded) && reloaded.isNoAi(), "a frozen cow thawed when it was saved and loaded");
        helper.assertTrue(FrozenMobs.thaw(reloaded) && !FrozenMobs.isFrozen(reloaded), "a reloaded frozen cow could not be thawed");

        helper.succeed();
    }

    private static void neptuneStaffPlacesAndFreezesWater(GameTestHelper helper) {
        final BlockPos ground = new BlockPos(4, 1, 5);
        final BlockPos water = ground.above();
        helper.setBlock(ground, Blocks.STONE);

        ServerPlayer player = survivalPlayer(helper, new ItemStack(ToolsItems.NEPTUNE_STAFF.get()));
        stand(helper, player, new BlockPos(4, 1, 2));

        helper.assertTrue(useOnTopOf(helper, player, ground).consumesAction(), "Place Water did nothing");
        helper.assertTrue(helper.getLevel().getFluidState(helper.absolutePos(water)).isSourceOfType(Fluids.WATER), "no water source above the clicked block");
        helper.assertValueEqual(player.getMainHandItem().getDamageValue(), 10, "durability spent placing water");

        NBTHelper.putString(player.getMainHandItem(), StaffMode.KEY, StaffMode.FREEZE_WATER.getSerializedName());
        player.getMainHandItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertBlockPresent(Blocks.ICE, water);

        helper.succeed();
    }

    private static void phoenixStaffPlacesLavaAndFireAndMeltsIce(GameTestHelper helper) {
        final BlockPos lavaGround = new BlockPos(2, 1, 5);
        final BlockPos fireGround = new BlockPos(4, 1, 5);
        final BlockPos ice = new BlockPos(6, 1, 5);
        helper.setBlock(lavaGround, Blocks.STONE);
        helper.setBlock(fireGround, Blocks.STONE);
        helper.setBlock(ice, Blocks.ICE);

        ServerPlayer player = survivalPlayer(helper, new ItemStack(ToolsItems.PHOENIX_STAFF.get()));
        stand(helper, player, new BlockPos(4, 1, 2));

        useOnTopOf(helper, player, lavaGround);
        helper.assertTrue(helper.getLevel().getFluidState(helper.absolutePos(lavaGround.above())).isSourceOfType(Fluids.LAVA), "no lava source above the clicked block");

        NBTHelper.putString(player.getMainHandItem(), StaffMode.KEY, StaffMode.PLACE_FIRE.getSerializedName());
        useOnTopOf(helper, player, fireGround);
        helper.assertBlockPresent(Blocks.FIRE, fireGround.above());

        NBTHelper.putString(player.getMainHandItem(), StaffMode.KEY, StaffMode.MELT_ICE.getSerializedName());
        player.getMainHandItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertBlockPresent(Blocks.WATER, ice);

        helper.succeed();
    }

    /**
     * A push moves the block away from the clicked face and a pull toward it. Obsidian is refused
     * and glazed terracotta only pushes, as with a sticky piston.
     */
    private static void powerStaffPushesAndPullsLikeAPiston(GameTestHelper helper) {
        final BlockPos stone = new BlockPos(4, 2, 4);
        final BlockPos obsidian = new BlockPos(2, 2, 4);
        final BlockPos terracotta = new BlockPos(6, 2, 4);
        helper.setBlock(stone, Blocks.STONE);
        helper.setBlock(obsidian, Blocks.OBSIDIAN);
        helper.setBlock(terracotta, Blocks.GLAZED_TERRACOTTA.white());

        ServerPlayer player = survivalPlayer(helper, new ItemStack(ToolsItems.POWER_STAFF.get()));
        stand(helper, player, new BlockPos(4, 1, 1));

        helper.assertTrue(clickSide(helper, player, stone, Direction.NORTH).consumesAction(), "the push was refused");
        helper.assertBlockPresent(Blocks.AIR, stone);
        helper.assertBlockPresent(Blocks.STONE, stone.south());

        NBTHelper.putString(player.getMainHandItem(), StaffMode.KEY, StaffMode.FLOAT_PULL.getSerializedName());
        helper.assertTrue(clickSide(helper, player, stone.south(), Direction.NORTH).consumesAction(), "the pull was refused");
        helper.assertBlockPresent(Blocks.STONE, stone);
        helper.assertBlockPresent(Blocks.AIR, stone.south());

        helper.assertTrue(clickSide(helper, player, obsidian, Direction.NORTH) == InteractionResult.FAIL, "obsidian was pulled");
        helper.assertBlockPresent(Blocks.OBSIDIAN, obsidian);
        helper.assertTrue(clickSide(helper, player, terracotta, Direction.NORTH) == InteractionResult.FAIL, "glazed terracotta was pulled");

        NBTHelper.putString(player.getMainHandItem(), StaffMode.KEY, StaffMode.FLOAT_PUSH.getSerializedName());
        helper.assertTrue(clickSide(helper, player, terracotta, Direction.NORTH).consumesAction(), "glazed terracotta could not be pushed");
        helper.assertBlockPresent(Blocks.GLAZED_TERRACOTTA.white(), terracotta.south());

        helper.succeed();
    }

    /** A dropping push leaves the block over air, so it starts falling from where it landed. */
    private static void powerStaffDropModeLetsTheBlockFall(GameTestHelper helper) {
        final BlockPos stone = new BlockPos(4, 3, 4);
        helper.setBlock(stone, Blocks.STONE);

        ItemStack staff = new ItemStack(ToolsItems.POWER_STAFF.get());
        NBTHelper.putString(staff, StaffMode.KEY, StaffMode.DROP_PUSH.getSerializedName());
        ServerPlayer player = survivalPlayer(helper, staff);
        stand(helper, player, new BlockPos(4, 1, 1));

        clickSide(helper, player, stone, Direction.NORTH);
        helper.assertBlockPresent(Blocks.AIR, stone);
        helper.assertBlockPresent(Blocks.AIR, stone.south());
        helper.assertTrue(helper.getEntities(EntityTypes.FALLING_BLOCK).stream().anyMatch(falling -> falling.getBlockState().is(Blocks.STONE)), "the pushed stone is not falling");

        helper.succeed();
    }

    private static InteractionResult clickSide(GameTestHelper helper, ServerPlayer player, BlockPos rel, Direction face) {
        return player.getMainHandItem().useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, hitSide(helper.absolutePos(rel), face)));
    }
}
