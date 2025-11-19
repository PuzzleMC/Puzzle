package net.puzzlemc.gui.screen.widget;


import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

public class PuzzleSliderWidget extends AbstractSliderButton {
    private final int min;
    private final int max;
    private final PuzzleWidget.TextAction setComponentAction;
    private final PuzzleWidget.ChangeSliderValueAction changeAction;

    public PuzzleSliderWidget(int min, int max, int x, int y, int width, int height, float defaultValue, PuzzleWidget.TextAction setComponentAction, PuzzleWidget.ChangeSliderValueAction changeAction) {
        super(x,y,width,height, Component.empty(),(defaultValue-min) / (max - min));

        this.min = min;
        this.max = max;

        this.setComponentAction = setComponentAction;
        this.changeAction = changeAction;
        try {
            this.updateMessage();
        } catch (Exception e) {e.fillInStackTrace(); this.visible = false;}
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
        this.setComponentAction.setTitle(this);
    }

    @Override
    protected void applyValue() {
        this.changeAction.onChange(this);
    }
}
