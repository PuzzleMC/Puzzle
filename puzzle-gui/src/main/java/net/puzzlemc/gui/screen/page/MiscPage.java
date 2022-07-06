package net.puzzlemc.gui.screen.page;

import net.minecraft.text.TranslatableText;
import net.puzzlemc.gui.PuzzleApi;
import net.minecraft.client.gui.screen.Screen;

public class MiscPage extends AbstractPuzzleOptionsPage {

    public MiscPage(Screen parent) {
        super(parent, new TranslatableText("puzzle.page.misc"), PuzzleApi.MISC_OPTIONS);
    }
}
