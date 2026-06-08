package net.puzzlemc.core;

import net.minecraft.resources.Identifier;

import net.puzzlemc.splashscreen.PuzzleSplashScreen;

import static net.puzzlemc.core.PuzzleCore.MOD_ID;

//? fabric {
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.server.packs.PackType;
//? if >= 1.21.9 {
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
//?} else {
/*import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
*///?}

public class PuzzleClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PuzzleCore.initModules();

        //? if >= 26.1 {
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(Identifier.fromNamespaceAndPath(MOD_ID, "splash_screen"), PuzzleSplashScreen.ReloadListener.INSTANCE);
        //?} else if >= 1.21.9 {
        //ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(Identifier.fromNamespaceAndPath(MOD_ID, "splash_screen"), PuzzleSplashScreen.ReloadListener.INSTANCE);
        //?} else {
        /*ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return Identifier.fromNamespaceAndPath(MOD_ID, "splash_screen");
            }
            @Override
            public void onResourceManagerReload(ResourceManager manager) {
                PuzzleSplashScreen.ReloadListener.INSTANCE.onResourceManagerReload(manager);
            }
        });
        *///?}
    }
}
//?}

//? neoforge {
/*import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
//? if >= 1.21.5 {
/^import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
^///?} else {
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
//?}

@Mod(value = MOD_ID, dist = Dist.CLIENT)
public class PuzzleClient {
    public PuzzleClient() {
        PuzzleCore.initModules();
    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT /^? if <= 1.21.5 {^/ , bus = EventBusSubscriber.Bus.MOD /^?}^/)
    public static class MidnightLibBusEvents {
        //? if >= 1.21.5 {
        /^@SubscribeEvent
        public static void onResourceReload(AddClientReloadListenersEvent event) {
            event.addListener(Identifier.fromNamespaceAndPath(MOD_ID, "splash_screen"), PuzzleSplashScreen.ReloadListener.INSTANCE);
        }
        ^///?} else {
        @SubscribeEvent
        public static void onResourceReload(RegisterClientReloadListenersEvent event) {
            event.registerReloadListener(PuzzleSplashScreen.ReloadListener.INSTANCE);
        }
        //?}
    }
 }
    *///?}
