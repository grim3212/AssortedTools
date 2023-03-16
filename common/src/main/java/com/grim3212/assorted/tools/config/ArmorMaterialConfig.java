package com.grim3212.assorted.tools.config;

import com.grim3212.assorted.lib.config.IConfigurationBuilder;
import com.grim3212.assorted.tools.api.item.ToolsArmorMaterials;

import java.util.Arrays;
import java.util.function.Supplier;

public class ArmorMaterialConfig {
    private final String name;

    public final Supplier<Integer> durability;
    public final Supplier<Integer> enchantability;
    public final Supplier<Double> toughness;
    public final Supplier<Double> knockbackResistance;
    public final Supplier<Integer> bootsReductionAmount;
    public final Supplier<Integer> leggingsReductionAmount;
    public final Supplier<Integer> chestPlateReductionAmount;
    public final Supplier<Integer> helmetReductionAmount;
    private final Supplier<ToolsArmorMaterials> materialRef;

    public ArmorMaterialConfig(IConfigurationBuilder builder, String name, String path, int durability, int enchantability, float toughness, float knockbackResistance, int[] reductionAmounts, Supplier<ToolsArmorMaterials> materialRef) {
        this.name = name;
        this.materialRef = materialRef;

        this.durability = builder.defineInteger(path + "." + name + ".durability", durability, 1, 100000, "The durability multiplier for this armor material");
        this.enchantability = builder.defineInteger(path + "." + name + ".enchantability", enchantability, 0, 100000, "The enchantability for this armor material");
        this.toughness = builder.defineDouble(path + "." + name + ".toughness", toughness, 0F, 100000F, "The toughness for this armor material");
        this.knockbackResistance = builder.defineDouble(path + "." + name + ".knockbackResistance", knockbackResistance, 0F, 100000F, "The knockback resistance for this armor material");

        this.bootsReductionAmount = builder.defineInteger(path + "." + name + ".bootsReductionAmount", reductionAmounts[0], 0, 100000, "The reduction amount for the boots of this armor material");
        this.leggingsReductionAmount = builder.defineInteger(path + "." + name + ".leggingsReductionAmount", reductionAmounts[1], 0, 100000, "The reduction amount for the leggings of this armor material");
        this.chestPlateReductionAmount = builder.defineInteger(path + "." + name + ".chestPlateReductionAmount", reductionAmounts[2], 0, 100000, "The reduction amount for the chestplate of this armor material");
        this.helmetReductionAmount = builder.defineInteger(path + "." + name + ".helmetReductionAmount", reductionAmounts[3], 0, 100000, "The reduction amount for the helmet of this armor material");
    }

    public String getName() {
        return name;
    }

    public int getDurability() {
        return durability.get();
    }

    public int getBootsReductionAmount() {
        return bootsReductionAmount.get();
    }

    public int getLeggingsReductionAmount() {
        return leggingsReductionAmount.get();
    }

    public int getChestPlateReductionAmount() {
        return chestPlateReductionAmount.get();
    }

    public int getHelmetReductionAmount() {
        return helmetReductionAmount.get();
    }

    public int[] getReductionAmounts() {
        return new int[]{getBootsReductionAmount(), getLeggingsReductionAmount(), getChestPlateReductionAmount(), getHelmetReductionAmount()};
    }

    public int getEnchantability() {
        return enchantability.get();
    }

    public float getToughness() {
        return toughness.get().floatValue();
    }

    public float getKnockbackResistance() {
        return knockbackResistance.get().floatValue();
    }

    public ToolsArmorMaterials getMaterial() {
        return this.materialRef.get();
    }

    @Override
    public String toString() {
        return "[Name:" + getName() + ", Durability:" + getDurability() + ", ReductionAmounts:" + Arrays.toString(getReductionAmounts()) + ", Enchantability:" + getEnchantability() + ", Toughness:" + getToughness() + ", KnockbackResistance:" + getKnockbackResistance() + "]";
    }
}
