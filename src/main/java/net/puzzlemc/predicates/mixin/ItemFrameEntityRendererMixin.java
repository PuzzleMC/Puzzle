package net.puzzlemc.predicates.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.puzzlemc.predicates.PuzzlePredicates;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if >= 1.21.4
import net.minecraft.client.renderer.entity.state.ItemFrameRenderState;

//? if >= 1.21.10 {
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.CameraRenderState;
//?}

@Mixin(ItemFrameRenderer.class)
public class ItemFrameEntityRendererMixin<T extends ItemFrame> {

    //? if < 1.21.4 {
    /*@Inject(method = "render(Lnet/minecraft/world/entity/decoration/ItemFrame;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))
    public void renderMixin(T itemFrameEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, CallbackInfo ci) {
        PuzzlePredicates.contextPos = itemFrameEntity.getOnPos();
    }
    *///?} else {
    //? if < 1.21.10 {
    /*@Inject(method = "render(Lnet/minecraft/client/renderer/entity/state/ItemFrameRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))
    public void renderMixin(ItemFrameRenderState rs, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
    *///?} else {
    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemFrameRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At("HEAD"))
    public void renderMixin(ItemFrameRenderState rs, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
    //?}
        PuzzlePredicates.contextPos = BlockPos.containing(rs.x, rs.y, rs.z);
    }
    //?}
}
