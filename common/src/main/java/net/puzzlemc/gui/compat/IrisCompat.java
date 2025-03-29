package net.puzzlemc.gui.compat;

import net.irisshaders.iris.api.v0.IrisApi;
import net.irisshaders.iris.api.v0.IrisApiConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.puzzlemc.gui.PuzzleApi;
import net.puzzlemc.gui.PuzzleGui;
import net.puzzlemc.gui.screen.widget.PuzzleWidget;

public class IrisCompat {
    public static void init() {
        PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Text.of("\uD83D\uDC41 Iris")));
        PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Text.translatable("iris.puzzle.option.enableShaders"), (button) -> button.setMessage(IrisApi.getInstance().getConfig().areShadersEnabled() ? PuzzleGui.YES : PuzzleGui.NO), (button) -> {
            IrisApiConfig irisConfig = IrisApi.getInstance().getConfig();
            irisConfig.setShadersEnabledAndApply(!irisConfig.areShadersEnabled());
        }));
        PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Text.translatable("options.iris.shaderPackSelection.title"), (button) -> button.setMessage(Text.literal("➥ ").append(Text.translatable("iris.puzzle.option.open").formatted(Formatting.GOLD))), (button) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            client.setScreen((Screen) IrisApi.getInstance().openMainIrisScreenObj(client.currentScreen));
        }));
    }
}
