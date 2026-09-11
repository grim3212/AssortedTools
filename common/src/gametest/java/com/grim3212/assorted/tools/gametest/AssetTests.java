package com.grim3212.assorted.tools.gametest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.common.handlers.ToolsCreativeItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.tools.gametest.ToolsTestSupport.*;

/**
 * What the mod ships: a model and a name for every item, and recipes that load.
 */
final class AssetTests {

    private AssetTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("every_item_has_a_model_and_a_name", AssetTests::everyItemHasAModelAndAName);
        out.accept("every_recipe_loads_or_is_conditioned_off", AssetTests::everyRecipeLoadsOrIsConditionedOff);
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
}
