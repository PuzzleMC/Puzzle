package net.puzzlemc.gui.screen.widget;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class PuzzleSliderWidget extends SliderWidget {
    private final int min;
    private final int max;
    private final PuzzleWidget.TextAction setTextAction;
    private final PuzzleWidget.ChangeSliderValueAction changeAction;

    public PuzzleSliderWidget(int min, int max, int x, int y, int width, int height, float defaultValue, PuzzleWidget.TextAction setTextAction, PuzzleWidget.ChangeSliderValueAction changeAction) {
        super(x,y,width,height,Text.of(""),(defaultValue-min) / (max - min));

        this.min = min;
        this.max = max;

        this.setTextAction = setTextAction;
        this.changeAction = changeAction;
        try {
            this.updateMessage();
        } catch (Exception e) {e.printStackTrace(); this.visible = false;}
    }
    public int getInt() {
        int difference = max - min;
        int r = (int) (value * difference);
        return r + min;
    }
    public void setInt(int v) {
        value = value / v - value * min;
    }

    @Override
    protected void updateMessage() {
        this.setTextAction.setTitle(this);
    }

    @Override
    protected void applyValue() {
        this.changeAction.onChange(this);
    }
}
