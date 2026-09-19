package com.grim3212.assorted.tools.client.data;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import net.minecraft.world.item.Item;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import com.grim3212.assorted.lib.data.LibLanguageProvider;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.api.ToolsTags;
import com.grim3212.assorted.tools.data.ToolsItemTagProvider;
import net.minecraft.data.PackOutput;

/**
 * Generates the en_us.json of this mod. A block, item or entity whose name is its id in title case needs
 * no line here (see {@link LibLanguageProvider}); these are the names that read differently, and
 * every key that is not a name.
 */
public class ToolsLanguageProvider extends LibLanguageProvider {

    /** A blank line between paragraphs; the manual splits its text the way the font does. */
    private static final String BREAK = "\n\n";

    public ToolsLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup.assortedtools", "Assorted Tools");

        this.add("key.category.assortedtools.general", "Assorted Tools");
        this.add("key.assortedtools.tool_switch_modes", "Switch Tool Modes");

        this.add("enchantment.assortedtools.chicken_jump", "Chicken Jump");
        this.add("enchantment.assortedtools.chicken_jump.desc", "Lets you hover like a chicken as well as get one extra jump per armor enchanted with Chicken Jump");
        this.add("enchantment.assortedtools.conductive", "Conductive");
        this.add("enchantment.assortedtools.conductive.desc", "When a throwing spear hits something there will be a chance for it to cause a lightning strike at that location.");
        this.add("enchantment.assortedtools.flammable", "Flammable");
        this.add("enchantment.assortedtools.flammable.desc", "When a throwing spear hits something there will be a chance for it to cause fire to be created at that location.");
        this.add("enchantment.assortedtools.unstable", "Unstable");
        this.add("enchantment.assortedtools.unstable.desc", "When a throwing spear hits something there will be an explosion at that location.");
        this.add("enchantment.assortedtools.bounciness", "Bounciness");
        this.add("enchantment.assortedtools.bounciness.desc", "When a throwing spear hit a block it will be able to bounce a number of times dependant on the level of bounciness.");
        this.add("enchantment.assortedtools.coral_cutter", "Coral Cutter");
        this.add("enchantment.assortedtools.coral_cutter.desc", "Lets you harvest Coral with this enchantment instead of needing a tool with Silk Touch.");

        this.add("death.attack.assortedtools.spear", "%1$s was speared by %2$s");
        this.add("death.attack.assortedtools.spear.item", "%1$s was speared by %2$s with %3$s");
        this.add("death.attack.assortedtools.boomerang", "%1$s was boomerang'd by %2$s");
        this.add("death.attack.assortedtools.boomerang.item", "%1$s was boomerang'd by %2$s with %3$s");

        this.add("entity.assortedtools.spear", "Throwing Spear");
        this.add("entity.assortedtools.better_spear", "Throwing Spear");
        this.add("entity.assortedtools.ice_charge", "Ice Charge");

        this.add("item.assortedtools.u_fragment", "\u00A71U\u00A7r Fragment");
        this.add("item.assortedtools.l_fragment", "\u00A72L\u00A7r Fragment");
        this.add("item.assortedtools.t_fragment", "\u00A74T\u00A7r Fragment");
        this.add("item.assortedtools.i_fragment", "\u00A75I\u00A7r Fragment");
        this.add("item.assortedtools.m_fragment", "\u00A76M\u00A7r Fragment");
        this.add("item.assortedtools.a_fragment", "\u00A78A\u00A7r Fragment");
        this.add("item.assortedtools.missing_fragment", "\u00A7k???\u00A7r Fragment");
        this.add("item.assortedtools.e_fragment", "\u00A7dE\u00A7r Fragment");
        this.add("item.assortedtools.frost_rod", "Frost Rod");
        this.add("item.assortedtools.frost_powder", "Frost Powder");
        this.add("item.assortedtools.ice_charge", "Ice Charge");
        this.add("item.assortedtools.chicken_suit_helmet", "Chicken Hat");
        this.add("item.assortedtools.chicken_suit_chestplate", "Chicken Suit");
        this.add("item.assortedtools.chicken_suit_leggings", "Chicken Legs");
        this.add("item.assortedtools.chicken_suit_boots", "Chicken Boots");

        this.add("tooltip.ultimate.fragment", "A fragment of a powerful tool from an ancient civilization");
        this.add("tooltip.pokeball.stored", "Stored: %s");
        this.add("tooltip.pokeball.stored_custom_name", "Stored: %s (%s)");
        this.add("tooltip.pokeball.empty", "Empty");
        this.add("tooltip.buckets.empty", "Empty");
        this.add("tooltip.buckets.contains", "Contains %s/%s Buckets");

        this.add("assortedtools.wand.mode.buildbox", "\u00A77Build box");
        this.add("assortedtools.wand.mode.buildroom", "\u00A77Build room");
        this.add("assortedtools.wand.mode.buildframe", "\u00A77Build frame");
        this.add("assortedtools.wand.mode.buildtorches", "\u00A7eBuild Torches");
        this.add("assortedtools.wand.mode.buildwater", "\u00A79Fill water");
        this.add("assortedtools.wand.mode.buildlava", "\u00A7cFill lava");
        this.add("assortedtools.wand.mode.buildcaves", "\u00A78Fill caves");
        this.add("assortedtools.wand.mode.breakweak", "\u00A7aBreak Weak");
        this.add("assortedtools.wand.mode.breakall", "\u00A74Break All");
        this.add("assortedtools.wand.mode.breakxores", "\u00A7bLeave Ores");
        this.add("assortedtools.wand.mode.mineall", "\u00A74Mine all");
        this.add("assortedtools.wand.mode.minedirt", "\u00A78Mine dirt");
        this.add("assortedtools.wand.mode.minewood", "\u00A7aMine wood");
        this.add("assortedtools.wand.mode.mineores", "\u00A7bSurface mine");
        this.add("assortedtools.wand.current", "\u00A7lCurrent\u00A7r: %s");
        this.add("assortedtools.wand.broken", "\u00A7lThis wand got screwed up.");
        this.add("assortedtools.wand.switched", "Switched wand mode to %s");

        this.add("assortedtools.staff.mode.place_water", "Place Water");
        this.add("assortedtools.staff.mode.freeze_mobs", "Freeze Mobs");
        this.add("assortedtools.staff.mode.freeze_water", "Freeze Water");
        this.add("assortedtools.staff.mode.place_lava", "Place Lava");
        this.add("assortedtools.staff.mode.place_fire", "Place Fire");
        this.add("assortedtools.staff.mode.thaw_mobs", "Thaw Mobs");
        this.add("assortedtools.staff.mode.melt_ice", "Melt Ice");
        this.add("assortedtools.staff.mode.float_push", "Floating Push");
        this.add("assortedtools.staff.mode.float_pull", "Floating Pull");
        this.add("assortedtools.staff.mode.drop_push", "Dropping Push");
        this.add("assortedtools.staff.mode.drop_pull", "Dropping Pull");
        this.add("assortedtools.staff.current", "\u00A7lMode\u00A7r: %s");
        this.add("assortedtools.staff.switched", "Switched staff mode to %s");

        this.add("result.wand.fill", "%s blocks filled up with stone.");
        this.add("result.wand.mine", "No ores found.");

        this.add("error.wand.nocave", "No caves were found.");
        this.add("error.wand.nowork", "No work to do.");
        this.add("error.wand.nostart", "You didn't select the starting block!");
        this.add("error.wand.cantbuild", "Can't build this block!");
        this.add("error.wand.toofar", "That's too far!");
        this.add("error.wand.toofewitems", "You don't have enough items (needed %s, you have %s).");
        this.add("error.wand.toomany", "Too many blocks to dig (%s, limit=%s).");
        this.add("error.wand.notsamecorner", "You can't do this! Corner blocks must be the same.");
        this.add("error.wand.cantfillcave", "You need a REINFORCED wand for cave filling!");
        this.add("error.wand.cantfilllava", "You need a REINFORCED wand for lava filling!");
        this.add("error.wand.cantfillwater", "You need a REINFORCED wand for water filling!");
        this.add("error.wand.cantminesurface", "You need a REINFORCED wand for surface mining!");
        this.add("error.wand.toofewlava", "You don't have enough lava buckets.");
        this.add("error.wand.toofewwater", "You need two buckets of water.");

        this.add("tag.item.assorteddecor.cage_supported", "Cage Supported Items");
        this.add("tag.item.assortedtools.enchantable.shears", "Enchantable Shears");
        this.add("tag.item.assortedtools.enchantable.throwing_spear", "Enchantable Throwing Spears");
        this.add("tag.item.assortedtools.ultimate_fragments", "Ultimate Fragments");
        this.add("tag.item.assortedtools.machetes", "Machetes");
        this.add("tag.item.c.rods.frost", "Frost Rods");
        for (String material : ToolsItemTagProvider.materialToolNames()) {
            for (String kind : ToolsTags.Items.MATERIAL_TOOL_KINDS) {
                this.add("tag.item.c." + kind + "." + material, titleCase(material) + " " + titleCase(kind));
            }
        }

        // A bucket names its filled form through a key of its own rather than an item.
        Pattern bucket = Pattern.compile("(.+)_bucket");
        for (Item item : BuiltInRegistries.ITEM) {
            Identifier id = BuiltInRegistries.ITEM.getKey(item);
            Matcher matcher = bucket.matcher(id.getPath());
            if (Constants.MOD_ID.equals(id.getNamespace()) && matcher.matches() && !matcher.group(1).endsWith("milk")) {
                this.add("item." + Constants.MOD_ID + "." + matcher.group(1) + "_bucket_filled", material(matcher.group(1)) + " %s Bucket");
            }
        }

        // Families whose names read differently from their ids.
        this.nameItems("(.+)_multitool", m -> material(m.group(1)) + " MultiTool");
        // The vanilla-style spears read as their ids; the thrown ones say so.
        this.nameItems("(.+)_throwing_spear", m -> material(m.group(1)) + " Throwing Spear");
        this.nameItems("(wood|gold)_(.+)", m -> material(m.group(1)) + " " + titleCase(m.group(2)));

        this.addManual();
    }

    /** The name a tool material reads as: the vanilla tiers use the adjective, the rest their id. */
    private static String material(String id) {
        return switch (id) {
            case "wood" -> "Wooden";
            case "gold" -> "Golden";
            default -> titleCase(id);
        };
    }

    /** The chapters in {@code assets/assortedtools/manual} name these keys. */
    private void addManual() {
        this.add("manual.assortedtools.title", "Assorted Tools");
        this.add("manual.assortedtools.description",
                "Tool and armor sets for every material, buckets that hold more than one, throwing weapons "
                        + "and the wands that do the digging for you.");

        this.addToolsChapter();
        this.addWeaponsChapter();
        this.addBucketsChapter();
        this.addArmorChapter();
        this.addWandsChapter();
        this.addStaffsChapter();
        this.addUltimateChapter();
        this.addPokeballChapter();
    }

    private void addToolsChapter() {
        this.add("manual.assortedtools.chapter.tools", "Tools");

        this.add("manual.assortedtools.chapter.tools.materials.title", "Materials");
        this.add("manual.assortedtools.chapter.tools.materials",
                "Every metal and gem Assorted Core adds gets a full tool set here, so tin, silver, bronze, "
                        + "steel, ruby and the rest of them." + BREAK
                        + "Vanilla's own materials are not repeated as pickaxes and swords, since the game "
                        + "already has those. They do turn up in the tools vanilla has no version of though like hammers, "
                        + "multitools, shears, spears and buckets all go from wood up to netherite.");

        this.add("manual.assortedtools.chapter.tools.basic.title", "The Basic Set");
        this.add("manual.assortedtools.chapter.tools.basic",
                "Pickaxe, axe, shovel, hoe and sword, in every material. They behave exactly as the vanilla ones do.");

        this.add("manual.assortedtools.chapter.tools.hammers.title", "Hammers");
        this.add("manual.assortedtools.chapter.tools.hammers",
                "A hammer destroys a block in one hit and drops nothing at all." + BREAK
                        + "Be careful where you use it but they can come in handy.");

        this.add("manual.assortedtools.chapter.tools.multitools.title", "Multitools");
        this.add("manual.assortedtools.chapter.tools.multitools",
                "A multitool is a sword, pickaxe, axe, shovel and hoe in one slot, and mines all of them at its "
                        + "material's speed.");

        this.add("manual.assortedtools.chapter.tools.machetes.title", "Machetes");
        this.add("manual.assortedtools.chapter.tools.machetes",
                "A machete is a lighter, quicker sword that cuts through leaves, vines, wool, cactus and the "
                        + "rest of the undergrowth." + BREAK
                        + "They come in every material, from wood up to netherite.");

        this.add("manual.assortedtools.chapter.tools.portable_workbench.title", "Portable Workbench");
        this.add("manual.assortedtools.chapter.tools.portable_workbench",
                "A crafting table you can carry. Right click with it to craft on the spot.");

        this.add("manual.assortedtools.chapter.tools.shears.title", "Shears");
        this.add("manual.assortedtools.chapter.tools.shears",
                "Shears in every material, from wood up to netherite. Also lookout for a new Shears enchantment that lets you harvest coral without needing Silk Touch.");
    }

    private void addWeaponsChapter() {
        this.add("manual.assortedtools.chapter.weapons", "Thrown Weapons");

        this.add("manual.assortedtools.chapter.weapons.spears.title", "Spears");
        this.add("manual.assortedtools.chapter.weapons.spears",
                "These spears act exactly like the Vanilla spears in Minecraft but in all of the different materials we support.");

        this.add("manual.assortedtools.chapter.weapons.throwing_spears.title", "Throwing Spears");
        this.add("manual.assortedtools.chapter.weapons.throwing_spears",
                "A throwing spear is thrown with right click and sticks where it lands, ready to be picked "
                        + "back up." + BREAK
                        + "They come in every material and also make sure you checkout the enchantment table for some fun with these.");

        this.add("manual.assortedtools.chapter.weapons.boomerangs.title", "Boomerangs");
        this.add("manual.assortedtools.chapter.weapons.boomerangs",
                "A boomerang is thrown with right click, flies out to its limit and comes back to you. It hurts "
                        + "what it passes through on the way." + BREAK
                        + "The diamond one goes further and hits harder, and can be set to follow where you are "
                        + "looking rather than flying straight." + BREAK
                        + "They might even pickup some items on the way.");
    }

    private void addBucketsChapter() {
        this.add("manual.assortedtools.chapter.buckets", "Buckets");

        this.add("manual.assortedtools.chapter.buckets.buckets.title", "Better Buckets");
        this.add("manual.assortedtools.chapter.buckets.buckets",
                "These buckets hold more than one bucket of what is in them, and the material decides how "
                        + "many." + BREAK
                        + "Some materials are very weak and are destroyed when they are emptied. " + BREAK
                        + "Hotter fluids need a sturdier bucket, so what a bucket can pick up is a question of "
                        + "material as well.");

        this.add("manual.assortedtools.chapter.buckets.milk.title", "Milking");
        this.add("manual.assortedtools.chapter.buckets.milk",
                "Any of these buckets can be used to milk cows." + BREAK
                        + "What you can milk depends on the bucket. All of them can manage a cow, better ones "
                        + "also manage a sheep, and the best of them a pig as well.");
    }

    private void addArmorChapter() {
        this.add("manual.assortedtools.chapter.armor", "Armor");

        this.add("manual.assortedtools.chapter.armor.armor.title", "Armor Sets");
        this.add("manual.assortedtools.chapter.armor.armor",
                "A full four piece set for every material this mod adds tools for, on the same ladder the "
                        + "tools use.");

        this.add("manual.assortedtools.chapter.armor.chicken_suit.title", "Chicken Suit");
        this.add("manual.assortedtools.chapter.armor.chicken_suit",
                "The chicken suit is not about protection. Every piece you are wearing gives you one more jump "
                        + "in the air, so the full set is four jumps." + BREAK
                        + "It is not flying. It is close enough to get across most things, and to get you down "
                        + "off most of the rest safely." + BREAK + 
                        "Try combining them with the corresponding piece in an Anvil to see what happens.");
    }

    private void addWandsChapter() {
        this.add("manual.assortedtools.chapter.wands", "Wands");

        this.add("manual.assortedtools.chapter.wands.modes.title", "How Wands Work");
        this.add("manual.assortedtools.chapter.wands.modes",
                "Each wand has several modes and you switch between them with the mode keybind, Z by default." + BREAK
                        + "A wand works on a region rather than a block, so it will tell you when you have "
                        + "picked too much, when the corners do not match, or when you do not have the blocks "
                        + "to finish the job. Nothing happens until it can be done properly." + BREAK
                        + "Wands have a limited number of uses.");

        this.add("manual.assortedtools.chapter.wands.basic.title", "Basic Wands");
        this.add("manual.assortedtools.chapter.wands.basic",
                "Three wands. The building wand puts up boxes, frames, rooms and lines of torches. The mining "
                        + "wand clears wood, dirt or ore. The breaking wand takes everything out, or leaves the "
                        + "ores where they are if you ask it to.");

        this.add("manual.assortedtools.chapter.wands.reinforced.title", "Reinforced Wands");
        this.add("manual.assortedtools.chapter.wands.reinforced",
                "The reinforced version of each wand lasts far longer and unlocks the modes the basic one "
                        + "refuses." + BREAK
                        + "Filling a cave, flooding a space with water, or pouring lava into one, and surface "
                        + "mining, all need a reinforced wand.");
    }

    private void addStaffsChapter() {
        this.add("manual.assortedtools.chapter.staffs", "Staffs");

        this.add("manual.assortedtools.chapter.staffs.neptune.title", "Neptune Staff");
        this.add("manual.assortedtools.chapter.staffs.neptune",
                "Switch its mode with the tool mode key, Z by default." + BREAK
                        + "Place Water pours a water source where you click. Freeze Mobs freezes every mob around "
                        + "you solid, and Freeze Water turns the still water around you to ice." + BREAK
                        + "It is no weapon, but anything it hits is frozen where it stands.");

        this.add("manual.assortedtools.chapter.staffs.phoenix.title", "Phoenix Staff");
        this.add("manual.assortedtools.chapter.staffs.phoenix",
                "The Neptune staff's the opposite. Place Lava and Place Fire where you click, Thaw Mobs "
                        + "frees every frozen mob around you, and Melt Ice melts the ice around you." + BREAK
                        + "A hit with it thaws a frozen mob.");

        this.add("manual.assortedtools.chapter.staffs.frost.title", "Frost Rods");
        this.add("manual.assortedtools.chapter.staffs.frost",
                "The blaze rod's cold counterpart. Strays drop them as blazes drop theirs, and any other "
                        + "monster killed in a snowy biome sometimes does." + BREAK
                        + "A frost rod grinds into frost powder, and frost powder, gunpowder and a snowball make "
                        + "ice charges.");

        this.add("manual.assortedtools.chapter.staffs.power.title", "Power Staff");
        this.add("manual.assortedtools.chapter.staffs.power",
                "Right click a block to push it one step away, or in a pull mode to drag it one step toward "
                        + "the side you clicked.");
    }

    private void addUltimateChapter() {
        this.add("manual.assortedtools.chapter.ultimate", "The Ultimate Fist");

        this.add("manual.assortedtools.chapter.ultimate.fragments.title", "Fragments");
        this.add("manual.assortedtools.chapter.ultimate.fragments",
                "Eight fragments of something an older civilization built, found in chests across Minecraft." + BREAK
                        + "Laid out in order they spell out what they came from.");

        this.add("manual.assortedtools.chapter.ultimate.fist.title", "The Ultimate Fist");
        this.add("manual.assortedtools.chapter.ultimate.fist",
                "All eight fragments and a nether star make the ultimate fist. It is extremely powerful and out "
                        + "of the box it mines very fast and hits hard enough to kill most things in one hit.");
    }

    private void addPokeballChapter() {
        this.add("manual.assortedtools.chapter.pokeball", "Pokeball");

        this.add("manual.assortedtools.chapter.pokeball.pokeball.title", "Pokeball");
        this.add("manual.assortedtools.chapter.pokeball.pokeball",
                "Throw a pokeball at a mob and it goes inside, exactly as it was. So health, name, anything it "
                        + "was carrying. Throw it again and the mob comes back out." + BREAK
                        + "A hostile mob let out of a pokeball is still hostile, and "
                        + "still remembers you so be careful.");
    }
}
