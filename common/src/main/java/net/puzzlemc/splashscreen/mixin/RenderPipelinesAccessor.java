package net.puzzlemc.splashscreen.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gl.RenderPipelines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderPipelines.class)
public interface RenderPipelinesAccessor {
    @Accessor
    static RenderPipeline.Snippet getPOSITION_TEX_COLOR_SNIPPET() {
        return null;
    }
}
