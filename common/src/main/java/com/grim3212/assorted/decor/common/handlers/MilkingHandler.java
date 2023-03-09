package com.grim3212.assorted.decor.common.handlers;

import com.google.common.collect.Lists;
import com.grim3212.assorted.decor.common.item.BetterBucketItem;
import com.grim3212.assorted.decor.common.item.BetterMilkBucketItem;
import com.grim3212.assorted.lib.events.EntityInteractEvent;
import com.grim3212.assorted.lib.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MilkingHandler {

    public static List<List<Class<? extends Entity>>> levels = Lists.newArrayList();

    static {
        addMilkable(0, Cow.class);
        addMilkable(1, Sheep.class);
        addMilkable(2, Pig.class);
    }

    /**
     * Adds a milkable entity to the specified level All levels above the specified
     * level will also be able to milk the same entity
     *
     * @param level    The milking level to add to
     * @param milkable The entity to add
     * @return True if adding was a success and it wasn't a duplicate
     */
    public static boolean addMilkable(int level, Class<? extends Entity> milkable) {
        if (levels.size() <= level)
            levels.add(level, new ArrayList<Class<? extends Entity>>());

        if (Collections.frequency(levels.get(level), milkable) > 1)
            return false;

        return levels.get(level).add(milkable);
    }

    public static void interact(EntityInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack stack = player.getItemInHand(event.getHand());

        if (!stack.isEmpty() && (stack.getItem() instanceof BetterBucketItem || stack.getItem() instanceof BetterMilkBucketItem)) {
            tryMilk(stack, event);
        }
    }

    private static void tryMilk(ItemStack stack, EntityInteractEvent event) {
        Entity entity = event.getTarget();
        Player player = event.getPlayer();

        if (entity instanceof LivingEntity && !entity.level.isClientSide()) {
            if (!player.isCreative() && !((LivingEntity) entity).isBaby()) {
                if (stack.getItem() instanceof BetterMilkBucketItem) {
                    BetterMilkBucketItem bucket = (BetterMilkBucketItem) stack.getItem();
                    int milkingLevel = bucket.getParent().tierHolder.getMilkingLevel();

                    if (bucket != null) {
                        for (int i = 0; i <= milkingLevel; i++) {
                            for (int j = 0; j < levels.get(i).size(); j++) {
                                if (levels.get(i).contains(entity.getClass())) {
                                    if (BetterBucketItem.getAmount(stack) < bucket.getParent().getMaximumMillibuckets()) {

                                        player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);

                                        int amount = BetterBucketItem.getAmount(stack);
                                        BetterBucketItem.setFluid(stack, "milk");
                                        BetterBucketItem.setAmount(stack, amount + (int) Services.FLUIDS.getBucketAmount());

                                        event.setCanceled(true);
                                        event.setResult(InteractionResult.SUCCESS);
                                    }
                                }
                            }
                        }
                    }
                } else if (stack.getItem() instanceof BetterBucketItem) {
                    BetterBucketItem bucket = (BetterBucketItem) stack.getItem();
                    ResourceLocation bucketName = Services.PLATFORM.getRegistry(Registries.ITEM).getRegistryName(bucket);
                    int milkingLevel = bucket.tierHolder.getMilkingLevel();

                    if (bucket != null) {
                        for (int i = 0; i <= milkingLevel; i++) {
                            for (int j = 0; j < levels.get(i).size(); j++) {
                                if (levels.get(i).contains(entity.getClass())) {
                                    if (BetterBucketItem.isEmptyOrContains(stack, "milk")) {

                                        if (BetterBucketItem.getAmount(stack) < bucket.getMaximumMillibuckets()) {
                                            player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);

                                            ItemStack milkBucket = new ItemStack(Services.PLATFORM.getRegistry(Registries.ITEM).getValue(new ResourceLocation(bucketName.toString().replace("_bucket", "_milk_bucket"))).orElseThrow());
                                            BetterBucketItem.setFluid(milkBucket, "milk");
                                            BetterBucketItem.setAmount(milkBucket, (int) Services.FLUIDS.getBucketAmount());

                                            if (event.getHand() == InteractionHand.MAIN_HAND)
                                                player.getInventory().setItem(player.getInventory().selected, milkBucket);
                                            else
                                                player.getInventory().setItem(40, milkBucket);

                                            event.setCanceled(true);
                                            event.setResult(InteractionResult.SUCCESS);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
