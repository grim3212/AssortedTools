# AssortedTools — in-world testing checklist

Manual checks only. Anything automatable lives in `common/src/gametest/java/.../gametest/`
and runs with `./gradlew :neoforge:runGameTestServer` / `:fabric:runGameTest`.

Run each list on **both** NeoForge and Fabric.

## Tools and armour
- [ ] Every material's armour set shows its texture on the player
- [ ] Hammers mine a 3x3 area
- [ ] Ultimate fist shows its enchant glint

## Thrown items
- [ ] Spear renders in hand and in the ground
- [ ] Pokeball shows the captured mob in its tooltip

## Buckets
- [ ] Bucket tooltip shows how much fluid it holds
- [ ] Bucket model changes with what it holds

## Wands
- [ ] Reinforced wands' extra modes: build water, build lava, build caves, mine ores
- [ ] The mode-switch keybind cycles modes for a wand in either hand, and the key is listed under
      the mod's own category in Controls

## Enchantments
- [ ] Bounciness, chicken jump, conductive, coral cutter, flammable and unstable all appear in an
      enchanting table / anvil on the right items, and each does what it says
- [ ] Chicken suit lets the wearer glide
