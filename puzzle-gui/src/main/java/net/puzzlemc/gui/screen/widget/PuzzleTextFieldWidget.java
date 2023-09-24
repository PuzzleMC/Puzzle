package net.puzzlemc.gui.screen.widget;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class PuzzleTextFieldWidget extends TextFieldWidget {
    private final PuzzleWidget.SetTextValueAction setValueAction;
    private final PuzzleWidget.ChangeTextValueAction change;

    public PuzzleTextFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, PuzzleWidget.SetTextValueAction setValue, PuzzleWidget.ChangeTextValueAction change) {
        super(textRenderer, x, y, width, height, Text.of(""));
        this.setValueAction = setValue;
        this.change = change;
        try {
            setValueAction.setTextValue(this);
        } catch (Exception e) {e.printStackTrace(); this.setVisible(false);}
    }
    @Override
    public void write(String text) {
        super.write(text);
        this.change.onChange(this);
        setValueAction.setTextValue(this);
    }
}
