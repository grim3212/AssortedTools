package com.grim3212.assorted.tools.client.handlers;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.tools.ToolsCommonMod;
import com.grim3212.assorted.tools.common.enchantment.ToolsEnchantments;
import com.grim3212.assorted.tools.common.item.ChickenSuitArmor;
import com.grim3212.assorted.tools.common.network.ChickenSuitUpdatePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.animal.chicken.ChickenSoundVariants;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ChickenJumpHandler {

    // Should be client side only so each client should have own instance of numJumps
    private static int numJumps;
    private static int cooldown = 0;
    private static final int MAX_COOLDOWN = 6;

    /**
     * Chickens pick their sounds from a data driven {@code ChickenSoundVariant} now, so there is no
     * single {@code SoundEvents.CHICKEN_AMBIENT} constant any more. The classic variant's adult
     * ambient sound is {@code entity.chicken.ambient}, which is what 1.20.1 played here.
     */
    private static final Holder<SoundEvent> CHICKEN_AMBIENT = SoundEvents.CHICKEN_SOUNDS.get(ChickenSoundVariants.SoundSet.CLASSIC).adultSounds().ambientSound();

    public static void tick(Minecraft mc) {
        if (ToolsCommonMod.COMMON_CONFIG.chickenSuitEnabled.get()) {
            Screen screen = mc.gui.screen();
            if (screen == null) {
                onTickInGame(mc);
            }
        }
    }

    private static void onTickInGame(Minecraft mc) {
        if (cooldown > 0) {
            --cooldown;
        }

        if (mc.player.onGround()) {
            numJumps = 0;
        }


        if (!mc.player.isInWater() && !mc.player.isInLava() && mc.player.needsSync) {
            int jumpsAllowed = getMaxJumps(mc.player);

            // Must at least have 1 piece of the suit
            if (jumpsAllowed > 1) {
                boolean jumpPressed = mc.options.keyJump.isDown();

                if (jumpPressed && cooldown == 0 && numJumps < jumpsAllowed) {
                    if (numJumps > 0) {
                        mc.player.jumpFromGround();
                        mc.player.fallDistance = 0.0f;

                        // Only play sound to client player
                        mc.level.playSound(mc.player, mc.player.blockPosition(), CHICKEN_AMBIENT.value(), SoundSource.PLAYERS, 1.0F, 1.0F);

                        // Double jump on server
                        Services.NETWORK.sendToServer(new ChickenSuitUpdatePacket());
                    } else {
                        // Allow for resetting fall damage when falling
                        // 'Flap those wings' :)
                        if (mc.player.fallDistance > 0.1f) {
                            // If we jumped while in mid-air we still only get the amount of jumps equal to armor pieces with the enchant
                            numJumps++;

                            mc.player.jumpFromGround();
                            mc.player.fallDistance = -numJumps;

                            // Only play sound to client player
                            mc.level.playSound(mc.player, mc.player.blockPosition(), CHICKEN_AMBIENT.value(), SoundSource.PLAYERS, 1.0F, 1.0F);

                            // Double jump on server
                            Services.NETWORK.sendToServer(new ChickenSuitUpdatePacket());
                        }
                    }

                    numJumps++;
                    cooldown = MAX_COOLDOWN;
                }

                // Handle the glide
                Vec3 mot = mc.player.getDeltaMovement();
                if (!mc.options.keyShift.isDown() && mot.y < 0.0D) {
                    double d = -0.14999999999999999D - 0.14999999999999999D * (1.0D - (double) numJumps / 5D);
                    if (mot.y < d) {
                        mc.player.setDeltaMovement(mot.x, d, mot.z);
                    }
                    mc.player.fallDistance = 0.0F;

                    // Glide on server
                    Services.NETWORK.sendToServer(new ChickenSuitUpdatePacket(numJumps));
                }

            }
        }
    }

    private static int getMaxJumps(Player player) {
        // Start at one for original jump
        int maxJumps = 1;
        // getArmorSlots() is gone; the humanoid armour slots are walked through EquipmentSlotGroup.
        for (EquipmentSlot slot : EquipmentSlotGroup.ARMOR) {
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR)
                continue;

            ItemStack stack = player.getItemBySlot(slot);
            if (stack.isEmpty())
                continue;

            if (stack.getItem() instanceof ChickenSuitArmor) {
                maxJumps++;
            } else if (ToolsEnchantments.hasChickenJump(stack)) {
                maxJumps++;
            }
        }

        return maxJumps;
    }

}
