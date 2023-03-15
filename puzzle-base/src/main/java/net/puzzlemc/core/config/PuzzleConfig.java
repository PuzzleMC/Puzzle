package net.puzzlemc.core.config;

import eu.midnightdust.lib.config.MidnightConfig;

import java.util.ArrayList;
import java.util.List;

public class PuzzleConfig extends MidnightConfig {
    @Entry(category = "gui", name = "Disabled integrations") public static List<String> disabledIntegrations = new ArrayList<>();
    @Entry(category = "gui", name = "Enable Puzzle button") public static boolean enablePuzzleButton = true;
    @Entry(category = "debug", name = "Enable debug messages") public static boolean debugMessages = false;

    @Entry(category = "debug", name = "puzzle.option.check_for_updates") public static boolean checkUpdates = true;
    @Entry(category = "gui", name = "puzzle.option.show_version_info") public static boolean showPuzzleInfo = true;
    @Entry(category = "features", name = "puzzle.option.resourcepack_splash_screen") public static boolean resourcepackSplashScreen = true;
    @Entry(category = "features", name = "puzzle.option.unlimited_model_rotations") public static boolean unlimitedRotations = true;
    @Entry(category = "features", name = "puzzle.option.bigger_custom_models") public static boolean biggerModels = true;

    @Entry(category = "features", name = "Splash Background Color") public static int backgroundColor = 15675965;
    @Entry(category = "features", name = "Splash Progress Bar Color") public static int progressBarColor = 16777215;
    @Entry(category = "features", name = "Splash Progress Bar Background Color") public static int progressBarBackgroundColor = 15675965;
    @Entry(category = "features", name = "Splash Progress Bar Frame Color") public static int progressFrameColor = 16777215;
    @Entry(category = "features", name = "puzzle.option.better_splash_screen_blend") public static boolean disableBlend = false;
}
