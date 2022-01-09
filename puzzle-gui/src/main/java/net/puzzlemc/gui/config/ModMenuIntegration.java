package net.puzzlemc.gui.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.puzzlemc.gui.screen.PuzzleOptionsScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return PuzzleOptionsScreen::new;
    }

    //Used to set the config screen for all modules //
    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        Map<String, ConfigScreenFactory<?>> map = new HashMap<>();
        map.put("puzzle",PuzzleOptionsScreen::new);
        map.put("puzzle-gui",PuzzleOptionsScreen::new);
        map.put("puzzle-blocks",PuzzleOptionsScreen::new);
        map.put("puzzle-base",PuzzleOptionsScreen::new);
        map.put("puzzle-models",PuzzleOptionsScreen::new);
        map.put("puzzle-emissives",PuzzleOptionsScreen::new);
        map.put("puzzle-splashscreen",PuzzleOptionsScreen::new);
        return map;
    }
}