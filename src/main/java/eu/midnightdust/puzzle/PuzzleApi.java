package eu.midnightdust.puzzle;

import eu.midnightdust.puzzle.screen.widget.PuzzleWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class PuzzleApi {
    public static List<PuzzleWidget> GRAPHICS_OPTIONS = new ArrayList<>();
    public static List<PuzzleWidget> MISC_OPTIONS = new ArrayList<>();
    public static List<PuzzleWidget> PERFORMANCE_OPTIONS = new ArrayList<>();
    public static List<PuzzleWidget> TEXTURE_OPTIONS = new ArrayList<>();

    public static void addToGraphicsOptions(PuzzleWidget button) {GRAPHICS_OPTIONS.add(button);}
    public static void addToMiscOptions(PuzzleWidget button) {MISC_OPTIONS.add(button);}
    public static void addToPerformanceOptions(PuzzleWidget button) {PERFORMANCE_OPTIONS.add(button);}
    public static void addToTextureOptions(PuzzleWidget button) {TEXTURE_OPTIONS.add(button);}
}
