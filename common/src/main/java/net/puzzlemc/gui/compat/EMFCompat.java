package net.puzzlemc.gui.compat;

import eu.midnightdust.lib.util.PlatformFunctions;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.puzzlemc.gui.PuzzleApi;
import net.puzzlemc.gui.screen.widget.PuzzleWidget;
import traben.entity_model_features.EMF;
import traben.entity_model_features.config.EMFConfig;

import java.util.EnumSet;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.TreeSet;

import static net.puzzlemc.gui.PuzzleGui.NO;
import static net.puzzlemc.gui.PuzzleGui.YES;

public class EMFCompat {
    public static void init() {
        PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("entity_model_features.title")));
        EMFConfig emfConfig = EMF.config().getConfig();
        if (PlatformFunctions.isModLoaded("physicsmod")) {
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("entity_model_features.config.physics"), (button) -> button.setMessage(emfConfig.attemptPhysicsModPatch_2 != EMFConfig.PhysicsModCompatChoice.OFF ?
                    Text.translatable("entity_model_features.config." + (emfConfig.attemptPhysicsModPatch_2 == EMFConfig.PhysicsModCompatChoice.VANILLA ? "physics.1" : "physics.2")) : ScreenTexts.OFF), (button) -> {
                final NavigableSet<EMFConfig.PhysicsModCompatChoice> set =
                        new TreeSet<>(EnumSet.allOf(EMFConfig.PhysicsModCompatChoice.class));

                emfConfig.attemptPhysicsModPatch_2 = Objects.requireNonNullElseGet(
                        set.higher(emfConfig.attemptPhysicsModPatch_2), set::first);
                EMF.config().saveToFile();
            }));
        }
    }
}
