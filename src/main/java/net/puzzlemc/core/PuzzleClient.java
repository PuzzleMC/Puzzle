package net.puzzlemc.core;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.puzzlemc.gui.screen.PuzzleOptionsScreen;
import net.puzzlemc.splashscreen.PuzzleSplashScreen;

import static net.puzzlemc.core.PuzzleCore.MOD_ID;

//? fabric {
public class PuzzleClient implements ClientModInitializer, ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return PuzzleOptionsScreen::new;
    }

    @Override
    public void onInitializeClient() {
        PuzzleCore.initModules();

        //ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(ResourceLocation.fromNamespaceAndPath(MOD_ID, "splash_screen"), PuzzleSplashScreen.ReloadListener.INSTANCE);
    }
}
//?}

//? neoforge {
// @Mod(value = MOD_ID, dist = Dist.CLIENT)
// public class PuzzleClient {
//        public PuzzleClient() {
//            PuzzleCore.initModules();
//            ModList.get().getModContainerById(MOD_ID).orElseThrow().registerExtensionPoint(IConfigScreenFactory.class, (client, parent) -> new PuzzleOptionsScreen(parent));
//        }
//
//        @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
//        public static class MidnightLibBusEvents {
//            @SubscribeEvent
//            public static void onResourceReload(AddClientReloadListenersEvent event) {
//                event.addListener(ResourceLocation.of(MOD_ID, "splash_screen"), PuzzleSplashScreen.ReloadListener.INSTANCE);
//            }
//        }
// }
    //?}