package net.puzzlemc.predicates.data.logic;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.data.BlockModelPredicate;

import java.util.List;

public class And extends BlockModelPredicate {

    final List<BlockModelPredicate> predicates;

    public And(List<BlockModelPredicate> predicates) {
        this.predicates = predicates;
    }

    @Override
    public boolean meetsCondition(BlockGetter world, BlockPos pos, BlockState state, ResourceLocation renderContext) {
        for (BlockModelPredicate action : predicates) {
            if (!action.meetsCondition(world, pos, state, renderContext)) return false;
        }
        return true;
    }

    public static And parse(JsonElement arg) {
        return new And(ImmutableList.copyOf(BlockModelPredicate.parseFromJson(arg)));
    }
}
