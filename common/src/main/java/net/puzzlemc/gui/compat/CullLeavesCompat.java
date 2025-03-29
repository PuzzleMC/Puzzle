package net.puzzlemc.gui.compat;

import eu.midnightdust.cullleaves.config.CullLeavesConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.puzzlemc.gui.PuzzleApi;
import net.puzzlemc.gui.screen.widget.PuzzleWidget;

import static net.puzzlemc.gui.PuzzleGui.NO;
import static net.puzzlemc.gui.PuzzleGui.YES;

public class CullLeavesCompat {
    public static void init() {
        PuzzleApi.addToPerformanceOptions(new PuzzleWidget(Text.of("\uD83C\uDF43 Cull Leaves")));
        PuzzleApi.addToPerformanceOptions(new PuzzleWidget(Text.translatable("cullleaves.puzzle.option.enabled"), (button) -> button.setMessage(CullLeavesConfig.enabled ? YES : NO), (button) -> {
            CullLeavesConfig.enabled = !CullLeavesConfig.enabled;
            CullLeavesConfig.write("cullleaves");
            MinecraftClient.getInstance().worldRenderer.reload();
        }));
    }
}
