package net.puzzlemc.splashscreen;

import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import eu.midnightdust.lib.util.MidnightColorUtil;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.puzzlemc.core.config.PuzzleConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.metadata.TextureResourceMetadata;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.resource.DefaultResourcePack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Properties;

public class PuzzleSplashScreen implements ClientModInitializer {
    public static final Identifier LOGO = new Identifier("textures/gui/title/mojangstudios.png");
    public static final Identifier BACKGROUND = new Identifier("optifine/splash_background.png");
    public static File CONFIG_PATH = new File(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve(".puzzle_cache")));
    public static Path LOGO_TEXTURE = Paths.get(CONFIG_PATH + "/mojangstudios.png");
    public static Path BACKGROUND_TEXTURE = Paths.get(CONFIG_PATH + "/splash_background.png");
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static boolean keepBackground = true;

    public static void resetColors() {
        PuzzleConfig.backgroundColor = 15675965;
        PuzzleConfig.progressBarColor = 16777215;
        PuzzleConfig.progressBarBackgroundColor = 15675965;
        PuzzleConfig.progressFrameColor = 16777215;
        PuzzleConfig.disableBlend = false;
        PuzzleConfig.write("puzzle");
    }

    public void onInitializeClient() {
        if (!CONFIG_PATH.exists()) { // Run when config directory is nonexistent //
            if (CONFIG_PATH.mkdir()) { // Create our custom config directory //
                if (Util.getOperatingSystem().equals(Util.OperatingSystem.WINDOWS)) {
                    try {
                        Files.setAttribute(CONFIG_PATH.toPath(), "dos:hidden", true);
                    } catch (IOException ignored) {
                    }
                }
            }
        }
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return new Identifier("puzzle", "splash_screen");
            }
            @Override
            public void reload(ResourceManager manager) {
                if (PuzzleConfig.resourcepackSplashScreen) {
                    PuzzleSplashScreen.resetColors();
                    client.getTextureManager().registerTexture(LOGO, new LogoTexture(LOGO));
                    client.getTextureManager().registerTexture(BACKGROUND, new LogoTexture(BACKGROUND));

                    manager.findResources("optifine", path -> path.getPath().contains("color.properties")).forEach((id, resource) -> {
                        try (InputStream stream = manager.getResource(id).get().getInputStream()) {
                            Properties properties = new Properties();
                            properties.load(stream);

                            if (properties.get("screen.loading") != null) {
                                PuzzleConfig.backgroundColor = MidnightColorUtil.hex2Rgb(properties.get("screen.loading").toString()).getRGB();
                            }
                            if (properties.get("screen.loading.bar") != null) {
                                PuzzleConfig.progressBarBackgroundColor = MidnightColorUtil.hex2Rgb(properties.get("screen.loading.bar").toString()).getRGB();
                            }
                            if (properties.get("screen.loading.progress") != null) {
                                PuzzleConfig.progressBarColor = MidnightColorUtil.hex2Rgb(properties.get("screen.loading.progress").toString()).getRGB();
                            }
                            if (properties.get("screen.loading.outline") != null) {
                                PuzzleConfig.progressFrameColor = MidnightColorUtil.hex2Rgb(properties.get("screen.loading.outline").toString()).getRGB();
                            }
                            if (properties.get("screen.loading.blend") != null) {
                                PuzzleConfig.disableBlend = properties.get("screen.loading.blend").toString().equals("off");
                                PuzzleConfig.progressFrameColor = MidnightColorUtil.hex2Rgb(properties.get("screen.loading.outline").toString()).getRGB();
                            }
                            PuzzleConfig.write("puzzle");
                        } catch (Exception e) {
                            LogManager.getLogger("Puzzle").error("Error occurred while loading color.properties " + id.toString(), e);
                        }
                    });
                    manager.findResources("textures", path -> path.getPath().contains("mojangstudios.png")).forEach((id, resource) -> {
                        try (InputStream stream = manager.getResource(id).get().getInputStream()) {
                            Files.copy(stream, LOGO_TEXTURE, StandardCopyOption.REPLACE_EXISTING);
                            client.getTextureManager().registerTexture(LOGO, new DynamicLogoTexture());
                        } catch (Exception e) {
                            LogManager.getLogger("Puzzle").error("Error occurred while loading custom minecraft logo " + id.toString(), e);
                        }
                    });
                    manager.findResources("puzzle", path -> path.getPath().contains("splash_background.png")).forEach((id, resource) -> {
                        try (InputStream stream = manager.getResource(id).get().getInputStream()) {
                            Files.copy(stream, BACKGROUND_TEXTURE, StandardCopyOption.REPLACE_EXISTING);
                            InputStream input = new FileInputStream(String.valueOf(PuzzleSplashScreen.BACKGROUND_TEXTURE));
                            client.getTextureManager().registerTexture(BACKGROUND, new NativeImageBackedTexture(NativeImage.read(input)));
                            keepBackground = true;
                        } catch (Exception e) {
                            LogManager.getLogger("Puzzle").error("Error occurred while loading custom splash background " + id.toString(), e);
                        }
                    });
                    if (!keepBackground) {
                        try {
                            Files.delete(BACKGROUND_TEXTURE);
                        } catch (Exception ignored) {}
                    }
                    keepBackground = false;
                }
            }
        });
    }
    @Environment(EnvType.CLIENT)
    public static class LogoTexture extends ResourceTexture {
        public LogoTexture(Identifier logo) { super(logo); }

        protected TextureData loadTextureData(ResourceManager resourceManager) {
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            DefaultResourcePack defaultResourcePack = minecraftClient.getDefaultResourcePack();
            try {
                InputStream inputStream = Objects.requireNonNull(defaultResourcePack.open(ResourceType.CLIENT_RESOURCES, LOGO)).get();
                TextureData var6;
                try {
                    var6 = new TextureData(new TextureResourceMetadata(true, true), NativeImage.read(inputStream));
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
                return var6;
            } catch (IOException var18) {
                return new TextureData(var18);
            }
        }
    }
    @Environment(EnvType.CLIENT)
    public static class DynamicLogoTexture extends ResourceTexture {
        public DynamicLogoTexture() {
            super(LOGO);
        }
        protected TextureData loadTextureData(ResourceManager resourceManager) {
            try {
                InputStream input = new FileInputStream(String.valueOf(PuzzleSplashScreen.LOGO_TEXTURE));
                return new TextureData(new TextureResourceMetadata(true, true), NativeImage.read(input));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
