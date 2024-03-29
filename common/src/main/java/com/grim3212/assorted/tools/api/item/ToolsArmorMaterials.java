package com.grim3212.assorted.tools.api.item;

import com.grim3212.assorted.lib.mixin.item.ArmorMaterialsMixin;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.tools.ToolsCommonMod;
import com.grim3212.assorted.tools.api.ToolsTags;
import com.grim3212.assorted.tools.config.ArmorMaterialConfig;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum ToolsArmorMaterials implements ArmorMaterial {
    CHICKEN_SUIT(() -> ToolsCommonMod.COMMON_CONFIG.chickenSuitArmorMaterial, () -> SoundEvents.WOOL_PLACE, () -> LibCommonTags.Items.FEATHERS),
    TIN(() -> ToolsCommonMod.COMMON_CONFIG.moddedArmors.get("tin"), () -> SoundEvents.ARMOR_EQUIP_IRON, () -> ToolsTags.Items.INGOTS_TIN),
    COPPER(() -> ToolsCommonMod.COMMON_CONFIG.moddedArmors.get("copper"), () -> SoundEvents.ARMOR_EQUIP_IRON, () -> LibCommonTags.Items.INGOTS_COPPER),
    SILVER(() -> ToolsCommonMod.COMMON_CONFIG.moddedArmors.get("silver"), () -> SoundEvents.ARMOR_EQUIP_DIAMOND, () -> ToolsTags.Items.INGOTS_SILVER),
    ALUMINUM(() -> ToolsCommonMod.COMMON_CONFIG.moddedArmors.get("aluminum"), () -> SoundEvents.ARMOR_EQUIP_IRON, () -> ToolsTags.Items.INGOTS_ALUMINUM),
    NICKEL(() -> ToolsCommonMod.COMMON_CONFIG.moddedArmors.get("nickel"), () -> SoundEvents.ARMOR_EQUIP_IRON, () -> ToolsTags.Items.INGOTS_NICKEL),
    PLATINUM(() -> ToolsCommonMod.COMMON_CONFIG.moddedArmors.get("platinum"), () -> SoundEvents.ARMOR_EQUIP_DIAMOND, () -> ToolsTags.Items.INGOTS_PLATINUM),
    LEAD(() -> ToolsCommonMod.COMMON_CONFIG.moddedArmors.get("lead"), () -> SoundEvents.ARMOR_EQUIP_IRON, () -> ToolsTags.Items.INGOTS_LEAD),
    BRONZE(() -> ToolsCommonMod.COMMON_CONFIG.moddedArmors.get("bronze"), () -> SoundEvents.ARMOR_EQUIP_IRON, () -> ToolsTags.Items.INGOTS_BRONZE),
    ELECTRUM(() -> ToolsCommonMod.COMMON_CONFIG.moddedArmors.get("electrum"), () -> SoundEvents.ARMOR_EQUIP_IRON, () -> ToolsTags.Items.INGOTS_ELECTRUM),
    INVAR(() -> ToolsCommonMod.COMMON_CONFIG.moddedArmors.get("invar"), () -> SoundEvents.ARMOR_EQUIP_IRON, () -> ToolsTags.Items.INGOTS_INVAR),
    STEEL(() -> ToolsCommonMod.COMMON_CONFIG.moddedArmors.get("steel"), () -> SoundEvents.ARMOR_EQUIP_IRON, () -> ToolsTags.Items.INGOTS_STEEL),
    RUBY(() -> ToolsCommonMod.COMMON_CONFIG.moddedArmors.get("ruby"), () -> SoundEvents.ARMOR_EQUIP_DIAMOND, () -> ToolsTags.Items.GEMS_RUBY),
    AMETHYST(() -> ToolsCommonMod.COMMON_CONFIG.moddedArmors.get("amethyst"), () -> SoundEvents.ARMOR_EQUIP_DIAMOND, () -> LibCommonTags.Items.GEMS_AMETHYST),
    SAPPHIRE(() -> ToolsCommonMod.COMMON_CONFIG.moddedArmors.get("sapphire"), () -> SoundEvents.ARMOR_EQUIP_DIAMOND, () -> ToolsTags.Items.GEMS_SAPPHIRE),
    TOPAZ(() -> ToolsCommonMod.COMMON_CONFIG.moddedArmors.get("topaz"), () -> SoundEvents.ARMOR_EQUIP_DIAMOND, () -> ToolsTags.Items.GEMS_TOPAZ),
    EMERALD(() -> ToolsCommonMod.COMMON_CONFIG.moddedArmors.get("emerald"), () -> SoundEvents.ARMOR_EQUIP_DIAMOND, () -> LibCommonTags.Items.GEMS_EMERALD),
    PERIDOT(() -> ToolsCommonMod.COMMON_CONFIG.moddedArmors.get("peridot"), () -> SoundEvents.ARMOR_EQUIP_DIAMOND, () -> ToolsTags.Items.GEMS_PERIDOT);

    private final Supplier<ArmorMaterialConfig> material;
    private final Supplier<SoundEvent> equipSound;
    private final Supplier<TagKey<Item>> repairMaterial;

    ToolsArmorMaterials(Supplier<ArmorMaterialConfig> material, Supplier<SoundEvent> equipSound, Supplier<TagKey<Item>> repairTagIn) {
        this.material = material;
        this.equipSound = equipSound;
        this.repairMaterial = repairTagIn;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return this.material.get().getDurability() * ArmorMaterialsMixin.assortedlib_getHealthperSlot().get(type);
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return this.material.get().getReductionAmounts().get(type);
    }

    @Override
    public int getEnchantmentValue() {
        return this.material.get().getEnchantability();
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
        return this.material.get().getName();
    }

    @Override
    public float getToughness() {
        return this.material.get().getToughness();
    }

    @Override
    public float getKnockbackResistance() {
        return this.material.get().getKnockbackResistance();
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
                return material.get().getName();
            }

            @Override
            public int getDurabilityForType(ArmorItem.Type type) {
                return 1;
            }

            @Override
            public int getDefenseForType(ArmorItem.Type type) {
                return 0;
            }

            @Override
            public int getEnchantmentValue() {
                return 1;
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
