package net.puzzlemc.gui;

import net.puzzlemc.core.config.PuzzleConfig;
import net.puzzlemc.gui.screen.widget.PuzzleWidget;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class PuzzleApi {
    private static Logger LOGGER = LogManager.getLogger("puzzle-gui");

    public static List<PuzzleWidget> GRAPHICS_OPTIONS = new ArrayList<>();
    public static List<PuzzleWidget> MISC_OPTIONS = new ArrayList<>();
    public static List<PuzzleWidget> PERFORMANCE_OPTIONS = new ArrayList<>();
    public static List<PuzzleWidget> RESOURCE_OPTIONS = new ArrayList<>();

    public static void addToGraphicsOptions(PuzzleWidget button) {
        GRAPHICS_OPTIONS.add(button);
        if (PuzzleConfig.debugMessages) LOGGER.info(button.descriptionText.getContent().toString() + " -> Graphics Options");
    }
    public static void addToMiscOptions(PuzzleWidget button) {
        MISC_OPTIONS.add(button);
        if (PuzzleConfig.debugMessages) LOGGER.info(button.descriptionText.getContent().toString() + " -> Misc Options");
    }
    public static void addToPerformanceOptions(PuzzleWidget button) {
        PERFORMANCE_OPTIONS.add(button);
        if (PuzzleConfig.debugMessages) LOGGER.info(button.descriptionText.getContent().toString() + "- > Performance Options");
    }
    public static void addToResourceOptions(PuzzleWidget button) {
        RESOURCE_OPTIONS.add(button);
        if (PuzzleConfig.debugMessages) LOGGER.info(button.descriptionText.getContent().toString() + " -> Resource Options");
    }
}
