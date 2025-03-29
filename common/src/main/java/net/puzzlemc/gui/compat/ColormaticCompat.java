package net.puzzlemc.gui.compat;

import io.github.kvverti.colormatic.Colormatic;
import io.github.kvverti.colormatic.ColormaticConfig;
import io.github.kvverti.colormatic.ColormaticConfigController;
import net.minecraft.text.Text;
import net.puzzlemc.gui.PuzzleApi;
import net.puzzlemc.gui.screen.widget.PuzzleWidget;

import static net.puzzlemc.gui.PuzzleGui.NO;
import static net.puzzlemc.gui.PuzzleGui.YES;

public class ColormaticCompat {
    public static void init() {
        PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.of("\uD83C\uDF08 Colormatic")));
        ColormaticConfig colormaticConfig = Colormatic.config();
        PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("colormatic.config.option.clearSky"), (button) -> button.setMessage(colormaticConfig.clearSky ? YES : NO), (button) -> {
            colormaticConfig.clearSky = !colormaticConfig.clearSky;
            ColormaticConfigController.persist(colormaticConfig);
        }));
        PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("colormatic.config.option.clearVoid"), (button) -> button.setMessage(colormaticConfig.clearVoid ? YES : NO), (button) -> {
            colormaticConfig.clearVoid = !colormaticConfig.clearVoid;
            ColormaticConfigController.persist(colormaticConfig);
        }));
        PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("colormatic.config.option.blendSkyLight"), (button) -> button.setMessage(colormaticConfig.blendSkyLight ? YES : NO), (button) -> {
            colormaticConfig.blendSkyLight = !colormaticConfig.blendSkyLight;
            ColormaticConfigController.persist(colormaticConfig);
        }));
        PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("colormatic.config.option.flickerBlockLight"), (button) -> button.setMessage(colormaticConfig.flickerBlockLight ? YES : NO), (button) -> {
            colormaticConfig.flickerBlockLight = !colormaticConfig.flickerBlockLight;
            ColormaticConfigController.persist(colormaticConfig);
        }));
        PuzzleApi.addToResourceOptions(new PuzzleWidget(0, 100, Text.translatable("colormatic.config.option.relativeBlockLightIntensity"),
                () -> (int) (100*(1.0 - colormaticConfig.relativeBlockLightIntensityExponent / -16.0)),
                (button) -> button.setMessage(Text.translatable("colormatic.config.option.relativeBlockLightIntensity")
                        .append(": ")
                        .append(Text.literal(String.valueOf((int)(100 * Math.exp(ColormaticConfig.scaled(colormaticConfig.relativeBlockLightIntensityExponent))))).append("%"))),
                (slider) -> {
                    try {
                        colormaticConfig.relativeBlockLightIntensityExponent = ((1.00 - ((slider.getInt())/100f)) * -16.0);
                        ColormaticConfigController.persist(colormaticConfig);
                    }
                    catch (NumberFormatException ignored) {}
                }));
    }
}
