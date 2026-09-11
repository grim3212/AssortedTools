package com.grim3212.assorted.tools.data;

import com.grim3212.assorted.lib.data.LibItemTagProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.tools.api.ToolsTags;
import com.grim3212.assorted.tools.common.enchantment.ToolsEnchantments;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ToolsItemTagProvider extends LibItemTagProvider {
    public ToolsItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookup, blockTags);
    }

    @Override
    public void addCommonTags(Function<TagKey<Item>, TagAppender<Item>> appender, BiConsumer<TagKey<Block>, TagKey<Item>> copier) {
        // The intrinsic tag appender is gone; TagAppender only accepts ResourceKeys. Wrap it back
        // into something that takes items so the tag lists below stay readable.
        Function<TagKey<Item>, ItemTagger> tagger = (tag) -> new ItemTagger(appender.apply(tag));

        tagger.apply(LibCommonTags.Items.FLUID_CONTAINERS).add(ToolsItems.WOOD_BUCKET.get(), ToolsItems.STONE_BUCKET.get(), ToolsItems.GOLD_BUCKET.get(), ToolsItems.DIAMOND_BUCKET.get(), ToolsItems.NETHERITE_BUCKET.get());
        tagger.apply(LibCommonTags.Items.BUCKETS_MILK).add(ToolsItems.WOOD_MILK_BUCKET.get(), ToolsItems.STONE_MILK_BUCKET.get(), ToolsItems.GOLD_MILK_BUCKET.get(), Items.MILK_BUCKET, ToolsItems.DIAMOND_MILK_BUCKET.get(), ToolsItems.NETHERITE_MILK_BUCKET.get());
        tagger.apply(LibCommonTags.Items.SHEARS).add(ToolsItems.WOOD_SHEARS.get(), ToolsItems.STONE_SHEARS.get(), ToolsItems.GOLD_SHEARS.get(), ToolsItems.DIAMOND_SHEARS.get(), ToolsItems.NETHERITE_SHEARS.get());

        // What an enchantment can go on is data now - the old `canEnchant(stack)` override is gone,
        // and a definition names a HolderSet of supported items instead. These two tags are what
        // those checks became: `instanceof BetterSpearItem` and `instanceof ShearsItem`.
        tagger.apply(ToolsEnchantments.SPEAR_ENCHANTABLE).add(ToolsItems.WOOD_SPEAR.get(), ToolsItems.STONE_SPEAR.get(), ToolsItems.IRON_SPEAR.get(), ToolsItems.GOLD_SPEAR.get(), ToolsItems.DIAMOND_SPEAR.get(), ToolsItems.NETHERITE_SPEAR.get());
        // Vanilla shears are included deliberately: the 1.20.1 check was `instanceof ShearsItem`,
        // which matched them, and the Coral Cutter mixin still keys off the same test.
        tagger.apply(ToolsEnchantments.SHEARS_ENCHANTABLE).add(Items.SHEARS, ToolsItems.WOOD_SHEARS.get(), ToolsItems.STONE_SHEARS.get(), ToolsItems.GOLD_SHEARS.get(), ToolsItems.DIAMOND_SHEARS.get(), ToolsItems.NETHERITE_SHEARS.get());

        // The vanilla-material equivalents of the same. See enchantableTool below for why these
        // entries are load bearing.
        enchantableTool(tagger, ToolsItems.WOOD_HAMMER.get(), ToolsItems.STONE_HAMMER.get(), ToolsItems.GOLD_HAMMER.get(), ToolsItems.IRON_HAMMER.get(), ToolsItems.DIAMOND_HAMMER.get(), ToolsItems.NETHERITE_HAMMER.get());
        enchantableTool(tagger, ToolsItems.WOODEN_MULTITOOL.get(), ToolsItems.STONE_MULTITOOL.get(), ToolsItems.GOLDEN_MULTITOOL.get(), ToolsItems.IRON_MULTITOOL.get(), ToolsItems.DIAMOND_MULTITOOL.get(), ToolsItems.NETHERITE_MULTITOOL.get());
        enchantableMeleeWeapon(tagger, ToolsItems.WOOD_HAMMER.get(), ToolsItems.STONE_HAMMER.get(), ToolsItems.GOLD_HAMMER.get(), ToolsItems.IRON_HAMMER.get(), ToolsItems.DIAMOND_HAMMER.get(), ToolsItems.NETHERITE_HAMMER.get());
        enchantableMeleeWeapon(tagger, ToolsItems.WOODEN_MULTITOOL.get(), ToolsItems.STONE_MULTITOOL.get(), ToolsItems.GOLDEN_MULTITOOL.get(), ToolsItems.IRON_MULTITOOL.get(), ToolsItems.DIAMOND_MULTITOOL.get(), ToolsItems.NETHERITE_MULTITOOL.get());
        enchantableMeleeWeapon(tagger, ToolsItems.ULTIMATE_FIST.get());
        enchantableTrident(tagger, ToolsItems.WOOD_SPEAR.get(), ToolsItems.STONE_SPEAR.get(), ToolsItems.GOLD_SPEAR.get(), ToolsItems.IRON_SPEAR.get(), ToolsItems.DIAMOND_SPEAR.get(), ToolsItems.NETHERITE_SPEAR.get());
        enchantableDurability(tagger, ToolsItems.WOOD_SHEARS.get(), ToolsItems.STONE_SHEARS.get(), ToolsItems.GOLD_SHEARS.get(), ToolsItems.DIAMOND_SHEARS.get(), ToolsItems.NETHERITE_SHEARS.get());
        enchantableDurability(tagger, ToolsItems.WOOD_BUCKET.get(), ToolsItems.STONE_BUCKET.get(), ToolsItems.GOLD_BUCKET.get(), ToolsItems.DIAMOND_BUCKET.get(), ToolsItems.NETHERITE_BUCKET.get());
        enchantableDurability(tagger, ToolsItems.BUILDING_WAND.get(), ToolsItems.BREAKING_WAND.get(), ToolsItems.MINING_WAND.get(), ToolsItems.REINFORCED_BUILDING_WAND.get(), ToolsItems.REINFORCED_BREAKING_WAND.get(), ToolsItems.REINFORCED_MINING_WAND.get());
        enchantableArmor(tagger, ItemTags.HEAD_ARMOR_ENCHANTABLE, ToolsItems.CHICKEN_SUIT_HELMET.get());
        enchantableArmor(tagger, ItemTags.CHEST_ARMOR_ENCHANTABLE, ToolsItems.CHICKEN_SUIT_CHESTPLATE.get());
        enchantableArmor(tagger, ItemTags.LEG_ARMOR_ENCHANTABLE, ToolsItems.CHICKEN_SUIT_LEGGINGS.get());
        enchantableArmor(tagger, ItemTags.FOOT_ARMOR_ENCHANTABLE, ToolsItems.CHICKEN_SUIT_BOOTS.get());

        // Other mods recognise a weapon or a mining tool by these convention tags, which both
        // loaders fill with vanilla items by hand, so a modded tool is only in them if it adds
        // itself. Hammers deal no damage of their own, so they are not melee weapons.
        tagger.apply(LibCommonTags.Items.TOOLS_MELEE_WEAPONS).add(ToolsItems.WOODEN_MULTITOOL.get(), ToolsItems.STONE_MULTITOOL.get(), ToolsItems.GOLDEN_MULTITOOL.get(), ToolsItems.IRON_MULTITOOL.get(), ToolsItems.DIAMOND_MULTITOOL.get(), ToolsItems.NETHERITE_MULTITOOL.get(), ToolsItems.ULTIMATE_FIST.get());
        tagger.apply(LibCommonTags.Items.TOOLS_MINING_TOOLS).add(ToolsItems.WOODEN_MULTITOOL.get(), ToolsItems.STONE_MULTITOOL.get(), ToolsItems.GOLDEN_MULTITOOL.get(), ToolsItems.IRON_MULTITOOL.get(), ToolsItems.DIAMOND_MULTITOOL.get(), ToolsItems.NETHERITE_MULTITOOL.get());

        ToolsItems.MATERIAL_GROUPS.forEach((s, group) -> {
            // Add to top level tags
            tagger.apply(ItemTags.SWORDS).add(group.SWORD.get());
            tagger.apply(ItemTags.PICKAXES).add(group.PICKAXE.get());
            tagger.apply(ItemTags.SHOVELS).add(group.SHOVEL.get());
            tagger.apply(ItemTags.AXES).add(group.AXE.get());
            tagger.apply(ItemTags.HOES).add(group.HOE.get());
            tagger.apply(LibCommonTags.Items.TOOLS_MELEE_WEAPONS).add(group.SWORD.get(), group.AXE.get(), group.MULTITOOL.get());
            tagger.apply(LibCommonTags.Items.TOOLS_MINING_TOOLS).add(group.PICKAXE.get(), group.MULTITOOL.get());
            tagger.apply(ItemTags.HEAD_ARMOR).add(group.HELMET.get());
            tagger.apply(ItemTags.CHEST_ARMOR).add(group.CHESTPLATE.get());
            tagger.apply(ItemTags.LEG_ARMOR).add(group.LEGGINGS.get());
            tagger.apply(ItemTags.FOOT_ARMOR).add(group.BOOTS.get());
            tagger.apply(LibCommonTags.Items.FLUID_CONTAINERS).add(group.BUCKET.get());
            tagger.apply(LibCommonTags.Items.BUCKETS_MILK).add(group.MILK_BUCKET.get());
            tagger.apply(LibCommonTags.Items.SHEARS).add(group.SHEARS.get());
            tagger.apply(ToolsEnchantments.SPEAR_ENCHANTABLE).add(group.SPEAR.get());
            tagger.apply(ToolsEnchantments.SHEARS_ENCHANTABLE).add(group.SHEARS.get());

            enchantableTool(tagger, group.PICKAXE.get(), group.SHOVEL.get(), group.AXE.get(), group.HOE.get(), group.HAMMER.get(), group.MULTITOOL.get());
            enchantableMeleeWeapon(tagger, group.SWORD.get(), group.AXE.get(), group.HAMMER.get(), group.MULTITOOL.get());
            enchantableArmor(tagger, ItemTags.HEAD_ARMOR_ENCHANTABLE, group.HELMET.get());
            enchantableArmor(tagger, ItemTags.CHEST_ARMOR_ENCHANTABLE, group.CHESTPLATE.get());
            enchantableArmor(tagger, ItemTags.LEG_ARMOR_ENCHANTABLE, group.LEGGINGS.get());
            enchantableArmor(tagger, ItemTags.FOOT_ARMOR_ENCHANTABLE, group.BOOTS.get());
            enchantableTrident(tagger, group.SPEAR.get());
            enchantableDurability(tagger, group.SHEARS.get(), group.BUCKET.get());
        });
        tagger.apply(ToolsTags.Items.ULTIMATE_FRAGMENTS).add(ToolsItems.U_FRAGMENT.get(), ToolsItems.L_FRAGMENT.get(), ToolsItems.T_FRAGMENT.get(), ToolsItems.I_FRAGMENT.get(), ToolsItems.M_FRAGMENT.get(), ToolsItems.A_FRAGMENT.get(), ToolsItems.MISSING_FRAGMENT.get(), ToolsItems.E_FRAGMENT.get());

        tagger.apply(ItemTags.PIGLIN_LOVED).add(ToolsItems.GOLD_HAMMER.get(), ToolsItems.GOLDEN_MULTITOOL.get(), ToolsItems.GOLD_SPEAR.get(), ToolsItems.BUILDING_WAND.get(), ToolsItems.REINFORCED_BUILDING_WAND.get(), ToolsItems.GOLD_BUCKET.get(), ToolsItems.GOLD_MILK_BUCKET.get(), ToolsItems.GOLD_SHEARS.get());

        tagger.apply(ToolsTags.Items.CAGE_SUPPORTED).add(ToolsItems.POKEBALL.get());
    }

    /**
     * Adds items to {@code #minecraft:enchantable/*} tags. They are opt-in: without these entries
     * every tool, weapon and armour piece is silently <b>unenchantable at a table</b>.
     */
    private void enchantableTool(Function<TagKey<Item>, ItemTagger> tagger, Item... items) {
        tagger.apply(ItemTags.MINING_ENCHANTABLE).add(items);
        tagger.apply(ItemTags.MINING_LOOT_ENCHANTABLE).add(items);
        enchantableDurability(tagger, items);
    }

    private void enchantableMeleeWeapon(Function<TagKey<Item>, ItemTagger> tagger, Item... items) {
        tagger.apply(ItemTags.WEAPON_ENCHANTABLE).add(items);
        tagger.apply(ItemTags.MELEE_WEAPON_ENCHANTABLE).add(items);
        tagger.apply(ItemTags.SHARP_WEAPON_ENCHANTABLE).add(items);
        tagger.apply(ItemTags.FIRE_ASPECT_ENCHANTABLE).add(items);
        enchantableDurability(tagger, items);
    }

    /**
     * Loyalty and Impaling. The tag also carries Riptide and Channeling, which {@code BetterSpearItem}
     * vetoes by key through {@code IItemEnchantmentCondition}.
     */
    private void enchantableTrident(Function<TagKey<Item>, ItemTagger> tagger, Item... items) {
        tagger.apply(ItemTags.TRIDENT_ENCHANTABLE).add(items);
        enchantableDurability(tagger, items);
    }

    private void enchantableArmor(Function<TagKey<Item>, ItemTagger> tagger, TagKey<Item> slotTag, Item... items) {
        tagger.apply(slotTag).add(items);
        tagger.apply(ItemTags.ARMOR_ENCHANTABLE).add(items);
        tagger.apply(ItemTags.EQUIPPABLE_ENCHANTABLE).add(items);
        enchantableDurability(tagger, items);
    }

    /**
     * Unbreaking, Mending and Curse of Vanishing, which every damageable item should accept.
     */
    private void enchantableDurability(Function<TagKey<Item>, ItemTagger> tagger, Item... items) {
        tagger.apply(ItemTags.DURABILITY_ENCHANTABLE).add(items);
        tagger.apply(ItemTags.VANISHING_ENCHANTABLE).add(items);
    }

    private record ItemTagger(TagAppender<Item> appender) {

        ItemTagger add(Item... items) {
            for (Item item : items) {
                this.appender.add(BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow());
            }

            return this;
        }

        ItemTagger addTag(TagKey<Item> tag) {
            this.appender.addTag(tag);
            return this;
        }
    }
}
