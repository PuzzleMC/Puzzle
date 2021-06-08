package net.puzzlemc.gui.screen.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class PuzzleWidget {
    public ButtonType buttonType;
    public int min;
    public int max;
    public Text descriptionText;
    public Text buttonText;
    public TextAction buttonTextAction;
    public ButtonWidget.PressAction onPress;
    public PuzzleWidget.SaveAction onSave;

    /**
     * Puzzle Text Widget Container
     * @param descriptionText The text you want to display.
     */
    public PuzzleWidget(Text descriptionText) {
        this.buttonType = ButtonType.TEXT;
        this.descriptionText = descriptionText;
    }

    /**
     * Puzzle Button Widget Container
     * @param descriptionText Tells the user what the option does.
     * @param getTitle Function to set the text on the button.
     * @param onPress Function to call when the user presses the button.
     */
    public PuzzleWidget(Text descriptionText, PuzzleWidget.TextAction getTitle, ButtonWidget.PressAction onPress) {
        this.buttonType = ButtonType.BUTTON;
        this.descriptionText = descriptionText;
        this.buttonTextAction = getTitle;
        this.onPress = onPress;
    }
    /**
     * Puzzle Slider Widget Container (WIP - Doesn't work)
     */
    public PuzzleWidget(int min, int max, Text descriptionText, TranslatableText buttonText) {
        this.buttonType = ButtonType.SLIDER;
        this.min = min;
        this.max = max;
        this.descriptionText = descriptionText;
        this.buttonText = buttonText;
    }
    /**
     * Puzzle Text Field Widget Container (WIP - Doesn't work)
     */
    public PuzzleWidget(Text descriptionText, Text buttonText) {
        this.buttonType = ButtonType.TEXT_FIELD;
        this.descriptionText = descriptionText;
        this.buttonText = buttonText;
    }
    @Environment(EnvType.CLIENT)
    public interface SaveAction {
        void onSave(ClickableWidget button);
    }
    @Environment(EnvType.CLIENT)
    public interface TextAction {
        void setTitle(ClickableWidget button);
    }
}
