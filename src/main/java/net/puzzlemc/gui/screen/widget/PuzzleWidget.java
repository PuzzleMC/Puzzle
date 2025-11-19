package net.puzzlemc.gui.screen.widget;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

import java.util.function.IntSupplier;

public class PuzzleWidget {
    public ButtonType buttonType;
    public int min;
    public int max;
    public Component descriptionText;
    public TextAction buttonTextAction;
    public Button.OnPress onPress;
    public PuzzleWidget.SetTextValueAction setTextValue;
    public IntSupplier defaultSliderValue;
    public PuzzleWidget.ChangeTextValueAction changeTextValue;
    public PuzzleWidget.ChangeSliderValueAction changeSliderValue;

    /**
     * Puzzle Text Widget Container
     * @param descriptionText The text you want to display.
     */
    public PuzzleWidget(Component descriptionText) {
        this.buttonType = ButtonType.TEXT;
        this.descriptionText = descriptionText;
    }

    /**
     * Puzzle Button Widget Container
     * @param descriptionText Tells the user what the option does.
     * @param getTitle Function to set the text on the button.
     * @param onPress Function to call when the user presses the button.
     */
    public PuzzleWidget(Component descriptionText, PuzzleWidget.TextAction getTitle, Button.OnPress onPress) {
        this.buttonType = ButtonType.BUTTON;
        this.descriptionText = descriptionText;
        this.buttonTextAction = getTitle;
        this.onPress = onPress;
    }
    /**
     * Puzzle Slider Widget Container
     */
    public PuzzleWidget(int min, int max, Component descriptionText, IntSupplier defaultSliderValue, PuzzleWidget.TextAction setTextAction, PuzzleWidget.ChangeSliderValueAction changeAction) {
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
    public PuzzleWidget(int min, int max, Component descriptionText, PuzzleWidget.SetTextValueAction setValue, ChangeTextValueAction changeAction) {
        this.buttonType = ButtonType.TEXT_FIELD;
        this.min = min;
        this.max = max;
        this.descriptionText = descriptionText;
        this.setTextValue = setValue;
        this.changeTextValue = changeAction;
    }
    public interface ChangeTextValueAction {
        void onChange(EditBox textField);
    }
    public interface ChangeSliderValueAction {
        void onChange(PuzzleSliderWidget slider);
    }
    public interface SetTextValueAction {
        void setTextValue(EditBox textField);
    }
    public interface TextAction {
        void setTitle(AbstractWidget button);
    }
}
