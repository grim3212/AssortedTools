package com.grim3212.assorted.tools.common.item;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.ToolsCommonMod;
import com.grim3212.assorted.tools.ToolsServices;
import com.grim3212.assorted.tools.common.item.configurable.ConfigurableArmorItem;
import com.grim3212.assorted.tools.config.ArmorMaterialConfig;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import com.grim3212.assorted.tools.config.ModdedItemTierConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ToolsItems {

    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID);

    public static final IRegistryObject<HammerItem> NETHERITE_HAMMER = register("netherite_hammer", () -> new HammerItem(ToolsCommonMod.COMMON_CONFIG.netheriteItemTier, new Item.Properties()));
    public static final IRegistryObject<HammerItem> DIAMOND_HAMMER = register("diamond_hammer", () -> new HammerItem(ToolsCommonMod.COMMON_CONFIG.diamondItemTier, new Item.Properties()));
    public static final IRegistryObject<HammerItem> GOLD_HAMMER = register("gold_hammer", () -> new HammerItem(ToolsCommonMod.COMMON_CONFIG.goldItemTier, new Item.Properties()));
    public static final IRegistryObject<HammerItem> IRON_HAMMER = register("iron_hammer", () -> new HammerItem(ToolsCommonMod.COMMON_CONFIG.ironItemTier, new Item.Properties()));
    public static final IRegistryObject<HammerItem> STONE_HAMMER = register("stone_hammer", () -> new HammerItem(ToolsCommonMod.COMMON_CONFIG.stoneItemTier, new Item.Properties()));
    public static final IRegistryObject<HammerItem> WOOD_HAMMER = register("wood_hammer", () -> new HammerItem(ToolsCommonMod.COMMON_CONFIG.woodItemTier, new Item.Properties()));

    public static final IRegistryObject<BoomerangItem> WOOD_BOOMERANG = register("wood_boomerang", () -> new BoomerangItem(true, new Item.Properties().stacksTo(1)));
    public static final IRegistryObject<BoomerangItem> DIAMOND_BOOMERANG = register("diamond_boomerang", () -> new BoomerangItem(false, new Item.Properties().stacksTo(1)));

    public static final IRegistryObject<WandBuildingItem> BUILDING_WAND = register("building_wand", () -> new WandBuildingItem(false, new Item.Properties().durability(30)));
    public static final IRegistryObject<WandBuildingItem> REINFORCED_BUILDING_WAND = register("reinforced_building_wand", () -> new WandBuildingItem(true, new Item.Properties().durability(200)));
    public static final IRegistryObject<WandBreakingItem> BREAKING_WAND = register("breaking_wand", () -> new WandBreakingItem(false, new Item.Properties().durability(15)));
    public static final IRegistryObject<WandBreakingItem> REINFORCED_BREAKING_WAND = register("reinforced_breaking_wand", () -> new WandBreakingItem(true, new Item.Properties().durability(120)));
    public static final IRegistryObject<WandMiningItem> MINING_WAND = register("mining_wand", () -> new WandMiningItem(false, new Item.Properties().durability(15)));
    public static final IRegistryObject<WandMiningItem> REINFORCED_MINING_WAND = register("reinforced_mining_wand", () -> new WandMiningItem(true, new Item.Properties().durability(120)));

    public static final IRegistryObject<ChickenSuitArmor> CHICKEN_SUIT_HELMET = register("chicken_suit_helmet", () -> new ChickenSuitArmor(EquipmentSlot.HEAD, new Item.Properties()));
    public static final IRegistryObject<ChickenSuitArmor> CHICKEN_SUIT_CHESTPLATE = register("chicken_suit_chestplate", () -> new ChickenSuitArmor(EquipmentSlot.CHEST, new Item.Properties()));
    public static final IRegistryObject<ChickenSuitArmor> CHICKEN_SUIT_LEGGINGS = register("chicken_suit_leggings", () -> new ChickenSuitArmor(EquipmentSlot.LEGS, new Item.Properties()));
    public static final IRegistryObject<ChickenSuitArmor> CHICKEN_SUIT_BOOTS = register("chicken_suit_boots", () -> new ChickenSuitArmor(EquipmentSlot.FEET, new Item.Properties()));

    public static final IRegistryObject<PokeballItem> POKEBALL = register("pokeball", () -> new PokeballItem(new Item.Properties()));

    public static final IRegistryObject<MultiToolItem> WOODEN_MULTITOOL = register("wooden_multitool", () -> ToolsServices.TOOLS.getMultiToolItem(ToolsCommonMod.COMMON_CONFIG.woodItemTier, new Item.Properties()));
    public static final IRegistryObject<MultiToolItem> STONE_MULTITOOL = register("stone_multitool", () -> ToolsServices.TOOLS.getMultiToolItem(ToolsCommonMod.COMMON_CONFIG.stoneItemTier, new Item.Properties()));
    public static final IRegistryObject<MultiToolItem> GOLDEN_MULTITOOL = register("golden_multitool", () -> ToolsServices.TOOLS.getMultiToolItem(ToolsCommonMod.COMMON_CONFIG.goldItemTier, new Item.Properties()));
    public static final IRegistryObject<MultiToolItem> IRON_MULTITOOL = register("iron_multitool", () -> ToolsServices.TOOLS.getMultiToolItem(ToolsCommonMod.COMMON_CONFIG.ironItemTier, new Item.Properties()));
    public static final IRegistryObject<MultiToolItem> DIAMOND_MULTITOOL = register("diamond_multitool", () -> ToolsServices.TOOLS.getMultiToolItem(ToolsCommonMod.COMMON_CONFIG.diamondItemTier, new Item.Properties()));
    public static final IRegistryObject<MultiToolItem> NETHERITE_MULTITOOL = register("netherite_multitool", () -> ToolsServices.TOOLS.getMultiToolItem(ToolsCommonMod.COMMON_CONFIG.netheriteItemTier, new Item.Properties()));

    public static final IRegistryObject<BetterSpearItem> WOOD_SPEAR = register("wood_spear", () -> new BetterSpearItem(new Item.Properties(), ToolsCommonMod.COMMON_CONFIG.woodItemTier));
    public static final IRegistryObject<BetterSpearItem> STONE_SPEAR = register("stone_spear", () -> new BetterSpearItem(new Item.Properties(), ToolsCommonMod.COMMON_CONFIG.stoneItemTier));
    public static final IRegistryObject<BetterSpearItem> IRON_SPEAR = register("iron_spear", () -> new BetterSpearItem(new Item.Properties(), ToolsCommonMod.COMMON_CONFIG.ironItemTier));
    public static final IRegistryObject<BetterSpearItem> GOLD_SPEAR = register("gold_spear", () -> new BetterSpearItem(new Item.Properties(), ToolsCommonMod.COMMON_CONFIG.goldItemTier));
    public static final IRegistryObject<BetterSpearItem> DIAMOND_SPEAR = register("diamond_spear", () -> new BetterSpearItem(new Item.Properties(), ToolsCommonMod.COMMON_CONFIG.diamondItemTier));
    public static final IRegistryObject<BetterSpearItem> NETHERITE_SPEAR = register("netherite_spear", () -> new BetterSpearItem(new Item.Properties(), ToolsCommonMod.COMMON_CONFIG.netheriteItemTier));

    public static final IRegistryObject<BetterBucketItem> WOOD_BUCKET = register("wood_bucket", () -> new BetterBucketItem(new Item.Properties(), ToolsCommonMod.COMMON_CONFIG.woodItemTier));
    public static final IRegistryObject<BetterMilkBucketItem> WOOD_MILK_BUCKET = register("wood_milk_bucket", () -> new BetterMilkBucketItem(() -> WOOD_BUCKET.get(), new Item.Properties()));
    public static final IRegistryObject<BetterBucketItem> STONE_BUCKET = register("stone_bucket", () -> new BetterBucketItem(new Item.Properties(), ToolsCommonMod.COMMON_CONFIG.stoneItemTier));
    public static final IRegistryObject<BetterMilkBucketItem> STONE_MILK_BUCKET = register("stone_milk_bucket", () -> new BetterMilkBucketItem(() -> STONE_BUCKET.get(), new Item.Properties()));
    public static final IRegistryObject<BetterBucketItem> GOLD_BUCKET = register("gold_bucket", () -> new BetterBucketItem(new Item.Properties(), ToolsCommonMod.COMMON_CONFIG.goldItemTier));
    public static final IRegistryObject<BetterMilkBucketItem> GOLD_MILK_BUCKET = register("gold_milk_bucket", () -> new BetterMilkBucketItem(() -> GOLD_BUCKET.get(), new Item.Properties()));
    public static final IRegistryObject<BetterBucketItem> DIAMOND_BUCKET = register("diamond_bucket", () -> new BetterBucketItem(new Item.Properties(), ToolsCommonMod.COMMON_CONFIG.diamondItemTier));
    public static final IRegistryObject<BetterMilkBucketItem> DIAMOND_MILK_BUCKET = register("diamond_milk_bucket", () -> new BetterMilkBucketItem(() -> DIAMOND_BUCKET.get(), new Item.Properties()));
    public static final IRegistryObject<BetterBucketItem> NETHERITE_BUCKET = register("netherite_bucket", () -> new BetterBucketItem(new Item.Properties(), ToolsCommonMod.COMMON_CONFIG.netheriteItemTier));
    public static final IRegistryObject<BetterMilkBucketItem> NETHERITE_MILK_BUCKET = register("netherite_milk_bucket", () -> new BetterMilkBucketItem(() -> NETHERITE_BUCKET.get(), new Item.Properties()));

    public static final IRegistryObject<MaterialShears> WOOD_SHEARS = register("wood_shears", () -> new MaterialShears(new Item.Properties(), ToolsCommonMod.COMMON_CONFIG.woodItemTier));
    public static final IRegistryObject<MaterialShears> STONE_SHEARS = register("stone_shears", () -> new MaterialShears(new Item.Properties(), ToolsCommonMod.COMMON_CONFIG.stoneItemTier));
    public static final IRegistryObject<MaterialShears> GOLD_SHEARS = register("gold_shears", () -> new MaterialShears(new Item.Properties(), ToolsCommonMod.COMMON_CONFIG.goldItemTier));
    public static final IRegistryObject<MaterialShears> DIAMOND_SHEARS = register("diamond_shears", () -> new MaterialShears(new Item.Properties(), ToolsCommonMod.COMMON_CONFIG.diamondItemTier));
    public static final IRegistryObject<MaterialShears> NETHERITE_SHEARS = register("netherite_shears", () -> new MaterialShears(new Item.Properties(), ToolsCommonMod.COMMON_CONFIG.netheriteItemTier));

    public static final IRegistryObject<FragmentItem> U_FRAGMENT = register("u_fragment", () -> new FragmentItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final IRegistryObject<FragmentItem> L_FRAGMENT = register("l_fragment", () -> new FragmentItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final IRegistryObject<FragmentItem> T_FRAGMENT = register("t_fragment", () -> new FragmentItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final IRegistryObject<FragmentItem> I_FRAGMENT = register("i_fragment", () -> new FragmentItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final IRegistryObject<FragmentItem> M_FRAGMENT = register("m_fragment", () -> new FragmentItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final IRegistryObject<FragmentItem> A_FRAGMENT = register("a_fragment", () -> new FragmentItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final IRegistryObject<FragmentItem> MISSING_FRAGMENT = register("missing_fragment", () -> new FragmentItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final IRegistryObject<FragmentItem> E_FRAGMENT = register("e_fragment", () -> new FragmentItem(new Item.Properties().rarity(Rarity.RARE)));

    public static final IRegistryObject<UltimateFistItem> ULTIMATE_FIST = register("ultimate_fist", () -> new UltimateFistItem(new Item.Properties().rarity(Rarity.EPIC)));

    public static final Map<String, MaterialGroup> MATERIAL_GROUPS = Maps.newHashMap();

    static {
        ToolsCommonMod.COMMON_CONFIG.moddedTiers.forEach((s, tier) -> MATERIAL_GROUPS.put(s, new MaterialGroup(tier, ToolsCommonMod.COMMON_CONFIG.moddedArmors.get(s))));
    }

    public static List<Item> buckets() {
        List<Item> buckets = Lists.newArrayList(WOOD_BUCKET.get(), STONE_BUCKET.get(), GOLD_BUCKET.get(), DIAMOND_BUCKET.get(), NETHERITE_BUCKET.get());
        MATERIAL_GROUPS.values().stream().forEach((mg) -> buckets.add(mg.BUCKET.get()));
        return buckets;
    }

    private static <T extends Item> IRegistryObject<T> register(final String name, final Supplier<T> sup) {
        return ITEMS.register(name, sup);
    }

    public static final class MaterialGroup {
        public final IRegistryObject<MaterialPickaxeItem> PICKAXE;
        public final IRegistryObject<MaterialShovelItem> SHOVEL;
        public final IRegistryObject<MaterialAxeItem> AXE;
        public final IRegistryObject<MaterialHoeItem> HOE;
        public final IRegistryObject<MaterialSwordItem> SWORD;
        public final IRegistryObject<HammerItem> HAMMER;
        public final IRegistryObject<MultiToolItem> MULTITOOL;
        public final IRegistryObject<BetterSpearItem> SPEAR;
        public final IRegistryObject<ConfigurableArmorItem> HELMET;
        public final IRegistryObject<ConfigurableArmorItem> CHESTPLATE;
        public final IRegistryObject<ConfigurableArmorItem> LEGGINGS;
        public final IRegistryObject<ConfigurableArmorItem> BOOTS;
        public final IRegistryObject<BetterBucketItem> BUCKET;
        public final IRegistryObject<BetterMilkBucketItem> MILK_BUCKET;
        public final IRegistryObject<MaterialShears> SHEARS;

        public final TagKey<Item> material;

        public final ItemTierConfig tier;

        public MaterialGroup(ModdedItemTierConfig tier, ArmorMaterialConfig armor) {
            this.PICKAXE = register(tier.getName() + "_pickaxe", () -> new MaterialPickaxeItem(tier, new Item.Properties()));
            this.SHOVEL = register(tier.getName() + "_shovel", () -> new MaterialShovelItem(tier, new Item.Properties()));
            this.AXE = register(tier.getName() + "_axe", () -> new MaterialAxeItem(tier, new Item.Properties()));
            this.HOE = register(tier.getName() + "_hoe", () -> new MaterialHoeItem(tier, new Item.Properties()));
            this.SWORD = register(tier.getName() + "_sword", () -> new MaterialSwordItem(tier, new Item.Properties()));

            this.HAMMER = register(tier.getName() + "_hammer", () -> new HammerItem(tier, new Item.Properties()));
            this.MULTITOOL = register(tier.getName() + "_multitool", () -> ToolsServices.TOOLS.getMultiToolItem(tier, new Item.Properties()));
            this.SPEAR = register(tier.getName() + "_spear", () -> new BetterSpearItem(new Item.Properties(), tier));

            this.BUCKET = register(tier.getName() + "_bucket", () -> new BetterBucketItem(new Item.Properties(), tier));
            this.MILK_BUCKET = register(tier.getName() + "_milk_bucket", () -> new BetterMilkBucketItem(() -> this.BUCKET.get(), new Item.Properties()));
            this.SHEARS = register(tier.getName() + "_shears", () -> new MaterialShears(new Item.Properties(), tier));

            if (armor != null) {
                this.HELMET = register(tier.getName() + "_helmet", () -> new MaterialArmorItem(armor.getMaterial(), EquipmentSlot.HEAD, new Item.Properties()));
                this.CHESTPLATE = register(tier.getName() + "_chestplate", () -> new MaterialArmorItem(armor.getMaterial(), EquipmentSlot.CHEST, new Item.Properties()));
                this.LEGGINGS = register(tier.getName() + "_leggings", () -> new MaterialArmorItem(armor.getMaterial(), EquipmentSlot.LEGS, new Item.Properties()));
                this.BOOTS = register(tier.getName() + "_boots", () -> new MaterialArmorItem(armor.getMaterial(), EquipmentSlot.FEET, new Item.Properties()));
            } else {
                throw new NullPointerException("Got null ArmorMaterialHolder when registering Extra Materials");
            }

            this.material = tier.getMaterial();
            this.tier = tier;
        }
    }

    public static void init() {
    }
}
