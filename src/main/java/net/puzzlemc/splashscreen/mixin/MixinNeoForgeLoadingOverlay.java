package net.puzzlemc.splashscreen.mixin;

//? neoforge || forge {
/*import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.neoforged.neoforge.client.loading.NeoForgeLoadingOverlay;
import net.puzzlemc.core.config.PuzzleConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(NeoForgeLoadingOverlay.class)
public class MixinNeoForgeLoadingOverlay extends LoadingOverlay {
    public MixinNeoForgeLoadingOverlay(Minecraft arg, ReloadInstance arg2, Consumer<Optional<Throwable>> consumer, boolean bl) {
        super(arg, arg2, consumer, bl);
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true) // Replaces the NeoForge loading screen in later stages with the (customized) vanilla version
    private void redirectNeoForgeLoading(GuiGraphics context, int mouseX, int mouseY, float tickDelta, CallbackInfo ci) {
        if (PuzzleConfig.resourcepackSplashScreen && PuzzleConfig.hasCustomSplashScreen) {
            super.render(context, mouseX, mouseY, tickDelta);
            ci.cancel();
        }
    }
}
*///?}
