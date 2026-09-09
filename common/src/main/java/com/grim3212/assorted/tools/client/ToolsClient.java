package com.grim3212.assorted.tools.client;

import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.client.color.FluidContainerTintSource;
import com.grim3212.assorted.tools.client.handlers.ChickenJumpHandler;
import com.grim3212.assorted.tools.client.handlers.KeyBindHandler;
import com.grim3212.assorted.tools.client.model.fluidcontainer.FluidContainerModel;
import com.grim3212.assorted.tools.client.render.entity.BetterSpearRenderer;
import com.grim3212.assorted.tools.client.render.entity.BoomerangRenderer;
import com.grim3212.assorted.tools.client.render.item.SpearSpecialRenderer;
import com.grim3212.assorted.tools.client.render.model.SpearModel;
import com.grim3212.assorted.tools.client.render.model.ToolsModelLayers;
import com.grim3212.assorted.tools.common.entity.ToolsEntities;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class ToolsClient {

    public static KeyMapping TOOL_SWITCH_MODES;

    public static void init() {
        // The group string becomes a registered KeyMapping.Category id, so the label this key sorts
        // under is "key.category.assortedlib.assorted_tools" rather than the old free-form group key.
        TOOL_SWITCH_MODES = ClientServices.KEYBINDS.createNew("key.assortedtools.tool_switch_modes", ClientServices.KEYBINDS.getInGameKeyConflictContext(), InputConstants.Type.KEYSYM, InputConstants.KEY_Z, Constants.MOD_NAME);
        ClientServices.CLIENT.registerKeyMapping(TOOL_SWITCH_MODES);

        ClientServices.CLIENT.registerClientTickStart(KeyBindHandler::tick);
        ClientServices.CLIENT.registerClientTickEnd(ChickenJumpHandler::tick);

        ClientServices.CLIENT.registerEntityLayer(ToolsModelLayers.SPEAR, SpearModel::createLayer);

        // BlockEntityWithoutLevelRenderer is gone: a special item renderer is selected by the item's
        // own model json ("minecraft:special" naming this id), so code only registers the id to codec
        // pair. The spear renderer used to be registered against each spear item here, and to be a
        // reload listener so it could look its inventory models up again; neither is possible or
        // needed now.
        // TODO(26.2): nothing selects this yet. The generated spear item models are still in 1.20.1
        //  shape - a "models/item/<mat>_spear.json" with an "overrides" list - and there is no
        //  "assets/assortedtools/items/<mat>_spear.json". Until those are regenerated as a
        //  "minecraft:select" on "minecraft:display_context" (gui/ground/fixed -> the flat
        //  <mat>_spear_gui model) whose fallback is a "minecraft:condition" on "minecraft:using_item"
        //  over two "minecraft:special" entries naming assortedtools:spear with the material's
        //  texture, spears do not render in hand at all. See SpearSpecialRenderer.
        ClientServices.CLIENT.registerBEWLR((register) -> {
            register.registerSpecialModelRenderer(SpearSpecialRenderer.ID, SpearSpecialRenderer.Unbaked.MAP_CODEC);
        });

        // TODO(26.2): registerItemProperty and registerAdditionalModel are both gone.
        //  The "assortedtools:throwing" ClampedItemPropertyFunction that swapped a spear to its
        //  _throwing model while the item was in use has no runtime equivalent - model selection by a
        //  property is data driven through client.renderer.item.properties.** referenced from the
        //  item model json, and the direct replacement for this particular predicate is vanilla's own
        //  "minecraft:using_item" conditional property.
        //  The <mat>_spear_gui models no longer need to be force-loaded either: they were extra
        //  models only because SpearBEWLR fetched them from the ModelManager by name at runtime, and
        //  an item model referenced from an item's json is loaded because it is referenced.

        ClientServices.CLIENT.registerEntityRenderer(() -> ToolsEntities.WOOD_BOOMERANG.get(), BoomerangRenderer::new);
        ClientServices.CLIENT.registerEntityRenderer(() -> ToolsEntities.DIAMOND_BOOMERANG.get(), BoomerangRenderer::new);
        ClientServices.CLIENT.registerEntityRenderer(() -> ToolsEntities.POKEBALL.get(), ThrownItemRenderer::new);
        ClientServices.CLIENT.registerEntityRenderer(() -> ToolsEntities.BETTER_SPEAR.get(), BetterSpearRenderer::new);

        // An item's tints live in its model json now; all that is registered from code is the source
        // type. The bucket models need a "tints" entry naming this id at the fluid layer's index,
        // which is what the old registerItemColor's "tintIndex != 1" check stood in for.
        ClientServices.CLIENT.registerItemTintSource(FluidContainerTintSource.ID, FluidContainerTintSource.MAP_CODEC);

        ClientServices.CLIENT.registerModelLoader(FluidContainerModel.LOADER_NAME, FluidContainerModel.Loader.INSTANCE);
    }

}
