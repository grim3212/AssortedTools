package com.grim3212.assorted.tools.gametest;

import com.grim3212.assorted.tools.common.entity.IceChargeEntity;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.monster.skeleton.Stray;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.lib.test.TestSupport.*;
import static com.grim3212.assorted.tools.gametest.ToolsTestSupport.*;

/**
 * The frost rod, frost powder and ice charge, the staff recipes they feed, and where the staffs and
 * rods are found.
 */
final class FrostTests {

    private FrostTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("frost_rod_crafts_through_to_a_neptune_staff", FrostTests::frostRodCraftsThroughToANeptuneStaff);
        out.accept("dispensed_ice_charge_freezes_water_and_puts_out_fire", FrostTests::dispensedIceChargeFreezesWaterAndPutsOutFire);
        out.accept("ice_charge_chills_what_it_hits", FrostTests::iceChargeChillsWhatItHits);
        out.accept("strays_drop_frost_rods", FrostTests::straysDropFrostRods);
        out.accept("staffs_turn_up_in_chests", FrostTests::staffsTurnUpInChests);
    }

    /** Rod to powder to charge to staff, the way blaze rods make fire charges; each staff takes its own charge. */
    private static void frostRodCraftsThroughToANeptuneStaff(GameTestHelper helper) {
        assertCrafts(helper, 1, 1, List.of(new ItemStack(ToolsItems.FROST_ROD.get())), ToolsItems.FROST_POWDER.get());
        assertCrafts(helper, 3, 1, List.of(new ItemStack(Items.GUNPOWDER), new ItemStack(ToolsItems.FROST_POWDER.get()), new ItemStack(Items.SNOWBALL)), ToolsItems.ICE_CHARGE.get());
        assertCrafts(helper, 1, 3, List.of(new ItemStack(Items.DIAMOND), new ItemStack(ToolsItems.ICE_CHARGE.get()), new ItemStack(ToolsItems.FROST_ROD.get())), ToolsItems.NEPTUNE_STAFF.get());
        assertCrafts(helper, 1, 3, List.of(new ItemStack(Items.DIAMOND), new ItemStack(Items.FIRE_CHARGE), new ItemStack(Items.BLAZE_ROD)), ToolsItems.PHOENIX_STAFF.get());

        // The old bucket recipes are gone.
        helper.assertTrue(craftsNothing(helper, List.of(new ItemStack(Items.DIAMOND), new ItemStack(Items.WATER_BUCKET), new ItemStack(Items.STICK))), "a water bucket still makes a Neptune staff");
        helper.assertTrue(craftsNothing(helper, List.of(new ItemStack(Items.DIAMOND), new ItemStack(Items.LAVA_BUCKET), new ItemStack(Items.BLAZE_ROD))), "a lava bucket still makes a Phoenix staff");
        helper.succeed();
    }

    /**
     * Fired straight down into a walled-in pool, two blocks from soul fire, which never burns out on
     * its own. The charge passes through the water and lands on the floor under it.
     */
    private static void dispensedIceChargeFreezesWaterAndPutsOutFire(GameTestHelper helper) {
        final BlockPos water = new BlockPos(4, 1, 4);
        final BlockPos fire = new BlockPos(6, 1, 4);
        final BlockPos dispenser = new BlockPos(4, 4, 4);
        helper.setBlock(water.below(), Blocks.STONE);
        for (Direction side : Direction.Plane.HORIZONTAL) {
            helper.setBlock(water.relative(side), Blocks.STONE);
        }
        helper.setBlock(water, Blocks.WATER);
        helper.setBlock(fire.below(), Blocks.SOUL_SOIL);
        helper.setBlock(fire, Blocks.SOUL_FIRE);
        helper.setBlock(dispenser, Blocks.DISPENSER.defaultBlockState().setValue(DispenserBlock.FACING, Direction.DOWN));
        helper.getBlockEntity(dispenser, DispenserBlockEntity.class).setItem(0, new ItemStack(ToolsItems.ICE_CHARGE.get()));
        helper.setBlock(dispenser.above(), Blocks.REDSTONE_BLOCK);

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertBlockPresent(Blocks.ICE, water))
                .thenExecute(() -> helper.assertBlockPresent(Blocks.AIR, fire))
                .thenSucceed();
    }

    /** A thrown charge leaves a cow frozen through, as powder snow would, and hurts it a little. */
    private static void iceChargeChillsWhatItHits(GameTestHelper helper) {
        Cow cow = helper.spawn(EntityTypes.COW, new BlockPos(4, 1, 4));
        cow.setNoAi(true);
        float health = cow.getHealth();

        ServerPlayer player = survivalPlayer(helper, ItemStack.EMPTY);
        Vec3 from = helper.absoluteVec(new Vec3(4.5D, 4.5D, 4.5D));
        IceChargeEntity charge = new IceChargeEntity(helper.getLevel(), player, new ItemStack(ToolsItems.ICE_CHARGE.get()));
        charge.snapTo(from.x, from.y, from.z, 0.0F, 0.0F);
        charge.shoot(0.0D, -1.0D, 0.0D, 1.5F, 0.0F);
        helper.getLevel().addFreshEntity(charge);

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(cow.isFullyFrozen(), "the cow is not frozen through"))
                .thenExecute(() -> helper.assertTrue(cow.getHealth() < health, "the ice charge did no damage"))
                .thenSucceed();
    }

    /** A stray killed by a player drops a frost rod half the time, as a blaze does its rod. */
    private static void straysDropFrostRods(GameTestHelper helper) {
        Stray stray = helper.spawn(EntityTypes.STRAY, new BlockPos(4, 1, 4));
        ServerPlayer player = survivalPlayer(helper, ItemStack.EMPTY);
        ServerLevel level = helper.getLevel();
        LootParams params = new LootParams.Builder(level)
                .withParameter(LootContextParams.THIS_ENTITY, stray)
                .withParameter(LootContextParams.ORIGIN, stray.position())
                .withParameter(LootContextParams.DAMAGE_SOURCE, level.damageSources().playerAttack(player))
                .withParameter(LootContextParams.ATTACKING_ENTITY, player)
                .withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player)
                .create(LootContextParamSets.ENTITY);

        int rods = count(helper, EntityTypes.STRAY.getDefaultLootTable().orElseThrow(), params, 100, ToolsItems.FROST_ROD.get());
        helper.assertTrue(rods > 10, "a hundred stray kills dropped only " + rods + " frost rods");
        helper.succeed();
    }

    /** Each staff is rare, one chest in fifty, so a thousand chests all but guarantee one. */
    private static void staffsTurnUpInChests(GameTestHelper helper) {
        LootParams params = new LootParams.Builder(helper.getLevel()).withParameter(LootContextParams.ORIGIN, helper.absoluteVec(Vec3.ZERO)).create(LootContextParamSets.CHEST);

        assertFound(helper, "igloo_chest", params, ToolsItems.NEPTUNE_STAFF.get());
        assertFound(helper, "bastion_treasure", params, ToolsItems.PHOENIX_STAFF.get());
        assertFound(helper, "stronghold_library", params, ToolsItems.POWER_STAFF.get());
        helper.succeed();
    }

    private static boolean craftsNothing(GameTestHelper helper, List<ItemStack> column) {
        return helper.getLevel().recipeAccess().getRecipeFor(RecipeType.CRAFTING, CraftingInput.of(1, 3, column), helper.getLevel()).isEmpty();
    }

    private static void assertFound(GameTestHelper helper, String chest, LootParams params, Item staff) {
        ResourceKey<LootTable> table = ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("chests/" + chest));
        int found = count(helper, table, params, 1000, staff);
        helper.assertTrue(found > 0 && found < 100, "a thousand " + chest + " chests held " + found + " " + staff);
    }

    private static int count(GameTestHelper helper, ResourceKey<LootTable> key, LootParams params, int rolls, Item item) {
        LootTable table = helper.getLevel().getServer().reloadableRegistries().getLootTable(key);
        int found = 0;
        for (int i = 0; i < rolls; i++) {
            for (ItemStack stack : table.getRandomItems(params)) {
                if (stack.is(item)) {
                    found += stack.getCount();
                }
            }
        }
        return found;
    }
}
