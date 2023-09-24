package net.puzzlemc.gui.screen.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.puzzlemc.gui.screen.PuzzleOptionsScreen;
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
                this.addButton(new PuzzleSliderWidget(button.min, button.max, this.width / 2 + 25, 0, 150, 20, button.defaultSliderValue.getAsInt(), button.buttonTextAction, button.changeSliderValue), button.descriptionText);
            } else if (button.buttonType == ButtonType.TEXT_FIELD) {
                this.addButton(new PuzzleTextFieldWidget(textRenderer, this.width / 2 + 25, 0, 150, 20, button.setTextValue, button.changeTextValue), button.descriptionText);
            } else
                LogManager.getLogger("Puzzle").warn("Button " + button + " is missing the buttonType variable. This shouldn't happen!");
        }
    }

    public void clear() {
        super.clearEntries();
    }
    public int getRowWidth() {
        return 400;
    }

    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + 60;
    }

    @Override
    public ButtonEntry getHoveredEntry() {
        return super.getHoveredEntry();
    }

    public static class ButtonEntry extends ElementListWidget.Entry<ButtonEntry> {
        private static final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        private List<ClickableWidget> buttons = new ArrayList<>();
        public final ClickableWidget button;
        public final Text text;
        private MinecraftClient client = MinecraftClient.getInstance();

        private ButtonEntry(ClickableWidget button, Text text) {
            if (button != null)
                this.buttons.add(button);
            this.button = button;
            this.text = text;
        }

        public static ButtonEntry create(ClickableWidget button, Text text) {
            return new ButtonEntry(button, text);
        }

        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            if (button != null) {
                button.setY(y);
                button.render(context, mouseX, mouseY, tickDelta);
            }
            if (button == null) context.drawCenteredTextWithShadow(textRenderer, Text.literal("－－－－－－ ").append(text).append(" －－－－－－"),x + 200,y+5,0xFFFFFF);
            else context.drawTextWithShadow(textRenderer, text,x+15,y+5,0xFFFFFF);

            if (!(client.currentScreen instanceof PuzzleOptionsScreen page)) return;
            if (button != null && (button.isMouseOver(mouseX, mouseY) || ((page.list.getHoveredEntry() == null || page.list.getHoveredEntry().button == null || !page.list.getHoveredEntry().button.isMouseOver(mouseX, mouseY)) && button.isFocused())) && text.getContent() instanceof TranslatableTextContent content) {
                String key = null;
                if (I18n.hasTranslation(content.getKey() + ".tooltip")) key = content.getKey() + ".tooltip";
                else if (I18n.hasTranslation(content.getKey() + ".desc")) key = content.getKey() + ".desc";
                if (key == null && content.getKey().endsWith(".title")) {
                    String strippedContent = content.getKey().substring(0, content.getKey().length()-6);
                    if (I18n.hasTranslation(strippedContent + ".tooltip")) key = strippedContent + ".tooltip";
                    else if (I18n.hasTranslation(strippedContent + ".desc")) key = strippedContent + ".desc";
                }

                if (key != null) {
                    List<Text> list = new ArrayList<>();
                    for (String str : I18n.translate(key).split("\n"))
                        list.add(Text.literal(str));
                    page.tooltip = list;
                }
            }
        }
        public int getY() {
            return button.getY();
        }
        public int getX() {
            return button.getX();
        }
        public int getHeight() {
            return button.getHeight();
        }
        public int getWidth() {
            return button.getWidth();
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
