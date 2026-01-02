package net.puzzlemc.predicates.mixin;

import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.world.entity.item.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(FallingBlockRenderer.class)
public class FallingBlockEntityRendererMixin {

    @Unique private FallingBlockEntity fallingBlockEntity;

//    //? if < 1.21.4 {
//    @Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/world/entity/item/FallingBlockEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
//    public void render(FallingBlockEntity fallingBlockEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, CallbackInfo ci) {
//        this.fallingBlockEntity = fallingBlockEntity;
//    }
//    //?}

//    @Redirect(at = @At(value = "INVOKE",
//            target = "Lnet/minecraft/client/renderer/block/BlockRenderDispatcher;getBlockModel(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/client/resources/model/BakedModel;"),
//            //? if < 1.21.4 {
//            //method = "render(Lnet/minecraft/world/entity/item/FallingBlockEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
//            //?} else {
//            method = "render(Lnet/minecraft/client/renderer/entity/state/FallingBlockRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
//            //?}
//    public BakedModel render(BlockRenderDispatcher instance, BlockState state) {
//        Optional<Identifier> identifier = MBPData.meetsPredicate(fallingBlockEntity.level(), fallingBlockEntity.blockPosition(), state, ContextIDs.FALLING_BLOCK);
//
//        Minecraft client = Minecraft.getInstance();
//        if (identifier.isPresent()) {
//            BakedModelManagerAccess access = BakedModelManagerAccess.of(client.getModelManager());
//            return access.reallyGetModel(identifier.get());
//        } else {
//            return client.getBlockRenderer().getBlockModelShaper().getBlockModel(state);
//        }
//    }
}
