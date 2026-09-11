package com.grim3212.assorted.tools.gametest;

import net.minecraft.gametest.framework.GameTestHelper;
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
 * <p>
 * The tests themselves are split by feature into the {@code *Tests} classes in this package,
 * with shared helpers in {@code ToolsTestSupport}; this only lists them.
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
        WandTests.register(out);
        MaterialSetTests.register(out);
        AssetTests.register(out);
    }
}
