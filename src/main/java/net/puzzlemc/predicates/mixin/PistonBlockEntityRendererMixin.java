package net.puzzlemc.predicates.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.blockentity.PistonHeadRenderer;

import net.puzzlemc.predicates.accessor.MovingBlockRenderStateContext;
import net.puzzlemc.predicates.common.ContextIDs;

import net.puzzlemc.predicates.util.ModelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

//? if > 1.21.5 {
import net.minecraft.client.renderer.entity.state.FallingBlockRenderState;
//?}


//? if < 1.21.5 {
/*import net.minecraft.client.renderer.block.model.BlockStateModel;
*///?} else if < 1.21.10 {
 /*import net.minecraft.client.renderer.block.model.BlockStateModel;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
*///?}

//? if < 1.21.10 {
/*import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.util.ConditionCheck;

import java.util.Optional;
*///?} else {
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
//?}

@Mixin(value = PistonHeadRenderer.class, priority = 1010)
public class PistonBlockEntityRendererMixin {
    //? if < 1.21.10 {
    /*@WrapOperation(at = @At(value = "INVOKE", target =
            /^? if < 1.21.5 {^/
            /^"Lnet/minecraft/client/renderer/block/BlockRenderDispatcher;getBlockModel(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/client/renderer/block/model/BlockStateModel;"
            ^//^?} else {^/
            "Lnet/minecraft/client/renderer/block/BlockRenderDispatcher;getBlockModel(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/client/renderer/block/model/BlockStateModel;"
            /^?}^/
    ), method = "renderBlock")
    public BlockStateModel render(BlockRenderDispatcher instance, BlockState state, Operation<BlockStateModel> original, @Local(argsOnly = true) BlockPos blockPos, @Local(argsOnly = true) Level level) {
        Optional<BlockStateModel> override = ConditionCheck.meetsPredicate(level, blockPos, state, ContextIDs.PISTON_PUSHING);

        return override.orElse(original.call(instance, state));
    }
    *///?} else {
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitMovingBlock(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/block/MovingBlockRenderState;)V"), method = "submit(Lnet/minecraft/client/renderer/blockentity/state/PistonHeadRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V")
    private void captureContext(SubmitNodeCollector instance, PoseStack poseStack, MovingBlockRenderState renderState, Operation<Void> original) {
        MovingBlockRenderStateContext.of(renderState).puzzle$setContextId(ContextIDs.PISTON_PUSHING);
        original.call(instance, poseStack, renderState);
    }
    //?}
}
