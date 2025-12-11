package net.puzzlemc.core;

//? fabric {
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.puzzlemc.gui.screen.PuzzleOptionsScreen;

public class PuzzleModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return PuzzleOptionsScreen::new;
    }
}
//?}