package net.puzzlemc.predicates.accessor;

import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.resources.Identifier;

public interface MovingBlockRenderStateContext {
    void puzzle$setContextId(Identifier id);
    Identifier puzzle$getContextId();

    static MovingBlockRenderStateContext of(MovingBlockRenderState state) {
        return (MovingBlockRenderStateContext) state;
    }
}
