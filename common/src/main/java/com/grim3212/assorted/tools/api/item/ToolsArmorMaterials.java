package com.grim3212.assorted.tools.api.item;

import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.ToolsCommonMod;
import com.grim3212.assorted.tools.api.ToolsTags;
import com.grim3212.assorted.tools.config.ArmorMaterialConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.function.Supplier;

/**
 * The mod's armour materials and their configuration defaults. The configured numbers become an
 * {@code ArmorMaterial} in {@link ArmorMaterialConfig#material()}; this holds the fixed parts:
 * equip sound, repair tag and equipment asset. Every material needs a matching {@code
 * assets/assortedtools/equipment/<name>.json}, or the armour renders untextured.
 */
public enum ToolsArmorMaterials {
    CHICKEN_SUIT(() -> ToolsCommonMod.COMMON_CONFIG.chickenSuitArmorMaterial, () -> BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.WOOL_PLACE), () -> LibCommonTags.Items.FEATHERS),
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
    private final Supplier<Holder<SoundEvent>> equipSound;
    private final Supplier<TagKey<Item>> repairMaterial;
    private final ResourceKey<EquipmentAsset> assetId;

    ToolsArmorMaterials(Supplier<ArmorMaterialConfig> material, Supplier<Holder<SoundEvent>> equipSound, Supplier<TagKey<Item>> repairTagIn) {
        this.material = material;
        this.equipSound = equipSound;
        this.repairMaterial = repairTagIn;
        this.assetId = ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(Constants.MOD_ID, this.name().toLowerCase(java.util.Locale.ROOT)));
    }

    public ArmorMaterialConfig config() {
        return this.material.get();
    }

    public Holder<SoundEvent> equipSound() {
        return this.equipSound.get();
    }

    public TagKey<Item> repairTag() {
        return this.repairMaterial.get();
    }

    public ResourceKey<EquipmentAsset> assetId() {
        return this.assetId;
    }

    public String getName() {
        return this.name().toLowerCase(java.util.Locale.ROOT);
    }
}
