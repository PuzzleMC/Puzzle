package net.puzzlemc.gui.screen.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class PuzzleButtonWidget extends ButtonWidget {
    private final PuzzleWidget.TextAction title;

    public PuzzleButtonWidget(int x, int y, int width, int height, PuzzleWidget.TextAction title, PressAction onPress) {
        super(x, y, width, height, Text.of(""), onPress, Supplier::get);
        this.title = title;
    }
    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        try {
            title.setTitle(this);
        } catch (Exception e) {e.printStackTrace(); this.visible = false;}
        super.renderButton(context, mouseX, mouseY, delta);
    }
}
