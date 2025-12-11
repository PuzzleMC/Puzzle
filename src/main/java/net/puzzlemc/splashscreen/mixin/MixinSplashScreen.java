package net.puzzlemc.splashscreen.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
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
import java.util.function.IntSupplier;

//? if >= 1.21.8 {
import net.minecraft.util.ARGB;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.RenderPipelines;
//?} else if >= 1.21.5 {
/*import net.minecraft.util.ARGB;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.RenderType;
import java.util.function.Function;
*///?} else {
/*import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.GameRenderer;

import static net.puzzlemc.core.PuzzleCore.LOGGER;
*///?}

import static net.puzzlemc.splashscreen.PuzzleSplashScreen.BACKGROUND;
//? if >= 1.21.5
import static net.minecraft.client.gui.screens.LoadingOverlay.MOJANG_STUDIOS_LOGO_LOCATION;

@Mixin(value = LoadingOverlay.class, priority = 2000)
public abstract class MixinSplashScreen extends Overlay {
    @Shadow private long fadeInStart;
    @Shadow @Final private Minecraft minecraft;
    @Shadow @Final private boolean fadeIn;
    @Shadow private long fadeOutStart;
    @Shadow
    private static int replaceAlpha(int color, int alpha) {
        return 0;
    }
    //? if < 1.21.5
    /*@Shadow @Final static Identifier MOJANG_STUDIOS_LOGO_LOCATION;*/

    @Inject(method = "registerTextures", at = @At("TAIL")) // Load our custom textures at game start //
    //? if >= 1.21.5 {
    private static void puzzle$initSplashscreen(TextureManager textureManager, CallbackInfo ci) {
    //?} else {
    /*private static void puzzle$initSplashscreen(Minecraft client, CallbackInfo ci) {
        TextureManager textureManager = client.getTextureManager();
        *///?}
        if (PuzzleConfig.resourcepackSplashScreen) {
            if (PuzzleSplashScreen.LOGO_TEXTURE.toFile().exists()) {
                textureManager./*? if >= 1.21.5 {*/ registerAndLoad /*?} else {*//*register*//*?}*/(MOJANG_STUDIOS_LOGO_LOCATION, new PuzzleSplashScreen.DynamicLogoTexture());
            }
            if (PuzzleSplashScreen.BACKGROUND_TEXTURE.toFile().exists()) {
                try {
                    InputStream input = new FileInputStream(String.valueOf(PuzzleSplashScreen.BACKGROUND_TEXTURE));
                    textureManager.register(BACKGROUND, new DynamicTexture(/*? if >= 1.21.5 {*/ () -> "splash_screen_background", /*?}*/ NativeImage.read(input)));
                } catch (IOException ignored) {}
            }
        }
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/function/IntSupplier;getAsInt()I"))
    private int puzzle$modifyBackground(IntSupplier instance) { // Set the Progress Bar Frame Color to our configured value //
        return (!PuzzleConfig.resourcepackSplashScreen || PuzzleConfig.progressBarBackgroundColor == 15675965) ? instance.getAsInt() : PuzzleConfig.backgroundColor | 255 << 24;
    }

    //? if >= 1.21.8 {
    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIIIIII)V"))
    private void puzzle$modifyRenderLayer(GuiGraphics context, RenderPipeline pipeline, Identifier sprite, int x, int y, float u, float v, int width, int height, int regionWidth, int regionHeight, int textureWidth, int textureHeight, int color, Operation<Void> original) {
        if (PuzzleConfig.resourcepackSplashScreen)
            context.blit(PuzzleSplashScreen.CUSTOM_LOGO_PIPELINE, sprite, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, color);
        else context.blit(pipeline, sprite, x, y, u, v, width, height, textureWidth, textureHeight, color);
    }
    //?} else if >= 1.21.5 {
    /*@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Ljava/util/function/Function;Lnet/minecraft/resources/Identifier;IIFFIIIIIII)V"))
    private void puzzle$modifyRenderLayer(GuiGraphics context, Function<Identifier, RenderType> renderType, Identifier sprite, int x, int y, float u, float v, int width, int height, int regionWidth, int regionHeight, int textureWidth, int textureHeight, int color, Operation<Void> original) {
        if (PuzzleConfig.resourcepackSplashScreen)
            context.blit(id -> PuzzleSplashScreen.CUSTOM_LOGO_LAYER, sprite, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, color);
        else context.blit(renderType, sprite, x, y, u, v, width, height, textureWidth, textureHeight, color);
    }
    *///?} else {
    /*@Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;blendFunc(II)V", shift = At.Shift.AFTER), remap = false)
    private void puzzle$betterBlend(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (PuzzleConfig.resourcepackSplashScreen) {
            if (PuzzleConfig.disableBlend) RenderSystem.disableBlend();
            else if (PuzzleConfig.customBlendFunction.size() == 4) {
                try {
                    RenderSystem.blendFuncSeparate(
                            GlStateManager.SourceFactor.valueOf(PuzzleConfig.customBlendFunction.get(0)),
                            GlStateManager.DestFactor.valueOf(PuzzleConfig.customBlendFunction.get(1)),
                            GlStateManager.SourceFactor.valueOf(PuzzleConfig.customBlendFunction.get(2)),
                            GlStateManager.DestFactor.valueOf(PuzzleConfig.customBlendFunction.get(3)));
                } catch (Exception e) {
                    LOGGER.error("Incorrect blend function defined in color.properties: {}{}", PuzzleConfig.customBlendFunction, e.getMessage());
                }
            }
        }
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
            context.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, 0, 0, 0, 0, width, height, width, height, ARGB.white(s));
            //?} else if >= 1.21.5 {
            /*context.blit(RenderType::guiTextured, BACKGROUND, 0, 0, 0, 0, width, height, width, height, ARGB.white(s));
            *///?} else {
            /*RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, s);
            context.blit(BACKGROUND, 0, 0, 0, 0, width, height, width, height);
            RenderSystem.disableBlend();
            *///?}
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
