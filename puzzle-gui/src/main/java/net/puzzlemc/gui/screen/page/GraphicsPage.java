package net.puzzlemc.gui.screen.page;

import net.minecraft.text.Text;
import net.puzzlemc.gui.PuzzleApi;
import net.minecraft.client.gui.screen.Screen;

public class GraphicsPage extends AbstractPuzzleOptionsPage {

    public GraphicsPage(Screen parent) {
        super(parent, Text.translatable("puzzle.page.graphics"), PuzzleApi.GRAPHICS_OPTIONS);
    }
}
