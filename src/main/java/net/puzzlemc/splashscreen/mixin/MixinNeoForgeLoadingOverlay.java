package net.puzzlemc.splashscreen.mixin;

//? neoforge || forge {
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.gui.DrawContext;
//import net.minecraft.client.gui.screen.SplashOverlay;
//import net.minecraft.resource.ResourceReload;
//import net.neoforged.neoforge.client.loading.NeoForgeLoadingOverlay;
//import net.puzzlemc.core.config.PuzzleConfig;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//import java.util.Optional;
//import java.util.function.Consumer;
//
//@Mixin(NeoForgeLoadingOverlay.class)
//public class MixinNeoForgeLoadingOverlay extends SplashOverlay {
//    public MixinNeoForgeLoadingOverlay(MinecraftClient client, ResourceReload monitor, Consumer<Optional<Throwable>> exceptionHandler, boolean reloading) {
//        super(client, monitor, exceptionHandler, reloading);
//    }
//
//    @Inject(method = "render", at = @At("HEAD"), cancellable = true) // Replaces the NeoForge loading screen in later stages with the (customized) vanilla version
//    private void redirectNeoForgeLoading(DrawContext context, int mouseX, int mouseY, float tickDelta, CallbackInfo ci) {
//        if (PuzzleConfig.resourcepackSplashScreen && PuzzleConfig.hasCustomSplashScreen) {
//            super.render(context, mouseX, mouseY, tickDelta);
//            ci.cancel();
//        }
//    }
//}
//?}
