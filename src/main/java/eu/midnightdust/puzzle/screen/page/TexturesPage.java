package eu.midnightdust.puzzle.screen.page;

import eu.midnightdust.puzzle.PuzzleApi;
import eu.midnightdust.puzzle.screen.page.AbstractPuzzleOptionsPage;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

public class TexturesPage extends AbstractPuzzleOptionsPage {

    public TexturesPage(Screen parent) {
        super(parent, new TranslatableText("puzzle.page.textures"), PuzzleApi.TEXTURE_OPTIONS);
    }
}
