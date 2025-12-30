package net.puzzlemc.predicates.accessor;

import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.world.entity.Entity;

public interface BlockRenderManagerAccess {
    void moreBlockPredicates$setContextEntity(Entity entity);

    static BlockRenderManagerAccess of(BlockRenderDispatcher manager) {
        return (BlockRenderManagerAccess) manager;
    }
}
