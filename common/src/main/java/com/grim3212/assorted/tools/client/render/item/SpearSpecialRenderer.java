package com.grim3212.assorted.tools.client.render.item;

import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.client.render.model.SpearModel;
import com.grim3212.assorted.tools.client.render.model.ToolsModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import org.joml.Vector3fc;

import java.util.function.Consumer;

/**
 * Draws a spear held in hand, as vanilla's {@code TridentSpecialRenderer} draws the trident. The
 * item model json picks the flat {@code <material>_spear_gui} model for the GUI, ground and fixed
 * contexts, swaps to the throwing model on {@code minecraft:using_item}, and supplies the
 * {@code scale(1, -1, -1)} as its {@code transformation}; none of that is done here.
 */
public class SpearSpecialRenderer implements NoDataSpecialModelRenderer {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "spear");

    private final SpearModel model;
    private final Identifier texture;

    public SpearSpecialRenderer(SpearModel model, Identifier texture) {
        this.model = model;
        this.texture = texture;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        submitNodeCollector.order(0).submitModel(this.model, Unit.INSTANCE, poseStack, this.texture, lightCoords, overlayCoords, outlineColor, null);
        if (hasFoil) {
            submitNodeCollector.order(1).submitModel(this.model, Unit.INSTANCE, poseStack, RenderTypes.entityGlint(), lightCoords, overlayCoords, outlineColor, null);
        }
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        PoseStack poseStack = new PoseStack();
        this.model.root().getExtentsForGui(poseStack, output);
    }

    /**
     * The material's texture is the only thing that varies between the spears, so it is a field of
     * the unbaked renderer rather than something resolved from the stack - the renderer no longer
     * gets to see the {@code ItemStack} it is drawing beyond {@code extractArgument}.
     */
    public record Unbaked(Identifier texture) implements NoDataSpecialModelRenderer.Unbaked {
        public static final MapCodec<SpearSpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
                i -> i.group(
                        Identifier.CODEC.fieldOf("texture").forGetter(SpearSpecialRenderer.Unbaked::texture)
                ).apply(i, SpearSpecialRenderer.Unbaked::new)
        );

        @Override
        public MapCodec<SpearSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpearSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
            return new SpearSpecialRenderer(new SpearModel(context.entityModelSet().bakeLayer(ToolsModelLayers.SPEAR)), this.texture);
        }
    }
}
