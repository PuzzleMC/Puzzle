package net.puzzlemc.predicates.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.entity.DisplayRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.common.ContextIDs;
import net.puzzlemc.predicates.util.ConditionCheck;
import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if < 1.21.4 {
/*import net.minecraft.world.entity.Display;
*///?} else {
import net.minecraft.client.renderer.entity.state.BlockDisplayEntityRenderState;
import net.minecraft.core.BlockPos;
//?}

//? if >= 1.21.10 {
import net.minecraft.client.renderer.SubmitNodeCollector;

import java.util.Optional;
//?} else {
//import org.spongepowered.asm.mixin.Shadow;
//?}

@Mixin(DisplayRenderer.BlockDisplayRenderer.class)
public class BlockDisplayEntityRendererMixin {
    //? if < 1.21.10
    /*@Shadow @Final private BlockRenderDispatcher blockRenderer;*/

    //? if < 1.21.4 {
    /*@Inject(method = "renderInner(Lnet/minecraft/world/entity/Display$BlockDisplay;Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IF)V", at = @At("HEAD"))
    public void getContext(Display.BlockDisplay blockDisplay, Display.BlockDisplay.BlockRenderState blockRenderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, float f, CallbackInfo ci) {
        BlockRenderManagerAccess.of(blockRenderer).moreBlockPredicates$setContextPos(blockDisplay.getOnPos());
    }
    *///?} else if < 1.21.10 {
    /*@Inject(method = "renderInner(Lnet/minecraft/client/renderer/entity/state/BlockDisplayEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IF)V", at = @At("HEAD"))
    public void getContext(BlockDisplayEntityRenderState rs, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, float f, CallbackInfo ci) {
        BlockRenderManagerAccess.of(blockRenderer).moreBlockPredicates$setContextPos(BlockPos.containing(rs.x, rs.y, rs.z));
    }
    *///?} else {
    @Inject(method = "submitInner(Lnet/minecraft/client/renderer/entity/state/BlockDisplayEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IF)V", at = @At("HEAD"), cancellable = true)
    public void puzzle$checkOverride(BlockDisplayEntityRenderState rs, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, float f, CallbackInfo ci) {
        if (rs.blockRenderState == null) return;

        Optional<BlockStateModel> model = ConditionCheck.meetsPredicate(Minecraft.getInstance().level, BlockPos.containing(rs.x, rs.y, rs.z), rs.blockRenderState.blockState(), ContextIDs.ENTITY);
        if (model.isPresent()) {
            BlockState state = rs.blockRenderState.blockState();
            float r = 1.f; //TODO: Get correct biome colors for this block
            float g = 1.f;
            float b = 1.f;
            submitNodeCollector.submitBlockModel(poseStack, ItemBlockRenderTypes.getRenderType(state), model.get(), r, g, b, light, OverlayTexture.NO_OVERLAY, rs.outlineColor);
            ci.cancel();
        }
    }
    //?}
}
