package net.puzzlemc.predicates.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import net.puzzlemc.predicates.data.logic.When;
import net.puzzlemc.predicates.util.Utils;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class MBPModelLoadingPlugin implements PreparableModelLoadingPlugin<HashSet<ResourceLocation>> {


    @Override
    public void onInitializeModelLoader(HashSet<ResourceLocation> data, ModelLoadingPlugin.Context pluginContext) {
        pluginContext.addModels(data);
    }

    public static class ModelIdLoader implements PreparableModelLoadingPlugin.DataLoader<HashSet<ResourceLocation>> {
        @Override
        public CompletableFuture<HashSet<ResourceLocation>> load(ResourceManager manager, Executor executor) {

            return CompletableFuture.supplyAsync(() -> {
                HashSet<ResourceLocation> wantedModels = new HashSet<>();

                Map<ResourceLocation, Resource> map = manager.listResources("mbp", id -> id.getPath().endsWith(".json"));
                for (ResourceLocation id : map.keySet()) {

                    try {
                        ResourceLocation blockTarget = new ResourceLocation(id.toString().substring(0,id.toString().length()-5).replace("mbp/", ""));
                        JsonObject asset = JsonParser.parseReader(map.get(id).openAsReader()).getAsJsonObject();

                        Optional<Block> block = Utils.getBlock(blockTarget);

                        if (block.isPresent()) {
                            JsonArray overrides = asset.getAsJsonArray("overrides");
                            for(JsonElement overrideEntry : overrides) {
                                try {
                                    When when = When.parse(overrideEntry);

                                    wantedModels.addAll(when.getModels());
                                } catch (Exception e) {
                                    PuzzlePredicates.LOGGER.error("Error found in file: " + id);
                                    e.printStackTrace();
                                }
                            }
                        }

                    } catch (Exception e) {
                        PuzzlePredicates.LOGGER.error("Error found in file: " + id);
                        e.printStackTrace();
                    }
                }
                return wantedModels;
            }, executor);


        }
    }
}
