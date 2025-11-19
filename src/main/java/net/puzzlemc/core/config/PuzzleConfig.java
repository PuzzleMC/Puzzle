package net.puzzlemc.core.config;

import eu.midnightdust.lib.config.MidnightConfig;

import java.util.ArrayList;
import java.util.List;

public class PuzzleConfig extends MidnightConfig {
    private static final String GUI = "gui";
    private static final String INTERNAL = "internal";
    private static final String FEATURES = "features";

    @Entry(category = GUI, name = "Disabled integrations") public static List<String> disabledIntegrations = new ArrayList<>();
    @Entry(category = GUI, name = "Enable Puzzle button") public static boolean enablePuzzleButton = true;

    @Entry(category = FEATURES, name = "puzzle.option.resourcepack_splash_screen") public static boolean resourcepackSplashScreen = true;
    @Entry(category = FEATURES, name = "puzzle.option.unlimited_model_rotations") public static boolean unlimitedRotations = true;
    @Entry(category = FEATURES, name = "puzzle.option.bigger_custom_models") public static boolean biggerModels = true;

    @Entry(category = INTERNAL, name = "Enable debug messages") public static boolean debugMessages = false;
    @Entry(category = INTERNAL, name = "Has custom splash screen") public static boolean hasCustomSplashScreen = false;
    @Entry(category = INTERNAL, name = "Splash Background Color") public static int backgroundColor = 15675965;
    @Entry(category = INTERNAL, name = "Splash Progress Bar Color") public static int progressBarColor = 16777215;
    @Entry(category = INTERNAL, name = "Splash Progress Bar Background Color") public static int progressBarBackgroundColor = 15675965;
    @Entry(category = INTERNAL, name = "Splash Progress Bar Frame Color") public static int progressFrameColor = 16777215;
    @Entry(category = INTERNAL, name = "puzzle.option.better_splash_screen_blend") public static boolean disableBlend = false;
    @Entry(category = INTERNAL, name = "Custom Blend Function") public static List<String> customBlendFunction = new ArrayList<>();
}
