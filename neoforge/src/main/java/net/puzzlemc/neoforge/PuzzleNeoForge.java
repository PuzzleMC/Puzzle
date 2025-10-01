package net.puzzlemc.neoforge;

import net.minecraft.util.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.puzzlemc.core.PuzzleCore;
import net.puzzlemc.gui.screen.PuzzleOptionsScreen;
import net.puzzlemc.splashscreen.PuzzleSplashScreen;

import static net.puzzlemc.core.PuzzleCore.MOD_ID;

@Mod(value = MOD_ID, dist = Dist.CLIENT)
public class PuzzleNeoForge {
    public PuzzleNeoForge() {
        PuzzleCore.initModules();
        ModList.get().getModContainerById(MOD_ID).orElseThrow().registerExtensionPoint(IConfigScreenFactory.class, (client, parent) -> new PuzzleOptionsScreen(parent));
    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class MidnightLibBusEvents {
        @SubscribeEvent
        public static void onResourceReload(AddClientReloadListenersEvent event) {
            event.addListener(Identifier.of(MOD_ID, "splash_screen"), PuzzleSplashScreen.ReloadListener.INSTANCE);
        }
    }
}
