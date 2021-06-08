package net.puzzlemc.gui;

import dev.lambdaurora.lambdabettergrass.LBGConfig;
import dev.lambdaurora.lambdabettergrass.LambdaBetterGrass;
import dev.lambdaurora.lambdynlights.DynamicLightsConfig;
import dev.lambdaurora.lambdynlights.LambDynLights;
import eu.midnightdust.cullleaves.config.CullLeavesConfig;
import net.puzzlemc.core.config.PuzzleConfig;
import net.puzzlemc.gui.screen.widget.PuzzleWidget;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.puzzlemc.splashscreen.PuzzleSplashScreen;
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
//        if (FabricLoader.getInstance().isModLoaded("ctm")) {
//            PuzzleApi.addToTextureOptions(new PuzzleWidget(Text.of("ConnectedTexturesMod for Fabric")));
//            ConfigManager ctmfConfigManager = CTMClient.getConfigManager();
//            ConfigManager.Config ctmfConfig = CTMClient.getConfigManager().getConfig();
//            PuzzleApi.addToTextureOptions(new PuzzleWidget(new TranslatableText("puzzle.option.ctm"), (button) -> button.setMessage(ctmfConfig.disableCTM ? NO : YES), (button) -> {
//                ctmfConfig.disableCTM = !ctmfConfig.disableCTM;
//                ctmfConfigManager.onConfigChange();
//            }));
//            PuzzleApi.addToTextureOptions(new PuzzleWidget(new TranslatableText("puzzle.option.inside_ctm"), (button) -> button.setMessage(ctmfConfig.connectInsideCTM ? YES : NO), (button) -> {
//                ctmfConfig.connectInsideCTM = !ctmfConfig.connectInsideCTM;
//                ctmfConfigManager.onConfigChange();
//            }));
//        }

        if (FabricLoader.getInstance().isModLoaded("lambdabettergrass")) {
            LBGConfig lbgConfig = LambdaBetterGrass.get().config;
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Text.of("LambdaBetterGrass")));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("lambdabettergrass.option.mode"), (button) -> button.setMessage(lbgConfig.getMode().getTranslatedText()), (button) -> lbgConfig.setMode(lbgConfig.getMode().next())));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("lambdabettergrass.option.better_snow"), (button) -> button.setMessage(lbgConfig.hasBetterLayer() ? YES : NO), (button) -> lbgConfig.setBetterLayer(!lbgConfig.hasBetterLayer())));
        }
    }
}
