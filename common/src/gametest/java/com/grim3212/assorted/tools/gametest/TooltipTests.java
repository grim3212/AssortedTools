package com.grim3212.assorted.tools.gametest;

import com.grim3212.assorted.lib.core.item.LibDataComponents;
import com.grim3212.assorted.lib.util.NBTHelper;
import com.grim3212.assorted.tools.common.item.BetterBucketItem;
import com.grim3212.assorted.tools.common.item.BucketContents;
import com.grim3212.assorted.tools.common.item.FragmentItem;
import com.grim3212.assorted.tools.common.item.ToolsDataComponents;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.grim3212.assorted.tools.common.item.WandModeInfo;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.grim3212.assorted.lib.test.TestSupport;
import static com.grim3212.assorted.lib.test.TestSupport.*;
import static com.grim3212.assorted.tools.gametest.ToolsTestSupport.*;

/**
 * Tooltip lines carried as default components, checked on the component itself and, on NeoForge,
 * which also builds the full tooltip on the server, in the whole tooltip; {@code
 * ToolsClientGameTests} covers Fabric. The pokeball's is in {@code ProjectileTests}.
 */
final class TooltipTests {

    private TooltipTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("fragment_tooltip_describes_it", TooltipTests::fragmentTooltipDescribesIt);
        out.accept("wand_tooltip_shows_its_mode", TooltipTests::wandTooltipShowsItsMode);
        out.accept("bucket_tooltip_counts_whole_buckets", TooltipTests::bucketTooltipCountsWholeBuckets);
    }

    /** Every fragment carries its line in an {@code assortedlib:description} component. */
    private static void fragmentTooltipDescribesIt(GameTestHelper helper) {
        List<Item> fragments = List.of(ToolsItems.U_FRAGMENT.get(), ToolsItems.L_FRAGMENT.get(), ToolsItems.T_FRAGMENT.get(), ToolsItems.I_FRAGMENT.get(),
                ToolsItems.M_FRAGMENT.get(), ToolsItems.A_FRAGMENT.get(), ToolsItems.MISSING_FRAGMENT.get(), ToolsItems.E_FRAGMENT.get());

        for (Item fragment : fragments) {
            ItemStack stack = new ItemStack(fragment);
            helper.assertValueEqual(tooltipKeys(helper, stack, LibDataComponents.DESCRIPTION.get()), List.of(FragmentItem.DESCRIPTION_KEY), fragment + "'s description");
            assertInFullTooltip(helper, stack, FragmentItem.DESCRIPTION_KEY);
        }
        helper.succeed();
    }

    /**
     * Every wand names the mode stored in its {@code custom_data}, read through its
     * {@code wand_mode_info} component, and one with no mode (from the creative tab or a command)
     * shows the broken line, whose key the lang file has.
     */
    private static void wandTooltipShowsItsMode(GameTestHelper helper) {
        DataComponentType<WandModeInfo> type = ToolsDataComponents.WAND_MODE_INFO.get();
        Map<Item, String> modes = Map.of(
                ToolsItems.BUILDING_WAND.get(), "buildbox", ToolsItems.REINFORCED_BUILDING_WAND.get(), "buildcaves",
                ToolsItems.BREAKING_WAND.get(), "breakweak", ToolsItems.REINFORCED_BREAKING_WAND.get(), "breakxores",
                ToolsItems.MINING_WAND.get(), "mineall", ToolsItems.REINFORCED_MINING_WAND.get(), "mineores");

        modes.forEach((wand, mode) -> {
            ItemStack stack = new ItemStack(wand);
            helper.assertValueEqual(tooltipKeys(helper, stack, type), List.of("assortedtools.wand.broken"), wand + "'s tooltip with no mode");

            NBTHelper.putString(stack, "Mode", mode);
            List<Component> lines = tooltipLines(helper, stack, type);
            helper.assertValueEqual(lines.stream().map(TestSupport::tooltipKey).toList(), List.of("assortedtools.wand.current"), wand + "'s tooltip");
            Object shown = ((TranslatableContents) lines.get(0).getContents()).getArgs()[0];
            helper.assertValueEqual(tooltipKey((Component) shown), "assortedtools.wand.mode." + mode, wand + "'s mode in its tooltip");
            assertInFullTooltip(helper, stack, "assortedtools.wand.current");
        });
        helper.succeed();
    }

    /**
     * A better bucket and a milk bucket both count whole buckets out of their tier's capacity. The
     * milk bucket used to show its raw amount, which is 81000 a bucket on Fabric.
     */
    private static void bucketTooltipCountsWholeBuckets(GameTestHelper helper) {
        int oneBucket = BetterBucketItem.getBucketAmount();

        BetterBucketItem diamond = ToolsItems.DIAMOND_BUCKET.get();
        helper.assertValueEqual(tooltipKeys(helper, diamond.getEmptyStack(), ToolsDataComponents.BUCKET_CONTENTS.get()), List.of("tooltip.buckets.empty"), "an empty bucket's tooltip");

        ItemStack water = diamond.getEmptyStack();
        BetterBucketItem.storeFluid(water, Fluids.WATER, 2 * oneBucket);
        assertContains(helper, water, 2, diamond.getMaximumMillibuckets() / oneBucket, "a diamond bucket of water");

        ItemStack milk = new ItemStack(ToolsItems.WOOD_MILK_BUCKET.get());
        BetterBucketItem.setAmount(milk, oneBucket);
        assertContains(helper, milk, 1, ToolsItems.WOOD_BUCKET.get().getMaximumMillibuckets() / oneBucket, "a wood milk bucket");

        helper.succeed();
    }

    private static void assertContains(GameTestHelper helper, ItemStack stack, int buckets, int capacity, String what) {
        List<Component> lines = tooltipLines(helper, stack, ToolsDataComponents.BUCKET_CONTENTS.get());
        helper.assertValueEqual(lines.stream().map(TestSupport::tooltipKey).toList(), List.of("tooltip.buckets.contains"), what + "'s tooltip");
        List<Object> args = List.of(((TranslatableContents) lines.get(0).getContents()).getArgs());
        helper.assertValueEqual(args, List.<Object>of(buckets, capacity), what + "'s count");
        assertInFullTooltip(helper, stack, "tooltip.buckets.contains");
    }

    private static void assertInFullTooltip(GameTestHelper helper, ItemStack stack, String key) {
        if (onNeoForge()) {
            helper.assertTrue(fullTooltipKeys(helper, stack).contains(key), key + " is missing from " + stack.getItem() + "'s tooltip");
        }
    }
}
