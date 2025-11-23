package net.puzzlemc.gui.compat;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.puzzlemc.gui.PuzzleApi;
import net.puzzlemc.gui.screen.widget.PuzzleWidget;
import traben.entity_model_features.EMF;
import traben.entity_model_features.config.EMFConfig;
import traben.entity_texture_features.config.ETFConfig;
import traben.entity_texture_features.config.screens.ETFConfigScreenMain;

import java.util.EnumSet;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.TreeSet;

import static net.puzzlemc.gui.PuzzleGui.NO;
import static net.puzzlemc.gui.PuzzleGui.YES;

public class EMFCompat {
    public static void init() {
        PuzzleApi.addToResourceOptions(new PuzzleWidget(Component.literal("\uD83D\uDC37 ").append(Component.translatable("entity_model_features.title"))));
        EMFConfig emfConfig = EMF.config().getConfig();
        PuzzleApi.addToResourceOptions(new PuzzleWidget(Component.translatable("entity_model_features.config.update"), (button) -> button.setMessage(Component.literal(emfConfig.modelUpdateFrequency.toString())), (button) -> {
            final NavigableSet<ETFConfig.UpdateFrequency> set = new TreeSet<>(EnumSet.allOf(ETFConfig.UpdateFrequency.class));
            emfConfig.modelUpdateFrequency = Objects.requireNonNullElseGet(set.higher(emfConfig.modelUpdateFrequency), set::first);
            EMF.config().saveToFile();
        }));
        PuzzleApi.addToResourceOptions(new PuzzleWidget(0, 65, Component.translatable("entity_model_features.config.lod"), () -> emfConfig.animationLODDistance,
                (button) -> button.setMessage(lodMessage(emfConfig.animationLODDistance)),
                (slider) -> {
                    try {
                        emfConfig.animationLODDistance = slider.getInt();
                    } catch (NumberFormatException ignored) {}
                    EMF.config().saveToFile();
                }));
        PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Component.translatable("entity_model_features.config.low_fps_lod"), (button) -> button.setMessage(emfConfig.retainDetailOnLowFps ? YES : NO), (button) -> {
            emfConfig.retainDetailOnLowFps = !emfConfig.retainDetailOnLowFps;
            EMF.config().saveToFile();
        }));
        PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Component.translatable("entity_model_features.config.large_mob_lod"), (button) -> button.setMessage(emfConfig.retainDetailOnLargerMobs ? YES : NO), (button) -> {
            emfConfig.retainDetailOnLargerMobs = !emfConfig.retainDetailOnLargerMobs;
            EMF.config().saveToFile();
        }));
        PuzzleApi.addToResourceOptions(new PuzzleWidget(Component.translatable("puzzle.action.open_config_screen"), (button) -> button.setMessage(Component.nullToEmpty("OPEN")), button -> Minecraft.getInstance().setScreen(new ETFConfigScreenMain(Minecraft.getInstance().screen))));
    }
    public static Component lodMessage(int value) {
        if (value == 20) return Component.literal(value + " (Default)");
        else if (value == 0 || value == 65) return CommonComponents.OPTION_OFF.copy().withStyle(ChatFormatting.RED);
        else return Component.literal(String.valueOf(value)).withStyle(ChatFormatting.AQUA);
    }
}
