package net.puzzlemc.predicates;

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

public class MBPData {
    public static final HashMap<Block, List<When>> PREDICATES = new HashMap<>();

    public static Optional<Identifier> meetsPredicate(BlockGetter world, BlockPos pos, BlockState state, Identifier renderContext) {
        if (PREDICATES.containsKey(state.getBlock())) {
            for (When when : PREDICATES.get(state.getBlock())) {
                if (when.meetsCondition(world, pos, state, renderContext)) {
                    long seed = Mth.getSeed(pos);

                    return Optional.of(when.getModel(seed));
                }
            }
        }

        return Optional.empty();
    }
}
