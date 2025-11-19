package net.puzzlemc.core;

import net.puzzlemc.core.config.PuzzleConfig;
import net.puzzlemc.gui.PuzzleGui;
import net.puzzlemc.splashscreen.PuzzleSplashScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PuzzleCore {
    public final static String MOD_ID = "puzzle";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void initModules() {
        PuzzleConfig.init(MOD_ID, PuzzleConfig.class);
        PuzzleGui.init();
        PuzzleSplashScreen.init();
    }
}
