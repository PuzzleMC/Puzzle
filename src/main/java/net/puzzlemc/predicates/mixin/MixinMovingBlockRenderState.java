package net.puzzlemc.predicates.mixin;

import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.resources.Identifier;
import net.puzzlemc.predicates.accessor.MovingBlockRenderStateContext;
import net.puzzlemc.predicates.common.ContextIDs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MovingBlockRenderState.class)
public class MixinMovingBlockRenderState implements MovingBlockRenderStateContext {
    @Unique Identifier puzzle$contextId;

    @Override
    public void puzzle$setContextId(Identifier id) {
        this.puzzle$contextId = id;
    }

    @Override
    public Identifier puzzle$getContextId() {
        return puzzle$contextId != null ? puzzle$contextId : ContextIDs.MISC;
    }
}
