package com.grim3212.assorted.tools.common.item;

import java.util.function.Supplier;

import com.grim3212.assorted.tools.AssortedTools;

import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ToolsItems {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AssortedTools.MODID);

	public static final RegistryObject<HammerItem> NETHERITE_HAMMER = register("netherite_hammer", () -> new HammerItem(ItemTier.NETHERITE, new Item.Properties().group(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));
	public static final RegistryObject<HammerItem> DIAMOND_HAMMER = register("diamond_hammer", () -> new HammerItem(ItemTier.DIAMOND, new Item.Properties().group(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));
	public static final RegistryObject<HammerItem> GOLD_HAMMER = register("gold_hammer", () -> new HammerItem(ItemTier.GOLD, new Item.Properties().group(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));
	public static final RegistryObject<HammerItem> IRON_HAMMER = register("iron_hammer", () -> new HammerItem(ItemTier.IRON, new Item.Properties().group(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));
	public static final RegistryObject<HammerItem> STONE_HAMMER = register("stone_hammer", () -> new HammerItem(ItemTier.STONE, new Item.Properties().group(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));
	public static final RegistryObject<HammerItem> WOOD_HAMMER = register("wood_hammer", () -> new HammerItem(ItemTier.WOOD, new Item.Properties().group(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP)));

	public static final RegistryObject<BoomerangItem> WOOD_BOOMERANG = register("wood_boomerang", () -> new BoomerangItem(true, new Item.Properties().group(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP).maxStackSize(1)));
	public static final RegistryObject<BoomerangItem> DIAMOND_BOOMERANG = register("diamond_boomerang", () -> new BoomerangItem(false, new Item.Properties().group(AssortedTools.ASSORTED_TOOLS_ITEM_GROUP).maxStackSize(1)));

	public static final RegistryObject<WandBuildingItem> BUILDING_WAND = register("building_wand", () -> new WandBuildingItem(false, new Item.Properties().maxDamage(30)));
	public static final RegistryObject<WandBuildingItem> REINFORCED_BUILDING_WAND = register("reinforced_building_wand", () -> new WandBuildingItem(true, new Item.Properties().maxDamage(200)));
	public static final RegistryObject<WandBreakingItem> BREAKING_WAND = register("breaking_wand", () -> new WandBreakingItem(false, new Item.Properties().maxDamage(15)));
	public static final RegistryObject<WandBreakingItem> REINFORCED_BREAKING_WAND = register("reinforced_breaking_wand", () -> new WandBreakingItem(true, new Item.Properties().maxDamage(120)));
	public static final RegistryObject<WandMiningItem> MINING_WAND = register("mining_wand", () -> new WandMiningItem(false, new Item.Properties().maxDamage(15)));
	public static final RegistryObject<WandMiningItem> REINFORCED_MINING_WAND = register("reinforced_mining_wand", () -> new WandMiningItem(true, new Item.Properties().maxDamage(120)));

	private static <T extends Item> RegistryObject<T> register(final String name, final Supplier<T> sup) {
		return ITEMS.register(name, sup);
	}
}
