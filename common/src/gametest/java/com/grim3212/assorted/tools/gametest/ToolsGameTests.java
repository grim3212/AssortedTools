package com.grim3212.assorted.tools.gametest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.grim3212.assorted.lib.events.AnvilUpdatedEvent;
import com.grim3212.assorted.lib.events.EntityInteractEvent;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.lib.core.fluid.FluidInformation;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.util.NBTHelper;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.ToolsCommonMod;
import com.grim3212.assorted.tools.api.ToolsTags;
import com.grim3212.assorted.tools.api.item.HarvestTiers;
import com.grim3212.assorted.tools.common.enchantment.ToolsEnchantments;
import com.grim3212.assorted.tools.common.entity.BetterSpearEntity;
import com.grim3212.assorted.tools.common.entity.BoomerangEntity;
import com.grim3212.assorted.tools.common.entity.PokeballEntity;
import com.grim3212.assorted.tools.common.entity.ToolsEntities;
import com.grim3212.assorted.tools.common.handlers.ChickenSuitConversionHandler;
import com.grim3212.assorted.tools.common.handlers.MilkingHandler;
import com.grim3212.assorted.tools.common.handlers.ToolsCreativeItems;
import com.grim3212.assorted.tools.common.item.BetterBucketItem;
import com.grim3212.assorted.tools.common.item.BetterMilkBucketItem;
import com.grim3212.assorted.tools.common.item.BetterSpearItem;
import com.grim3212.assorted.tools.common.item.BoomerangItem;
import com.grim3212.assorted.tools.common.item.MultiToolItem;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.grim3212.assorted.tools.common.item.WandBreakingItem;
import com.grim3212.assorted.tools.common.item.WandBuildingItem;
import com.grim3212.assorted.tools.common.item.WandMiningItem;
import com.grim3212.assorted.tools.common.network.ToolCycleModesPacket;
import com.grim3212.assorted.tools.config.ArmorMaterialConfig;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
        out.accept("uncrafted_better_bucket_milks_and_fills", ToolsGameTests::uncraftedBetterBucketMilksAndFills);
        out.accept("hammer_breaks_and_wears", ToolsGameTests::hammerBreaksAndWears);
        out.accept("multitool_mines_every_tool_class", ToolsGameTests::multitoolMinesEveryToolClass);
        out.accept("multitool_strips_and_paths", ToolsGameTests::multitoolStripsAndPaths);
        out.accept("harvest_tier_gates_drops", ToolsGameTests::harvestTierGatesDrops);
        out.accept("ultimate_fist_breaks_obsidian", ToolsGameTests::ultimateFistBreaksObsidian);
        out.accept("shears_shear_a_sheep", ToolsGameTests::shearsShearASheep);
        out.accept("pokeball_captures_and_releases", ToolsGameTests::pokeballCapturesAndReleases);
        out.accept("mining_wand_cycles_and_mines", ToolsGameTests::miningWandCyclesAndMines);
        out.accept("every_material_tool_set_crafts", ToolsGameTests::everyMaterialToolSetCrafts);
        out.accept("every_material_armour_set_crafts", ToolsGameTests::everyMaterialArmourSetCrafts);
        out.accept("tools_mine_at_their_tier_speed", ToolsGameTests::toolsMineAtTheirTierSpeed);
        out.accept("tools_take_the_right_enchantments", ToolsGameTests::toolsTakeTheRightEnchantments);
        out.accept("tools_enchantments_are_obtainable", ToolsGameTests::toolsEnchantmentsAreObtainable);
        out.accept("spears_are_never_offered_riptide_or_channeling", ToolsGameTests::spearsAreNeverOfferedRiptideOrChanneling);
        out.accept("spear_anvil_rejects_riptide_and_channeling", ToolsGameTests::spearAnvilRejectsRiptideAndChanneling);
        out.accept("armour_equips_and_protects", ToolsGameTests::armourEquipsAndProtects);
        out.accept("shears_cut_leaves", ToolsGameTests::shearsCutLeaves);
        out.accept("shears_cut_coral_with_coral_cutter", ToolsGameTests::shearsCutCoralWithCoralCutter);
        out.accept("coral_cutter_covers_every_coral", ToolsGameTests::coralCutterCoversEveryCoral);
        out.accept("spear_sticks_in_a_block_and_is_picked_up", ToolsGameTests::spearSticksInABlockAndIsPickedUp);
        out.accept("spear_damages_a_mob", ToolsGameTests::spearDamagesAMob);
        out.accept("boomerangs_fly_out_and_return", ToolsGameTests::boomerangsFlyOutAndReturn);
        out.accept("better_bucket_picks_up_and_places_lava", ToolsGameTests::betterBucketPicksUpAndPlacesLava);
        out.accept("better_bucket_name_shows_its_fluid", ToolsGameTests::betterBucketNameShowsItsFluid);
        out.accept("milk_bucket_can_be_drunk", ToolsGameTests::milkBucketCanBeDrunk);
        out.accept("dispenser_places_fluid_from_a_better_bucket", ToolsGameTests::dispenserPlacesFluidFromABetterBucket);
        out.accept("building_wand_builds_and_consumes_blocks", ToolsGameTests::buildingWandBuildsAndConsumesBlocks);
        out.accept("breaking_wands_clear_their_modes", ToolsGameTests::breakingWandsClearTheirModes);
        out.accept("chicken_suit_converts_armour_in_an_anvil", ToolsGameTests::chickenSuitConvertsArmourInAnAnvil);
        out.accept("every_item_has_a_model_and_a_name", ToolsGameTests::everyItemHasAModelAndAName);
        out.accept("every_recipe_loads_or_is_conditioned_off", ToolsGameTests::everyRecipeLoadsOrIsConditionedOff);
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
     * Every extra material's five tool recipes, asked of the recipe manager itself rather than
     * enumerated by hand, so a family added to the configuration is covered the day it is added.
     * <p>
     * A family whose material tag has nothing in it is skipped, not failed: the recipe carries an
     * {@code item_tag_populated} condition, so without a mod supplying the ingot the recipe is
     * legitimately never loaded. Copper, amethyst and emerald come from the loaders themselves, so
     * there is always something to check.
     */
    private static void everyMaterialToolSetCrafts(GameTestHelper helper) {
        ItemStack rod = firstOf(LibCommonTags.Items.RODS_WOODEN);
        helper.assertFalse(rod.isEmpty(), "c:rods/wooden is empty, so no tool recipe could match");

        final ItemStack no = ItemStack.EMPTY;
        int families = 0;

        for (ToolsItems.MaterialGroup group : ToolsItems.MATERIAL_GROUPS.values()) {
            ItemStack x = firstOf(group.material);
            if (x.isEmpty()) {
                continue;
            }

            assertCrafts(helper, 3, 3, List.of(x, x, x, no, rod, no, no, rod, no), group.PICKAXE.get());
            assertCrafts(helper, 1, 3, List.of(x, rod, rod), group.SHOVEL.get());
            assertCrafts(helper, 2, 3, List.of(x, x, x, rod, no, rod), group.AXE.get());
            assertCrafts(helper, 2, 3, List.of(x, x, no, rod, no, rod), group.HOE.get());
            assertCrafts(helper, 1, 3, List.of(x, x, rod), group.SWORD.get());
            families++;
        }

        helper.assertTrue(families > 0, "not one material family had a populated material tag, so nothing was checked");
        helper.succeed();
    }

    /** The same sweep for armour: four pieces per material family. */
    private static void everyMaterialArmourSetCrafts(GameTestHelper helper) {
        final ItemStack no = ItemStack.EMPTY;
        int families = 0;

        for (ToolsItems.MaterialGroup group : ToolsItems.MATERIAL_GROUPS.values()) {
            ItemStack x = firstOf(group.material);
            if (x.isEmpty()) {
                continue;
            }

            assertCrafts(helper, 3, 2, List.of(x, x, x, x, no, x), group.HELMET.get());
            assertCrafts(helper, 3, 3, List.of(x, no, x, x, x, x, x, x, x), group.CHESTPLATE.get());
            assertCrafts(helper, 3, 3, List.of(x, x, x, x, no, x, x, no, x), group.LEGGINGS.get());
            assertCrafts(helper, 3, 2, List.of(x, no, x, x, no, x), group.BOOTS.get());
            families++;
        }

        helper.assertTrue(families > 0, "not one material family had a populated material tag, so nothing was checked");
        helper.succeed();
    }

    /**
     * Mining speed. The numbers themselves are configuration, so each tool is measured against its
     * own tier rather than against a constant, and the whole set is then checked to be ordered the
     * same way the configured efficiencies are - which is what catches a tool wired to the wrong
     * tier, the one mistake a per tool equality cannot see.
     */
    private static void toolsMineAtTheirTierSpeed(GameTestHelper helper) {
        final BlockState stone = Blocks.STONE.defaultBlockState();
        final BlockState dirt = Blocks.DIRT.defaultBlockState();
        final BlockState log = Blocks.OAK_LOG.defaultBlockState();
        final BlockState hay = Blocks.HAY_BLOCK.defaultBlockState();
        // Glass is in none of the mineable tags, so every one of these falls back to bare hands.
        final BlockState glass = Blocks.GLASS.defaultBlockState();

        List<ItemTierConfig> tiers = new ArrayList<>();
        List<Item> pickaxes = new ArrayList<>();

        for (ToolsItems.MaterialGroup group : ToolsItems.MATERIAL_GROUPS.values()) {
            float efficiency = group.tier.getEfficiency();
            String name = group.tier.getName();

            assertSpeed(helper, group.PICKAXE.get(), stone, efficiency, name + " pickaxe on stone");
            assertSpeed(helper, group.SHOVEL.get(), dirt, efficiency, name + " shovel on dirt");
            assertSpeed(helper, group.AXE.get(), log, efficiency, name + " axe on a log");
            assertSpeed(helper, group.HOE.get(), hay, efficiency, name + " hoe on a hay block");
            assertSpeed(helper, group.PICKAXE.get(), glass, 1.0F, name + " pickaxe on a block it does not mine");

            helper.assertTrue(efficiency > 1.0F, name + " is configured to mine no faster than a bare hand");

            tiers.add(group.tier);
            pickaxes.add(group.PICKAXE.get());
        }

        // Sorted by configured efficiency, the measured speeds have to come out sorted too. This is
        // what catches an item handed the wrong tier, which a per tool equality cannot see.
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < tiers.size(); i++) {
            order.add(i);
        }
        order.sort(Comparator.comparingDouble(i -> tiers.get(i).getEfficiency()));

        for (int i = 1; i < order.size(); i++) {
            ItemTierConfig slower = tiers.get(order.get(i - 1));
            ItemTierConfig faster = tiers.get(order.get(i));
            float slowSpeed = new ItemStack(pickaxes.get(order.get(i - 1))).getDestroySpeed(stone);
            float fastSpeed = new ItemStack(pickaxes.get(order.get(i))).getDestroySpeed(stone);
            helper.assertTrue(slowSpeed <= fastSpeed, slower.getName() + " mines faster than " + faster.getName() + ", the wrong way round for their configured efficiencies");
        }

        helper.succeed();
    }

    /**
     * What may be enchanted is a {@code #minecraft:enchantable/*} item tag now, and those tags are
     * opt-in: an item missing from them is unenchantable at a table with no error anywhere. Every
     * tool, weapon and armour piece in this mod was missing from all of them at one point during
     * the port, so this pins the whole set - and reports every gap at once, because fixing them one
     * failure at a time is a very slow loop.
     */
    private static void toolsTakeTheRightEnchantments(GameTestHelper helper) {
        List<String> missing = new ArrayList<>();

        expectMiningTool(missing, ToolsItems.WOOD_HAMMER.get(), ToolsItems.STONE_HAMMER.get(), ToolsItems.GOLD_HAMMER.get(), ToolsItems.IRON_HAMMER.get(), ToolsItems.DIAMOND_HAMMER.get(), ToolsItems.NETHERITE_HAMMER.get());
        expectMiningTool(missing, ToolsItems.WOODEN_MULTITOOL.get(), ToolsItems.STONE_MULTITOOL.get(), ToolsItems.GOLDEN_MULTITOOL.get(), ToolsItems.IRON_MULTITOOL.get(), ToolsItems.DIAMOND_MULTITOOL.get(), ToolsItems.NETHERITE_MULTITOOL.get());
        expectMeleeWeapon(missing, ToolsItems.WOOD_HAMMER.get(), ToolsItems.STONE_HAMMER.get(), ToolsItems.GOLD_HAMMER.get(), ToolsItems.IRON_HAMMER.get(), ToolsItems.DIAMOND_HAMMER.get(), ToolsItems.NETHERITE_HAMMER.get());
        expectMeleeWeapon(missing, ToolsItems.WOODEN_MULTITOOL.get(), ToolsItems.STONE_MULTITOOL.get(), ToolsItems.GOLDEN_MULTITOOL.get(), ToolsItems.IRON_MULTITOOL.get(), ToolsItems.DIAMOND_MULTITOOL.get(), ToolsItems.NETHERITE_MULTITOOL.get());
        expectMeleeWeapon(missing, ToolsItems.ULTIMATE_FIST.get());
        expectDurable(missing, ToolsItems.WOOD_SPEAR.get(), ToolsItems.STONE_SPEAR.get(), ToolsItems.GOLD_SPEAR.get(), ToolsItems.IRON_SPEAR.get(), ToolsItems.DIAMOND_SPEAR.get(), ToolsItems.NETHERITE_SPEAR.get());
        expectDurable(missing, ToolsItems.WOOD_SHEARS.get(), ToolsItems.STONE_SHEARS.get(), ToolsItems.GOLD_SHEARS.get(), ToolsItems.DIAMOND_SHEARS.get(), ToolsItems.NETHERITE_SHEARS.get());
        expectDurable(missing, ToolsItems.WOOD_BUCKET.get(), ToolsItems.STONE_BUCKET.get(), ToolsItems.GOLD_BUCKET.get(), ToolsItems.DIAMOND_BUCKET.get(), ToolsItems.NETHERITE_BUCKET.get());
        expectDurable(missing, ToolsItems.BUILDING_WAND.get(), ToolsItems.BREAKING_WAND.get(), ToolsItems.MINING_WAND.get(), ToolsItems.REINFORCED_BUILDING_WAND.get(), ToolsItems.REINFORCED_BREAKING_WAND.get(), ToolsItems.REINFORCED_MINING_WAND.get());
        expectArmour(missing, ItemTags.HEAD_ARMOR_ENCHANTABLE, ToolsItems.CHICKEN_SUIT_HELMET.get());
        expectArmour(missing, ItemTags.CHEST_ARMOR_ENCHANTABLE, ToolsItems.CHICKEN_SUIT_CHESTPLATE.get());
        expectArmour(missing, ItemTags.LEG_ARMOR_ENCHANTABLE, ToolsItems.CHICKEN_SUIT_LEGGINGS.get());
        expectArmour(missing, ItemTags.FOOT_ARMOR_ENCHANTABLE, ToolsItems.CHICKEN_SUIT_BOOTS.get());

        // The mod's own two tags, which are what the six modded enchantments name as their
        // supported items - the replacement for the deleted canEnchant overrides.
        expect(missing, ToolsEnchantments.SPEAR_ENCHANTABLE, ToolsItems.WOOD_SPEAR.get(), ToolsItems.NETHERITE_SPEAR.get());
        expect(missing, ToolsEnchantments.SHEARS_ENCHANTABLE, Items.SHEARS, ToolsItems.DIAMOND_SHEARS.get());

        for (ToolsItems.MaterialGroup group : ToolsItems.MATERIAL_GROUPS.values()) {
            expectMiningTool(missing, group.PICKAXE.get(), group.SHOVEL.get(), group.AXE.get(), group.HOE.get(), group.HAMMER.get(), group.MULTITOOL.get());
            expectMeleeWeapon(missing, group.SWORD.get(), group.AXE.get(), group.HAMMER.get(), group.MULTITOOL.get());
            expectArmour(missing, ItemTags.HEAD_ARMOR_ENCHANTABLE, group.HELMET.get());
            expectArmour(missing, ItemTags.CHEST_ARMOR_ENCHANTABLE, group.CHESTPLATE.get());
            expectArmour(missing, ItemTags.LEG_ARMOR_ENCHANTABLE, group.LEGGINGS.get());
            expectArmour(missing, ItemTags.FOOT_ARMOR_ENCHANTABLE, group.BOOTS.get());
            expectDurable(missing, group.SPEAR.get(), group.SHEARS.get(), group.BUCKET.get());
            expect(missing, ToolsEnchantments.SPEAR_ENCHANTABLE, group.SPEAR.get());
            expect(missing, ToolsEnchantments.SHEARS_ENCHANTABLE, group.SHEARS.get());
        }

        helper.assertTrue(missing.isEmpty(), missing.size() + " item/tag pairs are missing from the enchantable tags: " + String.join(", ", missing));
        helper.succeed();
    }

    /**
     * With the default config every part is on, so all six of this mod's enchantments must be
     * registered - their definitions are conditional on their part now - and in the three vanilla
     * tags that make an enchantment obtainable.
     */
    private static void toolsEnchantmentsAreObtainable(GameTestHelper helper) {
        Registry<Enchantment> registry = helper.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        List<String> missing = new ArrayList<>();

        for (ResourceKey<Enchantment> key : List.of(ToolsEnchantments.CHICKEN_JUMP, ToolsEnchantments.BOUNCINESS, ToolsEnchantments.CONDUCTIVE, ToolsEnchantments.FLAMMABLE, ToolsEnchantments.UNSTABLE, ToolsEnchantments.CORAL_CUTTER)) {
            Optional<Holder.Reference<Enchantment>> enchantment = registry.get(key);
            if (enchantment.isEmpty()) {
                missing.add(key.identifier() + " (not registered)");
                continue;
            }

            for (TagKey<Enchantment> tag : List.of(EnchantmentTags.IN_ENCHANTING_TABLE, EnchantmentTags.TRADEABLE, EnchantmentTags.ON_RANDOM_LOOT)) {
                if (!enchantment.get().is(tag)) {
                    missing.add(key.identifier() + " not in #" + tag.location());
                }
            }
        }

        helper.assertTrue(missing.isEmpty(), "enchantments missing or not obtainable: " + String.join(", ", missing));
        helper.succeed();
    }

    /**
     * Asked the way the enchanting table asks - {@code EnchantmentHelper#getAvailableEnchantmentResults}
     * over {@code #minecraft:in_enchanting_table}, which NeoForge routes through
     * {@code isPrimaryItemFor} and Fabric through {@code ALLOW_ENCHANTING} - so the loader wiring is
     * under test, not just {@code BetterSpearItem}'s methods. A cost of 30 sits inside the window of
     * all four trident enchantments and all four spear enchantments. The vanilla trident is checked
     * alongside so the veto cannot leak onto it.
     */
    private static void spearsAreNeverOfferedRiptideOrChanneling(GameTestHelper helper) {
        Registry<Enchantment> registry = helper.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        Set<ResourceKey<Enchantment>> spear = offeredAtTable(registry, new ItemStack(ToolsItems.IRON_SPEAR.get()));
        Set<ResourceKey<Enchantment>> trident = offeredAtTable(registry, new ItemStack(Items.TRIDENT));

        helper.assertFalse(spear.contains(Enchantments.RIPTIDE) || spear.contains(Enchantments.CHANNELING), "a spear was offered riptide or channeling: " + spear);
        helper.assertTrue(spear.containsAll(List.of(Enchantments.LOYALTY, Enchantments.IMPALING)), "a spear was not offered loyalty and impaling: " + spear);
        helper.assertTrue(spear.containsAll(List.of(ToolsEnchantments.BOUNCINESS, ToolsEnchantments.CONDUCTIVE, ToolsEnchantments.FLAMMABLE, ToolsEnchantments.UNSTABLE)), "a spear was not offered all four spear enchantments: " + spear);
        helper.assertTrue(trident.containsAll(List.of(Enchantments.RIPTIDE, Enchantments.CHANNELING)), "the vanilla trident lost riptide or channeling: " + trident);
        helper.succeed();
    }

    /**
     * The anvil path, through a real {@link AnvilMenu} so each loader's own hook runs - NeoForge's
     * patched {@code supportsEnchantment} call and Fabric's {@code AnvilMenuMixin}. A book of Riptide
     * or Channeling combines with nothing on a spear; Loyalty still goes on, and a vanilla trident
     * still takes Riptide.
     */
    private static void spearAnvilRejectsRiptideAndChanneling(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper, ItemStack.EMPTY);
        Registry<Enchantment> registry = helper.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> riptide = registry.getOrThrow(Enchantments.RIPTIDE);
        Holder<Enchantment> loyalty = registry.getOrThrow(Enchantments.LOYALTY);

        helper.assertTrue(combine(player, new ItemStack(ToolsItems.IRON_SPEAR.get()), riptide).isEmpty(), "riptide went onto a spear at an anvil");
        helper.assertTrue(combine(player, new ItemStack(ToolsItems.IRON_SPEAR.get()), registry.getOrThrow(Enchantments.CHANNELING)).isEmpty(), "channeling went onto a spear at an anvil");

        ItemStack loyalSpear = combine(player, new ItemStack(ToolsItems.IRON_SPEAR.get()), loyalty);
        helper.assertValueEqual(loyalSpear.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY).getLevel(loyalty), 1, "loyalty level on a spear from an anvil");

        ItemStack riptideTrident = combine(player, new ItemStack(Items.TRIDENT), riptide);
        helper.assertValueEqual(riptideTrident.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY).getLevel(riptide), 1, "riptide level on a vanilla trident from an anvil");
        helper.succeed();
    }

    private static Set<ResourceKey<Enchantment>> offeredAtTable(Registry<Enchantment> registry, ItemStack stack) {
        return EnchantmentHelper.getAvailableEnchantmentResults(30, stack, registry.getOrThrow(EnchantmentTags.IN_ENCHANTING_TABLE).stream())
                .stream().map(instance -> instance.enchantment().unwrapKey().orElseThrow()).collect(Collectors.toSet());
    }

    private static ItemStack combine(ServerPlayer player, ItemStack left, Holder<Enchantment> enchantment) {
        ItemEnchantments.Mutable stored = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        stored.set(enchantment, 1);
        ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
        book.set(DataComponents.STORED_ENCHANTMENTS, stored.toImmutable());

        AnvilMenu menu = new AnvilMenu(0, player.getInventory());
        menu.getSlot(AnvilMenu.INPUT_SLOT).set(left);
        menu.getSlot(AnvilMenu.ADDITIONAL_SLOT).set(book);
        menu.createResult();
        return menu.getSlot(AnvilMenu.RESULT_SLOT).getItem();
    }

    /**
     * Armour goes into the armour slots and the defence it grants is the configured one. Those
     * per-slot numbers used to come from an {@code ArmorItem} override; they are baked into the
     * {@code equippable} and {@code attribute_modifiers} components at construction now, so this is
     * the only thing that proves the configuration reached them.
     */
    private static void armourEquipsAndProtects(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper, ItemStack.EMPTY);
        stand(helper, player, new BlockPos(4, 1, 4));

        ToolsItems.MATERIAL_GROUPS.forEach((name, group) -> {
            ArmorMaterialConfig armor = ToolsCommonMod.COMMON_CONFIG.moddedArmors.get(name);
            int expected = armor.getHelmetReductionAmount() + armor.getChestPlateReductionAmount() + armor.getLeggingsReductionAmount() + armor.getBootsReductionAmount();
            assertArmourValue(helper, player, name, group.HELMET.get(), group.CHESTPLATE.get(), group.LEGGINGS.get(), group.BOOTS.get(), expected);
        });

        ArmorMaterialConfig chicken = ToolsCommonMod.COMMON_CONFIG.chickenSuitArmorMaterial;
        assertArmourValue(helper, player, "chicken_suit",
                ToolsItems.CHICKEN_SUIT_HELMET.get(), ToolsItems.CHICKEN_SUIT_CHESTPLATE.get(), ToolsItems.CHICKEN_SUIT_LEGGINGS.get(), ToolsItems.CHICKEN_SUIT_BOOTS.get(),
                chicken.getHelmetReductionAmount() + chicken.getChestPlateReductionAmount() + chicken.getLeggingsReductionAmount() + chicken.getBootsReductionAmount());

        helper.succeed();
    }

    /**
     * Modded shears cut leaves and drop the leaf block. The speed comes from the
     * {@code minecraft:tool} component, but the drop does not: vanilla's leaves loot table asks for
     * {@code minecraft:shears} by identity, which a modded pair is not. NeoForge's own copy of that
     * table asks for the {@code shears_dig} ability instead, and Fabric only passes because of this
     * mod's {@code ItemPredicate} mixin - two entirely different mechanisms, one assertion.
     */
    private static void shearsCutLeaves(GameTestHelper helper) {
        final BlockPos leaves = new BlockPos(4, 1, 4);
        // Persistent, so the block update from setBlock cannot schedule it to decay instead.
        helper.setBlock(leaves, Blocks.OAK_LEAVES.defaultBlockState().setValue(BlockStateProperties.PERSISTENT, true));

        ServerPlayer player = survivalPlayer(helper, new ItemStack(ToolsItems.DIAMOND_SHEARS.get()));
        stand(helper, player, new BlockPos(4, 1, 2));

        helper.assertValueEqual(player.getItemInHand(InteractionHand.MAIN_HAND).getDestroySpeed(helper.getBlockState(leaves)), 15.0F, "shears speed on leaves");

        helper.assertTrue(player.gameMode.destroyBlock(helper.absolutePos(leaves)), "the shears could not break leaves");
        helper.assertItemEntityPresent(Items.OAK_LEAVES, leaves, 2.0D);
        helper.assertItemEntityNotPresent(Items.OAK_SAPLING, leaves, 2.0D);

        helper.succeed();
    }

    /**
     * The Coral Cutter enchantment, which is three separate pieces since the port: the destroy
     * speed comes from a mixin onto {@code ItemStack} (a data component cannot be conditioned on an
     * enchantment), the "may I harvest this" answer from {@code CorrectToolForDropEvent}, and live
     * coral instead of dead from {@code OnDropStacksEvent} re-rolling the loot with silk touch.
     * Unenchanted shears are checked alongside so a blanket "always works" cannot pass.
     */
    private static void shearsCutCoralWithCoralCutter(GameTestHelper helper) {
        final BlockPos plainTarget = new BlockPos(2, 1, 4);
        final BlockPos cutterTarget = new BlockPos(6, 1, 4);
        helper.setBlock(plainTarget, Blocks.TUBE_CORAL_BLOCK);
        helper.setBlock(cutterTarget, Blocks.TUBE_CORAL_BLOCK);

        BlockState coral = Blocks.TUBE_CORAL_BLOCK.defaultBlockState();
        Holder<Enchantment> coralCutter = helper.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ToolsEnchantments.CORAL_CUTTER);

        ItemStack plain = new ItemStack(ToolsItems.DIAMOND_SHEARS.get());
        ItemStack cutter = new ItemStack(ToolsItems.DIAMOND_SHEARS.get());
        cutter.enchant(coralCutter, 1);
        helper.assertTrue(ToolsEnchantments.hasCoralCutter(cutter), "the enchantment did not land on the shears");

        // The speed boost is ItemStackDestroySpeedMixin, out of the common mixin config, so this
        // is also what proves both loaders load assortedtools.mixins.json - NeoForge went a while
        // registering only its own. Unenchanted shears are measured first so a mixin that boosted
        // everything, or one that never ran at all, could not both read as a pass.
        helper.assertValueEqual(plain.getDestroySpeed(coral), 1.0F, "plain shears speed on coral");
        helper.assertValueEqual(cutter.getDestroySpeed(coral), 10.0F, "coral cutter shears speed on coral");
        helper.assertFalse(plain.isCorrectToolForDrops(coral), "plain shears should not harvest coral");
        helper.assertTrue(cutter.isCorrectToolForDrops(coral), "coral cutter shears should harvest coral");

        ServerPlayer player = survivalPlayer(helper, plain);
        stand(helper, player, new BlockPos(4, 1, 2));

        player.gameMode.destroyBlock(helper.absolutePos(plainTarget));
        helper.assertBlockPresent(Blocks.AIR, plainTarget);
        helper.assertItemEntityNotPresent(Items.TUBE_CORAL_BLOCK, plainTarget, 2.0D);

        player.setItemInHand(InteractionHand.MAIN_HAND, cutter);
        player.gameMode.destroyBlock(helper.absolutePos(cutterTarget));
        helper.assertBlockPresent(Blocks.AIR, cutterTarget);
        // Live coral, not the dead block a pickaxe without silk touch would leave behind.
        helper.assertItemEntityPresent(Items.TUBE_CORAL_BLOCK, cutterTarget, 2.0D);
        helper.assertItemEntityNotPresent(Items.DEAD_TUBE_CORAL_BLOCK, cutterTarget, 2.0D);

        helper.succeed();
    }

    /**
     * Every vanilla coral - live and dead; plant, fan, wall fan and block - is under
     * {@code c:corals/all}, which is what Coral Cutter's speed, harvest and drop rules all read.
     * Walks the block registry rather than a list so a coral vanilla adds later cannot slip past.
     * Then breaks a dead coral block and a dead fan, neither of which plain shears would drop.
     */
    private static void coralCutterCoversEveryCoral(GameTestHelper helper) {
        Holder<Enchantment> coralCutter = helper.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ToolsEnchantments.CORAL_CUTTER);
        ItemStack cutter = new ItemStack(ToolsItems.DIAMOND_SHEARS.get());
        cutter.enchant(coralCutter, 1);

        List<String> missed = new ArrayList<>();
        BuiltInRegistries.BLOCK.listElements()
                .filter(block -> block.key().identifier().getNamespace().equals("minecraft") && block.key().identifier().getPath().contains("coral"))
                .forEach(block -> {
                    BlockState state = block.value().defaultBlockState();
                    if (!state.is(ToolsTags.Blocks.ALL_CORALS) || cutter.getDestroySpeed(state) != 10.0F || !cutter.isCorrectToolForDrops(state)) {
                        missed.add(block.key().identifier().getPath());
                    }
                });
        helper.assertTrue(missed.isEmpty(), "coral cutter does not cover: " + String.join(", ", missed));

        final BlockPos deadBlock = new BlockPos(2, 1, 4);
        final BlockPos deadFan = new BlockPos(6, 1, 4);
        helper.setBlock(deadBlock, Blocks.DEAD_TUBE_CORAL_BLOCK);
        helper.setBlock(deadFan, Blocks.DEAD_TUBE_CORAL_FAN);

        ServerPlayer player = survivalPlayer(helper, cutter);
        stand(helper, player, new BlockPos(4, 1, 2));
        player.gameMode.destroyBlock(helper.absolutePos(deadBlock));
        player.gameMode.destroyBlock(helper.absolutePos(deadFan));

        helper.assertItemEntityPresent(Items.DEAD_TUBE_CORAL_BLOCK, deadBlock, 2.0D);
        helper.assertItemEntityPresent(Items.DEAD_TUBE_CORAL_FAN, deadFan, 2.0D);
        helper.succeed();
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

    /**
     * The building wand fills the gap between two corners, pays for it out of the inventory, and
     * refuses when the inventory cannot cover it.
     */
    private static void buildingWandBuildsAndConsumesBlocks(GameTestHelper helper) {
        final BlockPos start = new BlockPos(2, 1, 2);
        final BlockPos gap = new BlockPos(2, 1, 3);
        final BlockPos end = new BlockPos(2, 1, 4);
        final BlockPos poorStart = new BlockPos(6, 1, 2);
        final BlockPos poorGap = new BlockPos(6, 1, 3);
        final BlockPos poorEnd = new BlockPos(6, 1, 4);

        for (BlockPos corner : List.of(start, end, poorStart, poorEnd)) {
            helper.setBlock(corner, Blocks.STONE);
        }

        WandBuildingItem wand = ToolsItems.BUILDING_WAND.get();
        ItemStack stack = new ItemStack(wand);
        ServerPlayer player = survivalPlayer(helper, stack);
        stand(helper, player, new BlockPos(4, 1, 3));
        wand.onCraftedBy(stack, player);
        helper.assertValueEqual(NBTHelper.getString(stack, "Mode"), "buildbox", "the mode a freshly crafted building wand starts in");

        player.getInventory().add(new ItemStack(Blocks.STONE, 8));

        useOnTopOf(helper, player, start);
        helper.assertTrue(NBTHelper.hasTag(player.getItemInHand(InteractionHand.MAIN_HAND), "Start"), "the first click did not record a selection start");
        useOnTopOf(helper, player, end);

        helper.assertBlockPresent(Blocks.STONE, gap);
        helper.assertValueEqual(countInInventory(player, Blocks.STONE.asItem()), 7, "stone left after the wand filled one gap");
        helper.assertValueEqual(player.getItemInHand(InteractionHand.MAIN_HAND).getDamageValue(), 1, "building wand durability spent");

        // The same two clicks again with nothing to build from.
        removeFromInventory(player, Blocks.STONE.asItem());
        useOnTopOf(helper, player, poorStart);
        useOnTopOf(helper, player, poorEnd);

        helper.assertBlockPresent(Blocks.AIR, poorGap);
        helper.assertValueEqual(player.getItemInHand(InteractionHand.MAIN_HAND).getDamageValue(), 1, "the wand should not wear when it refuses to build");

        helper.succeed();
    }

    /**
     * The breaking wand in its default weak mode clears what a piston would break and nothing else,
     * and the reinforced one cycled to break-all clears stone as well.
     */
    private static void breakingWandsClearTheirModes(GameTestHelper helper) {
        final BlockPos torchStart = new BlockPos(1, 1, 1);
        final BlockPos torchMiddle = new BlockPos(2, 1, 1);
        final BlockPos torchEnd = new BlockPos(3, 1, 1);
        final BlockPos stoneStart = new BlockPos(5, 1, 1);
        final BlockPos stoneMiddle = new BlockPos(6, 1, 1);
        final BlockPos stoneEnd = new BlockPos(7, 1, 1);

        for (BlockPos torch : List.of(torchStart, torchMiddle, torchEnd)) {
            helper.setBlock(torch, Blocks.TORCH);
        }
        for (BlockPos stone : List.of(stoneStart, stoneMiddle, stoneEnd)) {
            helper.setBlock(stone, Blocks.STONE);
        }

        WandBreakingItem wand = ToolsItems.BREAKING_WAND.get();
        ItemStack stack = new ItemStack(wand);
        ServerPlayer player = survivalPlayer(helper, stack);
        stand(helper, player, new BlockPos(4, 1, 3));
        wand.onCraftedBy(stack, player);
        helper.assertValueEqual(NBTHelper.getString(stack, "Mode"), "breakweak", "the mode a freshly crafted breaking wand starts in");

        useOnTopOf(helper, player, torchStart);
        useOnTopOf(helper, player, torchEnd);

        helper.assertBlockPresent(Blocks.AIR, torchStart);
        helper.assertBlockPresent(Blocks.AIR, torchMiddle);
        helper.assertBlockPresent(Blocks.AIR, torchEnd);
        // Weak mode leaves stone alone: the wand decides per block, not per selection.
        helper.assertBlockPresent(Blocks.STONE, stoneMiddle);
        helper.assertValueEqual(player.getItemInHand(InteractionHand.MAIN_HAND).getDamageValue(), 1, "breaking wand durability spent");

        WandBreakingItem reinforced = ToolsItems.REINFORCED_BREAKING_WAND.get();
        ItemStack reinforcedStack = new ItemStack(reinforced);
        reinforced.onCraftedBy(reinforcedStack, player);
        player.setItemInHand(InteractionHand.MAIN_HAND, reinforcedStack);

        ToolCycleModesPacket.handle(new ToolCycleModesPacket(InteractionHand.MAIN_HAND), player);
        helper.assertValueEqual(NBTHelper.getString(player.getItemInHand(InteractionHand.MAIN_HAND), "Mode"), "breakall", "the mode after one cycle on a reinforced breaking wand");

        useOnTopOf(helper, player, stoneStart);
        useOnTopOf(helper, player, stoneEnd);

        helper.assertBlockPresent(Blocks.AIR, stoneStart);
        helper.assertBlockPresent(Blocks.AIR, stoneMiddle);
        helper.assertBlockPresent(Blocks.AIR, stoneEnd);
        helper.assertValueEqual(player.getItemInHand(InteractionHand.MAIN_HAND).getDamageValue(), 1, "reinforced breaking wand durability spent");

        helper.succeed();
    }

    /**
     * A chicken suit piece put in an anvil against matching armour hands back that armour with
     * Chicken Jump on it. The handler is called with the library event directly, the same way the
     * milking test is, because the two loaders raise it from different call sites.
     */
    private static void chickenSuitConvertsArmourInAnAnvil(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper, ItemStack.EMPTY);
        stand(helper, player, new BlockPos(4, 1, 4));

        AnvilUpdatedEvent matching = new AnvilUpdatedEvent(new ItemStack(Items.IRON_CHESTPLATE), new ItemStack(ToolsItems.CHICKEN_SUIT_CHESTPLATE.get()), "", 0, player);
        ChickenSuitConversionHandler.anvilUpdateEvent(matching);

        ItemStack output = matching.getOutput();
        helper.assertTrue(output.is(Items.IRON_CHESTPLATE), "converting an iron chestplate gave back " + output);
        // Read off the stack's own component rather than through EnchantmentHelper, whose
        // level lookup is deprecated; this is the same accessor the rest of the mod uses.
        helper.assertValueEqual(ToolsEnchantments.getLevel(output, ToolsEnchantments.CHICKEN_JUMP), 1, "chicken jump level on the converted armour");
        helper.assertValueEqual(matching.getMaterialCost(), 1, "chicken suit pieces consumed");
        helper.assertValueEqual(matching.getCost(), 5, "level cost of converting a chestplate");

        // A helmet against a chestplate is the wrong slot and has to be left alone.
        AnvilUpdatedEvent mismatched = new AnvilUpdatedEvent(new ItemStack(Items.IRON_HELMET), new ItemStack(ToolsItems.CHICKEN_SUIT_CHESTPLATE.get()), "", 0, player);
        ChickenSuitConversionHandler.anvilUpdateEvent(mismatched);
        helper.assertTrue(mismatched.getOutput().isEmpty(), "a chicken suit chestplate converted a helmet");

        // So does anything that is not armour at all.
        AnvilUpdatedEvent notArmour = new AnvilUpdatedEvent(new ItemStack(Items.DIAMOND_PICKAXE), new ItemStack(ToolsItems.CHICKEN_SUIT_HELMET.get()), "", 0, player);
        ChickenSuitConversionHandler.anvilUpdateEvent(notArmour);
        helper.assertTrue(notArmour.getOutput().isEmpty(), "a chicken suit helmet converted a pickaxe");

        helper.succeed();
    }

    /**
     * Every item this mod registers has a model and a name, and the creative tab they all live in
     * exists. Missing models and missing lang keys are the most repeated failure of this whole
     * port and are invisible to both a compiler and a headless server, so this reads the mod's own
     * assets straight off the classpath and reports every gap in one message.
     */
    private static void everyItemHasAModelAndAName(GameTestHelper helper) {
        helper.assertTrue(BuiltInRegistries.CREATIVE_MODE_TAB.get(ToolsCreativeItems.CREATIVE_TAB_KEY).isPresent(), "the Assorted Tools creative tab is not registered");

        JsonObject lang = readLang(helper);
        helper.assertTrue(lang.has("itemGroup." + Constants.MOD_ID), "the creative tab has no name in en_us.json");

        List<String> missing = new ArrayList<>();
        int items = 0;

        for (Item item : BuiltInRegistries.ITEM) {
            Identifier id = BuiltInRegistries.ITEM.getKey(item);
            if (!Constants.MOD_ID.equals(id.getNamespace())) {
                continue;
            }

            items++;
            if (!resourceExists("/assets/" + id.getNamespace() + "/items/" + id.getPath() + ".json")) {
                missing.add("no item model for " + id);
            }
            if (!lang.has(item.getDescriptionId())) {
                missing.add("no lang key " + item.getDescriptionId());
            }
        }

        for (Block block : BuiltInRegistries.BLOCK) {
            Identifier id = BuiltInRegistries.BLOCK.getKey(block);
            if (!Constants.MOD_ID.equals(id.getNamespace())) {
                continue;
            }

            if (!resourceExists("/assets/" + id.getNamespace() + "/blockstates/" + id.getPath() + ".json")) {
                missing.add("no blockstate for " + id);
            }
        }

        helper.assertTrue(items > 0, "no items are registered under the " + Constants.MOD_ID + " namespace, so nothing was checked");
        helper.assertTrue(missing.isEmpty(), missing.size() + " assets are missing across " + items + " items: " + String.join(", ", missing));

        helper.succeed();
    }

    /**
     * Every recipe file this mod ships either loaded, or carries load conditions and was skipped by
     * them. A file with neither failed to parse - which is what every extra-material recipe did on
     * Fabric without AssortedCore: Fabric's datagen wrote them without conditions, and the NeoForge
     * copy that shadowed it carries a key Fabric ignores. So only this loader's own key counts.
     */
    private static void everyRecipeLoadsOrIsConditionedOff(GameTestHelper helper) {
        MinecraftServer server = helper.getLevel().getServer();
        FileToIdConverter recipes = FileToIdConverter.json("recipe");
        String conditionsKey = Services.PLATFORM.getPlatformName().equals("Fabric") ? "fabric:load_conditions" : "neoforge:conditions";
        List<String> failed = new ArrayList<>();

        recipes.listMatchingResources(server.getResourceManager()).forEach((file, resource) -> {
            Identifier id = recipes.fileToId(file);
            if (!id.getNamespace().equals(Constants.MOD_ID) || server.getRecipeManager().byKey(ResourceKey.create(Registries.RECIPE, id)).isPresent()) {
                return;
            }

            try (BufferedReader reader = resource.openAsReader()) {
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                if (!json.has(conditionsKey)) {
                    failed.add(id.toString());
                }
            } catch (IOException e) {
                failed.add(id + " (" + e.getMessage() + ")");
            }
        });

        helper.assertTrue(failed.isEmpty(), failed.size() + " recipes failed to load without being conditioned off: " + String.join(", ", failed.subList(0, Math.min(10, failed.size()))));
        helper.succeed();
    }

    // -----------------------------------------------------------------------------------------
    // Helpers for the checks above
    // -----------------------------------------------------------------------------------------

    /** The first item in {@code tag}, or empty when nothing in the game populates it. */
    private static ItemStack firstOf(TagKey<Item> tag) {
        for (Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
            return new ItemStack(holder.value());
        }
        return ItemStack.EMPTY;
    }

    /**
     * Asserts that a grid of ingredients resolves to a recipe producing {@code expected}. Going
     * through the recipe manager is the whole point: it is what actually loaded the json, its
     * conditions included, so a recipe dropped by a failing condition shows up here and nowhere
     * else.
     */
    private static void assertCrafts(GameTestHelper helper, int width, int height, List<ItemStack> grid, Item expected) {
        Identifier id = BuiltInRegistries.ITEM.getKey(expected);
        CraftingInput input = CraftingInput.of(width, height, grid);
        Optional<RecipeHolder<CraftingRecipe>> found = helper.getLevel().recipeAccess().getRecipeFor(RecipeType.CRAFTING, input, helper.getLevel());

        helper.assertTrue(found.isPresent(), "no crafting recipe matched the pattern for " + id);

        ItemStack result = found.get().value().assemble(input);
        helper.assertTrue(result.is(expected), "the pattern for " + id + " crafted " + BuiltInRegistries.ITEM.getKey(result.getItem()) + " instead");
    }

    private static void assertSpeed(GameTestHelper helper, Item tool, BlockState state, float expected, String what) {
        helper.assertValueEqual(new ItemStack(tool).getDestroySpeed(state), expected, what);
    }

    private static void expect(List<String> missing, TagKey<Item> tag, Item... items) {
        for (Item item : items) {
            if (!new ItemStack(item).is(tag)) {
                missing.add(BuiltInRegistries.ITEM.getKey(item) + " not in #" + tag.location());
            }
        }
    }

    private static void expectDurable(List<String> missing, Item... items) {
        expect(missing, ItemTags.DURABILITY_ENCHANTABLE, items);
        expect(missing, ItemTags.VANISHING_ENCHANTABLE, items);
    }

    private static void expectMiningTool(List<String> missing, Item... items) {
        expect(missing, ItemTags.MINING_ENCHANTABLE, items);
        expect(missing, ItemTags.MINING_LOOT_ENCHANTABLE, items);
        expectDurable(missing, items);
    }

    private static void expectMeleeWeapon(List<String> missing, Item... items) {
        expect(missing, ItemTags.WEAPON_ENCHANTABLE, items);
        expect(missing, ItemTags.MELEE_WEAPON_ENCHANTABLE, items);
        expect(missing, ItemTags.SHARP_WEAPON_ENCHANTABLE, items);
        expect(missing, ItemTags.FIRE_ASPECT_ENCHANTABLE, items);
        expectDurable(missing, items);
    }

    private static void expectArmour(List<String> missing, TagKey<Item> slotTag, Item... items) {
        expect(missing, slotTag, items);
        expect(missing, ItemTags.ARMOR_ENCHANTABLE, items);
        expect(missing, ItemTags.EQUIPPABLE_ENCHANTABLE, items);
        expectDurable(missing, items);
    }

    /**
     * Puts one set of armour on and reads the defence back off the player. Equipment attribute
     * modifiers are only collected during the entity's own tick, so the player is ticked once
     * between putting the set on and asking.
     */
    private static void assertArmourValue(GameTestHelper helper, ServerPlayer player, String name, Item helmet, Item chestplate, Item leggings, Item boots, int expected) {
        player.setItemSlot(EquipmentSlot.HEAD, new ItemStack(helmet));
        player.setItemSlot(EquipmentSlot.CHEST, new ItemStack(chestplate));
        player.setItemSlot(EquipmentSlot.LEGS, new ItemStack(leggings));
        player.setItemSlot(EquipmentSlot.FEET, new ItemStack(boots));

        helper.assertTrue(player.getEquipmentSlotForItem(new ItemStack(helmet)) == EquipmentSlot.HEAD, name + " helmet does not belong in the head slot");
        helper.assertTrue(player.getEquipmentSlotForItem(new ItemStack(boots)) == EquipmentSlot.FEET, name + " boots do not belong in the feet slot");

        // doTick, not tick: ServerPlayer#tick does not call super, so the equipment sweep that
        // turns the armour's components into attribute modifiers only runs from doTick.
        player.doTick();

        helper.assertValueEqual(player.getArmorValue(), expected, "armour value while wearing a full set of " + name);
    }

    /** Throws whatever spear is in the main hand through the item's own charge and release. */
    private static BetterSpearEntity throwHeldSpear(GameTestHelper helper, ServerPlayer player) {
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        BetterSpearItem spear = (BetterSpearItem) stack.getItem();

        player.startUsingItem(InteractionHand.MAIN_HAND);
        // releaseUsing counts backwards: twenty short of the full duration is a twenty tick charge,
        // well past the ten it insists on.
        boolean thrown = spear.releaseUsing(stack, helper.getLevel(), player, spear.getUseDuration(stack, player) - 20);
        helper.assertTrue(thrown, "the spear refused to be thrown");

        List<BetterSpearEntity> spears = helper.getEntities(ToolsEntities.BETTER_SPEAR.get());
        helper.assertValueEqual(spears.size(), 1, "spear entities in the world after one throw");
        return spears.get(0);
    }

    /**
     * Ticks one entity by hand up to {@code limit} times, stopping as soon as {@code done} holds.
     * Bounded on purpose: a test that waits on physics it does not drive is a test that hangs.
     */
    private static boolean tickUntil(Entity entity, int limit, BooleanSupplier done) {
        for (int tick = 0; tick < limit; tick++) {
            if (done.getAsBoolean()) {
                return true;
            }
            entity.tick();
        }
        return done.getAsBoolean();
    }

    private static void assertBoomerangReturns(GameTestHelper helper, BoomerangItem item) {
        Identifier id = BuiltInRegistries.ITEM.getKey(item);
        ServerPlayer player = survivalPlayer(helper, new ItemStack(item));
        hover(helper, player, new Vec3(4.5D, 2.0D, 4.5D), -90.0F);

        helper.assertTrue(player.getItemInHand(InteractionHand.MAIN_HAND).use(helper.getLevel(), player, InteractionHand.MAIN_HAND).consumesAction(), id + " refused to be thrown");
        helper.assertTrue(player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty(), id + " stayed in the hand after being thrown");

        List<BoomerangEntity> flying = helper.getLevel().getEntitiesOfClass(BoomerangEntity.class, player.getBoundingBox().inflate(4.0D));
        helper.assertValueEqual(flying.size(), 1, "boomerang entities in the world after throwing " + id);

        BoomerangEntity boomerang = flying.get(0);
        // Out for its configured range and back at half a block a tick; two hundred is generous.
        boolean returned = tickUntil(boomerang, 200, boomerang::isRemoved);

        helper.assertTrue(returned, id + " never came back to the thrower");
        helper.assertValueEqual(countInInventory(player, item), 1, id + " in the thrower's inventory after it returned");

        helper.killAllEntitiesOfClass(BoomerangEntity.class);
        helper.getLevel().getServer().getPlayerList().remove(player);
    }

    private static int countInInventory(ServerPlayer player, Item item) {
        int count = 0;
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (stack.is(item)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    private static void removeFromInventory(ServerPlayer player, Item item) {
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            if (player.getInventory().getItem(slot).is(item)) {
                player.getInventory().setItem(slot, ItemStack.EMPTY);
            }
        }
    }

    /** Puts the player at a spot in the air inside the test box, looking at the given pitch. */
    private static void hover(GameTestHelper helper, ServerPlayer player, Vec3 relative, float xRot) {
        Vec3 at = helper.absoluteVec(relative);
        player.snapTo(at.x, at.y, at.z, 0.0F, xRot);
    }

    /**
     * The translation key behind a component. A dedicated server loads no mod language file, so a
     * rendered string would just be the key anyway - this asks for it directly instead.
     */
    private static String translationKey(Component component) {
        return component.getContents() instanceof TranslatableContents translatable ? translatable.getKey() : component.getString();
    }

    private static JsonObject readLang(GameTestHelper helper) {
        try (InputStream in = ToolsGameTests.class.getResourceAsStream("/assets/" + Constants.MOD_ID + "/lang/en_us.json")) {
            helper.assertTrue(in != null, "en_us.json is not on the classpath");
            return JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException("could not read en_us.json", e);
        }
    }

    /** Whether a resource is on the classpath. The mod's own assets are, even on a headless server. */
    private static boolean resourceExists(String path) {
        try (InputStream in = ToolsGameTests.class.getResourceAsStream(path)) {
            return in != null;
        } catch (IOException e) {
            return false;
        }
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
