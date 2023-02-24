package com.grim3212.assorted.decor.services;

import com.grim3212.assorted.decor.Constants;
import com.grim3212.assorted.lib.config.FabricConfigUtil;
import io.github.fablabsmc.fablabs.api.fiber.v1.builder.ConfigTreeBuilder;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class FabricConfigHelper implements IConfigHelper {

    public static void setup() {
        FabricConfigUtil.setup(Constants.MOD_ID, COMMON, CLIENT);
    }

    private static class Common implements FabricConfigUtil.IConfig {
        public final PropertyMirror<Boolean> partColorizerEnabled = PropertyMirror.create(ConfigTypes.BOOLEAN);
        public final PropertyMirror<Boolean> partNeonSignEnabled = PropertyMirror.create(ConfigTypes.BOOLEAN);
        public final PropertyMirror<Boolean> partHangeablesEnabled = PropertyMirror.create(ConfigTypes.BOOLEAN);
        public final PropertyMirror<Boolean> partFluroEnabled = PropertyMirror.create(ConfigTypes.BOOLEAN);
        public final PropertyMirror<Boolean> partRoadwaysEnabled = PropertyMirror.create(ConfigTypes.BOOLEAN);
        public final PropertyMirror<Boolean> partPaintingEnabled = PropertyMirror.create(ConfigTypes.BOOLEAN);
        public final PropertyMirror<Boolean> partDecorationsEnabled = PropertyMirror.create(ConfigTypes.BOOLEAN);
        public final PropertyMirror<Boolean> partCageEnabled = PropertyMirror.create(ConfigTypes.BOOLEAN);
        public final PropertyMirror<Boolean> partPlanterPotEnabled = PropertyMirror.create(ConfigTypes.BOOLEAN);
        public final PropertyMirror<Boolean> partExtrasEnabled = PropertyMirror.create(ConfigTypes.BOOLEAN);


        public final PropertyMirror<Boolean> colorizerConsumeBlock = PropertyMirror.create(ConfigTypes.BOOLEAN);
        public final PropertyMirror<Integer> shapeSmoothness = PropertyMirror.create(ConfigTypes.NATURAL.withMinimum(1).withMaximum(10));
        public final PropertyMirror<Integer> colorizerBrushCount = PropertyMirror.create(ConfigTypes.NATURAL.withMinimum(1).withMaximum(400));
        public final PropertyMirror<Boolean> framesBurn = PropertyMirror.create(ConfigTypes.BOOLEAN);
        public final PropertyMirror<Boolean> dyeFrames = PropertyMirror.create(ConfigTypes.BOOLEAN);
        public final PropertyMirror<Boolean> wallpapersBurn = PropertyMirror.create(ConfigTypes.BOOLEAN);
        public final PropertyMirror<Boolean> dyeWallpapers = PropertyMirror.create(ConfigTypes.BOOLEAN);
        public final PropertyMirror<Boolean> wallpapersCopyDye = PropertyMirror.create(ConfigTypes.BOOLEAN);
        public final PropertyMirror<Integer> numWallpaperOptions = PropertyMirror.create(ConfigTypes.NATURAL.withMinimum(1).withMinimum(256));

        @Override
        public ConfigTree configure(ConfigTreeBuilder builder) {
            builder.fork("Parts")
                    .beginValue("partColorizerEnabled", ConfigTypes.BOOLEAN, true)
                    .withComment("Set this to true if Colorizer blocks and items should be craftable and visible in the creative tab")
                    .finishValue(partColorizerEnabled::mirror)
                    .beginValue("partNeonSignEnabled", ConfigTypes.BOOLEAN, true)
                    .withComment("Set this to true if Neon Signs should be craftable and visible in the creative tab")
                    .finishValue(partNeonSignEnabled::mirror)
                    .beginValue("partHangeablesEnabled", ConfigTypes.BOOLEAN, true)
                    .withComment("Set this to true if the Hangeable blocks and items should be craftable and visible in the creative tab")
                    .finishValue(partHangeablesEnabled::mirror)
                    .beginValue("partFluroEnabled", ConfigTypes.BOOLEAN, true)
                    .withComment("Set this to true if the Fluro blocks should be craftable and visible in the creative tab")
                    .finishValue(partFluroEnabled::mirror)
                    .beginValue("partRoadwaysEnabled", ConfigTypes.BOOLEAN, true)
                    .withComment("Set this to true if Roadway blocks and items should be craftable and visible in the creative tab")
                    .finishValue(partRoadwaysEnabled::mirror)
                    .beginValue("partPaintingEnabled", ConfigTypes.BOOLEAN, true)
                    .withComment("Set this to true if Painting rollers and siding should be craftable and visible in the creative tab")
                    .finishValue(partPaintingEnabled::mirror)
                    .beginValue("partDecorationsEnabled", ConfigTypes.BOOLEAN, true)
                    .withComment("Set this to true if the decoration blocks should be craftable and visible in the creative tab")
                    .finishValue(partDecorationsEnabled::mirror)
                    .beginValue("partCageEnabled", ConfigTypes.BOOLEAN, true)
                    .withComment("Set this to true if the Cage should be craftable and visible in the creative tab")
                    .finishValue(partCageEnabled::mirror)
                    .beginValue("partPlanterPotEnabled", ConfigTypes.BOOLEAN, true)
                    .withComment("Set this to true if the Planter pot should be craftable and visible in the creative tab")
                    .finishValue(partPlanterPotEnabled::mirror)
                    .beginValue("partExtrasEnabled", ConfigTypes.BOOLEAN, true)
                    .withComment("Set this to true if the extras (chain link fence and new doors) should be craftable and visible in the creative tab")
                    .finishValue(partExtrasEnabled::mirror)
                    .finishBranch();

            builder.fork("Colorizer")
                    .beginValue("colorizerConsumeBlock", ConfigTypes.BOOLEAN, true)
                    .withComment("Set this to true if the colorizer brush should consume a block when using them")
                    .finishValue(colorizerConsumeBlock::mirror)
                    .beginValue("shapeSmoothness", ConfigTypes.NATURAL, 2)
                    .withComment("Set this to determine how smooth all of the different slopes collision boxes should be.")
                    .finishValue(shapeSmoothness::mirror)
                    .beginValue("colorizerBrushCount", ConfigTypes.NATURAL, 16)
                    .withComment("Set this to the amount of blocks that a brush will be able to colorize after grabbing a block.")
                    .finishValue(colorizerBrushCount::mirror)
                    .finishBranch();

            builder.fork("Wallpaper")
                    .beginValue("dyeWallpapers", ConfigTypes.BOOLEAN, true)
                    .withComment("Set this to true if you want to be able to dye wallpaper.")
                    .finishValue(dyeWallpapers::mirror)
                    .beginValue("wallpapersBurn", ConfigTypes.BOOLEAN, true)
                    .withComment("Set this to true if you want wallpaper to be able to get burnt.")
                    .finishValue(wallpapersBurn::mirror)
                    .beginValue("wallpapersCopyDye", ConfigTypes.BOOLEAN, true)
                    .withComment("Set this to true if you want wallpaper to be able to copy dye colors from adjacent wallpaper.")
                    .finishValue(wallpapersCopyDye::mirror)
                    .beginValue("numWallpaperOptions", ConfigTypes.NATURAL, 24)
                    .withComment("Set this to the amount of wallpapers currently defined on the texture.")
                    .finishValue(numWallpaperOptions::mirror)
                    .finishBranch();

            builder.fork("Frames")
                    .beginValue("dyeFrames", ConfigTypes.BOOLEAN, true)
                    .withComment("Set this to true if you want to be able to dye Frames.")
                    .finishValue(dyeFrames::mirror)
                    .beginValue("framesBurn", ConfigTypes.BOOLEAN, true)
                    .withComment("Set this to true if you want frames to be able to get burnt.")
                    .finishValue(framesBurn::mirror)
                    .finishBranch();

            return builder.build();
        }
    }

    private static final Common COMMON = new Common();


    @Override
    public int getShapeSmoothness() {
        return COMMON.shapeSmoothness.getValue();
    }

    @Override
    public boolean doesColorizerConsumeBlock() {
        return COMMON.colorizerConsumeBlock.getValue();
    }

    @Override
    public int colorizerBrushChargeCount() {
        return COMMON.colorizerBrushCount.getValue();
    }

    @Override
    public boolean canFramesBurn() {
        return COMMON.framesBurn.getValue();
    }

    @Override
    public boolean canDyeFrames() {
        return COMMON.dyeFrames.getValue();
    }

    @Override
    public boolean canDyeWallpapers() {
        return COMMON.dyeWallpapers.getValue();
    }

    @Override
    public boolean canWallpapersBurn() {
        return COMMON.wallpapersBurn.getValue();
    }

    @Override
    public boolean doWallpapersCopyDye() {
        return COMMON.wallpapersCopyDye.getValue();
    }

    @Override
    public int numWallpaperOptions() {
        return COMMON.numWallpaperOptions.getValue();
    }

    @Override
    public boolean partColorizerEnabled() {
        return COMMON.partColorizerEnabled.getValue();
    }

    @Override
    public boolean partNeonSignEnabled() {
        return COMMON.partNeonSignEnabled.getValue();
    }

    @Override
    public boolean partHangeablesEnabled() {
        return COMMON.partHangeablesEnabled.getValue();
    }

    @Override
    public boolean partFluroEnabled() {
        return COMMON.partFluroEnabled.getValue();
    }

    @Override
    public boolean partRoadwaysEnabled() {
        return COMMON.partRoadwaysEnabled.getValue();
    }

    @Override
    public boolean partPaintingEnabled() {
        return COMMON.partPaintingEnabled.getValue();
    }

    @Override
    public boolean partDecorationsEnabled() {
        return COMMON.partDecorationsEnabled.getValue();
    }

    @Override
    public boolean partCageEnabled() {
        return COMMON.partCageEnabled.getValue();
    }

    @Override
    public boolean partPlanterPotEnabled() {
        return COMMON.partPlanterPotEnabled.getValue();
    }

    @Override
    public boolean partExtrasEnabled() {
        return COMMON.partExtrasEnabled.getValue();
    }

    // Client side options
    @Override
    public float cageSpinModifier() {
        return CLIENT.cageSpinMod.getValue();
    }

    @Override
    public float wallpaperWidth() {
        return CLIENT.wallpaperWidth.getValue();
    }


    private static class Client implements FabricConfigUtil.IConfig {
        public final PropertyMirror<Float> cageSpinMod = PropertyMirror.create(ConfigTypes.FLOAT.withMinimum(1.0F).withMaximum(20.0F));
        public final PropertyMirror<Float> wallpaperWidth = PropertyMirror.create(ConfigTypes.FLOAT.withMinimum(0.1F).withMaximum(5.0F));

        @Override
        public ConfigTree configure(ConfigTreeBuilder builder) {
            builder.fork("Wallpaper")
                    .beginValue("wallpaperWidth", ConfigTypes.FLOAT, 1.0F)
                    .withComment("Set this to determine how much the wallpaper will stick off of the wall.")
                    .finishValue(wallpaperWidth::mirror)
                    .finishBranch();

            builder.fork("Cage")
                    .beginValue("cageSpinMod", ConfigTypes.FLOAT, 3.0F)
                    .withComment("This is the modifier for how fast entities spin when displayed inside the Cage.")
                    .finishValue(cageSpinMod::mirror)
                    .finishBranch();

            return builder.build();
        }
    }

    private static final Client CLIENT = new Client();
}
