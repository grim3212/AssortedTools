package com.grim3212.assorted.tools.client;

import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.client.color.FluidContainerTintSource;
import com.grim3212.assorted.tools.client.handlers.ChickenJumpHandler;
import com.grim3212.assorted.tools.client.handlers.KeyBindHandler;
import com.grim3212.assorted.tools.client.model.fluidcontainer.FluidContainerItemModel;
import com.grim3212.assorted.tools.client.render.entity.BetterSpearRenderer;
import com.grim3212.assorted.tools.client.render.entity.BoomerangRenderer;
import com.grim3212.assorted.tools.client.render.item.SpearSpecialRenderer;
import com.grim3212.assorted.tools.client.render.model.SpearModel;
import com.grim3212.assorted.tools.client.render.model.ToolsModelLayers;
import com.grim3212.assorted.tools.common.entity.ToolsEntities;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.Identifier;

public class ToolsClient {

    // A key mapping's group is a KeyMapping.Category id whose label is derived from the id itself, so
    // this needs a "key.category.assortedtools.general" entry in the lang file.
    public static final Identifier KEY_CATEGORY = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "general");

    public static KeyMapping TOOL_SWITCH_MODES;

    public static void init() {
        TOOL_SWITCH_MODES = ClientServices.KEYBINDS.createNew("key.assortedtools.tool_switch_modes", ClientServices.KEYBINDS.getInGameKeyConflictContext(), InputConstants.Type.KEYSYM, InputConstants.KEY_Z, KEY_CATEGORY);
        ClientServices.CLIENT.registerKeyMapping(TOOL_SWITCH_MODES);

        ClientServices.CLIENT.registerClientTickStart(KeyBindHandler::tick);
        ClientServices.CLIENT.registerClientTickEnd(ChickenJumpHandler::tick);

        ClientServices.CLIENT.registerEntityLayer(ToolsModelLayers.SPEAR, SpearModel::createLayer);

        // BlockEntityWithoutLevelRenderer is gone: a special item renderer is selected by the item's
        // own model json ("minecraft:special" naming this id), so code only registers the id to codec
        // pair. The generated spear item models select this renderer via "minecraft:display_context"
        // (gui/ground/fixed -> the flat <mat>_spear_gui model), falling back to "minecraft:using_item"
        // over two "minecraft:special" entries naming assortedtools:spear with the material's texture
        // - the direct replacement for the old "assortedtools:throwing" ClampedItemPropertyFunction.
        // Verified in game: spears render in hand and the throwing swap still works.
        ClientServices.CLIENT.registerBEWLR((register) -> {
            register.registerSpecialModelRenderer(SpearSpecialRenderer.ID, SpearSpecialRenderer.Unbaked.MAP_CODEC);
        });

        ClientServices.CLIENT.registerEntityRenderer(() -> ToolsEntities.WOOD_BOOMERANG.get(), BoomerangRenderer::new);
        ClientServices.CLIENT.registerEntityRenderer(() -> ToolsEntities.DIAMOND_BOOMERANG.get(), BoomerangRenderer::new);
        ClientServices.CLIENT.registerEntityRenderer(() -> ToolsEntities.POKEBALL.get(), ThrownItemRenderer::new);
        ClientServices.CLIENT.registerEntityRenderer(() -> ToolsEntities.BETTER_SPEAR.get(), BetterSpearRenderer::new);

        // An item's tints live in its model now; all that is registered from code is the source type.
        // FluidContainerItemModel lists this source on its fluid layer directly, so the registration
        // is what lets a resource pack name it from json as well - it is not what the bucket needs.
        ClientServices.CLIENT.registerItemTintSource(FluidContainerTintSource.ID, FluidContainerTintSource.MAP_CODEC);

        // A bucket cannot be a model json loader: the fluid it draws is only knowable after baking
        // has finished, and a json loader has to hand back finished quads during it. See
        // FluidContainerItemModel.
        ClientServices.CLIENT.registerItemModelType(FluidContainerItemModel.ID, FluidContainerItemModel.Unbaked.MAP_CODEC);
    }

}
