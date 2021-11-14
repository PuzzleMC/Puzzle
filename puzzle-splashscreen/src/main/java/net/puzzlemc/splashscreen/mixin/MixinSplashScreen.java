package net.puzzlemc.splashscreen.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Mixin(value = SplashOverlay.class, priority = 2000)
public abstract class MixinSplashScreen extends Overlay {
    @Shadow @Final static Identifier LOGO;
    @Shadow private long reloadCompleteTime;

    @Shadow @Final private MinecraftClient client;

    @Shadow private long reloadStartTime;

    @Inject(method = "init(Lnet/minecraft/client/MinecraftClient;)V", at = @At("TAIL"))
    private static void init(MinecraftClient client, CallbackInfo ci) { // Load our custom textures at game start //
        if (PuzzleConfig.resourcepackSplashScreen && PuzzleSplashScreen.LOGO_TEXTURE.toFile().exists()) {
            try {
                InputStream input = new FileInputStream(String.valueOf(PuzzleSplashScreen.LOGO_TEXTURE));
                client.getTextureManager().registerTexture(LOGO, new NativeImageBackedTexture(NativeImage.read(input)));
            } catch (IOException ignored) {}
        }
    }
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/SplashOverlay;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V", shift = At.Shift.AFTER, ordinal = 0))
    private void modifyBackgroundColor(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) { // Set the Background Color to our configured value //
        long l = Util.getMeasuringTimeMs();
        float f = this.reloadCompleteTime > -1L ? (float)(l - this.reloadCompleteTime) / 1000.0F : -1.0F;
        int m = MathHelper.ceil((1.0F - MathHelper.clamp(f - 1.0F, 0.0F, 1.0F)) * 255.0F);
        if (PuzzleConfig.resourcepackSplashScreen && PuzzleConfig.backgroundColor != 15675965)
        fill(matrices, 0, 0, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight(), withAlpha(PuzzleConfig.backgroundColor, m));
    }
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/SplashOverlay;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V", shift = At.Shift.AFTER, ordinal = 1))
    private void modifyBackgroundColor2(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) { // Set the Background Color to our configured value //
        long l = Util.getMeasuringTimeMs();
        float g = this.reloadStartTime > -1L ? (float)(l - this.reloadStartTime) / 500.0F : -1.0F;
        int m = MathHelper.ceil(MathHelper.clamp((double)g, 0.15D, 1.0D) * 255.0D);
        if (PuzzleConfig.resourcepackSplashScreen && PuzzleConfig.backgroundColor != 15675965)
            fill(matrices, 0, 0, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight(), withAlpha(PuzzleConfig.backgroundColor, m));
    }
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;_clear(IZ)V", shift = At.Shift.AFTER))
    private void modifyBackgroundColor3(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) { // Set the Background Color to our configured value //
        if (PuzzleConfig.resourcepackSplashScreen && PuzzleConfig.backgroundColor != 15675965) {
            int m = PuzzleConfig.backgroundColor;
            float p = (float) (m >> 16 & 255) / 255.0F;
            float q = (float) (m >> 8 & 255) / 255.0F;
            float r = (float) (m & 255) / 255.0F;
            GlStateManager._clearColor(p, q, r, 1.0F);
            GlStateManager._clear(16384, MinecraftClient.IS_SYSTEM_MAC);
        }
    }

    @ModifyArg(method = "renderProgressBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/SplashOverlay;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"), index = 5)
    private int modifyProgressFrame(int color) { // Set the Progress Bar Frame Color to our configured value //
        return PuzzleConfig.progressFrameColor | 255 << 24;
    }
    @ModifyArg(method = "renderProgressBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/SplashOverlay;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V", ordinal = 4), index = 5)
    private int modifyProgressColor(int color) { // Set the Progress Bar Color to our configured value //
        return PuzzleConfig.progressBarColor | 255 << 24;
    }
    private static int withAlpha(int color, int alpha) {
        return color & 16777215 | alpha << 24;
    }

}
