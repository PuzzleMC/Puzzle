package net.puzzlemc.predicates.mixin;

import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemEntityRenderer.class)
public class ItemEntityRendererMixin {

//    //? if < 1.21.4 {
//    @Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/world/entity/item/ItemEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
//    private void beforeRender(ItemEntity itemEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, CallbackInfo ci) {
//        PuzzlePredicates.currentEntity = itemEntity;
//    }
//    //?}
    //Lnet/minecraft/client/renderer/entity/ItemEntityRenderer;render(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V

}
