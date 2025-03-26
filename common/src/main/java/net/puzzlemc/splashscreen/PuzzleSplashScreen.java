package net.puzzlemc.splashscreen;

import eu.midnightdust.lib.util.MidnightColorUtil;
import eu.midnightdust.lib.util.PlatformFunctions;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureContents;
import net.minecraft.resource.*;
import net.minecraft.util.Util;
import net.puzzlemc.core.config.PuzzleConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.metadata.TextureResourceMetadata;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.util.Identifier;

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

import static net.puzzlemc.core.PuzzleCore.LOGGER;
import static net.puzzlemc.core.PuzzleCore.MOD_ID;

public class PuzzleSplashScreen {
    public static final Identifier LOGO = Identifier.of("textures/gui/title/mojangstudios.png");
    public static final Identifier BACKGROUND = Identifier.of("puzzle/splash_background.png");
    public static File CONFIG_PATH = new File(String.valueOf(PlatformFunctions.getConfigDirectory().resolve(".puzzle_cache")));
    public static Path LOGO_TEXTURE = Paths.get(CONFIG_PATH + "/mojangstudios.png");
    public static Path BACKGROUND_TEXTURE = Paths.get(CONFIG_PATH + "/splash_background.png");
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static boolean keepBackground = false;

    public static void init() {
        if (!CONFIG_PATH.exists()) { // Run when config directory is nonexistent //
            if (CONFIG_PATH.mkdir()) { // Create our custom config directory //
                if (Util.getOperatingSystem().equals(Util.OperatingSystem.WINDOWS)) {
                    try { Files.setAttribute(CONFIG_PATH.toPath(), "dos:hidden", true);
                    } catch (IOException ignored) {}
                }
            }
        }
    }


    public static class ReloadListener implements SynchronousResourceReloader {
        public static final ReloadListener INSTANCE = new ReloadListener();

        private ReloadListener() {}

        @Override
        public void reload(ResourceManager manager) {
            if (PuzzleConfig.resourcepackSplashScreen) {
                PuzzleSplashScreen.resetColors();
                client.getTextureManager().registerTexture(LOGO, new LogoTexture(LOGO));
                client.getTextureManager().registerTexture(BACKGROUND, new LogoTexture(BACKGROUND));

                manager.findResources("optifine", path -> path.getPath().contains("color.properties")).forEach((id, resource) -> {
                    try (InputStream stream = resource.getInputStream()) {
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
                manager.findResources("textures", path -> path.getPath().contains("mojangstudios.png")).forEach((id, resource) -> {
                    try (InputStream stream = resource.getInputStream()) {
                        Files.copy(stream, LOGO_TEXTURE, StandardCopyOption.REPLACE_EXISTING);
                        client.getTextureManager().registerTexture(LOGO, new DynamicLogoTexture());
                        if (logoCount.get() > 0) PuzzleConfig.hasCustomSplashScreen = true;
                        logoCount.getAndIncrement();
                    } catch (Exception e) {
                        LOGGER.error("Error occurred while loading custom minecraft logo {}", id.toString(), e);
                    }
                });
                manager.findResources(MOD_ID, path -> path.getPath().contains("splash_background.png")).forEach((id, resource) -> {
                    try (InputStream stream = resource.getInputStream()) {
                        Files.copy(stream, BACKGROUND_TEXTURE, StandardCopyOption.REPLACE_EXISTING);
                        InputStream input = new FileInputStream(String.valueOf(PuzzleSplashScreen.BACKGROUND_TEXTURE));
                        client.getTextureManager().registerTexture(BACKGROUND, new NativeImageBackedTexture(() -> "splash_screen_background", NativeImage.read(input)));
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

    public static class LogoTexture extends ResourceTexture {
        public LogoTexture(Identifier logo) { super(logo); }

        @Override
        public TextureContents loadContents(ResourceManager resourceManager) {
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            DefaultResourcePack defaultResourcePack = minecraftClient.getDefaultResourcePack();
            try {
                InputStream inputStream = Objects.requireNonNull(defaultResourcePack.open(ResourceType.CLIENT_RESOURCES, LOGO)).get();
                TextureContents var6;
                try {
                    var6 = new TextureContents(NativeImage.read(inputStream), new TextureResourceMetadata(true, true));
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
                return var6;
            } catch (IOException var18) {
                return TextureContents.createMissing();
            }
        }
    }

    public static class DynamicLogoTexture extends ResourceTexture {
        public DynamicLogoTexture() {
            super(LOGO);
        }
        @Override
        public TextureContents loadContents(ResourceManager resourceManager) {
            try {
                InputStream input = new FileInputStream(String.valueOf(PuzzleSplashScreen.LOGO_TEXTURE));
                return new TextureContents(NativeImage.read(input), new TextureResourceMetadata(true, true));
            } catch (IOException e) {
                LOGGER.error("Encountered an error during logo loading: ", e);
                try {
                    return TextureContents.load(resourceManager, LOGO);
                } catch (IOException ex) {
                    return TextureContents.createMissing();
                }
            }
        }
    }
}
