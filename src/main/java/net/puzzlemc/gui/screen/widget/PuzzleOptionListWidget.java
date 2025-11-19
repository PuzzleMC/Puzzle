package net.puzzlemc.gui.screen.widget;

import eu.midnightdust.lib.config.ButtonEntry;
import eu.midnightdust.lib.config.EntryInfo;
import eu.midnightdust.lib.config.MidnightConfig;
import eu.midnightdust.lib.config.MidnightConfigListWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.puzzlemc.gui.screen.PuzzleOptionsScreen;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import static net.puzzlemc.core.PuzzleCore.LOGGER;

public class PuzzleOptionListWidget extends MidnightConfigListWidget {
    Font textRenderer;

    public PuzzleOptionListWidget(Minecraft minecraftClient, int i, int j, int k, int l) {
        super(minecraftClient, i, j, k, l);
        this.centerListVertically = false;
        textRenderer = minecraftClient.font;
    }

    public void addAll(List<PuzzleWidget> buttons) {
        int buttonX = this.width - 160;
        for (PuzzleWidget button : buttons) {
            try {
                if (button.buttonType == ButtonType.TEXT)
                    this.addButton(List.of(), Component.literal("").append(button.descriptionText).withStyle(ChatFormatting.BOLD));
                else if (button.buttonType == ButtonType.BUTTON)
                    this.addButton(List.of(new PuzzleButtonWidget(buttonX, 0, 150, 20, button.buttonTextAction, button.onPress)), button.descriptionText);
                else if (button.buttonType == ButtonType.SLIDER)
                    this.addButton(List.of(new PuzzleSliderWidget(button.min, button.max, buttonX, 0, 150, 20, button.defaultSliderValue.getAsInt(), button.buttonTextAction, button.changeSliderValue)), button.descriptionText);
                else if (button.buttonType == ButtonType.TEXT_FIELD)
                    this.addButton(List.of(new PuzzleTextFieldWidget(textRenderer, buttonX, 0, 150, 20, button.setTextValue, button.changeTextValue)), button.descriptionText);
                else
                    LOGGER.warn("Button {} is missing the buttonType variable. This shouldn't happen!", button);
            }
            catch (Exception e) {
                LOGGER.error("Failed to add button {}. Likely caused by an update of the specific mod.", button.descriptionText);
            }

        }
    }
    public void addButton(List<AbstractWidget> buttons, Component text) {
        EntryInfo info = new EntryInfo(null, "puzzle");
        if (buttons.isEmpty()) info.comment = new MidnightConfig.Comment(){
            public Class<? extends Annotation> annotationType() {return null;}
            public boolean centered() {return true;}
            public String category() {return "";}
            public String name() {return "";}
            public String url() {return "";}
            public String requiredMod() {return "";}
        };
        var entry = new ButtonEntry(buttons, text, info);
        this.addEntry(entry);
    }
    public void /*? if >= 1.21 {*/ renderWidget /*?} else {*/ /*renderList *//*?}*/(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super./*? if >= 1.21 {*/ renderWidget /*?} else {*/ /*renderList *//*?}*/(context, mouseX, mouseY, delta);
        ButtonEntry e = this.getHovered();
        if (minecraft.screen instanceof PuzzleOptionsScreen page && e != null && !e.buttons.isEmpty() &&
                e.text.getContents() instanceof TranslatableContents content) {
            AbstractWidget button = e.buttons.getFirst();
            String key = null;
            if (I18n.exists(content.getKey() + ".tooltip")) key = content.getKey() + ".tooltip";
            else if (I18n.exists(content.getKey() + ".desc")) key = content.getKey() + ".desc";
            if (key == null && content.getKey().endsWith(".title")) {
                String strippedContent = content.getKey().substring(0, content.getKey().length()-6);
                if (I18n.exists(strippedContent + ".tooltip")) key = strippedContent + ".tooltip";
                else if (I18n.exists(strippedContent + ".desc")) key = strippedContent + ".desc";
            }

            if (key != null) {
                List<Component> list = new ArrayList<>();
                for (String str : I18n.get(key).split("\n"))
                    list.add(Component.literal(str));
                page.tooltip = list;
                if (!button.isMouseOver(mouseX, mouseY)) {
                    context.setComponentTooltipForNextFrame(textRenderer, list, button.getX(), button.getY() + (button.getHeight() * 2));
                }
                else context.setComponentTooltipForNextFrame(textRenderer, list, mouseX, mouseY);
            }
        }
    }

    @Override
    public ButtonEntry getHovered() {
        return super.getHovered();
    }
}