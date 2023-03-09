package com.grim3212.assorted.decor.data;

import com.grim3212.assorted.decor.api.ToolsTags;
import com.grim3212.assorted.decor.common.item.ToolsItems;
import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.data.tags.VanillaBlockTagsProvider;
import net.minecraft.data.tags.VanillaItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.apache.commons.lang3.NotImplementedException;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class ToolsCommonTagProvider {

    public static class BlockTagProvider extends VanillaBlockTagsProvider {

        public BlockTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup) {
            super(packOutput, lookup);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            throw new NotImplementedException();
        }

        @Override
        protected IntrinsicTagAppender<Block> tag(TagKey<Block> tag) {
            throw new NotImplementedException();
        }

        public void addCommonTags(Function<TagKey<Block>, IntrinsicTagAppender<Block>> tagger) {
            this.tag(ToolsTags.Blocks.MINEABLE_MULTITOOL).addTag(BlockTags.MINEABLE_WITH_AXE).addTag(BlockTags.MINEABLE_WITH_HOE).addTag(BlockTags.MINEABLE_WITH_PICKAXE).addTag(BlockTags.MINEABLE_WITH_SHOVEL);

            this.tag(ToolsTags.Blocks.DEAD_CORALS).add(Blocks.DEAD_TUBE_CORAL, Blocks.DEAD_BRAIN_CORAL, Blocks.DEAD_BUBBLE_CORAL, Blocks.DEAD_FIRE_CORAL, Blocks.DEAD_HORN_CORAL, Blocks.DEAD_TUBE_CORAL_FAN, Blocks.DEAD_BRAIN_CORAL_FAN, Blocks.DEAD_BUBBLE_CORAL_FAN, Blocks.DEAD_FIRE_CORAL_FAN, Blocks.DEAD_HORN_CORAL_FAN);
            this.tag(ToolsTags.Blocks.ALL_CORALS).addTag(BlockTags.CORAL_BLOCKS).addTag(BlockTags.CORAL_PLANTS).addTag(BlockTags.WALL_CORALS).addTag(BlockTags.CORALS).addTag(ToolsTags.Blocks.DEAD_CORALS);
        }
    }

    public static class ItemTagProvider extends VanillaItemTagsProvider {

        public ItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, TagsProvider<Block> blockTags) {
            super(output, lookup, blockTags);
        }

        @Override
        protected void addTags(HolderLookup.Provider lookup) {
            throw new NotImplementedException();
        }

        @Override
        protected IntrinsicTagAppender<Item> tag(TagKey<Item> tag) {
            throw new NotImplementedException();
        }

        @Override
        protected void copy(TagKey<Block> blockTag, TagKey<Item> itemTag) {
            throw new NotImplementedException();
        }

        public void addCommonTags(Function<TagKey<Item>, IntrinsicTagAppender<Item>> tagger, Consumer<Tuple<TagKey<Block>, TagKey<Item>>> copier) {
            this.tag(LibCommonTags.Items.FLUID_CONTAINERS).add(ToolsItems.WOOD_BUCKET.get(), ToolsItems.STONE_BUCKET.get(), ToolsItems.GOLD_BUCKET.get(), ToolsItems.DIAMOND_BUCKET.get(), ToolsItems.NETHERITE_BUCKET.get());
            this.tag(LibCommonTags.Items.BUCKETS_MILK).add(ToolsItems.WOOD_MILK_BUCKET.get(), ToolsItems.STONE_MILK_BUCKET.get(), ToolsItems.GOLD_MILK_BUCKET.get(), Items.MILK_BUCKET, ToolsItems.DIAMOND_MILK_BUCKET.get(), ToolsItems.NETHERITE_MILK_BUCKET.get());
            this.tag(LibCommonTags.Items.SHEARS).add(ToolsItems.WOOD_SHEARS.get(), ToolsItems.STONE_SHEARS.get(), ToolsItems.GOLD_SHEARS.get(), ToolsItems.DIAMOND_SHEARS.get(), ToolsItems.NETHERITE_SHEARS.get());

            ToolsItems.MATERIAL_GROUPS.forEach((s, group) -> {
                // Add to top level tags
                this.tag(LibCommonTags.Items.TOOLS_SWORDS).add(group.SWORD.get());
                this.tag(LibCommonTags.Items.TOOLS_PICKAXES).add(group.PICKAXE.get());
                this.tag(LibCommonTags.Items.TOOLS_SHOVELS).add(group.SHOVEL.get());
                this.tag(LibCommonTags.Items.TOOLS_AXES).add(group.AXE.get());
                this.tag(LibCommonTags.Items.TOOLS_HOES).add(group.HOE.get());
                this.tag(LibCommonTags.Items.ARMORS_HELMETS).add(group.HELMET.get());
                this.tag(LibCommonTags.Items.ARMORS_CHESTPLATES).add(group.CHESTPLATE.get());
                this.tag(LibCommonTags.Items.ARMORS_LEGGINGS).add(group.LEGGINGS.get());
                this.tag(LibCommonTags.Items.ARMORS_BOOTS).add(group.BOOTS.get());
                this.tag(LibCommonTags.Items.FLUID_CONTAINERS).add(group.BUCKET.get());
                this.tag(LibCommonTags.Items.BUCKETS_MILK).add(group.MILK_BUCKET.get());
                this.tag(LibCommonTags.Items.SHEARS).add(group.SHEARS.get());
            });
            this.tag(ToolsTags.Items.ULTIMATE_FRAGMENTS).add(ToolsItems.U_FRAGMENT.get(), ToolsItems.L_FRAGMENT.get(), ToolsItems.T_FRAGMENT.get(), ToolsItems.I_FRAGMENT.get(), ToolsItems.M_FRAGMENT.get(), ToolsItems.A_FRAGMENT.get(), ToolsItems.MISSING_FRAGMENT.get(), ToolsItems.E_FRAGMENT.get());

            this.tag(ItemTags.PIGLIN_LOVED).add(ToolsItems.GOLD_HAMMER.get(), ToolsItems.GOLDEN_MULTITOOL.get(), ToolsItems.GOLD_SPEAR.get(), ToolsItems.BUILDING_WAND.get(), ToolsItems.REINFORCED_BUILDING_WAND.get(), ToolsItems.GOLD_BUCKET.get(), ToolsItems.GOLD_MILK_BUCKET.get(), ToolsItems.GOLD_SHEARS.get());

        }
    }
}
