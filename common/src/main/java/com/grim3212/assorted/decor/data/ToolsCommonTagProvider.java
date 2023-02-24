package com.grim3212.assorted.decor.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.data.tags.VanillaBlockTagsProvider;
import net.minecraft.data.tags.VanillaItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
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

        }
    }
}
