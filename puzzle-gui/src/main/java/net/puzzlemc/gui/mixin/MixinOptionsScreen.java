package net.puzzlemc.gui.mixin;

import eu.midnightdust.core.config.MidnightLibConfig;
import eu.midnightdust.lib.util.screen.TexturedOverlayButtonWidget;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
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
    private static final Identifier PUZZLE_ICON_TEXTURE = new Identifier(PuzzleClient.id, "textures/gui/puzzle_button.png");

    protected MixinOptionsScreen(Text title) {
        super(title);
    }

    @Inject(at = @At("HEAD"), method = "init")
    private void puzzle$init(CallbackInfo ci) {
        if (PuzzleConfig.enablePuzzleButton) {
            int i = 0;
            if (FabricLoader.getInstance().isModLoaded("lod")) i = i + 358;
            if (MidnightLibConfig.config_screen_list.equals(MidnightLibConfig.ConfigButton.FALSE)) i = i - 25;
            this.addDrawableChild(new TexturedOverlayButtonWidget(this.width / 2 - 178 + i, this.height / 6 - 12, 20, 20, 0, 0, 20, PUZZLE_ICON_TEXTURE, 32, 64, (buttonWidget) -> (Objects.requireNonNull(this.client)).setScreen(new PuzzleOptionsScreen(this)), Text.translatable("midnightlib.overview.title")));
        }
    }
}
