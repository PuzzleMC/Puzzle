package net.puzzlemc.core.config;

import eu.midnightdust.lib.config.MidnightConfig;

import java.util.ArrayList;
import java.util.List;

public class PuzzleConfig extends MidnightConfig {
    private static final String FEATURES = "features";

    @Comment(name = "puzzle.midnightconfig.category.features", category = FEATURES, centered = true) public static String _c1;
    @Entry(category = FEATURES, name = "puzzle.option.resourcepack_splash_screen")
    public static boolean resourcepackSplashScreen = true;
    @Entry(category = FEATURES, name = "puzzle.option.bigger_custom_models")
    public static boolean biggerModels = true;
    //? if < 1.21.11 {
    /*@Entry(category = FEATURES, name = "puzzle.option.unlimited_model_rotations")
    public static boolean unlimitedRotations = true;
    *///?}

    @Comment(name = "puzzle.midnightconfig.category.internal", category = FEATURES, centered = true) public static String _c2;
    @Entry(category = FEATURES, name = "Show internal options")
    public static boolean advancedMode = false;
    @Condition(requiredOption = "advancedMode")
    @Entry(category = FEATURES, name = "Enable debug messages")
    public static boolean debugMessages = false;
    @Condition(requiredOption = "advancedMode")
    @Entry(category = FEATURES, name = "Has custom splash screen")
    public static boolean hasCustomSplashScreen = false;
    @Condition(requiredOption = "advancedMode")
    @Entry(category = FEATURES, name = "Splash Background Color")
    public static int backgroundColor = 15675965;
    @Condition(requiredOption = "advancedMode")
    @Entry(category = FEATURES, name = "Splash Progress Bar Color")
    public static int progressBarColor = 16777215;
    @Condition(requiredOption = "advancedMode")
    @Entry(category = FEATURES, name = "Splash Progress Bar Background Color")
    public static int progressBarBackgroundColor = 15675965;
    @Condition(requiredOption = "advancedMode")
    @Entry(category = FEATURES, name = "Splash Progress Bar Frame Color")
    public static int progressFrameColor = 16777215;
    @Condition(requiredOption = "advancedMode")
    @Entry(category = FEATURES, name = "puzzle.option.better_splash_screen_blend")
    public static boolean disableBlend = false;
    @Condition(requiredOption = "advancedMode")
    @Entry(category = FEATURES, name = "Custom Blend Function")
    public static List<String> customBlendFunction = new ArrayList<>();
}
