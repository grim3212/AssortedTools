package com.grim3212.assorted.tools.gametest;

import com.grim3212.assorted.lib.events.EntityInteractEvent;
import com.grim3212.assorted.lib.core.fluid.FluidInformation;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.tools.common.handlers.MilkingHandler;
import com.grim3212.assorted.tools.common.item.BetterBucketItem;
import com.grim3212.assorted.tools.common.item.BetterMilkBucketItem;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.tools.gametest.ToolsTestSupport.*;

/**
 * Better buckets and milk buckets: filling, emptying, milking, lava, names, drinking and dispensers.
 */
final class BucketTests {

    private BucketTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("better_bucket_fills_and_empties", BucketTests::betterBucketFillsAndEmpties);
        out.accept("better_bucket_milks_a_cow", BucketTests::betterBucketMilksACow);
        out.accept("uncrafted_better_bucket_milks_and_fills", BucketTests::uncraftedBetterBucketMilksAndFills);
        out.accept("better_bucket_picks_up_and_places_lava", BucketTests::betterBucketPicksUpAndPlacesLava);
        out.accept("better_bucket_name_shows_its_fluid", BucketTests::betterBucketNameShowsItsFluid);
        out.accept("milk_bucket_can_be_drunk", BucketTests::milkBucketCanBeDrunk);
        out.accept("dispenser_places_fluid_from_a_better_bucket", BucketTests::dispenserPlacesFluidFromABetterBucket);
    }

    /**
     * Two water sources picked up into one bucket, then put back down, through the item's own
     * {@code use} - so the whole chain runs: the POV raytrace, {@code FluidHelper}'s pickup and
     * placement, and the fluid tag inside {@code minecraft:custom_data} that replaced the per
     * loader NBT key.
     */
    private static void betterBucketFillsAndEmpties(GameTestHelper helper) {
        final BlockPos waterA = new BlockPos(2, 1, 2);
        final BlockPos waterB = new BlockPos(4, 1, 2);
        final BlockPos floorC = new BlockPos(6, 0, 2);
        final BlockPos floorD = new BlockPos(7, 0, 2);

        helper.setBlock(waterA, Blocks.WATER);
        helper.setBlock(waterB, Blocks.WATER);

        BetterBucketItem bucket = ToolsItems.DIAMOND_BUCKET.get();
        int oneBucket = BetterBucketItem.getBucketAmount();
        // The diamond tier is configured for sixteen buckets; anything above one proves the
        // capacity comes from the tier rather than from a single vanilla bucket.
        helper.assertValueEqual(bucket.getMaximumMillibuckets(), 16 * oneBucket, "diamond bucket capacity");

        ServerPlayer player = survivalPlayer(helper, bucket.getEmptyStack());

        useLookingDownAt(helper, player, waterA);
        useLookingDownAt(helper, player, waterB);

        ItemStack held = player.getItemInHand(InteractionHand.MAIN_HAND);
        helper.assertValueEqual(BetterBucketItem.getAmount(held), 2 * oneBucket, "millibuckets after two pickups");
        helper.assertValueEqual(BetterBucketItem.getFluid(held), "minecraft:water", "stored fluid name");
        helper.assertBlockPresent(Blocks.AIR, waterA);
        helper.assertBlockPresent(Blocks.AIR, waterB);

        useLookingDownAt(helper, player, floorC);
        useLookingDownAt(helper, player, floorD);

        helper.assertBlockPresent(Blocks.WATER, floorC.above());
        helper.assertBlockPresent(Blocks.WATER, floorD.above());
        helper.assertValueEqual(BetterBucketItem.getAmount(player.getItemInHand(InteractionHand.MAIN_HAND)), 0, "millibuckets after emptying twice");

        helper.succeed();
    }

    /**
     * Milking a cow, both halves: an empty bucket becomes the matching milk bucket, and milking
     * again with that milk bucket tops it up. The handler is called with the library event
     * directly because the two loaders raise that event from different call sites, and it is the
     * handler - not the plumbing - that this is about.
     */
    private static void betterBucketMilksACow(GameTestHelper helper) {
        Cow cow = helper.spawn(EntityTypes.COW, new BlockPos(4, 1, 4));

        // The gold tier holds four buckets and milks at level 0, which is where cows sit.
        BetterBucketItem bucket = ToolsItems.GOLD_BUCKET.get();
        ServerPlayer player = survivalPlayer(helper, bucket.getEmptyStack());
        int oneBucket = BetterBucketItem.getBucketAmount();

        EntityInteractEvent first = new EntityInteractEvent(player, InteractionHand.MAIN_HAND, cow);
        MilkingHandler.interact(first);

        helper.assertTrue(first.isCanceled(), "milking a cow with an empty bucket was not handled");
        ItemStack milk = player.getItemInHand(InteractionHand.MAIN_HAND);
        helper.assertTrue(milk.is(ToolsItems.GOLD_MILK_BUCKET.get()), "the bucket did not turn into its milk bucket");
        helper.assertValueEqual(BetterBucketItem.getAmount(milk), oneBucket, "milk held after one milking");

        EntityInteractEvent second = new EntityInteractEvent(player, InteractionHand.MAIN_HAND, cow);
        MilkingHandler.interact(second);

        helper.assertTrue(second.isCanceled(), "milking again into a part full milk bucket was not handled");
        helper.assertValueEqual(BetterBucketItem.getAmount(player.getItemInHand(InteractionHand.MAIN_HAND)), 2 * oneBucket, "milk held after two milkings");

        helper.succeed();
    }

    /**
     * A bucket that never went through {@code onCraftedBy} - one from the creative tab or a
     * command - is an empty bucket like any other. It used to carry no fluid name at all, which
     * matched neither the empty marker nor milk, so it could be neither milked nor filled. Filled
     * through the fluid abstraction, and milked through {@code Player#interactOn}, which is where
     * each loader raises the library's entity interact event.
     */
    private static void uncraftedBetterBucketMilksAndFills(GameTestHelper helper) {
        BetterBucketItem bucket = ToolsItems.GOLD_BUCKET.get();
        int oneBucket = BetterBucketItem.getBucketAmount();

        ItemStack fresh = new ItemStack(bucket);
        helper.assertValueEqual(BetterBucketItem.getFluid(fresh), BetterBucketItem.emptyMarker(), "fluid read off a never-crafted bucket");
        helper.assertTrue(BetterBucketItem.isEmptyOrContains(fresh, "milk"), "a never-crafted bucket does not count as empty");

        ItemStack filled = Services.FLUIDS.insertInto(fresh, new FluidInformation(Fluids.WATER, oneBucket));
        helper.assertValueEqual(BetterBucketItem.getFluid(filled), "minecraft:water", "fluid in a never-crafted bucket after filling");
        helper.assertValueEqual(BetterBucketItem.getAmount(filled), oneBucket, "water in a never-crafted bucket after filling");

        Cow cow = helper.spawn(EntityTypes.COW, new BlockPos(4, 1, 4));
        ServerPlayer player = survivalPlayer(helper, new ItemStack(bucket));
        player.interactOn(cow, InteractionHand.MAIN_HAND, Vec3.ZERO);
        ItemStack milk = player.getItemInHand(InteractionHand.MAIN_HAND);
        helper.assertTrue(milk.is(ToolsItems.GOLD_MILK_BUCKET.get()), "milking a cow with a never-crafted bucket left " + milk);
        helper.assertValueEqual(BetterBucketItem.getAmount(milk), oneBucket, "milk held after milking with a never-crafted bucket");

        helper.succeed();
    }

    /** The lava half of the bucket; water is covered by {@code better_bucket_fills_and_empties}. */
    private static void betterBucketPicksUpAndPlacesLava(GameTestHelper helper) {
        final BlockPos lava = new BlockPos(2, 1, 2);
        final BlockPos floor = new BlockPos(6, 0, 2);

        helper.setBlock(lava, Blocks.LAVA);

        BetterBucketItem bucket = ToolsItems.DIAMOND_BUCKET.get();
        ServerPlayer player = survivalPlayer(helper, bucket.getEmptyStack());

        useLookingDownAt(helper, player, lava);

        ItemStack held = player.getItemInHand(InteractionHand.MAIN_HAND);
        helper.assertValueEqual(BetterBucketItem.getAmount(held), BetterBucketItem.getBucketAmount(), "millibuckets after picking up lava");
        helper.assertValueEqual(BetterBucketItem.getFluid(held), "minecraft:lava", "stored fluid name");
        helper.assertBlockPresent(Blocks.AIR, lava);

        useLookingDownAt(helper, player, floor);

        helper.assertBlockPresent(Blocks.LAVA, floor.above());
        helper.assertValueEqual(BetterBucketItem.getAmount(player.getItemInHand(InteractionHand.MAIN_HAND)), 0, "millibuckets after emptying");

        helper.succeed();
    }

    /** A milk bucket is drunk, clears effects and comes back one bucket lighter. */
    private static void milkBucketCanBeDrunk(GameTestHelper helper) {
        BetterMilkBucketItem milkBucket = ToolsItems.GOLD_MILK_BUCKET.get();
        int oneBucket = BetterBucketItem.getBucketAmount();

        ItemStack milk = new ItemStack(milkBucket);
        BetterBucketItem.store(milk, "milk", 2 * oneBucket);

        ServerPlayer player = survivalPlayer(helper, milk);
        stand(helper, player, new BlockPos(4, 1, 4));
        player.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 200));
        helper.assertTrue(player.hasEffect(MobEffects.SLOWNESS), "the test player never got the effect it is meant to drink away");

        helper.assertTrue(milkBucket.getUseAnimation(milk) == ItemUseAnimation.DRINK, "a milk bucket should be drunk, not eaten");
        helper.assertTrue(milk.use(helper.getLevel(), player, InteractionHand.MAIN_HAND).consumesAction(), "the milk bucket refused to be used");
        helper.assertTrue(player.isUsingItem(), "using a milk bucket did not start the drink");

        ItemStack left = milk.finishUsingItem(helper.getLevel(), player);

        helper.assertFalse(player.hasEffect(MobEffects.SLOWNESS), "drinking milk did not clear the effect");
        helper.assertTrue(left.is(milkBucket), "drinking one bucket of milk should leave the milk bucket behind");
        helper.assertValueEqual(BetterBucketItem.getAmount(left), oneBucket, "milk left after one drink");

        helper.succeed();
    }

    /** A filled bucket is named after what is in it. The model half stays manual. */
    private static void betterBucketNameShowsItsFluid(GameTestHelper helper) {
        BetterBucketItem bucket = ToolsItems.DIAMOND_BUCKET.get();

        ItemStack empty = bucket.getEmptyStack();
        helper.assertValueEqual(translationKey(empty.getHoverName()), "item.assortedtools.diamond_bucket", "the name of an empty bucket");

        ItemStack water = bucket.getEmptyStack();
        BetterBucketItem.storeFluid(water, Fluids.WATER, BetterBucketItem.getBucketAmount());
        helper.assertValueEqual(translationKey(water.getHoverName()), "item.assortedtools.diamond_bucket_filled", "the name of a water filled bucket");

        ItemStack lava = bucket.getEmptyStack();
        BetterBucketItem.storeFluid(lava, Fluids.LAVA, BetterBucketItem.getBucketAmount());
        Component lavaName = lava.getHoverName();
        helper.assertValueEqual(translationKey(lavaName), "item.assortedtools.diamond_bucket_filled", "the name of a lava filled bucket");

        // The fluid is the argument the name is built around, so two fluids must not name the same.
        helper.assertFalse(water.getHoverName().equals(lavaName), "a water bucket and a lava bucket are named the same thing");

        helper.succeed();
    }

    /**
     * A dispenser drains a better bucket into the world through {@code DispenseBucketHandler}. The
     * handler is registered per bucket item from {@code BetterBucketItem}'s constructor, and
     * {@code DefaultDispenseItemBehavior#dispense} is final now, so {@code execute} being the hook
     * and {@code consumeWithRemainder} putting the drained bucket back are both new shapes.
     */
    private static void dispenserPlacesFluidFromABetterBucket(GameTestHelper helper) {
        final BlockPos dispenser = new BlockPos(4, 1, 4);
        final BlockPos front = new BlockPos(4, 2, 4);
        final BlockPos power = new BlockPos(4, 1, 3);

        BetterBucketItem bucket = ToolsItems.DIAMOND_BUCKET.get();
        int oneBucket = BetterBucketItem.getBucketAmount();

        helper.startSequence()
                .thenExecute(() -> {
                    // Facing up: the one facing that survives the structure being placed rotated.
                    helper.setBlock(dispenser, Blocks.DISPENSER.defaultBlockState().setValue(DispenserBlock.FACING, Direction.UP));

                    ItemStack filled = bucket.getEmptyStack();
                    BetterBucketItem.storeFluid(filled, Fluids.WATER, 2 * oneBucket);
                    helper.getBlockEntity(dispenser, DispenserBlockEntity.class).setItem(0, filled);

                    helper.setBlock(power, Blocks.REDSTONE_BLOCK);
                })
                // A powered dispenser schedules itself four ticks out; ten is comfortably past it.
                .thenExecuteAfter(10, () -> {
                    helper.assertBlockPresent(Blocks.WATER, front);

                    ItemStack left = helper.getBlockEntity(dispenser, DispenserBlockEntity.class).getItem(0);
                    helper.assertTrue(left.is(bucket), "the dispenser threw the bucket instead of keeping it");
                    // One bucket poured, one left: a dispenser that places without draining pours forever.
                    helper.assertValueEqual(BetterBucketItem.getAmount(left), oneBucket, "water left in the bucket after one dispense");
                })
                .thenSucceed();
    }
}
