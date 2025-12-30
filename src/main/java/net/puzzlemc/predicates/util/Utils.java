package net.puzzlemc.predicates.util;


import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public class Utils {

    private static RegistryAccess getRegistryManager() {
        Minecraft instance = Minecraft.getInstance();
        if (instance.level != null) {
            return instance.level.registryAccess();
        }
        return null;
    }
    public static final Supplier<Optional<Registry<@NotNull Biome>>> BIOME_REGISTRY = () -> {
        RegistryAccess drm = getRegistryManager();
        if (drm != null) {
            return drm.registry(Registries.BIOME);
        }
        return Optional.empty();
    };

    public static Optional<Biome> getBiome(ResourceLocation biomeId) {
        Optional<Registry<@NotNull Biome>> registry = BIOME_REGISTRY.get();
        return registry.flatMap(biomes -> biomes.getOptional(biomeId));
    }
    public static Optional<ResourceLocation> getBiome(Biome biome) {
        Optional<Registry<@NotNull Biome>> registry = BIOME_REGISTRY.get();
        return registry.flatMap(biomes -> Optional.ofNullable(biomes.getKey(biome)));
    }
    public static Optional<Block> getBlock(ResourceLocation blockId) {
        return BuiltInRegistries.BLOCK.getOptional(blockId);
    }

}
