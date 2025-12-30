package net.puzzlemc.predicates.mixin.fabric;

//? fabric {
import net.fabricmc.fabric.impl.client.indigo.renderer.render.AbstractTerrainRenderContext;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.BlockRenderInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractTerrainRenderContext.class)
public interface AbstractTerrainRenderContextAccessor {
    @Accessor
    BlockRenderInfo getBlockInfo();
}
//?} else {
/*import eu.midnightdust.core.MidnightLib;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MidnightLib.class)
public interface AbstractTerrainRenderContextAccessor {}
*///?}