package net.puzzlemc.predicates.accessor;

import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.BlockPos;

public interface BlockRenderManagerAccess {
    void moreBlockPredicates$setContextPos(BlockPos pos);

    static BlockRenderManagerAccess of(BlockRenderDispatcher manager) {
        return (BlockRenderManagerAccess) manager;
    }
}
