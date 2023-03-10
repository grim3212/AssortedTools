package com.grim3212.assorted.tools.api.item;

import com.grim3212.assorted.lib.mixin.item.ArmorMaterialsMixin;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.tools.api.ToolsTags;
import com.grim3212.assorted.tools.config.ArmorMaterialConfig;
import com.grim3212.assorted.tools.config.ToolsConfig;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum ToolsArmorMaterials implements ArmorMaterial {
    CHICKEN_SUIT(() -> ToolsConfig.Common.chickenSuitArmorMaterial, () -> SoundEvents.WOOL_PLACE, () -> LibCommonTags.Items.FEATHERS),
    TIN(() -> ToolsConfig.Common.moddedArmors.get("tin"), () -> SoundEvents.ARMOR_EQUIP_IRON, () -> ToolsTags.Items.INGOTS_TIN),
    COPPER(() -> ToolsConfig.Common.moddedArmors.get("copper"), () -> SoundEvents.ARMOR_EQUIP_IRON, () -> LibCommonTags.Items.INGOTS_COPPER),
    SILVER(() -> ToolsConfig.Common.moddedArmors.get("silver"), () -> SoundEvents.ARMOR_EQUIP_DIAMOND, () -> ToolsTags.Items.INGOTS_SILVER),
    ALUMINUM(() -> ToolsConfig.Common.moddedArmors.get("aluminum"), () -> SoundEvents.ARMOR_EQUIP_IRON, () -> ToolsTags.Items.INGOTS_ALUMINUM),
    NICKEL(() -> ToolsConfig.Common.moddedArmors.get("nickel"), () -> SoundEvents.ARMOR_EQUIP_IRON, () -> ToolsTags.Items.INGOTS_NICKEL),
    PLATINUM(() -> ToolsConfig.Common.moddedArmors.get("platinum"), () -> SoundEvents.ARMOR_EQUIP_DIAMOND, () -> ToolsTags.Items.INGOTS_PLATINUM),
    LEAD(() -> ToolsConfig.Common.moddedArmors.get("lead"), () -> SoundEvents.ARMOR_EQUIP_IRON, () -> ToolsTags.Items.INGOTS_LEAD),
    BRONZE(() -> ToolsConfig.Common.moddedArmors.get("bronze"), () -> SoundEvents.ARMOR_EQUIP_IRON, () -> ToolsTags.Items.INGOTS_BRONZE),
    ELECTRUM(() -> ToolsConfig.Common.moddedArmors.get("electrum"), () -> SoundEvents.ARMOR_EQUIP_IRON, () -> ToolsTags.Items.INGOTS_ELECTRUM),
    INVAR(() -> ToolsConfig.Common.moddedArmors.get("invar"), () -> SoundEvents.ARMOR_EQUIP_IRON, () -> ToolsTags.Items.INGOTS_INVAR),
    STEEL(() -> ToolsConfig.Common.moddedArmors.get("steel"), () -> SoundEvents.ARMOR_EQUIP_IRON, () -> ToolsTags.Items.INGOTS_STEEL),
    RUBY(() -> ToolsConfig.Common.moddedArmors.get("ruby"), () -> SoundEvents.ARMOR_EQUIP_DIAMOND, () -> ToolsTags.Items.GEMS_RUBY),
    AMETHYST(() -> ToolsConfig.Common.moddedArmors.get("amethyst"), () -> SoundEvents.ARMOR_EQUIP_DIAMOND, () -> LibCommonTags.Items.GEMS_AMETHYST),
    SAPPHIRE(() -> ToolsConfig.Common.moddedArmors.get("sapphire"), () -> SoundEvents.ARMOR_EQUIP_DIAMOND, () -> ToolsTags.Items.GEMS_SAPPHIRE),
    TOPAZ(() -> ToolsConfig.Common.moddedArmors.get("topaz"), () -> SoundEvents.ARMOR_EQUIP_DIAMOND, () -> ToolsTags.Items.GEMS_TOPAZ),
    EMERALD(() -> ToolsConfig.Common.moddedArmors.get("emerald"), () -> SoundEvents.ARMOR_EQUIP_DIAMOND, () -> LibCommonTags.Items.GEMS_EMERALD),
    PERIDOT(() -> ToolsConfig.Common.moddedArmors.get("peridot"), () -> SoundEvents.ARMOR_EQUIP_DIAMOND, () -> ToolsTags.Items.GEMS_PERIDOT);

    private final Supplier<ArmorMaterialConfig> material;
    private final Supplier<SoundEvent> equipSound;
    private final Supplier<TagKey<Item>> repairMaterial;

    ToolsArmorMaterials(Supplier<ArmorMaterialConfig> material, Supplier<SoundEvent> equipSound, Supplier<TagKey<Item>> repairTagIn) {
        this.material = material;
        this.equipSound = equipSound;
        this.repairMaterial = repairTagIn;
    }

    private ArmorMaterialConfig getMaterial() {
        return this.material.get();
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlot slot) {
        return this.getMaterial().getDurability() * ArmorMaterialsMixin.assortedlib_getHealthperSlot()[slot.getIndex()];
    }

    @Override
    public int getDefenseForSlot(EquipmentSlot slot) {
        return this.getMaterial().getReductionAmounts()[slot.getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return this.getMaterial().getEnchantability();
    }

    @Override
    public SoundEvent getEquipSound() {
        return equipSound.get();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(repairMaterial.get());
    }

    public TagKey<Item> getRepairMaterial() {
        return repairMaterial.get();
    }

    @Override
    public String getName() {
        return this.getMaterial().getName();
    }

    @Override
    public float getToughness() {
        return this.getMaterial().getToughness();
    }

    @Override
    public float getKnockbackResistance() {
        return this.getMaterial().getKnockbackResistance();
    }

    public ArmorMaterial defaultMaterial() {
        return new ArmorMaterial() {
            @Override
            public SoundEvent getEquipSound() {
                return equipSound.get();
            }

            @Override
            public Ingredient getRepairIngredient() {
                return Ingredient.of(repairMaterial.get());
            }

            @Override
            public String getName() {
                return getMaterial().getName();
            }

            @Override
            public int getDurabilityForSlot(EquipmentSlot p_40410_) {
                return 0;
            }

            @Override
            public int getDefenseForSlot(EquipmentSlot p_40411_) {
                return 0;
            }

            @Override
            public int getEnchantmentValue() {
                return 0;
            }

            @Override
            public float getToughness() {
                return 0;
            }

            @Override
            public float getKnockbackResistance() {
                return 0;
            }
        };
    }

}
