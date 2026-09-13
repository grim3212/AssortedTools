package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.lib.annotations.LoaderImplement;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.tools.api.item.ITiered;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BetterMilkBucketItem extends Item implements ITiered {

    private final Supplier<BetterBucketItem> parent;

    /**
     * @param tier the parent's tier, for the capacity in its tooltip; passed in because the parent
     *             is not read while items are still being registered
     */
    public BetterMilkBucketItem(Supplier<BetterBucketItem> parent, ItemTierConfig tier, Properties props) {
        // The drink animation, its duration, the swallowing sounds and clearing the drinker's
        // effects are all the consumable component's job now; there is nothing left to override.
        super(props.stacksTo(1).component(DataComponents.CONSUMABLE, Consumables.MILK_BUCKET).component(ToolsDataComponents.BUCKET_CONTENTS.get(), new BucketContents(tier.getMaxBuckets())));
        this.parent = parent;
    }

    public BetterBucketItem getParent() {
        return parent.get();
    }

    @Override
    public ItemTierConfig getTierHolder() {
        return this.getParent().getTierHolder();
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        // Vanilla finishes a drink by consuming the stack; a bucket loses one bucket of milk and
        // stays. Handing super a copy keeps everything else the component drives - the swallow, the
        // effect clear, the stat and the advancement.
        super.finishUsingItem(stack.copy(), worldIn, entityLiving);

        if (entityLiving instanceof Player player && !player.getAbilities().instabuild) {
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
