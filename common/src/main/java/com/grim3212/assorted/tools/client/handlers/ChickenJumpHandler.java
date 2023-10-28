package com.grim3212.assorted.tools.client.handlers;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.tools.ToolsCommonMod;
import com.grim3212.assorted.tools.common.enchantment.ToolsEnchantments;
import com.grim3212.assorted.tools.common.item.ChickenSuitArmor;
import com.grim3212.assorted.tools.common.network.ChickenSuitUpdatePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

public class ChickenJumpHandler {

    // Should be client side only so each client should have own instance of numJumps
    private static int numJumps;
    private static int cooldown = 0;
    private static final int MAX_COOLDOWN = 6;

    public static void tick(Minecraft mc) {
        if (ToolsCommonMod.COMMON_CONFIG.chickenSuitEnabled.get()) {
            Screen screen = Minecraft.getInstance().screen;
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


        if (!mc.player.isInWater() && !mc.player.isInLava() && mc.player.hasImpulse) {
            int jumpsAllowed = getMaxJumps(mc.player);

            // Must at least have 1 piece of the suit
            if (jumpsAllowed > 1) {
                boolean jumpPressed = mc.options.keyJump.isDown();

                if (jumpPressed && cooldown == 0 && numJumps < jumpsAllowed) {
                    if (numJumps > 0) {
                        mc.player.jumpFromGround();
                        mc.player.fallDistance = 0.0f;

                        // Only play sound to client player
                        mc.level.playSound(mc.player, mc.player.blockPosition(), SoundEvents.CHICKEN_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.0F);

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
                            mc.level.playSound(mc.player, mc.player.blockPosition(), SoundEvents.CHICKEN_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.0F);

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
        for (ItemStack stack : player.getArmorSlots()) {
            if (stack.isEmpty())
                continue;

            if (stack.getItem() instanceof ChickenSuitArmor) {
                maxJumps++;
            } else if (EnchantmentHelper.getEnchantments(stack).containsKey(ToolsEnchantments.CHICKEN_JUMP.get())) {
                maxJumps++;
            }
        }

        return maxJumps;
    }

}
