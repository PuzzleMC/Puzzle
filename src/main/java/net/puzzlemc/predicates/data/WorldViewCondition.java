package net.puzzlemc.predicates.data;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface WorldViewCondition {
    boolean meetsCondition(BlockGetter world, BlockPos pos, BlockState state, ResourceLocation renderContext);
}
