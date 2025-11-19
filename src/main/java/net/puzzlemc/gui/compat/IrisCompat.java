package net.puzzlemc.gui.compat;

import net.irisshaders.iris.api.v0.IrisApi;
import net.irisshaders.iris.api.v0.IrisApiConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.puzzlemc.gui.PuzzleApi;
import net.puzzlemc.gui.PuzzleGui;
import net.puzzlemc.gui.screen.widget.PuzzleWidget;

public class IrisCompat {
    public static void init() {
        PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Component.literal("\uD83D\uDC41 Iris")));
        PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Component.translatable("iris.puzzle.option.enableShaders"), (button) -> button.setMessage(IrisApi.getInstance().getConfig().areShadersEnabled() ? PuzzleGui.YES : PuzzleGui.NO), (button) -> {
            IrisApiConfig irisConfig = IrisApi.getInstance().getConfig();
            irisConfig.setShadersEnabledAndApply(!irisConfig.areShadersEnabled());
        }));
        PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Component.translatable("options.iris.shaderPackSelection.title"), (button) -> button.setMessage(Component.literal("➥ ").append(Component.translatable("iris.puzzle.option.open").withStyle(ChatFormatting.GOLD))), (button) -> {
            Minecraft client = Minecraft.getInstance();
            client.setScreen((Screen) IrisApi.getInstance().openMainIrisScreenObj(client.screen));
        }));
    }
}
