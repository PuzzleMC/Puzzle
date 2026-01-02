package net.puzzlemc.predicates;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import net.puzzlemc.predicates.data.logic.When;
import net.puzzlemc.predicates.util.PredicateStore;
import net.puzzlemc.predicates.util.RegistryUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

//? fabric {
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
//? if > 1.21.4 {
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.minecraft.server.packs.resources.PreparableReloadListener;
//?}

public class MBPModelLoadingPlugin implements PreparableModelLoadingPlugin<@NotNull HashSet<Identifier>> {

    //? if < 1.21.5 {
    /*@Override
    public void /^? if = 1.21.4 {^/ /^initialize ^//^?} else {^/ onInitializeModelLoader /^?}^/ (HashSet<Identifier> data, ModelLoadingPlugin.Context pluginContext) {
        pluginContext.addModels(data);
    }
    *///?} else {
    @Override
    public void initialize(HashSet<Identifier> data, ModelLoadingPlugin.@NotNull Context pluginContext) {
        data.forEach(id -> {
            ExtraModelKey<@NotNull BlockStateModel> modelKey = ExtraModelKey.create(id::toString);
            PredicateStore.predicates.put(id, modelKey);
            pluginContext.addModel(modelKey, SimpleUnbakedExtraModel.blockStateModel(id));
        });
    }
    //?}

    public static class ModelIdLoader implements PreparableModelLoadingPlugin.DataLoader<@NotNull HashSet<Identifier>> {
        //? if < 1.21.10 {
        /*@Override
        public CompletableFuture<HashSet<Identifier>> load(ResourceManager manager, Executor executor) {
            return CompletableFuture.supplyAsync(() -> collectModels(manager), executor);
        }
        *///?} else {
        @Override
        public @NotNull CompletableFuture<HashSet<Identifier>> load(PreparableReloadListener.@NotNull SharedState sharedState, @NotNull Executor executor) {
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
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import net.minecraft.client.renderer.block.model.BlockStateModel;
//?} else if = 1.21.5 {
//import net.neoforged.neoforge.client.model.standalone.StandaloneModelBaker;
//import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
//import net.minecraft.client.renderer.block.model.BlockStateModel;
//?} else {
/^import net.minecraft.client.resources.model.ModelIdentifier;
^///?}

import static net.puzzlemc.core.PuzzleCore.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT /^? if <= 1.21.5 {^/ /^, bus = EventBusSubscriber.Bus.MOD ^//^?}^/)
public class MBPModelLoadingPlugin {
*///?}

    //? neoforge && < 1.21.5 {
    /*@SubscribeEvent
    private static void load(ModelEvent.RegisterAdditional event) {
        MBPModelLoadingPlugin.collectModels(Minecraft.getInstance().getResourceManager()).forEach(id -> event.register(/^? if > 1.21.1 {^/ /^id^/ /^?} else {^/ new ModelIdentifier(id, "standalone") /^?}^/));
    }
    *///?} else if neoforge {
    /*@SubscribeEvent
    private static void load(ModelEvent.RegisterStandalone event) {
        MBPModelLoadingPlugin.collectModels(Minecraft.getInstance().getResourceManager()).forEach(id -> {
            StandaloneModelKey<@NotNull BlockStateModel> modelKey = new StandaloneModelKey<>(
                    /^? if < 1.21.6 {^/ /^id ^//^?} else {^/ id::toString /^?}^/
            );
            PredicateStore.predicates.put(id, modelKey);
            event.register(modelKey, /^? if < 1.21.6 {^/ /^StandaloneModelBaker.blockStateModel() ^//^?} else {^/ SimpleUnbakedStandaloneModel.blockStateModel(id)/^?}^/);
        });
    }
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
                            logError(id, e);
                        }
                    }
                }

            } catch (Exception e) {
                logError(id, e);
            }
        }
        return wantedModels;
    }

    private static void logError(Identifier id, Exception e) {
        PuzzlePredicates.LOGGER.error("Error found in file: {}", id);
        e.printStackTrace();
    }
}
