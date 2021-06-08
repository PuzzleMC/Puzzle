package net.puzzlemc.gui.screen.page;

import net.puzzlemc.gui.PuzzleApi;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

public class ResourcesPage extends AbstractPuzzleOptionsPage {

    public ResourcesPage(Screen parent) {
        super(parent, new TranslatableText("puzzle.page.resources"), PuzzleApi.RESOURCE_OPTIONS);
    }
}
