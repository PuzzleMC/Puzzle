package net.puzzlemc.predicates.util;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.data.logic.When;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public abstract class ConditionCheck {
    public static final HashMap<Block, List<When>> PREDICATES = new HashMap<>();

    /**
     * Tests whether the block meets the predicate by checking all relevant predicates.
     * @param world a worldview instance
     * @param pos the block position
     * @param state the block state
     * @param renderContext the current render context id
     * @return Optional instance containing either the first matching model or nothing at all
     */
    public static Optional<BlockStateModel> meetsPredicate(BlockGetter world, BlockPos pos, BlockState state, Identifier renderContext) {
        if (PREDICATES.containsKey(state.getBlock())) {
            for (When when : PREDICATES.get(state.getBlock())) {
                if (when.meetsCondition(world, pos, state, renderContext)) {
                    @SuppressWarnings("deprecation")
                    long seed = Mth.getSeed(pos);

                    return Optional.of(when.getModel(seed).getOverrideModel());
                }
            }
        }

        return Optional.empty();
    }
}
