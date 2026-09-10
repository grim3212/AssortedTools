package com.grim3212.assorted.tools.gametest;

import com.mojang.authlib.GameProfile;
import com.grim3212.assorted.lib.events.EntityInteractEvent;
import com.grim3212.assorted.lib.util.NBTHelper;
import com.grim3212.assorted.tools.api.item.HarvestTiers;
import com.grim3212.assorted.tools.common.entity.PokeballEntity;
import com.grim3212.assorted.tools.common.handlers.MilkingHandler;
import com.grim3212.assorted.tools.common.item.BetterBucketItem;
import com.grim3212.assorted.tools.common.item.MultiToolItem;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.grim3212.assorted.tools.common.item.WandMiningItem;
import com.grim3212.assorted.tools.common.network.ToolCycleModesPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Automated in-world checks for AssortedTools.
 * <p>
 * The bodies live in common because the behaviour they check is common; each loader module only
 * registers them into {@code Registries.TEST_FUNCTION} through its own hook, and
 * {@code data/assortedtools/test_instance/*.json} pairs each one with the shared {@code test_box}
 * structure.
 * <p>
 * The port rewrote how this mod stores things on disk - the bucket's fluid, the wand's selection
 * anchor and the pokeball's captured entity all moved into {@code minecraft:custom_data} - replaced
 * numeric harvest levels with block tags, and deleted the mixins the multitool and the modded
 * shears used to lean on. None of that is visible to a compiler, so that is what these cover.
 * <p>
 * Manual checks that need a human are in {@code TESTING-CHECKLIST.md}.
 */
public final class ToolsGameTests {

    private ToolsGameTests() {
    }

    /** Every test in this mod, named once, so both loaders register the same set. */
    public static void forEach(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("better_bucket_fills_and_empties", ToolsGameTests::betterBucketFillsAndEmpties);
        out.accept("better_bucket_milks_a_cow", ToolsGameTests::betterBucketMilksACow);
        out.accept("hammer_breaks_and_wears", ToolsGameTests::hammerBreaksAndWears);
        out.accept("multitool_mines_every_tool_class", ToolsGameTests::multitoolMinesEveryToolClass);
        out.accept("multitool_strips_and_paths", ToolsGameTests::multitoolStripsAndPaths);
        out.accept("harvest_tier_gates_drops", ToolsGameTests::harvestTierGatesDrops);
        out.accept("ultimate_fist_breaks_obsidian", ToolsGameTests::ultimateFistBreaksObsidian);
        out.accept("shears_shear_a_sheep", ToolsGameTests::shearsShearASheep);
        out.accept("pokeball_captures_and_releases", ToolsGameTests::pokeballCapturesAndReleases);
        out.accept("mining_wand_cycles_and_mines", ToolsGameTests::miningWandCyclesAndMines);
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
     * The hammer breaks whatever it is swung at and then refuses the normal destroy path, which is
     * the shape {@code canAttackBlock} used to have and {@code canDestroyBlock} has now. Obsidian,
     * because a wooden hammer has no business mining it: the point is that the hammer decides and
     * not the tier.
     */
    private static void hammerBreaksAndWears(GameTestHelper helper) {
        final BlockPos target = new BlockPos(4, 1, 4);
        helper.setBlock(target, Blocks.OBSIDIAN);

        BlockState obsidian = Blocks.OBSIDIAN.defaultBlockState();
        ServerPlayer player = survivalPlayer(helper, new ItemStack(ToolsItems.WOOD_HAMMER.get()));
        stand(helper, player, new BlockPos(4, 1, 2));

        ItemStack hammer = player.getItemInHand(InteractionHand.MAIN_HAND);
        helper.assertTrue(hammer.isCorrectToolForDrops(obsidian), "a hammer should be the correct tool for anything");
        helper.assertValueEqual(hammer.getDestroySpeed(obsidian), 80.0F, "hammer destroy speed");

        boolean destroyed = player.gameMode.destroyBlock(helper.absolutePos(target));

        helper.assertFalse(destroyed, "the hammer should break the block itself and then abort the normal destroy path");
        helper.assertBlockPresent(Blocks.AIR, target);
        helper.assertValueEqual(player.getItemInHand(InteractionHand.MAIN_HAND).getDamageValue(), 1, "hammer durability spent");

        helper.succeed();
    }

    /**
     * One multitool mines pickaxe, axe and shovel blocks and drops all three, which is what the
     * {@code c:mineable/multitool} tag baked into its {@code minecraft:tool} component has to
     * deliver now that the item carries no tool class of its own.
     */
    private static void multitoolMinesEveryToolClass(GameTestHelper helper) {
        final BlockPos stone = new BlockPos(2, 1, 4);
        final BlockPos log = new BlockPos(4, 1, 4);
        final BlockPos dirt = new BlockPos(6, 1, 4);

        helper.setBlock(stone, Blocks.STONE);
        helper.setBlock(log, Blocks.OAK_LOG);
        helper.setBlock(dirt, Blocks.DIRT);

        MultiToolItem multitool = ToolsItems.DIAMOND_MULTITOOL.get();
        ServerPlayer player = survivalPlayer(helper, new ItemStack(multitool));
        stand(helper, player, new BlockPos(4, 1, 2));

        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        // Cobweb is the one block the multitool answers for by hand rather than through the tag.
        helper.assertValueEqual(stack.getDestroySpeed(Blocks.COBWEB.defaultBlockState()), 15.0F, "multitool speed on cobweb");
        float efficiency = multitool.getTierHolder().getEfficiency();
        helper.assertValueEqual(stack.getDestroySpeed(Blocks.STONE.defaultBlockState()), efficiency, "multitool speed on stone");
        helper.assertValueEqual(stack.getDestroySpeed(Blocks.OAK_LOG.defaultBlockState()), efficiency, "multitool speed on a log");
        helper.assertValueEqual(stack.getDestroySpeed(Blocks.DIRT.defaultBlockState()), efficiency, "multitool speed on dirt");

        helper.assertTrue(player.gameMode.destroyBlock(helper.absolutePos(stone)), "the multitool could not break stone");
        helper.assertTrue(player.gameMode.destroyBlock(helper.absolutePos(log)), "the multitool could not break a log");
        helper.assertTrue(player.gameMode.destroyBlock(helper.absolutePos(dirt)), "the multitool could not break dirt");

        helper.assertItemEntityPresent(Items.COBBLESTONE, stone, 2.0D);
        helper.assertItemEntityPresent(Items.OAK_LOG, log, 2.0D);
        helper.assertItemEntityPresent(Items.DIRT, dirt, 2.0D);

        helper.succeed();
    }

    /**
     * The multitool's right click behaviour, which is nine lines delegating to vanilla's own axe
     * and shovel now instead of two loader specific mixins reimplementing them. On NeoForge those
     * vanilla implementations refuse to act unless the stack declares the matching
     * {@code ItemAbility}, so a missing declaration shows up here as a block that did not change.
     */
    private static void multitoolStripsAndPaths(GameTestHelper helper) {
        final BlockPos log = new BlockPos(2, 1, 4);
        final BlockPos grass = new BlockPos(4, 1, 4);

        helper.setBlock(log, Blocks.OAK_LOG);
        helper.setBlock(grass, Blocks.GRASS_BLOCK);

        ServerPlayer player = survivalPlayer(helper, new ItemStack(ToolsItems.DIAMOND_MULTITOOL.get()));
        stand(helper, player, new BlockPos(3, 1, 2));

        helper.assertTrue(useOnTopOf(helper, player, log).consumesAction(), "the multitool did not act as an axe");
        helper.assertBlockPresent(Blocks.STRIPPED_OAK_LOG, log);

        helper.assertTrue(useOnTopOf(helper, player, grass).consumesAction(), "the multitool did not act as a shovel");
        helper.assertBlockPresent(Blocks.DIRT_PATH, grass);

        helper.succeed();
    }

    /**
     * Harvest level used to be an integer compared against the block's; it is a block tag baked
     * into the tool now. Both halves are pinned here: the mapping itself, and that a tier too low
     * really does lose the drops in world.
     */
    private static void harvestTierGatesDrops(GameTestHelper helper) {
        helper.assertTrue(HarvestTiers.incorrectBlocksForDrops(0) == BlockTags.INCORRECT_FOR_WOODEN_TOOL, "harvest level 0 should map to the wooden tag");
        helper.assertTrue(HarvestTiers.incorrectBlocksForDrops(1) == BlockTags.INCORRECT_FOR_STONE_TOOL, "harvest level 1 should map to the stone tag");
        helper.assertTrue(HarvestTiers.incorrectBlocksForDrops(2) == BlockTags.INCORRECT_FOR_IRON_TOOL, "harvest level 2 should map to the iron tag");
        helper.assertTrue(HarvestTiers.incorrectBlocksForDrops(3) == BlockTags.INCORRECT_FOR_DIAMOND_TOOL, "harvest level 3 should map to the diamond tag");
        // Everything above diamond collapses onto netherite: there is nothing stronger to map to.
        helper.assertTrue(HarvestTiers.incorrectBlocksForDrops(7) == BlockTags.INCORRECT_FOR_NETHERITE_TOOL, "harvest levels above 3 should map to the netherite tag");

        final BlockPos oreA = new BlockPos(1, 1, 6);
        final BlockPos oreB = new BlockPos(3, 1, 6);
        final BlockPos obsidianA = new BlockPos(5, 1, 6);
        final BlockPos obsidianB = new BlockPos(7, 1, 6);

        // Diamond ore, not iron: a stone tool mines iron ore perfectly well in vanilla, so iron ore
        // would not tell the two tiers apart.
        helper.setBlock(oreA, Blocks.DIAMOND_ORE);
        helper.setBlock(oreB, Blocks.DIAMOND_ORE);
        helper.setBlock(obsidianA, Blocks.OBSIDIAN);
        helper.setBlock(obsidianB, Blocks.OBSIDIAN);

        ServerPlayer player = survivalPlayer(helper, new ItemStack(ToolsItems.STONE_MULTITOOL.get()));
        stand(helper, player, new BlockPos(4, 1, 4));

        player.gameMode.destroyBlock(helper.absolutePos(oreA));
        helper.assertBlockPresent(Blocks.AIR, oreA);
        helper.assertItemEntityNotPresent(Items.DIAMOND, oreA, 1.5D);

        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ToolsItems.IRON_MULTITOOL.get()));
        player.gameMode.destroyBlock(helper.absolutePos(oreB));
        helper.assertItemEntityPresent(Items.DIAMOND, oreB, 1.5D);

        player.gameMode.destroyBlock(helper.absolutePos(obsidianA));
        helper.assertBlockPresent(Blocks.AIR, obsidianA);
        helper.assertItemEntityNotPresent(Items.OBSIDIAN, obsidianA, 1.5D);

        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ToolsItems.DIAMOND_MULTITOOL.get()));
        player.gameMode.destroyBlock(helper.absolutePos(obsidianB));
        helper.assertItemEntityPresent(Items.OBSIDIAN, obsidianB, 1.5D);

        helper.succeed();
    }

    /**
     * The ultimate fist mines anything and pays a durability point for it. It carries no
     * {@code minecraft:tool} component at all, so both of those are its own overrides rather than
     * anything the component system does for it.
     */
    private static void ultimateFistBreaksObsidian(GameTestHelper helper) {
        final BlockPos target = new BlockPos(4, 1, 4);
        helper.setBlock(target, Blocks.OBSIDIAN);

        ServerPlayer player = survivalPlayer(helper, new ItemStack(ToolsItems.ULTIMATE_FIST.get()));
        stand(helper, player, new BlockPos(4, 1, 2));

        ItemStack fist = player.getItemInHand(InteractionHand.MAIN_HAND);
        helper.assertTrue(fist.isCorrectToolForDrops(Blocks.OBSIDIAN.defaultBlockState()), "the ultimate fist should mine anything");
        helper.assertTrue(fist.getDestroySpeed(Blocks.OBSIDIAN.defaultBlockState()) > 1.0F, "the ultimate fist has no mining speed");

        helper.assertTrue(player.gameMode.destroyBlock(helper.absolutePos(target)), "the ultimate fist could not break obsidian");
        helper.assertItemEntityPresent(Items.OBSIDIAN, target, 2.0D);
        helper.assertValueEqual(player.getItemInHand(InteractionHand.MAIN_HAND).getDamageValue(), 1, "ultimate fist durability spent");

        helper.succeed();
    }

    /**
     * Modded shears shear a sheep. Vanilla dispatches shearing on {@code is(Items.SHEARS)}, so this
     * only works because {@code MaterialShears#interactLivingEntity} handles it: a NeoForge patch
     * on the superclass there, the mod's own fallback on Fabric, after the Fabric only mixin that
     * used to do it was deleted.
     */
    private static void shearsShearASheep(GameTestHelper helper) {
        final BlockPos where = new BlockPos(4, 1, 4);
        Sheep sheep = helper.spawn(EntityTypes.SHEEP, where);
        helper.assertTrue(sheep.readyForShearing(), "a freshly spawned sheep should be shearable");

        ServerPlayer player = survivalPlayer(helper, new ItemStack(ToolsItems.DIAMOND_SHEARS.get()));
        stand(helper, player, new BlockPos(4, 1, 2));

        InteractionResult result = player.getItemInHand(InteractionHand.MAIN_HAND).interactLivingEntity(player, sheep, InteractionHand.MAIN_HAND);

        helper.assertTrue(result != InteractionResult.PASS, "modded shears were ignored by the sheep");
        helper.assertFalse(sheep.readyForShearing(), "the sheep was not sheared");
        helper.assertValueEqual(player.getItemInHand(InteractionHand.MAIN_HAND).getDamageValue(), 1, "shears durability spent");

        boolean wool = helper.getEntities(EntityTypes.ITEM, where, 4.0D).stream().anyMatch(item -> item.getItem().is(ItemTags.WOOL));
        helper.assertTrue(wool, "shearing dropped no wool");

        helper.succeed();
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
     * The mining wand: cycling its mode through the packet handler, then the two click selection
     * that mines everything between the corners. The selection anchor moved out of the stack's bare
     * NBT into {@code minecraft:custom_data} during the port, which is what the {@code Start}
     * assertions here are watching.
     */
    private static void miningWandCyclesAndMines(GameTestHelper helper) {
        WandMiningItem wand = ToolsItems.MINING_WAND.get();
        ItemStack stack = new ItemStack(wand);

        ServerPlayer player = survivalPlayer(helper, stack);
        stand(helper, player, new BlockPos(3, 1, 2));
        wand.onCraftedBy(stack, player);
        helper.assertValueEqual(NBTHelper.getString(stack, "Mode"), "mineall", "the mode a freshly crafted mining wand starts in");

        ToolCycleModesPacket.handle(new ToolCycleModesPacket(InteractionHand.MAIN_HAND), player);
        helper.assertValueEqual(NBTHelper.getString(player.getItemInHand(InteractionHand.MAIN_HAND), "Mode"), "minedirt", "the mode after one cycle");

        // Back round to mineall: a plain mining wand only has the three non reinforced modes.
        ToolCycleModesPacket.handle(new ToolCycleModesPacket(InteractionHand.MAIN_HAND), player);
        ToolCycleModesPacket.handle(new ToolCycleModesPacket(InteractionHand.MAIN_HAND), player);
        helper.assertValueEqual(NBTHelper.getString(player.getItemInHand(InteractionHand.MAIN_HAND), "Mode"), "mineall", "the mode after cycling all the way round");

        final BlockPos start = new BlockPos(2, 1, 4);
        final BlockPos middle = new BlockPos(3, 1, 4);
        final BlockPos end = new BlockPos(4, 1, 4);
        helper.setBlock(start, Blocks.STONE);
        helper.setBlock(middle, Blocks.STONE);
        helper.setBlock(end, Blocks.STONE);

        useOnTopOf(helper, player, start);
        helper.assertTrue(NBTHelper.hasTag(player.getItemInHand(InteractionHand.MAIN_HAND), "Start"), "the first click did not record a selection start");

        useOnTopOf(helper, player, end);

        helper.assertBlockPresent(Blocks.AIR, start);
        helper.assertBlockPresent(Blocks.AIR, middle);
        helper.assertBlockPresent(Blocks.AIR, end);
        helper.assertItemEntityPresent(Items.COBBLESTONE, middle, 3.0D);
        helper.assertFalse(NBTHelper.hasTag(player.getItemInHand(InteractionHand.MAIN_HAND), "Start"), "the selection was not cleared after mining");
        helper.assertValueEqual(player.getItemInHand(InteractionHand.MAIN_HAND).getDamageValue(), 1, "wand durability spent");

        helper.succeed();
    }

    /**
     * A real, fully joined survival player, because none of {@code GameTestHelper}'s three mock
     * player factories is usable here: {@code makeMockServerPlayerInLevel} hard codes
     * {@code gameMode()} to CREATIVE, and creative changes the answer of nearly everything under
     * test (a bucket does not fill, a hammer does not break, a cow cannot be milked, a wand costs
     * nothing); the other two hand back a player that was never placed, so its {@code connection}
     * is null and anything that messages it - a wand announcing its new mode - throws.
     * <p>
     * This is what the deprecated in-level factory does, minus that game mode override.
     */
    private static ServerPlayer survivalPlayer(GameTestHelper helper, ItemStack held) {
        ServerLevel level = helper.getLevel();
        CommonListenerCookie cookie = CommonListenerCookie.createInitial(new GameProfile(UUID.randomUUID(), "assortedtools-test"), false);
        ServerPlayer player = new ServerPlayer(level.getServer(), level, cookie.gameProfile(), cookie.clientInformation());

        Connection connection = new Connection(PacketFlow.SERVERBOUND);
        new EmbeddedChannel(connection);
        level.getServer().getPlayerList().placeNewPlayer(connection, player, cookie);
        helper.runBeforeTestEnd(() -> level.getServer().getPlayerList().remove(player));

        player.setGameMode(GameType.SURVIVAL);
        helper.assertFalse(player.isCreative(), "the test player is in creative, which changes every path under test");
        player.setItemInHand(InteractionHand.MAIN_HAND, held);
        return player;
    }

    /** Stands the player on top of {@code rel}, which is inside reach of anything nearby. */
    private static void stand(GameTestHelper helper, ServerPlayer player, BlockPos rel) {
        Vec3 on = helper.absoluteVec(Vec3.atBottomCenterOf(rel.above()));
        player.snapTo(on.x, on.y, on.z, 0.0F, 0.0F);
    }

    /**
     * Hovers the player two blocks above {@code rel} looking straight down and uses the held item.
     * This is the only way to drive {@code BetterBucketItem#use}: it decides everything from a POV
     * raytrace rather than from a position it is handed.
     */
    private static void useLookingDownAt(GameTestHelper helper, ServerPlayer player, BlockPos rel) {
        Vec3 above = helper.absoluteVec(Vec3.atCenterOf(rel).add(0.0D, 2.0D, 0.0D));
        player.snapTo(above.x, above.y, above.z, 0.0F, 90.0F);
        player.getItemInHand(InteractionHand.MAIN_HAND).getItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
    }

    /** Right clicks the top face of {@code rel} with whatever is in the main hand. */
    private static InteractionResult useOnTopOf(GameTestHelper helper, ServerPlayer player, BlockPos rel) {
        BlockPos pos = helper.absolutePos(rel);
        BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(pos).add(0.0D, 0.5D, 0.0D), Direction.UP, pos, false);
        return player.getItemInHand(InteractionHand.MAIN_HAND).useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, hit));
    }

    private static void throwPokeball(GameTestHelper helper, ServerPlayer player, ItemStack ball, Vec3 at) {
        Vec3 from = helper.absoluteVec(new Vec3(4.5D, 1.8D, 1.5D));
        PokeballEntity thrown = new PokeballEntity(player, helper.getLevel(), ball);
        thrown.snapTo(from.x, from.y, from.z, 0.0F, 0.0F);

        Vec3 aim = at.subtract(from);
        thrown.shoot(aim.x, aim.y, aim.z, 1.0F, 0.0F);
        helper.getLevel().addFreshEntity(thrown);
    }

    private static List<ItemStack> filledPokeballs(GameTestHelper helper) {
        return helper.getEntities(EntityTypes.ITEM).stream()
                .map(ItemEntity::getItem)
                .filter(stack -> stack.is(ToolsItems.POKEBALL.get()) && NBTHelper.hasTag(stack, "StoredEntity"))
                .toList();
    }
}
