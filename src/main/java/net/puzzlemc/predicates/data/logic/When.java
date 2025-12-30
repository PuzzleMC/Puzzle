package net.puzzlemc.predicates.data.logic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.data.BlockModelPredicate;
import net.puzzlemc.predicates.data.WorldViewCondition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class When implements WorldViewCondition {

    final And conditions;
    private final List<Identifier> applyModelList;

    public When(And conditions, List<Identifier> applyModelList) {
        this.conditions = conditions;
        this.applyModelList = Collections.unmodifiableList(applyModelList);
    }

    public Identifier getModel(long seed) {
        return applyModelList.get((int) (Math.abs(seed) % applyModelList.size()));
    }

    public List<Identifier> getModels() {
        return applyModelList;
    }

    public static When parse(JsonElement arg) {
        JsonObject object = arg.getAsJsonObject();
        List<BlockModelPredicate> conditions = BlockModelPredicate.parseFromJson(object.get("when"));

        List<Identifier> applyModelList;
        JsonElement apply = object.get("apply");
        if (apply.isJsonArray()) {
            applyModelList = new ArrayList<>();
            for (JsonElement entry : apply.getAsJsonArray()) {
                String applyId;
                int weight = 1;
                if (entry.isJsonObject()) {
                    JsonObject obj = entry.getAsJsonObject();
                    applyId = obj.get("model").getAsString();
                    if (obj.has("weight")) weight = obj.get("weight").getAsInt();
                } else {
                    applyId = entry.getAsString();
                }

                String[] id = applyId.split(":");
                Identifier currentModelID = Identifier.fromNamespaceAndPath(id[0], "block/" + id[1]);
                for (int i = 0; i < weight; i++) {
                    applyModelList.add(currentModelID);
                }
            }
        } else {
            String[] id = apply.getAsString().split(":");
            applyModelList = List.of(Identifier.fromNamespaceAndPath(id[0], "block/" + id[1]));
        }

        return new When(new And(conditions), applyModelList);
    }

    @Override
    public boolean meetsCondition(BlockGetter world, BlockPos pos, BlockState state, Identifier renderContext) {
        return conditions.meetsCondition(world, pos, state, renderContext);
    }
}
