package com.grim3212.assorted.tools.config;

import com.grim3212.assorted.lib.config.ConfigGroup;
import com.grim3212.assorted.lib.config.ConfigOption;
import com.grim3212.assorted.tools.api.item.ToolsArmorMaterials;

import java.util.Arrays;
import java.util.function.Supplier;

public class ArmorMaterialConfig {
    private final String name;

    private final ConfigOption.ConfigOptionInteger durability;
    private final ConfigOption.ConfigOptionInteger enchantability;
    private final ConfigOption.ConfigOptionFloat toughness;
    private final ConfigOption.ConfigOptionFloat knockbackResistance;
    private final ConfigOption.ConfigOptionInteger helmetReductionAmount;
    private final ConfigOption.ConfigOptionInteger chestPlateReductionAmount;
    private final ConfigOption.ConfigOptionInteger leggingsReductionAmount;
    private final ConfigOption.ConfigOptionInteger bootsReductionAmount;
    private final Supplier<ToolsArmorMaterials> materialRef;

    public ArmorMaterialConfig(ConfigGroup group, String name, int durability, int enchantability, float toughness, float knockbackResistance, int[] reductionAmounts, Supplier<ToolsArmorMaterials> materialRef) {
        this.name = name;
        this.materialRef = materialRef;

        ConfigGroup armorMaterialGroup = new ConfigGroup(name);

        this.durability = new ConfigOption.ConfigOptionInteger("durability", durability, "The durability multiplier for this armor material", 1, 100000);
        this.enchantability = new ConfigOption.ConfigOptionInteger("enchantability", enchantability, "The enchantability for this armor material", 0, 100000);
        this.toughness = new ConfigOption.ConfigOptionFloat("toughness", toughness, "The toughness for this armor material", 0F, 100000F);
        this.knockbackResistance = new ConfigOption.ConfigOptionFloat("knockbackResistance", knockbackResistance, "The knockback resistance for this armor material", 0F, 100000F);

        this.bootsReductionAmount = new ConfigOption.ConfigOptionInteger("bootsReductionAmount", reductionAmounts[0], "The reduction amount for the boots of this armor material", 0, 100000);
        this.leggingsReductionAmount = new ConfigOption.ConfigOptionInteger("leggingsReductionAmount", reductionAmounts[1], "The reduction amount for the leggings of this armor material", 0, 100000);
        this.chestPlateReductionAmount = new ConfigOption.ConfigOptionInteger("chestPlateReductionAmount", reductionAmounts[2], "The reduction amount for the chestplate of this armor material", 0, 100000);
        this.helmetReductionAmount = new ConfigOption.ConfigOptionInteger("helmetReductionAmount", reductionAmounts[3], "The reduction amount for the helmet of this armor material", 0, 100000);

        armorMaterialGroup.addOptions(this.durability, this.enchantability, this.toughness, this.knockbackResistance, this.bootsReductionAmount, this.leggingsReductionAmount, this.chestPlateReductionAmount, this.helmetReductionAmount);
        group.addSubGroups(armorMaterialGroup);
    }

    public String getName() {
        return name;
    }

    public int getDurability() {
        return durability.getValue();
    }

    public int getBootsReductionAmount() {
        return bootsReductionAmount.getValue();
    }

    public int getLeggingsReductionAmount() {
        return leggingsReductionAmount.getValue();
    }

    public int getChestPlateReductionAmount() {
        return chestPlateReductionAmount.getValue();
    }

    public int getHelmetReductionAmount() {
        return helmetReductionAmount.getValue();
    }

    public int[] getReductionAmounts() {
        return new int[]{getBootsReductionAmount(), getLeggingsReductionAmount(), getChestPlateReductionAmount(), getHelmetReductionAmount()};
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
