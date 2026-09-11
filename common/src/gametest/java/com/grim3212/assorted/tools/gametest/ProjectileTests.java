package com.grim3212.assorted.tools.gametest;

import com.grim3212.assorted.lib.util.NBTHelper;
import com.grim3212.assorted.tools.common.entity.BetterSpearEntity;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.tools.gametest.ToolsTestSupport.*;

/**
 * Thrown things: the pokeball, spears and boomerangs.
 */
final class ProjectileTests {

    private ProjectileTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("pokeball_captures_and_releases", ProjectileTests::pokeballCapturesAndReleases);
        out.accept("spear_sticks_in_a_block_and_is_picked_up", ProjectileTests::spearSticksInABlockAndIsPickedUp);
        out.accept("spear_damages_a_mob", ProjectileTests::spearDamagesAMob);
        out.accept("boomerangs_fly_out_and_return", ProjectileTests::boomerangsFlyOutAndReturn);
    }

    /**
     * A pokeball captures a cow and a second throw releases it. The captured mob lives in the
     * ball's {@code custom_data} as an {@code Entity#save(ValueOutput)} tag - a different
     * serializer from the one the 1.20.1 ball wrote - so a round trip is the only way to know it
     * still reads back.
     */
    private static void pokeballCapturesAndReleases(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper, ItemStack.EMPTY);
        stand(helper, player, new BlockPos(1, 1, 1));

        Cow cow = helper.spawn(EntityTypes.COW, new BlockPos(4, 1, 6));
        throwPokeball(helper, player, new ItemStack(ToolsItems.POKEBALL.get()), cow.getBoundingBox().getCenter());

        helper.startSequence()
                .thenWaitUntil(() -> {
                    helper.assertEntityNotPresent(EntityTypes.COW);
                    helper.assertTrue(!filledPokeballs(helper).isEmpty(), "the pokeball dropped nothing after capturing");
                })
                .thenExecute(() -> {
                    ItemStack captured = filledPokeballs(helper).get(0).copy();
                    CompoundTag stored = NBTHelper.getTag(captured, "StoredEntity");
                    helper.assertValueEqual(stored.getStringOr("id", ""), "minecraft:cow", "the captured entity id");

                    helper.killAllEntitiesOfClass(ItemEntity.class);
                    // Aimed at the floor: a block hit is what releases, an entity hit is what captures.
                    throwPokeball(helper, player, captured, helper.absoluteVec(Vec3.atCenterOf(new BlockPos(4, 0, 6))));
                })
                .thenWaitUntil(() -> helper.assertEntityPresent(EntityTypes.COW))
                .thenSucceed();
    }

    /**
     * A spear thrown through the item's own {@code releaseUsing}, ticked by hand until it lands,
     * then picked back up. The pickup only works because {@code AbstractArrow#setOwner} promotes
     * {@code pickup} to ALLOWED for a player owner - the constructor leaves it DISALLOWED - so it
     * is worth pinning rather than assuming.
     */
    private static void spearSticksInABlockAndIsPickedUp(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper, new ItemStack(ToolsItems.IRON_SPEAR.get()));
        hover(helper, player, new Vec3(4.5D, 4.0D, 4.5D), 90.0F);

        BetterSpearEntity spear = throwHeldSpear(helper, player);

        // Straight down from four blocks up at 2.5 a tick: a handful of ticks, and a bounded loop
        // rather than physics the test is waiting on.
        boolean landed = tickUntil(spear, 40, () -> spear.shakeTime > 0);
        helper.assertTrue(landed, "the spear never stuck in the floor");
        helper.assertFalse(spear.isRemoved(), "the spear vanished instead of sticking");

        // shakeTime counts back down to zero before an arrow can be collected.
        tickUntil(spear, 20, () -> spear.shakeTime <= 0);
        spear.playerTouch(player);

        helper.assertTrue(spear.isRemoved(), "the spear was not collected");
        helper.assertValueEqual(countInInventory(player, ToolsItems.IRON_SPEAR.get()), 1, "spears in the inventory after picking it back up");

        helper.succeed();
    }

    /** The same throw, into a cow. */
    private static void spearDamagesAMob(GameTestHelper helper) {
        Cow cow = helper.spawn(EntityTypes.COW, new BlockPos(4, 1, 4));
        float before = cow.getHealth();

        ServerPlayer player = survivalPlayer(helper, new ItemStack(ToolsItems.IRON_SPEAR.get()));
        hover(helper, player, new Vec3(4.5D, 5.0D, 4.5D), 90.0F);

        BetterSpearEntity spear = throwHeldSpear(helper, player);
        boolean hit = tickUntil(spear, 40, () -> cow.getHealth() < before);

        helper.assertTrue(hit, "the spear did not damage the cow");
        helper.assertTrue(cow.isAlive(), "an iron spear should not kill a cow outright");

        helper.succeed();
    }

    /**
     * Both boomerangs fly out and come back, ending up in the thrower's inventory. Thrown straight
     * up on purpose: the flight is longer than the test box, and up is the one direction with
     * nothing in it whichever way the structure is rotated.
     */
    private static void boomerangsFlyOutAndReturn(GameTestHelper helper) {
        assertBoomerangReturns(helper, ToolsItems.WOOD_BOOMERANG.get());
        assertBoomerangReturns(helper, ToolsItems.DIAMOND_BOOMERANG.get());
        helper.succeed();
    }
}
