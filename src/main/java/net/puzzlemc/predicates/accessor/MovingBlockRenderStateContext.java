package net.puzzlemc.predicates.accessor;

//? >= 1.21.10 {
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.resources.Identifier;
//?}

public interface MovingBlockRenderStateContext {
    //? >= 1.21.10 {
    void puzzle$setContextId(Identifier id);
    Identifier puzzle$getContextId();

    static MovingBlockRenderStateContext of(MovingBlockRenderState state) {
        return (MovingBlockRenderStateContext) state;
    }
    //?}
}
