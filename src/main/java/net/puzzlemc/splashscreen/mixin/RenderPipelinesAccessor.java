package net.puzzlemc.splashscreen.mixin;

//? if > 1.21.1 {
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.RenderPipelines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderPipelines.class)
public interface RenderPipelinesAccessor {
    @Accessor
    static RenderPipeline.Snippet getGUI_TEXTURED_SNIPPET() {
        return null;
    }
}
//?} else {

/*import eu.midnightdust.core.MidnightLib;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MidnightLib.class)
public interface RenderPipelinesAccessor {} // TODO: Properly disable this mixin when on 1.21.1 and lower
*///?}