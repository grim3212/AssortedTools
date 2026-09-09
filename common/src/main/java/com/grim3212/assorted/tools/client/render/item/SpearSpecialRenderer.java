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
 * Draws a spear held in hand, replacing {@code SpearBEWLR}.
 * <p>
 * {@code BlockEntityWithoutLevelRenderer} is gone in 26.2 and with it the ability to attach an item
 * renderer to an {@code Item} from code. An item opts in from its own model json, which names a
 * {@code minecraft:special} renderer by id, and the only thing registered in code is the id to
 * {@link MapCodec} pair - see {@code ToolsClient}. This is exactly how vanilla's trident is drawn
 * ({@code TridentSpecialRenderer}); the spear is the same shape of item, so it is modelled on it.
 * <p>
 * Two things the old renderer did in code are now the model json's job and are <b>not</b> handled
 * here:
 * <ul>
 * <li>the flat inventory sprite for the GUI, GROUND and FIXED display contexts, which
 * {@code SpearBEWLR#renderByItem} used to branch to - a {@code minecraft:select} on
 * {@code minecraft:display_context} picks the {@code <material>_spear_gui} model for those cases
 * and the {@code minecraft:special} entry for the rest;</li>
 * <li>the {@code assortedtools:throwing} item property that swapped in the
 * {@code <material>_spear_throwing} model while the item was being used, which is a
 * {@code minecraft:condition} on {@code minecraft:using_item}.</li>
 * </ul>
 * The {@code scale(1, -1, -1)} the old renderer applied before drawing is likewise the model json's
 * {@code transformation}, as it is for the trident.
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
