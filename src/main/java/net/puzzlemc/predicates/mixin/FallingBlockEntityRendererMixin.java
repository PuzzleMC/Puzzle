package net.puzzlemc.predicates.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.puzzlemc.predicates.accessor.MovingBlockRenderStateContext;
import net.puzzlemc.predicates.common.ContextIDs;
import net.puzzlemc.predicates.util.ModelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
//? if < 1.21.4 {
/*import net.minecraft.world.entity.item.FallingBlockEntity;
*///?} else if < 1.21.10 {
/*import net.minecraft.client.renderer.entity.state.FallingBlockRenderState;
*///?}

//? if < 1.21.5 {
/*import net.minecraft.client.renderer.block.model.BlockStateModel;
*///?} else if < 1.21.10 {
/*import net.minecraft.client.renderer.block.model.BlockStateModel;
*///?}

//? if >= 1.21.10 {
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
//?} else {
/*import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.MBPData;
import java.util.Optional;
*///?}

@Mixin(value = FallingBlockRenderer.class, priority = 1010)
public class FallingBlockEntityRendererMixin {
    //? if < 1.21.10 {
    /*@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/BlockRenderDispatcher;getBlockModel(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/client/renderer/block/model/BlockStateModel;"),
            method =
            /^? if <= 1.21.4 {^/
            /^"render(Lnet/minecraft/world/entity/item/FallingBlockEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
            ^//^?} else {^/
            "render(Lnet/minecraft/client/renderer/entity/state/FallingBlockRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
            /^?}^/
    )
    public BlockStateModel render(BlockRenderDispatcher instance, BlockState state, Operation<BlockStateModel> original, /^? if < 1.21.4 {^/ /^@Local(argsOnly = true) FallingBlockEntity fallingBlock ^//^?} else {^/ @Local(argsOnly = true) FallingBlockRenderState fallingBlock  /^?}^/) {
        Optional<ModelData> override = MBPData.meetsPredicate(
                //? if < 1.21.4 {
                /^fallingBlock.level(), fallingBlock.blockPosition()
                ^///?} else {
                fallingBlock.level, fallingBlock.blockPos
                //?}
                , state, ContextIDs.FALLING_BLOCK);

        return override.map(ov -> ov.getOverrideModel().raw())
                .orElseGet(() -> original.call(instance, state));
    }
    *///?} else {
    @WrapOperation(at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitMovingBlock(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/block/MovingBlockRenderState;)V"),
            method = "submit(Lnet/minecraft/client/renderer/entity/state/FallingBlockRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V")
    private void captureContext(SubmitNodeCollector instance, PoseStack poseStack, MovingBlockRenderState renderState, Operation<Void> original) {
        MovingBlockRenderStateContext.of(renderState).puzzle$setContextId(ContextIDs.FALLING_BLOCK);
        original.call(instance, poseStack, renderState);
    }
    //?}
}
