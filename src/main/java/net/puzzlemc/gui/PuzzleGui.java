package net.puzzlemc.gui;

import eu.midnightdust.core.MidnightLib;
import eu.midnightdust.lib.util.PlatformFunctions;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.puzzlemc.core.config.PuzzleConfig;
import net.puzzlemc.gui.compat.*;
import net.puzzlemc.gui.screen.widget.PuzzleWidget;
import net.minecraft.client.Minecraft;
import net.puzzlemc.splashscreen.PuzzleSplashScreen;

import static net.puzzlemc.core.PuzzleCore.LOGGER;
import static net.puzzlemc.core.PuzzleCore.MOD_ID;

public class PuzzleGui {
    public static final Component YES = Component.translatable("gui.yes").withStyle(ChatFormatting.GREEN);
    public static final Component NO = Component.translatable("gui.no").withStyle(ChatFormatting.RED);
    public static final ResourceLocation PUZZLE_BUTTON = ResourceLocation.fromNamespaceAndPath(MOD_ID, "icon/button");

    public static void init() {
        MidnightLib.hiddenMods.add(MOD_ID);
        PuzzleApi.addToMiscOptions(new PuzzleWidget(Component.literal("\uD83E\uDDE9 Puzzle")));
        PuzzleApi.addToMiscOptions(new PuzzleWidget(Component.translatable("puzzle.midnightconfig.title"), (button) -> button.setMessage(Component.literal("OPEN")), (button) -> {
            Minecraft.getInstance().setScreen(PuzzleConfig.getScreen(Minecraft.getInstance().screen, MOD_ID));
        }));
        PuzzleApi.addToResourceOptions(new PuzzleWidget(Component.literal("\uD83E\uDDE9 Puzzle")));
        PuzzleApi.addToResourceOptions(new PuzzleWidget(Component.translatable("puzzle.option.resourcepack_splash_screen"), (button) -> button.setMessage(PuzzleConfig.resourcepackSplashScreen ? YES : NO), (button) -> {
            PuzzleConfig.resourcepackSplashScreen = !PuzzleConfig.resourcepackSplashScreen;
            PuzzleSplashScreen.resetColors();
            PuzzleConfig.write(MOD_ID);
            Minecraft.getInstance().getTextureManager().registerAndLoad(PuzzleSplashScreen.LOGO, new PuzzleSplashScreen.LogoTexture(PuzzleSplashScreen.LOGO));
        }));
        PuzzleApi.addToResourceOptions(new PuzzleWidget(Component.translatable("puzzle.option.unlimited_model_rotations"), (button) -> button.setMessage(PuzzleConfig.unlimitedRotations ? YES : NO), (button) -> {
            PuzzleConfig.unlimitedRotations = !PuzzleConfig.unlimitedRotations;
            PuzzleConfig.write(MOD_ID);
        }));
        PuzzleApi.addToResourceOptions(new PuzzleWidget(Component.translatable("puzzle.option.bigger_custom_models"), (button) -> button.setMessage(PuzzleConfig.biggerModels ? YES : NO), (button) -> {
            PuzzleConfig.biggerModels = !PuzzleConfig.biggerModels;
            PuzzleConfig.write(MOD_ID);
        }));
        if (isActive("cullleaves")) CullLeavesCompat.init();
        if (isActive("colormatic")) ColormaticCompat.init();
        if (isActive("borderlessmining")) BorderlessMiningCompat.init();
        if (isActive("iris")) IrisCompat.init();
    }

    public static boolean lateInitDone = false;
    public static void lateInit() { // Some mods are initialized after Puzzle, so we can't access them in our ClientModInitializer
        if (isActive("lambdynlights")) LDLCompat.init();
        if (isActive("citresewn")) CITRCompat.init();
        if (isActive("lambdabettergrass")) LBGCompat.init();
        if (isActive("continuity")) ContinuityCompat.init();
        try {
            if (isActive("entity_Componenture_features")) ETFCompat.init();
            if (isActive("entity_model_features")) EMFCompat.init();
        } catch (Exception e) {
            LOGGER.error("ETF/EMF config structure changed. Again...", e);
        }

        lateInitDone = true;
    }
    public static boolean isActive(String modid) {
        return PlatformFunctions.isModLoaded(modid) && !PuzzleConfig.disabledIntegrations.contains(modid);
    }
}
