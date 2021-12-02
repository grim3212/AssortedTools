package com.grim3212.assorted.tools.common.item;

import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.Maps;
import com.grim3212.assorted.tools.AssortedTools;
import com.grim3212.assorted.tools.common.handler.ArmorMaterialHolder;
import com.grim3212.assorted.tools.common.handler.ModdedItemTierHolder;
import com.grim3212.assorted.tools.common.handler.ToolsConfig;
import com.grim3212.assorted.tools.common.item.configurable.ConfigurableArmorItem;

import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ToolsItems {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AssortedTools.MODID);

	public static final RegistryObject<HammerItem> NETHERITE_HAMMER = register("netherite_hammer", () -> new HammerItem(ToolsConfig.COMMON.netheriteItemTier, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));
	public static final RegistryObject<HammerItem> DIAMOND_HAMMER = register("diamond_hammer", () -> new HammerItem(ToolsConfig.COMMON.diamondItemTier, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));
	public static final RegistryObject<HammerItem> GOLD_HAMMER = register("gold_hammer", () -> new HammerItem(ToolsConfig.COMMON.goldItemTier, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));
	public static final RegistryObject<HammerItem> IRON_HAMMER = register("iron_hammer", () -> new HammerItem(ToolsConfig.COMMON.ironItemTier, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));
	public static final RegistryObject<HammerItem> STONE_HAMMER = register("stone_hammer", () -> new HammerItem(ToolsConfig.COMMON.stoneItemTier, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));
	public static final RegistryObject<HammerItem> WOOD_HAMMER = register("wood_hammer", () -> new HammerItem(ToolsConfig.COMMON.woodItemTier, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));

	public static final RegistryObject<BoomerangItem> WOOD_BOOMERANG = register("wood_boomerang", () -> new BoomerangItem(true, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP).stacksTo(1)));
	public static final RegistryObject<BoomerangItem> DIAMOND_BOOMERANG = register("diamond_boomerang", () -> new BoomerangItem(false, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP).stacksTo(1)));

	public static final RegistryObject<WandBuildingItem> BUILDING_WAND = register("building_wand", () -> new WandBuildingItem(false, new Item.Properties().durability(30)));
	public static final RegistryObject<WandBuildingItem> REINFORCED_BUILDING_WAND = register("reinforced_building_wand", () -> new WandBuildingItem(true, new Item.Properties().durability(200)));
	public static final RegistryObject<WandBreakingItem> BREAKING_WAND = register("breaking_wand", () -> new WandBreakingItem(false, new Item.Properties().durability(15)));
	public static final RegistryObject<WandBreakingItem> REINFORCED_BREAKING_WAND = register("reinforced_breaking_wand", () -> new WandBreakingItem(true, new Item.Properties().durability(120)));
	public static final RegistryObject<WandMiningItem> MINING_WAND = register("mining_wand", () -> new WandMiningItem(false, new Item.Properties().durability(15)));
	public static final RegistryObject<WandMiningItem> REINFORCED_MINING_WAND = register("reinforced_mining_wand", () -> new WandMiningItem(true, new Item.Properties().durability(120)));

	public static final RegistryObject<ChickenSuitArmor> CHICKEN_SUIT_HELMET = register("chicken_suit_helmet", () -> new ChickenSuitArmor(EquipmentSlot.HEAD, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));
	public static final RegistryObject<ChickenSuitArmor> CHICKEN_SUIT_CHESTPLATE = register("chicken_suit_chestplate", () -> new ChickenSuitArmor(EquipmentSlot.CHEST, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));
	public static final RegistryObject<ChickenSuitArmor> CHICKEN_SUIT_LEGGINGS = register("chicken_suit_leggings", () -> new ChickenSuitArmor(EquipmentSlot.LEGS, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));
	public static final RegistryObject<ChickenSuitArmor> CHICKEN_SUIT_BOOTS = register("chicken_suit_boots", () -> new ChickenSuitArmor(EquipmentSlot.FEET, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));

	public static final RegistryObject<PokeballItem> POKEBALL = register("pokeball", () -> new PokeballItem(new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));

	public static final RegistryObject<MultiToolItem> WOODEN_MULTITOOL = register("wooden_multitool", () -> new MultiToolItem(ToolsConfig.COMMON.woodItemTier, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));
	public static final RegistryObject<MultiToolItem> STONE_MULTITOOL = register("stone_multitool", () -> new MultiToolItem(ToolsConfig.COMMON.stoneItemTier, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));
	public static final RegistryObject<MultiToolItem> GOLDEN_MULTITOOL = register("golden_multitool", () -> new MultiToolItem(ToolsConfig.COMMON.goldItemTier, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));
	public static final RegistryObject<MultiToolItem> IRON_MULTITOOL = register("iron_multitool", () -> new MultiToolItem(ToolsConfig.COMMON.ironItemTier, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));
	public static final RegistryObject<MultiToolItem> DIAMOND_MULTITOOL = register("diamond_multitool", () -> new MultiToolItem(ToolsConfig.COMMON.diamondItemTier, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));
	public static final RegistryObject<MultiToolItem> NETHERITE_MULTITOOL = register("netherite_multitool", () -> new MultiToolItem(ToolsConfig.COMMON.netheriteItemTier, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));

	public static final RegistryObject<BetterSpearItem> WOOD_SPEAR = register("wood_spear", () -> new BetterSpearItem(new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP), ToolsConfig.COMMON.woodItemTier));
	public static final RegistryObject<BetterSpearItem> STONE_SPEAR = register("stone_spear", () -> new BetterSpearItem(new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP), ToolsConfig.COMMON.stoneItemTier));
	public static final RegistryObject<BetterSpearItem> IRON_SPEAR = register("iron_spear", () -> new BetterSpearItem(new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP), ToolsConfig.COMMON.ironItemTier));
	public static final RegistryObject<BetterSpearItem> GOLD_SPEAR = register("gold_spear", () -> new BetterSpearItem(new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP), ToolsConfig.COMMON.goldItemTier));
	public static final RegistryObject<BetterSpearItem> DIAMOND_SPEAR = register("diamond_spear", () -> new BetterSpearItem(new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP), ToolsConfig.COMMON.diamondItemTier));
	public static final RegistryObject<BetterSpearItem> NETHERITE_SPEAR = register("netherite_spear", () -> new BetterSpearItem(new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP), ToolsConfig.COMMON.netheriteItemTier));

	public static final Map<String, MaterialGroup> MATERIAL_GROUPS = Maps.newHashMap();

	static {

		ToolsConfig.COMMON.moddedTiers.forEach((s, tier) -> MATERIAL_GROUPS.put(s, new MaterialGroup(tier, ToolsConfig.COMMON.moddedArmors.get(s))));

	}

	private static <T extends Item> RegistryObject<T> register(final String name, final Supplier<T> sup) {
		return ITEMS.register(name, sup);
	}

	public static final class MaterialGroup {
		public final RegistryObject<MaterialPickaxeItem> PICKAXE;
		public final RegistryObject<MaterialShovelItem> SHOVEL;
		public final RegistryObject<MaterialAxeItem> AXE;
		public final RegistryObject<MaterialHoeItem> HOE;
		public final RegistryObject<MaterialSwordItem> SWORD;
		public final RegistryObject<HammerItem> HAMMER;
		public final RegistryObject<MultiToolItem> MULTITOOL;
		public final RegistryObject<BetterSpearItem> SPEAR;
		public final RegistryObject<ConfigurableArmorItem> HELMET;
		public final RegistryObject<ConfigurableArmorItem> CHESTPLATE;
		public final RegistryObject<ConfigurableArmorItem> LEGGINGS;
		public final RegistryObject<ConfigurableArmorItem> BOOTS;

		public final Tag<Item> material;

		public MaterialGroup(ModdedItemTierHolder tier, ArmorMaterialHolder armor) {
			this.PICKAXE = register(tier.getName() + "_pickaxe", () -> new MaterialPickaxeItem(tier, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));
			this.SHOVEL = register(tier.getName() + "_shovel", () -> new MaterialShovelItem(tier, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));
			this.AXE = register(tier.getName() + "_axe", () -> new MaterialAxeItem(tier, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));
			this.HOE = register(tier.getName() + "_hoe", () -> new MaterialHoeItem(tier, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));
			this.SWORD = register(tier.getName() + "_sword", () -> new MaterialSwordItem(tier, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));

			this.HAMMER = register(tier.getName() + "_hammer", () -> new HammerItem(tier, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP), true));
			this.MULTITOOL = register(tier.getName() + "_multitool", () -> new MultiToolItem(tier, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP), true));
			this.SPEAR = register(tier.getName() + "_spear", () -> new BetterSpearItem(new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP), tier, true));

			if (armor != null) {
				this.HELMET = register(tier.getName() + "_helmet", () -> new MaterialArmorItem(armor.getMaterial(), EquipmentSlot.HEAD, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));
				this.CHESTPLATE = register(tier.getName() + "_chestplate", () -> new MaterialArmorItem(armor.getMaterial(), EquipmentSlot.CHEST, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));
				this.LEGGINGS = register(tier.getName() + "_leggings", () -> new MaterialArmorItem(armor.getMaterial(), EquipmentSlot.LEGS, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));
				this.BOOTS = register(tier.getName() + "_boots", () -> new MaterialArmorItem(armor.getMaterial(), EquipmentSlot.FEET, new Item.Properties().tab(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));
			} else {
				throw new NullPointerException("Got null ArmorMaterialHolder when registering Extra Materials");
			}

			this.material = tier.getMaterial();
		}
	}
}
