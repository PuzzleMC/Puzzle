package net.puzzlemc.gui.compat;

import net.minecraft.text.Text;
import net.puzzlemc.gui.PuzzleApi;
import net.puzzlemc.gui.screen.widget.PuzzleWidget;
import traben.entity_texture_features.ETFApi;
import traben.entity_texture_features.config.ETFConfig;

import java.util.EnumSet;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.TreeSet;

import static net.puzzlemc.gui.PuzzleGui.NO;
import static net.puzzlemc.gui.PuzzleGui.YES;

public class ETFCompat {
    public static void init() {
        PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.literal("\uD83D\uDC2E ").append(Text.translatable("config.entity_texture_features.title"))));
        ETFConfig etfConfig = ETFApi.getETFConfigObject();
        PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("config.entity_texture_features.enable_custom_textures.title"), (button) -> button.setMessage(etfConfig.enableCustomTextures ? YES : NO), (button) -> {
            etfConfig.enableCustomTextures = !etfConfig.enableCustomTextures;
            ETFApi.saveETFConfigChangesAndResetETF();
        }));
        PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("config.entity_texture_features.enable_emissive_textures.title"), (button) -> button.setMessage(etfConfig.enableEmissiveTextures ? YES : NO), (button) -> {
            etfConfig.enableEmissiveTextures = !etfConfig.enableEmissiveTextures;
            ETFApi.saveETFConfigChangesAndResetETF();
        }));
        PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("config.entity_texture_features.emissive_mode.title"), (button) -> button.setMessage(
                Text.literal(etfConfig.emissiveRenderMode.toString())), (button) -> {
            final NavigableSet<ETFConfig.EmissiveRenderModes> set =
                    new TreeSet<>(EnumSet.allOf(ETFConfig.EmissiveRenderModes.class));

            etfConfig.emissiveRenderMode = Objects.requireNonNullElseGet(
                    set.higher(etfConfig.emissiveRenderMode), set::first);
            ETFApi.saveETFConfigChangesAndResetETF();
        }));
        PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("config.entity_texture_features.blinking_mob_settings.title"), (button) -> button.setMessage(etfConfig.enableBlinking ? YES : NO), (button) -> {
            etfConfig.enableBlinking = !etfConfig.enableBlinking;
            ETFApi.saveETFConfigChangesAndResetETF();
        }));
        PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("config.entity_texture_features.player_skin_features.title"), (button) -> button.setMessage(etfConfig.skinFeaturesEnabled ? YES : NO), (button) -> {
            etfConfig.skinFeaturesEnabled = !etfConfig.skinFeaturesEnabled;
            ETFApi.saveETFConfigChangesAndResetETF();
        }));
    }
}
