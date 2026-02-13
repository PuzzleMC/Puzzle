package net.puzzlemc.predicates;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import net.puzzlemc.core.config.PuzzleConfig;
import net.puzzlemc.predicates.data.logic.When;
import net.puzzlemc.predicates.util.ConditionCheck;
import net.puzzlemc.predicates.util.ModelData;

import java.util.*;

import org.jetbrains.annotations.NotNull;

import static net.puzzlemc.predicates.PuzzlePredicates.LOGGER;

//? fabric {
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
//? if > 1.21.4 {
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.minecraft.server.packs.resources.PreparableReloadListener;
//?}

public class MBPModelLoadingPlugin implements PreparableModelLoadingPlugin<@NotNull HashSet<ModelData>> {

    //? if < 1.21.5 {
    /*@Override
    public void /^? if = 1.21.4 {^/ /^initialize ^//^?} else {^/ onInitializeModelLoader /^?}^/ (HashSet<ModelData> set, ModelLoadingPlugin.Context pluginContext) {
        set.forEach(data -> pluginContext.addModels(data.modelLocation()));
        ModelData.clearCache();
    }
    *///?} else {
    @Override
    public void initialize(HashSet<ModelData> set, ModelLoadingPlugin.@NotNull Context pluginContext) {
        set.forEach(data -> {
            pluginContext.addModel(data.modelKey, SimpleUnbakedExtraModel.blockStateModel(data.modelLocation(), data.asModelState()));
        });
        ModelData.clearCache();
    }
    //?}

    public static class ModelIdLoader implements PreparableModelLoadingPlugin.DataLoader<@NotNull HashSet<ModelData>> {
        //? if < 1.21.10 {
        /*@Override
        public CompletableFuture<HashSet<ModelData>> load(ResourceManager manager, Executor executor) {
            return CompletableFuture.supplyAsync(() -> collectModels(manager), executor);
        }
        *///?} else {
        @Override
        public @NotNull CompletableFuture<HashSet<ModelData>> load(PreparableReloadListener.@NotNull SharedState sharedState, @NotNull Executor executor) {
            return CompletableFuture.supplyAsync(() -> collectModels(sharedState.resourceManager()), executor);
        }
        //?}
    }
//?} else if neoforge {
/*import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;

//? if > 1.21.5 {
/^import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import net.minecraft.client.renderer.block.model.BlockStateModel;
^///?} else if = 1.21.5 {
/^import net.neoforged.neoforge.client.model.standalone.StandaloneModelBaker;
^///?} else {
import net.minecraft.client.resources.model.ModelIdentifier;
//?}

import static net.puzzlemc.core.PuzzleCore.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT /^? if <= 1.21.5 {^/ , bus = EventBusSubscriber.Bus.MOD /^?}^/)
public class MBPModelLoadingPlugin {
*///?}

    //? neoforge && < 1.21.5 {
    /*@SubscribeEvent
    private static void load(ModelEvent.RegisterAdditional event) {
        MBPModelLoadingPlugin.collectModels(Minecraft.getInstance().getResourceManager()).forEach(data -> event.register(/^? if > 1.21.1 {^/ /^data.modelLocation() ^//^?} else {^/ new ModelIdentifier(data.modelLocation(), "standalone") /^?}^/));
        ModelData.clearCache();
    }
    *///?} else if neoforge {
    /*@SubscribeEvent
    private static void load(ModelEvent.RegisterStandalone event) {
        MBPModelLoadingPlugin.collectModels(Minecraft.getInstance().getResourceManager()).forEach(data -> {
            event.register(data.modelKey, /^? if < 1.21.6 {^/ StandaloneModelBaker.blockStateModel(data.asModelState()) /^?} else {^/ /^SimpleUnbakedStandaloneModel.blockStateModel(data.modelLocation(), data.asModelState())^//^?}^/);
        });
        ModelData.clearCache();
    }
    *///?}

    public static HashSet<ModelData> collectModels(ResourceManager manager) {
        HashSet<ModelData> wantedModels = new HashSet<>();
        ConditionCheck.PREDICATES.clear();

        Map<Identifier, Resource> map = manager.listResources("mbp", id -> id.getPath().endsWith(".json"));

        for (Identifier id : map.keySet()) {
            try {
                Identifier blockTarget = Identifier.tryParse(id.toString().substring(0,id.toString().length()-5).replace("mbp/", ""));
                JsonObject asset = JsonParser.parseReader(map.get(id).openAsReader()).getAsJsonObject();

                Optional<Block> block = BuiltInRegistries.BLOCK.getOptional(blockTarget);

                if (block.isPresent()) {
                    JsonArray overrides = asset.getAsJsonArray("overrides");
                    List<When> whenList = new ArrayList<>();
                    for(JsonElement overrideEntry : overrides) {
                        try {
                            When when = When.parse(overrideEntry);
                            whenList.add(when);
                            wantedModels.addAll(when.getModels());
                        } catch (Exception e) {
                            logError(id, e);
                        }
                    }
                    ConditionCheck.PREDICATES.put(block.get(), Collections.unmodifiableList(whenList));
                } else {
                    if (PuzzleConfig.debugMessages)
                        PuzzlePredicates.LOGGER.error("Block entry not found in file {}: {}", id, blockTarget);
                }

            } catch (Exception e) {
                logError(id, e);
            }
        }

        return wantedModels;
    }

    private static void logError(Identifier id, Exception e) {
        LOGGER.error("Error found in file: {}\n{}", id, e);
    }
}
