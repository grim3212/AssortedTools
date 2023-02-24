package com.grim3212.assorted.decor.api.item;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public abstract class ArmorMaterialHolder {

    private final String name;

    private final SuppliedProperty<Integer> durability;
    private final SuppliedProperty<Integer> enchantability;
    private final SuppliedProperty<Float> toughness;
    private final SuppliedProperty<Float> knockbackResistance;
    private final SuppliedProperty<List<Integer>> reductionAmounts;
    private final Supplier<ToolsArmorMaterials> materialRef;

    public ArmorMaterialHolder(String name, int durability, int enchantability, float toughness, float knockbackResistance, int[] reductionAmounts, Supplier<ToolsArmorMaterials> materialRef) {
        this.name = name;
        this.materialRef = materialRef;

        this.durability = new SuppliedProperty<>(durability);
        this.enchantability = new SuppliedProperty<>(enchantability);
        this.toughness = new SuppliedProperty<>(toughness);
        this.knockbackResistance = new SuppliedProperty<>(knockbackResistance);
        this.reductionAmounts = new SuppliedProperty<>(Arrays.stream(reductionAmounts).boxed().toList());

        this.buildOptions(durability, enchantability, toughness, knockbackResistance, reductionAmounts, materialRef);
    }

    public abstract void buildOptions(int durability, int enchantability, float toughness, float knockbackResistance, int[] reductionAmounts, Supplier<ToolsArmorMaterials> materialRef);

    public String getName() {
        return name;
    }

    public int getDurability() {
        return durability.getValue();
    }

    public int[] getReductionAmounts() {
        return reductionAmounts.getValue().stream().mapToInt(i -> i).toArray();
    }

    public int getEnchantability() {
        return enchantability.getValue();
    }

    public float getToughness() {
        return toughness.getValue();
    }

    public float getKnockbackResistance() {
        return knockbackResistance.getValue();
    }

    public ToolsArmorMaterials getMaterial() {
        return this.materialRef.get();
    }

    @Override
    public String toString() {
        return "[Name:" + getName() + ", Durability:" + getDurability() + ", ReductionAmounts:" + Arrays.toString(getReductionAmounts()) + ", Enchantability:" + getEnchantability() + ", Toughness:" + getToughness() + ", KnockbackResistance:" + getKnockbackResistance() + "]";
    }
}
