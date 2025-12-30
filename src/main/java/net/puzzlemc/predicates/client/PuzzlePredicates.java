package net.puzzlemc.predicates.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.impl.client.model.loading.ModelLoadingPluginManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.puzzlemc.predicates.MBPData;
import net.puzzlemc.predicates.data.logic.When;
import net.puzzlemc.predicates.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Environment(EnvType.CLIENT)
public class PuzzlePredicates {
    public static final Logger LOGGER = LoggerFactory.getLogger("mbp");
    public static Entity currentEntity;

    public static void init() {
        ModelLoadingPluginManager.registerPlugin(new MBPModelLoadingPlugin.ModelIdLoader(), new MBPModelLoadingPlugin());
    }

    public static class ReloadListener implements ResourceManagerReloadListener {
        public static final ReloadListener INSTANCE = new ReloadListener();

        @Override
        public void onResourceManagerReload(ResourceManager manager) {
            MBPData.PREDICATES.clear();

            Map<ResourceLocation, Resource> map = manager.listResources("mbp", id -> id.getPath().endsWith(".json"));
            for (ResourceLocation id : map.keySet()) {

                try {
                    ResourceLocation blockTarget = new ResourceLocation(id.toString().substring(0,id.toString().length()-5).replace("mbp/", ""));
                    JsonObject asset = JsonParser.parseReader(map.get(id).openAsReader()).getAsJsonObject();

                    Optional<Block> block = Utils.getBlock(blockTarget);

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
