package net.puzzlemc.gui.screen.widget;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import net.puzzlemc.gui.screen.PuzzleOptionsScreen;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import static net.puzzlemc.core.PuzzleCore.LOGGER;

@Environment(EnvType.CLIENT)
public class PuzzleOptionListWidget extends MidnightConfig.MidnightConfigListWidget {
    TextRenderer textRenderer;

    public PuzzleOptionListWidget(MinecraftClient minecraftClient, int i, int j, int k, int l) {
        super(minecraftClient, i, j, k, l);
        this.centerListVertically = false;
        textRenderer = minecraftClient.textRenderer;
    }

    public void addAll(List<PuzzleWidget> buttons) {
        int buttonX = this.width - 160;
        for (PuzzleWidget button : buttons) {
            if (button.buttonType == ButtonType.TEXT)
                this.addButton(List.of(), Text.literal("").append(button.descriptionText).formatted(Formatting.BOLD));
            else if (button.buttonType == ButtonType.BUTTON)
                this.addButton(List.of(new PuzzleButtonWidget(buttonX, 0, 150, 20, button.buttonTextAction, button.onPress)), button.descriptionText);
            else if (button.buttonType == ButtonType.SLIDER)
                this.addButton(List.of(new PuzzleSliderWidget(button.min, button.max, buttonX, 0, 150, 20, button.defaultSliderValue.getAsInt(), button.buttonTextAction, button.changeSliderValue)), button.descriptionText);
            else if (button.buttonType == ButtonType.TEXT_FIELD)
                this.addButton(List.of(new PuzzleTextFieldWidget(textRenderer, buttonX, 0, 150, 20, button.setTextValue, button.changeTextValue)), button.descriptionText);
            else
                LOGGER.warn("Button {} is missing the buttonType variable. This shouldn't happen!", button);
        }
    }
    public void addButton(List<ClickableWidget> buttons, Text text) {
        var info = new MidnightConfig.EntryInfo(null, "puzzle-gui");
        info.comment = new MidnightConfig.Comment() {
            @Override
            public boolean centered() {
                return buttons.isEmpty();
            }
            public Class<? extends Annotation> annotationType() {return null;}
            public String category() {return "";}
            public String name() {return "";}
            public String url() {return "";}
            public String requiredMod() {return "";}
        };
        var entry = new MidnightConfig.ButtonEntry(buttons, text, info);
        this.addEntry(entry);
    }
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);
        MidnightConfig.ButtonEntry e = this.getHoveredEntry();
        if (client.currentScreen instanceof PuzzleOptionsScreen page && e != null && !e.buttons.isEmpty() &&
                e.text.getContent() instanceof TranslatableTextContent content) {
            ClickableWidget button = e.buttons.getFirst();
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
                if (!button.isMouseOver(mouseX, mouseY)) {
                    context.drawTooltip(textRenderer, list, button.getX(), button.getY() + (button.getHeight() * 2));
                }
                else context.drawTooltip(textRenderer, list, mouseX, mouseY);
            }
        }
    }

    @Override
    public MidnightConfig.ButtonEntry getHoveredEntry() {
        return super.getHoveredEntry();
    }
}
