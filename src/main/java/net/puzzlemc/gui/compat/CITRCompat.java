package net.puzzlemc.gui.compat;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.puzzlemc.gui.PuzzleApi;
import net.puzzlemc.gui.screen.widget.PuzzleWidget;
import shcm.shsupercm.fabric.citresewn.config.CITResewnConfig;

import static net.minecraft.network.chat.CommonComponents.GUI_NO;
import static net.minecraft.network.chat.CommonComponents.GUI_YES;

public class CITRCompat {
    public static void init() {
        if (CITResewnConfig.INSTANCE != null) {
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Component.literal("⛏ CIT Resewn")));
            CITResewnConfig citConfig = CITResewnConfig.INSTANCE;
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Component.translatable("config.citresewn.enabled.title"), (button) -> button.setMessage(citConfig.enabled ? GUI_YES : GUI_NO), (button) -> {
                citConfig.enabled = !citConfig.enabled;
                citConfig.write();
                Minecraft.getInstance().reloadResourcePacks();
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Component.translatable("config.citresewn.mute_errors.title"), (button) -> button.setMessage(citConfig.mute_errors ? GUI_YES : GUI_NO), (button) -> {
                citConfig.mute_errors = !citConfig.mute_errors;
                citConfig.write();
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Component.translatable("config.citresewn.mute_warns.title"), (button) -> button.setMessage(citConfig.mute_warns ? GUI_YES : GUI_NO), (button) -> {
                citConfig.mute_warns = !citConfig.mute_warns;
                citConfig.write();
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Component.translatable("config.citresewn.broken_paths.title"), (button) -> button.setMessage(citConfig.broken_paths ? GUI_YES : GUI_NO), (button) -> {
                citConfig.broken_paths = !citConfig.broken_paths;
                citConfig.write();
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(0, 100, Component.translatable("config.citresewn.cache_ms.title"), () -> citConfig.cache_ms,
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
    public static Component message(CITResewnConfig config) {
        int ticks = config.cache_ms;
        if (ticks <= 1) {
            return (Component.translatable("config.citresewn.cache_ms.ticks." + ticks)).withStyle(ChatFormatting.AQUA);
        } else {
            ChatFormatting color = ChatFormatting.DARK_RED;
            if (ticks <= 40) color = ChatFormatting.RED;
            if (ticks <= 20) color = ChatFormatting.GOLD;
            if (ticks <= 10) color = ChatFormatting.DARK_GREEN;
            if (ticks <= 5) color = ChatFormatting.GREEN;

            return (Component.translatable("config.citresewn.cache_ms.ticks.any", ticks)).withStyle(color);
        }
    }
}
