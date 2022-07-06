package net.puzzlemc.gui.screen.page;

import net.minecraft.text.TranslatableText;
import net.puzzlemc.gui.PuzzleApi;
import net.minecraft.client.gui.screen.Screen;

public class GraphicsPage extends AbstractPuzzleOptionsPage {

    public GraphicsPage(Screen parent) {
        super(parent, new TranslatableText("puzzle.page.graphics"), PuzzleApi.GRAPHICS_OPTIONS);
    }
}
