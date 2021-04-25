package eu.midnightdust.puzzle.screen.widget;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
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

    public void addButton(AbstractButtonWidget button, Text text) {
        this.addEntry(ButtonEntry.create(button, text));
    }

    public void addAll(List<PuzzleWidget> buttons) {
        for (int i = 0; i < buttons.size(); ++i) {
            PuzzleWidget button = buttons.get(i);
            if (button.buttonType == ButtonType.BUTTON) {
                this.addButton(new PuzzleButtonWidget(this.width / 2 - 155 + 160, 0, 150, 20, button.buttonTextAction, button.onPress), button.descriptionText);
            }
            else if (button.buttonType == ButtonType.SLIDER) {
                this.addButton(new PuzzleSliderWidget(button.min,button.max,this.width / 2 - 155 + 160, 0, 150, 20, ((TranslatableText) button.buttonText),1), button.descriptionText);
            }
            else if (button.buttonType == ButtonType.TEXT_FIELD) {
                this.addButton(new PuzzleTextFieldWidget(textRenderer,this.width / 2 - 155 + 160, 0, 150, 20,null, button.buttonText), button.descriptionText);
            }
            else LogManager.getLogger("Puzzle").warn("Button " + button + " is missing the buttonType variable. This shouldn't happen!");
        }
    }

    public int getRowWidth() {
        return 400;
    }

    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + 32;
    }

    public Optional<AbstractButtonWidget> getHoveredButton(double mouseX, double mouseY) {
        for (ButtonEntry buttonEntry : this.children()) {
            for (AbstractButtonWidget abstractButtonWidget : buttonEntry.buttons) {
                if (abstractButtonWidget.isMouseOver(mouseX, mouseY)) {
                    return Optional.of(abstractButtonWidget);
                }
            }
        }
        return Optional.empty();
    }

    public static class ButtonEntry extends ElementListWidget.Entry<ButtonEntry> {
        private static final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        private final List<AbstractButtonWidget> buttons;
        private final Map<AbstractButtonWidget, Text> buttonsWithText;

        private ButtonEntry(ImmutableMap<AbstractButtonWidget, Text> buttons) {
            this.buttons = buttons.keySet().asList();
            this.buttonsWithText = buttons;
        }

        public static ButtonEntry create(AbstractButtonWidget button, Text text) {
            return new ButtonEntry(ImmutableMap.of(button, text));
        }

        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.buttonsWithText.forEach((button, text) -> {
                button.y = y;
                button.render(matrices, mouseX, mouseY, tickDelta);
                drawTextWithShadow(matrices,textRenderer, text,x+15,y+5,0xFFFFFF);
            });
        }

        public List<? extends Element> children() {
            return buttons;
        }
    }
}
