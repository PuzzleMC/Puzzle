package net.puzzlemc.blocks;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.impl.blockrenderlayer.BlockRenderLayerMapImpl;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.puzzlemc.core.config.PuzzleConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class PuzzleBlocks implements ClientModInitializer {
    Logger LOGGER = LogManager.getLogger("puzzle-blocks");

    public void onInitializeClient() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return new Identifier("puzzle", "blocks");
            }

            @Override
            public void reload(ResourceManager manager) {
                if (PuzzleConfig.customRenderLayers) {
                    for (Identifier id : manager.findResources("optifine", path -> path.contains("block.properties"))) {
                        try (InputStream stream = manager.getResource(id).getInputStream()) {
                            Properties properties = new Properties();
                            properties.load(stream);

                            if (properties.get("layer.solid") != null) {
                                String[] solid_blocks = properties.get("layer.solid").toString().split(" ");
                                Arrays.stream(solid_blocks).iterator().forEachRemaining(string -> {
                                    if (PuzzleConfig.debugMessages) LOGGER.info(string + " -> solid");
                                    Block block = Registry.BLOCK.getOrEmpty(Identifier.tryParse(string)).get();
                                    if (Registry.BLOCK.getOrEmpty(Identifier.tryParse(string)).isPresent()) {
                                        BlockRenderLayerMapImpl.INSTANCE.putBlock(block, RenderLayer.getSolid());
                                    }
                                });
                            }
                            if (properties.get("layer.cutout") != null) {
                                String[] solid_blocks = properties.get("layer.cutout").toString().split(" ");
                                Arrays.stream(solid_blocks).iterator().forEachRemaining(string -> {
                                    if (PuzzleConfig.debugMessages) LOGGER.info(string + " -> cutout");
                                    Block block = Registry.BLOCK.getOrEmpty(Identifier.tryParse(string)).get();
                                    if (Registry.BLOCK.getOrEmpty(Identifier.tryParse(string)).isPresent()) {
                                        BlockRenderLayerMapImpl.INSTANCE.putBlock(block, RenderLayer.getCutout());
                                    }
                                });
                            }
                            if (properties.get("layer.cutout_mipped") != null) {
                                String[] solid_blocks = properties.get("layer.cutout_mipped").toString().split(" ");
                                Arrays.stream(solid_blocks).iterator().forEachRemaining(string -> {
                                    if (PuzzleConfig.debugMessages) LOGGER.info(string + " -> cutout_mipped");
                                    Block block = Registry.BLOCK.getOrEmpty(Identifier.tryParse(string)).get();
                                    if (Registry.BLOCK.getOrEmpty(Identifier.tryParse(string)).isPresent()) {
                                        BlockRenderLayerMapImpl.INSTANCE.putBlock(block, RenderLayer.getCutoutMipped());
                                    }
                                });
                            }
                            if (properties.get("layer.translucent") != null) {
                                String[] solid_blocks = properties.get("layer.translucent").toString().split(" ");
                                Arrays.stream(solid_blocks).iterator().forEachRemaining(string -> {
                                    if (PuzzleConfig.debugMessages) LOGGER.info(string + " -> translucent");
                                    Block block = Registry.BLOCK.getOrEmpty(Identifier.tryParse(string)).get();
                                    if (Registry.BLOCK.getOrEmpty(Identifier.tryParse(string)).isPresent()) {
                                        BlockRenderLayerMapImpl.INSTANCE.putBlock(block, RenderLayer.getTranslucent());
                                    }
                                });
                            }
                        } catch (Exception e) {
                            LogManager.getLogger("Puzzle").error("Error occurred while loading block.properties " + id.toString(), e);
                        }
                    }
                }
            }
        });
    }
}
