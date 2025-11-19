package net.puzzlemc.core;



import net.minecraft.resources.ResourceLocation;

import net.puzzlemc.gui.screen.PuzzleOptionsScreen;
import net.puzzlemc.splashscreen.PuzzleSplashScreen;

import static net.puzzlemc.core.PuzzleCore.MOD_ID;

//? fabric {
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.server.packs.PackType;
//? if >= 1.21.9 {
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
//?} else {
/*import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
*///?}

public class PuzzleClient implements ClientModInitializer, ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return PuzzleOptionsScreen::new;
    }

    @Override
    public void onInitializeClient() {
        PuzzleCore.initModules();

        //ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener
        //? if >= 1.21.9 {
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(ResourceLocation.fromNamespaceAndPath(MOD_ID, "splash_screen"), PuzzleSplashScreen.ReloadListener.INSTANCE);
        //?} else {
        /*ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return ResourceLocation.fromNamespaceAndPath(MOD_ID, "splash_screen");
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
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = MOD_ID, dist = Dist.CLIENT)
public class PuzzleClient {
    public PuzzleClient() {
        PuzzleCore.initModules();
        ModList.get().getModContainerById(MOD_ID).orElseThrow().registerExtensionPoint(IConfigScreenFactory.class, (client, parent) -> new PuzzleOptionsScreen(parent));
    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT /^? if <= 1.21.5 {^/ /^, bus = EventBusSubscriber.Bus.MOD ^//^?}^/)
    public static class MidnightLibBusEvents {
        @SubscribeEvent
        public static void onResourceReload(AddClientReloadListenersEvent event) {
            event.addListener(ResourceLocation.fromNamespaceAndPath(MOD_ID, "splash_screen"), PuzzleSplashScreen.ReloadListener.INSTANCE);
        }
    }
 }
    *///?}