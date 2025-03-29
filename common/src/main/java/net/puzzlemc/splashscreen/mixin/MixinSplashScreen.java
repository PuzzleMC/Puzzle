package net.puzzlemc.splashscreen.mixin;

import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.puzzlemc.core.config.PuzzleConfig;
import net.puzzlemc.splashscreen.PuzzleSplashScreen;
import net.puzzlemc.splashscreen.util.BlendFactors;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.function.IntSupplier;

import static net.puzzlemc.core.PuzzleCore.LOGGER;
import static net.puzzlemc.splashscreen.PuzzleSplashScreen.BACKGROUND;

@Mixin(value = SplashOverlay.class, priority = 2000)
public abstract class MixinSplashScreen extends Overlay {
    @Shadow @Final public static Identifier LOGO;
    @Shadow private long reloadCompleteTime;
    @Shadow @Final private MinecraftClient client;
    @Shadow @Final private boolean reloading;
    @Shadow private long reloadStartTime;
    @Shadow
    private static int withAlpha(int color, int alpha) {
        return 0;
    }

    @Inject(method = "init", at = @At("TAIL"))
    private static void puzzle$initSplashscreen(TextureManager textureManager, CallbackInfo ci) { // Load our custom textures at game start //
        if (PuzzleConfig.resourcepackSplashScreen) {
            if (PuzzleSplashScreen.LOGO_TEXTURE.toFile().exists()) {
                textureManager.registerTexture(LOGO, new PuzzleSplashScreen.DynamicLogoTexture());
            }
            if (PuzzleSplashScreen.BACKGROUND_TEXTURE.toFile().exists()) {
                try {
                    InputStream input = new FileInputStream(String.valueOf(PuzzleSplashScreen.BACKGROUND_TEXTURE));
                    textureManager.registerTexture(BACKGROUND, new NativeImageBackedTexture(() -> "splash_screen_background", NativeImage.read(input)));
                } catch (IOException ignored) {}
            }
        }
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/function/IntSupplier;getAsInt()I"))
    private int puzzle$modifyBackground(IntSupplier instance) { // Set the Progress Bar Frame Color to our configured value //
        return (!PuzzleConfig.resourcepackSplashScreen || PuzzleConfig.progressBarBackgroundColor == 15675965) ? instance.getAsInt() : PuzzleConfig.backgroundColor | 255 << 24;
    }
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/ColorHelper;getWhite(F)I", shift = At.Shift.AFTER))
    private void puzzle$betterBlend(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (PuzzleConfig.resourcepackSplashScreen) {
            if (PuzzleConfig.disableBlend) GlStateManager._disableBlend();
            else if (PuzzleConfig.customBlendFunction.size() == 4) {
                try {
                    GlStateManager._blendFuncSeparate(
                            BlendFactors.SrcFactor.valueFromString(PuzzleConfig.customBlendFunction.get(0)),
                            BlendFactors.DstFactor.valueFromString(PuzzleConfig.customBlendFunction.get(1)),
                            BlendFactors.SrcFactor.valueFromString(PuzzleConfig.customBlendFunction.get(2)),
                            BlendFactors.DstFactor.valueFromString(PuzzleConfig.customBlendFunction.get(3)));
                } catch (Exception e) {
                    LOGGER.error("Incorrect blend function defined in color.properties: {}{}", PuzzleConfig.customBlendFunction, e.getMessage());
                }
            }
        }
    }
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowWidth()I", ordinal = 2))
    private void puzzle$renderSplashBackground(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (Files.exists(PuzzleSplashScreen.BACKGROUND_TEXTURE) && PuzzleConfig.resourcepackSplashScreen) {
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();
            long l = Util.getMeasuringTimeMs();
            float f = this.reloadCompleteTime > -1L ? (float)(l - this.reloadCompleteTime) / 1000.0F : -1.0F;
            float g = this.reloadStartTime> -1L ? (float)(l - this.reloadStartTime) / 500.0F : -1.0F;
            float s;
            if (f >= 1.0F) s = 1.0F - MathHelper.clamp(f - 1.0F, 0.0F, 1.0F);
            else if (reloading) s = MathHelper.clamp(g, 0.0F, 1.0F);
            else s = 1.0F;
            //GlStateManager._enableBlend();
            //GlStateManager.blendEquation(32774);
            //GlStateManager._defaultBlendFunc();
            context.getMatrices().translate(0, 0, 1f);
            context.drawTexture(RenderLayer::getGuiTextured, BACKGROUND, 0, 0, 0, 0, width, height, width, height, ColorHelper.getWhite(s));
            //RenderSystem._defaultBlendFunc();
            //RenderSystem._disableBlend();
        }
    }
    @Inject(method = "renderProgressBar", at = @At("HEAD"))
    private void puzzle$addProgressBarBackground(DrawContext context, int minX, int minY, int maxX, int maxY, float opacity, CallbackInfo ci) {
        context.getMatrices().translate(0, 0, 1f);
        if (!PuzzleConfig.resourcepackSplashScreen || PuzzleConfig.progressBarBackgroundColor == 15675965) return;
        long l = Util.getMeasuringTimeMs();
        float f = this.reloadCompleteTime > -1L ? (float)(l - this.reloadCompleteTime) / 1000.0F : -1.0F;
        int m = MathHelper.ceil((1.0F - MathHelper.clamp(f - 1.0F, 0.0F, 1.0F)) * 255.0F);
        //RenderSystem.disableBlend();
        context.fill(minX, minY, maxX, maxY, withAlpha(PuzzleConfig.progressBarBackgroundColor, m));
    }

    @ModifyArg(method = "renderProgressBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"), index = 4)
    private int puzzle$modifyProgressFrame(int color) { // Set the Progress Bar Frame Color to our configured value //
        return (!PuzzleConfig.resourcepackSplashScreen || PuzzleConfig.progressFrameColor == 16777215) ? color : PuzzleConfig.progressFrameColor | 255 << 24;
    }
    @ModifyArg(method = "renderProgressBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V", ordinal = 0), index = 4)
    private int puzzle$modifyProgressColor(int color) { // Set the Progress Bar Color to our configured value //
        return (!PuzzleConfig.resourcepackSplashScreen || PuzzleConfig.progressBarColor == 16777215) ? color : PuzzleConfig.progressBarColor | 255 << 24;
    }
}
