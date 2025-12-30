package net.puzzlemc.predicates.data.conditions;

import com.google.gson.JsonElement;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.data.BlockModelPredicate;

public class IsContext extends BlockModelPredicate {

    final Identifier expectedContext;

    public IsContext(Identifier expectedContext) {
        this.expectedContext = expectedContext;
    }

    @Override
    public boolean meetsCondition(BlockGetter world, BlockPos pos, BlockState state, Identifier renderContext) {
        return expectedContext.equals(renderContext);
    }

    public static IsContext parse(JsonElement arg) {
        return new IsContext(Identifier.tryParse(arg.getAsString()));
    }

}
