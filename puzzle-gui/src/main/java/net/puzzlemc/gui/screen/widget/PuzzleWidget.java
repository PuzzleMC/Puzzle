package net.puzzlemc.gui.screen.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.function.IntSupplier;

public class PuzzleWidget {
    public ButtonType buttonType;
    public int min;
    public int max;
    public Text descriptionText;
    public TextAction buttonTextAction;
    public ButtonWidget.PressAction onPress;
    public PuzzleWidget.SetTextValueAction setTextValue;
    public IntSupplier defaultSliderValue;
    public PuzzleWidget.ChangeTextValueAction changeTextValue;
    public PuzzleWidget.ChangeSliderValueAction changeSliderValue;

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
     * Puzzle Slider Widget Container
     */
    public PuzzleWidget(int min, int max, Text descriptionText, IntSupplier defaultSliderValue, PuzzleWidget.TextAction setTextAction, PuzzleWidget.ChangeSliderValueAction changeAction) {
        this.buttonType = ButtonType.SLIDER;
        this.min = min;
        this.max = max;
        this.descriptionText = descriptionText;
        this.defaultSliderValue = defaultSliderValue;
        this.buttonTextAction = setTextAction;
        this.changeSliderValue = changeAction;
    }
    /**
     * Puzzle Text Field Widget Container (WIP - Doesn't work)
     */
    public PuzzleWidget(int min, int max, Text descriptionText, PuzzleWidget.SetTextValueAction setValue, ChangeTextValueAction changeAction) {
        this.buttonType = ButtonType.TEXT_FIELD;
        this.min = min;
        this.max = max;
        this.descriptionText = descriptionText;
        this.setTextValue = setValue;
        this.changeTextValue = changeAction;
    }
    @Environment(EnvType.CLIENT)
    public interface ChangeTextValueAction {
        void onChange(TextFieldWidget textField);
    }
    @Environment(EnvType.CLIENT)
    public interface ChangeSliderValueAction {
        void onChange(PuzzleSliderWidget slider);
    }
    @Environment(EnvType.CLIENT)
    public interface SetTextValueAction {
        void setTextValue(TextFieldWidget textField);
    }
    @Environment(EnvType.CLIENT)
    public interface TextAction {
        void setTitle(ClickableWidget button);
    }
}
