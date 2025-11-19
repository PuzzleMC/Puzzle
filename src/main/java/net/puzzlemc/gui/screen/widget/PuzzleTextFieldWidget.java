package net.puzzlemc.gui.screen.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class PuzzleTextFieldWidget extends EditBox {
    private final PuzzleWidget.SetTextValueAction setValueAction;
    private final PuzzleWidget.ChangeTextValueAction change;

    public PuzzleTextFieldWidget(Font font, int x, int y, int width, int height, PuzzleWidget.SetTextValueAction setValue, PuzzleWidget.ChangeTextValueAction change) {
        super(font, x, y, width, height, Component.empty());
        this.setValueAction = setValue;
        this.change = change;
        try {
            setValueAction.setTextValue(this);
        } catch (Exception e) {e.fillInStackTrace(); this.setVisible(false);}
    }
    @Override
    public void setValue(String text) {
        super.setValue(text);
        this.change.onChange(this);
        setValueAction.setTextValue(this);
    }
}
