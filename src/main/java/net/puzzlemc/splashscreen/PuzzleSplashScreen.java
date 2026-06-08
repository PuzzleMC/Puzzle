package net.puzzlemc.splashscreen;

import com.mojang.blaze3d.platform.NativeImage;
import eu.midnightdust.lib.util.MidnightColorUtil;
import eu.midnightdust.lib.util.PlatformFunctions;
import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ReloadableTexture;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.VanillaPackResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.puzzlemc.core.config.PuzzleConfig;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

//? if >= 1.21.5 {
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import net.minecraft.client.renderer.texture.TextureContents;
import net.puzzlemc.splashscreen.mixin.RenderPipelinesAccessor;
//?}
//? if >= 26.1 {
import com.mojang.blaze3d.pipeline.ColorTargetState;
//?} else {
//import com.mojang.blaze3d.platform.DepthTestFunction;
//?}

//? if = 1.21.4 || = 1.21.5 {
/*import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.TriState;
import net.minecraft.client.renderer.texture.TextureContents;
*///?}

//? if = 1.21.4 {
/*import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.platform.GlStateManager;
import static net.minecraft.client.renderer.RenderStateShard.*;
*///?}

//? if >= 1.21.11
import net.minecraft.client.renderer.texture.MipmapStrategy;

import static net.puzzlemc.core.PuzzleCore.LOGGER;
import static net.puzzlemc.core.PuzzleCore.MOD_ID;

public class PuzzleSplashScreen {
    public static final Identifier LOGO = Identifier.fromNamespaceAndPath("minecraft", "textures/gui/title/mojangstudios.png");
    public static final Identifier BACKGROUND = Identifier.fromNamespaceAndPath("minecraft", "puzzle/splash_background.png");
    public static File CONFIG_PATH = new File(String.valueOf(PlatformFunctions.getConfigDirectory().resolve(".puzzle_cache")));
    public static Path LOGO_TEXTURE = Paths.get(CONFIG_PATH + "/mojangstudios.png");
    public static Path BACKGROUND_TEXTURE = Paths.get(CONFIG_PATH + "/splash_background.png");
    private static Minecraft client = Minecraft.getInstance();
    private static boolean keepBackground = false;
    //? if >= 1.21.5
    public static RenderPipeline CUSTOM_LOGO_PIPELINE;
    //? if = 1.21.5 || = 1.21.4
    /*public static RenderType CUSTOM_LOGO_LAYER;*/

    public static void init() {
        if (!CONFIG_PATH.exists()) { // Run when config directory is nonexistent //
            if (CONFIG_PATH.mkdir()) { // Create our custom config directory //
                if (Util.getPlatform().equals(Util.OS.WINDOWS)) {
                    try { Files.setAttribute(CONFIG_PATH.toPath(), "dos:hidden", true);
                    } catch (IOException ignored) {}
                }
            }
        }
        //? if >= 1.21.4
        buildRenderLayer();
    }

    //? if >= 1.21.4 {
    public static void buildRenderLayer() {
        if (PuzzleConfig.resourcepackSplashScreen) {
            //? if >= 1.21.5 {
            BlendFunction blendFunction = new BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE);
            if (PuzzleConfig.disableBlend) blendFunction = null;
            else if (PuzzleConfig.customBlendFunction.size() == 4) {
                try {
                    blendFunction = new BlendFunction(
                            SourceFactor.valueOf(PuzzleConfig.customBlendFunction.get(0)),
                            DestFactor.valueOf(PuzzleConfig.customBlendFunction.get(1)),
                            SourceFactor.valueOf(PuzzleConfig.customBlendFunction.get(2)),
                            DestFactor.valueOf(PuzzleConfig.customBlendFunction.get(3)));
                } catch (Exception e) {
                    LOGGER.error("Incorrect blend function defined in color.properties: {}{}", PuzzleConfig.customBlendFunction, e.getMessage());
                }
            }

            var CUSTOM_LOGO_PIPELINE_BUILDER = RenderPipeline.builder(RenderPipelinesAccessor.getGUI_TEXTURED_SNIPPET())
                .withLocation("pipeline/mojang_logo_puzzle")
            //? if >= 26.1 {
                .withDepthStencilState(Optional.empty());
            CUSTOM_LOGO_PIPELINE_BUILDER = blendFunction != null ? CUSTOM_LOGO_PIPELINE_BUILDER.withColorTargetState(new ColorTargetState(blendFunction)) : CUSTOM_LOGO_PIPELINE_BUILDER;
            //?} else {
            /*
                .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
                .withDepthWrite(false);
            CUSTOM_LOGO_PIPELINE_BUILDER = blendFunction != null ? CUSTOM_LOGO_PIPELINE_BUILDER.withBlend(blendFunction) : CUSTOM_LOGO_PIPELINE_BUILDER.withoutBlend();
            *///?}
            CUSTOM_LOGO_PIPELINE = CUSTOM_LOGO_PIPELINE_BUILDER.build();
            //?}

            //? if = 1.21.5 {
            /*CUSTOM_LOGO_LAYER = RenderType.create("mojang_logo_puzzle", 786432, CUSTOM_LOGO_PIPELINE,
                    RenderType.CompositeState.builder()
                            .setTextureState(new RenderStateShard.TextureStateShard(LoadingOverlay.MOJANG_STUDIOS_LOGO_LOCATION, TriState.DEFAULT, false))
                            .createCompositeState(false));
            *///?} else if = 1.21.4 {
            /*RenderStateShard.TransparencyStateShard transparency = new RenderStateShard.TransparencyStateShard("puzzle_logo_transparency", () -> {
                RenderSystem.enableBlend();
                if (PuzzleConfig.disableBlend) RenderSystem.disableBlend();
                else if (PuzzleConfig.customBlendFunction.size() == 4) {
                    try {
                        RenderSystem.blendFuncSeparate(
                                GlStateManager.SourceFactor.valueOf(PuzzleConfig.customBlendFunction.get(0)),
                                GlStateManager.DestFactor.valueOf(PuzzleConfig.customBlendFunction.get(1)),
                                GlStateManager.SourceFactor.valueOf(PuzzleConfig.customBlendFunction.get(2)),
                                GlStateManager.DestFactor.valueOf(PuzzleConfig.customBlendFunction.get(3)));
                    } catch (Exception e) {
                        LOGGER.error("Incorrect blend function defined in color.properties: {}{}", PuzzleConfig.customBlendFunction, e.getMessage());
                    }
                }
            }, () -> {
                RenderSystem.disableBlend();
                RenderSystem.defaultBlendFunc();
            });
            CUSTOM_LOGO_LAYER = RenderType.create("mojang_logo", DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS, 786432, RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(LoadingOverlay.MOJANG_STUDIOS_LOGO_LOCATION, TriState.DEFAULT, false))
                    .setShaderState(POSITION_TEXTURE_COLOR_SHADER)
                    .setTransparencyState(transparency)
                    .setDepthTestState(NO_DEPTH_TEST)
                    .setWriteMaskState(COLOR_WRITE)
                    .createCompositeState(false));
            *///?}
        }
    }
    //?}

    public static class ReloadListener implements ResourceManagerReloadListener {
        public static final ReloadListener INSTANCE = new ReloadListener();

        private ReloadListener() {}

        @Override
        public void onResourceManagerReload(ResourceManager manager) {
            client = Minecraft.getInstance();
            if (PuzzleConfig.resourcepackSplashScreen) {
                PuzzleSplashScreen.resetColors();
                client.getTextureManager()./*? if >= 1.21.4 {*/ registerAndLoad /*?} else {*//*register*//*?}*/(LOGO, new LogoTexture(LOGO));
                client.getTextureManager()./*? if >= 1.21.4 {*/ registerAndLoad /*?} else {*//*register*//*?}*/(BACKGROUND, new LogoTexture(BACKGROUND));

                manager.listResources("optifine", path -> path.getPath().contains("color.properties")).forEach((id, resource) -> {
                    try (InputStream stream = resource.open()) {
                        Properties properties = new Properties();
                        properties.load(stream);

                        if (properties.get("screen.loading") != null) {
                            PuzzleConfig.backgroundColor = MidnightColorUtil.hex2Rgb(properties.get("screen.loading").toString()).getRGB();
                            PuzzleConfig.hasCustomSplashScreen = true;
                        }
                        if (properties.get("screen.loading.bar") != null) {
                            PuzzleConfig.progressBarBackgroundColor = MidnightColorUtil.hex2Rgb(properties.get("screen.loading.bar").toString()).getRGB();
                            PuzzleConfig.hasCustomSplashScreen = true;
                        }
                        if (properties.get("screen.loading.progress") != null) {
                            PuzzleConfig.progressBarColor = MidnightColorUtil.hex2Rgb(properties.get("screen.loading.progress").toString()).getRGB();
                            PuzzleConfig.hasCustomSplashScreen = true;
                        }
                        if (properties.get("screen.loading.outline") != null) {
                            PuzzleConfig.progressFrameColor = MidnightColorUtil.hex2Rgb(properties.get("screen.loading.outline").toString()).getRGB();
                            PuzzleConfig.hasCustomSplashScreen = true;
                        }
                        if (properties.get("screen.loading.blend") != null) {
                            // Recommended blend: SRC_ALPHA ONE_MINUS_SRC_ALPHA ONE ZERO
                            PuzzleConfig.disableBlend = properties.get("screen.loading.blend").toString().equals("off");
                            PuzzleConfig.customBlendFunction = new ArrayList<>(Arrays.stream(properties.get("screen.loading.blend").toString().split(" ")).toList());
                            PuzzleConfig.hasCustomSplashScreen = true;
                        }

                    } catch (Exception e) {
                        LOGGER.error("Error occurred while loading color.properties {}", id.toString(), e);
                    }
                });
                AtomicInteger logoCount = new AtomicInteger();
                manager.listResources("textures", path -> path.getPath().contains("mojangstudios.png")).forEach((id, resource) -> {
                    try (InputStream stream = resource.open()) {
                        Files.copy(stream, LOGO_TEXTURE, StandardCopyOption.REPLACE_EXISTING);
                        client.getTextureManager()./*? if >= 1.21.4 {*/ registerAndLoad /*?} else {*//*register*//*?}*/(LOGO, new DynamicLogoTexture());
                        if (logoCount.get() > 0) PuzzleConfig.hasCustomSplashScreen = true;
                        logoCount.getAndIncrement();
                    } catch (Exception e) {
                        LOGGER.error("Error occurred while loading custom minecraft logo {}", id.toString(), e);
                    }
                });
                manager.listResources(MOD_ID, path -> path.getPath().contains("splash_background.png")).forEach((id, resource) -> {
                    try (InputStream stream = resource.open()) {
                        Files.copy(stream, BACKGROUND_TEXTURE, StandardCopyOption.REPLACE_EXISTING);
                        InputStream input = new FileInputStream(String.valueOf(PuzzleSplashScreen.BACKGROUND_TEXTURE));
                        client.getTextureManager().register(BACKGROUND, new DynamicTexture(/*? if >= 1.21.5 {*/ () -> "splash_screen_background", /*?}*/ NativeImage.read(input)));
                        keepBackground = true;
                        PuzzleConfig.hasCustomSplashScreen = true;
                    } catch (Exception e) {
                        LOGGER.error("Error occurred while loading custom splash background {}", id.toString(), e);
                    }
                });
                if (!keepBackground) {
                    try {
                        Files.delete(BACKGROUND_TEXTURE);
                    } catch (Exception ignored) {}
                }
                keepBackground = false;
                PuzzleConfig.write(MOD_ID);
                //? if >= 1.21.5
                buildRenderLayer();
            }
        }
    }

    public static void resetColors() {
        PuzzleConfig.backgroundColor = 15675965;
        PuzzleConfig.progressBarColor = 16777215;
        PuzzleConfig.progressBarBackgroundColor = 15675965;
        PuzzleConfig.progressFrameColor = 16777215;
        PuzzleConfig.disableBlend = false;
        PuzzleConfig.customBlendFunction = new ArrayList<>();
        PuzzleConfig.hasCustomSplashScreen = false;
    }


    public static class LogoTexture extends ReloadableTexture {
        public LogoTexture(Identifier logo) {
            super(logo);
        }

        @Override
        public @NotNull TextureContents loadContents(ResourceManager resourceManager) {
            Minecraft client = Minecraft.getInstance();
            VanillaPackResources defaultResourcePack = client.getVanillaPackResources();
            try (InputStream input = Objects.requireNonNull(defaultResourcePack.getResource(PackType.CLIENT_RESOURCES, LOGO)).get()) {
                return  /*? if >= 1.21.4 {*/ new TextureContents(NativeImage.read(input), new TextureMetadataSection(true, true /*? if >= 1.21.11 {*/, MipmapStrategy.AUTO, 0 /*?}*/)) /*?} else {*/ /*new TextureContents(new TextureMetadataSection(true, true), NativeImage.read(input))  *//*?}*/;
            } catch (IOException ex) {
                return /*? if >= 1.21.4 {*/ TextureContents.createMissing() /*?} else {*/ /*new TextureContents(ex) *//*?}*/;
            }
        }
    }

    public static class DynamicLogoTexture extends ReloadableTexture {
        public DynamicLogoTexture() {
            super(LOGO);
        }
        @Override
        public @NotNull TextureContents loadContents(ResourceManager resourceManager) {
            try (InputStream input = new FileInputStream(String.valueOf(PuzzleSplashScreen.LOGO_TEXTURE))) {
                return  /*? if >= 1.21.4 {*/ new TextureContents(NativeImage.read(input), new TextureMetadataSection(true, true/*? if >= 1.21.11 {*/, MipmapStrategy.AUTO, 0 /*?}*/)) /*?} else {*/ /*new TextureContents(new TextureMetadataSection(true, true), NativeImage.read(input))  *//*?}*/;
            } catch (IOException e) {
                LOGGER.error("Encountered an error during logo loading: ", e);
                //? if >= 1.21.4 {
                try {
                    return TextureContents.load(resourceManager, LOGO);
                } catch (IOException ex) {
                    return TextureContents.createMissing();
                }
                //?} else {
                /*return TextureContents.load(resourceManager, LOGO);
                *///?}
            }
        }
    }
}