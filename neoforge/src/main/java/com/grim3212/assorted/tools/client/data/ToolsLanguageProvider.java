package com.grim3212.assorted.tools.client.data;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import net.minecraft.world.item.Item;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import com.grim3212.assorted.lib.data.LibLanguageProvider;
import com.grim3212.assorted.tools.Constants;
import net.minecraft.data.PackOutput;

/**
 * Generates the en_us.json of this mod. A block, item or entity whose name is its id in title case needs
 * no line here (see {@link LibLanguageProvider}); these are the names that read differently, and
 * every key that is not a name.
 */
public class ToolsLanguageProvider extends LibLanguageProvider {

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

        this.add("item.assortedtools.u_fragment", "\u00A71U\u00A7r Fragment");
        this.add("item.assortedtools.l_fragment", "\u00A72L\u00A7r Fragment");
        this.add("item.assortedtools.t_fragment", "\u00A74T\u00A7r Fragment");
        this.add("item.assortedtools.i_fragment", "\u00A75I\u00A7r Fragment");
        this.add("item.assortedtools.m_fragment", "\u00A76M\u00A7r Fragment");
        this.add("item.assortedtools.a_fragment", "\u00A78A\u00A7r Fragment");
        this.add("item.assortedtools.missing_fragment", "\u00A7k???\u00A7r Fragment");
        this.add("item.assortedtools.e_fragment", "\u00A7dE\u00A7r Fragment");
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
    }

    /** The name a tool material reads as: the vanilla tiers use the adjective, the rest their id. */
    private static String material(String id) {
        return switch (id) {
            case "wood" -> "Wooden";
            case "gold" -> "Golden";
            default -> titleCase(id);
        };
    }
}
