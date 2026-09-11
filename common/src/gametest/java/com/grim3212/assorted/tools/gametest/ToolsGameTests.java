package com.grim3212.assorted.tools.gametest;

import net.minecraft.gametest.framework.GameTestHelper;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Automated in-world checks for AssortedTools. The tests live in the {@code *Tests} classes, with
 * shared helpers in {@code ToolsTestSupport}; this only lists them.
 */
public final class ToolsGameTests {

    private ToolsGameTests() {
    }

    /** Every test in this mod, named once, so both loaders register the same set. */
    public static void forEach(BiConsumer<String, Consumer<GameTestHelper>> out) {
        BucketTests.register(out);
        MiningToolTests.register(out);
        ShearsTests.register(out);
        EnchantmentTests.register(out);
        ProjectileTests.register(out);
        TooltipTests.register(out);
        WandTests.register(out);
        MaterialSetTests.register(out);
        AssetTests.register(out);
    }
}
