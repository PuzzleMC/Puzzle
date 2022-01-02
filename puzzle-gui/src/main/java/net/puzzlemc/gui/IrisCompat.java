package net.puzzlemc.gui;

import net.coderbot.iris.Iris;
import net.coderbot.iris.config.IrisConfig;
import net.coderbot.iris.gui.screen.ShaderPackScreen;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.puzzlemc.gui.screen.widget.PuzzleWidget;

import java.io.IOException;

public class IrisCompat {
    public static void init() {
        if (FabricLoader.getInstance().isModLoaded("iris")) {
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Text.of("Iris")));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Text.of("Enable Shaders"), (button) -> button.setMessage(Iris.getIrisConfig().areShadersEnabled() ? PuzzleClient.YES : PuzzleClient.NO), (button) -> {
                IrisConfig irisConfig = Iris.getIrisConfig();
                irisConfig.setShadersEnabled(!irisConfig.areShadersEnabled());
                try {
                    Iris.getIrisConfig().save();
                } catch (IOException e) {
                    Iris.logger.error("Error saving configuration file!");
                    Iris.logger.catching(e);
                }

                try {
                    Iris.reload();
                } catch (IOException e) {
                    Iris.logger.error("Error reloading shader pack while applying changes!");
                    Iris.logger.catching(e);
                }
            }));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("options.iris.shaderPackSelection.title"), (button) -> button.setMessage(Text.of("OPEN")), (button) -> {
                MinecraftClient client = MinecraftClient.getInstance();
                ShaderPackScreen shaderPackPage = new ShaderPackScreen(client.currentScreen);
                client.setScreen(shaderPackPage);
            }));
        }
    }
}
