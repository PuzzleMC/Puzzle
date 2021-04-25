package eu.midnightdust.puzzle.screen.page;

import eu.midnightdust.puzzle.PuzzleApi;
import eu.midnightdust.puzzle.screen.page.AbstractPuzzleOptionsPage;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

public class GraphicsPage extends AbstractPuzzleOptionsPage {

    public GraphicsPage(Screen parent) {
        super(parent, new TranslatableText("puzzle.page.graphics"), PuzzleApi.GRAPHICS_OPTIONS);
    }
}
