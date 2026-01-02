package net.puzzlemc.predicates.mixin.fabric;

//? fabric {
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//? if > 1.21.4 {
import net.fabricmc.fabric.impl.client.indigo.renderer.render.AbstractTerrainRenderContext;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.BlockRenderInfo;

@Mixin(AbstractTerrainRenderContext.class)
public interface AbstractTerrainRenderContextAccessor {
    @Accessor
    BlockRenderInfo getBlockInfo();
}
//?} else {
/*import net.fabricmc.fabric.impl.client.indigo.renderer.render.ChunkRenderInfo;
import net.minecraft.world.level.BlockAndTintGetter;

@Mixin(ChunkRenderInfo.class)
public interface AbstractTerrainRenderContextAccessor {
    @Accessor
    BlockAndTintGetter getBlockView();
}
*///?}

//?} else {
/*import eu.midnightdust.core.MidnightLib;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MidnightLib.class)
public interface AbstractTerrainRenderContextAccessor {}
*///?}