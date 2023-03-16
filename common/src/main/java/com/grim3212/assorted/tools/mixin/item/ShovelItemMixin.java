package com.grim3212.assorted.tools.mixin.item;

import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ShovelItem.class)
public interface ShovelItemMixin {

    @Accessor("FLATTENABLES")
    static Map<Block, BlockState> assortedtools_FLATTENABLES() {
        throw new AssertionError();
    }
}
