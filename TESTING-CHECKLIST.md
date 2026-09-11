# AssortedTools — in-world testing checklist

Manual checks only. Anything automatable lives in `common/src/gametest/java/.../gametest/`
and runs with `./gradlew :neoforge:runGameTestServer` / `:fabric:runGameTest`.

Run each list on **both** NeoForge and Fabric.

## Tools and armour
- [ ] Every material's armour set shows its texture on the player
- [ ] Hammers mine a 3x3 area
- [ ] Ultimate fist shows its enchant glint
- [ ] Multitool strips logs, scrapes and de-waxes copper, makes paths and tills
- [ ] Material shears shear (Fabric in particular)

## Thrown items
- [ ] Spear renders in hand and in the ground
- [ ] Pokeball shows the captured mob in its tooltip
- [ ] A thrown boomerang renders in flight

## Buckets
- [ ] Bucket tooltip shows how much fluid it holds
- [ ] Bucket model changes with what it holds
- [ ] A filled bucket looks right in the GUI, including fluids from other mods

## Wands
- [ ] Reinforced wands' extra modes: build water, build lava, build caves, mine ores
- [ ] The mode-switch keybind cycles modes for a wand in either hand, and the key is listed under
      the mod's own category in Controls

## Enchantments
- [ ] Bounciness, chicken jump, conductive, coral cutter, flammable and unstable all appear in an
      enchanting table / anvil on the right items, and each does what it says
- [ ] Chicken suit lets the wearer glide
- [ ] Chicken suit double jump works and plays the classic chicken sound
- [ ] What the enchanting table offers for each tool looks sane compared with 1.20.1
