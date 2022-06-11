package net.puzzlemc.gui.screen.page;

import net.minecraft.text.Text;
import net.puzzlemc.gui.PuzzleApi;
import net.minecraft.client.gui.screen.Screen;

public class ResourcesPage extends AbstractPuzzleOptionsPage {

    public ResourcesPage(Screen parent) {
        super(parent, Text.translatable("puzzle.page.resources"), PuzzleApi.RESOURCE_OPTIONS);
    }
}
