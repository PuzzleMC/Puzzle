package net.puzzlemc.gui;

import dev.lambdaurora.lambdabettergrass.LBGConfig;
import dev.lambdaurora.lambdabettergrass.LambdaBetterGrass;
import dev.lambdaurora.lambdynlights.DynamicLightsConfig;
import dev.lambdaurora.lambdynlights.LambDynLights;
import eu.midnightdust.cullleaves.config.CullLeavesConfig;
import me.pepperbell.continuity.client.config.ContinuityConfig;
import net.dorianpb.cem.internal.config.CemConfig;
import net.dorianpb.cem.internal.config.CemConfigFairy;
import net.dorianpb.cem.internal.config.CemOptions;
import net.puzzlemc.core.config.PuzzleConfig;
import net.puzzlemc.gui.mixin.CemConfigAccessor;
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
import traben.entity_texture_features.client.ETF_CLIENT;
import traben.entity_texture_features.client.ETF_METHODS;
import traben.entity_texture_features.config.ETFConfig;
import traben.entity_texture_features.config.ETFConfigScreen;

public class PuzzleClient implements ClientModInitializer {

    public final static String id = "puzzle";
    public static final Text YES = new TranslatableText("gui.yes").formatted(Formatting.GREEN);
    public static final Text NO = new TranslatableText("gui.no").formatted(Formatting.RED);

    @Override
    public void onInitializeClient() {
        MinecraftClient client = MinecraftClient.getInstance();
        PuzzleApi.addToMiscOptions(new PuzzleWidget(Text.of("Puzzle")));
        PuzzleApi.addToMiscOptions(new PuzzleWidget(new TranslatableText("puzzle.option.check_for_updates"), (button) -> button.setMessage(PuzzleConfig.checkUpdates ? YES : NO), (button) -> {
            PuzzleConfig.checkUpdates = !PuzzleConfig.checkUpdates;
            PuzzleConfig.write(id);
        }));
        PuzzleApi.addToMiscOptions(new PuzzleWidget(new TranslatableText("puzzle.option.show_version_info"), (button) -> button.setMessage(PuzzleConfig.showPuzzleInfo ? YES : NO), (button) -> {
            PuzzleConfig.showPuzzleInfo = !PuzzleConfig.showPuzzleInfo;
            PuzzleConfig.write(id);
        }));
        PuzzleApi.addToMiscOptions(new PuzzleWidget(new TranslatableText("puzzle.midnightconfig.title"), (button) -> button.setMessage(Text.of("OPEN")), (button) -> {
            client.setScreen(PuzzleConfig.getScreen(client.currentScreen, "puzzle"));
        }));
        PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.of("Puzzle")));
        if (FabricLoader.getInstance().isModLoaded("puzzle-splashscreen") && !PuzzleConfig.disabledIntegrations.contains("puzzle-splashscreen")) {
            PuzzleApi.addToResourceOptions(new PuzzleWidget(new TranslatableText("puzzle.option.resourcepack_splash_screen"), (button) -> button.setMessage(PuzzleConfig.resourcepackSplashScreen ? YES : NO), (button) -> {
                PuzzleConfig.resourcepackSplashScreen = !PuzzleConfig.resourcepackSplashScreen;
                PuzzleConfig.write(id);
                PuzzleSplashScreen.resetColors();
                MinecraftClient.getInstance().getTextureManager().registerTexture(PuzzleSplashScreen.LOGO, new PuzzleSplashScreen.LogoTexture());
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(new TranslatableText("puzzle.option.disable_splash_screen_blend"), (button) -> button.setMessage(PuzzleConfig.disableSplashScreenBlend ? YES : NO), (button) -> {
                PuzzleConfig.disableSplashScreenBlend = !PuzzleConfig.disableSplashScreenBlend;
                PuzzleConfig.write(id);
            }));
        }
        if (FabricLoader.getInstance().isModLoaded("puzzle-models") && !PuzzleConfig.disabledIntegrations.contains("puzzle-models")) {
            PuzzleApi.addToResourceOptions(new PuzzleWidget(new TranslatableText("puzzle.option.unlimited_model_rotations"), (button) -> button.setMessage(PuzzleConfig.unlimitedRotations ? YES : NO), (button) -> {
                PuzzleConfig.unlimitedRotations = !PuzzleConfig.unlimitedRotations;
                PuzzleConfig.write(id);
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(new TranslatableText("puzzle.option.bigger_custom_models"), (button) -> button.setMessage(PuzzleConfig.biggerModels ? YES : NO), (button) -> {
                PuzzleConfig.biggerModels = !PuzzleConfig.biggerModels;
                PuzzleConfig.write(id);
            }));
        }
        if (FabricLoader.getInstance().isModLoaded("cullleaves") && !PuzzleConfig.disabledIntegrations.contains("cullleaves")) {
            PuzzleApi.addToPerformanceOptions(new PuzzleWidget(Text.of("CullLeaves")));
            PuzzleApi.addToPerformanceOptions(new PuzzleWidget(Text.of("Cull Leaves"), (button) -> button.setMessage(CullLeavesConfig.enabled ? YES : NO), (button) -> {
                CullLeavesConfig.enabled = !CullLeavesConfig.enabled;
                CullLeavesConfig.write("cullleaves");
                MinecraftClient.getInstance().worldRenderer.reload();
            }));
        }
        if (FabricLoader.getInstance().isModLoaded("iris") && !PuzzleConfig.disabledIntegrations.contains("iris")) {
            IrisCompat.init();
        }

        if (FabricLoader.getInstance().isModLoaded("continuity") && !PuzzleConfig.disabledIntegrations.contains("continuity")) {
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
        if (FabricLoader.getInstance().isModLoaded("entity_texture_features") && !PuzzleConfig.disabledIntegrations.contains("entity_texture_features")) {
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.of("Entity Texture Features")));
            ETFConfig etfConfig = ETF_CLIENT.ETFConfigData;
            ETFConfigScreen etfConfigScreen = new ETFConfigScreen();
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.of("Enable Optifine Random mobs"), (button) -> button.setMessage(etfConfig.enableCustomTextures ? YES : NO), (button) -> {
                etfConfig.enableCustomTextures = !etfConfig.enableCustomTextures;
                etfConfigScreen.saveConfig();
                etfConfigScreen.resetVisuals();
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.of("Enable Optifine Emissive entity textures"), (button) -> button.setMessage(etfConfig.enableEmissiveTextures ? YES : NO), (button) -> {
                etfConfig.enableEmissiveTextures = !etfConfig.enableEmissiveTextures;
                etfConfigScreen.saveConfig();
                etfConfigScreen.resetVisuals();
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.of("Emissive texture Z-Fighting / Shader patch"), (button) -> button.setMessage(etfConfig.doShadersEmissiveFix ? YES : NO), (button) -> {
                etfConfig.doShadersEmissiveFix = !etfConfig.doShadersEmissiveFix;
                etfConfigScreen.saveConfig();
                etfConfigScreen.resetVisuals();
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.of("Enable Blinking Mobs"), (button) -> button.setMessage(etfConfig.enableBlinking ? YES : NO), (button) -> {
                etfConfig.enableBlinking = !etfConfig.enableBlinking;
                etfConfigScreen.saveConfig();
                etfConfigScreen.resetVisuals();
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.of("Enable Player Skin Features"), (button) -> button.setMessage(etfConfig.skinFeaturesEnabled ? YES : NO), (button) -> {
                etfConfig.skinFeaturesEnabled = !etfConfig.skinFeaturesEnabled;
                etfConfigScreen.saveConfig();
                etfConfigScreen.resetVisuals();
            }));
        }
    }
    public static boolean lateInitDone = false;
    public static void lateInit() { // Some mods are initialized after Puzzle, so we can't access them in our ClientModInitializer
        if (FabricLoader.getInstance().isModLoaded("lambdynlights") && !PuzzleConfig.disabledIntegrations.contains("lambdynlights")) {
            DynamicLightsConfig ldlConfig = LambDynLights.get().config;

            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Text.of("LambDynamicLights")));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("lambdynlights.option.mode"), (button) -> button.setMessage(ldlConfig.getDynamicLightsMode().getTranslatedText()), (button) -> ldlConfig.setDynamicLightsMode(ldlConfig.getDynamicLightsMode().next())));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("").append("DynLights: ").append(new TranslatableText("lambdynlights.option.light_sources.entities")), (button) -> button.setMessage(ldlConfig.getEntitiesLightSource().get() ? YES : NO), (button) -> ldlConfig.getEntitiesLightSource().set(!ldlConfig.getEntitiesLightSource().get())));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("").append("DynLights: ").append(new TranslatableText("lambdynlights.option.light_sources.block_entities")), (button) -> button.setMessage(ldlConfig.getBlockEntitiesLightSource().get() ? YES : NO), (button) -> ldlConfig.getBlockEntitiesLightSource().set(!ldlConfig.getBlockEntitiesLightSource().get())));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("").append("DynLights: ").append(new TranslatableText("entity.minecraft.creeper")), (button) -> button.setMessage(ldlConfig.getCreeperLightingMode().getTranslatedText()), (button) -> ldlConfig.setCreeperLightingMode(ldlConfig.getCreeperLightingMode().next())));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("").append("DynLights: ").append(new TranslatableText("block.minecraft.tnt")), (button) -> button.setMessage(ldlConfig.getTntLightingMode().getTranslatedText()), (button) -> ldlConfig.setTntLightingMode(ldlConfig.getTntLightingMode().next())));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("").append("DynLights: ").append(new TranslatableText("lambdynlights.option.light_sources.water_sensitive_check")), (button) -> button.setMessage(ldlConfig.getWaterSensitiveCheck().get() ? YES : NO), (button) -> ldlConfig.getWaterSensitiveCheck().set(!ldlConfig.getWaterSensitiveCheck().get())));
        }
        if (FabricLoader.getInstance().isModLoaded("citresewn") && !PuzzleConfig.disabledIntegrations.contains("citresewn") && CITResewn.INSTANCE != null && CITResewnConfig.INSTANCE() != null) {
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
        }
        if (FabricLoader.getInstance().isModLoaded("lambdabettergrass") && !PuzzleConfig.disabledIntegrations.contains("lambdabettergrass")) {
            LBGConfig lbgConfig = LambdaBetterGrass.get().config;
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Text.of("LambdaBetterGrass")));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("lambdabettergrass.option.mode"), (button) -> button.setMessage(lbgConfig.getMode().getTranslatedText()), (button) -> lbgConfig.setMode(lbgConfig.getMode().next())));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("lambdabettergrass.option.better_snow"), (button) -> button.setMessage(lbgConfig.hasBetterLayer() ? YES : NO), (button) -> lbgConfig.setBetterLayer(!lbgConfig.hasBetterLayer())));
        }
        if (FabricLoader.getInstance().isModLoaded("cem") && FabricLoader.getInstance().isModLoaded("completeconfig") && !PuzzleConfig.disabledIntegrations.contains("cem")) {
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.of("Custom Entity Models")));
            CemConfig cemConfig = (CemConfig) CemConfigFairy.getConfig();
            CemOptions cemOptions = CemConfigFairy.getConfig();
            PuzzleApi.addToResourceOptions(new PuzzleWidget(new TranslatableText("config.cem.use_optifine_folder"), (button) -> button.setMessage(cemConfig.useOptifineFolder() ? YES : NO), (button) -> {
                ((CemConfigAccessor)cemOptions).setUseOptifineFolder(!cemConfig.useOptifineFolder());
                cemConfig.save();
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(new TranslatableText("config.cem.use_new_model_creation_fix"), (button) -> button.setMessage(cemConfig.useTransparentParts() ? YES : NO), (button) -> {
                ((CemConfigAccessor)cemOptions).setUseModelCreationFix(!cemConfig.useTransparentParts());
                cemConfig.save();
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(new TranslatableText("config.cem.use_old_animations"), (button) -> button.setMessage(cemConfig.useOldAnimations() ? YES : NO), (button) -> {
                ((CemConfigAccessor)cemOptions).setUseOldAnimations(!cemConfig.useOldAnimations());
                cemConfig.save();
            }));
        }
        lateInitDone = true;
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
