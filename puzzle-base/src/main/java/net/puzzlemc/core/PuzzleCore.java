package net.puzzlemc.core;

import net.puzzlemc.core.config.PuzzleConfig;
import net.puzzlemc.core.util.UpdateChecker;
import net.fabricmc.api.ClientModInitializer;

public class PuzzleCore implements ClientModInitializer {

    public final static String version = "Puzzle A1";
    public final static String name = "Puzzle";
    public final static String id = "puzzle";
    public final static String website = "https://github.com/PuzzleMC/Puzzle";
    public static String updateURL = website; //+"download";

    public final static String UPDATE_URL = "https://raw.githubusercontent.com/PuzzleMC/Puzzle/1.17/puzzle_versions.json";

    @Override
    public void onInitializeClient() {
        PuzzleConfig.init(id, PuzzleConfig.class);
        if (PuzzleConfig.checkUpdates) UpdateChecker.init();
    }
}
