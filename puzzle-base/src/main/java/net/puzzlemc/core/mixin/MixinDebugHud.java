package net.puzzlemc.core.mixin;

import com.mojang.blaze3d.platform.GlDebugInfo;
import net.puzzlemc.core.PuzzleCore;
import net.puzzlemc.core.config.PuzzleConfig;
import net.puzzlemc.core.util.UpdateChecker;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DebugHud.class)
public abstract class MixinDebugHud extends DrawableHelper {
    @Inject(at = @At("RETURN"), method = "getRightText", cancellable = true)
    private void puzzle$getRightText(CallbackInfoReturnable<List<String>> cir) {
        if (PuzzleConfig.showPuzzleInfo) {
            List<String> entries = cir.getReturnValue();
            String message;
            if (UpdateChecker.isUpToDate) {
                message = "Puzzle is up to date (" + PuzzleCore.version + ")";
            } else {
                message = "Puzzle is outdated (" + PuzzleCore.version + " -> " + UpdateChecker.latestVersion + ")";
            }
            for (int i = 0; i < entries.size(); i++) {
                String str = entries.get(i);

                if (str.startsWith(GlDebugInfo.getVersion())) {
                    entries.add(i + 1, message);
                    break;
                }
            }
        }
    }
}
