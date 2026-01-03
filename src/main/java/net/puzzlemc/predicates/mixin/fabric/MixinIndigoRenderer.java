package net.puzzlemc.predicates.mixin.fabric;

//? if fabric && >= 1.21.10 {
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.renderer.v1.render.BlockVertexConsumerProvider;
import net.fabricmc.fabric.impl.client.indigo.renderer.IndigoRenderer;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.MBPData;
import net.puzzlemc.predicates.accessor.MovingBlockRenderStateContext;
import net.puzzlemc.predicates.util.PredicateStore;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Optional;

/**
 * Ensures our (moving) block model overrides will also be visible with Fabric's indigo renderer
 */
@Mixin(IndigoRenderer.class)
public class MixinIndigoRenderer {
    @WrapMethod(method = "render(Lnet/minecraft/client/renderer/block/ModelBlockRenderer;Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/client/renderer/block/model/BlockStateModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/fabricmc/fabric/api/renderer/v1/render/BlockVertexConsumerProvider;ZJI)V")
    public void puzzle$renderCustomMovingBlockIndigo(ModelBlockRenderer modelRenderer, BlockAndTintGetter blockView, BlockStateModel model, BlockState state, BlockPos pos, PoseStack matrices, BlockVertexConsumerProvider vertexConsumers, boolean cull, long seed, int overlay, Operation<Void> original) {
        // Fabric passes the MovingBlockRenderState as the blockView parameter
        if (blockView instanceof MovingBlockRenderState renderState) {
            Optional<Identifier> identifier = MBPData.meetsPredicate(renderState.level, pos, state, MovingBlockRenderStateContext.of(renderState).puzzle$getContextId());

            if (identifier.isPresent())
                model = PredicateStore.reallyGetModel(identifier.get()).raw();
        }
        original.call(modelRenderer, blockView, model, state, pos, matrices, vertexConsumers, cull, seed, overlay);
    }
}
//?} else {
/*import eu.midnightdust.core.MidnightLib;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MidnightLib.class)
public class MixinIndigoRenderer {}
*///?}