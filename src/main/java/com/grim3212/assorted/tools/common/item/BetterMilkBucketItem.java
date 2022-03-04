package com.grim3212.assorted.tools.common.item;

import java.util.List;
import java.util.function.Supplier;

import com.grim3212.assorted.tools.common.item.BetterBucketItem.BucketFluidHandler;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidAttributes;

public class BetterMilkBucketItem extends Item {

	private final Supplier<BetterBucketItem> parent;
	private boolean milkPause = false;

	public BetterMilkBucketItem(Supplier<BetterBucketItem> parent, Properties props) {
		super(props.stacksTo(1));
		this.parent = parent;
	}

	public void pauseForMilk() {
		this.milkPause = true;
	}

	public BetterBucketItem getParent() {
		return parent.get();
	}

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if (BetterBucketItem.getAmount(stack) <= 0) {
			tooltip.add(new TranslatableComponent("tooltip.buckets.empty"));
		} else {
			tooltip.add(new TranslatableComponent("tooltip.buckets.contains", BetterBucketItem.getAmount(stack), this.getParent().getMaximumMillibuckets()));
		}
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> stacks) {
		if (this.allowdedIn(tab)) {
			ItemStack stack = new ItemStack(this);
			BetterBucketItem.setFluid(stack, "milk");
			BetterBucketItem.setAmount(stack, this.parent.get().getMaximumMillibuckets());

			stacks.add(stack);
		}
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
		if (!worldIn.isClientSide)
			entityLiving.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
		if (entityLiving instanceof ServerPlayer) {
			ServerPlayer serverplayer = (ServerPlayer) entityLiving;
			CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, stack);
			serverplayer.awardStat(Stats.ITEM_USED.get(this));
		}

		if (entityLiving instanceof Player && !((Player) entityLiving).getAbilities().instabuild) {
			int amount = BetterBucketItem.getAmount(stack);
			BetterBucketItem.setAmount(stack, amount - FluidAttributes.BUCKET_VOLUME);
		}

		return this.getParent().tryBreakBucket(stack);
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemStack) {
		int amount = BetterBucketItem.getAmount(itemStack);
		BetterBucketItem.setAmount(itemStack, amount - FluidAttributes.BUCKET_VOLUME);

		return this.getParent().tryBreakBucket(itemStack);
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return BetterBucketItem.getAmount(stack) >= FluidAttributes.BUCKET_VOLUME;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 32;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.DRINK;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if (milkPause) {
			milkPause = false;
			return InteractionResultHolder.success(player.getItemInHand(hand));
		}

		player.startUsingItem(hand);
		return InteractionResultHolder.success(player.getItemInHand(hand));
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		// Don't show if the bucket is empty
		if (BetterBucketItem.getAmount(stack) <= 0)
			return false;
		return true;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		// Get remainder calculations from stored and maxAmount
		int reversedAmount = this.getParent().getMaximumMillibuckets() - BetterBucketItem.getAmount(stack);
		return Math.round(13.0F - (float) reversedAmount * 13.0F / (float) this.getParent().getMaximumMillibuckets());
	}

	@Override
	public int getBarColor(ItemStack stack) {
		float f = Math.max(0.0F, (float) BetterBucketItem.getAmount(stack) / (float) this.getParent().getMaximumMillibuckets());
		return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
		BetterBucketItem parent = this.getParent();
		return new BucketFluidHandler(stack, parent.getBreakStack(), parent.empty, parent.getMaximumMillibuckets());
	}
}
