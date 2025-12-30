package net.puzzlemc.predicates.data.logic;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.data.BlockModelPredicate;

import java.util.ArrayList;
import java.util.List;

public class Or extends BlockModelPredicate {

    final List<And> conditions;

    public Or(List<And> conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean meetsCondition(BlockGetter world, BlockPos pos, BlockState state, ResourceLocation renderContext) {
        for (And action : conditions) {
            if (action.meetsCondition(world, pos, state, renderContext)) return true;
        }
        return false;
    }

    public static Or parse(JsonElement arg) {
        ArrayList<And> conditionsList = new ArrayList<>();
        JsonArray entryArray = arg.getAsJsonArray();
        for (JsonElement entry : entryArray) {
            conditionsList.add(And.parse(entry));
        }
        return new Or(ImmutableList.copyOf(conditionsList));
    }
}
