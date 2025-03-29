package net.puzzlemc.gui.compat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.puzzlemc.gui.PuzzleApi;
import net.puzzlemc.gui.screen.widget.PuzzleWidget;
import shcm.shsupercm.fabric.citresewn.config.CITResewnConfig;

import static net.minecraft.screen.ScreenTexts.NO;
import static net.minecraft.screen.ScreenTexts.YES;

public class CITRCompat {
    public static void init() {
        if (CITResewnConfig.INSTANCE != null) {
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.of("⛏ CIT Resewn")));
            CITResewnConfig citConfig = CITResewnConfig.INSTANCE;
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("config.citresewn.enabled.title"), (button) -> button.setMessage(citConfig.enabled ? YES : NO), (button) -> {
                citConfig.enabled = !citConfig.enabled;
                citConfig.write();
                MinecraftClient.getInstance().reloadResources();
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("config.citresewn.mute_errors.title"), (button) -> button.setMessage(citConfig.mute_errors ? YES : NO), (button) -> {
                citConfig.mute_errors = !citConfig.mute_errors;
                citConfig.write();
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("config.citresewn.mute_warns.title"), (button) -> button.setMessage(citConfig.mute_warns ? YES : NO), (button) -> {
                citConfig.mute_warns = !citConfig.mute_warns;
                citConfig.write();
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("config.citresewn.broken_paths.title"), (button) -> button.setMessage(citConfig.broken_paths ? YES : NO), (button) -> {
                citConfig.broken_paths = !citConfig.broken_paths;
                citConfig.write();
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(0, 100, Text.translatable("config.citresewn.cache_ms.title"), () -> citConfig.cache_ms,
                    (button) -> button.setMessage(message(citConfig)),
                    (slider) -> {
                        try {
                            citConfig.cache_ms = slider.getInt();
                        } catch (NumberFormatException ignored) {
                        }
                        citConfig.write();
                    }));
        }
    }
    public static Text message(CITResewnConfig config) {
        int ticks = config.cache_ms;
        if (ticks <= 1) {
            return (Text.translatable("config.citresewn.cache_ms.ticks." + ticks)).formatted(Formatting.AQUA);
        } else {
            Formatting color = Formatting.DARK_RED;
            if (ticks <= 40) color = Formatting.RED;
            if (ticks <= 20) color = Formatting.GOLD;
            if (ticks <= 10) color = Formatting.DARK_GREEN;
            if (ticks <= 5) color = Formatting.GREEN;

            return (Text.translatable("config.citresewn.cache_ms.ticks.any", ticks)).formatted(color);
        }
    }
}
