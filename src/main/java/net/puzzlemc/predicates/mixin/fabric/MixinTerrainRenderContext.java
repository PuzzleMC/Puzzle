package net.puzzlemc.predicates.mixin.fabric;

//? fabric {
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.TerrainRenderContext;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.util.ConditionCheck;
import net.puzzlemc.predicates.common.ContextIDs;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Optional;

//? if <= 1.21.4 {
/*import net.fabricmc.fabric.impl.client.indigo.renderer.render.ChunkRenderInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import com.mojang.blaze3d.vertex.PoseStack;
*///?}

/**
 * Ensures our block model overrides will also be visible with Fabric's indigo renderer
 */
@SuppressWarnings({"UnstableApiUsage"})
@Mixin(TerrainRenderContext.class)
public class MixinTerrainRenderContext {
    //? if > 1.21.4 {
    @WrapMethod(method = "bufferModel(Lnet/minecraft/client/renderer/block/model/BlockStateModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V")
    private void puzzle$renderCustomTerrainBlockIndigo(BlockStateModel model, BlockState state, BlockPos pos, Operation<Void> original) {
        if (state.getRenderShape() == RenderShape.MODEL) {
            BlockAndTintGetter world = ((AbstractTerrainRenderContextAccessor)this).getBlockInfo().blockView;
            Optional<BlockStateModel> newModel = ConditionCheck.meetsPredicate(world, pos, state, ContextIDs.CHUNK_MESH);
            if (newModel.isPresent()) {
                original.call(newModel.get(), state, pos);
                return;
            }
        }
        original.call(model, state, pos);
    }
    //?} else {
    /*@Shadow @Final private ChunkRenderInfo chunkInfo;

    @WrapMethod(method = "tessellateBlock")
    private void puzzle$renderCustomTerrainBlockIndigo(BlockState state, BlockPos pos, BlockStateModel model, PoseStack matrixStack, Operation<Void> original) {
        if (state.getRenderShape() == RenderShape.MODEL) {
            BlockAndTintGetter world = ((AbstractTerrainRenderContextAccessor) chunkInfo).getBlockView();
            if (world == null) return;
            Optional<BlockStateModel> newModel = ConditionCheck.meetsPredicate(world, pos, state, ContextIDs.CHUNK_MESH);
            if (newModel.isPresent()) {
                original.call(state, pos, newModel.get(), matrixStack);
                return;
            }
        }
        original.call(state, pos, model, matrixStack);
    }
    *///?}
}
//?} else {
/*import eu.midnightdust.core.MidnightLib;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MidnightLib.class)
public class MixinTerrainRenderContext {}
*///?}