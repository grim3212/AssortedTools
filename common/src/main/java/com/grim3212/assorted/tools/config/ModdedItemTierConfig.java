package com.grim3212.assorted.tools.config;

import com.grim3212.assorted.lib.config.IConfigurationBuilder;
import com.grim3212.assorted.tools.api.item.BucketOptions;
import com.grim3212.assorted.tools.api.item.SpearStats;
import com.grim3212.assorted.tools.api.item.ToolsItemTier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

/**
 * An extra material's configuration: the tool numbers every tier has, plus the spear's lunge
 * values, which vanilla tunes per material and only the extra materials need (vanilla's own
 * materials have vanilla's own spears). Like the rest, they are baked into the item at
 * registration, so <b>changes need a restart</b>.
 */
public class ModdedItemTierConfig extends ItemTierConfig {
    private final ToolsItemTier moddedTier;

    // Spear config properties, in the order Item.Properties#spear takes them.
    public final Supplier<Double> spearSwingSeconds;
    public final Supplier<Double> spearDamageMultiplier;
    public final Supplier<Double> spearDelaySeconds;
    public final Supplier<Double> spearDismountSeconds;
    public final Supplier<Double> spearDismountSpeed;
    public final Supplier<Double> spearKnockbackSeconds;
    public final Supplier<Double> spearKnockbackSpeed;
    public final Supplier<Double> spearDamageSeconds;
    public final Supplier<Double> spearDamageSpeed;

    public ModdedItemTierConfig(IConfigurationBuilder builder, String name, String path, ToolsItemTier defaultTier) {
        super(builder, name, path, defaultTier);
        this.moddedTier = defaultTier;

        // Defaults are vanilla's values for the vanilla material of the same harvest level.
        SpearStats defaults = SpearStats.forHarvestLevel(defaultTier.getLevel());
        String spear = path + "." + name + ".spear.";
        this.spearSwingSeconds = builder.defineDouble(spear + "swingSeconds", defaults.swingSeconds(), 0F, 100F, "How long this material's spear takes to stab, in seconds. Vanilla: wood 0.65, stone 0.75, iron 0.95, diamond 1.05, netherite 1.15.");
        this.spearDamageMultiplier = builder.defineDouble(spear + "damageMultiplier", defaults.damageMultiplier(), 0F, 100F, "The multiplier on the damage a lunge with this material's spear does. Vanilla: wood 0.7, stone 0.82, iron 0.95, diamond 1.075, netherite 1.2.");
        this.spearDelaySeconds = builder.defineDouble(spear + "delaySeconds", defaults.delaySeconds(), 0F, 100F, "The delay before a lunge with this material's spear lands, in seconds. Vanilla: wood 0.75, stone 0.7, iron 0.6, diamond 0.5, netherite 0.4.");
        this.spearDismountSeconds = builder.defineDouble(spear + "dismountSeconds", defaults.dismountSeconds(), 0F, 100F, "How long a lunge has to be charged before it can dismount a rider, in seconds. Vanilla: wood 5.0, stone 4.5, iron 2.5, diamond 3.0, netherite 2.5.");
        this.spearDismountSpeed = builder.defineDouble(spear + "dismountSpeed", defaults.dismountSpeed(), 0F, 1000F, "How fast the attacker has to be moving for a lunge to dismount a rider. Vanilla: wood 14.0, stone 13.0, iron 11.0, diamond 10.0, netherite 9.0.");
        this.spearKnockbackSeconds = builder.defineDouble(spear + "knockbackSeconds", defaults.knockbackSeconds(), 0F, 100F, "How long a lunge has to be charged before it knocks back, in seconds. Vanilla: wood 10.0, stone 9.0, iron 6.75, diamond 6.5, netherite 5.5.");
        this.spearKnockbackSpeed = builder.defineDouble(spear + "knockbackSpeed", defaults.knockbackSpeed(), 0F, 1000F, "How fast the attacker has to be moving for a lunge to knock back. Vanilla: 5.1 for every material.");
        this.spearDamageSeconds = builder.defineDouble(spear + "damageSeconds", defaults.damageSeconds(), 0F, 100F, "How long a lunge has to be charged before it damages, in seconds. Vanilla: wood 15.0, stone 13.75, iron 11.25, diamond 10.0, netherite 8.75.");
        this.spearDamageSpeed = builder.defineDouble(spear + "damageSpeed", defaults.damageSpeed(), 0F, 1000F, "How fast the attacker has to be moving, relative to the target, for a lunge to damage. Vanilla: 4.6 for every material.");
    }

    public TagKey<Item> getMaterial() {
        return this.moddedTier.repairTag();
    }

    public BucketOptions getBucketOptions() {
        return this.moddedTier.getBucketOptions();
    }

    /** The configured lunge values, read once at registration like the rest of the material. */
    public SpearStats getSpearStats() {
        return new SpearStats(
                this.spearSwingSeconds.get(), this.spearDamageMultiplier.get(), this.spearDelaySeconds.get(),
                this.spearDismountSeconds.get(), this.spearDismountSpeed.get(),
                this.spearKnockbackSeconds.get(), this.spearKnockbackSpeed.get(),
                this.spearDamageSeconds.get(), this.spearDamageSpeed.get());
    }
}
