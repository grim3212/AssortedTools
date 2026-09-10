# AssortedTools — in-world testing checklist

Manual checks only. Anything automatable lives in `common/src/main/java/.../gametest/`
and runs with `./gradlew :neoforge:runGameTestServer` / `:fabric:runGameTest`.

Run each list on **both** NeoForge and Fabric.

## Tools and armour
- [ ] Every material's sword, pickaxe, axe, shovel and hoe craft, mine at the right speed and
      take the right enchantments
- [ ] Every material's armour set crafts, equips and shows its texture on the player
- [ ] Hammers mine a 3x3 area
- [ ] Multitools work as pickaxe, axe and shovel
- [ ] Shears shear sheep, cut leaves and cut coral (coral cutter enchantment)
- [ ] Ultimate fist breaks anything instantly and shows its enchant glint

## Thrown items
- [ ] Spear throws, sticks in a block, damages a mob, and can be picked back up
- [ ] Spear renders in hand and in the ground
- [ ] Wood and diamond boomerangs fly out and return to the thrower
- [ ] Pokeball captures a mob, shows it in the tooltip, and releases it again

## Buckets
- [ ] Better buckets pick up and place water and lava
- [ ] Bucket holds more than one bucket of fluid, and the tooltip and name show the fluid
- [ ] Milk bucket milks a cow and can be drunk
- [ ] Bucket model changes with what it holds
- [ ] Dispenser places fluid from a better bucket

## Wands
- [ ] Building, mining and breaking wands each do their thing, and the reinforced versions too
- [ ] The mode-switch keybind cycles modes for a wand in either hand, and the key is listed under
      the mod's own category in Controls
- [ ] Building wand consumes blocks from the inventory and refuses when there are not enough

## Enchantments
- [ ] Bounciness, chicken jump, conductive, coral cutter, flammable and unstable all appear in an
      enchanting table / anvil on the right items, and each does what it says
- [ ] Chicken suit lets the wearer glide, and converts as expected

## Creative
- [ ] The Assorted Tools tab exists and every item in it has a model and a name
- [ ] The fragment items all render
