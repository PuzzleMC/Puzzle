package net.puzzlemc.gui.mixin;

import eu.midnightdust.core.config.MidnightLibConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.widget.TextIconButtonWidget;
import net.puzzlemc.core.config.PuzzleConfig;
import net.puzzlemc.gui.PuzzleClient;
import net.puzzlemc.gui.screen.PuzzleOptionsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(OptionsScreen.class)
public abstract class MixinOptionsScreen extends Screen {
    protected MixinOptionsScreen(Text title) {
        super(title);
    }

    @Inject(at = @At("HEAD"), method = "init")
    private void puzzle$init(CallbackInfo ci) {
        if (PuzzleConfig.enablePuzzleButton) {
            int i = 0;
            if (FabricLoader.getInstance().isModLoaded("lod")) i = i + 358;
            if (MidnightLibConfig.config_screen_list.equals(MidnightLibConfig.ConfigButton.FALSE)) i = i - 25;
            TextIconButtonWidget iconButton = TextIconButtonWidget.builder(Text.translatable("puzzle.screen.title"), (buttonWidget) -> (Objects.requireNonNull(this.client)).setScreen(new PuzzleOptionsScreen(this)), true).dimension(20, 20).texture(PuzzleClient.PUZZLE_BUTTON, 20, 20).build();
            iconButton.setPosition(this.width / 2 - 178 + i, this.height / 6 - 12);
            this.addDrawableChild(iconButton);
        }
    }
}
