package net.puzzlemc.predicates.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.blockentity.PistonHeadRenderer;
import net.puzzlemc.predicates.accessor.MovingBlockRenderStateContext;
import net.puzzlemc.predicates.common.ContextIDs;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


//? if < 1.21.5 {
//import net.minecraft.client.resources.model.BakedModel;
//?} else if < 1.21.10 {
// import net.minecraft.client.renderer.block.model.BlockStateModel;
//?}

//? if < 1.21.10 {
//?} else {
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.state.PistonHeadRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.MBPData;
import net.puzzlemc.predicates.util.PredicateStore;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//?}

@Mixin(PistonHeadRenderer.class)
public class PistonBlockEntityRendererMixin {
    //? if < 1.21.10 {
//    @Unique private BlockPos tempBlockPos;
//    @Unique private Level tempWorld;
//
//    @Inject(at = @At("HEAD"), method = "renderBlock")
//    public void renderModel(BlockPos pos, BlockState state, PoseStack matrices, MultiBufferSource vertexConsumers, Level world, boolean cull, int overlay, CallbackInfo ci) {
//        this.tempBlockPos = pos;
//        this.tempWorld = world;
//    }
//
//    @Redirect(at = @At(value = "INVOKE", target =
//            /*? if < 1.21.5 {*/
//            /*"Lnet/minecraft/client/renderer/block/BlockRenderDispatcher;getBlockModel(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/client/resources/model/BakedModel;"*/
//            /*?} else {*/
//            "Lnet/minecraft/client/renderer/block/BlockRenderDispatcher;getBlockModel(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/client/renderer/block/model/BlockStateModel;"
//            /*?}*/
//    ), method = "renderBlock")
//    public /*? if < 1.21.5 {*/ /*BakedModel*/ /*?} else {*/ BlockStateModel /*?}*/ renderModel(BlockRenderDispatcher instance, BlockState state) {
//        Optional<Identifier> identifier = MBPData.meetsPredicate(tempWorld, tempBlockPos, state, ContextIDs.PISTON_PUSHING);
//
//        Minecraft client = Minecraft.getInstance();
//        return identifier.map(resourceLocation -> PredicateStore.reallyGetModel(resourceLocation).raw())
//                .orElseGet(() -> client.getBlockRenderer().getBlockModelShaper().getBlockModel(state));
//    }
    //?} else {
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitMovingBlock(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/block/MovingBlockRenderState;)V"), method = "submit(Lnet/minecraft/client/renderer/blockentity/state/PistonHeadRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V")
    private void captureContext(SubmitNodeCollector instance, PoseStack poseStack, MovingBlockRenderState renderState, Operation<Void> original) {
        MovingBlockRenderStateContext.of(renderState).puzzle$setContextId(ContextIDs.PISTON_PUSHING);
        original.call(instance, poseStack, renderState);
    }
    //?}
}
