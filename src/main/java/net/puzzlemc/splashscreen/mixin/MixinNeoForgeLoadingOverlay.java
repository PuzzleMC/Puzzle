package net.puzzlemc.splashscreen.mixin;

//? neoforge {
/*import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.neoforged.fml.earlydisplay.DisplayWindow;
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

    @Inject(method = "<init>", at = @At("TAIL"))
    private void puzzle$initTexturesNeoforge(Minecraft mc, ReloadInstance reloader, Consumer<Optional<Throwable>> errorConsumer, DisplayWindow displayWindow, CallbackInfo ci) {
        LoadingOverlay.registerTextures(/^? if >= 1.21.4 {^/ /^mc.getTextureManager() ^//^?} else {^/ mc /^?}^/);
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true) // Replaces the NeoForge loading screen in later stages with the (customized) vanilla version
    private void puzzle$redirectNeoForgeLoading(GuiGraphics context, int mouseX, int mouseY, float tickDelta, CallbackInfo ci) {
        if (PuzzleConfig.resourcepackSplashScreen && PuzzleConfig.hasCustomSplashScreen) {
            super.render(context, mouseX, mouseY, tickDelta);
            ci.cancel();
        }
    }
}
*///?}
