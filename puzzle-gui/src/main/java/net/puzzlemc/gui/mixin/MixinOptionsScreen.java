package net.puzzlemc.gui.mixin;

import net.puzzlemc.gui.screen.PuzzleOptionsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(OptionsScreen.class)
public class MixinOptionsScreen extends Screen {

    protected MixinOptionsScreen(Text title) {
        super(title);
    }

    @Inject(at = @At("TAIL"),method = "init")
    public void init(CallbackInfo ci) {
        PuzzleOptionsScreen puzzleScreen = new PuzzleOptionsScreen(this);
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 155, this.height / 6 + 144 - 6, 150, 20, new TranslatableText("puzzle.screen.title").append("..."), (button) -> Objects.requireNonNull(this.client).setScreen(puzzleScreen)));
    }

}
