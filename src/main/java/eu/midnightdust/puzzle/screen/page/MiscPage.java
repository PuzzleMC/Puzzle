package eu.midnightdust.puzzle.screen.page;

import eu.midnightdust.puzzle.PuzzleApi;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

public class MiscPage extends AbstractPuzzleOptionsPage {

    public MiscPage(Screen parent) {
        super(parent, new TranslatableText("puzzle.page.misc"), PuzzleApi.MISC_OPTIONS);
    }
}
