package net.puzzlemc.gui;

import dev.lambdaurora.lambdabettergrass.LBGConfig;
import dev.lambdaurora.lambdabettergrass.LambdaBetterGrass;
import dev.lambdaurora.lambdynlights.DynamicLightsConfig;
import dev.lambdaurora.lambdynlights.LambDynLights;
import eu.midnightdust.cullleaves.config.CullLeavesConfig;
import me.pepperbell.continuity.client.config.ContinuityConfig;
import net.dorianpb.cem.internal.config.CemConfig;
import net.dorianpb.cem.internal.config.CemOptions;
import net.puzzlemc.core.config.PuzzleConfig;
import net.puzzlemc.gui.screen.widget.PuzzleWidget;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.puzzlemc.splashscreen.PuzzleSplashScreen;
import shcm.shsupercm.fabric.citresewn.CITResewn;
import shcm.shsupercm.fabric.citresewn.config.CITResewnConfig;
//import team.chisel.ctm.client.CTMClient;
//import team.chisel.ctm.client.config.ConfigManager;

public class PuzzleClient implements ClientModInitializer {

    public final static String id = "puzzle";
    public static final Text YES = new TranslatableText("gui.yes").formatted(Formatting.GREEN);
    public static final Text NO = new TranslatableText("gui.no").formatted(Formatting.RED);

    @Override
    public void onInitializeClient() {
        PuzzleApi.addToMiscOptions(new PuzzleWidget(Text.of("Puzzle")));
        PuzzleApi.addToMiscOptions(new PuzzleWidget(Text.of("Check for Updates"), (button) -> button.setMessage(PuzzleConfig.checkUpdates ? YES : NO), (button) -> {
            PuzzleConfig.checkUpdates = !PuzzleConfig.checkUpdates;
            PuzzleConfig.write(id);
        }));
        PuzzleApi.addToMiscOptions(new PuzzleWidget(Text.of("Show Puzzle version info"), (button) -> button.setMessage(PuzzleConfig.showPuzzleInfo ? YES : NO), (button) -> {
            PuzzleConfig.showPuzzleInfo = !PuzzleConfig.showPuzzleInfo;
            PuzzleConfig.write(id);
        }));
        PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.of("Puzzle")));
        if (FabricLoader.getInstance().isModLoaded("puzzle-splashscreen")) {
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.of("Use resourcepack splash screen "), (button) -> button.setMessage(PuzzleConfig.resourcepackSplashScreen ? YES : NO), (button) -> {
                PuzzleConfig.resourcepackSplashScreen = !PuzzleConfig.resourcepackSplashScreen;
                PuzzleConfig.write(id);
                PuzzleSplashScreen.resetColors();
                MinecraftClient.getInstance().getTextureManager().registerTexture(PuzzleSplashScreen.LOGO, new PuzzleSplashScreen.LogoTexture());
            }));
        }
        if (FabricLoader.getInstance().isModLoaded("puzzle-randomentities")) {
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.of("Random Entity Textures"), (button) -> button.setMessage(PuzzleConfig.randomEntityTextures ? YES : NO), (button) -> {
                PuzzleConfig.randomEntityTextures = !PuzzleConfig.randomEntityTextures;
                PuzzleConfig.write(id);
            }));
        }
        if (FabricLoader.getInstance().isModLoaded("puzzle-models")) {
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.of("Unlimited Model Rotations"), (button) -> button.setMessage(PuzzleConfig.unlimitedRotations ? YES : NO), (button) -> {
                PuzzleConfig.unlimitedRotations = !PuzzleConfig.unlimitedRotations;
                PuzzleConfig.write(id);
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.of("Bigger Custom Models"), (button) -> button.setMessage(PuzzleConfig.biggerModels ? YES : NO), (button) -> {
                PuzzleConfig.biggerModels = !PuzzleConfig.biggerModels;
                PuzzleConfig.write(id);
            }));
        }
        if (FabricLoader.getInstance().isModLoaded("puzzle-blocks")) {
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.of("Render Layer Overwrites"), (button) -> button.setMessage(PuzzleConfig.customRenderLayers ? YES : NO), (button) -> {
                PuzzleConfig.customRenderLayers = !PuzzleConfig.customRenderLayers;
                PuzzleConfig.write(id);
            }));
        }

        if (FabricLoader.getInstance().isModLoaded("cullleaves")) {
            PuzzleApi.addToPerformanceOptions(new PuzzleWidget(Text.of("CullLeaves")));
            PuzzleApi.addToPerformanceOptions(new PuzzleWidget(Text.of("Cull Leaves"), (button) -> button.setMessage(CullLeavesConfig.enabled ? YES : NO), (button) -> {
                CullLeavesConfig.enabled = !CullLeavesConfig.enabled;
                CullLeavesConfig.write("cullleaves");
                MinecraftClient.getInstance().worldRenderer.reload();
            }));
        }

        if (FabricLoader.getInstance().isModLoaded("lambdynlights")) {
            DynamicLightsConfig ldlConfig = LambDynLights.get().config;
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Text.of("LambDynamicLights")));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("lambdynlights.option.mode"), (button) -> button.setMessage(ldlConfig.getDynamicLightsMode().getTranslatedText()), (button) -> ldlConfig.setDynamicLightsMode(ldlConfig.getDynamicLightsMode().next())));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("").append("DynLights: ").append(new TranslatableText("lambdynlights.option.entities")), (button) -> button.setMessage(ldlConfig.hasEntitiesLightSource() ? YES : NO), (button) -> ldlConfig.setEntitiesLightSource(!ldlConfig.hasEntitiesLightSource())));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("").append("DynLights: ").append(new TranslatableText("lambdynlights.option.block_entities")), (button) -> button.setMessage(ldlConfig.hasBlockEntitiesLightSource() ? YES : NO), (button) -> ldlConfig.setBlockEntitiesLightSource(!ldlConfig.hasBlockEntitiesLightSource())));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("").append("DynLights: ").append(new TranslatableText("entity.minecraft.creeper")), (button) -> button.setMessage(ldlConfig.getCreeperLightingMode().getTranslatedText()), (button) -> ldlConfig.setCreeperLightingMode(ldlConfig.getCreeperLightingMode().next())));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("").append("DynLights: ").append(new TranslatableText("block.minecraft.tnt")), (button) -> button.setMessage(ldlConfig.getTntLightingMode().getTranslatedText()), (button) -> ldlConfig.setTntLightingMode(ldlConfig.getTntLightingMode().next())));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("").append("DynLights: ").append(new TranslatableText("lambdynlights.option.water_sensitive")), (button) -> button.setMessage(ldlConfig.hasWaterSensitiveCheck() ? YES : NO), (button) -> ldlConfig.setWaterSensitiveCheck(!ldlConfig.hasWaterSensitiveCheck())));
        }
        if (FabricLoader.getInstance().isModLoaded("continuity")) {
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.of("Continuity")));
            ContinuityConfig contConfig = ContinuityConfig.INSTANCE;
            PuzzleApi.addToResourceOptions(new PuzzleWidget(new TranslatableText("options.continuity.disable_ctm"), (button) -> button.setMessage(contConfig.disableCTM.get() ? YES : NO), (button) -> {
                contConfig.disableCTM.set(!contConfig.disableCTM.get());
                contConfig.onChange();
                contConfig.save();
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(new TranslatableText("options.continuity.use_manual_culling"), (button) -> button.setMessage(contConfig.useManualCulling.get() ? YES : NO), (button) -> {
                contConfig.useManualCulling.set(!contConfig.useManualCulling.get());
                contConfig.onChange();
                contConfig.save();
            }));
        }

        if (FabricLoader.getInstance().isModLoaded("lambdabettergrass")) {
            LBGConfig lbgConfig = LambdaBetterGrass.get().config;
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Text.of("LambdaBetterGrass")));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("lambdabettergrass.option.mode"), (button) -> button.setMessage(lbgConfig.getMode().getTranslatedText()), (button) -> lbgConfig.setMode(lbgConfig.getMode().next())));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("lambdabettergrass.option.better_snow"), (button) -> button.setMessage(lbgConfig.hasBetterLayer() ? YES : NO), (button) -> lbgConfig.setBetterLayer(!lbgConfig.hasBetterLayer())));
        }

    }
    public static boolean citInitialized = false;
    public static void initCITResewn() { // CITResewn is initialized after Puzzle, so we can't access it in our ClientModInitializer
        if (!citInitialized && FabricLoader.getInstance().isModLoaded("citresewn") && CITResewn.INSTANCE != null && CITResewnConfig.INSTANCE() != null) {
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.of("CIT Resewn")));
            CITResewnConfig citConfig = CITResewnConfig.INSTANCE();
            PuzzleApi.addToResourceOptions(new PuzzleWidget(new TranslatableText("config.citresewn.enabled.title"), (button) -> button.setMessage(citConfig.enabled ? YES : NO), (button) -> {
                citConfig.enabled = !citConfig.enabled;
                citConfig.write();
                MinecraftClient.getInstance().reloadResources();
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(new TranslatableText("config.citresewn.mute_errors.title"), (button) -> button.setMessage(citConfig.mute_errors ? YES : NO), (button) -> {
                citConfig.mute_errors = !citConfig.mute_errors;
                citConfig.write();
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(new TranslatableText("config.citresewn.mute_warns.title"), (button) -> button.setMessage(citConfig.mute_warns ? YES : NO), (button) -> {
                citConfig.mute_warns = !citConfig.mute_warns;
                citConfig.write();
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(new TranslatableText("config.citresewn.broken_paths.title"), (button) -> button.setMessage(citConfig.broken_paths ? YES : NO), (button) -> {
                citConfig.broken_paths = !citConfig.broken_paths;
                citConfig.write();
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(0, 100,new TranslatableText("config.citresewn.cache_ms.title"), (slider) -> slider.setInt(citConfig.cache_ms),
                    (button) -> button.setMessage(message(citConfig)),
                    (slider) -> {
                try {
                    citConfig.cache_ms = slider.getInt();
                }
                catch (NumberFormatException ignored) {}
                citConfig.write();
            }));
            citInitialized = true;
        }
    }
    public static Text message(CITResewnConfig config) {
        int ticks = config.cache_ms;
            if (ticks <= 1) {
                return (new TranslatableText("config.citresewn.cache_ms.ticks." + ticks)).formatted(Formatting.AQUA);
            } else {
                Formatting color = Formatting.DARK_RED;
                if (ticks <= 40) {
                    color = Formatting.RED;
                }

                if (ticks <= 20) {
                    color = Formatting.GOLD;
                }

                if (ticks <= 10) {
                    color = Formatting.DARK_GREEN;
                }

                if (ticks <= 5) {
                    color = Formatting.GREEN;
                }

                return (new TranslatableText("config.citresewn.cache_ms.ticks.any", ticks)).formatted(color);
            }
    }
}