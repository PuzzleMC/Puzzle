package net.puzzlemc.core.mixin;

import eu.midnightdust.lib.util.PlatformFunctions;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.puzzlemc.core.PuzzleCore;
import net.puzzlemc.core.config.PuzzleConfig;
import net.puzzlemc.core.util.ModMenuUtil;
import net.puzzlemc.core.util.UpdateChecker;
import net.minecraft.client.gui.screen.*;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(TitleScreen.class)
public abstract class MixinTitleScreen extends Screen {
    private final String versionText = PuzzleCore.version.replace("+", " for ");

    protected MixinTitleScreen(Text title) {
        super(title);
    }
    @Inject(at = @At("TAIL"), method = "init")
    private void puzzle$init(CallbackInfo ci) {
        int yOffset = 8;
        if (PlatformFunctions.isModLoaded("modmenu") && ModMenuUtil.hasClassicButton()) {
            yOffset += 12;
        }
        Text puzzleText;
        if (UpdateChecker.isUpToDate) {
            puzzleText = Text.literal(versionText);
        }
        else {
            puzzleText = Text.literal("").append(Text.of(versionText+" | ")).append(Text.translatable("puzzle.text.update_available"));
        }
        if (PuzzleConfig.showPuzzleInfo) {
            PressableTextWidget text = this.addDrawableChild(new PressableTextWidget(2, this.height - 12 - yOffset, this.textRenderer.getWidth(puzzleText), 10, puzzleText, (button) -> {
                if (Objects.requireNonNull(this.client).options.getChatLinksPrompt().getValue()) {
                    this.client.setScreen(new ConfirmLinkScreen(this::confirmLink, PuzzleCore.updateURL, true));
                } else {
                    Util.getOperatingSystem().open(PuzzleCore.updateURL);
                }
            }, this.textRenderer));
            if (UpdateChecker.isUpToDate) text.active = false;
        }
    }

    private void confirmLink(boolean open) {
        if (open) {
            Util.getOperatingSystem().open(PuzzleCore.updateURL);
        }
        Objects.requireNonNull(this.client).setScreen(this);
    }
}
