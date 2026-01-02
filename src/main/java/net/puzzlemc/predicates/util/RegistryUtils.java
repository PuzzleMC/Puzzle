package net.puzzlemc.predicates.util;


import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public class RegistryUtils {

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
            return drm. /*? if < 1.21.4 {*/ /*registry *//*?} else {*/ lookup /*?}*/(Registries.BIOME);
        }
        return Optional.empty();
    };

    public static Optional<Biome> getBiome(Identifier biomeId) {
        Optional<Registry<@NotNull Biome>> registry = BIOME_REGISTRY.get();
        return registry.flatMap(biomes -> biomes.getOptional(biomeId));
    }
    public static Optional<Identifier> getBiome(Biome biome) {
        Optional<Registry<@NotNull Biome>> registry = BIOME_REGISTRY.get();
        return registry.flatMap(biomes -> Optional.ofNullable(biomes.getKey(biome)));
    }
    public static Optional<Block> getBlock(Identifier blockId) {
        return BuiltInRegistries.BLOCK.getOptional(blockId);
    }

}
