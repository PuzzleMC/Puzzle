package net.puzzlemc.gui.compat;

import dev.lambdaurora.lambdabettergrass.LBGConfig;
import dev.lambdaurora.lambdabettergrass.LambdaBetterGrass;
import net.minecraft.text.Text;
import net.puzzlemc.gui.PuzzleApi;
import net.puzzlemc.gui.screen.widget.PuzzleWidget;

import static net.puzzlemc.gui.PuzzleGui.NO;
import static net.puzzlemc.gui.PuzzleGui.YES;

public class LBGCompat {
    public static void init() {
        LBGConfig lbgConfig = LambdaBetterGrass.get().config;
        PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Text.of("\uD83C\uDF31 LambdaBetterGrass")));
        PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Text.translatable("lambdabettergrass.option.mode"), (button) -> button.setMessage(lbgConfig.getMode().getTranslatedText()), (button) -> lbgConfig.setMode(lbgConfig.getMode().next())));
        PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Text.translatable("lambdabettergrass.option.better_snow"), (button) -> button.setMessage(lbgConfig.hasBetterLayer() ? YES : NO), (button) -> lbgConfig.setBetterLayer(!lbgConfig.hasBetterLayer())));
    }
}
