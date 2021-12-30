package net.puzzlemc.emissives;

import net.fabricmc.api.ClientModInitializer;
import net.puzzlemc.core.config.PuzzleConfig;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PuzzleEmissiveTextures implements ClientModInitializer {
    public static final Map<Identifier, Identifier> emissiveTextures = new HashMap<>();

    public void onInitializeClient() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return new Identifier("puzzle", "emissive_textures");
            }

            @Override
            public void reload(ResourceManager manager) {
                if (PuzzleConfig.emissiveTextures) {
                    String suffix = "_e";

                    for (Identifier id : manager.findResources("optifine", path -> path.contains("emissive.properties"))) {
                        try (InputStream stream = manager.getResource(id).getInputStream()) {
                            Properties properties = new Properties();
                            properties.load(stream);

                            if (properties.get("suffix.emissive") != null) {
                                suffix = properties.get("suffix.emissive").toString();
                            }
                        } catch (Exception e) {
                            LogManager.getLogger("Puzzle").error("Error occurred while loading emissive.properties " + id.toString(), e);
                        }
                        String finalSuffix = suffix;
                        for (Identifier emissiveId : manager.findResources("textures", path -> path.endsWith(finalSuffix + ".png"))) {
                            try {
                                String normalTexture = emissiveId.toString();
                                normalTexture = normalTexture.substring(0, normalTexture.lastIndexOf(finalSuffix));
                                Identifier normalId =  Identifier.tryParse(normalTexture + ".png");
                                emissiveTextures.put(normalId, emissiveId);
                                if (PuzzleConfig.debugMessages) LogManager.getLogger("Puzzle").info(normalId + " " + emissiveId);
                            } catch (Exception e) {
                                LogManager.getLogger("Puzzle").error("Error occurred while loading emissive texture " + emissiveId.toString(), e);
                            }
                        }
                    }
                }
            }
        });
    }
}
