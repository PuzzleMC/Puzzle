package net.puzzlemc.predicates.data.logic;

import com.google.gson.JsonElement;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.data.BlockModelPredicate;

public class Not extends BlockModelPredicate {

    final And condition;

    public Not(And condition) {
        this.condition = condition;
    }

    @Override
    public boolean meetsCondition(BlockGetter world, BlockPos pos, BlockState state, Identifier renderContext) {
        return !condition.meetsCondition(world, pos, state, renderContext);
    }

    public static Not parse(JsonElement arg) {
        return new Not(And.parse(arg));
    }
}
