package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.common.inventory.PortableWorkbenchMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class PortableWorkbenchItem extends Item {

    private static final Component CONTAINER_TITLE = Component.translatable("container.crafting");

    public PortableWorkbenchItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            player.openMenu(new SimpleMenuProvider((containerId, inventory, p) -> new PortableWorkbenchMenu(containerId, inventory, ContainerLevelAccess.create(level, p.blockPosition())), CONTAINER_TITLE));
            player.awardStat(Stats.ITEM_USED.get(this));
        }

        return InteractionResult.SUCCESS;
    }
}
