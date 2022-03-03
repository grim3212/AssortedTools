package com.grim3212.assorted.tools.common.util;

import java.util.List;
import java.util.Optional;

import com.grim3212.assorted.tools.AssortedTools;

import net.minecraft.core.HolderSet.Named;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ConfigurableBlockStates {

	private final NonNullList<BlockState> loadedStates;

	private ConfigurableBlockStates(NonNullList<BlockState> loadedStates) {
		this.loadedStates = loadedStates;
	}

	public NonNullList<BlockState> getLoadedStates() {
		return loadedStates;
	}

	public static class Builder {

		private final NonNullList<ResourceLocation> blocks;
		private final NonNullList<ResourceLocation> tags;

		public Builder() {
			this.blocks = NonNullList.create();
			this.tags = NonNullList.create();
		}

		public void addBlock(ResourceLocation b) {
			this.blocks.add(b);
		}

		public void addTag(ResourceLocation t) {
			this.tags.add(t);
		}

		public Builder processString(List<String> input) {
			for (String in : input) {
				AssortedTools.LOGGER.debug("Processing input : " + in);
				if (in != null && !in.isEmpty()) {
					if (in.startsWith("tag|")) {
						String s = in.substring(4);
						AssortedTools.LOGGER.debug("Add tag [" + s + "]");
						this.addTag(new ResourceLocation(s));
					} else if (in.startsWith("block|")) {
						String s = in.substring(6);
						AssortedTools.LOGGER.debug("Add block [" + s + "]");
						this.addBlock(new ResourceLocation(s));
					} else {
						AssortedTools.LOGGER.warn("Can't process input as a configurable block [" + in + "]");
					}
				}
			}
			return this;
		}

		public ConfigurableBlockStates build() {
			NonNullList<BlockState> states = NonNullList.create();

			for (ResourceLocation b : this.blocks) {
				Block block = Registry.BLOCK.get(b);

				if (block != Blocks.AIR) {
					states.add(block.defaultBlockState());
				} else {
					AssortedTools.LOGGER.warn(b.toString() + " is not a valid block.");
				}
			}

			for (ResourceLocation t : this.tags) {
				Optional<Named<Block>> foundTag = Registry.BLOCK.getTag(BlockTags.create(t));
				if (foundTag != null && foundTag.isPresent()) {
					foundTag.stream().flatMap((o) -> o.stream().map((bls) -> bls.value().defaultBlockState())).forEach(state -> states.add(state));
				} else {
					AssortedTools.LOGGER.warn(t.toString() + " is not a valid block tag.");
				}
			}

			return new ConfigurableBlockStates(states);
		}
	}

}
