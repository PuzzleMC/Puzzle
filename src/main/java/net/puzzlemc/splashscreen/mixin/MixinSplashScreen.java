package net.puzzlemc.splashscreen.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
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
import java.nio.file.Files;
import java.util.function.Function;
import java.util.function.IntSupplier;

//? if >= 1.21.8 {
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.RenderPipelines;
//?} else {
/*import net.minecraft.client.renderer.RenderType;
*///?}

import static net.puzzlemc.splashscreen.PuzzleSplashScreen.BACKGROUND;

@Mixin(value = LoadingOverlay.class, priority = 2000)
public abstract class MixinSplashScreen extends Overlay {
    @Shadow @Final public static ResourceLocation MOJANG_STUDIOS_LOGO_LOCATION;
    @Shadow private long fadeInStart;
    @Shadow @Final private Minecraft minecraft;
    @Shadow @Final private boolean fadeIn;
    @Shadow private long fadeOutStart;
    @Shadow
    private static int replaceAlpha(int color, int alpha) {
        return 0;
    }

    @Inject(method = "registerTextures", at = @At("TAIL"))
    private static void puzzle$initSplashscreen(TextureManager textureManager, CallbackInfo ci) { // Load our custom textures at game start //
        if (PuzzleConfig.resourcepackSplashScreen) {
            if (PuzzleSplashScreen.LOGO_TEXTURE.toFile().exists()) {
                textureManager.registerAndLoad(MOJANG_STUDIOS_LOGO_LOCATION, new PuzzleSplashScreen.DynamicLogoTexture());
            }
            if (PuzzleSplashScreen.BACKGROUND_TEXTURE.toFile().exists()) {
                try {
                    InputStream input = new FileInputStream(String.valueOf(PuzzleSplashScreen.BACKGROUND_TEXTURE));
                    textureManager.register(BACKGROUND, new DynamicTexture(() -> "splash_screen_background", NativeImage.read(input)));
                } catch (IOException ignored) {}
            }
        }
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/function/IntSupplier;getAsInt()I"))
    private int puzzle$modifyBackground(IntSupplier instance) { // Set the Progress Bar Frame Color to our configured value //
        return (!PuzzleConfig.resourcepackSplashScreen || PuzzleConfig.progressBarBackgroundColor == 15675965) ? instance.getAsInt() : PuzzleConfig.backgroundColor | 255 << 24;
    }

    //? if >= 1.21.8 {
    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/ResourceLocation;IIFFIIIIIII)V"))
    private void puzzle$modifyRenderLayer(GuiGraphics context, RenderPipeline pipeline, ResourceLocation sprite, int x, int y, float u, float v, int width, int height, int regionWidth, int regionHeight, int textureWidth, int textureHeight, int color, Operation<Void> original) {
        if (PuzzleConfig.resourcepackSplashScreen)
            context.blit(PuzzleSplashScreen.CUSTOM_LOGO_PIPELINE, sprite, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, color);
        else context.blit(pipeline, sprite, x, y, u, v, width, height, textureWidth, textureHeight, color);
    }
    //?} else {
    /*@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Ljava/util/function/Function;Lnet/minecraft/resources/ResourceLocation;IIFFIIIIIII)V"))
    private void puzzle$modifyRenderLayer(GuiGraphics context, Function<ResourceLocation, RenderType> renderType, ResourceLocation sprite, int x, int y, float u, float v, int width, int height, int regionWidth, int regionHeight, int textureWidth, int textureHeight, int color, Operation<Void> original) {
        if (PuzzleConfig.resourcepackSplashScreen)
            context.blit(id -> PuzzleSplashScreen.CUSTOM_LOGO_LAYER, sprite, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, color);
        else context.blit(renderType, sprite, x, y, u, v, width, height, textureWidth, textureHeight, color);
    }
    *///?}

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;guiWidth()I", ordinal = 2))
    private void puzzle$renderSplashBackground(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (Files.exists(PuzzleSplashScreen.BACKGROUND_TEXTURE) && PuzzleConfig.resourcepackSplashScreen) {
            int width = minecraft.getWindow().getGuiScaledWidth();
            int height = minecraft.getWindow().getGuiScaledHeight();
            long l = Util.getMillis();
            float f = this.fadeOutStart > -1L ? (float)(l - this.fadeOutStart) / 1000.0F : -1.0F;
            float g = this.fadeInStart> -1L ? (float)(l - this.fadeInStart) / 500.0F : -1.0F;
            float s;
            if (f >= 1.0F) s = 1.0F - Mth.clamp(f - 1.0F, 0.0F, 1.0F);
            else if (fadeIn) s = Mth.clamp(g, 0.0F, 1.0F);
            else s = 1.0F;
            //? if >= 1.21.8 {
            context.blit(RenderPipelines.GUI_TEXTURED
            //?} else {
            /*context.blit(RenderType::guiTextured
            *///?}
            , BACKGROUND, 0, 0, 0, 0, width, height, width, height, ARGB.white(s));
        }
    }

    @Inject(method = "drawProgressBar", at = @At("HEAD"))
    private void puzzle$addProgressBarBackground(GuiGraphics context, int minX, int minY, int maxX, int maxY, float opacity, CallbackInfo ci) {
        if (!PuzzleConfig.resourcepackSplashScreen || PuzzleConfig.progressBarBackgroundColor == 15675965) return;
        long l = Util.getMillis();
        float f = this.fadeOutStart > -1L ? (float)(l - this.fadeOutStart) / 1000.0F : -1.0F;
        int m = Mth.ceil((1.0F - Mth.clamp(f - 1.0F, 0.0F, 1.0F)) * 255.0F);
        context.fill(minX, minY, maxX, maxY, replaceAlpha(PuzzleConfig.progressBarBackgroundColor, m));
    }

    @ModifyArg(method = "drawProgressBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V"), index = 4)
    private int puzzle$modifyProgressFrame(int color) { // Set the Progress Bar Frame Color to our configured value //
        return (!PuzzleConfig.resourcepackSplashScreen || PuzzleConfig.progressFrameColor == 16777215) ? color : PuzzleConfig.progressFrameColor | 255 << 24;
    }
    @ModifyArg(method = "drawProgressBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V", ordinal = 0), index = 4)
    private int puzzle$modifyProgressColor(int color) { // Set the Progress Bar Color to our configured value //
        return (!PuzzleConfig.resourcepackSplashScreen || PuzzleConfig.progressBarColor == 16777215) ? color : PuzzleConfig.progressBarColor | 255 << 24;
    }
}
