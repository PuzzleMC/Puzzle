package net.puzzlemc.core;

import net.fabricmc.loader.api.FabricLoader;
import net.puzzlemc.core.config.PuzzleConfig;
import net.puzzlemc.core.util.UpdateChecker;
import net.fabricmc.api.ClientModInitializer;

public class PuzzleCore implements ClientModInitializer {

    public final static String version = "Puzzle "+ FabricLoader.getInstance().getModContainer("puzzle").get().getMetadata().getVersion();
    public final static String name = "Puzzle";
    public final static String id = "puzzle";
    public final static String website = "https://github.com/PuzzleMC/Puzzle";
    public final static String updateURL = "https://modrinth.com/mod/puzzle";

    public final static String UPDATE_CHECKER_URL = "https://raw.githubusercontent.com/PuzzleMC/Puzzle-Versions/main/puzzle_versions.json";

    @Override
    public void onInitializeClient() {
        PuzzleConfig.init(id, PuzzleConfig.class);
        if (PuzzleConfig.checkUpdates) UpdateChecker.init();
    }
}
