package net.puzzlemc.gui;

import dev.lambdaurora.lambdabettergrass.LBGConfig;
import dev.lambdaurora.lambdabettergrass.LambdaBetterGrass;
import dev.lambdaurora.lambdynlights.DynamicLightsConfig;
import dev.lambdaurora.lambdynlights.LambDynLights;
import eu.midnightdust.core.MidnightLibClient;
import eu.midnightdust.cullleaves.config.CullLeavesConfig;
import eu.midnightdust.lib.util.PlatformFunctions;
import io.github.kvverti.colormatic.Colormatic;
import io.github.kvverti.colormatic.ColormaticConfigController;
import link.infra.borderlessmining.config.ConfigHandler;
import me.pepperbell.continuity.client.config.ContinuityConfig;
import me.pepperbell.continuity.client.config.Option;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.util.Identifier;
import net.puzzlemc.core.config.PuzzleConfig;
import net.puzzlemc.gui.screen.widget.PuzzleWidget;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.puzzlemc.splashscreen.PuzzleSplashScreen;
import shcm.shsupercm.fabric.citresewn.config.CITResewnConfig;
import traben.entity_model_features.config.EMFConfig;
import traben.entity_texture_features.ETFApi;
import traben.entity_texture_features.config.ETFConfig;
import io.github.kvverti.colormatic.ColormaticConfig;

public class PuzzleClient implements ClientModInitializer {

    public final static String id = "puzzle";
    public static final Text YES = Text.translatable("gui.yes").formatted(Formatting.GREEN);
    public static final Text NO = Text.translatable("gui.no").formatted(Formatting.RED);
    public static final Identifier PUZZLE_BUTTON = new Identifier(id, "icon/button");

    @Override
    public void onInitializeClient() {
        MidnightLibClient.hiddenMods.add("puzzle");
        MinecraftClient client = MinecraftClient.getInstance();
        PuzzleApi.addToMiscOptions(new PuzzleWidget(Text.of("Puzzle")));
        PuzzleApi.addToMiscOptions(new PuzzleWidget(Text.translatable("puzzle.option.check_for_updates"), (button) -> button.setMessage(PuzzleConfig.checkUpdates ? YES : NO), (button) -> {
            PuzzleConfig.checkUpdates = !PuzzleConfig.checkUpdates;
            PuzzleConfig.write(id);
        }));
        PuzzleApi.addToMiscOptions(new PuzzleWidget(Text.translatable("puzzle.option.show_version_info"), (button) -> button.setMessage(PuzzleConfig.showPuzzleInfo ? YES : NO), (button) -> {
            PuzzleConfig.showPuzzleInfo = !PuzzleConfig.showPuzzleInfo;
            PuzzleConfig.write(id);
        }));
        PuzzleApi.addToMiscOptions(new PuzzleWidget(Text.translatable("puzzle.midnightconfig.title"), (button) -> button.setMessage(Text.of("OPEN")), (button) -> {
            client.setScreen(PuzzleConfig.getScreen(client.currentScreen, "puzzle"));
        }));
        PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.of("Puzzle")));
        if (isActive("puzzle-splashscreen")) {
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("puzzle.option.resourcepack_splash_screen"), (button) -> button.setMessage(PuzzleConfig.resourcepackSplashScreen ? YES : NO), (button) -> {
                PuzzleConfig.resourcepackSplashScreen = !PuzzleConfig.resourcepackSplashScreen;
                PuzzleConfig.write(id);
                PuzzleSplashScreen.resetColors();
                MinecraftClient.getInstance().getTextureManager().registerTexture(PuzzleSplashScreen.LOGO, new PuzzleSplashScreen.LogoTexture(PuzzleSplashScreen.LOGO));
            }));
        }
        if (isActive("puzzle-models")) {
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("puzzle.option.unlimited_model_rotations"), (button) -> button.setMessage(PuzzleConfig.unlimitedRotations ? YES : NO), (button) -> {
                PuzzleConfig.unlimitedRotations = !PuzzleConfig.unlimitedRotations;
                PuzzleConfig.write(id);
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("puzzle.option.bigger_custom_models"), (button) -> button.setMessage(PuzzleConfig.biggerModels ? YES : NO), (button) -> {
                PuzzleConfig.biggerModels = !PuzzleConfig.biggerModels;
                PuzzleConfig.write(id);
            }));
        }
        if (isActive("cullleaves")) {
            PuzzleApi.addToPerformanceOptions(new PuzzleWidget(Text.of("CullLeaves")));
            PuzzleApi.addToPerformanceOptions(new PuzzleWidget(Text.translatable("cullleaves.puzzle.option.enabled"), (button) -> button.setMessage(CullLeavesConfig.enabled ? YES : NO), (button) -> {
                CullLeavesConfig.enabled = !CullLeavesConfig.enabled;
                CullLeavesConfig.write("cullleaves");
                MinecraftClient.getInstance().worldRenderer.reload();
            }));
        }
        if (isActive("colormatic")) {
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.of("Colormatic")));
            ColormaticConfig colormaticConfig = Colormatic.config();
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("colormatic.config.option.clearSky"), (button) -> button.setMessage(colormaticConfig.clearSky ? YES : NO), (button) -> {
                colormaticConfig.clearSky = !colormaticConfig.clearSky;
                ColormaticConfigController.persist(colormaticConfig);
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("colormatic.config.option.clearVoid"), (button) -> button.setMessage(colormaticConfig.clearVoid ? YES : NO), (button) -> {
                colormaticConfig.clearVoid = !colormaticConfig.clearVoid;
                ColormaticConfigController.persist(colormaticConfig);
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("colormatic.config.option.blendSkyLight"), (button) -> button.setMessage(colormaticConfig.blendSkyLight ? YES : NO), (button) -> {
                colormaticConfig.blendSkyLight = !colormaticConfig.blendSkyLight;
                ColormaticConfigController.persist(colormaticConfig);
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("colormatic.config.option.flickerBlockLight"), (button) -> button.setMessage(colormaticConfig.flickerBlockLight ? YES : NO), (button) -> {
                colormaticConfig.flickerBlockLight = !colormaticConfig.flickerBlockLight;
                ColormaticConfigController.persist(colormaticConfig);
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(0, 100, Text.translatable("colormatic.config.option.relativeBlockLightIntensity"),
                    () -> (int) (100*(1.0 - colormaticConfig.relativeBlockLightIntensityExponent / -16.0)),
                    (button) -> button.setMessage(Text.translatable("colormatic.config.option.relativeBlockLightIntensity")
                        .append(": ")
                        .append(Text.literal(String.valueOf((int)(100 * Math.exp(ColormaticConfig.scaled(colormaticConfig.relativeBlockLightIntensityExponent))))).append("%"))),
                        (slider) -> {
                            try {
                                colormaticConfig.relativeBlockLightIntensityExponent = ((1.00 - ((slider.getInt())/100f)) * -16.0);
                                ColormaticConfigController.persist(colormaticConfig);
                            }
                            catch (NumberFormatException ignored) {}
            }));
        }
        if (isActive("borderlessmining")) {
            PuzzleApi.addToMiscOptions(new PuzzleWidget(Text.of("Borderless Mining")));
            ConfigHandler bmConfig = ConfigHandler.getInstance();
            PuzzleApi.addToMiscOptions(new PuzzleWidget(Text.translatable("config.borderlessmining.general.enabled"), (button) -> button.setMessage(bmConfig.isEnabledOrPending() ? YES : NO), (button) -> {
                bmConfig.setEnabledPending(!bmConfig.isEnabledOrPending());
                bmConfig.save();
            }));
            if (MinecraftClient.IS_SYSTEM_MAC) {
                PuzzleApi.addToMiscOptions(new PuzzleWidget(Text.translatable("config.borderlessmining.general.enabledmac"), (button) -> button.setMessage(bmConfig.enableMacOS ? YES : NO), (button) -> {
                    bmConfig.enableMacOS = !bmConfig.enableMacOS;
                    bmConfig.setEnabledPending(bmConfig.isEnabled());
                    bmConfig.save();
                }));
            }
        }
        if (isActive("iris")) {
            IrisCompat.init();
        }
    }
    public static boolean lateInitDone = false;
    public static void lateInit() { // Some mods are initialized after Puzzle, so we can't access them in our ClientModInitializer
        if (isActive("lambdynlights")) {
            DynamicLightsConfig ldlConfig = LambDynLights.get().config;

            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Text.of("LambDynamicLights")));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Text.translatable("lambdynlights.option.mode"), (button) -> button.setMessage(ldlConfig.getDynamicLightsMode().getTranslatedText()), (button) -> ldlConfig.setDynamicLightsMode(ldlConfig.getDynamicLightsMode().next())));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Text.translatable("").append("DynLights: ").append(Text.translatable("lambdynlights.option.light_sources.entities")), (button) -> button.setMessage(ldlConfig.getEntitiesLightSource().get() ? YES : NO), (button) -> ldlConfig.getEntitiesLightSource().set(!ldlConfig.getEntitiesLightSource().get())));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Text.translatable("").append("DynLights: ").append(Text.translatable("lambdynlights.option.light_sources.block_entities")), (button) -> button.setMessage(ldlConfig.getBlockEntitiesLightSource().get() ? YES : NO), (button) -> ldlConfig.getBlockEntitiesLightSource().set(!ldlConfig.getBlockEntitiesLightSource().get())));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Text.translatable("").append("DynLights: ").append(Text.translatable("entity.minecraft.creeper")), (button) -> button.setMessage(ldlConfig.getCreeperLightingMode().getTranslatedText()), (button) -> ldlConfig.setCreeperLightingMode(ldlConfig.getCreeperLightingMode().next())));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Text.translatable("").append("DynLights: ").append(Text.translatable("block.minecraft.tnt")), (button) -> button.setMessage(ldlConfig.getTntLightingMode().getTranslatedText()), (button) -> ldlConfig.setTntLightingMode(ldlConfig.getTntLightingMode().next())));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Text.translatable("").append("DynLights: ").append(Text.translatable("lambdynlights.option.light_sources.water_sensitive_check")), (button) -> button.setMessage(ldlConfig.getWaterSensitiveCheck().get() ? YES : NO), (button) -> ldlConfig.getWaterSensitiveCheck().set(!ldlConfig.getWaterSensitiveCheck().get())));
        }
        if (isActive("citresewn") && CITResewnConfig.INSTANCE != null) {
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.of("CIT Resewn")));
            CITResewnConfig citConfig = CITResewnConfig.INSTANCE;
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("config.citresewn.enabled.title"), (button) -> button.setMessage(citConfig.enabled ? YES : NO), (button) -> {
                citConfig.enabled = !citConfig.enabled;
                citConfig.write();
                MinecraftClient.getInstance().reloadResources();
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("config.citresewn.mute_errors.title"), (button) -> button.setMessage(citConfig.mute_errors ? YES : NO), (button) -> {
                citConfig.mute_errors = !citConfig.mute_errors;
                citConfig.write();
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("config.citresewn.mute_warns.title"), (button) -> button.setMessage(citConfig.mute_warns ? YES : NO), (button) -> {
                citConfig.mute_warns = !citConfig.mute_warns;
                citConfig.write();
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("config.citresewn.broken_paths.title"), (button) -> button.setMessage(citConfig.broken_paths ? YES : NO), (button) -> {
                citConfig.broken_paths = !citConfig.broken_paths;
                citConfig.write();
            }));
            PuzzleApi.addToResourceOptions(new PuzzleWidget(0, 100,Text.translatable("config.citresewn.cache_ms.title"), () -> citConfig.cache_ms,
                    (button) -> button.setMessage(message(citConfig)),
                    (slider) -> {
                try {
                    citConfig.cache_ms = slider.getInt();
                }
                catch (NumberFormatException ignored) {}
                citConfig.write();
            }));
        }
        if (isActive("lambdabettergrass")) {
            LBGConfig lbgConfig = LambdaBetterGrass.get().config;
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Text.of("LambdaBetterGrass")));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Text.translatable("lambdabettergrass.option.mode"), (button) -> button.setMessage(lbgConfig.getMode().getTranslatedText()), (button) -> lbgConfig.setMode(lbgConfig.getMode().next())));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(Text.translatable("lambdabettergrass.option.better_snow"), (button) -> button.setMessage(lbgConfig.hasBetterLayer() ? YES : NO), (button) -> lbgConfig.setBetterLayer(!lbgConfig.hasBetterLayer())));
        }
        if (isActive("continuity")) {
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.of("Continuity")));
            ContinuityConfig contConfig = ContinuityConfig.INSTANCE;
            contConfig.getOptionMapView().forEach((s, option) -> {
                if (s.equals("use_manual_culling")) return;
                try {
                    Option.BooleanOption booleanOption = ((Option.BooleanOption)option);
                    PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("options.continuity."+s), (button) -> button.setMessage(booleanOption.get() ? YES : NO), (button) -> {
                        booleanOption.set(!booleanOption.get());
                        contConfig.save();
                    }));
                } catch (Exception ignored) {}
            });
        }
        if (isActive("entity_texture_features")) {
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("config.entity_texture_features.title")));
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
                etfConfig.emissiveRenderMode = etfConfig.emissiveRenderMode.next();
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
        if (isActive("entity_model_features")) {
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("entity_model_features.title")));
            EMFConfig emfConfig = EMFConfig.getConfig();
            PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("entity_model_features.config.force_models"), (button) -> button.setMessage(emfConfig.attemptRevertingEntityModelsAlteredByAnotherMod ? YES : NO), (button) -> {
                emfConfig.attemptRevertingEntityModelsAlteredByAnotherMod = !emfConfig.attemptRevertingEntityModelsAlteredByAnotherMod;
                EMFConfig.EMF_saveConfig();
            }));
            if (PlatformFunctions.isModLoaded("physicsmod")) {
                PuzzleApi.addToResourceOptions(new PuzzleWidget(Text.translatable("entity_model_features.config.physics"), (button) -> button.setMessage(emfConfig.attemptPhysicsModPatch_2 != EMFConfig.PhysicsModCompatChoice.OFF ?
                        Text.translatable("entity_model_features.config." + (emfConfig.attemptPhysicsModPatch_2 == EMFConfig.PhysicsModCompatChoice.VANILLA ? "physics.1" : "physics.2")) : ScreenTexts.OFF), (button) -> {
                    emfConfig.attemptPhysicsModPatch_2 = emfConfig.attemptPhysicsModPatch_2.next();
                    EMFConfig.EMF_saveConfig();
                }));
            }
        }
        lateInitDone = true;
    }
    public static Text message(CITResewnConfig config) {
        int ticks = config.cache_ms;
            if (ticks <= 1) {
                return (Text.translatable("config.citresewn.cache_ms.ticks." + ticks)).formatted(Formatting.AQUA);
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

                return (Text.translatable("config.citresewn.cache_ms.ticks.any", ticks)).formatted(color);
            }
    }
    public static boolean isActive(String modid) {
        return PlatformFunctions.isModLoaded(modid) && !PuzzleConfig.disabledIntegrations.contains(modid);
    }
}
