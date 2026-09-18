# Changelog

## 11.1.2

Brought over from GrimPack:

- Machetes, one for every material: wood through netherite and each of the extra materials. A
  machete is a lighter, quicker sword that cuts leaves, vines, wool, cactus and other undergrowth
  at its material's speed. What it cuts is `#assortedtools:mineable/machete`.
- The portable workbench, a crafting table you carry. It is listed beside the crafting table in
  JEI.
- The Neptune and Phoenix staffs. The Neptune staff places water, freezes the mobs around you or
  freezes the still water around you, and freezes what it hits. The Phoenix staff places lava or
  fire, thaws the mobs a Neptune staff froze, or melts the ice around you, and thaws what it hits.
  A frozen mob stands still and silent, turned to ice as it was in GrimPack, and stays that way
  until a Phoenix staff thaws it or it catches fire.
- The power staff, which pushes a block away from the face you click or pulls it toward you, and
  either leaves it floating or lets it fall. It moves what a piston could; packs can refuse more
  blocks through `#assortedtools:power_staff_immovable`.
- The staffs switch modes with the same key as the wands, Z by default.
- The Neptune staff is made from a diamond, an ice charge and a frost rod, and the Phoenix staff
  from a diamond, a fire charge and a blaze rod.
- Frost rods, the blaze rod's cold counterpart. Strays drop them as blazes drop theirs, and any
  other monster killed by a player in a snowy (`#c:is_snowy`) biome sometimes does. A rod grinds
  into two frost powder, and frost powder, gunpowder and a snowball make three ice charges.

## 11.1.1

- Fix ChickenSuit JEI implementation
- Support Instruction Manual
- The mining wand's dirt mode digs podzol, mycelium, moss and mud again.
- Requires Assorted Lib 4.1.0.

## 11.0.1

- Better buckets now respect their tier's `maxPickupTemp`, so a wooden bucket leaves lava alone
  whether it is clicked on, dispensed at, or filled from a tank.
- A wooden or stone bucket that breaks after placing a fluid leaves the plain material it was made
  of.

## 11.0.0

Updated to Minecraft 26.2, for NeoForge and Fabric.

- Requires Assorted Lib 4.0.0.
