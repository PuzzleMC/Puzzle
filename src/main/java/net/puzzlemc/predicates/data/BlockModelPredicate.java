package net.puzzlemc.predicates.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.puzzlemc.predicates.PuzzlePredicates;
import net.puzzlemc.predicates.data.conditions.*;
import net.puzzlemc.predicates.data.logic.And;
import net.puzzlemc.predicates.data.logic.Not;
import net.puzzlemc.predicates.data.logic.Or;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class BlockModelPredicate implements WorldViewCondition {
    private static final HashMap<String, Function<JsonElement, BlockModelPredicate>> HANDLERS = new HashMap<>() {{
        // Logical operators
        put("or", Or::parse);
        put("and", And::parse);
        put("not", Not::parse);

        // Actual conditions
        put("adjacent_block", AdjacentBlock::parse);
        put("coordinate_range", CoordinateRange::parse);
        put("biome", InBiome::parse);
        put("state", IsBlockState::parse);
        put("light_range", LightRange::parse);
        put("is_context", IsContext::parse);
    }};

    public static ArrayList<BlockModelPredicate> parseFromJson(JsonElement element) {
        ArrayList<JsonObject> objects = new ArrayList<>();
        if (element.isJsonArray()) {
            JsonArray arr = element.getAsJsonArray();
            for (JsonElement obj : arr) {
                objects.add(obj.getAsJsonObject());
            }
        } else {
            objects.add(element.getAsJsonObject());
        }

        ArrayList<BlockModelPredicate> predicates = new ArrayList<>();
        for(JsonObject curObject : objects) {
            for (Map.Entry<String, JsonElement> entries : curObject.entrySet()) {
                if (HANDLERS.containsKey(entries.getKey())) {
                    try {
                        predicates.add(HANDLERS.get(entries.getKey()).apply(entries.getValue()));
                    } catch (JsonParseException e) {
                        PuzzlePredicates.LOGGER.warn("Failed to load predicate \"{}\"! Reason: {}", entries.getKey(), e.getMessage());
                    }
                } else {
                    PuzzlePredicates.LOGGER.warn("Unhandled predicate \"{}\"!", entries.getKey());
                }
            }
        }

        return predicates;
    }



}
