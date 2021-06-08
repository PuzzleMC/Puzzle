package net.puzzlemc.gui.screen.widget;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class PuzzleSliderWidget extends SliderWidget {
    private final int min;
    private final double difference;

    public PuzzleSliderWidget(int min, int max, int x, int y, int width, int height, TranslatableText label, double value) {
        super(x,y,width,height,label,value);
        this.updateMessage();
        this.min = min;
        this.difference = max - min;
    }

    @Override
    protected void updateMessage() {
        Text text = new LiteralText((int) (min + this.value * difference) + "");
        this.setMessage(new TranslatableText("label").append(": ").append(text));
    }

    @Override
    protected void applyValue() {

    }
}
