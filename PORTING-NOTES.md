# AssortedTools 26.2 — shared notes

Read `../PORTING-26.2.md` and `../UPGRADE-GUIDE-1.20.1-to-26.2.md` first. This file only covers what
is specific to this mod. **The policy in `PORTING-26.2.md` binds: no deprecated API, no
back-compat/conversion paths.**

## Build

```bash
cd D:/Development/MinecraftModding/AssortedTools
./gradlew :common:compileJava -PcommonOnly --console=plain > /tmp/<yourname>.txt 2>&1; echo "EXIT=$?"
```

`-PcommonOnly` narrows `settings.gradle` to the `common` module so the loader modules do not have to
compile. **Never edit `settings.gradle` to do this** — other agents are building at the same time.
Never pipe gradle into `tail`; the pipe eats the exit code.

## The foundation is already done — do not re-litigate it

`api/item/ToolsItemTier`, `api/item/ToolsArmorMaterials`, `api/item/HarvestTiers`,
`config/ItemTierConfig`, `config/ArmorMaterialConfig`, `config/ToolsCommonConfig` and all eight
`common/item/configurable/*` classes are ported. Read them before touching anything downstream.

The shape you need to know:

| Old | New |
|---|---|
| `Tier` | `ToolMaterial` — a record. `ItemTierConfig.material()` builds one from the configured values. |
| `ArmorMaterial` interface | `ArmorMaterial` record in `net.minecraft.world.item.equipment`. `ArmorMaterialConfig.material()` builds one. |
| `ArmorItem.Type` | `ArmorType` in `net.minecraft.world.item.equipment` |
| `tierHolder.getDefaultTier()` returning `Tier` | returns `ToolMaterial` |
| numeric harvest level | `TagKey<Block> incorrectBlocksForDrops`; `HarvestTiers.incorrectBlocksForDrops(int)` maps it |

**Deleted from vanilla — nothing to extend:** `ArmorItem`, `TieredItem`, `DiggerItem`,
`PickaxeItem`, `SwordItem`, `Tier`, `Tiers`, `ToolAction`/`ToolActions`, `TierSortingRegistry`,
`EnchantmentCategory`.
**Still present:** `ShovelItem`, `AxeItem`, `HoeItem` (they carry right-click behaviour, not stats),
`ToolMaterial`, `ArmorMaterial`, `ArmorType`, `Equippable`.

Tools and armour are plain `Item`s configured through `Item.Properties`:

```java
new Item.Properties().pickaxe(material, attackDamageBaseline, attackSpeedBaseline)
new Item.Properties().axe(material, dmg, spd)      // also .hoe .shovel .sword
new Item.Properties().tool(material, minesEfficientlyTag, dmg, spd, disableBlockingSeconds)
new Item.Properties().humanoidArmor(armorMaterial, ArmorType.HELMET)
new Item.Properties().durability(int).repairable(TagKey<Item>).enchantable(int)
```

Every stat is a data component fixed at construction. **Do not try to keep a stat live-configurable
by overriding a getter — the getters are gone.** `Item` no longer has `getMaxDamage(ItemStack)`,
`getEnchantmentValue()`, `isValidRepairItem`, or `getDefaultAttributeModifiers`. It still has
`getDestroySpeed(ItemStack, BlockState)` and `isCorrectToolForDrops(ItemStack, BlockState)`.

### `IItemExtraProperties` / `ExtraPropertyHelper` are dead here — strip them

The library's `ExtraPropertyHelper.getDamage/setDamage/isDamaged` now do exactly what
`ItemStack` does natively, because damage is the `minecraft:damage` component. Every AssortedTools
item that implemented `IItemExtraProperties` was doing nothing but forwarding. **Remove
`implements IItemExtraProperties` and the four forwarding methods wherever you find them.** Do not
edit AssortedLib to do it.

## Item registration

Items must know their id before construction. Follow `AssortedCore`'s `CoreItems`:

```java
private static <T extends Item> IRegistryObject<T> register(final String name, final Function<Item.Properties, ? extends T> factory) {
    final ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
    return ITEMS.register(name, () -> factory.apply(new Item.Properties().setId(key)));
}
```

The call sites change from `register("x", () -> new XItem(a, new Item.Properties().rarity(...)))` to
`register("x", props -> new XItem(a, props.rarity(...)))`.

## Enchantments are data now

`Enchantment` is a record built from JSON. There is no class to subclass and no
`EnchantmentCategory`. The six enchantment classes are being replaced with `ResourceKey<Enchantment>`
constants plus datagen. **`ToolsEnchantments`'s public helper methods keep their exact signatures**
(`getConductivity(ItemStack)`, `hasFlammable(ItemStack)`, `getInstability(ItemStack)`,
`getMaxBounces(ItemStack)`, `hasCoralCutter(ItemStack)`), so if you are not the enchantment agent,
leave every call site alone — they will keep compiling.

## Recording your work

Append behaviour changes and dead code to `../REVIEW-BEHAVIOUR-CHANGES.md` under a new
`## AssortedTools` heading (create it once; if it exists, add to it). Mark anything you could not
resolve with a `// TODO(26.2): ...` comment at the site.
