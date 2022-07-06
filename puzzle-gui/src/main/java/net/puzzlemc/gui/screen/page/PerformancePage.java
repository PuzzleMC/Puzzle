package net.puzzlemc.gui.screen.page;

import net.minecraft.text.Text;
import net.puzzlemc.gui.PuzzleApi;
import net.minecraft.client.gui.screen.Screen;

public class PerformancePage extends AbstractPuzzleOptionsPage {

    public PerformancePage(Screen parent) {
        super(parent, Text.translatable("puzzle.page.performance"), PuzzleApi.PERFORMANCE_OPTIONS);
    }
}
