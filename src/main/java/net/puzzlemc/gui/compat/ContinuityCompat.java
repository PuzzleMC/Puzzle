package net.puzzlemc.gui.compat;

import me.pepperbell.continuity.client.config.ContinuityConfig;
import me.pepperbell.continuity.client.config.Option;
import net.minecraft.network.chat.Component;
import net.puzzlemc.gui.PuzzleApi;
import net.puzzlemc.gui.screen.widget.PuzzleWidget;

import static net.puzzlemc.gui.PuzzleGui.NO;
import static net.puzzlemc.gui.PuzzleGui.YES;

public class ContinuityCompat {
    public static void init() {
        PuzzleApi.addToResourceOptions(new PuzzleWidget(Component.literal("▒ Continuity")));
        ContinuityConfig contConfig = ContinuityConfig.INSTANCE;
        contConfig.getOptionMapView().forEach((s, option) -> {
            if (s.equals("use_manual_culling")) return;
            try {
                Option.BooleanOption booleanOption = ((Option.BooleanOption)option);
                PuzzleApi.addToResourceOptions(new PuzzleWidget(Component.translatable("options.continuity."+s), (button) -> button.setMessage(booleanOption.get() ? YES : NO), (button) -> {
                    booleanOption.set(!booleanOption.get());
                    contConfig.save();
                }));
            } catch (Exception ignored) {}
        });
    }
}
