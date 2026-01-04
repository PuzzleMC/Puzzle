package net.puzzlemc.predicates.data.logic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.data.BlockModelPredicate;
import net.puzzlemc.predicates.data.WorldViewCondition;
import net.puzzlemc.predicates.util.ModelData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class When implements WorldViewCondition {

    final And conditions;
    private final List<ModelData> applyModelList;

    public When(And conditions, List<ModelData> applyModelList) {
        this.conditions = conditions;
        this.applyModelList = Collections.unmodifiableList(applyModelList);
    }

    public ModelData getModel(long seed) {
        return applyModelList.get((int) (Math.abs(seed) % applyModelList.size()));
    }

    public List<ModelData> getModels() {
        return applyModelList;
    }

    public static When parse(JsonElement arg) {
        JsonObject object = arg.getAsJsonObject();
        List<BlockModelPredicate> conditions = BlockModelPredicate.parseFromJson(object.get("when"));

        List<ModelData> applyModelList;
        JsonElement apply = object.get("apply");
        if (apply.isJsonArray()) {
            applyModelList = new ArrayList<>();
            for (JsonElement entry : apply.getAsJsonArray()) {
                String applyId;
                int weight = 1;
                ModelData data;
                if (entry.isJsonObject()) {
                    JsonObject obj = entry.getAsJsonObject();
                    applyId = obj.get("model").getAsString();
                    if (obj.has("weight")) weight = obj.get("weight").getAsInt();
                    data = ModelData.parse(obj, applyId);
                } else {
                    applyId = entry.getAsString();
                    data = ModelData.basic(applyId);
                }

                for (int i = 0; i < weight; i++) {
                    applyModelList.add(data);
                }
            }
        } else {
            ModelData data = ModelData.basic(apply.getAsString());
            applyModelList = List.of(data);
        }

        return new When(new And(conditions), applyModelList);
    }

    @Override
    public boolean meetsCondition(BlockGetter world, BlockPos pos, BlockState state, Identifier renderContext) {
        return conditions.meetsCondition(world, pos, state, renderContext);
    }
}
