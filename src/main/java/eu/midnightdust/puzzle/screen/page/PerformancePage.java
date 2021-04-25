package eu.midnightdust.puzzle.screen.page;

import eu.midnightdust.puzzle.PuzzleApi;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

public class PerformancePage extends AbstractPuzzleOptionsPage {

    public PerformancePage(Screen parent) {
        super(parent, new TranslatableText("puzzle.page.performance"), PuzzleApi.PERFORMANCE_OPTIONS);
    }
}
