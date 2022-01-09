package net.puzzlemc.core.config;

import eu.midnightdust.lib.config.MidnightConfig;

import java.util.ArrayList;
import java.util.List;

public class PuzzleConfig extends MidnightConfig {
    @Entry public static List<String> disabledIntegrations = new ArrayList<>();
    @Entry public static boolean enablePuzzleButton = true;
    @Entry public static boolean debugMessages = false;

    @Entry public static boolean checkUpdates = true;
    @Entry public static boolean showPuzzleInfo = true;
    @Entry public static boolean resourcepackSplashScreen = true;
    @Entry public static boolean disableSplashScreenBlend = false;
    @Entry public static boolean emissiveTextures = true;
    @Entry public static boolean unlimitedRotations = true;
    @Entry public static boolean biggerModels = true;

    @Entry public static int backgroundColor = 15675965;
    @Entry public static int progressBarColor = 16777215;
    @Entry public static int progressFrameColor = 16777215;
}
