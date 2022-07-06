package net.puzzlemc.gui.screen.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;

import java.util.*;

@Environment(EnvType.CLIENT)
public class PuzzleOptionListWidget extends ElementListWidget<PuzzleOptionListWidget.ButtonEntry> {
    TextRenderer textRenderer;

    public PuzzleOptionListWidget(MinecraftClient minecraftClient, int i, int j, int k, int l, int m) {
        super(minecraftClient, i, j, k, l, m);
        this.centerListVertically = false;
        textRenderer = minecraftClient.textRenderer;
    }

    public void addButton(ClickableWidget button, Text text) {
        this.addEntry(ButtonEntry.create(button, text));
    }

    public void addAll(List<PuzzleWidget> buttons) {
        for (PuzzleWidget button : buttons) {
            if (button.buttonType == ButtonType.TEXT) {
                this.addButton(null, button.descriptionText);
            } else if (button.buttonType == ButtonType.BUTTON) {
                this.addButton(new PuzzleButtonWidget(this.width / 2 + 25, 0, 150, 20, button.buttonTextAction, button.onPress), button.descriptionText);
            } else if (button.buttonType == ButtonType.SLIDER) {
                this.addButton(new PuzzleSliderWidget(button.min, button.max, this.width / 2 + 25, 0, 150, 20, button.setSliderValue, button.buttonTextAction, button.changeSliderValue), button.descriptionText);
            } else if (button.buttonType == ButtonType.TEXT_FIELD) {
                this.addButton(new PuzzleTextFieldWidget(textRenderer, this.width / 2 + 25, 0, 150, 20, button.setTextValue, button.changeTextValue), button.descriptionText);
            } else
                LogManager.getLogger("Puzzle").warn("Button " + button + " is missing the buttonType variable. This shouldn't happen!");
        }
    }

    public int getRowWidth() {
        return 400;
    }

    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + 60;
    }

    public static class ButtonEntry extends ElementListWidget.Entry<ButtonEntry> {
        private static final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        private List<ClickableWidget> buttons = new ArrayList<>();
        private final ClickableWidget button;
        private final Text text;

        private ButtonEntry(ClickableWidget button, Text text) {
            if (button != null)
            this.buttons.add(button);
            this.button = button;
            this.text = text;
        }

        public static ButtonEntry create(ClickableWidget button, Text text) {
            return new ButtonEntry(button, text);
        }

        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            if (button != null) {
                button.y = y;
                button.render(matrices, mouseX, mouseY, tickDelta);
            }
            if (button == null) drawCenteredText(matrices,textRenderer, Text.literal("－－－－－－ ").append(text).append(" －－－－－－"),x + 200,y+5,0xFFFFFF);
            else drawTextWithShadow(matrices,textRenderer, text,x+15,y+5,0xFFFFFF);
        }

        public List<? extends Element> children() {
            return buttons;
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return buttons;
        }
    }
}
