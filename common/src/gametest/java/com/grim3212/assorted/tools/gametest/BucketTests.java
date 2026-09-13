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
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.lib.test.TestSupport.*;
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
        out.accept("fluid_ingredient_draws_a_filled_bucket", BucketTests::fluidIngredientDrawsAFilledBucket);
        out.accept("better_bucket_never_mixes_two_fluids", BucketTests::betterBucketNeverMixesTwoFluids);
        out.accept("dispenser_fills_a_bucket_and_leaves_what_it_cannot_take", BucketTests::dispenserFillsABucketAndLeavesWhatItCannotTake);
    }

    /**
     * A dispenser fills an empty bucket from the source in front of it, and pours into a source it
     * has no room for rather than swallowing it. {@code FluidHelper#tryPickupFluid} takes the source
     * block out of the world as it answers, so the handler has to know it has room before it picks
     * anything up.
     */
    private static void dispenserFillsABucketAndLeavesWhatItCannotTake(GameTestHelper helper) {
        final BlockPos fills = new BlockPos(2, 1, 2);
        final BlockPos refuses = new BlockPos(6, 1, 6);

        BetterBucketItem bucket = ToolsItems.DIAMOND_BUCKET.get();
        int oneBucket = BetterBucketItem.getBucketAmount();
        int capacity = bucket.getMaximumMillibuckets();

        ItemStack full = bucket.getEmptyStack();
        BetterBucketItem.storeFluid(full, Fluids.WATER, capacity);

        helper.startSequence()
                .thenExecute(() -> {
                    loadedDispenser(helper, fills, bucket.getEmptyStack());
                    loadedDispenser(helper, refuses, full);
                })
                // A powered dispenser schedules itself four ticks out.
                .thenExecuteAfter(10, () -> {
                    helper.assertBlockPresent(Blocks.AIR, fills.above());
                    ItemStack filled = helper.getBlockEntity(fills, DispenserBlockEntity.class).getItem(0);
                    helper.assertTrue(filled.is(bucket), "the dispenser threw the bucket instead of filling it");
                    helper.assertValueEqual(BetterBucketItem.getAmount(filled), oneBucket, "water in the bucket after one pickup");
                    helper.assertValueEqual(BetterBucketItem.getFluid(filled), "minecraft:water", "fluid stored by the dispenser");

                    helper.assertBlockPresent(Blocks.WATER, refuses.above());
                    ItemStack left = helper.getBlockEntity(refuses, DispenserBlockEntity.class).getItem(0);
                    helper.assertTrue(left.is(bucket), "the dispenser threw out a bucket that had no room");
                    helper.assertValueEqual(BetterBucketItem.getAmount(left), capacity - oneBucket, "water in a bucket that had no room and poured instead");
                })
                .thenSucceed();
    }

    /**
     * A dispenser facing up, holding {@code held}, with a walled-in water source above it and a
     * redstone block beside it. Facing up survives the structure being placed rotated; the walls
     * keep the source out of the next test's box.
     */
    private static void loadedDispenser(GameTestHelper helper, BlockPos dispenser, ItemStack held) {
        BlockPos front = dispenser.above();

        helper.setBlock(dispenser, Blocks.DISPENSER.defaultBlockState().setValue(DispenserBlock.FACING, Direction.UP));
        helper.setBlock(front.above(), Blocks.STONE);
        for (Direction side : Direction.Plane.HORIZONTAL) {
            helper.setBlock(front.relative(side), Blocks.STONE);
        }
        helper.setBlock(front, Blocks.WATER);

        helper.getBlockEntity(dispenser, DispenserBlockEntity.class).setItem(0, held);
        helper.setBlock(dispenser.north(), Blocks.REDSTONE_BLOCK);
    }

    /**
     * A bucket holds one fluid at a time, and the refusal has to happen before the pickup:
     * {@code FluidHelper#tryPickupFluid} removes the source block as it answers. Only the stored
     * fluid is asserted, because a click that is not a pickup falls through to the item's place
     * branch, which may legitimately empty the bucket into what was clicked.
     */
    private static void betterBucketNeverMixesTwoFluids(GameTestHelper helper) {
        final BlockPos water = new BlockPos(2, 1, 2);
        final BlockPos lava = new BlockPos(6, 1, 6);

        helper.setBlock(water, Blocks.WATER);
        helper.setBlock(lava, Blocks.LAVA);

        BetterBucketItem bucket = ToolsItems.DIAMOND_BUCKET.get();
        int oneBucket = BetterBucketItem.getBucketAmount();
        ServerPlayer player = survivalPlayer(helper, bucket.getEmptyStack());

        useLookingDownAt(helper, player, water);
        helper.assertValueEqual(BetterBucketItem.getFluid(player.getItemInHand(InteractionHand.MAIN_HAND)), "minecraft:water", "stored fluid after picking up water");
        helper.assertValueEqual(BetterBucketItem.getAmount(player.getItemInHand(InteractionHand.MAIN_HAND)), oneBucket, "millibuckets after picking up water");

        useLookingDownAt(helper, player, lava);

        ItemStack held = player.getItemInHand(InteractionHand.MAIN_HAND);
        helper.assertFalse(BetterBucketItem.getFluid(held).equals("minecraft:lava"), "a water filled bucket turned into a lava bucket when it was clicked on lava");
        helper.assertTrue(BetterBucketItem.getAmount(held) <= oneBucket, "a water filled bucket counted the lava it refused towards its own contents");

        helper.succeed();
    }

    /**
     * Two water sources picked up into one bucket and put back down through the item's own {@code
     * use}, so the raytrace, {@code FluidHelper} and the fluid kept in {@code custom_data} all run.
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
     * Milking a cow turns an empty bucket into milk, and milking again tops it up. The handler is
     * called directly because the two loaders raise the event from different call sites.
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
     * A bucket that never went through {@code onCraftedBy} (creative tab, command) still fills and
     * milks. Milking goes through {@code Player#interactOn}, where each loader raises the entity
     * interact event.
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
        helper.assertValueEqual(tooltipKey(empty.getHoverName()), "item.assortedtools.diamond_bucket", "the name of an empty bucket");

        ItemStack water = bucket.getEmptyStack();
        BetterBucketItem.storeFluid(water, Fluids.WATER, BetterBucketItem.getBucketAmount());
        helper.assertValueEqual(tooltipKey(water.getHoverName()), "item.assortedtools.diamond_bucket_filled", "the name of a water filled bucket");

        ItemStack lava = bucket.getEmptyStack();
        BetterBucketItem.storeFluid(lava, Fluids.LAVA, BetterBucketItem.getBucketAmount());
        Component lavaName = lava.getHoverName();
        helper.assertValueEqual(tooltipKey(lavaName), "item.assortedtools.diamond_bucket_filled", "the name of a lava filled bucket");

        // The fluid is the argument the name is built around, so two fluids must not name the same.
        helper.assertFalse(water.getHoverName().equals(lavaName), "a water bucket and a lava bucket are named the same thing");

        helper.succeed();
    }

    /**
     * What a recipe viewer draws for a fluid ingredient over water. A better bucket keeps its fluid in
     * components, so the ingredient has to collect and draw the <em>filled</em> stack: the bare item a
     * default display would name is an empty bucket, which the ingredient does not accept. The
     * library's own test covers vanilla buckets.
     */
    private static void fluidIngredientDrawsAFilledBucket(GameTestHelper helper) {
        Ingredient water = Services.INGREDIENTS.fluid(null, FluidTags.WATER, Services.FLUIDS.getBucketAmount());

        List<ItemStack> drawn = water.display().resolveForStacks(SlotDisplayContext.fromLevel(helper.getLevel()));
        List<ItemStack> buckets = drawn.stream().filter(stack -> stack.getItem() instanceof BetterBucketItem).toList();
        helper.assertFalse(buckets.isEmpty(), "a water fluid ingredient draws " + drawn + ", with no better bucket among them");

        for (ItemStack bucket : buckets) {
            helper.assertValueEqual(BetterBucketItem.getFluid(bucket), "minecraft:water", "the fluid a drawn " + bucket.getItem() + " holds");
            helper.assertTrue(water.test(bucket), "a water fluid ingredient draws a " + bucket.getItem() + " that it does not accept");
        }

        helper.succeed();
    }

    /**
     * A dispenser drains a better bucket into the world through {@code DispenseBucketHandler}, and
     * {@code consumeWithRemainder} puts the drained bucket back.
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
