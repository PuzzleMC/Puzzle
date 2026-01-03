package net.puzzlemc.predicates.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.DisplayRenderer;
import net.puzzlemc.predicates.accessor.BlockRenderManagerAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if < 1.21.4 {
//import net.minecraft.world.entity.Display;
//?} else {
import net.minecraft.client.renderer.entity.state.BlockDisplayEntityRenderState;
import net.minecraft.core.BlockPos;
//?}

//? if >= 1.21.10 {
import net.minecraft.client.renderer.SubmitNodeCollector;
//?}

@Mixin(DisplayRenderer.BlockDisplayRenderer.class)
public class BlockDisplayEntityRendererMixin {
    //? if < 1.21.10
    /*@Shadow @Final private BlockRenderDispatcher blockRenderer;*/

    //? if < 1.21.4 {
//    @Inject(method = "renderInner(Lnet/minecraft/world/entity/Display$BlockDisplay;Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IF)V", at = @At("HEAD"))
//    public void getContext(Display.BlockDisplay blockDisplay, Display.BlockDisplay.BlockRenderState blockRenderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, float f, CallbackInfo ci) {
//        BlockRenderManagerAccess.of(blockRenderer).moreBlockPredicates$setContextPos(blockDisplay.getOnPos());
//    }
    //?} else if < 1.21.10 {
    /*@Inject(method = "renderInner(Lnet/minecraft/client/renderer/entity/state/BlockDisplayEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IF)V", at = @At("HEAD"))
    public void getContext(BlockDisplayEntityRenderState rs, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, float f, CallbackInfo ci) {
        BlockRenderManagerAccess.of(blockRenderer).moreBlockPredicates$setContextPos(BlockPos.containing(rs.x, rs.y, rs.z));
    }
    *///?} else {
    @Inject(method = "submitInner(Lnet/minecraft/client/renderer/entity/state/BlockDisplayEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IF)V", at = @At("HEAD"))
    public void getContext(BlockDisplayEntityRenderState rs, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, float f, CallbackInfo ci) {
        // TODO drawing of block displays is now handled asynchronously, making it a terrible idea to cache the position here
    }
    //?}
}
