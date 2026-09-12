package com.grim3212.assorted.tools.gametest;

import com.grim3212.assorted.lib.core.fluid.FluidInformation;
import com.grim3212.assorted.lib.core.fluid.IFluidVariantHandler;
import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.util.NBTHelper;
import com.grim3212.assorted.tools.common.item.CapturedEntity;
import com.grim3212.assorted.tools.common.item.FragmentItem;
import com.grim3212.assorted.tools.common.item.ToolsDataComponents;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.List;
import java.util.Optional;

/**
 * The component tooltips (pokeball, fragment, wand, bucket) as Fabric builds them, which only
 * happens on the client; the server gametests cover NeoForge. Run with {@code ./gradlew :fabric:runClientGameTest}; it exits non-zero on a
 * failure.
 */
public class ToolsClientGameTests implements FabricClientGameTest {

    @Override
    public void runTest(ClientGameTestContext context) {
        // Inside a world: an ItemStack cannot be made on the title screen, because an item's default
        // components are only bound once a world's registries have loaded.
        try (TestSingleplayerContext world = context.worldBuilder().create()) {
            context.runOnClient(client -> {
                List<String> empty = tooltipKeys(client, new ItemStack(ToolsItems.POKEBALL.get()));
                if (!empty.contains("tooltip.pokeball.empty")) {
                    throw new AssertionError("an empty pokeball's tooltip is " + empty);
                }

                CompoundTag cow = new CompoundTag();
                cow.putString("id", "minecraft:cow");
                cow.putString("pokeball_name", "entity.minecraft.cow");
                ItemStack full = new ItemStack(ToolsItems.POKEBALL.get());
                full.set(ToolsDataComponents.CAPTURED_ENTITY.get(), new CapturedEntity(cow));
                List<String> stored = tooltipKeys(client, full);
                if (!stored.contains("tooltip.pokeball.stored")) {
                    throw new AssertionError("a full pokeball's tooltip is " + stored);
                }

                List<String> fragment = tooltipKeys(client, new ItemStack(ToolsItems.U_FRAGMENT.get()));
                if (!fragment.contains(FragmentItem.DESCRIPTION_KEY)) {
                    throw new AssertionError("a fragment's tooltip is " + fragment);
                }

                ItemStack wandStack = new ItemStack(ToolsItems.MINING_WAND.get());
                NBTHelper.putString(wandStack, "Mode", "mineall");
                List<String> wand = tooltipKeys(client, wandStack);
                if (!wand.contains("assortedtools.wand.current")) {
                    throw new AssertionError("a mining wand's tooltip is " + wand);
                }

                List<String> bucket = tooltipKeys(client, ToolsItems.WOOD_BUCKET.get().getEmptyStack());
                if (!bucket.contains("tooltip.buckets.empty")) {
                    throw new AssertionError("an empty bucket's tooltip is " + bucket);
                }

                assertFluidTextures(Fluids.WATER);
                assertFluidTextures(Fluids.LAVA);
            });
        }
    }

    /**
     * A fluid's still and flowing textures, asked for both ways AssortedLib can be asked: through the
     * client fluid helper, and through the fluid variant handler a foreign fluid gets. On Fabric the
     * transfer API stopped handing back sprites, and the handler used to answer nothing at all, which
     * is what draws a bucket empty; both now read the fluid's baked model, so both must agree and
     * neither may be the missing texture. This is the client half of what keeps water and lava
     * showing in a filled bucket.
     */
    private static void assertFluidTextures(Fluid fluid) {
        FluidInformation contents = new FluidInformation(fluid);

        Identifier still = ClientServices.FLUIDS.getStillFluidTexture(fluid);
        Identifier flowing = ClientServices.FLUIDS.getFlowingFluidTexture(fluid);
        if (still.equals(MissingTextureAtlasSprite.getLocation()) || flowing.equals(MissingTextureAtlasSprite.getLocation())) {
            throw new AssertionError(fluid + " resolved to the missing texture: still " + still + ", flowing " + flowing);
        }

        IFluidVariantHandler handler = Services.FLUIDS.getVariantHandlerFor(contents)
                .orElseThrow(() -> new AssertionError("no fluid variant handler for " + fluid));

        Optional<Identifier> handlerStill = handler.getStillTexture(contents);
        Optional<Identifier> handlerFlowing = handler.getFlowingTexture(contents);
        if (!handlerStill.equals(Optional.of(still)) || !handlerFlowing.equals(Optional.of(flowing))) {
            throw new AssertionError("the variant handler for " + fluid + " answered still " + handlerStill
                    + " and flowing " + handlerFlowing + ", but the client helper says " + still + " and " + flowing);
        }
    }

    private static List<String> tooltipKeys(Minecraft client, ItemStack stack) {
        return stack.getTooltipLines(Item.TooltipContext.of(client.level), client.player, TooltipFlag.NORMAL).stream()
                .map(line -> line.getContents() instanceof TranslatableContents translatable ? translatable.getKey() : line.getString())
                .toList();
    }
}
