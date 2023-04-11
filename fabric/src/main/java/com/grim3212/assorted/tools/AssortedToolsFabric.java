package com.grim3212.assorted.tools;

import com.grim3212.assorted.lib.events.EntityInteractEvent;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.tools.common.item.FabricBetterBucketFluidHandler;
import com.grim3212.assorted.tools.common.item.MaterialShears;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;

public class AssortedToolsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ToolsCommonMod.init();

        FluidStorage.ITEM.registerForItems((i, c) -> new FabricBetterBucketFluidHandler(c), ToolsItems.buckets().toArray(new Item[0]));

        // Shears todo:
        // loot fix
        // Beehive, Pumpkin, TripWire
        // Decide if a mixin for interact is better than this event? Probably
        Services.EVENTS.registerEvent(EntityInteractEvent.class, (final EntityInteractEvent event) -> {
            Player player = event.getPlayer();
            ItemStack stack = player.getItemInHand(event.getHand());

            if (!stack.isEmpty() && stack.getItem() instanceof MaterialShears) {
                Entity entity = event.getTarget();
                if (entity instanceof Shearable shearable) {
                    if (shearable.readyForShearing()) {
                        shearable.shear(SoundSource.PLAYERS);
                        entity.gameEvent(GameEvent.SHEAR, player);

                        if (!entity.level.isClientSide) {
                            stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(event.getHand()));
                        }
                    }
                }
            }
        });
    }
}
