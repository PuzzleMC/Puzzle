package net.puzzlemc.splashscreen.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.puzzlemc.core.config.PuzzleConfig;
import net.puzzlemc.splashscreen.PuzzleSplashScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.IntSupplier;

@Mixin(value = SplashOverlay.class, priority = 2000)
public abstract class MixinSplashScreen extends Overlay {
    @Shadow @Final static Identifier LOGO;
    @Shadow private long reloadCompleteTime;

    @Shadow
    private static int withAlpha(int color, int alpha) {
        return 0;
    }

    @Inject(method = "init(Lnet/minecraft/client/MinecraftClient;)V", at = @At("TAIL"))
    private static void puzzle$initSplashscreen(MinecraftClient client, CallbackInfo ci) { // Load our custom textures at game start //
        if (PuzzleConfig.resourcepackSplashScreen && PuzzleSplashScreen.LOGO_TEXTURE.toFile().exists()) {
            try {
                InputStream input = new FileInputStream(String.valueOf(PuzzleSplashScreen.LOGO_TEXTURE));
                client.getTextureManager().registerTexture(LOGO, new NativeImageBackedTexture(NativeImage.read(input)));
            } catch (IOException ignored) {}
        }
    }
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/function/IntSupplier;getAsInt()I"))
    private int puzzle$modifyBackground(IntSupplier instance) { // Set the Progress Bar Frame Color to our configured value //
        return (!PuzzleConfig.resourcepackSplashScreen || PuzzleConfig.progressBarBackgroundColor == 15675965) ? instance.getAsInt() : PuzzleConfig.backgroundColor | 255 << 24;
    }
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;blendFunc(II)V", shift = At.Shift.AFTER), remap = false)
    private void puzzle$betterBlend(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (PuzzleConfig.disableBlend) RenderSystem.defaultBlendFunc();
    }
    @Inject(method = "renderProgressBar", at = @At("HEAD"))
    private void puzzle$addProgressBarBackground(MatrixStack matrices, int minX, int minY, int maxX, int maxY, float opacity, CallbackInfo ci) {
        RenderSystem.disableBlend();
        if (!PuzzleConfig.resourcepackSplashScreen || PuzzleConfig.progressBarBackgroundColor == 15675965) return;
        long l = Util.getMeasuringTimeMs();
        float f = this.reloadCompleteTime > -1L ? (float)(l - this.reloadCompleteTime) / 1000.0F : -1.0F;
        int m = MathHelper.ceil((1.0F - MathHelper.clamp(f - 1.0F, 0.0F, 1.0F)) * 255.0F);
        fill(matrices, minX, minY, maxX, maxY, withAlpha(PuzzleConfig.progressBarBackgroundColor, m));
    }
    @Inject(method = "renderProgressBar", at = @At("TAIL"))
    private void puzzle$fixProgressBarEnd(MatrixStack matrices, int minX, int minY, int maxX, int maxY, float opacity, CallbackInfo ci) { // For some reason the end of the progressbar is colored wrong
        if (!PuzzleConfig.resourcepackSplashScreen || PuzzleConfig.progressFrameColor == 16777215) return;
        long l = Util.getMeasuringTimeMs();
        float f = this.reloadCompleteTime > -1L ? (float)(l - this.reloadCompleteTime) / 1000.0F : -1.0F;
        int m = MathHelper.ceil((1.0F - MathHelper.clamp(f - 1.0F, 0.0F, 1.0F)) * 255.0F);
        fill(matrices, maxX-1, minY, maxX, maxY, withAlpha(PuzzleConfig.progressFrameColor, m));
    }

    @ModifyArg(method = "renderProgressBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/SplashOverlay;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"), index = 5)
    private int puzzle$modifyProgressFrame(int color) { // Set the Progress Bar Frame Color to our configured value //
        return (!PuzzleConfig.resourcepackSplashScreen || PuzzleConfig.progressFrameColor == 16777215) ? color : PuzzleConfig.progressFrameColor | 255 << 24;
    }
    @ModifyArg(method = "renderProgressBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/SplashOverlay;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V", ordinal = 4), index = 5)
    private int puzzle$modifyProgressColor(int color) { // Set the Progress Bar Color to our configured value //
        return (!PuzzleConfig.resourcepackSplashScreen || PuzzleConfig.progressBarColor == 16777215) ? color : PuzzleConfig.progressBarColor | 255 << 24;
    }
}
