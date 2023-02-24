package com.grim3212.assorted.decor.services;

public interface IConfigHelper {

    int getShapeSmoothness();

    boolean doesColorizerConsumeBlock();

    int colorizerBrushChargeCount();

    boolean canFramesBurn();

    boolean canDyeFrames();

    boolean canDyeWallpapers();

    boolean canWallpapersBurn();

    boolean doWallpapersCopyDye();

    int numWallpaperOptions();

    boolean partColorizerEnabled();

    boolean partNeonSignEnabled();

    boolean partHangeablesEnabled(); // includes wallpaper and frames

    boolean partFluroEnabled();

    boolean partRoadwaysEnabled();

    boolean partPaintingEnabled();

    boolean partDecorationsEnabled();

    boolean partCageEnabled();

    boolean partPlanterPotEnabled();

    boolean partExtrasEnabled();

    // Client configs
    float cageSpinModifier();

    float wallpaperWidth();
}
