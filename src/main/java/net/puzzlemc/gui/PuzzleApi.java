package net.puzzlemc.gui;

import net.puzzlemc.core.config.PuzzleConfig;
import net.puzzlemc.gui.screen.widget.PuzzleWidget;

import java.util.ArrayList;
import java.util.List;

import static net.puzzlemc.core.PuzzleCore.LOGGER;

public class PuzzleApi {
    public static List<PuzzleWidget> GRAPHICS_OPTIONS = new ArrayList<>();
    public static List<PuzzleWidget> MISC_OPTIONS = new ArrayList<>();
    public static List<PuzzleWidget> PERFORMANCE_OPTIONS = new ArrayList<>();
    public static List<PuzzleWidget> RESOURCE_OPTIONS = new ArrayList<>();

    public static void addToGraphicsOptions(PuzzleWidget button) {
        GRAPHICS_OPTIONS.add(button);
        if (PuzzleConfig.debugMessages)
            LOGGER.info("{} -> Graphics Options", button.descriptionText.getContents().toString());
    }
    public static void addToMiscOptions(PuzzleWidget button) {
        MISC_OPTIONS.add(button);
        if (PuzzleConfig.debugMessages)
            LOGGER.info("{} -> Misc Options", button.descriptionText.getContents().toString());
    }
    public static void addToPerformanceOptions(PuzzleWidget button) {
        PERFORMANCE_OPTIONS.add(button);
        if (PuzzleConfig.debugMessages)
            LOGGER.info("{}- > Performance Options", button.descriptionText.getContents().toString());
    }
    public static void addToResourceOptions(PuzzleWidget button) {
        RESOURCE_OPTIONS.add(button);
        if (PuzzleConfig.debugMessages)
            LOGGER.info("{} -> Resource Options", button.descriptionText.getContents().toString());
    }
}
