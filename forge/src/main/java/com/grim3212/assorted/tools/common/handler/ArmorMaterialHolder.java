package com.grim3212.assorted.tools.common.handler;

import com.grim3212.assorted.decor.api.item.ToolsArmorMaterials;
import net.minecraft.util.LazyLoadedValue;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ArmorMaterialHolder {

    private final String name;

    private final ForgeConfigSpec.IntValue durability;
    private final ForgeConfigSpec.IntValue enchantability;
    private final ForgeConfigSpec.DoubleValue toughness;
    private final ForgeConfigSpec.DoubleValue knockbackResistance;
    private final ForgeConfigSpec.ConfigValue<List<Integer>> reductionAmounts;
    private final LazyLoadedValue<ToolsArmorMaterials> materialRef;

    public ArmorMaterialHolder(ForgeConfigSpec.Builder builder, String name, int durability, int enchantability, double toughness, double knockbackResistance, int[] reductionAmounts, Supplier<ToolsArmorMaterials> materialRef) {
        this.name = name;
        this.materialRef = new LazyLoadedValue<>(materialRef);

        builder.push(name);
        this.durability = builder.comment("The durability multiplier for this armor material").defineInRange("durability", durability, 1, 100000);
        this.enchantability = builder.comment("The enchantability for this armor material").defineInRange("enchantability", enchantability, 0, 100000);
        this.toughness = builder.comment("The toughness for this armor material").defineInRange("toughness", toughness, 0, 100000);
        this.knockbackResistance = builder.comment("The knockback resistance for this armor material").defineInRange("knockbackResistance", knockbackResistance, 0, 100000);
        this.reductionAmounts = builder.comment("The reduction amounts for each piece of armor for this armor material").define("reductionAmounts", Arrays.stream(reductionAmounts).boxed().collect(Collectors.toList()));
        builder.pop();
    }

    public String getName() {
        return name;
    }

    public int getDurability() {
        return durability.get();
    }

    public int[] getReductionAmounts() {
        return reductionAmounts.get().stream().mapToInt(i -> i).toArray();
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
