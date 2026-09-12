package com.grim3212.assorted.tools.api.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

/**
 * The nine values vanilla tunes per spear material, in the order {@code Item.Properties#spear} takes
 * them: how long the stab swing lasts, the kinetic damage multiplier, the delay before a lunge
 * lands, then the charge time and speed a lunge needs to dismount, to knock back and to damage.
 * The constants are vanilla's own, read from {@code Items} in 26.2; an extra material's are read
 * from the configuration, defaulting to the vanilla material of the same harvest level. Held as
 * doubles, which is what the configuration stores, so a default prints as written.
 */
public record SpearStats(double swingSeconds, double damageMultiplier, double delaySeconds, double dismountSeconds, double dismountSpeed, double knockbackSeconds, double knockbackSpeed, double damageSeconds, double damageSpeed) {

    public static final SpearStats WOOD = new SpearStats(0.65, 0.7, 0.75, 5.0, 14.0, 10.0, 5.1, 15.0, 4.6);
    public static final SpearStats STONE = new SpearStats(0.75, 0.82, 0.7, 4.5, 13.0, 9.0, 5.1, 13.75, 4.6);
    public static final SpearStats IRON = new SpearStats(0.95, 0.95, 0.6, 2.5, 11.0, 6.75, 5.1, 11.25, 4.6);
    public static final SpearStats DIAMOND = new SpearStats(1.05, 1.075, 0.5, 3.0, 10.0, 6.5, 5.1, 10.0, 4.6);
    public static final SpearStats NETHERITE = new SpearStats(1.15, 1.2, 0.4, 2.5, 9.0, 5.5, 5.1, 8.75, 4.6);

    /** The vanilla material a harvest level corresponds to: wood, stone, iron, diamond, netherite. */
    public static SpearStats forHarvestLevel(int level) {
        return switch (level) {
            case 0 -> WOOD;
            case 1 -> STONE;
            case 2 -> IRON;
            case 3 -> DIAMOND;
            default -> NETHERITE;
        };
    }

    public Item.Properties apply(Item.Properties props, ToolMaterial material) {
        return props.spear(material, (float) swingSeconds, (float) damageMultiplier, (float) delaySeconds, (float) dismountSeconds, (float) dismountSpeed, (float) knockbackSeconds, (float) knockbackSpeed, (float) damageSeconds, (float) damageSpeed);
    }
}
