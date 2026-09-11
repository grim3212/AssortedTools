package com.grim3212.assorted.tools.gametest;

import com.grim3212.assorted.tools.api.ToolsTags;
import com.grim3212.assorted.tools.common.enchantment.ToolsEnchantments;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.tools.gametest.ToolsTestSupport.*;

/**
 * Material shears: sheep, leaves and the Coral Cutter enchantment.
 */
final class ShearsTests {

    private ShearsTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("shears_shear_a_sheep", ShearsTests::shearsShearASheep);
        out.accept("shears_cut_leaves", ShearsTests::shearsCutLeaves);
        out.accept("shears_cut_coral_with_coral_cutter", ShearsTests::shearsCutCoralWithCoralCutter);
        out.accept("coral_cutter_covers_every_coral", ShearsTests::coralCutterCoversEveryCoral);
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
}
