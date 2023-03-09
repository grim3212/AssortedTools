package com.grim3212.assorted.decor.client.handlers;

import com.grim3212.assorted.decor.common.enchantment.ToolsEnchantments;
import com.grim3212.assorted.decor.common.item.ChickenSuitArmor;
import com.grim3212.assorted.decor.common.network.ChickenSuitUpdatePacket;
import com.grim3212.assorted.decor.config.ToolsConfig;
import com.grim3212.assorted.lib.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

public class ChickenJumpHandler {

    private int numJumps;

    public void tick() {
        if (ToolsConfig.Common.chickenSuitEnabled.getValue()) {
            Screen screen = Minecraft.getInstance().screen;
            if (screen == null) {
                onTickInGame();
            }
        }
    }

    public void onTickInGame() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player.isOnGround()) {
            numJumps = 0;
        }

        if (!mc.player.isInWater() && !mc.player.isInLava() && mc.player.hasImpulse) {
            int jumpsAllowed = getMaxJumps(mc.player);

            // Must at least have 1 piece of the suit
            if (jumpsAllowed > 1) {
                if (mc.options.keyJump.consumeClick() && numJumps < jumpsAllowed) {
                    // Do not perform any calculation on the initial jump
                    // This is handled by vanilla already
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
                        if (mc.player.fallDistance > 0.4f) {
                            mc.player.jumpFromGround();
                            mc.player.fallDistance = -this.numJumps;

                            // Only play sound to client player
                            mc.level.playSound(mc.player, mc.player.blockPosition(), SoundEvents.CHICKEN_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.0F);

                            // Double jump on server
                            Services.NETWORK.sendToServer(new ChickenSuitUpdatePacket());
                        }
                    }

                    numJumps++;
                }

                Vec3 mot = mc.player.getDeltaMovement();
                if (!mc.options.keyShift.isDown() && mot.y < 0.0D) {
                    double d = -0.14999999999999999D - 0.14999999999999999D * (1.0D - (double) numJumps / 5D);
                    if (mot.y < d) {
                        mc.player.setDeltaMovement(mot.x, d, mot.z);
                    }
                    mc.player.fallDistance = 0.0F;

                    // Glide on server
                    Services.NETWORK.sendToServer(new ChickenSuitUpdatePacket(this.numJumps));
                }

            } else {
                numJumps = 0;
            }
        } else {
            numJumps = 0;
        }
    }

    private int getMaxJumps(Player player) {
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
