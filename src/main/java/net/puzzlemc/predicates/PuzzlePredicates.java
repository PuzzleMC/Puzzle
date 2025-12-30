package net.puzzlemc.predicates;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
//import net.fabricmc.fabric.impl.client.model.loading.ModelLoadingPluginManager;
//import net.fabricmc.fabric.impl.client.model.loading.ModelLoadingPluginManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;

import net.puzzlemc.predicates.data.logic.When;
import net.puzzlemc.predicates.util.PredicateStore;
import net.puzzlemc.predicates.util.RegistryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

//? if fabric {
import net.fabricmc.fabric.impl.client.model.loading.ModelLoadingPluginManager;
//?} else if neoforge {
/*import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
//? if < 1.21.6 {
/^import net.neoforged.neoforge.client.model.standalone.StandaloneModelBaker;
^///?} else {
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
//?}

import static net.puzzlemc.core.PuzzleCore.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT /^? if <= 1.21.5 {^/ /^, bus = EventBusSubscriber.Bus.MOD ^//^?}^/)
*///?}
public class PuzzlePredicates {
    public static final Logger LOGGER = LoggerFactory.getLogger("mbp");
    public static Entity currentEntity;

    public static void init() {
        //? fabric
        ModelLoadingPluginManager.registerPlugin(new MBPModelLoadingPlugin.ModelIdLoader(), new MBPModelLoadingPlugin());
    }
    //? neoforge && < 1.21.5 {
//    @SubscribeEvent
//    private static void load(ModelEvent.RegisterAdditional event) {
//        //MBPModelLoadingPlugin.collectModels(Minecraft.getInstance().getResourceManager()).forEach(id -> event.register(new ModelIdentifier(id, "standalone")));
//    }
    //?} else if neoforge {
    /*@SubscribeEvent
    private static void load(ModelEvent.RegisterStandalone event) {
        MBPModelLoadingPlugin.collectModels(Minecraft.getInstance().getResourceManager()).forEach(id -> {
            StandaloneModelKey<BlockStateModel> modelKey = new StandaloneModelKey<>(
                    /^? if < 1.21.6 {^/ /^id ^//^?} else {^/ id::toString /^?}^/
            );
            PredicateStore.predicates.put(id, modelKey);
            event.register(modelKey, /^? if < 1.21.6 {^/ /^StandaloneModelBaker.blockStateModel() ^//^?} else {^/ SimpleUnbakedStandaloneModel.blockStateModel(id)/^?}^/);
        });
    }
    *///?}

    public static class ReloadListener implements ResourceManagerReloadListener {
        public static final ReloadListener INSTANCE = new ReloadListener();

        @Override
        public void onResourceManagerReload(ResourceManager manager) {
            MBPData.PREDICATES.clear();

            Map<Identifier, Resource> map = manager.listResources("mbp", id -> id.getPath().endsWith(".json"));
            for (Identifier id : map.keySet()) {

                try {
                    Identifier blockTarget = Identifier.tryParse(id.toString().substring(0,id.toString().length()-5).replace("mbp/", ""));
                    JsonObject asset = JsonParser.parseReader(map.get(id).openAsReader()).getAsJsonObject();

                    Optional<Block> block = RegistryUtils.getBlock(blockTarget);

                    if (block.isPresent()) {
                        JsonArray overrides = asset.getAsJsonArray("overrides");
                        List<When> whenList = new ArrayList<>();
                        for(JsonElement overrideEntry : overrides) {
                            When when = When.parse(overrideEntry);
                            whenList.add(when);
                        }
                        MBPData.PREDICATES.put(block.get(), Collections.unmodifiableList(whenList));
                    } else {
                        PuzzlePredicates.LOGGER.error("Block entry not found in file %s: %s ".formatted(id, blockTarget));
                    }

                } catch (Exception e) {
                    PuzzlePredicates.LOGGER.error("Error in file: %s".formatted(id));
                    e.printStackTrace();
                }
            }
        }
    }

}
