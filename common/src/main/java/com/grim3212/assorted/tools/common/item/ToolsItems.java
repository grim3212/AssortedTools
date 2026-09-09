package com.grim3212.assorted.tools.common.item;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.ToolsCommonMod;
import com.grim3212.assorted.tools.common.item.configurable.ConfigurableArmorItem;
import com.grim3212.assorted.tools.config.ArmorMaterialConfig;
import com.grim3212.assorted.tools.config.ItemTierConfig;
import com.grim3212.assorted.tools.config.ModdedItemTierConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ToolsItems {

    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID);

    public static final IRegistryObject<HammerItem> NETHERITE_HAMMER = register("netherite_hammer", props -> new HammerItem(ToolsCommonMod.COMMON_CONFIG.netheriteItemTier, props.fireResistant()));
    public static final IRegistryObject<HammerItem> DIAMOND_HAMMER = register("diamond_hammer", props -> new HammerItem(ToolsCommonMod.COMMON_CONFIG.diamondItemTier, props));
    public static final IRegistryObject<HammerItem> GOLD_HAMMER = register("gold_hammer", props -> new HammerItem(ToolsCommonMod.COMMON_CONFIG.goldItemTier, props));
    public static final IRegistryObject<HammerItem> IRON_HAMMER = register("iron_hammer", props -> new HammerItem(ToolsCommonMod.COMMON_CONFIG.ironItemTier, props));
    public static final IRegistryObject<HammerItem> STONE_HAMMER = register("stone_hammer", props -> new HammerItem(ToolsCommonMod.COMMON_CONFIG.stoneItemTier, props));
    public static final IRegistryObject<HammerItem> WOOD_HAMMER = register("wood_hammer", props -> new HammerItem(ToolsCommonMod.COMMON_CONFIG.woodItemTier, props));

    public static final IRegistryObject<BoomerangItem> WOOD_BOOMERANG = register("wood_boomerang", props -> new BoomerangItem(true, props.stacksTo(1)));
    public static final IRegistryObject<BoomerangItem> DIAMOND_BOOMERANG = register("diamond_boomerang", props -> new BoomerangItem(false, props.stacksTo(1)));

    public static final IRegistryObject<WandBuildingItem> BUILDING_WAND = register("building_wand", props -> new WandBuildingItem(false, props.durability(30)));
    public static final IRegistryObject<WandBuildingItem> REINFORCED_BUILDING_WAND = register("reinforced_building_wand", props -> new WandBuildingItem(true, props.durability(200)));
    public static final IRegistryObject<WandBreakingItem> BREAKING_WAND = register("breaking_wand", props -> new WandBreakingItem(false, props.durability(15)));
    public static final IRegistryObject<WandBreakingItem> REINFORCED_BREAKING_WAND = register("reinforced_breaking_wand", props -> new WandBreakingItem(true, props.durability(120)));
    public static final IRegistryObject<WandMiningItem> MINING_WAND = register("mining_wand", props -> new WandMiningItem(false, props.durability(15)));
    public static final IRegistryObject<WandMiningItem> REINFORCED_MINING_WAND = register("reinforced_mining_wand", props -> new WandMiningItem(true, props.durability(120)));

    public static final IRegistryObject<ChickenSuitArmor> CHICKEN_SUIT_HELMET = register("chicken_suit_helmet", props -> new ChickenSuitArmor(ArmorType.HELMET, props));
    public static final IRegistryObject<ChickenSuitArmor> CHICKEN_SUIT_CHESTPLATE = register("chicken_suit_chestplate", props -> new ChickenSuitArmor(ArmorType.CHESTPLATE, props));
    public static final IRegistryObject<ChickenSuitArmor> CHICKEN_SUIT_LEGGINGS = register("chicken_suit_leggings", props -> new ChickenSuitArmor(ArmorType.LEGGINGS, props));
    public static final IRegistryObject<ChickenSuitArmor> CHICKEN_SUIT_BOOTS = register("chicken_suit_boots", props -> new ChickenSuitArmor(ArmorType.BOOTS, props));

    public static final IRegistryObject<PokeballItem> POKEBALL = register("pokeball", props -> new PokeballItem(props));

    public static final IRegistryObject<MultiToolItem> WOODEN_MULTITOOL = register("wooden_multitool", props -> new MultiToolItem(ToolsCommonMod.COMMON_CONFIG.woodItemTier, props));
    public static final IRegistryObject<MultiToolItem> STONE_MULTITOOL = register("stone_multitool", props -> new MultiToolItem(ToolsCommonMod.COMMON_CONFIG.stoneItemTier, props));
    public static final IRegistryObject<MultiToolItem> GOLDEN_MULTITOOL = register("golden_multitool", props -> new MultiToolItem(ToolsCommonMod.COMMON_CONFIG.goldItemTier, props));
    public static final IRegistryObject<MultiToolItem> IRON_MULTITOOL = register("iron_multitool", props -> new MultiToolItem(ToolsCommonMod.COMMON_CONFIG.ironItemTier, props));
    public static final IRegistryObject<MultiToolItem> DIAMOND_MULTITOOL = register("diamond_multitool", props -> new MultiToolItem(ToolsCommonMod.COMMON_CONFIG.diamondItemTier, props));
    public static final IRegistryObject<MultiToolItem> NETHERITE_MULTITOOL = register("netherite_multitool", props -> new MultiToolItem(ToolsCommonMod.COMMON_CONFIG.netheriteItemTier, props.fireResistant()));

    public static final IRegistryObject<BetterSpearItem> WOOD_SPEAR = register("wood_spear", props -> new BetterSpearItem(props, ToolsCommonMod.COMMON_CONFIG.woodItemTier));
    public static final IRegistryObject<BetterSpearItem> STONE_SPEAR = register("stone_spear", props -> new BetterSpearItem(props, ToolsCommonMod.COMMON_CONFIG.stoneItemTier));
    public static final IRegistryObject<BetterSpearItem> IRON_SPEAR = register("iron_spear", props -> new BetterSpearItem(props, ToolsCommonMod.COMMON_CONFIG.ironItemTier));
    public static final IRegistryObject<BetterSpearItem> GOLD_SPEAR = register("gold_spear", props -> new BetterSpearItem(props, ToolsCommonMod.COMMON_CONFIG.goldItemTier));
    public static final IRegistryObject<BetterSpearItem> DIAMOND_SPEAR = register("diamond_spear", props -> new BetterSpearItem(props, ToolsCommonMod.COMMON_CONFIG.diamondItemTier));
    public static final IRegistryObject<BetterSpearItem> NETHERITE_SPEAR = register("netherite_spear", props -> new BetterSpearItem(props.fireResistant(), ToolsCommonMod.COMMON_CONFIG.netheriteItemTier));

    public static final IRegistryObject<BetterBucketItem> WOOD_BUCKET = register("wood_bucket", props -> new BetterBucketItem(props, ToolsCommonMod.COMMON_CONFIG.woodItemTier));
    public static final IRegistryObject<BetterMilkBucketItem> WOOD_MILK_BUCKET = register("wood_milk_bucket", props -> new BetterMilkBucketItem(() -> WOOD_BUCKET.get(), props));
    public static final IRegistryObject<BetterBucketItem> STONE_BUCKET = register("stone_bucket", props -> new BetterBucketItem(props, ToolsCommonMod.COMMON_CONFIG.stoneItemTier));
    public static final IRegistryObject<BetterMilkBucketItem> STONE_MILK_BUCKET = register("stone_milk_bucket", props -> new BetterMilkBucketItem(() -> STONE_BUCKET.get(), props));
    public static final IRegistryObject<BetterBucketItem> GOLD_BUCKET = register("gold_bucket", props -> new BetterBucketItem(props, ToolsCommonMod.COMMON_CONFIG.goldItemTier));
    public static final IRegistryObject<BetterMilkBucketItem> GOLD_MILK_BUCKET = register("gold_milk_bucket", props -> new BetterMilkBucketItem(() -> GOLD_BUCKET.get(), props));
    public static final IRegistryObject<BetterBucketItem> DIAMOND_BUCKET = register("diamond_bucket", props -> new BetterBucketItem(props, ToolsCommonMod.COMMON_CONFIG.diamondItemTier));
    public static final IRegistryObject<BetterMilkBucketItem> DIAMOND_MILK_BUCKET = register("diamond_milk_bucket", props -> new BetterMilkBucketItem(() -> DIAMOND_BUCKET.get(), props));
    public static final IRegistryObject<BetterBucketItem> NETHERITE_BUCKET = register("netherite_bucket", props -> new BetterBucketItem(props.fireResistant(), ToolsCommonMod.COMMON_CONFIG.netheriteItemTier));
    public static final IRegistryObject<BetterMilkBucketItem> NETHERITE_MILK_BUCKET = register("netherite_milk_bucket", props -> new BetterMilkBucketItem(() -> NETHERITE_BUCKET.get(), props.fireResistant()));

    public static final IRegistryObject<MaterialShears> WOOD_SHEARS = register("wood_shears", props -> new MaterialShears(props, ToolsCommonMod.COMMON_CONFIG.woodItemTier));
    public static final IRegistryObject<MaterialShears> STONE_SHEARS = register("stone_shears", props -> new MaterialShears(props, ToolsCommonMod.COMMON_CONFIG.stoneItemTier));
    public static final IRegistryObject<MaterialShears> GOLD_SHEARS = register("gold_shears", props -> new MaterialShears(props, ToolsCommonMod.COMMON_CONFIG.goldItemTier));
    public static final IRegistryObject<MaterialShears> DIAMOND_SHEARS = register("diamond_shears", props -> new MaterialShears(props, ToolsCommonMod.COMMON_CONFIG.diamondItemTier));
    public static final IRegistryObject<MaterialShears> NETHERITE_SHEARS = register("netherite_shears", props -> new MaterialShears(props.fireResistant(), ToolsCommonMod.COMMON_CONFIG.netheriteItemTier));

    public static final IRegistryObject<FragmentItem> U_FRAGMENT = register("u_fragment", props -> new FragmentItem(props.rarity(Rarity.RARE)));
    public static final IRegistryObject<FragmentItem> L_FRAGMENT = register("l_fragment", props -> new FragmentItem(props.rarity(Rarity.RARE)));
    public static final IRegistryObject<FragmentItem> T_FRAGMENT = register("t_fragment", props -> new FragmentItem(props.rarity(Rarity.RARE)));
    public static final IRegistryObject<FragmentItem> I_FRAGMENT = register("i_fragment", props -> new FragmentItem(props.rarity(Rarity.RARE)));
    public static final IRegistryObject<FragmentItem> M_FRAGMENT = register("m_fragment", props -> new FragmentItem(props.rarity(Rarity.RARE)));
    public static final IRegistryObject<FragmentItem> A_FRAGMENT = register("a_fragment", props -> new FragmentItem(props.rarity(Rarity.RARE)));
    public static final IRegistryObject<FragmentItem> MISSING_FRAGMENT = register("missing_fragment", props -> new FragmentItem(props.rarity(Rarity.RARE)));
    public static final IRegistryObject<FragmentItem> E_FRAGMENT = register("e_fragment", props -> new FragmentItem(props.rarity(Rarity.RARE)));

    public static final IRegistryObject<UltimateFistItem> ULTIMATE_FIST = register("ultimate_fist", props -> new UltimateFistItem(props.fireResistant().rarity(Rarity.EPIC)));

    public static final Map<String, MaterialGroup> MATERIAL_GROUPS = Maps.newHashMap();

    static {
        ToolsCommonMod.COMMON_CONFIG.moddedTiers.forEach((s, tier) -> MATERIAL_GROUPS.put(s, new MaterialGroup(tier, ToolsCommonMod.COMMON_CONFIG.moddedArmors.get(s))));
    }

    public static List<Item> buckets() {
        List<Item> buckets = Lists.newArrayList(WOOD_BUCKET.get(), STONE_BUCKET.get(), GOLD_BUCKET.get(), DIAMOND_BUCKET.get(), NETHERITE_BUCKET.get());
        MATERIAL_GROUPS.values().stream().forEach((mg) -> buckets.add(mg.BUCKET.get()));
        return buckets;
    }

    private static <T extends Item> IRegistryObject<T> register(final String name, final Function<Item.Properties, ? extends T> factory) {
        // Since 1.21.2 every item has to know its own id before it is constructed, so the
        // properties are built here where the registration name is known.
        final ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        return ITEMS.register(name, () -> factory.apply(new Item.Properties().setId(key)));
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
            this.PICKAXE = register(tier.getName() + "_pickaxe", props -> new MaterialPickaxeItem(tier, props));
            this.SHOVEL = register(tier.getName() + "_shovel", props -> new MaterialShovelItem(tier, props));
            this.AXE = register(tier.getName() + "_axe", props -> new MaterialAxeItem(tier, props));
            this.HOE = register(tier.getName() + "_hoe", props -> new MaterialHoeItem(tier, props));
            this.SWORD = register(tier.getName() + "_sword", props -> new MaterialSwordItem(tier, props));

            this.HAMMER = register(tier.getName() + "_hammer", props -> new HammerItem(tier, props));
            this.MULTITOOL = register(tier.getName() + "_multitool", props -> new MultiToolItem(tier, props));
            this.SPEAR = register(tier.getName() + "_spear", props -> new BetterSpearItem(props, tier));

            this.BUCKET = register(tier.getName() + "_bucket", props -> new BetterBucketItem(props, tier));
            this.MILK_BUCKET = register(tier.getName() + "_milk_bucket", props -> new BetterMilkBucketItem(() -> this.BUCKET.get(), props));
            this.SHEARS = register(tier.getName() + "_shears", props -> new MaterialShears(props, tier));

            if (armor != null) {
                this.HELMET = register(tier.getName() + "_helmet", props -> new MaterialArmorItem(armor.getMaterial(), ArmorType.HELMET, props));
                this.CHESTPLATE = register(tier.getName() + "_chestplate", props -> new MaterialArmorItem(armor.getMaterial(), ArmorType.CHESTPLATE, props));
                this.LEGGINGS = register(tier.getName() + "_leggings", props -> new MaterialArmorItem(armor.getMaterial(), ArmorType.LEGGINGS, props));
                this.BOOTS = register(tier.getName() + "_boots", props -> new MaterialArmorItem(armor.getMaterial(), ArmorType.BOOTS, props));
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
