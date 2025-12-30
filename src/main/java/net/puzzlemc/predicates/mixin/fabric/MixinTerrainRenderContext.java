package net.puzzlemc.predicates.mixin.fabric;

//? fabric {
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import eu.midnightdust.core.MidnightLib;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.TerrainRenderContext;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.common.BlockRendering;
import net.puzzlemc.predicates.common.ContextIDs;
import net.puzzlemc.predicates.util.PredicateModel;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Optional;

@Mixin(TerrainRenderContext.class)
public class MixinTerrainRenderContext { // Makes sure our blocks will also be visible with Fabric's indigo renderer
    @WrapMethod(method = "bufferModel(Lnet/minecraft/client/renderer/block/model/BlockStateModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V")
    private void redirect(BlockStateModel model, BlockState state, BlockPos pos, Operation<Void> original) {
        if (state.getRenderShape() == RenderShape.MODEL) {
            BlockAndTintGetter world = ((AbstractTerrainRenderContextAccessor)this).getBlockInfo().blockView;
            if (world == null) return;
            Optional<PredicateModel> newModel = BlockRendering.tryModelOverride(null, world, state, pos, ContextIDs.MISC);
            if (newModel.isPresent()) {
                original.call(newModel.get().raw(), state, pos);
                return;
            }
        }
        original.call(model, state, pos);
    }
}
//?} else {
/*import eu.midnightdust.core.MidnightLib;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MidnightLib.class)
public class MixinTerrainRenderContext {}
*///?}