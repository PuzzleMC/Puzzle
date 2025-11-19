package net.puzzlemc.gui.compat;

import com.mojang.text2speech.OperatingSystem;
import link.infra.borderlessmining.config.ConfigHandler;
import net.minecraft.network.chat.Component;
import net.puzzlemc.gui.PuzzleApi;
import net.puzzlemc.gui.screen.widget.PuzzleWidget;

import static net.puzzlemc.gui.PuzzleGui.NO;
import static net.puzzlemc.gui.PuzzleGui.YES;

public class BorderlessMiningCompat {
    public static void init() {
        PuzzleApi.addToMiscOptions(new PuzzleWidget(Component.literal("\uD83E\uDE9F Borderless Mining")));
        ConfigHandler bmConfig = ConfigHandler.getInstance();
        PuzzleApi.addToMiscOptions(new PuzzleWidget(Component.translatable("config.borderlessmining.general.enabled"), (button) -> button.setMessage(bmConfig.isEnabledOrPending() ? YES : NO), (button) -> {
            bmConfig.setEnabledPending(!bmConfig.isEnabledOrPending());
            bmConfig.save();
        }));
        if (OperatingSystem.get() == OperatingSystem.MAC_OS) {
            PuzzleApi.addToMiscOptions(new PuzzleWidget(Component.translatable("config.borderlessmining.general.enabledmac"), (button) -> button.setMessage(bmConfig.enableMacOS ? YES : NO), (button) -> {
                bmConfig.enableMacOS = !bmConfig.enableMacOS;
                bmConfig.setEnabledPending(bmConfig.isEnabled());
                bmConfig.save();
            }));
        }
    }
}
