package net.puzzlemc.gui;

import eu.midnightdust.lib.util.MidnightColorUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.irisshaders.iris.api.v0.IrisApi;
import net.irisshaders.iris.api.v0.IrisApiConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.puzzlemc.gui.screen.widget.PuzzleWidget;

public class IrisCompat {
    public static void init() {
        if (FabricLoader.getInstance().isModLoaded("iris")) {
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Text.of("Iris")));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("iris.puzzle.option.enableShaders"), (button) -> button.setMessage(IrisApi.getInstance().getConfig().areShadersEnabled() ? PuzzleClient.YES : PuzzleClient.NO), (button) -> {
                IrisApiConfig irisConfig = IrisApi.getInstance().getConfig();
                irisConfig.setShadersEnabledAndApply(!irisConfig.areShadersEnabled());
            }));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("options.iris.shaderPackSelection.title"), (button) -> button.setMessage(new LiteralText("➥ ").append(new TranslatableText("iris.puzzle.option.open").setStyle(Style.EMPTY.withColor(MidnightColorUtil.radialRainbow(0.5f, 1).getRGB())))), (button) -> {
                MinecraftClient client = MinecraftClient.getInstance();
                client.setScreen((Screen) IrisApi.getInstance().openMainIrisScreenObj(client.currentScreen));
            }));
        }
    }
}
