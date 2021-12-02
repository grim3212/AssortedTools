package com.grim3212.assorted.tools.common.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import com.grim3212.assorted.tools.common.item.BetterBucketItem;
import com.grim3212.assorted.tools.common.item.BetterMilkBucketItem;
import com.grim3212.assorted.tools.common.item.ToolsItems;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidAttributes;

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

	@SubscribeEvent
	public void interact(EntityInteract event) {
		Player player = event.getPlayer();
		ItemStack stack = player.getItemInHand(event.getHand());

		if (!stack.isEmpty() && (stack.getItem() instanceof BetterBucketItem || stack.getItem() instanceof BetterMilkBucketItem)) {
			this.tryMilk(stack, event);
		}
	}

	private void tryMilk(ItemStack stack, EntityInteract event) {
		Entity entity = event.getEntity();
		Player player = event.getPlayer();
		

		if (entity instanceof LivingEntity && !entity.level.isClientSide()) {
			if (!player.isCreative() && !((LivingEntity) entity).isBaby()) {
				if (stack.getItem() instanceof BetterMilkBucketItem) {
					BetterMilkBucketItem bucket = (BetterMilkBucketItem) stack.getItem();
					int milkingLevel = bucket.getParent().bucketType.getMilkingLevel();

					if (bucket != null) {
						for (int i = 0; i <= milkingLevel; i++) {
							for (int j = 0; j < levels.get(i).size(); j++) {
								if (levels.get(i).contains(entity.getClass())) {
									if (BetterBucketItem.getAmount(stack) < bucket.getParent().bucketType.getMaxBuckets()) {
										player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);

										int amount = BetterBucketItem.getAmount(stack);
										BetterBucketItem.setFluid(stack, "milk");
										BetterBucketItem.setAmount(stack, amount + FluidAttributes.BUCKET_VOLUME);

										bucket.pauseForMilk();
									}
								}
							}
						}
					}
				} else if (stack.getItem() instanceof BetterBucketItem) {
					BetterBucketItem bucket = (BetterBucketItem) stack.getItem();
					int milkingLevel = bucket.bucketType.getMilkingLevel();
					
					

					if (bucket != null) {
						for (int i = 0; i <= milkingLevel; i++) {
							for (int j = 0; j < levels.get(i).size(); j++) {
								if (levels.get(i).contains(entity.getClass())) {
									System.out.println("Emtpty bucket");
									if (BetterBucketItem.isEmptyOrContains(stack, "milk")) {
										
										
										if (BetterBucketItem.getAmount(stack) < bucket.bucketType.getMaxBuckets()) {
											player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);

											ItemStack milkBucket = ItemStack.EMPTY;
											switch (bucket.bucketType) {
												case DIAMOND:
													milkBucket = new ItemStack(ToolsItems.DIAMOND_MILK_BUCKET.get());
													break;
												case GOLD:
//												milkBucket = new ItemStack(ToolsItems.golden_milk_bucket);
													break;
												case OBSIDIAN:
//												milkBucket = new ItemStack(ToolsItems.obsidian_milk_bucket);
													break;
												case STONE:
//												milkBucket = new ItemStack(ToolsItems.stone_milk_bucket);
													break;
												case WOOD:
//												milkBucket = new ItemStack(ToolsItems.wooden_milk_bucket);
													break;
												case NETHERITE:
													break;
											}

											BetterBucketItem.setFluid(milkBucket, "milk");
											BetterBucketItem.setAmount(milkBucket, FluidAttributes.BUCKET_VOLUME);

											if (event.getHand() == InteractionHand.MAIN_HAND)
												player.getInventory().setItem(player.getInventory().selected, milkBucket);
											else
												player.getInventory().setItem(40, milkBucket);

											bucket.pauseForMilk();
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
