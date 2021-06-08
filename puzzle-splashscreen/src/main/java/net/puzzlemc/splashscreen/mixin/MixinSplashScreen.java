package net.puzzlemc.splashscreen.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SplashScreen;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.puzzlemc.core.config.PuzzleConfig;
import net.puzzlemc.splashscreen.PuzzleSplashScreen;
import net.puzzlemc.splashscreen.util.ConfigTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SplashScreen.class, priority = 2000)
public class MixinSplashScreen {
    @Shadow @Final static Identifier LOGO;
    @Shadow @Final private boolean reloading;
    @Shadow private long reloadStartTime;
    @Shadow private long reloadCompleteTime;

    @Inject(method = "init(Lnet/minecraft/client/MinecraftClient;)V", at = @At("TAIL"), cancellable=true)
    private static void init(MinecraftClient client, CallbackInfo ci) { // Load our custom textures at game start //
        if (PuzzleConfig.resourcepackSplashScreen && PuzzleSplashScreen.LOGO_TEXTURE.toFile().exists()) client.getTextureManager().registerTexture(LOGO, new ConfigTexture(LOGO));
    }
    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/SplashScreen;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"), index = 5)
    private int modifyBackgroundColor(int color) { // Set the Background Color to our configured value //
        long l = Util.getMeasuringTimeMs();
        if (this.reloading && this.reloadStartTime == -1L) {
            this.reloadStartTime = l;
        }

        float f = this.reloadCompleteTime > -1L ? (float)(l - this.reloadCompleteTime) / 1000.0F : -1.0F;
        int m = MathHelper.ceil((1.0F - MathHelper.clamp(f - 1.0F, 0.0F, 1.0F)) * 255.0F);

        return PuzzleConfig.backgroundColor | m << 24;
    }

    @ModifyArg(method = "renderProgressBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/SplashScreen;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"), index = 5)
    private int modifyProgressFrame(int color) { // Set the Progress Bar Frame Color to our configured value //
        return PuzzleConfig.progressFrameColor | 255 << 24;
    }
    @ModifyArg(method = "renderProgressBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/SplashScreen;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V", ordinal = 4), index = 5)
    private int modifyProgressColor(int color) { // Set the Progress Bar Color to our configured value //
        return PuzzleConfig.progressBarColor | 255 << 24;
    }

}
