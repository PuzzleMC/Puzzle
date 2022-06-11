package net.puzzlemc.splashscreen;

import eu.midnightdust.lib.util.MidnightColorUtil;
import net.fabricmc.api.ClientModInitializer;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class PuzzleSplashScreen implements ClientModInitializer {
    public static final Identifier LOGO = new Identifier("textures/gui/title/mojangstudios.png");
    public static File CONFIG_PATH = new File(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve(".puzzle_cache")));
    public static Path LOGO_TEXTURE = Paths.get(CONFIG_PATH + "/mojangstudios.png");
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static void resetColors() {
        PuzzleConfig.backgroundColor = 15675965;
        PuzzleConfig.progressBarColor = 16777215;
        PuzzleConfig.progressBarBackgroundColor = 15675965;
        PuzzleConfig.progressFrameColor = 16777215;
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

            public void method_14491(ResourceManager manager) {
                reload(manager);
            }
            @Override
            public void reload(ResourceManager manager) {
                if (PuzzleConfig.resourcepackSplashScreen) {
                    PuzzleSplashScreen.resetColors();
                    client.getTextureManager().registerTexture(LOGO, new LogoTexture());

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
                            if (properties.get("screen.loading") != null) {
                                PuzzleConfig.write("puzzle");
                            }
                        } catch (Exception e) {
                            LogManager.getLogger("Puzzle").error("Error occurred while loading color.properties " + id.toString(), e);
                        }
                    });
                    manager.findResources("textures", path -> path.getPath().contains("mojangstudios.png")).forEach((id, resource) -> {
                        try (InputStream stream = manager.getResource(id).get().getInputStream()) {
                            Files.copy(stream, LOGO_TEXTURE, StandardCopyOption.REPLACE_EXISTING);
                            DefaultResourcePack defaultResourcePack = client.getResourcePackProvider().getPack();
                            InputStream defaultLogo = defaultResourcePack.open(ResourceType.CLIENT_RESOURCES, LOGO);
                            InputStream input = new FileInputStream(String.valueOf(PuzzleSplashScreen.LOGO_TEXTURE));
                            if (input != defaultLogo)
                            client.getTextureManager().registerTexture(LOGO, new NativeImageBackedTexture(NativeImage.read(input)));
                            else Files.delete(LOGO_TEXTURE);
                        } catch (Exception e) {
                            LogManager.getLogger("Puzzle").error("Error occurred while loading custom minecraft logo " + id.toString(), e);
                        }
                    });
                }
            }
        });
    }
    @Environment(EnvType.CLIENT)
    public static class LogoTexture extends ResourceTexture {
        public LogoTexture() { super(LOGO); }

        protected TextureData loadTextureData(ResourceManager resourceManager) {
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            DefaultResourcePack defaultResourcePack = minecraftClient.getResourcePackProvider().getPack();
            try {
                InputStream inputStream = defaultResourcePack.open(ResourceType.CLIENT_RESOURCES, LOGO);
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
}
