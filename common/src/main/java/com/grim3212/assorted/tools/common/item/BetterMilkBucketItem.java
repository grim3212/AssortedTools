package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.lib.annotations.LoaderImplement;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.tools.api.item.ITiered;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
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

    /**
     * {@code Item.appendHoverText} is marked deprecated in 26.x - tooltips are meant to come from
     * data components implementing {@code TooltipProvider} - but it is still the only per item
     * hook, and vanilla's own items still override it.
     */
    @SuppressWarnings("deprecation")
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flagIn) {
        if (BetterBucketItem.getAmount(stack) <= 0) {
            tooltip.accept(Component.translatable("tooltip.buckets.empty"));
        } else {
            tooltip.accept(Component.translatable("tooltip.buckets.contains", BetterBucketItem.getAmount(stack), this.getParent().getMaximumMillibuckets()));
        }
    }

    @Override
    public ItemTierConfig getTierHolder() {
        return this.getParent().getTierHolder();
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        if (!worldIn.isClientSide())
            entityLiving.removeAllEffects();
        if (entityLiving instanceof ServerPlayer serverplayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, stack);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (entityLiving instanceof Player && !((Player) entityLiving).getAbilities().instabuild) {
            int amount = BetterBucketItem.getAmount(stack);
            BetterBucketItem.setAmount(stack, amount - getBucketAmount());
        }

        return this.getParent().tryBreakBucket(stack);
    }

    /**
     * See {@code BetterBucketItem#craftingRemainder}: both loaders now ask for a nullable
     * {@code ItemStackTemplate} and neither has a {@code hasCraftingRemainingItem} any more.
     */
    private @Nullable ItemStackTemplate craftingRemainder(ItemStack stack) {
        if (BetterBucketItem.getAmount(stack) < getBucketAmount()) {
            return null;
        }

        ItemStack remainder = stack.copy();
        BetterBucketItem.setAmount(remainder, BetterBucketItem.getAmount(remainder) - getBucketAmount());

        ItemStack result = this.getParent().tryBreakBucket(remainder);
        return result.isEmpty() ? null : ItemStackTemplate.fromStack(result);
    }

    @LoaderImplement(loader = LoaderImplement.Loader.FORGE, value = "IItemExtension")
    public @Nullable ItemStackTemplate getCraftingRemainder(ItemInstance instance) {
        return instance instanceof ItemStack stack ? this.craftingRemainder(stack) : this.getCraftingRemainder();
    }

    @LoaderImplement(loader = LoaderImplement.Loader.FABRIC, value = "FabricItem")
    public @Nullable ItemStackTemplate getCraftingRemainder(ItemStack stack) {
        return this.craftingRemainder(stack);
    }

    public int getBucketAmount() {
        //Currently set to an int might change to a long
        return (int) Services.FLUIDS.getBucketAmount();
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 32;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.DRINK;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
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
