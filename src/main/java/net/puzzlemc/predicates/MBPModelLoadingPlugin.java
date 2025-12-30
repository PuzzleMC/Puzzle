package net.puzzlemc.predicates;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
//import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import net.puzzlemc.predicates.data.logic.When;
import net.puzzlemc.predicates.util.PredicateStore;
import net.puzzlemc.predicates.util.RegistryUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

//? fabric {
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;

public class MBPModelLoadingPlugin implements PreparableModelLoadingPlugin<HashSet<Identifier>> {


//    @Override
//    public void onInitializeModelLoader(HashSet<Identifier> data, ModelLoadingPlugin.Context pluginContext) {
//        pluginContext.addModels(data);
//    }

    @Override
    public void initialize(HashSet<Identifier> data, ModelLoadingPlugin.Context pluginContext) {
        //pluginContext.addModels(data);
        data.forEach(id -> {
            ExtraModelKey<BlockStateModel> modelKey = ExtraModelKey.create(id::toString);
            PredicateStore.predicates.put(id, modelKey);
            pluginContext.addModel(modelKey, SimpleUnbakedExtraModel.blockStateModel(id));
        });

    }

    public static class ModelIdLoader implements PreparableModelLoadingPlugin.DataLoader<HashSet<Identifier>> {
        //? if < 1.21.10 {
//        @Override
//        public CompletableFuture<HashSet<Identifier>> load(ResourceManager manager, Executor executor) {
//            return CompletableFuture.supplyAsync(() -> collectModels(manager), executor);
//        }
        //?} else {
        @Override
        public CompletableFuture<HashSet<Identifier>> load(PreparableReloadListener.SharedState sharedState, Executor executor) {
            return CompletableFuture.supplyAsync(() -> collectModels(sharedState.resourceManager()), executor);
        }
        //?}
    }
//?} else {
/*public class MBPModelLoadingPlugin {
*///?}

    public static HashSet<Identifier> collectModels(ResourceManager manager) {
        HashSet<Identifier> wantedModels = new HashSet<>();

        Map<Identifier, Resource> map = manager.listResources("mbp", id -> id.getPath().endsWith(".json"));
        for (Identifier id : map.keySet()) {

            try {
                Identifier blockTarget = Identifier.tryParse(id.toString().substring(0,id.toString().length()-5).replace("mbp/", ""));
                JsonObject asset = JsonParser.parseReader(map.get(id).openAsReader()).getAsJsonObject();

                Optional<Block> block = RegistryUtils.getBlock(blockTarget);

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
    }
}
