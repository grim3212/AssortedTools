package com.grim3212.assorted.tools.client.data;

import com.grim3212.assorted.lib.data.LibManualProvider;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import com.grim3212.assorted.tools.common.crafting.ToolsConditions;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.util.function.Predicate;

/**
 * This mod's section of the instruction manual. The families here are named by the shape of their
 * ids rather than listed, so a material added to the config lands on its page without a change
 * here. Each page shows a worked example; the whole family opens it.
 */
public class ToolsManualProvider extends LibManualProvider {

    public ToolsManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addChapters() {
        this.section(120, ToolsItems.MATERIAL_GROUPS.get("steel").MULTITOOL.get());

        this.addTools();
        this.addWeapons();
        this.addBuckets();
        this.addArmor();
        this.addWands();
        this.addStaffs();
        this.addUltimate();
        this.addPokeball();
    }

    private void addTools() {
        ChapterBuilder tools = this.chapter("tools");

        tools.text("materials");
        tools.recipesById("basic", recipeId("steel_pickaxe"), recipeId("steel_axe"), recipeId("steel_shovel"), recipeId("steel_hoe"), recipeId("steel_sword")).whenPartEnabled(ToolsConditions.Parts.EXTRA_MATERIAL)
                .every(50)
                .opensEveryItem(endsWithAny("_pickaxe", "_axe", "_shovel", "_hoe", "_sword"));
        tools.recipes("hammers", ToolsItems.WOOD_HAMMER.get(), ToolsItems.IRON_HAMMER.get(), ToolsItems.NETHERITE_HAMMER.get()).whenPartEnabled(ToolsConditions.Parts.HAMMERS).every(50)
                .opensEveryItem(endsWith("_hammer"));
        tools.recipesById("multitools", recipeId(ToolsItems.IRON_MULTITOOL.get()), recipeId("steel_multitool"), recipeId(ToolsItems.DIAMOND_MULTITOOL.get()), recipeId(ToolsItems.NETHERITE_MULTITOOL.get())).whenPartEnabled(ToolsConditions.Parts.EXTRA_MATERIAL, ToolsConditions.Parts.MULTITOOL).every(50)
                .opensEveryItem(endsWith("_multitool"));
        tools.recipes("machetes", ToolsItems.WOOD_MACHETE.get(), ToolsItems.IRON_MACHETE.get(), ToolsItems.DIAMOND_MACHETE.get()).whenPartEnabled(ToolsConditions.Parts.MACHETES).every(50)
                .opensEveryItem(endsWith("_machete"));
        tools.recipes("portable_workbench", ToolsItems.PORTABLE_WORKBENCH.get()).whenPartEnabled(ToolsConditions.Parts.PORTABLE_WORKBENCH).opens(ToolsItems.PORTABLE_WORKBENCH.get());
        tools.recipesById("shears", recipeId(ToolsItems.WOOD_SHEARS.get()), recipeId("steel_shears"), recipeId(ToolsItems.NETHERITE_SHEARS.get())).whenPartEnabled(ToolsConditions.Parts.EXTRA_MATERIAL, ToolsConditions.Parts.MORE_SHEARS)
                .every(50).opensEveryItem(endsWith("_shears"));
    }

    private void addWeapons() {
        ChapterBuilder weapons = this.chapter("weapons");

        // A throwing spear is its own page, so the melee spear has to exclude it.
        weapons.recipesById("spears", recipeId("steel_spear"), recipeId("ruby_spear"), recipeId("emerald_spear")).whenPartEnabled(ToolsConditions.Parts.EXTRA_MATERIAL, ToolsConditions.Parts.SPEARS).every(50)
                .opensEveryItem(id -> id.getPath().endsWith("_spear") && !id.getPath().endsWith("_throwing_spear"));
        weapons.recipes("throwing_spears", ToolsItems.WOOD_THROWING_SPEAR.get(), ToolsItems.IRON_THROWING_SPEAR.get(), ToolsItems.DIAMOND_THROWING_SPEAR.get()).whenPartEnabled(ToolsConditions.Parts.THROWING_SPEARS).every(50)
                .opensEveryItem(endsWith("_throwing_spear"));
        weapons.recipes("boomerangs", ToolsItems.WOOD_BOOMERANG.get(), ToolsItems.DIAMOND_BOOMERANG.get()).whenPartEnabled(ToolsConditions.Parts.BOOMERANGS).every(50)
                .opensEveryItem(endsWith("_boomerang"));
    }

    private void addBuckets() {
        ChapterBuilder buckets = this.chapter("buckets").whenPartEnabled(ToolsConditions.Parts.BETTER_BUCKETS);

        buckets.recipes("buckets", ToolsItems.WOOD_BUCKET.get(), ToolsItems.STONE_BUCKET.get(), ToolsItems.GOLD_BUCKET.get(), ToolsItems.DIAMOND_BUCKET.get(), ToolsItems.NETHERITE_BUCKET.get()).every(50)
                .opensEveryItem(id -> id.getPath().endsWith("_bucket") && !id.getPath().endsWith("_milk_bucket"));
        // Milk is taken from an animal rather than crafted, so there is no recipe to draw.
        buckets.items("milk", item("wood_milk_bucket"), item("diamond_milk_bucket"), item("netherite_milk_bucket")).whenPartEnabled(ToolsConditions.Parts.BETTER_BUCKETS)
                .every(50).opensEveryItem(endsWith("_milk_bucket"));
    }

    private void addArmor() {
        ChapterBuilder armor = this.chapter("armor").whenPartEnabled(ToolsConditions.Parts.EXTRA_MATERIAL);

        // The chicken suit is its own page, so the plain sets exclude it.
        armor.recipesById("armor", recipeId("steel_helmet"), recipeId("steel_chestplate"), recipeId("steel_leggings"), recipeId("steel_boots"))
                .every(50)
                .opensEveryItem(id -> isArmor(id) && !id.getPath().startsWith("chicken_suit_"));
        armor.recipes("chicken_suit", ToolsItems.CHICKEN_SUIT_HELMET.get(), ToolsItems.CHICKEN_SUIT_CHESTPLATE.get(), ToolsItems.CHICKEN_SUIT_LEGGINGS.get(), ToolsItems.CHICKEN_SUIT_BOOTS.get()).whenPartEnabled(ToolsConditions.Parts.CHICKEN_SUIT).every(50)
                .opensEveryItem(id -> id.getPath().startsWith("chicken_suit_"));
    }

    private void addWands() {
        ChapterBuilder wands = this.chapter("wands").whenPartEnabled(ToolsConditions.Parts.WANDS);

        wands.text("modes");
        wands.recipes("basic", ToolsItems.BUILDING_WAND.get(), ToolsItems.MINING_WAND.get(), ToolsItems.BREAKING_WAND.get()).every(60)
                .opensEveryItem(id -> id.getPath().endsWith("_wand") && !id.getPath().startsWith("reinforced_"));
        wands.recipes("reinforced", ToolsItems.REINFORCED_BUILDING_WAND.get(), ToolsItems.REINFORCED_MINING_WAND.get(), ToolsItems.REINFORCED_BREAKING_WAND.get())
                .every(60)
                .opensEveryItem(id -> id.getPath().endsWith("_wand") && id.getPath().startsWith("reinforced_"));
    }

    private void addStaffs() {
        ChapterBuilder staffs = this.chapter("staffs");

        staffs.recipes("neptune", ToolsItems.NEPTUNE_STAFF.get()).whenPartEnabled(ToolsConditions.Parts.STAFFS).opens(ToolsItems.NEPTUNE_STAFF.get());
        staffs.recipes("phoenix", ToolsItems.PHOENIX_STAFF.get()).whenPartEnabled(ToolsConditions.Parts.STAFFS).opens(ToolsItems.PHOENIX_STAFF.get());
        staffs.recipes("frost", ToolsItems.FROST_POWDER.get(), ToolsItems.ICE_CHARGE.get()).every(60).whenPartEnabled(ToolsConditions.Parts.STAFFS).opens(ToolsItems.FROST_ROD.get(), ToolsItems.FROST_POWDER.get(), ToolsItems.ICE_CHARGE.get());
        staffs.recipes("power", ToolsItems.POWER_STAFF.get()).whenPartEnabled(ToolsConditions.Parts.POWER_STAFF).opens(ToolsItems.POWER_STAFF.get());
    }

    private void addUltimate() {
        ChapterBuilder ultimate = this.chapter("ultimate").whenPartEnabled(ToolsConditions.Parts.ULTIMATE_FIST);

        // Fragments are chest loot only.
        ultimate.items("fragments", item("u_fragment"), item("l_fragment"), item("t_fragment"), item("i_fragment"),
                        item("m_fragment"), item("a_fragment"), item("missing_fragment"), item("e_fragment"))
                .every(30).opensEveryItem(endsWith("_fragment"));
        ultimate.recipes("fist", ToolsItems.ULTIMATE_FIST.get()).opens(ToolsItems.ULTIMATE_FIST.get());
    }

    private void addPokeball() {
        this.chapter("pokeball").whenPartEnabled(ToolsConditions.Parts.POKEBALL).recipes("pokeball", ToolsItems.POKEBALL.get()).opens(ToolsItems.POKEBALL.get());
    }

    private static boolean isArmor(Identifier id) {
        String path = id.getPath();
        return path.endsWith("_helmet") || path.endsWith("_chestplate")
                || path.endsWith("_leggings") || path.endsWith("_boots");
    }

    private static Predicate<Identifier> endsWith(String suffix) {
        return id -> id.getPath().endsWith(suffix);
    }

    private static Predicate<Identifier> endsWithAny(String... suffixes) {
        return id -> {
            for (String suffix : suffixes) {
                if (id.getPath().endsWith(suffix)) {
                    return true;
                }
            }
            return false;
        };
    }

    private static net.minecraft.world.item.Item item(String path) {
        return net.minecraft.core.registries.BuiltInRegistries.ITEM
                .getValue(Identifier.fromNamespaceAndPath(Constants.MOD_ID, path));
    }
}
