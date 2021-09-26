package net.puzzlemc.gui.screen;

import net.coderbot.iris.gui.screen.ShaderPackScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;

import java.util.Objects;

public class IrisButton extends DrawableHelper {
    public static ButtonWidget getButton(int x, int y, int width, int height, Screen parent, MinecraftClient client) {
        ShaderPackScreen shaderPackPage = new ShaderPackScreen(parent);
        return new ButtonWidget(x, y, width, height, shaderPackPage.getTitle().copy().append("..."), (button) ->
                Objects.requireNonNull(client).setScreen(shaderPackPage));
    }
}
