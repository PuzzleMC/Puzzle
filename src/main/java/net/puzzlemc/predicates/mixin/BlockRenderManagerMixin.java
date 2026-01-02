package net.puzzlemc.predicates.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;

import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.MBPData;
import net.puzzlemc.predicates.accessor.BlockRenderManagerAccess;
import net.puzzlemc.predicates.common.BlockRendering;
import net.puzzlemc.predicates.common.ContextIDs;
import net.puzzlemc.predicates.util.PredicateModel;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

//? neoforge {
/*//? if < 1.21.5
/^import net.neoforged.neoforge.client.model.data.ModelData;^/

//? if <= 1.21.5 {
/^import net.minecraft.client.renderer.RenderType;
^///?} else {
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
//?}
*///?}

//? if > 1.21.4 {
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
//?}

@Mixin(value = BlockRenderDispatcher.class, priority = 2000)
public class BlockRenderManagerMixin implements BlockRenderManagerAccess {

    @Shadow @Final private BlockColors blockColors;
    @Shadow
    @Final
    private BlockModelShaper blockModelShaper;
    @Shadow
    @Final
    private ModelBlockRenderer modelRenderer;
    @Unique @Nullable private Entity figura$contextEntity;

    //? if fabric && < 1.21.5 {
    /*@Inject(at = @At("HEAD"), method = "renderBatched", cancellable = true)
    public void renderBlock(BlockState state, BlockPos pos, BlockAndTintGetter world, PoseStack matrices, VertexConsumer vertexConsumer, boolean cull, RandomSource random, CallbackInfo ci) {
    *///?} else if fabric && >= 1.21.5 {
    @Inject(at = @At("HEAD"), method = "renderBatched", cancellable = true)
    public void renderBlock(BlockState state, BlockPos pos, BlockAndTintGetter world, PoseStack matrices, VertexConsumer vertexConsumer, boolean cull, List<BlockModelPart> list, CallbackInfo ci) {
    //?} else if neoforge && < 1.21.5 {
    /*@Inject(at = @At("HEAD"), method = "renderBatched(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/BlockAndTintGetter;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;Lnet/neoforged/neoforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V", cancellable = true)
    public void renderBlock(BlockState state, BlockPos pos, BlockAndTintGetter world, PoseStack matrices, VertexConsumer vertexConsumer, boolean cull, RandomSource random, ModelData modelData, RenderType renderType, CallbackInfo ci) {
    *///?} else if neoforge && >= 1.21.5 {
    /*@Inject(at = @At("HEAD"), method = "renderBatched(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/BlockAndTintGetter;Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/util/function/Function;ZLjava/util/List;)V", cancellable = true)
    public void renderBlock(BlockState state, BlockPos pos, BlockAndTintGetter world, PoseStack matrices, Function</^? if < 1.21.8 {^/ /^RenderType ^//^?} else {^/ ChunkSectionLayer /^?}^/, VertexConsumer> bufferLookup, boolean cull, List<BlockModelPart> list, CallbackInfo ci) {
    *///?}
        RenderShape blockRenderType = state.getRenderShape();
        if (blockRenderType == RenderShape.MODEL) {
            Optional<PredicateModel> newModel = BlockRendering.tryModelOverride(this.blockModelShaper, world, state, pos, ContextIDs.MISC);
            newModel.ifPresent(predicateModel -> {
                //? if > 1.21.4 {
                this.modelRenderer.tesselateBlock(world, predicateModel.raw().collectParts(RandomSource.create()), state, pos, matrices, /*? fabric {*/ vertexConsumer /*?} else {*//*bufferLookup *//*?}*/, cull, OverlayTexture.NO_OVERLAY);
                //?} else {
                /*this.modelRenderer.tesselateBlock(world, predicateModel.raw(), state, pos, matrices, vertexConsumer, cull, random, state.getSeed(pos), OverlayTexture.NO_OVERLAY
                        //? if neoforge
                        /^, modelData, renderType^/
                );
                *///?}
                ci.cancel();
            });
        }
    }

//    //? if fabric {
//    @Inject(at = @At("HEAD"), method = "renderSingleBlock", cancellable = true)
//    public void renderBlockAsEntity(BlockState state, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, CallbackInfo ci) {
//    //?} else {
//    /*@Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/renderer/block/BlockRenderDispatcher;renderSingleBlock(Lnet/minecraft/world/level/block/state/BlockState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/neoforged/neoforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V", cancellable = true)
//    public void renderBlockAsEntity(BlockState state, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, ModelData modelData, RenderType renderType, CallbackInfo ci) {
//    *///?}
//        if (state.getRenderShape() == RenderShape.MODEL) {
//            BlockPos pos = figura$contextEntity == null ? BlockPos.ZERO : figura$contextEntity.getOnPos();
//            figura$contextEntity = null;
//            Optional<Identifier> id = MBPData.meetsPredicate(Minecraft.getInstance().level, pos, state, ContextIDs.ENTITY);
//            if (id.isEmpty()) return;
//
//            BakedModel bakedModel = ((BakedModelManagerAccess) this.blockModelShaper.getModelManager()).reallyGetModel(id.get());
//            int i = this.blockColors.getColor(state, null, null, 0);
//            float f = (float) (i >> 16 & 0xFF) / 255.0F;
//            float g = (float) (i >> 8 & 0xFF) / 255.0F;
//            float h = (float) (i & 0xFF) / 255.0F;
//            this.modelRenderer
//                    .renderModel(
//                            matrices.last(),
//                            vertexConsumers.getBuffer(ItemBlockRenderTypes.getRenderType(state /*? if < 1.21.4 {*/ /*, false*/ /*?}*/)),
//                            state,
//                            bakedModel,
//                            f,
//                            g,
//                            h,
//                            light,
//                            overlay
//                            //? if neoforge
//                            /*, modelData, renderType*/
//                    );
//            ci.cancel();
//        }
//    }

    @Override
    public void moreBlockPredicates$setContextEntity(Entity entity) {
        figura$contextEntity = entity;
    }
}
