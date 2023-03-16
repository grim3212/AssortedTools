package com.grim3212.assorted.tools.mixin.item;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(AxeItem.class)
public interface AxeItemMixin {

    @Accessor("STRIPPABLES")
    static Map<Block, Block> assortedtools_STRIPPABLES() {
        throw new AssertionError();
    }
}
