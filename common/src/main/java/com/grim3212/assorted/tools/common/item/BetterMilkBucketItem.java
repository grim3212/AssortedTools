package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.lib.annotations.LoaderImplement;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.tools.api.item.ITiered;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Supplier;

public class BetterMilkBucketItem extends Item implements ITiered {

    private final Supplier<BetterBucketItem> parent;

    public BetterMilkBucketItem(Supplier<BetterBucketItem> parent, Properties props) {
        super(props.stacksTo(1));
        this.parent = parent;
    }

    public BetterBucketItem getParent() {
        return parent.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (BetterBucketItem.getAmount(stack) <= 0) {
            tooltip.add(Component.translatable("tooltip.buckets.empty"));
        } else {
            tooltip.add(Component.translatable("tooltip.buckets.contains", BetterBucketItem.getAmount(stack), this.getParent().getMaximumMillibuckets()));
        }
    }

    @Override
    public ItemTierConfig getTierHolder() {
        return this.getParent().getTierHolder();
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        if (!worldIn.isClientSide)
            entityLiving.removeAllEffects();
        if (entityLiving instanceof ServerPlayer) {
            ServerPlayer serverplayer = (ServerPlayer) entityLiving;
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, stack);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (entityLiving instanceof Player && !((Player) entityLiving).getAbilities().instabuild) {
            int amount = BetterBucketItem.getAmount(stack);
            BetterBucketItem.setAmount(stack, amount - getBucketAmount());
        }

        return this.getParent().tryBreakBucket(stack);
    }

    @LoaderImplement(loader = LoaderImplement.Loader.FORGE, value = "IForgeItem")
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        int amount = BetterBucketItem.getAmount(itemStack);
        BetterBucketItem.setAmount(itemStack, amount - getBucketAmount());

        return this.getParent().tryBreakBucket(itemStack);
    }

    @LoaderImplement(loader = LoaderImplement.Loader.FORGE, value = "IForgeItem")
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return BetterBucketItem.getAmount(stack) >= getBucketAmount();
    }

    @LoaderImplement(loader = LoaderImplement.Loader.FABRIC, value = "FabricItem")
    public ItemStack getRecipeRemainder(ItemStack stack) {
        if (this.hasCraftingRemainingItem(stack)) {
            return this.getCraftingRemainingItem(stack);
        }

        return ItemStack.EMPTY;
    }

    public int getBucketAmount() {
        //Currently set to an int might change to a long
        return (int) Services.FLUIDS.getBucketAmount();
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
}
